package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;

public class EditarArrendatario extends AppCompatActivity {

    private Contrato contrato = null;
    private Arrendatario arrendatario = null;
    private sqlite.model.HojaRuta hojaRuta = null;

    private TextView mostrarEditarArdFecha, tvEditarArd, tvEditarNumberArd, tvEditarNifArd;
    private EditText nombreArrendatario, nifArrendatario, numberContrato;
    private Button editArrendatario, selecEditarFechaArd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_arrendatario);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editArrendatario);
        act.setDisplayHomeAsUpEnabled(true);

        tvEditarArd = (TextView) findViewById(R.id.tvEditarArd);
        tvEditarNumberArd = (TextView) findViewById(R.id.tvEditarNumberArd);
        tvEditarNifArd = (TextView) findViewById(R.id.tvEditarNifArd);

        nombreArrendatario = (EditText) findViewById(R.id.nombreEditarArrendatario);
        nifArrendatario = (EditText) findViewById(R.id.nifEditarArrendatario);
        numberContrato = (EditText) findViewById(R.id.numberContratoEditarArd);
        editArrendatario = (Button) findViewById(R.id.button_editarArrendatario);

        Bundle formulario = this.getIntent().getExtras();
        String contratoId = formulario.getString("contratoId");
        String arrendatarioId = formulario.getString("arrendatarioId");
        String hojaRutaId = formulario.getString("hojaRutaId");

        selecEditarFechaArd = (Button) findViewById(R.id.selecFechaEditarArd);
        mostrarEditarArdFecha = (TextView) findViewById(R.id.mostrarFechaEditarArd);

        selecEditarFechaArd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(EditarArrendatario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        mostrarEditarArdFecha.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        contrato = hrm.getContrato(contratoId, this);
        arrendatario = hrm.getArrendatario(arrendatarioId, this);
        hojaRuta = hrm.getHojaRuta(hojaRutaId, this);

        boolean isTdaArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());
        enableDisableFields(isTdaArrendador);
        fillFields(isTdaArrendador, arrendatario);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void enableDisableFields(boolean isTdaArrendador){
        if(isTdaArrendador){
            tvEditarArd.setVisibility(View.VISIBLE);
            tvEditarNumberArd.setVisibility(View.VISIBLE);
            tvEditarNifArd.setVisibility(View.VISIBLE);

            nombreArrendatario.setVisibility(View.VISIBLE);
            nifArrendatario.setVisibility(View.VISIBLE);
            numberContrato.setVisibility(View.VISIBLE);
            editArrendatario.setVisibility(View.VISIBLE);

            selecEditarFechaArd.setVisibility(View.VISIBLE);
            mostrarEditarArdFecha.setVisibility(View.VISIBLE);
        }
        else{ // Si TDA no es arrendador, no tiene fecha ni numero
            tvEditarNumberArd.setVisibility(View.GONE);
            numberContrato.setVisibility(View.GONE);
            selecEditarFechaArd.setVisibility(View.GONE);
            mostrarEditarArdFecha.setVisibility(View.GONE);

            tvEditarArd.setVisibility(View.VISIBLE);
            tvEditarNifArd.setVisibility(View.VISIBLE);
            nombreArrendatario.setVisibility(View.VISIBLE);
            nifArrendatario.setVisibility(View.VISIBLE);

            editArrendatario.setVisibility(View.VISIBLE);
        }
    }

    public void fillFields(boolean isTdaArrendador, Arrendatario arrendatario){
        nombreArrendatario.setText(arrendatario.getArd_name());
        nifArrendatario.setText(arrendatario.getArd_nifCif());
        if(isTdaArrendador){
            mostrarEditarArdFecha.setText(arrendatario.getArd_fecha());
            numberContrato.setText(arrendatario.getArd_number());
        }
    }

    public void actualizarArrendatario(View view){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        boolean isTdaArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());
        String nombre = null;
        String nifCif = null;
        String numero = null;
        String fecha = null;
        if(isTdaArrendador){
            nombre = nombreArrendatario.getText().toString();
            nifCif = nifArrendatario.getText().toString();
            numero = numberContrato.getText().toString();
            fecha = mostrarEditarArdFecha.getText().toString();
            boolean hayErrores = errorControl(isTdaArrendador, fecha, numero, nombre, nifCif);
            if(!hayErrores){
                arrendatario.setArd_name(nombre.toUpperCase());
                arrendatario.setArd_nifCif(nifCif);
                arrendatario.setArd_number(numero);
                arrendatario.setArd_fecha(fecha);
                hrm.updateArrendatario(arrendatario, this); //revisar la actualizacion
                if(!hojaRuta.getHjr_alias().isEmpty() && hojaRuta.getHjr_alias() != null){
                    String delim2 = "_";  //File.separator;
                    String[] getArdFromAlias = hojaRuta.getHjr_alias().split(delim2);
                    hojaRuta.setHjr_alias(arrendatario.getArd_name().toUpperCase() + "_" + getArdFromAlias[1]);
                    hrm.updateHojaRuta(hojaRuta, this);
                }
                finish();
                Toast.makeText(this, "Arrendatario actualizado", Toast.LENGTH_LONG).show();
            }
        }
        else{
            nombre = nombreArrendatario.getText().toString();
            nifCif = nifArrendatario.getText().toString();
            boolean hayErrores = errorControl(isTdaArrendador, null, null, nombre, nifCif);
            if(!hayErrores){
                arrendatario.setArd_name(nombre.toUpperCase());
                arrendatario.setArd_nifCif(nifCif);
                arrendatario.setArd_number(null);
                arrendatario.setArd_fecha(null);
                hrm.updateArrendatario(arrendatario, this);

                if(!hojaRuta.getHjr_alias().isEmpty() && hojaRuta.getHjr_alias() != null){
                    String delim2 = "_";  //File.separator;
                    String[] getArdFromAlias = hojaRuta.getHjr_alias().split(delim2);
                    hojaRuta.setHjr_alias(arrendatario.getArd_name().toUpperCase() + "_" + getArdFromAlias[1]);
                    hrm.updateHojaRuta(hojaRuta, this);
                }
                finish();
                Toast.makeText(this, "Arrendatario actualizado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean errorControl(boolean isTdaArrendador, String date, String number, String ardName, String nifArd) {
        if(isTdaArrendador){
            mostrarEditarArdFecha.setError(null);
            numberContrato.setError(null);
            nombreArrendatario.setError(null);
            nifArrendatario.setError(null);

            if(ardName.isEmpty() || ardName == null){
                nombreArrendatario.setError("Por favor introduzca un nombre para el arrendatario");
            }
            if(number.isEmpty() || number == null){
                numberContrato.setError("Por favor introduzca un número de contrato");
            }
            if(date.isEmpty() || date == null){
                mostrarEditarArdFecha.setError("Por favor introduzca una fecha.");
            }
            if(nifArd.isEmpty() || nifArd == null){
                nifArrendatario.setError("Por favor introduzca un NIF");
            }

            if(mostrarEditarArdFecha.getError() != null || numberContrato.getError() != null
                    || nombreArrendatario.getError() != null || nifArrendatario.getError() != null){
                return true;  //hay errores
            }
            else{
                return checkIfExists(number, arrendatario.getArd_number()); // return true si Hay errores o false si no.
            }
        }
        else{
            nombreArrendatario.setError(null);
            nifArrendatario.setError(null);

            if(ardName.isEmpty() || ardName == null){
                nombreArrendatario.setError("Por favor introduzca un nombre para el arrendatario");
            }
            if(nifArd.isEmpty() || nifArd == null){
                nifArrendatario.setError("Por favor introduzca un NIF");
            }

            if(nombreArrendatario.getError() != null || nifArrendatario.getError() != null){
                return true;  //hay errores
            }
            else{
                return false;
            }
        }
    }

    private boolean checkIfExists(String newNumber, String oldNumber){
        if(newNumber.equalsIgnoreCase(oldNumber)){
            return false;
        }
        else{
            HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
            ArrayList<Contrato> contratos = hrm.getContratos(this);
            if(contratos != null){
                if(contratos.size() > 0) {
                    for (Contrato c : contratos) {
                        if (c.getCon_number() != null) {
                            if (c.getCon_number().equalsIgnoreCase(newNumber)) {
                                numberContrato.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                                break;
                            } else {
                                numberContrato.setError(null);
                            }
                        } else {
                            numberContrato.setError(null);
                        }
                    }
                }
                else{
                    numberContrato.setError(null);
                }
            }
            else{
                numberContrato.setError(null);
            }

            if(numberContrato.getError() != null){
                return true; // hay errores
            }
            else{
                ArrayList<sqlite.model.Arrendatario> ards = hrm.getAllArrendatarios(this);
                if(ards != null){
                    if(ards.size() > 0){
                        for(Arrendatario arrendatario : ards){
                            if(arrendatario.getArd_number() != null){
                                if(arrendatario.getArd_number().equalsIgnoreCase(newNumber)){
                                    numberContrato.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                                    break;
                                }
                                else{
                                    numberContrato.setError(null);
                                }
                            }
                            else{
                                numberContrato.setError(null);
                            }
                        }
                    }
                    else{
                        numberContrato.setError(null);
                    }
                }
                else{
                    numberContrato.setError(null);
                }
            }

            if(numberContrato.getError() != null){
                return true;
            }
            else{
                return false;
            }
        }
    }

}
