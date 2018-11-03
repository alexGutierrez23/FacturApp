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

import com.example.usuario.testingbd.AdaptadorRendimientoFactura;
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
import appImplementations.InvoiceMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Invoice;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.InvoiceDML;
import sqlite.repo.Taxi_driver_accountDML;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RendimientoFacturasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RendimientoFacturasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RendimientoFacturasFragment extends Fragment implements AdaptadorRendimientoFactura.AdaptadorRendimientoFacturaInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RendimientoFacturasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RendimientoFacturasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RendimientoFacturasFragment newInstance(String param1, String param2) {
        RendimientoFacturasFragment fragment = new RendimientoFacturasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rendimiento_facturas, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvFragmentRdtoFactura);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragmentRendimientoFacturas_recyclerView);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabRefresh = (FloatingActionButton) view.findViewById(R.id.fabAnyadirRdtoTicket);

        AdaptadorRendimientoFactura adaptadorRendimientoFactura = null;
        Invoice[] invoice = null;

        Map<String, ArrayList<Rendimiento>> rendimientoPorMes = refreshRendimientoFacturas(getInvoices());

        onCreateRendimientoFacturas(rendimientoPorMes);

        ArrayList<Invoice> rendimientoFacturaList = getRendimientoFacturas();
        if(!rendimientoFacturaList.isEmpty()){
            invoice = new Invoice[rendimientoFacturaList.size()];
            for(int i = 0; i < rendimientoFacturaList.size(); i++){
                invoice[i] = rendimientoFacturaList.get(i);
            }
            adaptadorRendimientoFactura = new AdaptadorRendimientoFactura(invoice);
            adaptadorRendimientoFactura.setAdaptadorRendimientoFacturaInterface(this);  //Inicializamos llamada a la interfaz
        }
        else{
            tv.setText("No se pueden generar rendimientos si no hay facturas creadas.");
        }

        if(fabRefresh != null){
            fabRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //refresh fragment
                    RendimientoFacturasFragment rdtoFacturasFragment = new RendimientoFacturasFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.rdtoFacturaFragmentLayout, rdtoFacturasFragment, rdtoFacturasFragment.getTag()).commit();
                }
            });
        }

        recyclerView.setAdapter(adaptadorRendimientoFactura);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private ArrayList<Invoice> getRendimientoFacturas(){
        return ((MenuPrincipal) getActivity()).getRendimientoFacturaList();
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

    private ArrayList<Invoice> getInvoices(){
        return ((MenuPrincipal) getActivity()).getInvoiceList();
    }

    @Override
    public void verRdtoFacturaPdf(int position, String invId){
        viewPdf(position, invId);
    }

    @Override
    public void enviarRdtoFactura(int position, String invId, View view){
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
                + File.separator + c.GENERADOS_RENDIMIENTOS
                + File.separator + emailNombreArchivo;

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, null, "" + "Rendimiento Factura " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    private void viewPdf(int position, String invId){
        Constants c = new Constants();
        Intent i = new Intent(getActivity(), DisplayPdf.class);
        i.putExtra("var_docId", invId);
        i.putExtra("var_idType", c.RENDIMIENTO_FACTURA_TYPE);
        startActivity(i);
        getActivity().finish();
    }

    private void onCreateRendimientoFacturas(Map<String, ArrayList<Rendimiento>> rendimientoPorMes){
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
    private Map<String, ArrayList<Rendimiento>> refreshRendimientoFacturas(ArrayList<Invoice> invList){
        CommonMethods cm = new CommonMethodsImplementation();
        Map<String, ArrayList<Rendimiento>> rendimientosPorMes = new HashMap<>();  // clave sera el mes y valor sera Objeto tipo Rendimiento ticket
        ArrayList<Rendimiento> rdtoList = new ArrayList<>();
        for(Invoice inv : invList){
            Rendimiento rdto = new Rendimiento();
            rdto.setName(inv.getInv_numFactRed());

            rdto.setDate(inv.getInv_date());
            rdto.setMonth(cm.getMonthFromDate(inv.getInv_date()));

            String year = cm.getYearFromDate(inv.getInv_date());
            year = year.replace(" ", "");
            rdto.setYear(year);
            rdto.setPrice(inv.getInv_price());

            String mesAnyo = cm.guessMonth(rdto.getMonth());
            mesAnyo = mesAnyo + "_" + rdto.getYear();

            if(!rendimientosPorMes.isEmpty()){
                if(rendimientosPorMes.containsKey(mesAnyo)){
                    rendimientosPorMes.get(mesAnyo).add(rdto);
                }
                else{
                    rdtoList.add(rdto);
                    rendimientosPorMes.put(mesAnyo, rdtoList);
                }
            }
            else{
                rdtoList.add(rdto);
                rendimientosPorMes.put(mesAnyo, rdtoList);
            }
        }
        return rendimientosPorMes;
    }

    private void createRendimientoPdf(String mesAnyoRendimiento, ArrayList<Rendimiento> rdtoList)
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
                + File.separator + c.GENERADOS_FACTURAS + File.separator + year);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_RENDIMIENTOS + File.separator + c.GENERADOS_FACTURAS + File.separator + year
                + File.separator + nombreArchivo + ".pdf";

        File outputFile = new File(nombre_completo);

        try {
            pdfSubDir.mkdirs();
            c = new Constants();

            Document documento = new Document(PageSize.NOTE);

            PdfWriter pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(nombre_completo));
            documento.open();   //Ancho de impresion = 48mm

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            String htmlToPdf = crearRendimiento(rdtoList, tdaAdr, tda, nombreArchivo, mesAnyoRendimiento, nombre_completo).toString();

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

    private StringBuffer crearRendimiento(ArrayList<Rendimiento> rdtoList, Address tdaAddress,
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
        sb.append(rellenarPreciosFinales(rdtoList, nombreArchivo, mesAnyoRendimiento, nombre_completo));
        sb.append("</table>");

        sb.append("</body></html>");

        return sb;
    }

    private String rellenarPreciosFinales(ArrayList<Rendimiento> rdtoList, String nombreArchivo,
                                          String fecha, String nombre_completo){
        double neto = 0.0;
        double iva = 0.0;
        double total = 0.0;

        CommonMethods cm = new CommonMethodsImplementation();

        for(Rendimiento rdto : rdtoList){
            String vatPrice = rdto.getPrice();
            if(vatPrice != null){
                if(vatPrice.equals("")){
                    vatPrice = "0";
                }
            }
            else{
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

        InvoiceDML invDml = new InvoiceDML(getActivity());
        Invoice inv = new Invoice();
        inv.setInv_date(fecha);
        inv.setInv_numFactRed(nombreArchivo);
        inv.setInv_pdf(nombre_completo);
        //tck.setTck_price(total1);

        ArrayList<Invoice> list = getRendimientoFacturas();
        boolean exists = false;
        for(Invoice invoice : list){
            if(invoice.getInv_numFactRed().equals(nombreArchivo)){
                inv.setInv_id(inv.getInv_id());
                exists = true;
                break;
            }
        }
        if(!exists){
            invDml.insertarInvoice(new DatabaseHelper(getActivity()), inv, getActivity());
        }
        else{
            invDml.update(inv, new DatabaseHelper(getActivity()).getWritableDatabase());
        }


        return "<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + neto1 + "€</b></td>"
                + "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + iva1 + "€</b></td>" +
                "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>"+ total1 + "€</b></td></tr>";
    }


}
