package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.model.HojaRuta;
import sqlite.model.Trip;

public class EditarHojaRuta2 extends AppCompatActivity {

    private String lugarInicio = null;
    private String inicio = null;
    private String destino = null;
    private String pasajeros = null;
    private String observaciones = null;
    private String hojaRutaId;
    private HojaRuta hr = null;
    private Trip trip = null;

    private EditText numHojaRuta;

    private TextView fechaInicio, fechaFin, horaInicio, horaFin;
    private Button pickDateIni, pickDateFin, pickHoraIni, pickHoraFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_hoja_ruta2);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editHojaRuta2);
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        lugarInicio = formulario.getString("var_lugarInicio");
        inicio = formulario.getString("var_inicio");
        destino = formulario.getString("var_destino");
        pasajeros = formulario.getString("var_pasajeros");
        observaciones = formulario.getString("var_observaciones");
        hojaRutaId = formulario.getString("hojaRutaId");

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        hr = hrm.getHojaRuta(hojaRutaId, this);
        trip = hrm.getTrip(hr.getHjr_trp_id(), this);

        numHojaRuta = (EditText) findViewById(R.id.numberHojaRutaEditarHR);

        fechaInicio = (TextView) findViewById(R.id.mostrarFechaInicioHojaRutaEditarHR);
        fechaInicio.requestFocus();
        fechaFin = (TextView) findViewById(R.id.mostrarFechaFinHojaRutaEditarHR);
        horaInicio = (TextView) findViewById(R.id.mostrarHoraInicioHojaRutaEditarHR);
        horaFin = (TextView) findViewById(R.id.mostrarHoraFinHojaRutaEditarHR);

        numHojaRuta.setText(hr.getHjr_number());
        fechaInicio.setText(hr.getHjr_date_inicio());
        fechaFin.setText(hr.getHjr_date_fin());
        horaInicio.setText(hr.getHjr_time_inicio());
        horaFin.setText(hr.getHjr_time_fin());

        pickDateIni = (Button) findViewById(R.id.selecFechaInicioHojaRutaEditarHR);
        pickDateFin = (Button) findViewById(R.id.selecFechaFinHojaRutaEditarHR);
        pickHoraIni = (Button) findViewById(R.id.selecHoraInicioHojaRutaEditarHR);
        pickHoraFin = (Button) findViewById(R.id.selecHoraFinHojaRutaEditarHR);

        pickDateIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(EditarHojaRuta2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        fechaInicio.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        pickDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(EditarHojaRuta2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        fechaFin.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        pickHoraIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditarHojaRuta2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = setCorrectTime(selectedHour, selectedMinute);
                        horaInicio.setText(time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        pickHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditarHojaRuta2.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = setCorrectTime(selectedHour, selectedMinute);
                        horaFin.setText(time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    private String setCorrectTime(int hour, int minutes){
        String result = null;
        String hourResult = null;
        if(hour == 0){
            hourResult = "0" + String.valueOf(hour);
        }
        else{
            hourResult = String.valueOf(hour);
        }
        if(minutes < 10){
            result = hourResult + ":" + "0" + String.valueOf(minutes);
        }
        else{
            result = hourResult + ":" + minutes;
        }

        return result;
    }

    public void goToEditarHojaRuta3(View v){
        CommonMethods cm = new CommonMethodsImplementation();
        String startDate = fechaInicio.getText().toString();
        String numeroHr = numHojaRuta.getText().toString();
        String endDate = fechaFin.getText().toString();
        int compararFechas = 0;

        try{
            if((startDate != null && TextUtils.isEmpty(startDate)) && (endDate != null && TextUtils.isEmpty(endDate))){
                compararFechas = cm.compararFechas(startDate, endDate);
            }
        }
        catch (ParseException pe){
            Log.e("goToCrearHojaRuta3",
                    "Error al comparar las fechas de la hoja de ruta: inicio -> '" + startDate + "', fin-> '" + endDate + "'",
                    pe);
        }
        if(startDate != null && !TextUtils.isEmpty(startDate)){
            if(compararFechas <= 0){
                HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                if(numeroHr != null && !TextUtils.isEmpty(numeroHr)){
                    hr.setHjr_number(numeroHr);
                }
                else{
                    int numHojasRutaPorMesAnyo = hrm.getCountNumHojaRuta(fechaInicio.getText().toString(), this);
                    String numeroHojaRuta = hrm.crearNumHojaRuta(numHojasRutaPorMesAnyo, startDate);
                    hr.setHjr_number(numeroHojaRuta);
                }
                hr.setHjr_pasajeros(pasajeros.toUpperCase());
                hr.setHjr_observaciones(observaciones.toUpperCase());
                hr.setHjr_date_inicio(fechaInicio.getText().toString());
                hr.setHjr_date_fin(fechaFin.getText().toString());
                hr.setHjr_time_inicio(horaInicio.getText().toString());
                hr.setHjr_time_fin(horaFin.getText().toString());

                trip.setTrp_start(inicio.toUpperCase());
                trip.setTrp_finish(destino.toUpperCase());
                trip.setTrp_start_place(lugarInicio.toUpperCase());

                hrm.updateTrip(trip, this);
                hrm.updateHojaRuta(hr, this);

                //Primero borramos el PDF
                cm.borrarPdf(hr.getHjr_pathPdf());
                //Despues generamos
                hrm.generatePDF(hojaRutaId, hr, this);
                Toast.makeText(this, "Hoja de ruta " + hr.getHjr_alias() + " actualizada.", Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, com.example.usuario.testingbd.HojaRuta.class);
                i.putExtra("contratoId", hr.getHjr_con_id());
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "La fecha inicio ha de ser menor que la de fin", Toast.LENGTH_LONG).show();
            }
        }
        else{
            fechaInicio.setError("Seleccione una fecha para continuar.");
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
        Toast.makeText(this, "Finalice la hoja de ruta, por favor.", Toast.LENGTH_LONG).show();
    }

}
