package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Contrato;
import sqlite.model.Stop;

public class CrearHojaRuta3 extends AppCompatActivity {

    private String hojaRutaId = null;
    private EditText parada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_hoja_ruta3);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_createHojaRuta3);

        Bundle formulario = this.getIntent().getExtras();
        hojaRutaId = formulario.getString("var_hojaRutaId");
        parada = (EditText) findViewById(R.id.paradasHojaRuta);
    }

    public void addStops(View v){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        if(parada != null) {
            if (!parada.getText().toString().isEmpty()) {
                Stop stop = new Stop();
                stop.setStp_hjr_id(hojaRutaId);
                stop.setStp_place(parada.getText().toString().toUpperCase());
                hrm.crearStop(stop, this);

                Intent i = new Intent(this, CrearHojaRuta3.class);
                i.putExtra("var_hojaRutaId", hojaRutaId);

                startActivity(i);
                finish();
            }
        }
        Toast.makeText(this, "No se pueden añadir paradas vacías", Toast.LENGTH_LONG).show();
    }

    public void generarHojaRuta(View v){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        if(parada != null) {
            if(!parada.getText().toString().isEmpty()){
                Stop stop = new Stop();
                stop.setStp_hjr_id(hojaRutaId);
                stop.setStp_place(parada.getText().toString().toUpperCase());
                hrm.crearStop(stop, this);
            }
        }
        sqlite.model.HojaRuta hr = hrm.getHojaRuta(hojaRutaId, this);
        Contrato contrato = hrm.getContrato(hr.getHjr_con_id(), this);

        hrm.generatePDF(hojaRutaId, hr, this);
        Toast.makeText(this, "Hoja de ruta " + hr.getHjr_alias() + " creada.", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HojaRuta.class);
        i.putExtra("contratoId", hr.getHjr_con_id());
        startActivity(i);
        finish();
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
        Toast.makeText(this, "Finalice la hoja de ruta, por favor.", Toast.LENGTH_LONG).show();
    }

}
