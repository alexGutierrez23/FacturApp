package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Trip;

public class EditarHojaRuta1 extends AppCompatActivity {

    private EditText lugarInicio, inicio, destino, pasajeros, observaciones;

    private String hojaRutaId = null;
    private sqlite.model.HojaRuta hr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_hoja_ruta1);

        Bundle formulario = this.getIntent().getExtras();
        hojaRutaId = formulario.getString("hojaRutaId");

        lugarInicio = (EditText) findViewById(R.id.lugarInicioServicioEditarHR);
        lugarInicio.requestFocus();
        inicio = (EditText) findViewById(R.id.inicioServicioEditarHR);
        destino = (EditText) findViewById(R.id.destinoServicioEditarHR);
        pasajeros = (EditText) findViewById(R.id.pasajerosEditarHR);
        observaciones = (EditText) findViewById(R.id.observacionesEditarHR);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editHojaRuta);
        act.setDisplayHomeAsUpEnabled(true);

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        hr = hrm.getHojaRuta(hojaRutaId, this);
        Trip trip = hrm.getTrip(hr.getHjr_trp_id(), this);

        lugarInicio.setText(trip.getTrp_start_place());
        inicio.setText(trip.getTrp_start());
        destino.setText(trip.getTrp_finish());
        pasajeros.setText(hr.getHjr_pasajeros());
        observaciones.setText(hr.getHjr_observaciones());
    }

    public void goToEditarHojaRuta2(View v){
        Intent i = new Intent(this, EditarHojaRuta2.class);

        i.putExtra("var_lugarInicio", lugarInicio.getText().toString());
        i.putExtra("var_inicio", inicio.getText().toString());
        i.putExtra("var_destino", destino.getText().toString());
        i.putExtra("var_pasajeros", pasajeros.getText().toString());
        i.putExtra("var_observaciones", observaciones.getText().toString());
        i.putExtra("hojaRutaId", hojaRutaId);

        startActivity(i);
        finish();
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
        Intent i = new Intent(this, HojaRuta.class);
        i.putExtra("contratoId", hr.getHjr_con_id());
        startActivity(i);
        finish();
    }

}
