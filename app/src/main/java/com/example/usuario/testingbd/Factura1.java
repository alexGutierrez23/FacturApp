package com.example.usuario.testingbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import appImplementations.CommonMethodsImplementation;
import appImplementations.InvoiceMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import sqlite.model.Invoice;

public class Factura1 extends AppCompatActivity {

    private Button btnDatosViajeFact1;
    private EditText nombreFactura, nifFactura;
    private EditText emailFactura;

    private HashMap<String, String> customerAddressMap = null;
    private String tdaId = null;
    private String fechaFactura = null;
    private AddressTesting adr = null;
    private boolean backFromFactura3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura1);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_factura1);
        act.setDisplayHomeAsUpEnabled(true);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        nombreFactura = (EditText) findViewById(R.id.nombreFactura);
        nifFactura = (EditText) findViewById(R.id.nifFactura);
        emailFactura = (EditText) findViewById(R.id.emailFactura1);
        btnDatosViajeFact1 = (Button) findViewById(R.id.login);

        Intent i = getIntent();
        //customerAddressMap = (HashMap<String, String>) i.getSerializableExtra("customerAddressMap");
        Bundle formulario = this.getIntent().getExtras();
        tdaId = formulario.getString("tdaId");
        adr = (AddressTesting) i.getSerializableExtra("var_adr");
        fechaFactura = formulario.getString("var_fechaFactura");
        backFromFactura3 = formulario.getBoolean("var_backFromFactura3");
    }

    public void goToDatosViaje(View v){
        Intent i = new Intent(this, Factura2.class);

        String name = nombreFactura.getText().toString();
        String nif = nifFactura.getText().toString();
        String emailCompFactura = emailFactura.getText().toString();
        boolean emailOk = true;

        if(!TextUtils.isEmpty(emailCompFactura)){
            if(!Patterns.EMAIL_ADDRESS.matcher(emailCompFactura).matches()){
                emailFactura.setError(null);
                emailFactura.setError(getString(R.string.validEmail));
                emailOk = false;
            }
            else{
                emailFactura.setError(null);
                emailOk = true;
            }
        }

        if(emailOk){
            CommonMethods cm = new CommonMethodsImplementation();
            InvoiceMethods invoiceMethods = new InvoiceMethodsImplementation();

            CustomerTesting customerTesting = new CustomerTesting();
            customerTesting.setName(name);
            customerTesting.setAddress(adr);
            customerTesting.setNif(nif);
            customerTesting.setEmail(emailCompFactura);

            Invoice invoice = new Invoice();
            invoice.setInv_date(fechaFactura);
            invoice.setInv_time(cm.getHourPhone());
            //String invoiceId = invoiceMethods.createInvoice(this, invoice, tdaId, null, null, null);

            //i.putExtra("var_invoiceId", invoiceId);
            i.putExtra("var_invoice", invoice);
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_customerTesting", customerTesting);
            i.putExtra("var_backFromFactura3", false);

            startActivity(i);
            finish();
        }
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
        Intent i = new Intent(this, Factura.class);
        startActivity(i);
        finish();
    }

}
