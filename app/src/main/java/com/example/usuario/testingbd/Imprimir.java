package com.example.usuario.testingbd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import Constants.Constants;
import appImplementations.BluetoothImplementation;
import appImplementations.CommonMethodsImplementation;
import appInterfaces.Bluetooth;
import appInterfaces.CommonMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.model.Trip;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TicketDML;
import sqlite.repo.TripDML;

public class Imprimir extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothSocket mmSocket = null;
    BluetoothDevice mmDevice = null;

    OutputStream mmOutputStream = null;
    InputStream mmInputStream = null;
    Thread workerThread = null;

    byte[] readBuffer = null;
    int readBufferPosition;
    volatile boolean stopWorker;

    private String tda_id = null;
    private String tckId = null;
    private String email = null;
    private Ticket tck = null;
    private Taxi_driver_account tda = null;
    private Address adr = null;
    private Trip trip = null;
    private String nombreCliente = null;
    private String nifCliente = null;

    private ProgressBar pgbImprimir = null;
    private Button enviarTicket = null;
    private Button imprimirTicket = null;
    private Button salirTickets = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprimir);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_imprimir);
        act.setIcon(R.mipmap.ic_launcher);
        act.setLogo(R.mipmap.ic_launcher);

        pgbImprimir = (ProgressBar) findViewById(R.id.progressBarImprimir);
        pgbImprimir.setVisibility(View.GONE);
        //pgbImprimir.setEnabled(false);
        enviarTicket = (Button) findViewById(R.id.enviarMail);
        imprimirTicket = (Button) findViewById(R.id.imprimir);
        salirTickets = (Button) findViewById(R.id.salir);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Bundle formulario = this.getIntent().getExtras();
        tda_id = formulario.getString("tdaId");
        tckId = formulario.getString("tckId");
        email = formulario.getString("txtCorreo");
        nombreCliente = formulario.getString("nombreCliente");
        nifCliente = formulario.getString("nifCliente");
        TicketDML t = new TicketDML(this);
        tck = t.getTicketById(dbHelper, tckId, this);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        tda = tdaDml.getTdaById(dbHelper, tda_id, this);
        adr = tdaDml.getAddressByTdaId(dbHelper, tda_id, this);
        TripDML tripDML = new TripDML(this);
        trip = tripDML.getTripByTicketId(dbHelper, tckId, this);
    }

    public void enviarMail(View v){
        pgbImprimir.setVisibility(View.GONE);
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();

        String emailNombreArchivo = tck.getTck_numTickRed() + ".pdf";
        String emailNumFactura = tck.getTck_number();
        String emailGenerados = c.GENERADOS_TICKETS;
        String emailNombreCarpeta = c.NOMBRE_CARPETA_APP;
        String[] to = {tda.getTda_email()};
        String[] cc = {email};
        String mensaje = getString(R.string.ticketEmailSubject) + tck.getTck_date() + "";
     /*   String sdCardRoot = Environment.getExternalStorageDirectory()
                + File.separator + emailNombreCarpeta
                + File.separator + "Mis_Pruebas"//emailGenerados
                + File.separator + emailNombreArchivo;
     */   String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP + File.separator
                + c.GENERADOS_TICKETS + File.separator + tck.getTck_numTickRed() + ".pdf";
        Intent emailIntent = cm.enviarPdfEmail(nombre_completo, to, cc, "" + "Ticket " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    public void imprimir(View v){
        imprimirTicket();
    }

    public void salir(View v){
        Intent i = new Intent(this, MenuPrincipal.class);
        finish();
        startActivity(i);
    }

    private void disableButtons(){
        imprimirTicket.setVisibility(View.GONE);
        salirTickets.setVisibility(View.GONE);
        enviarTicket.setVisibility(View.GONE);
    }

    private void enableButtons(){
        imprimirTicket.setVisibility(View.VISIBLE);
        salirTickets.setVisibility(View.VISIBLE);
        enviarTicket.setVisibility(View.VISIBLE);
    }

    public void imprimirTicket(){
        disableButtons();

        pgbImprimir.setVisibility(View.VISIBLE);
        if(abrirConexion(this)){
            enviarDatos();
            enableButtons();
        }
        else{
            pgbImprimir.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.notConnectedToPrinter), Toast.LENGTH_SHORT).show();
            enableButtons();
        }
    }

   /* @TargetApi(Build.VERSION_CODES.KITKAT)
    public void printTicket(Ticket ticket, Taxi_driver_account taxi_driver_account, Context context){
        tda = taxi_driver_account;
        tda_id = String.valueOf(taxi_driver_account.getTda_id());
        try {
            if(abrirConexion(context)){
                URI uriPath = URI.create(new File(ticket.getTck_pdf()).toURI().toString());
                //String path = new File(ticket.getTck_pdf()).toURI().toString();
                //InputStream fis = context.openFileInput(ticket.getTck_pdf());
 /*               InputStream is = new FileInputStream(uriPath.getPath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                //byte[] b = new byte[(int) ticket.getTck_pdf().length()];
                int bytesRead = is.read(b);
                while(bytesRead != -1) {
                    bos.write(b, 0, bytesRead);
                    bytesRead = is.read(b);
                }
                byte[] bytes = bos.toByteArray();

                byte[] printformat = { 27, 33, 0 }; //try adding this print format
*/
 /*               PdfReader reader = new PdfReader(ticket.getTck_pdf());
                System.out.println("This PDF has "+reader.getNumberOfPages()+" pages.");
                String page = PdfTextExtractor.getTextFromPage(reader, 2);
                System.out.println("Page Content:\n\n"+page+"\n\n");
                System.out.println("Is this document tampered: "+reader.isTampered());
                System.out.println("Is this document encrypted: "+reader.isEncrypted());
*/
               // System.err.println("bytes: " + new String(bytes, StandardCharsets.UTF_8));
               // System.err.println("printFormat: " + new String(printformat, StandardCharsets.UTF_8));
               // mmOutputStream.write(printformat);
               // mmOutputStream.write(bytes);

                // tell the user data were sent
   /*             Toast.makeText(context, "Ticket imprimido correctamente", Toast.LENGTH_SHORT).show();

                closeBT();
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private void esperar (int segundos) {
        try {
            Thread.sleep (segundos*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean abrirConexion(Context context){
        boolean conexionAbierta = false;
        try {
            if(findBT(context)){
                //esperar(1);
                if(openBT(context)){
                    esperar(1);
                    conexionAbierta = true;
                }
            }
        } catch (IOException ex) {
        }
        return conexionAbierta;
    }

    private void enviarDatos() {
        try {
            sendData();
            esperar(1);
            closeBT();
        } catch (IOException e) {
        }
    }

    // This will find a bluetooth printer device
    private boolean findBT(Context context) {
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
                Toast.makeText(context,"Encienda la impresora, por favor", Toast.LENGTH_SHORT).show();
                found = false;
                this.onStop();
            }
            else{
                Toast.makeText(context, "Dispositivo BT encontrado", Toast.LENGTH_SHORT).show();
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
    private boolean openBT(Context context) throws IOException {
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
                Toast.makeText(context, getString(R.string.connected) + mmDevice.getName(), Toast.LENGTH_SHORT).show();
                conectado = true;
                beginListenForData();
            }
            else{
                Toast.makeText(context, getString(R.string.turnOnPrinter), Toast.LENGTH_SHORT).show();
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
    private void sendData() throws IOException {
        pgbImprimir.setProgress(75);
        Constants c = null;
        Bluetooth bt = null;
        try {
            c = new Constants();
            bt = new BluetoothImplementation();
            String euro = "\u20ac";
            String servTran = "\n" + "       " + bt.reemplaza_Char(c.NOMBRE_CARPETA_APP);
            servTran = bt.reemplaza_Char(servTran);
            servTran = servTran + "\n";

            //String name = "  " + bt.reemplaza_Char(tda.getTda_name() + "\n");
            String name = bt.reemplaza_Char(tda.getTda_name() + "\n");
            name = bt.reemplaza_Char(name);
            String nif = bt.reemplaza_Char("N.I.F.: " + tda.getTda_nif());
            nif = bt.reemplaza_Char(nif);
            //nif = "        " + nif;
            nif += "\n";
            String dir = bt.reemplaza_Char(adr.getAdr_street());
            dir = bt.reemplaza_Char(dir);
            //dir = "     " + dir;
            dir += "\n";
            String cp = bt.reemplaza_Char(adr.getAdr_town() + ", " + adr.getAdr_postcode() + ", " + adr.getAdr_county());
            cp = bt.reemplaza_Char(cp);
            //cp = "         " + cp;
            cp += "\n";
            String licencia = null;
            if (!TextUtils.isEmpty(tda.getTda_license_number())) {
                licencia = bt.reemplaza_Char("Licencia Taxi " + tda.getTda_license_number());
                licencia = bt.reemplaza_Char(licencia);
                licencia = "   " + licencia;
                licencia += "\n";
            }
            String linea = "-------------------------------";
            linea += "\n";

            String fechaHora = tck.getTck_date() + "         " + tck.getTck_time() + "\n" + "\n";
            String numeroTicket = bt.comprobarNull(tck.getTck_number());
            String origenImp = bt.comprobarNull(trip.getTrp_start());

            String destinoImp = bt.comprobarNull(trip.getTrp_finish());

            String precioT = bt.comprobarNull(trip.getTrp_price());
            String clientName = bt.comprobarNull(nombreCliente);

            String nifCif = bt.comprobarNull(nifCliente);
            String num = "Num. factura reducido: \n" + numeroTicket + "\n" + "\n";
            num = bt.reemplaza_Char(num);
            //String cl = "Cliente: \n" + clientName + "\n" + "\n";
            String cl = "Cliente: \n";
            cl = bt.reemplaza_Char(cl);
            String nc = "N.I.F./C.I.F.: \n" + nifCif + "\n" + "\n";
            nc = bt.reemplaza_Char(nc);
            //String ori = "Origen: \n" + origenImp + "\n" + "\n";
            String ori = "Origen: \n";
            ori = bt.reemplaza_Char(ori);
            //String des = "Destino: \n" + destinoImp + "\n" + "\n";
            String des = "Destino: \n";
            des = bt.reemplaza_Char(des);
            String pr = "Precio: \n" + precioT + " EUR" + "  (10% IVA inc.)" + "\n" + "\n";
            pr = bt.reemplaza_Char(pr);
            String fin1 = "Gracias por utilizar nuestro    servicio." + "\n" + "\n" + "Thank you for using our service." + "\n" + "\n";
            fin1 = bt.reemplaza_Char(fin1);
            //String msg2 = "\n";

            ArrayList<String> a = bt.maximumTam(bt.reemplaza_Char(origenImp));
            ArrayList<String> a1 = bt.maximumTam(bt.reemplaza_Char(destinoImp));
            ArrayList<String> a2 = bt.maximumTam(bt.reemplaza_Char(clientName));

            Toast.makeText(this, "Imprimiendo " + tck.getTck_numTickRed(), Toast.LENGTH_SHORT).show();

            mmOutputStream.write(servTran.getBytes());
            mmOutputStream.write(bt.reemplaza_Char(name).getBytes());
            mmOutputStream.write(bt.reemplaza_Char(nif).getBytes());
            mmOutputStream.write(bt.reemplaza_Char(dir).getBytes());
            mmOutputStream.write(bt.reemplaza_Char(cp).getBytes());
            if (!TextUtils.isEmpty(licencia)) {
                mmOutputStream.write(bt.reemplaza_Char(licencia).getBytes());
            }
            mmOutputStream.write(linea.getBytes());
            mmOutputStream.write(fechaHora.getBytes());
            mmOutputStream.write(bt.reemplaza_Char(num).getBytes());

            mmOutputStream.write(cl.getBytes());
            for (String s : a2) {
                mmOutputStream.write((bt.reemplazar(s) + "\n").getBytes());
            }
            mmOutputStream.write("\n \n".getBytes());

            mmOutputStream.write(bt.reemplaza_Char(nc).getBytes());

            mmOutputStream.write(bt.reemplaza_Char(ori).getBytes());
            for (String s : a) {
                mmOutputStream.write((s + "\n").getBytes());
            }
            mmOutputStream.write("\n \n".getBytes());

            mmOutputStream.write(bt.reemplaza_Char(des).getBytes());
            for (String s : a1) {
                mmOutputStream.write((s + "\n").getBytes());
            }
            mmOutputStream.write("\n \n".getBytes());

            mmOutputStream.write(bt.reemplaza_Char(pr).getBytes());
            mmOutputStream.write(bt.reemplaza_Char(fin1).getBytes());
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
