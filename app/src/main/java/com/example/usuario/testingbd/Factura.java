package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.model.Taxi_Driver;

public class Factura extends AppCompatActivity {

    private TextView mostrarFechaFactura;
    private EditText dirFactura, numDirFactura, poblacionFactura, provinciaFactura;
    private EditText codigoPostalFactura, paisFactura;
    private Button pickDate;

    private String tdaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_factura);
        act.setDisplayHomeAsUpEnabled(true);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        dirFactura = (EditText) findViewById(R.id.direccionCustomer);  //dividir direccion
        numDirFactura = (EditText) findViewById(R.id.dirNumber);
        poblacionFactura = (EditText) findViewById(R.id.dirMunicipio);
        provinciaFactura = (EditText) findViewById(R.id.dirProvincia);
        paisFactura = (EditText) findViewById(R.id.dirPais);
        codigoPostalFactura = (EditText) findViewById(R.id.dirCodigoPostal);
        pickDate = (Button) findViewById(R.id.selecFecha1);
        mostrarFechaFactura = (TextView) findViewById(R.id.mostrarFechaFact);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(Factura.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        mostrarFechaFactura.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        //Bundle formulario = this.getIntent().getExtras();
        //tdaId = formulario.getString("tdaId");
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Intent i = new Intent(this, MenuPrincipal.class);
        //startActivity(i);
        finish();
    }

    public void goToFactura1(View v){
        boolean fechaOk = false;
        if(TextUtils.isEmpty(mostrarFechaFactura.getText().toString()) || mostrarFechaFactura.getText().toString() == null){
            fechaOk = false;
            mostrarFechaFactura.setError("Seleccione una fecha");
        }
        else{
            fechaOk = true;
            mostrarFechaFactura.setError(null);
        }

        if(fechaOk){
            Intent i = new Intent(this, Factura1.class);
            AddressTesting adr = new AddressTesting();
            adr.setStreet(dirFactura.getText().toString());
            adr.setNumber(numDirFactura.getText().toString());
            adr.setPostcode(codigoPostalFactura.getText().toString());
            adr.setTown(poblacionFactura.getText().toString());
            adr.setCounty(provinciaFactura.getText().toString());
            adr.setCountry(paisFactura.getText().toString());
            //i.putExtra("customerAddressMap", customerAddressMap);
            i.putExtra("var_adr", adr);
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_fechaFactura", mostrarFechaFactura.getText().toString());
            i.putExtra("var_backFromFactura3", false);

            startActivity(i);
            finish();
        }
    }
}
