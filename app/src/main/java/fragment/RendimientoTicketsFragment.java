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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.usuario.testingbd.AdaptadorRendimientoTicket;
import com.example.usuario.testingbd.DisplayPdf;
import com.example.usuario.testingbd.MenuPrincipal;
import com.example.usuario.testingbd.R;
import com.example.usuario.testingbd.Rendimiento;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.TicketMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.TicketMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TicketDML;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RendimientoTicketsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RendimientoTicketsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RendimientoTicketsFragment extends Fragment implements AdaptadorRendimientoTicket.AdaptadorRendimientoTicketInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AdaptadorRendimientoTicket.AdaptadorRendimientoTicketInterface mcallback;

    public RendimientoTicketsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RendimientoTickets.
     */
    // TODO: Rename and change types and number of parameters
    public static RendimientoTicketsFragment newInstance(String param1, String param2) {
        RendimientoTicketsFragment fragment = new RendimientoTicketsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rendimiento_tickets, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentRdtoTicket);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragmentRendimientoTickets_recyclerView);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabAddTicket = (FloatingActionButton) view.findViewById(R.id.fabAnyadirRdtoTicket);

        AdaptadorRendimientoTicket adaptadorRendimientoTicket = null;
        Ticket[] ticket = null;

        Map<String, ArrayList<Rendimiento>> rendimientoPorMes = refreshRendimientoTickets(getTickets());

        onCreateRendimientoTickets(rendimientoPorMes);

        ArrayList<Ticket> rendimientoTicketList = getRendimientoTickets();
        if(!rendimientoTicketList.isEmpty()){
            ticket = new Ticket[rendimientoTicketList.size()];
            for(int i = 0; i < rendimientoTicketList.size(); i++){
                ticket[i] = rendimientoTicketList.get(i);
            }
            adaptadorRendimientoTicket = new AdaptadorRendimientoTicket(ticket);
            adaptadorRendimientoTicket.setAdaptadorRendimientoTicketInterface(this);  //Inicializamos llamada a la interfaz
        }
        else{
            tv.setText("No se pueden generar rendimientos si no hay tickets creados.");
        }

        if(fabAddTicket != null){
            fabAddTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //refresh fragment
                    RendimientoTicketsFragment rdtoTckFragment = new RendimientoTicketsFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.rdtoTicketFragmentLayout, rdtoTckFragment, rdtoTckFragment.getTag()).commit();
                }
            });
        }

        recyclerView.setAdapter(adaptadorRendimientoTicket);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private ArrayList<Ticket> getRendimientoTickets(){
        return ((MenuPrincipal) getActivity()).getRendimientoTicketList();
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
    public void verRdtoTicketPdf(int position, String tckId){
            viewPdf(position, tckId);
    }

    private void viewPdf(int position, String tckId){
        Constants c = new Constants();
        Intent i = new Intent(getActivity(), DisplayPdf.class);
        i.putExtra("var_docId", tckId);
        i.putExtra("var_idType", c.RENDIMIENTO_TICKET_TYPE);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void enviarRdtoTicket(int position, String tckId, View view){
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
        Taxi_Driver txd = cm.getTxd(getActivity());
        String txdId = String.valueOf(txd.getTxd_id());
        String tdaId = cm.getTdaIdGivenTxdId(getActivity(), txdId);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(getActivity());
        Taxi_driver_account tda = tdaDml.getTdaById(new DatabaseHelper(getActivity()), tdaId, getActivity());

        String emailNombreArchivo = ticket.getTck_numTickRed() + ".pdf";
        String emailNumFactura = ticket.getTck_number();
        String[] to = {tda.getTda_email()};
        String mensaje = getString(R.string.ticketEmailSubject) + " " + ticket.getTck_date();
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_RENDIMIENTOS
                + File.separator + emailNombreArchivo;

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, null, "" + "Rendimiento Ticket " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    private void onCreateRendimientoTickets(Map<String, ArrayList<Rendimiento>> rendimientoPorMes){
        Iterator it = rendimientoPorMes.entrySet().iterator();
        for(Map.Entry<String, ArrayList<Rendimiento>> entry : rendimientoPorMes.entrySet()){
            try {
                createRendimientoPdf(entry.getKey(), entry.getValue());
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /*Este metodo va a guardar en cada mes una lista de ticket de tal forma
        que podemos crear automaticamente PDFs con el rendimiento de cada mes */
    private Map<String, ArrayList<Rendimiento>> refreshRendimientoTickets(ArrayList<Ticket> tckList){
        CommonMethods cm = new CommonMethodsImplementation();
        Map<String, ArrayList<Rendimiento>> rendimientosPorMes = new HashMap<>();  // clave sera el mes y valor sera Objeto tipo Rendimiento ticket
        ArrayList<Rendimiento> rdtoList = new ArrayList<>();
        for(Ticket tck : tckList){
            Rendimiento rdtoTicket = new Rendimiento();
            rdtoTicket.setName(tck.getTck_numTickRed());

            rdtoTicket.setDate(tck.getTck_date());
            rdtoTicket.setMonth(cm.getMonthFromDate(tck.getTck_date()));

            String year = cm.getYearFromDate(tck.getTck_date());
            year = year.replace(" ", "");
            rdtoTicket.setYear(year);
            rdtoTicket.setPrice(tck.getTck_price());

            String mesAnyo = cm.guessMonth(rdtoTicket.getMonth());
            mesAnyo = mesAnyo + "_" + rdtoTicket.getYear();

            if(!rendimientosPorMes.isEmpty()){
                if(rendimientosPorMes.containsKey(mesAnyo)){
                    rendimientosPorMes.get(mesAnyo).add(rdtoTicket);
                }
                else{
                    rdtoList.add(rdtoTicket);
                    rendimientosPorMes.put(mesAnyo, rdtoList);
                }
            }
            else{
                rdtoList.add(rdtoTicket);
                rendimientosPorMes.put(mesAnyo, rdtoList);
            }
        }
        return rendimientosPorMes;
    }

    private void createRendimientoPdf(String mesAnyoRendimiento, ArrayList<Rendimiento> rdtoTicketList)
            throws DocumentException, FileNotFoundException {

        Constants c = null;
        CommonMethods cm = new CommonMethodsImplementation();

        Taxi_Driver txd = cm.getTxd(getActivity());
        String txdId = String.valueOf(txd.getTxd_id());
        Taxi_driver_account tda = new Taxi_driver_account();
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(getActivity());
        int tda_id = tdaDml.getTdaIdByTxdId(new DatabaseHelper(getActivity()),
                txdId, getActivity());
        tda = tdaDml.getTdaById(new DatabaseHelper(getActivity()),
                String.valueOf(tda_id), getActivity());

        Address tdaAdr = cm.getAddressById(tda.getTda_adr_id(), getActivity());

        //Cogemos primer elemento de la lsita porque todos perteneceran al mismoAnyo
        String nombreArchivo = "Rendimiento-" + mesAnyoRendimiento;

        String[] temp1 = mesAnyoRendimiento.split("_");
        String year = temp1[temp1.length-1];

        File pdfDir = new File(Environment.getExternalStorageDirectory()
                + File.separator + c.NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + c.GENERADOS_RENDIMIENTOS
                + File.separator + c.GENERADOS_TICKETS + File.separator + year);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_RENDIMIENTOS + File.separator + c.GENERADOS_TICKETS
                + File.separator + year
                + File.separator + nombreArchivo + ".pdf";

        File outputFile = new File(nombre_completo);

        try {
            pdfSubDir.mkdirs();
            c = new Constants();

            Document documento = new Document(PageSize.NOTE);

            PdfWriter pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(nombre_completo));
            documento.open();   //Ancho de impresion = 48mm

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            String htmlToPdf = crearRendimiento(rdtoTicketList, tdaAdr, tda, nombreArchivo, mesAnyoRendimiento, nombre_completo).toString();

            worker.parseXHtml(pdfWriter, documento, new StringReader(htmlToPdf));
            documento.close();
            System.gc();
        }catch (IOException e){
            Log.e("IOEXCEPTION ", e.getMessage());
            e.printStackTrace();
        }
        catch (DocumentException de){
            Log.e("DOCUMENT EXCEPTION ", de.getMessage());
            de.printStackTrace();
        }
    }

    private StringBuffer crearRendimiento(ArrayList<Rendimiento> rdtoTicketList, Address tdaAddress,
                                          Taxi_driver_account tda, String nombreArchivo,
                                          String mesAnyoRendimiento, String nombre_completo){
        Constants c =new Constants();
        StringBuffer sb = new StringBuffer();
        CommonMethods cm = new CommonMethodsImplementation();
        sb.append("<html>");
        sb.append("<head style=\"height:297mm; width:210mm; margin-left:auto; margin-right:auto;\">");
        sb.append("<table border=0 width=100% align=center cellspacing=0 cellpadding=0 >");
        sb.append("<tr><td align=left width=45% ><big>");
        sb.append(c.NOMBRE_CARPETA_APP + "<br/>");
        if(!TextUtils.isEmpty(tda.getTda_name())){
            sb.append(tda.getTda_name());
            sb.append("<br/>");
        }
        if(!TextUtils.isEmpty(tda.getTda_nif())){
            sb.append("NIF: " + tda.getTda_nif());
            sb.append("<br/>");
        }
        if(!TextUtils.isEmpty(tdaAddress.getAdr_street())){
            sb.append(tdaAddress.getAdr_street());
        }
        if(!TextUtils.isEmpty(tdaAddress.getAdr_number())){
            sb.append(", ");
            sb.append(tdaAddress.getAdr_number());
        }
        if(!TextUtils.isEmpty(tdaAddress.getAdr_town())){
            sb.append("<br/>");
            sb.append(tdaAddress.getAdr_town());
            sb.append(", ");
        }
        if(!TextUtils.isEmpty(tdaAddress.getAdr_postcode())){
            sb.append(tdaAddress.getAdr_postcode());
            sb.append(", ");
        }
        if(!TextUtils.isEmpty(tdaAddress.getAdr_county())){
            sb.append(tdaAddress.getAdr_county());
        }

        if(!TextUtils.isEmpty(tda.getTda_license_number())){
            sb.append("<br/>" + tda.getTda_license_number());
            sb.append("</big><br/><br/><br/><br/></td></tr>");
        }
        else{
            sb.append("</big><br/><br/><br/><br/></td></tr>");
        }

        sb.append("</table>");
        sb.append("</head>");

        sb.append("<body style=\"height:297mm; width:210mm; margin-left:auto; margin-right:auto;\">");
        sb.append("<table width=60% align=left cellspacing=2 cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append("<b>Nº RENDIMIENTO</b></td></tr>");
        sb.append("<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append(nombreArchivo);
        sb.append("</td></tr></table>");

        sb.append("<table width=100%><tr><td><br/><br/><br/></td></tr></table>");

        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>NETO</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>IVA</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>TOTAL FACTURA</b></td></tr>");
        sb.append(rellenarPreciosFinales(rdtoTicketList, nombreArchivo, mesAnyoRendimiento, nombre_completo));
        sb.append("</table>");

        sb.append("</body></html>");

        return sb;
    }

    private String rellenarPreciosFinales(ArrayList<Rendimiento> rdtoTicketList, String nombreArchivo,
                                          String fecha, String nombre_completo){
        double neto = 0.0;
        double iva = 0.0;
        double total = 0.0;

        CommonMethods cm = new CommonMethodsImplementation();

        for(Rendimiento rdtoTicket : rdtoTicketList){
            String vatPrice = rdtoTicket.getPrice();
            if(vatPrice.equals("")){
                vatPrice = "0";
            }
            Double price = Double.parseDouble(vatPrice);
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#.00", dfs);
            String precio = df.format(price);
            String sacarIva = df.format(cm.sacarIva(price));
            String precioSinIva = df.format(cm.sacarPrecioSinIva(price));

            neto = neto + Double.parseDouble(precioSinIva);
            iva = iva + Double.parseDouble(sacarIva);
            total = total + Double.parseDouble(precio);
        }
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.00", dfs);
        String neto1 = df.format(neto);
        if(neto1.equals(".00")){
            neto1 = "0.00";
        }
        String iva1 = df.format(iva);
        if(iva1.equals(".00")){
            iva1 = "0.00";
        }
        String total1 = df.format(total);
        if(total1.equals(".00")){
            total1 = "0.00";
        }

        TicketDML tckDml = new TicketDML(getActivity());
        Ticket tck = new Ticket();
        tck.setTck_price(total1);
        tck.setTck_date(fecha);
        tck.setTck_numTickRed(nombreArchivo);
        tck.setTck_pdf(nombre_completo);
        ArrayList<Ticket> list = getRendimientoTickets();
        boolean exists = false;
        for(Ticket ticket : list){
            if(ticket.getTck_numTickRed().equals(nombreArchivo)){
                tck.setTck_id(tck.getTck_id());
                exists = true;
                break;
            }
        }
        if(!exists){
            tckDml.insertarTicket(new DatabaseHelper(getActivity()), tck, getActivity());
        }
        else{
            tckDml.update(tck, new DatabaseHelper(getActivity()).getWritableDatabase());
        }


        return "<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + neto1 + "€</b></td>"
                + "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + iva1 + "€</b></td>" +
                "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>"+ total1 + "€</b></td></tr>";
    }

}
