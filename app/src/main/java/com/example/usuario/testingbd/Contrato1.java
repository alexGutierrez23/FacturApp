package com.example.usuario.testingbd;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.Taxi_driver_account;

public class Contrato1 extends AppCompatActivity {

    private int day, month, year;
    private static final int DATE_DIALOG_ID = 0;   //Para identificar el datePickerDialog
    private Button pickDate, createContract;
    private EditText number, matriculaVehiculo;
    private EditText arrendador, nifArrendador;
    private CheckBox cbContrato;
    private String fecha;
    private TextView dateDisplay;

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener(){
                public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth){
                    year = year1;
                    month = monthOfYear;
                    day = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrato);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_crearContrato);
        act.setDisplayHomeAsUpEnabled(true);

        pickDate = (Button) findViewById(R.id.selecFechaContrato);
        number = (EditText) findViewById(R.id.numberContrato);
        matriculaVehiculo = (EditText) findViewById(R.id.matriculaVehiculo);
        arrendador = (EditText) findViewById(R.id.nombreArrendadorContrato);
        nifArrendador = (EditText) findViewById(R.id.nifArrendadorContrato);
        cbContrato = (CheckBox) findViewById(R.id.checkBoxContrato);

        dateDisplay = (TextView) findViewById(R.id.mostrarFechaContrato);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        //get the current date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //Mostrar la fecha actual
        //updateDisplay();

     /*   if(cbContrato.isChecked()){
            nifArrendador.setVisibility(View.GONE);
            arrendador.setVisibility(View.GONE);
            dateDisplay.setVisibility(View.GONE);
            pickDate.setVisibility(View.GONE);
        }
        else{
            nifArrendador.setVisibility(View.VISIBLE);
            arrendador.setVisibility(View.VISIBLE);
            dateDisplay.setVisibility(View.VISIBLE);
            pickDate.setVisibility(View.VISIBLE);
        }  */
    }

    public void onCheckBoxSelected(View v){
        boolean opcionSeleccionada = ((CheckBox) v).isChecked();
        if(opcionSeleccionada){
            nifArrendador.setVisibility(View.GONE);
            arrendador.setVisibility(View.GONE);
            dateDisplay.setVisibility(View.GONE);
            pickDate.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
        }
        else{
            nifArrendador.setVisibility(View.VISIBLE);
            arrendador.setVisibility(View.VISIBLE);
            dateDisplay.setVisibility(View.VISIBLE);
            pickDate.setVisibility(View.VISIBLE);
            number.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay(){
        dateDisplay.setText(
                new StringBuilder()
                        //Mes es 0 por lo que añadimos +1
                        .append(day ).append(" / ")
                        .append(month +1).append(" / ")
                        .append(year).append(" ")
        );
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, year, month, day);
        }
        return null;
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

    public void GenContrato(View v) {
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        CommonMethods cm = new CommonMethodsImplementation();
        ArrayList<Contrato> contratos = hrm.getContratos(this);

        if(cbContrato.isChecked()){
            String matricula = matriculaVehiculo.getText().toString();

            Contrato contrato = new Contrato();
            Taxi_driver_account tda = hrm.getTaxiDriverAccount(this);
            contrato.setCon_tda_id(String.valueOf(tda.getTda_id()));
            if(!matricula.isEmpty() && matricula != null){
                contrato.setCon_matricula_vehiculo(matricula);
            }
            contrato.setCon_arrendador(tda.getTda_name());
            contrato.setCon_nif_arrendador(tda.getTda_nif());
            int totalContratos = hrm.getNumeroTotalContratos(this);
            if(totalContratos > 0){
                totalContratos++;
                String alias = tda.getTda_name() + "_" + String.valueOf(totalContratos);
                contrato.setCon_alias(alias);
            }
            else{
                String alias = tda.getTda_name() + "_1";
                contrato.setCon_alias(alias);
            }
            contrato.setCon_isTda_arrendador("1");

            String contratoId = hrm.insertarContrato(contrato, this);
            Toast.makeText(this, "Contrato creado correctamente", Toast.LENGTH_LONG).show();
            createDialogHJR(v, contratoId);
        }
        else{
            String numberContrato = number.getText().toString();
            String fecha = dateDisplay.getText().toString();
            String matricula = matriculaVehiculo.getText().toString();
            String nombreArrendador = arrendador.getText().toString();
            String nifArrend = nifArrendador.getText().toString();

            if(!errorControl(contratos, nombreArrendador, numberContrato, fecha)){ //Si no hay errores
                Contrato contrato = new Contrato();
                contrato.setCon_number(numberContrato);
                contrato.setCon_date(fecha);
                Taxi_driver_account tda = hrm.getTaxiDriverAccount(this);
                contrato.setCon_tda_id(String.valueOf(tda.getTda_id()));
                contrato.setCon_matricula_vehiculo(matricula);
                contrato.setCon_arrendador(nombreArrendador);
                contrato.setCon_nif_arrendador(nifArrend);
                int totalContratos = hrm.getNumeroTotalContratos(this);
                if(totalContratos > 0){
                    totalContratos++;
                    String alias = nombreArrendador + "_" + String.valueOf(totalContratos);
                    contrato.setCon_alias(alias);
                }
                else{
                    String alias = nombreArrendador + "_1";
                    contrato.setCon_alias(alias);
                }
                String yeaId = hrm.insertarYearContrato(cm.takeYearFromDate(fecha), this);
                contrato.setCon_yea_id(yeaId);

                String contratoId = hrm.insertarContrato(contrato, this);
                Toast.makeText(this, "Contrato creado correctamente", Toast.LENGTH_LONG).show();
                createDialogHJR(v, contratoId);
            }

            /*
            Fragment cf = new Fragment();
            getFragmentManager().beginTransaction().replace(R.id.appBarMainLayout, cf)
                .addToBackStack(null)
                .commit();
            */
        }
    }

    private boolean errorControl(ArrayList<Contrato> contratos, String ard, String num, String date) {
        if(ard.isEmpty() || ard == null){
            arrendador.setError("Escriba un nombre para el arrendador, por favor.");
        }
        if(num.isEmpty() || num == null){
            number.setError("Inserte un número de contrato");
        }
        if(date.isEmpty() || date == null){
            dateDisplay.setError("Por favor introduzca una fecha.");
        }

        if(dateDisplay.getError() != null || number.getError() != null || arrendador.getError() != null){
            return true;  //hay errores
        }
        else{
            return checkIfExists(contratos, ard, num); // return true si Hay errores o false si no.
        }
    }

    private boolean checkIfExists(ArrayList<Contrato> contratos, String ard, String num){
        if(contratos != null){
            if(contratos.size() > 0) {
                for (Contrato c : contratos) {
                    if (c.getCon_number() != null) {
                        if (c.getCon_number().equalsIgnoreCase(num)) {
                            number.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                            break;
                        } else {
                            number.setError(null);
                        }
                    } else {
                        number.setError(null);
                    }
                }
            }
            else{
                number.setError(null);
            }
        }
        else{
            number.setError(null);
        }

        if(number.getError() != null){
            return true; // hay errores
        }
        else{
            HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
            ArrayList<sqlite.model.Arrendatario> ards = hrm.getAllArrendatarios(this);
            if(ards != null){
                if(ards.size() > 0){
                    for(Arrendatario arrendatario : ards){
                        if(arrendatario.getArd_number() != null){
                            if(arrendatario.getArd_number().equalsIgnoreCase(num)){
                                number.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    public void createDialogHJR(View v, String contratoId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String conId = contratoId;
        builder.setTitle("Contrato");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("¿Quiere crear la hoja de ruta?")
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), CrearArrendatario.class);
                        i.putExtra("contratoId", conId);
                        finish();
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), VistaContratos.class);
                        finish();
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
