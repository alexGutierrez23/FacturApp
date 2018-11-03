package fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.testingbd.AdaptadorContrato;
import com.example.usuario.testingbd.Contrato1;
import com.example.usuario.testingbd.R;
import com.example.usuario.testingbd.VistaContratos;

import java.util.ArrayList;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.YearsDML;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContratoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContratoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContratoFragment extends Fragment implements AdaptadorContrato.AdaptadorContratoInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AdaptadorContrato.AdaptadorContratoInterface mcallback;

    private Contrato[] contratoList;
    private AdaptadorContrato adaptadorContrato;
    private RecyclerView recyclerView;
    private Spinner spinner;

    private boolean refreshFragment = false;
    private String contratoCardOnUpdate = null;
    private int positionOnUpdate;

    public ContratoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContratoFragment newInstance(String param1, String param2) {
        ContratoFragment fragment = new ContratoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contrato, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentContrato);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerViewContrato);
        recyclerView.setHasFixedSize(true);

        RelativeLayout spinnerRL = (RelativeLayout) view.findViewById(R.id.spinnerLayout);
        spinner = (Spinner) spinnerRL.findViewById(R.id.spinnerContrato);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);

        mcallback = this;

        //String arrayContratos[] = fillArray();
        String arrayContratos[] = fillArrayContratos();
        if(arrayContratos == null ){
            spinnerRL.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            tv.setVisibility(View.GONE);
            spinnerRL.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            //ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, arrayContratos){
            setSpinner(arrayContratos);
        }

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.planets_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                //String yearSelected = parentView.getItemAtPosition(position).toString();
                String arrendador = parentView.getItemAtPosition(position).toString();

                //Actualizamos con el cardView de contratos de ese anyo
                if(!arrendador.equals("Por favor, seleccione un contrato...")){
                    HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                    //Contrato contrato = hrm.getContratoByAlias(arrendador, getContext());
                    ArrayList<Contrato> contratos = getContratosByArrendador(arrendador);
                    Contrato[] contrato = null;

                    if(!contratos.isEmpty()){
                        contrato = new Contrato[contratos.size()];
                        for(int i = 0; i < contratos.size(); i++){
                            contrato[i] = contratos.get(i);
                        }
                        adaptadorContrato = new AdaptadorContrato(contrato);
                        adaptadorContrato.setAdaptadorContratoInterface(mcallback);  //Inicializamos llamada a la interfaz
                        recyclerView.setAdapter(adaptadorContrato);
                    }

                   /* contratoList = new Contrato[1];
                    contratoList[0] = contrato;
                    adaptadorContrato = new AdaptadorContrato(contratoList);
                    adaptadorContrato.setAdaptadorContratoInterface(mcallback);
                    recyclerView.setAdapter(adaptadorContrato); */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        FloatingActionButton fabAddContrato = (FloatingActionButton) view.findViewById(R.id.fabAnyadirContrato);

        //adaptadorContrato = new AdaptadorContrato(contratoList);
        //adaptadorContrato.setAdaptadorContratoInterface(this);

        if(fabAddContrato != null){
            fabAddContrato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), Contrato1.class);
                    startActivity(i);
                    getActivity().finish();
                }
            });
        }

        recyclerView.setAdapter(adaptadorContrato);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void setSpinner(String[] arrayContratos){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, arrayContratos){
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
    }

    private ArrayList<Contrato> getContratosByYear(String yearId){
        return getContratosList(yearId);
    }

    public String[] fillArrayContratosYears(){
        ArrayList<String> contratosYears = getYears();
        String[] arrayContratos = null;

        if(contratosYears != null){
            int contratosYearsSize = contratosYears.size() + 1;
            arrayContratos = new String[contratosYearsSize];
            arrayContratos[0] = "Por favor, seleccione un año...";
            for(int i = 1; i < contratosYearsSize; i++){
                arrayContratos[i] = contratosYears.get(i-1);
            }
        }
        return arrayContratos;
    }

    public ArrayList<String> getYears(){
        YearsDML yeaDml = new YearsDML(getContext());
        return yeaDml.getContratoYears(new DatabaseHelper(getContext()).getWritableDatabase(), getContext());
    }

    public String[] fillArrayContratos(){
        ArrayList<Contrato> contratos = getContratos();
        ArrayList<Contrato> contratosAux = new ArrayList<>();
        for(Contrato c: contratos){
            String arrendadorUpper = c.getCon_arrendador().toUpperCase();
            if(contratosAux.size() == 0){
                contratosAux.add(c);
            }
            else{
                boolean encontrado = false;
                for(Contrato ct : contratosAux){
                    if(ct.getCon_arrendador().equalsIgnoreCase(arrendadorUpper)){
                        encontrado = true;
                    }
                }
                if(!encontrado){
                    contratosAux.add(c);
                }
            }
        }

        ArrayList<String> contratosPorArrendador = new ArrayList<>();
        for(Contrato c : contratosAux){
            contratosPorArrendador.add(c.getCon_arrendador());
        }
        String[] arrayContratos = null;
        if(contratosPorArrendador != null){
            int contratosAliasSize = contratosPorArrendador.size() + 1;
            arrayContratos = new String[contratosAliasSize];
            arrayContratos[0] = "Por favor, seleccione un contrato...";
            for(int i = 1; i < contratosAliasSize; i++){
                arrayContratos[i] = contratosPorArrendador.get(i-1);
            }
        }
        return arrayContratos;
    }

    private ArrayList<Contrato> getContratos(){
        return ((VistaContratos) getActivity()).getContratos();
    }

    private ArrayList<Contrato> getContratosByArrendador(String arrendador){
        return ((VistaContratos) getActivity()).getContratosByArrendador(arrendador);
    }

    public ArrayList<Contrato> getContratosList(String yearId) {
        return ((VistaContratos) getActivity()).getContratosList(yearId);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    /*
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try{
                mcallback = (Adaptador.AdaptadorTicketInterface) context;
            }
            catch (ClassCastException cce){
                throw new ClassCastException(context.toString()
                        + " must implement AdaptadorTicketInterface");
            }
        }
    */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(refreshFragment){
            ContratoFragment contratoFragment = new ContratoFragment();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.contratoFragmentLayout, contratoFragment, contratoFragment.getTag()).commit();

            //spinner.setSelection(positionOnUpdate);

            HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
            Contrato contrato = hrm.getContrato(contratoCardOnUpdate, getContext());
            contratoList = new Contrato[1];
            contratoList[0] = contrato;
            adaptadorContrato = new AdaptadorContrato(contratoList);
            adaptadorContrato.setAdaptadorContratoInterface(mcallback);
            recyclerView.setAdapter(adaptadorContrato);
            adaptadorContrato.notifyDataSetChanged();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        refreshFragment = false;
    }


    @Override
    public void verHojasRuta(int position, String conId){
        viewHojasRuta(position, conId);
    }

    private void viewHojasRuta(int position, String conId){
        Constants c = new Constants();

        Intent i = new Intent(getActivity(), com.example.usuario.testingbd.HojaRuta.class);
        i.putExtra("contratoId", conId);
        startActivity(i);
    }


    @Override
    public void borrarContrato(int position, String conId, View view){
        deleteContratoDialog(conId, view);
    }

    private void deleteContratoDialog(String con_id, View v){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        final sqlite.model.Contrato contrato = hrm.getContrato(con_id, getActivity());
        if(contrato != null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_borrarContratoTitulo) + " " + contrato.getCon_alias());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarContrato)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            hrm.borrarContrato(String.valueOf(contrato.getCon_id()), getActivity());
                            Toast.makeText(getActivity(), "Contrato " + contrato.getCon_alias() + " " + getString(R.string.confirmacionBorrar),
                                    Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getActivity(), VistaContratos.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Toast.makeText(getActivity(), "No se puede borrar este contrato",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void editarContrato(int position, String conId, View view){
        Intent i = new Intent(getActivity(), com.example.usuario.testingbd.EditarContrato.class);
        i.putExtra("contratoId", conId);
        startActivity(i);
        getActivity().finish();
    }

    private Taxi_driver_account getTda(){
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(getActivity());
        String txdId = String.valueOf(txd.getTxd_id());
        String tdaId = cm.getTdaIdGivenTxdId(getActivity(), txdId);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(getActivity());
        Taxi_driver_account tda = tdaDml.getTdaById(new DatabaseHelper(getActivity()), tdaId, getActivity());
        return tda;
    }

    @Override
    public ArrayList<HojaRuta> getHojasRuta(int position, String conId){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getHojasRuta(conId, getActivity());
    }

    @Override
    public boolean esTdaArrendador(int position, String esArrendador){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getBooleanIsTdaArrendador(esArrendador);
    }

    @Override
    public Arrendatario getArrendatario(int position, String arrendatarioId){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        return hrm.getArrendatario(arrendatarioId, getActivity());
    }

}