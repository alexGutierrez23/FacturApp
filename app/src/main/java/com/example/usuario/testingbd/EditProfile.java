package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.Taxi_driver_accountDML;

public class EditProfile extends AppCompatActivity {

    private EditText direccion, provincia, poblacion, codigoPostal, mobileNumber;
    private Button siguiente;

    private String txdId = null;
    private String tdaId = null;
    private Taxi_driver_account tda = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editProfile);
        act.setDisplayHomeAsUpEnabled(true);

        direccion = (EditText) findViewById(R.id.direccionEditar);  //dividir direccion
        provincia = (EditText) findViewById(R.id.dirProvinciaEditar);
        poblacion = (EditText) findViewById(R.id.dirMunicipioEditar);
        codigoPostal = (EditText) findViewById(R.id.dirCpEditar);
        mobileNumber = (EditText) findViewById(R.id.dirMobileEditar);
        siguiente = (Button) findViewById(R.id.siguienteEditProfile);

        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(this);
        txdId = String.valueOf(txd.getTxd_id());

        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        tdaId = String.valueOf(tdaDml.getTdaIdByTxdId(new DatabaseHelper(this),
                String.valueOf(txd.getTxd_id()), this));
        tda = tdaDml.getTdaById(new DatabaseHelper(this), String.valueOf(tdaId), this);

        Address adr = cm.getAddressById(tda.getTda_adr_id(), this);
        direccion.setText(adr.getAdr_street());
        poblacion.setText(adr.getAdr_town());
        provincia.setText(adr.getAdr_county());
        codigoPostal.setText(adr.getAdr_postcode());
        mobileNumber.setText(tda.getTda_mobile());
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
        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
        finish();
    }

    public void goToEditProfile2(View v) {  //Evento onClick de los botones
        Intent i = new Intent(this, EditProfile2.class);

        String direccionRegistro = direccion.getText().toString();
        String poblacionRegistro = poblacion.getText().toString();
        String provinciaRegistro = provincia.getText().toString();
        String cpRegistro = codigoPostal.getText().toString();
        String mobile = mobileNumber.getText().toString();

        boolean direccionOk = false;
        boolean poblacionOk = false;
        boolean provinciaOk = false;
        boolean cpOk = false;
        boolean mobileOk = false;

        if(TextUtils.isEmpty(direccionRegistro)){
            direccion.setError(getString(R.string.notEmptyField));
        }
        else{
            direccion.setError(null);
            direccionOk = true;
        }
        if(TextUtils.isEmpty(poblacionRegistro)){
            poblacion.setError(getString(R.string.notEmptyField));
        }
        else{
            poblacion.setError(null);
            poblacionOk = true;
        }
        if(TextUtils.isEmpty(provinciaRegistro)){
            provincia.setError(getString(R.string.notEmptyField));
        }
        else{
            provincia.setError(null);
            provinciaOk = true;
        }
        if(TextUtils.isEmpty(cpRegistro)){
            codigoPostal.setError(getString(R.string.notEmptyField));
        }
        else{
            codigoPostal.setError(null);
            cpOk = true;
        }
        if(TextUtils.isEmpty(mobile)){
            mobileNumber.setError(getString(R.string.notEmptyField));
        }
        else{
            mobileNumber.setError(null);
            mobileOk = true;
        }

        if( direccionOk && poblacionOk && provinciaOk && cpOk && mobileOk ){
            AddressTesting adrTesting = new AddressTesting();
            adrTesting.setAddress(direccionRegistro);
            adrTesting.setTown(poblacionRegistro);
            adrTesting.setCounty(provinciaRegistro);
            adrTesting.setPostcode(cpRegistro);

            i.putExtra("adrTesting", adrTesting);
            i.putExtra("txdId", txdId);
            i.putExtra("tdaId", tdaId);
            i.putExtra("mobileNumber", mobile);

            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, R.string.completeForm, Toast.LENGTH_SHORT).show();
        }

    }
}
