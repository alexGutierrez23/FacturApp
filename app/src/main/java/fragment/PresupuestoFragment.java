package fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.testingbd.AdaptadorPresupuesto;
import com.example.usuario.testingbd.DisplayPdf;
import com.example.usuario.testingbd.MenuPrincipal;
import com.example.usuario.testingbd.R;

import java.io.File;
import java.util.ArrayList;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.PresupuestoMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.PresupuestoMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Presupuesto;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.LastFileDML;
import sqlite.repo.PresupuestoDML;
import sqlite.repo.Taxi_driver_accountDML;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PresupuestoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PresupuestoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PresupuestoFragment extends Fragment implements AdaptadorPresupuesto.AdaptadorPresupuestoInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView = null;

    public PresupuestoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PresupuestoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PresupuestoFragment newInstance(String param1, String param2) {
        PresupuestoFragment fragment = new PresupuestoFragment();
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
        View view = inflater.inflate(R.layout.fragment_presupuesto, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentPresupuesto);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerViewPresupuesto);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabAddPresupuesto = (FloatingActionButton) view.findViewById(R.id.fabAnyadirPresupuesto);

        ArrayList<Presupuesto> presupuestoList = getPresupuestos();
        AdaptadorPresupuesto adaptadorPresupuesto = null;
        Presupuesto[] presupuesto = null;

        if(!presupuestoList.isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            presupuesto = new Presupuesto[presupuestoList.size()];
            for(int i = 0; i < presupuestoList.size(); i++){
                presupuesto[i] = presupuestoList.get(i);
            }
            adaptadorPresupuesto = new AdaptadorPresupuesto(presupuesto);
            adaptadorPresupuesto.setAdaptadorPresupuestoInterface(this);
        }
        else{
            tv.setText("No hay presupuestos creados.");
            recyclerView.setVisibility(View.GONE);
        }

        if(fabAddPresupuesto != null){
            fabAddPresupuesto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), com.example.usuario.testingbd.Presupuesto.class);
                    startActivity(i);
                    ((MenuPrincipal)getActivity()).finish();
                }
            });
        }

        //  Adaptador adaptador = new Adaptador(new String[]{"Example 1", "Example 2", "Example 3",
        //      "Example 4", "Example 5", "Example 6", "Example 7"}); //aqui va la lista de objetos de ticket
        recyclerView.setAdapter(adaptadorPresupuesto);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private ArrayList<Presupuesto> getPresupuestos(){
        return ((MenuPrincipal) getActivity()).getPresupuestoList();
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public void verPresupuestoPdf(int position, String invId){
        viewPdf(position, invId);
    }

    private void viewPdf(int position, String preId){
        Constants c = new Constants();
        Intent i = new Intent(getActivity(), DisplayPdf.class);
        i.putExtra("var_docId", preId);
        i.putExtra("var_idType", c.PRESUPUESTO_TYPE);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void enviarPresupuesto(int position, String preId, View view){
        enviarPresupuestoDialog(view, preId);
    }

    public void enviarPresupuestoDialog(View v, String preId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        PresupuestoMethods pm = new PresupuestoMethodsImplementation();
        final Presupuesto pre = pm.getPresupuesto(Integer.parseInt(preId), getActivity());
        builder.setTitle(getString(R.string.dialog_enviarPresupuestoTitulo) + " " + pre.getPre_number());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.dialog_enviarPresupuesto)
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviar(pre);
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

    public void enviar(Presupuesto presupuesto){
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(getActivity());
        String txdId = String.valueOf(txd.getTxd_id());
        String tdaId = cm.getTdaIdGivenTxdId(getActivity(), txdId);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(getActivity());
        Taxi_driver_account tda = tdaDml.getTdaById(new DatabaseHelper(getActivity()), tdaId, getActivity());

        String emailNumFactura = presupuesto.getPre_number();
        String[] to = {tda.getTda_email()};
        String mensaje = getString(R.string.presupuestoEmailSubject) + " " + presupuesto.getPre_date();
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_PRESUPUESTOS + File.separator + presupuesto.getPre_numPresupuestoRed() + ".pdf";

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, null, "" + "Presupuesto " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    @Override
    public void borrarPresupuesto(int position, String invId, View view){
        deletePresupuestoDialog(invId, view);
    }

    public void deletePresupuestoDialog(String pre_id, View v){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final PresupuestoDML preDml = new PresupuestoDML(getActivity());
        final boolean lastPresupuesto = getLastPresupuesto(pre_id);
        if(lastPresupuesto){
            final Presupuesto p = getPresupuesto(pre_id);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_borrarPresupuestoTitulo) + " " + p.getPre_number());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarPresupuesto)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String preId = cm.borrarFile(c.PRESUPUESTO_TYPE, getActivity());
                            preDml.borrarPresupuesto(new DatabaseHelper(getActivity()), preId, getActivity());

                            String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                                    + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_PRESUPUESTOS
                                    + File.separator + p.getPre_numPresupuestoRed() + ".pdf";
                            cm.borrarPdf(nombre_completo);
                            Toast.makeText(getActivity(), p.getPre_numPresupuestoRed() + " " + getString(R.string.confirmacionBorrar), Toast.LENGTH_LONG).show();
                            //refresh fragment
                            recyclerView.setVisibility(View.GONE);
                            PresupuestoFragment preFragment = new PresupuestoFragment();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.presupuestoFragmentLayout, preFragment, preFragment.getTag()).commit();
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
            Toast.makeText(getActivity(), R.string.borrarPresupuestoError, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getLastPresupuesto(String pre_id){
        boolean lastPresupuesto = false;
        CommonMethods cm = new CommonMethodsImplementation();

        PresupuestoDML pre = new PresupuestoDML(getActivity());
        LastFileDML lfiDml = new LastFileDML(getActivity());
        String preId = cm.getLastPresupuesto(getActivity());
        if(preId == null || preId.equals("-1")){
            lastPresupuesto = false;
        }
        else if(!preId.equals(pre_id)){
            lastPresupuesto = false;
        }
        else{
            lastPresupuesto = true;
        }
        return lastPresupuesto;
    }

    private Presupuesto getPresupuesto(String pre_id){
        PresupuestoDML pre = new PresupuestoDML(getActivity());
        return pre.getPresupuestoById(new DatabaseHelper(getActivity()), Integer.parseInt(pre_id), getActivity());
    }

}
