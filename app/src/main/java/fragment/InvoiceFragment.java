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

import com.example.usuario.testingbd.AdaptadorInvoice;
import com.example.usuario.testingbd.DisplayPdf;
import com.example.usuario.testingbd.Factura;
import com.example.usuario.testingbd.MenuPrincipal;
import com.example.usuario.testingbd.R;

import java.io.File;
import java.util.ArrayList;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.InvoiceMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Invoice;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.InvoiceDML;
import sqlite.repo.LastFileDML;
import sqlite.repo.Taxi_driver_accountDML;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceFragment extends Fragment implements AdaptadorInvoice.AdaptadorInvoiceInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView = null;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoiceFragment newInstance(String param1, String param2) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentInvoice);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerViewInvoice);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabAddInvoice = (FloatingActionButton) view.findViewById(R.id.fabAnyadirInvoice);

        ArrayList<Invoice> invoiceList = getInvoices();
        AdaptadorInvoice adaptadorInvoice = null;
        Invoice[] invoice = null;

        if(!invoiceList.isEmpty()){
            invoice = new Invoice[invoiceList.size()];
            for(int i = 0; i < invoiceList.size(); i++){
                invoice[i] = invoiceList.get(i);
            }
            recyclerView.setVisibility(View.VISIBLE);
            adaptadorInvoice = new AdaptadorInvoice(invoice);
            adaptadorInvoice.setAdaptadorInvoiceInterface(this);  //Inicializamos llamada a la interfaz
        }
        else{
            tv.setText("No hay facturas creadas.");
            recyclerView.setVisibility(View.GONE);
        }

        if(fabAddInvoice != null){
            fabAddInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), Factura.class);
                    startActivity(i);
                    ((MenuPrincipal)getActivity()).finish();
                }
            });
        }

        //View viewItems = inflater.inflate(R.layout.fragment_invoice, container, false);

        //  Adaptador adaptador = new Adaptador(new String[]{"Example 1", "Example 2", "Example 3",
        //      "Example 4", "Example 5", "Example 6", "Example 7"}); //aqui va la lista de objetos de ticket


        recyclerView.setAdapter(adaptadorInvoice);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    private ArrayList<Invoice> getInvoices(){
        return ((MenuPrincipal) getActivity()).getInvoiceList();
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
    public void verFacturaPdf(int position, String invId){
        viewPdf(position, invId);
    }

    private void viewPdf(int position, String invId){
        Constants c = new Constants();
        Intent i = new Intent(getActivity(), DisplayPdf.class);
        i.putExtra("var_docId", invId);
        i.putExtra("var_idType", c.FACTURA_TYPE);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void enviarFactura(int position, String invId, View view){
        createDialog(view, invId);
    }

    public void createDialog(View v, String invId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        InvoiceMethods im = new InvoiceMethodsImplementation();
        final Invoice invoice = im.getInvoice(Integer.parseInt(invId), getActivity());
        builder.setTitle(getString(R.string.dialog_enviarFacturaTitulo) + " " + invoice.getInv_number());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.dialog_enviarFactura)
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviar(invoice);
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

    public void enviar(Invoice invoice){
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(getActivity());
        String txdId = String.valueOf(txd.getTxd_id());
        String tdaId = cm.getTdaIdGivenTxdId(getActivity(), txdId);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(getActivity());
        Taxi_driver_account tda = tdaDml.getTdaById(new DatabaseHelper(getActivity()), tdaId, getActivity());

        String emailNombreArchivo = invoice.getInv_numFactRed() + ".pdf";
        String emailNumFactura = invoice.getInv_number();
        String[] to = {tda.getTda_email()};
        String mensaje = getString(R.string.invoiceEmailSubject) + " " + invoice.getInv_date();
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_FACTURAS
                + File.separator + emailNombreArchivo;

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, null, "" + "Factura " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    @Override
    public void borrarFactura(int position, String invId, View view){
        deleteInvoiceDialog(invId, view);
    }

    private void deleteInvoiceDialog(String inv_id, View v){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final InvoiceDML invoiceDML = new InvoiceDML(getActivity());
        final boolean lastInvoice = getLastInvoice(inv_id);
        if(lastInvoice){
            final Invoice i = getInvoice(inv_id);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_borrarFacturaTitulo) + " " + i.getInv_number());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarFactura)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String invId = cm.borrarFile(c.FACTURA_TYPE, getActivity());
                            invoiceDML.borrarInvoice(new DatabaseHelper(getActivity()), invId, getActivity());

                            String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                                    + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_FACTURAS
                                    + File.separator + i.getInv_numFactRed() + ".pdf";
                            cm.borrarPdf(nombre_completo);
                            Toast.makeText(getActivity(), i.getInv_numFactRed() + " " + getString(R.string.confirmacionBorrar), Toast.LENGTH_LONG).show();

                            //refresh fragment
  /*                          Intent i = new Intent(getContext(), MenuPrincipal.class);
                            getActivity().finish();
                            startActivity(i);
*/                          recyclerView.setVisibility(View.GONE);
                            InvoiceFragment invFragment = new InvoiceFragment();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.invoiceFragmentLayout, invFragment, invFragment.getTag()).commit();
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
            Toast.makeText(getActivity(), R.string.borrarInvoiceError, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getLastInvoice(String inv_id){
        boolean lastInvoice = false;
        CommonMethods cm = new CommonMethodsImplementation();
        InvoiceDML inv = new InvoiceDML(getActivity());
        LastFileDML lfiDml = new LastFileDML(getActivity());
        String invId = cm.getLastInvoice(getActivity());
        if(invId == null || invId.equals("-1")){
            lastInvoice = false;
        }
        else if(!inv_id.equals(invId)){
            lastInvoice = false;
        }
        else{
            lastInvoice = true;
        }
        return lastInvoice;
    }

    private Invoice getInvoice(String inv_id){
        InvoiceDML invoiceDML = new InvoiceDML(getActivity());
        return invoiceDML.getInvoiceById(new DatabaseHelper(getActivity()), Integer.parseInt(inv_id), getActivity());
    }

}
