package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import appImplementations.CommonMethodsImplementation;
import sqlite.model.Presupuesto;

public class Presupuesto2 extends AppCompatActivity {

    private Button btnDatosViajePre2;
    private EditText origenPresupuesto, destinoPresupuesto, precioPresupuesto;

    //private String presupuestoId = null;
    private sqlite.model.Presupuesto presupuesto = null;
    private String tdaId = null;
    private CustomerTesting cusTesting = null;
    private ArrayList<String> infoIds = null;

    private boolean backFromPresupuesto3;

    private HashMap<String, String> datosCliente = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto2);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        Intent i = getIntent();
        //datosCliente = (HashMap<String, String>) i.getSerializableExtra("datosClienteMap");
        cusTesting = (CustomerTesting) i.getSerializableExtra("var_customerTesting");
        infoIds = (ArrayList<String>) i.getSerializableExtra("var_infoIds");

        Bundle formulario = this.getIntent().getExtras();
        presupuesto = (Presupuesto) i.getSerializableExtra("var_presupuesto");
        tdaId = formulario.getString("tdaId");
        backFromPresupuesto3 = formulario.getBoolean("var_backFromPresupuesto3");

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_presupuesto2);
        if(!backFromPresupuesto3){
            act.setDisplayHomeAsUpEnabled(true);
        }

        origenPresupuesto = (EditText) findViewById(R.id.origPresupuesto);
        destinoPresupuesto = (EditText) findViewById(R.id.destPresupuesto);
        precioPresupuesto = (EditText) findViewById(R.id.precioPres1);
        btnDatosViajePre2 = (Button) findViewById(R.id.goToDatosViaje2);
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
        if(!backFromPresupuesto3){
            Intent i = new Intent(this, Presupuesto1.class);
            i.putExtra("var_adr", cusTesting.getAddress());
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_fechaPresupuesto", presupuesto.getPre_date());
            i.putExtra("var_backFromPresupuesto3", false);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "No se puede volver atrás. Finalice el presupuesto, por favor.", Toast.LENGTH_LONG).show();
        }
    }

    public void goToDatosViajePresupuesto2(View v){
        Intent i = new Intent(this, Presupuesto3.class);
        HashMap<String, String> tripPresupuesto = new HashMap<>();
        tripPresupuesto.put("origenPresupuesto", origenPresupuesto.getText().toString());
        tripPresupuesto.put("destinoPresupuesto", destinoPresupuesto.getText().toString());
        tripPresupuesto.put("precioPresupuesto", precioPresupuesto.getText().toString());

        i.putExtra("tripPresupuesto", tripPresupuesto);
        i.putExtra("var_presupuesto", presupuesto);
        Log.e("Fecha PRE: ", presupuesto.getPre_date());
        //i.putExtra("var_presupuestoId", presupuestoId);
        i.putExtra("tdaId", tdaId);
        i.putExtra("var_customerTesting", cusTesting);
        i.putExtra("var_infoIds", infoIds);
        if(backFromPresupuesto3){
            i.putExtra("var_backFromPresupuesto3", true);
        }
        else{
            i.putExtra("var_backFromPresupuesto3", false);
        }


        startActivity(i);
        finish();

    }
}
