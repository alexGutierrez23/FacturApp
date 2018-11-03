package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.model.Presupuesto;

public class Presupuesto1 extends AppCompatActivity {

    private Button btnDatosViajePre1;
    private EditText nombrePresupuesto, nifPresupuesto;
    private EditText emailPresupuesto;

    private HashMap<String, String> customerAddressMap = null;
    private String tdaId = null;
    private String fechaPresupuesto = null;
    private AddressTesting adr = null;

    private boolean backFromPresupuesto3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto1);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_presupuesto1);
        act.setDisplayHomeAsUpEnabled(true);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        nombrePresupuesto = (EditText) findViewById(R.id.nombrePresupuesto);
        nifPresupuesto = (EditText) findViewById(R.id.nifPresupuesto);
        emailPresupuesto = (EditText) findViewById(R.id.emailPresupuesto1);
        btnDatosViajePre1 = (Button) findViewById(R.id.login);

        Intent i = getIntent();
        //customerAddressMap = (HashMap<String, String>) i.getSerializableExtra("customerAddressMap");
        Bundle formulario = this.getIntent().getExtras();
        tdaId = formulario.getString("tdaId");
        adr = (AddressTesting) i.getSerializableExtra("var_adr");
        fechaPresupuesto = formulario.getString("var_fechaPresupuesto");
        backFromPresupuesto3 = formulario.getBoolean("var_backFromPresupuesto3");
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
        Intent i = new Intent(this, com.example.usuario.testingbd.Presupuesto.class);
        startActivity(i);
        finish();
    }

    public void goToDatosViajePresupuesto(View v){
        CommonMethods cm = new CommonMethodsImplementation();
        Intent i = new Intent(this, Presupuesto2.class);

        String name = nombrePresupuesto.getText().toString();
        String nif = nifPresupuesto.getText().toString();
        String emailCompPresupuesto = emailPresupuesto.getText().toString();
        boolean emailOk = true;

        if(!TextUtils.isEmpty(emailCompPresupuesto)){
            if(!Patterns.EMAIL_ADDRESS.matcher(emailCompPresupuesto).matches()){
                emailPresupuesto.setError(null);
                emailPresupuesto.setError(getString(R.string.validEmail));
                emailOk = false;
            }
            else{
                emailPresupuesto.setError(null);
                emailOk = true;
            }
        }
        if(emailOk){
            Presupuesto presupuesto = new Presupuesto();
            presupuesto.setPre_date(fechaPresupuesto);
            presupuesto.setPre_time(cm.getHourPhone());

            CustomerTesting customerTesting = new CustomerTesting();
            customerTesting.setName(name);
            customerTesting.setAddress(adr);
            customerTesting.setNif(nif);
            customerTesting.setEmail(emailCompPresupuesto);

            i.putExtra("var_presupuesto", presupuesto);
            Log.e("Fecha PRE: ", presupuesto.getPre_date());
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_customerTesting", customerTesting);
            i.putExtra("var_backFromPresupuesto3", false);

            startActivity(i);
            finish();
        }
    }

}
