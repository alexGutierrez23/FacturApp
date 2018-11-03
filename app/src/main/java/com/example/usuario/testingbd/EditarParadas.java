package com.example.usuario.testingbd;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Contrato;
import sqlite.model.Stop;

public class EditarParadas extends AppCompatActivity {

    private sqlite.model.HojaRuta hojaRuta = null;
    private ArrayList<Stop> stops = null;
    private int stopId;

    private EditText editarParadasEditText;
    private TextView editarParadasTv;
    private Button editarParadas, anyadirParadas, borrarParada;
    private RelativeLayout editarParadasLayout, anyadirParadasLayout, spinnerParadasLayout;
    private RadioButton addStopRB, editStopRB, removeStopRB;
    private RadioGroup addStopsRG;

    private Spinner spinnerStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_paradas);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_editStops);
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        String hojaRutaId = formulario.getString("hojaRutaId");

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        hojaRuta = hrm.getHojaRuta(hojaRutaId, this);
        stops = hrm.getStopsByHjrId(hojaRutaId, this);

        addStopsRG = (RadioGroup) findViewById(R.id.radioGroupStops);
        addStopRB = (RadioButton) findViewById(R.id.radioButtonAddStop);
        editStopRB = (RadioButton) findViewById(R.id.radioButtonEditStop);
        removeStopRB = (RadioButton) findViewById(R.id.radioButtonRemoveStop);

        editarParadasLayout = (RelativeLayout) findViewById(R.id.editarParadasLayout);
        editarParadasTv = (TextView) findViewById(R.id.tvEditarParadas);
        editarParadasEditText = (EditText) findViewById(R.id.nombreEditarParada);
        editarParadas = (Button) findViewById(R.id.button_editarParadas);
        anyadirParadas = (Button) findViewById(R.id.button_addParadas);
        borrarParada = (Button) findViewById(R.id.button_removeParada);

        spinnerParadasLayout = (RelativeLayout) findViewById(R.id.spinnerParadasLayout);
        spinnerStop = (Spinner) spinnerParadasLayout.findViewById(R.id.spinnerParadas);
        spinnerStop.getBackground().setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);

        String arrayStops[] = hrm.fillArrayStops(hojaRuta, this);
        setStuff(arrayStops);
        setSpinner(arrayStops);

        spinnerStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String parada = parentView.getItemAtPosition(position).toString();

                if(!parada.equals("Por favor, seleccione una parada...")){
                    editarParadasEditText.setText(parada);
                    for(Stop stop : stops){
                        if(stop.getStp_place().equalsIgnoreCase(parada)){
                            stopId = stop.getStp_id();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
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

    private void setStuff(String arrayStops[]){
        if(arrayStops.length == 1 || arrayStops.length == 0){
            editStopRB.setEnabled(false);
            editStopRB.setChecked(false);
            addStopRB.setChecked(true);
            addStopRB.setEnabled(false);
            removeStopRB.setEnabled(false);
            removeStopRB.setChecked(false);

            spinnerParadasLayout.setVisibility(View.GONE);
            editarParadasLayout.setVisibility(View.VISIBLE);
            anyadirParadas.setVisibility(View.VISIBLE);
            editarParadas.setVisibility(View.GONE);
            borrarParada.setVisibility(View.GONE);
        }
        else{
            editStopRB.setEnabled(true);
            editStopRB.setChecked(true);
            addStopRB.setEnabled(true);
            addStopRB.setChecked(false);
            removeStopRB.setEnabled(true);
            removeStopRB.setChecked(false);

            spinnerParadasLayout.setVisibility(View.VISIBLE);
            editarParadasLayout.setVisibility(View.VISIBLE);
            editarParadas.setVisibility(View.VISIBLE);
            anyadirParadas.setVisibility(View.GONE);
            borrarParada.setVisibility(View.GONE);
        }
    }

    public void onStopOptionsRbSelected(View v){
        boolean opcionSeleccionada = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.radioButtonAddStop:
                if(opcionSeleccionada){
                    spinnerParadasLayout.setVisibility(View.GONE);
                    editarParadasLayout.setVisibility(View.VISIBLE);
                    anyadirParadas.setVisibility(View.VISIBLE);
                    editarParadas.setVisibility(View.GONE);
                    borrarParada.setVisibility(View.GONE);
                }
                break;
            case R.id.radioButtonEditStop:
                if(opcionSeleccionada){
                    spinnerParadasLayout.setVisibility(View.VISIBLE);
                    editarParadasLayout.setVisibility(View.VISIBLE);
                    editarParadas.setVisibility(View.VISIBLE);
                    anyadirParadas.setVisibility(View.GONE);
                    borrarParada.setVisibility(View.GONE);
                }
                break;
            case R.id.radioButtonRemoveStop:
                if(opcionSeleccionada){
                    spinnerParadasLayout.setVisibility(View.VISIBLE);
                    editarParadasLayout.setVisibility(View.VISIBLE);
                    editarParadas.setVisibility(View.GONE);
                    anyadirParadas.setVisibility(View.GONE);
                    borrarParada.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setSpinner(String[] arrayStops){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayStops) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // desactivo la primera opcion (no quiero que la pulsen)
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStop.setAdapter(spinnerAdapter);
    }

    public void actualizarParadas(View view){
        if(stopId != -1){
            String paradaActualizada = editarParadasEditText.getText().toString();

            if(!TextUtils.isEmpty(paradaActualizada) && paradaActualizada != null){
                editarParadasEditText.setError(null);
                HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                if(stops != null && stops.size() > 0){
                    for(Stop stop : stops){
                        if(stop.getStp_id() == stopId){
                            stop.setStp_place(paradaActualizada.toUpperCase());
                            stop.setStp_deleted("0");
                            hrm.updateStop(stop, this);
                            stops = hrm.getStopsByHjrId(String.valueOf(hojaRuta.getHjr_id()), this);
                            stopId = -1;
                            String arrayStops[] = hrm.fillArrayStops(hojaRuta, this);
                            setStuff(arrayStops);
                            setSpinner(arrayStops);
                            editarParadasEditText.setText(null);
                            Toast.makeText(this, "Paradas actualizadas", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Cree una parada primero", Toast.LENGTH_LONG).show();
                }
            }
            else{
                editarParadasEditText.setError("Por favor, escriba un nombre para la parada");
            }
        }
        else{
            Toast.makeText(this, "Por favor, seleccione una parada.", Toast.LENGTH_LONG).show();
        }
    }

    public void anyadirParadas(View view){
        String paradaActualizada = editarParadasEditText.getText().toString();
        if(!TextUtils.isEmpty(paradaActualizada) && paradaActualizada != null) {
            editarParadasEditText.setError(null);
            HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
            Stop stop = new Stop();
            stop.setStp_hjr_id(String.valueOf(hojaRuta.getHjr_id()));
            stop.setStp_place(paradaActualizada.toUpperCase());
            hrm.crearStop(stop, this);
            stops = hrm.getStopsByHjrId(String.valueOf(hojaRuta.getHjr_id()), this);
            stopId = -1;
            String arrayStops[] = hrm.fillArrayStops(hojaRuta, this);
            setStuff(arrayStops);
            setSpinner(arrayStops);
            editarParadasEditText.setText(null);
            Toast.makeText(this, "Parada creada", Toast.LENGTH_LONG).show();
        }
        else{
            editarParadasEditText.setError("Por favor, escriba un nombre para la parada");
        }
    }

    public void borrarParada(View view){
        if(stopId != -1){
            String paradaActualizada = editarParadasEditText.getText().toString();

            if(!TextUtils.isEmpty(paradaActualizada) && paradaActualizada != null){
                editarParadasEditText.setError(null);
                HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                if(stops != null && stops.size() > 0){
                    for(Stop stop : stops){
                        if(stop.getStp_id() == stopId){
                            stop.setStp_deleted("1");
                            hrm.updateStop(stop, this);
                            stops = hrm.getStopsByHjrId(String.valueOf(hojaRuta.getHjr_id()), this);
                            stopId = -1;
                            String arrayStops[] = hrm.fillArrayStops(hojaRuta, this);
                            setStuff(arrayStops);
                            setSpinner(arrayStops);
                            editarParadasEditText.setText(null);
                            Toast.makeText(this, "Paradas actualizadas", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Seleccione una parada para borrarla.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                editarParadasEditText.setError("Por favor, escriba una parada");
            }
        }
        else{
            Toast.makeText(this, "Por favor, seleccione una parada.", Toast.LENGTH_LONG).show();
        }
    }

    public void salirEditarParadas(View view){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        Contrato contrato = hrm.getContrato(hojaRuta.getHjr_con_id(), this);

        CommonMethods cm = new CommonMethodsImplementation();
        Log.e("hoja ruta path: ", "" + hojaRuta.getHjr_pathPdf());
        cm.borrarPdf(hojaRuta.getHjr_pathPdf());
        hrm.generatePDF(String.valueOf(hojaRuta.getHjr_id()), hojaRuta, this);

        finish();
        Toast.makeText(this, "Hoja de ruta " + hojaRuta.getHjr_alias() + " actualizada.", Toast.LENGTH_LONG).show();
    }
}
