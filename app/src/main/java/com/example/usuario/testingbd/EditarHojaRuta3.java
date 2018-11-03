package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Stop;

public class EditarHojaRuta3 extends AppCompatActivity {

    private String hojaRutaId = null;
    private EditText parada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_hoja_ruta3);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editHojaRuta3);

        Bundle formulario = this.getIntent().getExtras();
        hojaRutaId = formulario.getString("hojaRutaId");

        parada = (EditText) findViewById(R.id.paradasHojaRutaEditarHR);
    }

    public void addStopsEditarHR(View v){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        Stop stop = new Stop();
        stop.setStp_hjr_id(hojaRutaId);
        stop.setStp_place(parada.getText().toString());
        hrm.crearStop(stop, this);

        Intent i = new Intent(this, CrearHojaRuta3.class);
        i.putExtra("hojaRutaId", hojaRutaId);

        startActivity(i);
        finish();
    }

    public void actualizarHojaRuta(View v){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        if(parada != null) {
            if(!parada.getText().toString().isEmpty()){
                Stop stop = new Stop();
                stop.setStp_hjr_id(hojaRutaId);
                stop.setStp_place(parada.getText().toString());
                hrm.crearStop(stop, this);
            }
        }
        sqlite.model.HojaRuta hr = hrm.getHojaRuta(hojaRutaId, this);

        //Primero borramos el PDF
        CommonMethods cm = new CommonMethodsImplementation();
        cm.borrarPdf(hr.getHjr_pathPdf());
        //Despues generamos
        hrm.generatePDF(hojaRutaId, hr, this);
        Toast.makeText(this, "Hoja de ruta " + hr.getHjr_alias() + " actualizada.", Toast.LENGTH_LONG).show();

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
        Toast.makeText(this, "Por favor, actualice la hoja de ruta.", Toast.LENGTH_LONG).show();
    }

}
