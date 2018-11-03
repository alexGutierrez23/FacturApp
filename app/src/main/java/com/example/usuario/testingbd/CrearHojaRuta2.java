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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.model.*;

public class CrearHojaRuta2 extends AppCompatActivity {
    private String lugarInicio = null;
    private String inicio = null;
    private String destino = null;
    private String pasajeros = null;
    private String observaciones = null;
    private String contratoId = null;
    private String arrendatarioId = null;

    private TextView fechaInicio, fechaFin, horaInicio, horaFin;
    private Button pickDateIni, pickDateFin, pickHoraIni, pickHoraFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_hoja_ruta2);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_createHojaRuta2);
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        lugarInicio = formulario.getString("var_lugarInicio");
        inicio = formulario.getString("var_inicio");
        destino = formulario.getString("var_destino");
        pasajeros = formulario.getString("var_pasajeros");
        observaciones = formulario.getString("var_observaciones");
        contratoId = formulario.getString("contratoId");
        arrendatarioId = formulario.getString("arrendatarioId");

        fechaInicio = (TextView) findViewById(R.id.mostrarFechaInicioHojaRuta);
        fechaInicio.requestFocus();
        fechaFin = (TextView) findViewById(R.id.mostrarFechaFinHojaRuta);
        horaInicio = (TextView) findViewById(R.id.mostrarHoraInicioHojaRuta);
        horaFin = (TextView) findViewById(R.id.mostrarHoraFinHojaRuta);

        pickDateIni = (Button) findViewById(R.id.selecFechaInicioHojaRuta);
        pickDateFin = (Button) findViewById(R.id.selecFechaFinHojaRuta);
        pickHoraIni = (Button) findViewById(R.id.selecHoraInicioHojaRuta);
        pickHoraFin = (Button) findViewById(R.id.selecHoraFinHojaRuta);

        Date dateInicio = null;
        Date dateFin = null;

        pickDateIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(CrearHojaRuta2.this, new DatePickerDialog.OnDateSetListener() {
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
                mdatePicker = new DatePickerDialog(CrearHojaRuta2.this, new DatePickerDialog.OnDateSetListener() {
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
                mTimePicker = new TimePickerDialog(CrearHojaRuta2.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(CrearHojaRuta2.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void goToCrearHojaRuta3(View v){
        CommonMethods cm = new CommonMethodsImplementation();
        String startDate = fechaInicio.getText().toString();
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
                fechaInicio.setError(null);
                HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                int numHojasRutaPorMesAnyo = hrm.getCountNumHojaRuta(fechaInicio.getText().toString(), this);

                sqlite.model.HojaRuta hr = new sqlite.model.HojaRuta();
                String numeroHojaRuta = hrm.crearNumHojaRuta(numHojasRutaPorMesAnyo, startDate);
                hr.setHjr_number(numeroHojaRuta);
                hr.setHjr_pasajeros(pasajeros.toUpperCase());
                hr.setHjr_observaciones(observaciones.toUpperCase());
                hr.setHjr_date_inicio(fechaInicio.getText().toString());
                hr.setHjr_date_fin(fechaFin.getText().toString());
                hr.setHjr_time_inicio(horaInicio.getText().toString());
                hr.setHjr_time_fin(horaFin.getText().toString());
                hr.setHjr_con_id(contratoId);
                hr.setHjr_ard_id(arrendatarioId);
                sqlite.model.Arrendatario ard = hrm.getArrendatario(arrendatarioId, this);
                int totalHojasRuta = hrm.getNumeroTotalHojasRuta(this);
                if(totalHojasRuta > 0){
                    totalHojasRuta = totalHojasRuta + 1;
                    String alias = ard.getArd_name() + "_" + String.valueOf(totalHojasRuta);
                    hr.setHjr_alias(alias);
                }
                else{
                    String alias = ard.getArd_name() + "_1";
                    hr.setHjr_alias(alias);
                }
                Trip trip = new Trip();
                trip.setTrp_start(inicio.toUpperCase());
                trip.setTrp_finish(destino.toUpperCase());
                trip.setTrp_start_place(lugarInicio.toUpperCase());
                String trp_id = hrm.crearTrip(trip, this);
                hr.setHjr_trp_id(trp_id);

                String hr_id = hrm.crearHojaRuta(hr, this);
                Intent i = new Intent(this, CrearHojaRuta3.class);

                i.putExtra("var_hojaRutaId", hr_id);

                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "Fecha inicio ha de ser menor que la de fin", Toast.LENGTH_LONG).show();
            }
        }
        else{
            fechaInicio.setError("Seleccione una fecha para continuar.");
        }
    }
}
