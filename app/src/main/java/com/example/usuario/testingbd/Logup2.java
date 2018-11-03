package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import appImplementations.RegistrationMethodsImplementation;
import appInterfaces.RegistrationMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.Taxi_DriverDML;
import sqlite.repo.Taxi_driver_accountDML;

public class Logup2 extends AppCompatActivity {

    private EditText nombreRegistro, nifRegistro, email, licenciaLogup, printerLogup, mobileLogup;
    private Button registrar;

    private AddressTesting adrTesting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup2);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_logUp2);
        act.setDisplayHomeAsUpEnabled(true);

        nombreRegistro = (EditText) findViewById(R.id.nombreRegistro);  //dividir direccion
        nifRegistro = (EditText) findViewById(R.id.nifRegistro);
        email = (EditText) findViewById(R.id.emailLogin);
        mobileLogup = (EditText) findViewById(R.id.mobileNumberLogin);
        licenciaLogup = (EditText) findViewById(R.id.licenciaLogup);
        printerLogup = (EditText) findViewById(R.id.printerLogup);
        registrar = (Button) findViewById(R.id.registro);

        Intent i = getIntent();
        adrTesting = (AddressTesting) i.getSerializableExtra("adrTesting");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Logup.class);
        startActivity(i);
        finish();
    }

    public void registrarTaxiDriver(View v) {  //Evento onClick de los botones{
        Intent i = new Intent(this, LoginActivity.class);

        Address adr = new Address();
        adr.setAdr_street(adrTesting.getAddress());
        adr.setAdr_town(adrTesting.getTown());
        adr.setAdr_county(adrTesting.getCounty());
        adr.setAdr_postcode(adrTesting.getPostcode());

        String name = nombreRegistro.getText().toString();
        String nif = nifRegistro.getText().toString();
        String emailTda = email.getText().toString();
        String mobile = mobileLogup.getText().toString();
        String licencia = licenciaLogup.getText().toString();
        String printer = printerLogup.getText().toString();

        boolean nombreOk = false;
        boolean nifOk = false;
        boolean emailOk = false;
        boolean printerOk = false;
        boolean mobileOk = false;

        if(TextUtils.isEmpty(name)){
            nombreRegistro.setError(getString(R.string.notEmptyField));
        }
        else{
            nombreRegistro.setError(null);
            nombreOk = true;
        }
        if(TextUtils.isEmpty(nif)){
            nifRegistro.setError(getString(R.string.notEmptyField));
        }
        else{
            nifRegistro.setError(null);
            nifOk = true;
        }
        if(TextUtils.isEmpty(emailTda)){
            email.setError(getString(R.string.notEmptyField));
        }
        else{
            if(!Patterns.EMAIL_ADDRESS.matcher(emailTda).matches()){
                email.setError(null);
                email.setError(getString(R.string.validEmail));
            }
            else{
                email.setError(null);
                emailOk = true;
            }
        }
        if(TextUtils.isEmpty(mobile)){
            mobileLogup.setError(getString(R.string.notEmptyField));
        }
        else{
            mobileLogup.setError(null);
            mobileOk = true;
        }
        if(TextUtils.isEmpty(printer)){
            printerLogup.setError(getString(R.string.notEmptyField));
        }
        else{
            printerLogup.setError(null);
            printerOk = true;
        }

        if( nombreOk && nifOk && emailOk && printerOk && mobileOk){
            //Si ha rellenado todos los campos
            Taxi_driver_account tda = new Taxi_driver_account();
            tda.setTda_name(name);
            tda.setTda_nif(nif);
            tda.setTda_email(emailTda);
            tda.setTda_license_number(licencia);
            tda.setTda_printerDevice(printer);
            tda.setTda_mobile(mobile);

            RegistrationMethods rm = new RegistrationMethodsImplementation();
            String txdId = rm.registrarTaxiDriver(this, adr, tda);
            Taxi_DriverDML txdDml = new Taxi_DriverDML(this);
            Taxi_Driver txd = txdDml.getTaxiDriverById(new DatabaseHelper(this).getWritableDatabase(),
                    txdId,this);

            Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
            int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(this), txdId, this);
            String tdaId = String.valueOf(tda_id);

            i.putExtra("txdId", txdId);
            i.putExtra("tdaId", tdaId);
            startActivity(i);
            enviar(tda, txd);
            Toast.makeText(this, R.string.sendCodeThroughEmail, Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(this, R.string.completeForm, Toast.LENGTH_SHORT).show();
        }
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
}
