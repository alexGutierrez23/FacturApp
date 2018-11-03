package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.model.Taxi_Driver;

public class Ticket1 extends AppCompatActivity {

    private Button btnTicket;
    private EditText txtOrigen, txtDestino, txtPrecio;

    private String tdaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket1);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_ticket1);
        act.setDisplayHomeAsUpEnabled(true);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        txtOrigen = (EditText) findViewById(R.id.origenTicket);
        txtDestino = (EditText) findViewById(R.id.destinoTicket);
        txtPrecio = (EditText) findViewById(R.id.precioTicket);
        btnTicket = (Button) findViewById(R.id.buttonNextStepTicket);

        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(this);
        if (txd != null) {
            String txdId = String.valueOf(txd.getTxd_id());
            tdaId = cm.getTdaIdGivenTxdId(this, txdId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
            /*    Intent i = new Intent(this, MenuPrincipal.class);
                startActivity(i);
                finish(); */
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

    public void siguienteFormulario(View v){
        Intent i = new Intent(this, Ticket2.class);
        i.putExtra("var_origenTicket", txtOrigen.getText().toString());
        i.putExtra("var_destinoTicket", txtDestino.getText().toString());
        i.putExtra("var_precioTicket", txtPrecio.getText().toString());
        i.putExtra("tdaId", tdaId);
        startActivity(i);
        finish();
    }


}
