package com.example.usuario.testingbd;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

public class Arrendatario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrendatario);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerContrato);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
    }
        //String arrayContratos[] = fillArrayContratos();
   /*     ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayContratos){
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
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                //String yearSelected = parentView.getItemAtPosition(position).toString();
                String alias = parentView.getItemAtPosition(position).toString();

                //Actualizamos con el cardView de contratos de ese anyo
                if(!alias.equals("Por favor seleccione un contrato...")){
                    HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                    Contrato contrato = hrm.getContratoByAlias(alias, getContext());

                    contratoList = new Contrato[1];
                    contratoList[0] = contrato;
                    adaptadorContrato = new AdaptadorContrato(contratoList);
                    adaptadorContrato.setAdaptadorContratoInterface(mcallback);
                    recyclerView.setAdapter(adaptadorContrato);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public String[] fillArrayContratos(){
        ArrayList<Contrato> contratos = getContratos();
        ArrayList<String> contratosAlias = new ArrayList<>();
        for(Contrato c : contratos){
            contratosAlias.add(c.getCon_alias());
        }
        String[] arrayContratos = null;
        if(contratosAlias != null){
            int contratosAliasSize = contratosAlias.size() + 1;
            arrayContratos = new String[contratosAliasSize];
            arrayContratos[0] = "Por favor seleccione un contrato...";
            for(int i = 1; i < contratosAliasSize; i++){
                arrayContratos[i] = contratosAlias.get(i-1);
            }
        }
        return arrayContratos;
    }*/
}
