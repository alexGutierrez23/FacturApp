package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Contrato;

public class EditarContrato extends AppCompatActivity {

    private EditText aliasContrato, nombreArrendador, nifArrendador;
    private Button siguiente;
    private String contratoId = null;
    private Contrato contrato = null;

    private boolean isTdaArrendador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contrato);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editContrato);
        act.setDisplayHomeAsUpEnabled(true);

        aliasContrato = (EditText) findViewById(R.id.aliasEditarContrato);
        nombreArrendador = (EditText) findViewById(R.id.nombreArrendadorEditarContrato);
        nifArrendador = (EditText) findViewById(R.id.nifArrendadorEditarContrato);
        siguiente = (Button) findViewById(R.id.button_goToEditarContrato2);

        Bundle formulario = this.getIntent().getExtras();
        contratoId = formulario.getString("contratoId");

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        contrato = hrm.getContrato(contratoId, this);

        isTdaArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());

        aliasContrato.setText(contrato.getCon_alias());
        aliasContrato.setEnabled(false);
        nombreArrendador.setText(contrato.getCon_arrendador());
        nifArrendador.setText(contrato.getCon_nif_arrendador());
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
        Intent i = new Intent(this, VistaContratos.class);
        startActivity(i);
        finish();
    }

    public void goToEditarContrato2(View v) {  //Evento onClick de los botones
        Intent i = new Intent(this, EditarContrato2.class);

        String alias = aliasContrato.getText().toString();
        String nombreArrendad = nombreArrendador.getText().toString();
        String nifArrendad = nifArrendador.getText().toString();
        String matricula = contrato.getCon_matricula_vehiculo();

        i.putExtra("aliasContrato", alias);
        i.putExtra("nombreArrendador", nombreArrendad);
        i.putExtra("nifArrendador", nifArrendad);
        i.putExtra("matriculaVehiculo", matricula);
        i.putExtra("contratoId", contratoId);
        i.putExtra("isTdaArrendador", isTdaArrendador);

        startActivity(i);
        finish();
    }
}
