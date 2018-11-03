package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import appImplementations.CommonMethodsImplementation;
import sqlite.model.Invoice;

public class Factura2 extends AppCompatActivity {

    private Button btnDatosViajeFact2;
    private EditText origenFactura, destinoFactura, precioFactura;

    private String invoiceId = null;
    private String tdaId = null;
    private CustomerTesting cusTesting = null;
    private ArrayList<String> infoIds = null;
    private Invoice invoice = null;
    private boolean backFromFactura3;

    private HashMap<String, String> datosCliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura2);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        Bundle formulario = this.getIntent().getExtras();
        //invoiceId = formulario.getString("var_invoiceId");
        tdaId = formulario.getString("tdaId");
        backFromFactura3 = formulario.getBoolean("var_backFromFactura3");

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_factura2);
        if(!backFromFactura3){
            act.setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        cusTesting = (CustomerTesting) i.getSerializableExtra("var_customerTesting");
        invoice = (Invoice) i.getSerializableExtra("var_invoice");
        infoIds = (ArrayList<String>) i.getSerializableExtra("var_infoIds");

        origenFactura = (EditText) findViewById(R.id.origFactura);
        destinoFactura = (EditText) findViewById(R.id.destFactura);
        precioFactura = (EditText) findViewById(R.id.precioFact1);
        btnDatosViajeFact2 = (Button) findViewById(R.id.goToDatosViaje2);
    }

    public void goToDatosViaje2(View v){
        Intent i = new Intent(this, Factura3.class);
        HashMap<String, String> tripFactura = new HashMap<>();
        tripFactura.put("origenFactura", origenFactura.getText().toString());
        tripFactura.put("destinoFactura", destinoFactura.getText().toString());
        tripFactura.put("precioFactura", precioFactura.getText().toString());

        i.putExtra("tripFactura", tripFactura);
        //i.putExtra("var_invoiceId", invoiceId);
        i.putExtra("var_invoice", invoice);
        i.putExtra("tdaId", tdaId);
        i.putExtra("var_customerTesting", cusTesting);
        i.putExtra("var_infoIds", infoIds);
        if(backFromFactura3){
            i.putExtra("var_backFromFactura3", true);
        }
        else{
            i.putExtra("var_backFromFactura3", false);
        }

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
        if(!backFromFactura3){
            Intent i = new Intent(this, Factura1.class);
            i.putExtra("var_adr", cusTesting.getAddress());
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_fechaFactura", invoice.getInv_date());
            i.putExtra("var_backFromFactura3", false);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "No se puede volver atrás. Finalice la factura, por favor.", Toast.LENGTH_LONG).show();
        }
    }
}
