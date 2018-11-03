package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class EditarContrato2 extends AppCompatActivity {

    private EditText matVehiculo, numContrato;
    private TextView fechaContrato, tvFecha, tvEditarContrato;
    private Button actualizar, selecFecha;
    private String contratoId = null;
    private String aliasContrato;
    private String nombreArrendador = null;
    private String nifArrendador = null;
    private String matriculaVehiculo = null;
    private String numeroContrato = null;
    private String fechaCont = null;
    private boolean isTdaArrendador;

    private Contrato contrato = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contrato2);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editContrato2);
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        aliasContrato = formulario.getString("aliasContrato");
        nombreArrendador = formulario.getString("nombreArrendador");
        nifArrendador = formulario.getString("nifArrendador");
        matriculaVehiculo = formulario.getString("matriculaVehiculo");
        contratoId = formulario.getString("contratoId");
        isTdaArrendador = formulario.getBoolean("isTdaArrendador");

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        contrato = hrm.getContrato(contratoId, this);

        matVehiculo = (EditText) findViewById(R.id.matriculaVehiculoEditar);
        actualizar = (Button) findViewById(R.id.button_actualizarContrato);

        tvFecha = (TextView) findViewById(R.id.textViewFechaEditarContrato);
        fechaContrato = (TextView) findViewById(R.id.mostrarFechaContratoEditar);

        tvEditarContrato = (TextView) findViewById(R.id.textViewEditarNumContrato);
        numContrato = (EditText) findViewById(R.id.numContratoEditar);

        selecFecha = (Button) findViewById(R.id.selecFechaEditarContrato);
        selecFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(EditarContrato2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        fechaContrato.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        if(!isTdaArrendador){
            tvEditarContrato.setVisibility(View.VISIBLE);
            tvFecha.setVisibility(View.VISIBLE);
            fechaContrato.setVisibility(View.VISIBLE);
            numContrato.setVisibility(View.VISIBLE);
            selecFecha.setVisibility(View.VISIBLE);

            numeroContrato = contrato.getCon_number();
            fechaCont = contrato.getCon_date();
            numContrato.setText(numeroContrato);
            fechaContrato.setText(fechaCont);
        }
        else{
            tvEditarContrato.setVisibility(View.GONE);
            tvFecha.setVisibility(View.GONE);
            fechaContrato.setVisibility(View.GONE);
            numContrato.setVisibility(View.GONE);
            selecFecha.setVisibility(View.GONE);
        }
        matVehiculo.setText(matriculaVehiculo);
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
        Intent i = new Intent(this, EditarContrato.class);
        i.putExtra("contratoId", contratoId);
        startActivity(i);
        finish();
    }

    public void actualizarContrato(View v) {  //Evento onClick de los botones{
        String matVeh = matVehiculo.getText().toString();
        String numCon = null;
        String fecha = null;
        if(!isTdaArrendador){
            numCon = numContrato.getText().toString();
            fecha = fechaContrato.getText().toString();
        }

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();

        if( !errorControl(fecha, numCon) ){
            Contrato contrato = hrm.getContrato(contratoId, this);
            contrato.setCon_matricula_vehiculo(matVeh);
            if(!isTdaArrendador){
                contrato.setCon_date(fecha);
                contrato.setCon_number(numCon);
            }
            contrato.setCon_alias(aliasContrato);
            contrato.setCon_arrendador(nombreArrendador);
            contrato.setCon_nif_arrendador(nifArrendador);
            //contrato.setCon_arrendatario(nombreArrendatario);
            //contrato.setCon_arrendatario_nifCif(nifArrendatario);
            hrm.updateContrato(contrato, this);

            Toast.makeText(this, R.string.contratoUpdated, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, VistaContratos.class);
            startActivity(i);
            finish();
        }
    }

    private boolean errorControl(String date, String number) {
        if(!isTdaArrendador){
            fechaContrato.setError(null);
            numContrato.setError(null);

            if(number.isEmpty() || number == null){
                numContrato.setError("Por favor introduzca un número de contrato");
            }
            if(date.isEmpty() || date == null){
                fechaContrato.setError("Por favor introduzca una fecha.");
            }

            if(fechaContrato.getError() != null || numContrato.getError() != null){
                return true;  //hay errores
            }
            else{
                return checkIfExists(number, contrato.getCon_number()); // return true si Hay errores o false si no.
            }
        }
        else{
            return false;
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
                                numContrato.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                                break;
                            } else {
                                numContrato.setError(null);
                            }
                        } else {
                            numContrato.setError(null);
                        }
                    }
                }
                else{
                    numContrato.setError(null);
                }
            }
            else{
                numContrato.setError(null);
            }

            if(numContrato.getError() != null){
                return true; // hay errores
            }
            else{
                ArrayList<sqlite.model.Arrendatario> ards = hrm.getAllArrendatarios(this);
                if(ards != null){
                    if(ards.size() > 0){
                        for(Arrendatario arrendatario : ards){
                            if(arrendatario.getArd_number() != null){
                                if(arrendatario.getArd_number().equalsIgnoreCase(newNumber)){
                                    numContrato.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                                    break;
                                }
                                else{
                                    numContrato.setError(null);
                                }
                            }
                            else{
                                numContrato.setError(null);
                            }
                        }
                    }
                    else{
                        numContrato.setError(null);
                    }
                }
                else{
                    numContrato.setError(null);
                }
            }

            if(numContrato.getError() != null){
                return true;
            }
            else{
                return false;
            }
        }
    }
}
