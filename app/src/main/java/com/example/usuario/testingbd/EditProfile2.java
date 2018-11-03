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

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.Taxi_driver_accountDML;

public class EditProfile2 extends AppCompatActivity {

    private EditText nombreEditProfile, nifEditProfile, emailEditProfile,
            licenciaEditProfile, printerEditProfile;
    private Button EditProfile, ayudaProfile;

    private String txdId = null;
    private String tdaId = null;
    private Taxi_driver_account tda = null;
    private AddressTesting adrTesting = null;
    private String mobileNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editProfile2);
        act.setDisplayHomeAsUpEnabled(true);

        nombreEditProfile = (EditText) findViewById(R.id.nombreEditar);  //dividir direccion
        nifEditProfile = (EditText) findViewById(R.id.nifEditar);
        emailEditProfile = (EditText) findViewById(R.id.emailEditar);
        licenciaEditProfile = (EditText) findViewById(R.id.licenciaEditar);
        printerEditProfile = (EditText) findViewById(R.id.printerEditar);
        EditProfile = (Button) findViewById(R.id.guardar);

        Intent i = getIntent();
        adrTesting = (AddressTesting) i.getSerializableExtra("adrTesting");
        Bundle formulario = this.getIntent().getExtras();
        tdaId = formulario.getString("tdaId");
        txdId = formulario.getString("txdId");
        mobileNumber = formulario.getString("mobileNumber");

        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        tda = tdaDml.getTdaById(new DatabaseHelper(this), String.valueOf(tdaId), this);

        nombreEditProfile.setText(tda.getTda_name());
        nifEditProfile.setText(tda.getTda_nif());
        emailEditProfile.setText(tda.getTda_email());
        if(tda.getTda_license_number() != null){
            licenciaEditProfile.setText(tda.getTda_license_number());
        }
        printerEditProfile.setText(tda.getTda_printerDevice());
    }

    public void actualizarPerfil(View v) {  //Evento onClick de los botones{
        String name = nombreEditProfile.getText().toString();
        String nif = nifEditProfile.getText().toString();
        String emailTda = emailEditProfile.getText().toString();
        String licencia = licenciaEditProfile.getText().toString();
        String printer = printerEditProfile.getText().toString();

        boolean nombreOk = false;
        boolean nifOk = false;
        boolean emailOk = false;
        boolean printerOk = false;

        if(TextUtils.isEmpty(name)){
            nombreEditProfile.setError(getString(R.string.notEmptyField));
        }
        else{
            nombreEditProfile.setError(null);
            nombreOk = true;
        }
        if(TextUtils.isEmpty(nif)){
            nifEditProfile.setError(getString(R.string.notEmptyField));
        }
        else{
            nifEditProfile.setError(null);
            nifOk = true;
        }
        if(TextUtils.isEmpty(emailTda)){
            emailEditProfile.setError(getString(R.string.notEmptyField));
        }
        else{
            if(!Patterns.EMAIL_ADDRESS.matcher(emailTda).matches()){
                emailEditProfile.setError(null);
                emailEditProfile.setError(getString(R.string.validEmail));
            }
            else{
                emailEditProfile.setError(null);
                emailOk = true;
            }
        }
        if(TextUtils.isEmpty(printer)){
            printerEditProfile.setError(getString(R.string.notEmptyField));
        }
        else{
            printerEditProfile.setError(null);
            printerOk = true;
        }

        if( nombreOk && nifOk && emailOk && printerOk){
            //Si ha rellenado todos los campos
            CommonMethods cm = new CommonMethodsImplementation();

            Address adr = cm.getAddressById(tda.getTda_adr_id(), this);;
            adr.setAdr_street(adrTesting.getAddress());
            adr.setAdr_town(adrTesting.getTown());
            adr.setAdr_county(adrTesting.getCounty());
            adr.setAdr_postcode(adrTesting.getPostcode());

            cm.updateAddress(adr, this);

            tda.setTda_name(name);
            tda.setTda_nif(nif);
            tda.setTda_email(emailTda);
            tda.setTda_license_number(licencia);
            tda.setTda_printerDevice(printer);
            tda.setTda_mobile(mobileNumber);

            cm.updateTaxiDriverAccount(tda,this);

            Toast.makeText(this, R.string.profileUpdated, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, MenuPrincipal.class);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, R.string.completeForm, Toast.LENGTH_SHORT).show();
        }
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
        Intent i = new Intent(this, EditProfile.class);
        startActivity(i);
        finish();
    }

}
