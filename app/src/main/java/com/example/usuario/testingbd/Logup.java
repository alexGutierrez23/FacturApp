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

public class Logup extends AppCompatActivity {

    private EditText direccion, provincia, poblacion, codigoPostal;
    private Button siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_logUp);
        act.setDisplayUseLogoEnabled(true);
        act.setDisplayHomeAsUpEnabled(true);

        direccion = (EditText) findViewById(R.id.direccionRegistro);  //dividir direccion
        provincia = (EditText) findViewById(R.id.dirProvinciaRegistro);
        poblacion = (EditText) findViewById(R.id.dirMunicipioRegistro);
        codigoPostal = (EditText) findViewById(R.id.dirCpRegistro);
        siguiente = (Button) findViewById(R.id.siguienteRegistro);
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
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void goToNextLogup(View v) {  //Evento onClick de los botones
        CommonMethods cm = new CommonMethodsImplementation();
        Intent i = new Intent(this, Logup2.class);

        String direccionRegistro = direccion.getText().toString();
        String poblacionRegistro = poblacion.getText().toString();
        String provinciaRegistro = provincia.getText().toString();
        String cpRegistro = codigoPostal.getText().toString();

        boolean direccionOk = false;
        boolean poblacionOk = false;
        boolean provinciaOk = false;
        boolean cpOk = false;

        if(TextUtils.isEmpty(direccionRegistro)){
            direccion.setError(getString(R.string.notEmptyField));
        }
        else{
            //boolean valid = cm.isValidData(direccionRegistro);
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

        if( direccionOk && poblacionOk && provinciaOk && cpOk ){
            AddressTesting adrTesting = new AddressTesting();
            adrTesting.setAddress(direccionRegistro);
            adrTesting.setTown(poblacionRegistro);
            adrTesting.setCounty(provinciaRegistro);
            adrTesting.setPostcode(cpRegistro);

            i.putExtra("adrTesting", adrTesting);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, R.string.completeForm, Toast.LENGTH_SHORT).show();
        }

    }
}
