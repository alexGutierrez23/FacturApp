package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.HojaRutaMethods;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;

public class CrearArrendatario extends AppCompatActivity {

    private EditText arrendatario, nifArrendatario, newArdNumberArrendatario, selecArdNumberArrendatario;
    private Button crearArrendatario, finalizarContrato;
    private RadioGroup radioGroupArd;
    private RadioButton newArdRB, selecArdRB;
    private Spinner spinnerArd;
    private TextView tvArd, mostrarFecha, mostrarFechaSpinner;
    private Button buttonCreateArd, selecFecha, selecFechaSpinner, buttonCreateArdSpinner;

    private RelativeLayout layoutSelectArrendatario, layoutNewArrendatario;

    private String contratoId = null;
    private String ardSelected = null;
    String nombreArrendatarioSelected = null;
    boolean esTdaArrendador = false;

    private ArrayList<Arrendatario> arrendatarios = null;
    ArrayList<HojaRuta> listaHojasRuta = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_arrendatario);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_createArrendatario);
        act.setDisplayHomeAsUpEnabled(true);

        Bundle formulario = this.getIntent().getExtras();
        contratoId = formulario.getString("contratoId");

        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        Contrato contrato = hrm.getContrato(contratoId, this);
        esTdaArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());

        listaHojasRuta = hrm.getHojasRuta(contratoId, this);

        tvArd = (TextView) findViewById(R.id.textViewArrendatario);

        /* LAYOUT SELECCIONAR ARRENDATARIO */
        layoutSelectArrendatario = (RelativeLayout) findViewById(R.id.layoutSeleccionarArd);
        spinnerArd = (Spinner) layoutSelectArrendatario.findViewById(R.id.spinnerArrendatario);
        spinnerArd.getBackground().setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
        selecFechaSpinner = (Button) layoutSelectArrendatario.findViewById(R.id.selecFechaArd2);
        mostrarFechaSpinner = (TextView) layoutSelectArrendatario.findViewById(R.id.mostrarFechaArd2);
        selecArdNumberArrendatario = (EditText) layoutSelectArrendatario.findViewById(R.id.numberContratoSelecArd);
        buttonCreateArdSpinner = (Button) layoutSelectArrendatario.findViewById(R.id.button_crearArrendatarioSpinner);

        /* LAYOUT NUEVO ARRENDATARIO*/
        layoutNewArrendatario = (RelativeLayout) findViewById(R.id.layoutNuevoArd);
        selecFecha = (Button) layoutNewArrendatario.findViewById(R.id.selecFechaArd);
        mostrarFecha = (TextView) layoutNewArrendatario.findViewById(R.id.mostrarFechaArd);
        buttonCreateArd = (Button) layoutNewArrendatario.findViewById(R.id.button_crearArrendatario);
        arrendatario = (EditText) layoutNewArrendatario.findViewById(R.id.nombreArrendatario);
        nifArrendatario = (EditText) layoutNewArrendatario.findViewById(R.id.nifArrendatario);
        newArdNumberArrendatario = (EditText) layoutNewArrendatario.findViewById(R.id.numberContratoNewArd);

        radioGroupArd = (RadioGroup) findViewById(R.id.radioGroupArd);
        newArdRB = (RadioButton) findViewById(R.id.radioButtonNuevoArd);
        selecArdRB = (RadioButton) findViewById(R.id.radioButtonSelecArd);

        selecFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(CrearArrendatario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        mostrarFecha.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        selecFechaSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(CrearArrendatario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        mostrarFechaSpinner.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        //Desactivo el radiobutton de seleccionar arrendatario si no hay ninguno creado
        arrendatarios = new ArrayList<>();
        arrendatarios = hrm.getAllArrendatarios(this);
        showSpinner(arrendatarios);

        if(arrendatarios == null || arrendatarios.size() == 0){
            selecArdRB.setEnabled(false);
            selecArdRB.setChecked(false);
            newArdRB.setChecked(true);
            newArdRB.setEnabled(false);
            layoutSelectArrendatario.setVisibility(View.GONE);
            layoutNewArrendatario.setVisibility(View.VISIBLE);
            if(esTdaArrendador){
                mostrarFecha.setVisibility(View.VISIBLE);
                selecFecha.setVisibility(View.VISIBLE);
            }
            else{
                mostrarFecha.setVisibility(View.GONE);
                selecFecha.setVisibility(View.GONE);
            }
        }
        else {
            selecArdRB.setEnabled(true);
            selecArdRB.setChecked(false);
            newArdRB.setChecked(false);
            newArdRB.setEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onArrendatarioRbSelected(View v){
        boolean opcionSeleccionada = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.radioButtonNuevoArd:
                if(opcionSeleccionada){
                    layoutSelectArrendatario.setVisibility(View.GONE);
                    layoutNewArrendatario.setVisibility(View.VISIBLE);

                    if(esTdaArrendador){
                        mostrarFecha.setVisibility(View.VISIBLE);
                        selecFecha.setVisibility(View.VISIBLE);
                        newArdNumberArrendatario.setVisibility(View.VISIBLE);
                    }
                    else{
                        newArdNumberArrendatario.setVisibility(View.GONE);
                        mostrarFecha.setVisibility(View.GONE);
                        selecFecha.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.radioButtonSelecArd:
                if(opcionSeleccionada){
                    layoutSelectArrendatario.setVisibility(View.VISIBLE);
                    layoutNewArrendatario.setVisibility(View.GONE);

                    if(esTdaArrendador){
                        mostrarFechaSpinner.setVisibility(View.VISIBLE);
                        selecFechaSpinner.setVisibility(View.VISIBLE);
                        selecArdNumberArrendatario.setVisibility(View.VISIBLE);
                    }
                    else{
                        selecArdNumberArrendatario.setVisibility(View.GONE);
                        mostrarFechaSpinner.setVisibility(View.GONE);
                        selecFechaSpinner.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    public void crearArrendatario(View v){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        String fechaArd = null;
        String fechaArdSpinner = null;
        String numberArd = null;
        boolean numberOk = false;
        boolean nameRepeated = false;
        String numberArdSpinner = null;
        if(radioGroupArd.getCheckedRadioButtonId() == newArdRB.getId()){
            fechaArd = mostrarFecha.getText().toString();
            numberArd = newArdNumberArrendatario.getText().toString();
            String ardName = arrendatario.getText().toString();
            String nifArd = nifArrendatario.getText().toString();

            mostrarFecha.setError(null);
            newArdNumberArrendatario.setError(null);
            arrendatario.setError(null);
            nifArrendatario.setError(null);
            if(esTdaArrendador){
                if(!errorControl(fechaArd, numberArd, ardName, nifArd)){
                    if(!isNameRepeated(ardName)) {
                        if(!hasArrendatarios(listaHojasRuta)){
                            ArrayList<Arrendatario> ards = hrm.getAllArrendatarios(this);
                            Arrendatario ard = new Arrendatario();
                            ard.setArd_name(ardName.toUpperCase());
                            ard.setArd_nifCif(nifArd);
                            ard.setArd_fecha(fechaArd);
                            ard.setArd_number(numberArd);
                            String ardId = hrm.insertarArrendatario(ard, this);
                            Intent i = new Intent(this, CrearHojaRuta.class);
                            i.putExtra("arrendatarioId", ardId);
                            i.putExtra("contratoId", contratoId);
                            finish();
                            startActivity(i);
                        }
                    }
                }
            }
            else{
                Arrendatario ard = new Arrendatario();
                ard.setArd_name(ardName.toUpperCase());
                ard.setArd_nifCif(nifArd);
                String ardId = hrm.insertarArrendatario(ard, this);
                Intent i = new Intent(this, CrearHojaRuta.class);
                i.putExtra("arrendatarioId", ardId);
                i.putExtra("contratoId", contratoId);
                finish();
                startActivity(i);
            }
        }
        else if(radioGroupArd.getCheckedRadioButtonId() == selecArdRB.getId()){
            boolean numberSelecArdOk = false;
            fechaArdSpinner = mostrarFechaSpinner.getText().toString();
            numberArdSpinner = selecArdNumberArrendatario.getText().toString();
            //boolean completado = ;
            if(nombreArrendatarioSelected != null){
                if(esTdaArrendador){
                    if(!errorControl(fechaArdSpinner, numberArdSpinner)){
                        if(!hasArrendatarios(listaHojasRuta)){
                            mostrarFechaSpinner.setError(null);
                            Arrendatario ard = hrm.getArrendatarioByArdName(nombreArrendatarioSelected, this);
                            ard.setArd_fecha(fechaArdSpinner);
                            ard.setArd_number(numberArdSpinner);
                            String ardId = hrm.insertarArrendatario(ard, this); // Insertamos uno nuevo para no alterar las hojas de ruta creadas pero con mismos datos del arrendatario
                            Intent i = new Intent(this, CrearHojaRuta.class);
                            i.putExtra("arrendatarioId", ardId);
                            i.putExtra("contratoId", contratoId);
                            finish();
                            startActivity(i);
                        }
                    }
                }
                else{
                    mostrarFechaSpinner.setError(null);
                    selecArdNumberArrendatario.setError(null);
                    Arrendatario ard = hrm.getArrendatarioByArdName(nombreArrendatarioSelected, this);
                    String ardId = hrm.insertarArrendatario(ard, this);
                    Intent i = new Intent(this, CrearHojaRuta.class);
                    i.putExtra("arrendatarioId", ardId);
                    i.putExtra("contratoId", contratoId);
                    finish();
                    startActivity(i);
                }
            }else{
                Toast.makeText(this, "Seleccione un arrendatario de la lista, por favor.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean hasArrendatarios(ArrayList<HojaRuta> listaHojasRuta){
        boolean hasArrendatario = false;
        // Si hay arrendatarios creados, el conductor es el arrendador
            // Y ademas el contrato ya tiene hojas de ruta -> poner el arrendatario automaticamente en el spinner
        if(listaHojasRuta.size() > 0 && listaHojasRuta != null){
            for(HojaRuta hr : listaHojasRuta){
                if(!hr.getHjr_con_id().isEmpty() && hr.getHjr_con_id() != null){
                    if(hr.getHjr_con_id().equalsIgnoreCase(contratoId)){
                        Toast.makeText(this,
                                "No se puede crear un contrato con arrendatario distinto si eres arrendador. Cree otro contrato, por favor.",
                                Toast.LENGTH_LONG).show();
                        hasArrendatario = true;
                        return hasArrendatario;
                    }
                }
            }
        }
        // Si no tiene hojas de ruta creadas, permitimos la creacion
        else{
            hasArrendatario = false;
        }
        return hasArrendatario;
    }

    private boolean errorControl(String fechaArdSpinner, String numberArdSpinner) {
        mostrarFechaSpinner.setError(null);
        selecArdNumberArrendatario.setError(null);

        if(fechaArdSpinner.isEmpty() || fechaArdSpinner == null){
            mostrarFechaSpinner.setError("Por favor introduzca un nombre para el arrendatario");
        }
        if(numberArdSpinner.isEmpty() || numberArdSpinner == null){
            selecArdNumberArrendatario.setError("Por favor introduzca un número de contrato");
        }

        if(mostrarFechaSpinner.getError() != null || selecArdNumberArrendatario.getError() != null){
            return true;  //hay errores
        }
        else{
            return checkIfExists(numberArdSpinner, selecArdNumberArrendatario); // return true si Hay errores o false si no.
        }
    }

    private boolean errorControl(String date, String number, String ardName, String nifArd) {
        mostrarFecha.setError(null);
        newArdNumberArrendatario.setError(null);
        arrendatario.setError(null);
        nifArrendatario.setError(null);

        if(ardName.isEmpty() || ardName == null){
            arrendatario.setError("Por favor introduzca un nombre para el arrendatario");
        }
        if(number.isEmpty() || number == null){
            newArdNumberArrendatario.setError("Por favor introduzca un número de contrato");
        }
        if(date.isEmpty() || date == null){
            mostrarFecha.setError("Por favor introduzca una fecha.");
        }
        if(nifArd.isEmpty() || nifArd == null){
            nifArrendatario.setError("Por favor introduzca un NIF");
        }

        if(mostrarFecha.getError() != null || newArdNumberArrendatario.getError() != null
                || arrendatario.getError() != null || nifArrendatario.getError() != null){
            return true;  //hay errores
        }
        else{
            return checkIfExists(number, newArdNumberArrendatario); // return true si Hay errores o false si no.
        }
    }

    private boolean checkIfExists(String number, EditText numberArdt){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        ArrayList<Contrato> contratos = hrm.getContratos(this);
        if(contratos != null){
            if(contratos.size() > 0) {
                for (Contrato c : contratos) {
                    if (c.getCon_number() != null) {
                        if (c.getCon_number().equalsIgnoreCase(number)) {
                            numberArdt.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                            break;
                        } else {
                            numberArdt.setError(null);
                        }
                    } else {
                        numberArdt.setError(null);
                    }
                }
            }
            else{
                numberArdt.setError(null);
            }
        }
        else{
            numberArdt.setError(null);
        }

        if(numberArdt.getError() != null){
            return true; // hay errores
        }
        else{
            ArrayList<sqlite.model.Arrendatario> ards = hrm.getAllArrendatarios(this);
            if(ards != null){
                if(ards.size() > 0){
                    for(Arrendatario arrendatario : ards){
                        if(arrendatario.getArd_number() != null){
                            if(arrendatario.getArd_number().equalsIgnoreCase(number)){
                                numberArdt.setError("Este número de contrato ya existe. Por favor, escriba otro diferente");
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    private void showSpinner(ArrayList<Arrendatario> arrendatarios){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        String arrayArrendatarios[] = hrm.fillArrayArrendatarios(arrendatarios);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayArrendatarios){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    // desactivo la primera opcion (no quiero que la pulsen)
                    return false;
                }
                else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerArd.setAdapter(spinnerAdapter);

        spinnerArd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                nombreArrendatarioSelected = parentView.getItemAtPosition(position).toString();

                if(!nombreArrendatarioSelected.equals("Por favor seleccione un arrendatario...")){
                    buttonCreateArd.setEnabled(true);
                    //HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                    //Contrato contrato = hrm.getContratoByAlias(alias, getApplicationContext());

                    //Hay que restringir el nombre y nif que se crean para los arrendatarios!
                }
                else{
                    nombreArrendatarioSelected = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                nombreArrendatarioSelected = null;
            }
        });
    }

    private boolean isNameRepeated(String ardName){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        ArrayList<Arrendatario> ards = hrm.getAllArrendatarios(this);
        if(ards != null){
            if(ards.size() > 0){
                for(Arrendatario ard : ards){
                    if(ard.getArd_name() != null){
                        if(ard.getArd_name().equals(ardName)){
                            arrendatario.setError("Este arrendatario ya existe. Seleccione este arrendatario de la lista");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
