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

import com.example.usuario.testingbd.Adaptador;
import com.example.usuario.testingbd.DisplayPdf;
import com.example.usuario.testingbd.MenuPrincipal;
import com.example.usuario.testingbd.R;
import com.example.usuario.testingbd.Ticket1;

import java.io.File;
import java.util.ArrayList;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.TicketMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.TicketMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TicketDML;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment implements Adaptador.AdaptadorTicketInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Adaptador.AdaptadorTicketInterface mcallback;

    RecyclerView recyclerView = null;

    private static final String DIALOG_TYPE_EMAIL = "EMAIL";
    private static final String DIALOG_TYPE_DELETE = "DELETE";

    public TicketFragment() {
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
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
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
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentTicket);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerView);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabAddTicket = (FloatingActionButton) view.findViewById(R.id.fabAnyadirTicket);

        ArrayList<Ticket> ticketList = getTickets();
        Adaptador adaptador = null;
        Ticket[] ticket = null;

        if(!ticketList.isEmpty()){
            recyclerView.setVisibility(View.VISIBLE);
            ticket = new Ticket[ticketList.size()];
            for(int i = 0; i < ticketList.size(); i++){
                ticket[i] = ticketList.get(i);
            }
            adaptador = new Adaptador(ticket);
            adaptador.setAdaptadorTicketInterface(this);  //Inicializamos llamada a la interfaz
        }
        else{
            tv.setText("No hay tickets creados.");
            recyclerView.setVisibility(View.GONE);
        }

        if(fabAddTicket != null){
            fabAddTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), Ticket1.class);
                    startActivity(i);
                    ((MenuPrincipal)getActivity()).finish();
                }
            });
        }

      //  Adaptador adaptador = new Adaptador(new String[]{"Example 1", "Example 2", "Example 3",
      //      "Example 4", "Example 5", "Example 6", "Example 7"}); //aqui va la lista de objetos de ticket
        recyclerView.setAdapter(adaptador);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private ArrayList<Ticket> getTickets(){
        return ((MenuPrincipal) getActivity()).getTicketList();
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
    public void verTicketPdf(int position, String tckId){
        viewPdf(position, tckId);
    }

    private void viewPdf(int position, String tckId){
        Constants c = new Constants();
        Intent i = new Intent(getActivity(), DisplayPdf.class);
        i.putExtra("var_docId", tckId);
        i.putExtra("var_idType", c.TICKET_TYPE);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void enviarTicket(int position, String tckId, View view){
        createDialog(view, tckId);
    }

    public void createDialog(View v, String tckId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TicketMethods tm = new TicketMethodsImplementation();
        final Ticket ticket = tm.getTicket(tckId, getActivity());
        builder.setTitle(getString(R.string.dialog_enviarTicketTitulo) + " " + ticket.getTck_number());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.dialog_enviarTicket)
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviar(ticket);
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

    public void enviar(Ticket ticket){
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();
        Taxi_driver_account tda = getTda();

        String emailNombreArchivo = ticket.getTck_numTickRed() + ".pdf";
        String emailNumFactura = ticket.getTck_number();
        String[] to = {tda.getTda_email()};
        String mensaje = getString(R.string.ticketEmailSubject) + " " + ticket.getTck_date();
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_TICKETS
                + File.separator + emailNombreArchivo;

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, null, "" + "Ticket " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    @Override
    public void borrarTicket(int position, String tckId, View view){
        deleteTicketDialog(tckId, view);
    }

    private void deleteTicketDialog(String tck_id, View v){
        final Constants c = new Constants();
        final CommonMethods cm = new CommonMethodsImplementation();
        final TicketDML tckDml = new TicketDML(getActivity());
        boolean lastTicket = getLastTicket(tck_id);
        if(lastTicket){
            final Ticket t = getTicket(tck_id);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.dialog_borrarTicketTitulo) + " " + t.getTck_number());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarTicket)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final String tckid = cm.borrarFile(c.TICKET_TYPE, getActivity());
                            tckDml.borrarTicket(new DatabaseHelper(getActivity()), tckid, getActivity());

                            String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                                    + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_TICKETS
                                    + File.separator + t.getTck_numTickRed() + ".pdf";
                            cm.borrarPdf(nombre_completo);
                            Toast.makeText(getActivity(), t.getTck_numTickRed() + " " + getString(R.string.confirmacionBorrar), Toast.LENGTH_LONG).show();

                            //refresh fragment
                            recyclerView.setVisibility(View.GONE);
                            TicketFragment tckFragment = new TicketFragment();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.ticketFragmentLayout, tckFragment, tckFragment.getTag()).commit();


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
            Toast.makeText(getActivity(), R.string.borrarTicketError, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getLastTicket(String tckId){
        boolean lastTicket = false;
        CommonMethods cm = new CommonMethodsImplementation();
        String tck_id = cm.getLastTicket(getActivity());
        if(tck_id == null || tck_id.equals("-1")){
            lastTicket = false;
        }
        else if(!tck_id.equals(tckId)){
            lastTicket = false;
        }
        else{
            lastTicket = true;
        }
        return lastTicket;
    }

    private Ticket getTicket(String tck_id){
        TicketDML tckDml = new TicketDML(getActivity());
        return tckDml.getTicketById(new DatabaseHelper(getActivity()), tck_id, getActivity());
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
}
