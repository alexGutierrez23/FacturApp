package com.example.usuario.testingbd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import Constants.Constants;
import appImplementations.BluetoothImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.Bluetooth;
import appInterfaces.HojaRutaMethods;
import fragment.HojaRutaFragment;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.Stop;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;


public class HojaRuta extends AppCompatActivity {

    private ViewPager viewPagerHojaRuta;

    private String contratoId = null;
    //private String arrendatarioId = null;
    //private boolean arrendatarioCreado;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket mmSocket = null;
    private BluetoothDevice mmDevice = null;

    private OutputStream mmOutputStream = null;
    private InputStream mmInputStream = null;
    private Thread workerThread = null;

    private ArrayList<BluetoothDevice> devicesFound = null;

    private byte[] readBuffer = null;
    private int readBufferPosition;
    private int counter;
    private volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoja_ruta);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle("HOJAS DE RUTA");
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        contratoId = formulario.getString("contratoId");
        //Esta var bool sera true si redireccionamos a creacion hoja ruta desde crear arrendatario.
        // Sera false si llamamos a crear hoja ruta con fab add button.
       /* arrendatarioCreado = formulario.getBoolean("arrendatarioCreado");
        if(arrendatarioCreado){
            arrendatarioId = formulario.getString("arrendatarioId");
        }
*/
        viewPagerHojaRuta = (ViewPager) findViewById(R.id.viewpagerHojaRuta);
        PagerAdapter pagerAdapter = new HojaRutaPagerAdapter(getSupportFragmentManager(), HojaRuta.this, contratoId);
        viewPagerHojaRuta.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
            /*    Intent i = new Intent(this, MenuPrincipal.class);
                startActivity(i);
                MenuPrincipal mp = new MenuPrincipal();
                MenuItem menuItem = (MenuItem) findViewById(R.id.nav_hojaRuta); //Seguir aqui
                mp.onNavigationItemSelected(menuItem);
                finish();
                return true;
            */
            NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public class HojaRutaPagerAdapter extends FragmentPagerAdapter {
        Context context;
        String contratoId = null;
        //String arrendatarioId = null;

        public HojaRutaPagerAdapter(FragmentManager fragmentManager, Context context, String contratoId){
            super(fragmentManager);
            this.context = context;
            this.contratoId = contratoId;
            //this.arrendatarioId = arrendatarioId;
        }

        @Override
        public int getCount(){
            return 1;
        }  //truco para no obtener null

        @Override
        public Fragment getItem(int position){
            Bundle bundle = new Bundle();
            bundle.putString("contratoId", contratoId);
         /*   bundle.putBoolean("arrendatarioCreado", arrendatarioCreado);
            if(arrendatarioCreado){
                bundle.putString("arrendatarioId", arrendatarioId);
            }*/
            //set Fragmentclass Arguments
            HojaRutaFragment hrf = new HojaRutaFragment();
            hrf.setArguments(bundle);
            return hrf;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "HOJAS DE RUTA";
        }
    }

    public ArrayList<sqlite.model.HojaRuta> getHojasRutaList(String contratoId) {
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getHojasRuta(contratoId, this);
    }

    public void imprimirHojaRuta(String hjrId){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        sqlite.model.HojaRuta hr = hrm.getHojaRuta(hjrId, this);
        Arrendatario ard = hrm.getArrendatario(hr.getHjr_ard_id(), this);
        Contrato contrato = hrm.getContrato(hr.getHjr_con_id(), this);
        Trip trip = hrm.getTrip(hr.getHjr_trp_id(), this);
        Taxi_driver_account tda = hrm.getTaxiDriverAccount(this);
        ArrayList<Stop> stops = hrm.getStopsByHjrId(hjrId, this);

        if(abrirConexion(tda)){
            enviarDatos(tda, contrato, hr, trip, stops, ard);
        }
        else{
            Toast.makeText(this, getString(R.string.notConnectedToPrinter), Toast.LENGTH_SHORT).show();
        }
    }

    private void esperar (int segundos) {
        try {
            Thread.sleep (segundos*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean abrirConexion(Taxi_driver_account tda){
        boolean conexionAbierta = false;
        try {
            if(findBT(tda)){
                //esperar(1);
                if(openBT()){
                    esperar(1);
                    conexionAbierta = true;
                }
            }
        } catch (IOException ex) {
        }
        return conexionAbierta;
    }

    private void enviarDatos(Taxi_driver_account tda, Contrato contrato, sqlite.model.HojaRuta hr,
                             Trip trip, ArrayList<Stop> stops, Arrendatario ard) {
        try {
            sendData(tda, contrato, hr, trip, stops, ard);
            esperar(1);
            closeBT();
        } catch (IOException e) {
        }
    }

    // This will find a bluetooth printer device
    private boolean findBT(Taxi_driver_account tda) {
        boolean found = false;
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                this.onRestart();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 1);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                    .getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if ((device.getName().equals(tda.getTda_printerDevice()))) {
                        //if ((device.getName().equals("BlueTooth Printer"))) {   //Juan Carlos
                        //if (device.getName().equals("XXZN14-49-0207")) {   //Enrique
                        //if (device.getName().equals("Add Rafael Printer ñ")) {//Falta la de Rafael
                        mmDevice = device;
                        //sol = true;
                        break;
                    }
                   /* else{
                        this.onStop();
                    }*/
                }
            }
            if(mmDevice == null){
                Toast.makeText(this,"Encienda la impresora, por favor", Toast.LENGTH_SHORT).show();
                found = false;
                this.onStop();
            }
            else{
                Toast.makeText(this, "Dispositivo BT encontrado", Toast.LENGTH_SHORT).show();
                found = true;
                //pgbImprimir.setProgress(25);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    // Tries to open a connection to the bluetooth printer device
    private boolean openBT() throws IOException {
        boolean conectado = false;
        try {
            // Standard SerialPortService ID
            //final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            try{
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            }
            catch(Exception e){
                Log.e("", "Error createSocketServiceRecord");
            }
            try{
                mmSocket.connect();
                Log.e("","Connected");
            }
            catch(Exception e){
                Log.e("",e.getMessage());
                try {
                    Log.e("","Reintentando...");

                    mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                    mmSocket.connect();

                    Log.e("","Conectado");
                }
                catch (Exception e2) {
                    Log.e("", "No se pudo establecer conexion bluetooth");
                }
            }

            if(mmSocket.isConnected()){
                //pgbImprimir.setProgress(50);
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();
                Toast.makeText(this, getString(R.string.connected) + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                conectado = true;
                beginListenForData();
            }
            else{
                Toast.makeText(this, getString(R.string.turnOnPrinter), Toast.LENGTH_SHORT).show();
                conectado = false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conectado;
    }

    // After opening a connection to bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    public void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "UTF-8");
                                        //readBufferPosition = 3;
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {

                                                //tvName.setText(data);
                                                //myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * This will send data to be printed by the bluetooth printer
     */
    private void sendData(Taxi_driver_account tda, Contrato contrato, sqlite.model.HojaRuta hr,
                          Trip trip, ArrayList<Stop> stops, Arrendatario ard) throws IOException {
        Constants c = null;
        Bluetooth bt = null;
        HojaRutaMethods hrm = null;
        try {
            c = new Constants();
            bt = new BluetoothImplementation();
            hrm = new HojaRutaMethodsImplementation();
            boolean isTdaArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());

            String titulo = "\n" + "       " + bt.reemplaza_Char("HOJA DE RUTA ADSCRITA \n AL CONTRATO FIRMADO EN MADRID \n");
            if(isTdaArrendador){
                titulo = titulo + hrm.getDateStringFormat(ard.getArd_fecha()) + ",\n N° " + ard.getArd_number();
            }
            else{
                titulo = titulo + hrm.getDateStringFormat(contrato.getCon_date()) + ",\n N° " + contrato.getCon_number();
            }
            titulo = bt.reemplaza_Char(titulo);
            titulo = titulo + "\n";

            String subtitulo = bt.reemplaza_Char("(Conforme Orden Fom /2799/2015, de 18 de Diciembre)");
            subtitulo = subtitulo + "\n";

            //String name = "  " + bt.reemplaza_Char(tda.getTda_name() + "\n");
            String transportista = bt.reemplaza_Char("TRANSPORTISTA: \n" + "D. " + tda.getTda_name() + "\n");
            transportista = bt.reemplaza_Char(transportista);
            String nif = bt.reemplaza_Char("N.I.F.: " + tda.getTda_nif());
            nif = bt.reemplaza_Char(nif);
            nif += "\n";
            String matricula = bt.reemplaza_Char("MATRICULA VEHICULO: \n" + contrato.getCon_matricula_vehiculo() + "\n");
            matricula = bt.reemplaza_Char(matricula);

            String arrendador = bt.reemplaza_Char("ARRENDADOR:\n" + contrato.getCon_arrendador()
                    + "\nCIF: " + contrato.getCon_nif_arrendador() + "\n");
            arrendador = bt.reemplaza_Char(arrendador);

            String arrendatario = bt.reemplaza_Char("ARRENDATARIO:\n" + ard.getArd_name()
                   + "\nNIF/CIF: " + ard.getArd_nifCif() + "\n");
            arrendatario = bt.reemplaza_Char(arrendatario);

            String fechaInicio = bt.reemplaza_Char("FECHA INICIO:" + hr.getHjr_date_inicio() + "\n");
            fechaInicio = bt.reemplaza_Char(fechaInicio);

            String fechaFin = bt.reemplaza_Char("FECHA FIN:" + hr.getHjr_date_fin() + "\n");
            fechaFin = bt.reemplaza_Char(fechaFin);

            String horaInicio = bt.reemplaza_Char("HORA INICIO:\n" + hr.getHjr_time_inicio() + "\n");
            horaInicio = bt.reemplaza_Char(horaInicio);

            String horaFin = bt.reemplaza_Char("HORA FIN:\n" + hr.getHjr_time_fin() + "\n");
            horaFin = bt.reemplaza_Char(horaFin);

            String lugarInicio = bt.reemplaza_Char("LUGAR INICIO SERVICIO:\n" + trip.getTrp_start_place() + "\n");
            lugarInicio = bt.reemplaza_Char(lugarInicio);

            String inicio = bt.reemplaza_Char("INICIO SERVICIO:\n" + trip.getTrp_start() + "\n");
            inicio = bt.reemplaza_Char(inicio);

            String fin = bt.reemplaza_Char("DESTINO SERVICIO:\n" + trip.getTrp_finish() + "\n");
            fin = bt.reemplaza_Char(fin);

            String pasajeros = bt.reemplaza_Char("PASAJEROS:\n" + hr.getHjr_pasajeros() + "\n");
            pasajeros = bt.reemplaza_Char(pasajeros);

            String observaciones = bt.reemplaza_Char("OBSERVACIONES:\n" + hr.getHjr_observaciones() + "\n");
            observaciones = bt.reemplaza_Char(observaciones);

            String hjrNum = null;
            if(isTdaArrendador) {
                hjrNum = bt.reemplaza_Char("Num Hoja Ruta:\n");
                hjrNum = hjrNum + hr.getHjr_number() + "\n";
                hjrNum = bt.reemplaza_Char(hjrNum);
            }

            String notaFinal = bt.reemplaza_Char("NOTA: EL SERVICIO DE TRANSPORTE SE REALIZA A JORNADA COMPLETA " +
                    "DIARIA PARA EL ARRENDATARIO, SALVO AUTORIZACION EXPRESA PARA REALIZAR OTROS DIFERENTES");
            notaFinal = bt.reemplaza_Char(notaFinal);

            Toast.makeText(this, "Imprimiendo Hoja Ruta" + hr.getHjr_alias(), Toast.LENGTH_SHORT).show();

            mmOutputStream.write(titulo.getBytes());
            ArrayList<String> a2 = bt.maximumTam(bt.reemplaza_Char(subtitulo));
            for(String st : a2){
                st = st + "\n";
                mmOutputStream.write(st.getBytes());
            }
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(transportista.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(nif.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(matricula.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(arrendador.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(arrendatario.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(fechaInicio.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(fechaFin.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(horaInicio.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(horaFin.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(lugarInicio.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(inicio.getBytes());
            mmOutputStream.write("\n".getBytes());
            for(Stop stop: stops){
                String parada = bt.reemplaza_Char("PARADA INTERMEDIA Y ESPERA:\n" + stop.getStp_place() + "\n");
                parada = bt.reemplaza_Char(parada);
                mmOutputStream.write(parada.getBytes());
                mmOutputStream.write("\n".getBytes());
            }
            mmOutputStream.write(fin.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(pasajeros.getBytes());
            mmOutputStream.write("\n".getBytes());
            mmOutputStream.write(observaciones.getBytes());
            mmOutputStream.write("\n".getBytes());

            ArrayList<String> a3 = bt.maximumTam(bt.reemplaza_Char(notaFinal));
            for(String st : a3){
                st = st + "\n";
                mmOutputStream.write(st.getBytes());
            }
            mmOutputStream.write(hjrNum.getBytes());

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
//            pgbImprimir.setProgress(100);
            //myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
