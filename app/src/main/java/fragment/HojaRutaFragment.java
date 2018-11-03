package fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.testingbd.AdaptadorHojaRuta;
import com.example.usuario.testingbd.CrearArrendatario;
import com.example.usuario.testingbd.CrearHojaRuta;
import com.example.usuario.testingbd.DisplayPdf;
import com.example.usuario.testingbd.EditarArrendatario;
import com.example.usuario.testingbd.EditarParadas;
import com.example.usuario.testingbd.HojaRuta;
import com.example.usuario.testingbd.R;

import java.io.File;
import java.util.ArrayList;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.HojaRutaMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.Taxi_driver_accountDML;

/**
 * Created by Usuario on 22/02/2018.
 */

public class HojaRutaFragment extends Fragment implements AdaptadorHojaRuta.AdaptadorHojaRutaInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HojaRutaFragment.OnFragmentInteractionListener mListener;
    private AdaptadorHojaRuta.AdaptadorHojaRutaInterface mcallback;

    sqlite.model.HojaRuta[] hojaRutaList;
    AdaptadorHojaRuta adaptadorHojaRuta;

    private String contratoId = null;

    public HojaRutaFragment() {
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
    public static HojaRutaFragment newInstance(String param1, String param2) {
        HojaRutaFragment fragment = new HojaRutaFragment();
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
        View view = inflater.inflate(R.layout.fragment_hojaruta, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentHojaRuta);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerViewHojaRuta);
        recyclerView.setHasFixedSize(true);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerHojaRuta);
        spinner.setVisibility(View.GONE);

        final String contratoId = getArguments().getString("contratoId");
        /*boolean ardCreado = getArguments().getBoolean("arrendatarioCreado");
        if(ardCreado){
            getArguments().getString("arrendatarioId");
        }*/

        mcallback = this;
        sqlite.model.HojaRuta[] hojaRutas = null;
        ArrayList<sqlite.model.HojaRuta> hrList = getHojasRuta(contratoId);
        if(!hrList.isEmpty()) {
            tv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            hojaRutas = new sqlite.model.HojaRuta[hrList.size()];
            for (int i = 0; i < hrList.size(); i++) {
                hojaRutas[i] = hrList.get(i);
            }
        }
        else{
            tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        FloatingActionButton fabAddHojaRuta = (FloatingActionButton) view.findViewById(R.id.fabAnyadirHojaRuta);

        if(fabAddHojaRuta != null){
            fabAddHojaRuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
                    Contrato contrato = hrm.getContrato(contratoId, getActivity());
                    boolean esArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());
                    if(esArrendador){
                        ArrayList<sqlite.model.HojaRuta> hojasRuta = hrm.getHojasRuta(contratoId, getActivity());
                        if(hojasRuta != null && hojasRuta.size() > 0){
                            //En este caso, el arrendatario sera el mismo ya que yo soy el arrendador y no se puede repetir el arrendatario!
                            String arrendatarioId = null;
                            for(sqlite.model.HojaRuta hr : hojasRuta){
                                arrendatarioId = hr.getHjr_ard_id();
                                break;
                            }
                            intent = new Intent(getActivity(), CrearHojaRuta.class);
                            intent.putExtra("contratoId", contratoId);
                            intent.putExtra("arrendatarioId", arrendatarioId);
                        }
                        else{
                            intent = new Intent(getActivity(), CrearArrendatario.class);
                            intent.putExtra("contratoId", contratoId);
                        }
                    }
                    else{
                        intent = new Intent(getActivity(), CrearArrendatario.class);
                        intent.putExtra("contratoId", contratoId);
                    }
                    startActivity(intent);
                    ((HojaRuta)getActivity()).finish();
                }
            });
        }

        adaptadorHojaRuta = new AdaptadorHojaRuta(hojaRutas);
        adaptadorHojaRuta.setAdaptadorHojaRutaInterface(this);

        recyclerView.setAdapter(adaptadorHojaRuta);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
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

    private ArrayList<sqlite.model.HojaRuta> getHojasRuta(String contratoId){
        return ((HojaRuta) getActivity()).getHojasRutaList(contratoId);
    }

    @Override
    public void verHojaRutaPdf(int position, String hjrId){
        viewPdf(position, hjrId);
    }

    private void viewPdf(int position, String hjrId){
        Constants c = new Constants();
        Intent i = new Intent(getActivity(), DisplayPdf.class);
        i.putExtra("var_docId", hjrId);
        i.putExtra("var_idType", c.HOJARUTA_TYPE);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void enviarHojaRuta(int position, String hjrId, View view){
        createDialog(view, hjrId);
    }

    public void createDialog(View v, String hjrId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        final sqlite.model.HojaRuta hojaRuta = hrm.getHojaRuta(hjrId, getActivity());
        builder.setTitle(getString(R.string.dialog_enviarHojaRutaTitulo) + " " + hojaRuta.getHjr_alias());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.dialog_enviarHojaRuta)
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviar(hojaRuta);
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void enviar(sqlite.model.HojaRuta hojaRuta){
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(getActivity());
        String txdId = String.valueOf(txd.getTxd_id());
        String tdaId = cm.getTdaIdGivenTxdId(getActivity(), txdId);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(getActivity());
        Taxi_driver_account tda = tdaDml.getTdaById(new DatabaseHelper(getActivity()), tdaId, getActivity());
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        Contrato contrato = hrm.getContrato(hojaRuta.getHjr_con_id(), getActivity());

        String[] to = {tda.getTda_email()};
        StringBuffer sb = new StringBuffer();
        sb.append(getString(R.string.hojaRutaEmailSubject_1) + " " + hojaRuta.getHjr_number());
        sb.append(" ");
        sb.append(getString(R.string.hojaRutaEmailSubject_2) + " " + contrato.getCon_alias());
        String mensaje = sb.toString();

        String hojaRutaName = "HojaRuta_" + hojaRuta.getHjr_alias();
        String emailNombreArchivo = hojaRutaName + ".pdf";
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_HOJASRUTA + File.separator + hrm.setHrFolder(hojaRuta)
                + File.separator + emailNombreArchivo;

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, null, "" + hojaRutaName, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    @Override
    public void editarHojaRuta(int position, String hrId, View view){
        Intent i = new Intent(getActivity(), com.example.usuario.testingbd.EditarHojaRuta1.class);
        i.putExtra("hojaRutaId", hrId);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void borrarHojaRuta(int position, String hjrId, View view){
        deleteHojaRutaDialog(hjrId, view);
    }

    private void deleteHojaRutaDialog(String hjr_id, View v){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        final sqlite.model.HojaRuta hr = hrm.getHojaRuta(hjr_id, getActivity());
        if(hr != null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_borrarHojaRutaTitulo) + " " + hr.getHjr_alias());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarHojaRuta)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            hrm.borrarHojaRuta(String.valueOf(hr.getHjr_id()), getActivity());

                            String hojaRutaName = "HojaRuta_" + hr.getHjr_number() + ".pdf";
                            String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator + c.NOMBRE_CARPETA_APP
                                    + File.separator + c.GENERADOS_HOJASRUTA + File.separator + hrm.setHrFolder(hr)
                                    + File.separator + hojaRutaName;

                            Toast.makeText(getActivity(), "Hoja de ruta " + hr.getHjr_number() + " " + getString(R.string.confirmacionBorrar),
                                    Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getActivity(), HojaRuta.class);
                            i.putExtra("contratoId", hr.getHjr_con_id());
                            startActivity(i);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Toast.makeText(getActivity(), "No se puede borrar esta Hoja de ruta",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void imprimirHojaRuta(int position, String hjrId, View view){
        imprimirHojaRutaDialog(hjrId, view);
    }

    private void imprimirHojaRutaDialog(String hjr_id, View v){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        final sqlite.model.HojaRuta hr = hrm.getHojaRuta(hjr_id, getActivity());
        if(hr != null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_imprimirHojaRutaTitulo) + " " + hr.getHjr_alias());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_imprimirHojaRuta)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String hjrId = String.valueOf(hr.getHjr_id());
                            ((HojaRuta) getActivity()).imprimirHojaRuta(hjrId);
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
            Toast.makeText(getActivity(), "No se puede imprimir esta Hoja de ruta",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void editarArrendatario(int position, String hjrId, String ardId, String conId, View view){
        Intent i = new Intent(getContext(), EditarArrendatario.class);
        i.putExtra("contratoId", conId);
        i.putExtra("arrendatarioId", ardId);
        i.putExtra("hojaRutaId", hjrId);
        startActivity(i);
    }

    @Override
    public void editarParadas(int position, String hjrId, View view){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        sqlite.model.HojaRuta hr = hrm.getHojaRuta(hjrId, getActivity());
        String arrayStops[] = hrm.fillArrayStops(hr, getActivity());
        if(arrayStops != null && arrayStops.length > 0){
            Intent i = new Intent(getContext(), EditarParadas.class);
            i.putExtra("hojaRutaId", hjrId);
            startActivity(i);
        }
        else {
            Toast.makeText(getActivity(), "Esta hoja de ruta no tiene paradas.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void borrarArrendatario(int position, String ardId, String conId){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        final Arrendatario ard = hrm.getArrendatario(ardId, getContext());
        final ArrayList<sqlite.model.HojaRuta> hojaRutas = hrm.getHojasRutaByArrendatarioId(ardId, getContext());
        if(ard != null){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_borrarArdTitulo) + " " + ard.getArd_name());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarArd)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String ardId = String.valueOf(ard.getArd_id());
                            //Se borra tanto el arrendatario como todas las hojas de ruta asociadas
                            hrm.borrarArrendatario(ardId, getContext());
                            for(sqlite.model.HojaRuta hr : hojaRutas){
                                hrm.borrarHojaRuta(String.valueOf(hr.getHjr_id()), getContext());
                            }
                            Toast.makeText(getActivity(), "El arrendatario '" + ard.getArd_name() +
                                            "' ha sido borrado correctamente.",
                                    Toast.LENGTH_LONG).show();
                            getActivity().finish();
                            // Hay que actualizar la pantalla de contratos, esto es:
                            // ir atras con getAct.finish + restart contratoFragment
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
            Toast.makeText(getActivity(), "No se puede borrar este arrendatario",
                    Toast.LENGTH_LONG).show();
        }
    }

}
