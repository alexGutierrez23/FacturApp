package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.model.Taxi_Driver;

public class Presupuesto extends AppCompatActivity {

    private EditText dirPresupuesto, poblacionPresupuesto, provinciaPresupuesto;
    private EditText codigoPostalPresupuesto, paisPresupuesto, mostrarFechaPresupuesto;
    private Button pickDate;

    private String tdaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_presupuesto);
        act.setDisplayHomeAsUpEnabled(true);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        dirPresupuesto = (EditText) findViewById(R.id.direccionCustPresupuesto);  //dividir direccion
        poblacionPresupuesto = (EditText) findViewById(R.id.dirMunicipioPresupuesto);
        provinciaPresupuesto = (EditText) findViewById(R.id.dirProvinciaPresupuesto);
        paisPresupuesto = (EditText) findViewById(R.id.dirPaisPresupuesto);
        codigoPostalPresupuesto = (EditText) findViewById(R.id.dirCodigoPostalPresupuesto);
        pickDate = (Button) findViewById(R.id.selecFecha1);
        mostrarFechaPresupuesto = (EditText) findViewById(R.id.mostrarFechaPresupuesto);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(Presupuesto.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        mostrarFechaPresupuesto.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
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
        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
        finish();
    }

    public void goToPresupuesto1(View v){
        boolean fechaOk = false;
        if(TextUtils.isEmpty(mostrarFechaPresupuesto.getText().toString()) || mostrarFechaPresupuesto.getText().toString() == null){
            fechaOk = false;
            mostrarFechaPresupuesto.setError("Seleccione una fecha");
        }
        else{
            fechaOk = true;
            mostrarFechaPresupuesto.setError(null);
        }

        if(fechaOk){
            Intent i = new Intent(this, Presupuesto1.class);

            AddressTesting adr = new AddressTesting();
            adr.setStreet(dirPresupuesto.getText().toString());
            adr.setPostcode(codigoPostalPresupuesto.getText().toString());
            adr.setTown(poblacionPresupuesto.getText().toString());
            adr.setCounty(provinciaPresupuesto.getText().toString());
            adr.setCountry(paisPresupuesto.getText().toString());

            i.putExtra("var_adr", adr);
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_fechaPresupuesto", mostrarFechaPresupuesto.getText().toString());
            Log.e("Fecha PRE: ", mostrarFechaPresupuesto.getText().toString());
            i.putExtra("var_backFromPresupuesto3", false);

            startActivity(i);
            finish();
        }
    }
}
