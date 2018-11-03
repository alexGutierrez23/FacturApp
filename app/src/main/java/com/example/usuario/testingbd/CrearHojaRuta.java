package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CrearHojaRuta extends AppCompatActivity {
    private EditText lugarInicio, inicio, destino, pasajeros, observaciones;

    private String contratoId = null;
    private String arrendatarioId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_hoja_ruta);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_createHojaRuta);
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        contratoId = formulario.getString("contratoId");
        arrendatarioId = formulario.getString("arrendatarioId");

        lugarInicio = (EditText) findViewById(R.id.lugarInicioServicio);
        lugarInicio.requestFocus();
        inicio = (EditText) findViewById(R.id.inicioServicio);
        destino = (EditText) findViewById(R.id.destinoServicio);
        pasajeros = (EditText) findViewById(R.id.pasajerosHojaRuta);
        observaciones = (EditText) findViewById(R.id.obervacionesHojaRuta);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToCrearHojaRuta2(View v){
        Intent i = new Intent(this, CrearHojaRuta2.class);

        i.putExtra("var_lugarInicio", lugarInicio.getText().toString());
        i.putExtra("var_inicio", inicio.getText().toString());
        i.putExtra("var_destino", destino.getText().toString());
        i.putExtra("var_pasajeros", pasajeros.getText().toString());
        i.putExtra("var_observaciones", observaciones.getText().toString());
        i.putExtra("contratoId", contratoId);
        i.putExtra("arrendatarioId", arrendatarioId);

        startActivity(i);
        finish();
    }

}
