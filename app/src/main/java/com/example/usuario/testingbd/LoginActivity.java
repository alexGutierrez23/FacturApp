package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.InvoiceMethodsImplementation;
import appImplementations.RegistrationMethodsImplementation;
import appImplementations.TicketMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import appInterfaces.RegistrationMethods;
import appInterfaces.TicketMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Invoice;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.repo.Taxi_DriverDML;
import sqlite.repo.Taxi_driver_accountDML;

public class LoginActivity extends AppCompatActivity {

    private String tdaId = null;
    private String txdId = null;

    private EditText codeLogin = null;
    private Button logUpButton = null;
    private Button logInButton = null;
    private Button reenviarCodigo = null;
    private TextView textoRegistro = null;

    private int tdaCount = 0;
    private int txdCount = 0;

    private String lastTicket = null;
    private String lastInvoice = null;

    private boolean areLogsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_login);
        act.setIcon(R.mipmap.ic_launcher);
        act.setLogo(R.mipmap.ic_launcher);

        codeLogin = (EditText) findViewById(R.id.codeLogin);
        logUpButton = (Button) findViewById(R.id.logupButton);
        logInButton = (Button) findViewById(R.id.login);
        textoRegistro = (TextView) findViewById(R.id.textView12);
        reenviarCodigo = (Button) findViewById(R.id.reenviarCodigo);

        RegistrationMethods rm = new RegistrationMethodsImplementation();
        tdaCount = rm.getTdaNumber(this);
        txdCount = rm.getTxdNumber(this);

        try{
            Bundle formulario = this.getIntent().getExtras();
            if(formulario.getString("txdId") != null && formulario.getString("tdaId") != null){
                txdId = formulario.getString("txdId");
                tdaId = formulario.getString("tdaId");
            }
        }
        catch (NullPointerException npe){
            Log.e("txdId, tdaId: ", npe.getMessage());
            npe.getStackTrace();
        }

        if(txdCount == 0 && tdaCount == 0){
            logUpButton.setEnabled(true);
            logUpButton.setVisibility(View.VISIBLE);
            textoRegistro.setVisibility(View.VISIBLE);
            logInButton.setEnabled(false);
            reenviarCodigo.setVisibility(View.GONE);
            logInButton.setVisibility(View.GONE);
            codeLogin.setVisibility(View.GONE);
            Toast.makeText(this, R.string.logUpMessage, Toast.LENGTH_LONG).show();
        }
        else{
            CommonMethods cm = new CommonMethodsImplementation();
            if(txdId == null){
                Taxi_Driver txd = cm.getTxd(this);
                txdId = String.valueOf(txd.getTxd_id());
                Taxi_driver_account tda = new Taxi_driver_account();
                Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
                int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this),
                        txdId, this);
                tdaId = String.valueOf(tda_id);
            }
            else{
                if(tdaId == null){
                    Taxi_driver_account tda = new Taxi_driver_account();
                    Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
                    int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this),
                            txdId, this);
                    tdaId = String.valueOf(tda_id);
                }
            }
            boolean activated = rm.isTdaActivated(this, tdaId);
        /*    if(activated){
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        Intent i = new Intent(LoginActivity.this.getApplicationContext(), MenuPrincipal.class);
                        finish();
                        startActivity(i);
                    }
                }, 0);
            }*/
           // else{
            if(!activated){
                logUpButton.setEnabled(false);
                logUpButton.setVisibility(View.GONE);
                textoRegistro.setVisibility(View.GONE);
                logInButton.setEnabled(true);
                logInButton.setVisibility(View.VISIBLE);
                reenviarCodigo.setVisibility(View.VISIBLE);
                codeLogin.setVisibility(View.VISIBLE);
                Toast.makeText(this, R.string.logupCompletedMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void login(View v) {  //Evento onClick de los botones
        Intent i = new Intent(this, MenuPrincipal.class);
        CommonMethods cm = new CommonMethodsImplementation();

        String code = codeLogin.getText().toString();

        boolean isCodeAllowed = false;

        if(TextUtils.isEmpty(code)){
            codeLogin.setError(getString(R.string.notEmptyField));
        }
        else{
            codeLogin.setError(null);
            Taxi_Driver txd = null;
            boolean correctCode = false;
            code = code.trim();  //Evitamos que los usuarios metan espacios sin querer
     /*       if(TextUtils.isEmpty(txdId)){
                if(txdCount == 1){
                    Taxi_DriverDML txdDml = new Taxi_DriverDML(this);
                    txdDml.
                }
            }
            if(TextUtils.isEmpty(tdaId)){
                if(tdaCount == 1){
                    Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
                    tdaDml.
                }
            } */
            if(txdId == null){
                txd = cm.getTxd(this);
                txdId = String.valueOf(txd.getTxd_id());
                correctCode = checkCode(code, txd.getTxd_confirmationCode());
            }
            else{
                txd = cm.getTxd(this);
                correctCode = checkCode(code, txd.getTxd_confirmationCode());
            }

            if(correctCode){
                Taxi_driver_account tda = new Taxi_driver_account();
                Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
                int tdaId = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this),
                        String.valueOf(txd.getTxd_id()), this);
                tda = tdaDml.getTdaById(new DatabaseHelper(this), String.valueOf(tdaId), this);
                RegistrationMethods rm = new RegistrationMethodsImplementation();
                tda.setTda_activated("1");
                rm.activateTaxiDriverAccount(this, tda);

                Taxi_DriverDML txdDml = new Taxi_DriverDML(this);
                if(txdId != null){
                    //Si devuelve "0" significa que no hemos cargado ninguna vez. Si devuelve "1" ya lo hemos cargado.
                    areLogsLoaded = txdDml.areLogLoaded(new DatabaseHelper(this), txdId, this);
                }
                if(!areLogsLoaded){
                    loadLogs();
                    txdDml.updateLogLoaded(new DatabaseHelper(this), txdId, this);
                    areLogsLoaded = txdDml.areLogLoaded(new DatabaseHelper(this), txdId, this);
                }

                startActivity(i);
                finish();
            }
            else{
                codeLogin.setError("Este código no es correcto.");
            }
        }

    }

    private boolean checkCode(String code, String confirmationCode){
        boolean correctCode = false;

        if(code.equals(confirmationCode)){
            correctCode = true;
        }
        return correctCode;
    }

    private void loadLogs(){
        //Este inicio de sesion ira en Login.java
        RegistrationMethods rm = new RegistrationMethodsImplementation();
        //txdId = rm.registrarTaxiDriver(this);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this), txdId, this);
        tdaId = String.valueOf(tda_id);

        // 1)Cargamos por primera vez y actualizamos el valor en la BBDD.
        // 2) Creo un nuevo ticket con ese numero y lo meto en la BBDD de Tickets y asi existe ese valor.
        // Por tanto, no volvera a cargar mas que una vez salvo que eliminemos la APP.
        CommonMethods cm = new CommonMethodsImplementation();
        Constants constants = new Constants();
        lastTicket = cm.buscarArchivosGuardados(constants.TICKET_TYPE, constants.NOMBRE_CARPETA_APP);
        lastInvoice = cm.buscarArchivosGuardados(constants.FACTURA_TYPE, constants.NOMBRE_CARPETA_APP);
        //Insertar ultimo ticket del log (no necesitamos los demas xq lo q hacemos es
        // iniciar la ceunta desde el ultimo numero de ticket
        if(lastTicket != null){
            lastTicket = cm.eliminarEspaciosEnBlanco(lastTicket);
            lastTicket = cm.quitarLast(lastTicket);
            Ticket tck = new Ticket();
            tck.setTck_date(cm.getDatePhone());
            tck.setTck_time(cm.getHourPhone());
            tck.setTck_cus_id(null);
            TicketMethods tm = new TicketMethodsImplementation();
            tm.createTicket(tck, tdaId, null, null, cm.sacarNumeroReducido(lastTicket), this);
        }
        if(lastInvoice != null){
            lastInvoice = cm.eliminarEspaciosEnBlanco(lastInvoice);
            lastInvoice = cm.quitarLast(lastInvoice);
            Invoice invoice = new Invoice();
            invoice.setInv_date(cm.getDatePhone());
            invoice.setInv_time(cm.getHourPhone());
            InvoiceMethods im = new InvoiceMethodsImplementation();
            im.createInvoice(this, invoice, tdaId, null, null, cm.sacarNumeroReducido(lastInvoice));
        }
    }

    public void reenviarCodigo(View v){
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(this);
        String tdaId = cm.getTdaIdGivenTxdId(this, String.valueOf(txd.getTxd_id()));
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        Taxi_driver_account tda = tdaDml.getTdaById(new DatabaseHelper(this), tdaId, this);
        enviar(tda, txd);
    }

    public void enviar(Taxi_driver_account tda, Taxi_Driver txd){
        RegistrationMethods rm = new RegistrationMethodsImplementation();

        String[] to = {getString(R.string.myEmail)};
        StringBuffer sb = new StringBuffer();
        sb.append(getString(R.string.verificationCodeMessage1));
        sb.append(" " + tda.getTda_name() + " ");
        sb.append(getString(R.string.verificationCodeMessage2));
        sb.append(" " + txd.getTxd_confirmationCodeEncoded());
        String mensaje = sb.toString();
        String asunto = getString(R.string.verificationCodeSubject);

        Intent emailIntent = rm.enviarEmail(to, asunto, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    public void logup(View v) {  //Evento onClick de los botones
        Intent i = new Intent(this, Logup.class);
        finish();
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Inicie sesión, por favor.", Toast.LENGTH_LONG).show();
    }

}
