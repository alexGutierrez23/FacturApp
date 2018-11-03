package com.example.usuario.testingbd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.InvoiceMethodsImplementation;
import appImplementations.RegistrationMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import appInterfaces.RegistrationMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Invoice;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;
import sqlite.repo.InvoiceDML;

public class Factura3 extends AppCompatActivity {

    private Button btnMasDatosFact, btnGenerarFact;
    private EditText cantidadViajesFactura, infoAdicionalFactura;

    private HashMap<String, String> factura = null;
    private HashMap<String, String> tripFactura = null;
    private String invoiceId = null;
    private String tdaId = null;
    private CustomerTesting customerTesting = null;
    private ArrayList<InformationTesting> infoTesting = null;
    ArrayList<String> infoIds = null;
    private Invoice invoice = null;

    Invoice finalInvoice = null;

    private boolean backFromFactura3;

    private static ArrayList<String> nombres = null;

    private InvoiceDetails id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura3);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_factura3);
        if(!backFromFactura3){
            act.setDisplayHomeAsUpEnabled(true);
        }

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        Intent i = getIntent();
        tripFactura = (HashMap<String, String>) i.getSerializableExtra("tripFactura");
        customerTesting = (CustomerTesting) i.getSerializableExtra("var_customerTesting");
        Bundle formulario = this.getIntent().getExtras();
        invoice = (Invoice) i.getSerializableExtra("var_invoice");
        tdaId = formulario.getString("tdaId");
        infoIds = (ArrayList<String>) i.getSerializableExtra("var_infoIds");
        backFromFactura3 = formulario.getBoolean("var_backFromFactura3");

      //  cusId = formulario.getString("var_cusId");
        //    i.putExtra("datosClienteMap", datosCliente);
        //    i.putExtra("var_invoiceId", invoiceId);

        cantidadViajesFactura = (EditText) findViewById(R.id.cantidadViajes);
        infoAdicionalFactura = (EditText) findViewById(R.id.informacionAdicional);
        btnMasDatosFact = (Button) findViewById(R.id.masDatosFact);
        btnGenerarFact = (Button) findViewById(R.id.generarFact);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!backFromFactura3){
            Intent i = new Intent(this, Factura2.class);
            i.putExtra("var_invoice", invoice);
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_customerTesting", customerTesting);
            i.putExtra("var_infoIds", infoIds);
            i.putExtra("var_backFromFactura3", false);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "No se puede volver atrás. Finalice la factura, por favor.", Toast.LENGTH_LONG).show();
        }
    }

    public void generarFactura(View v) throws FileNotFoundException, DocumentException {
        InvoiceMethods invMethods = new InvoiceMethodsImplementation();
        CommonMethods cm = new CommonMethodsImplementation();
        RegistrationMethods rm = new RegistrationMethodsImplementation();
        Constants c = new Constants();
        invoiceId = invMethods.createInvoice(this, invoice, tdaId, null, null, null);

        Trip trip = fillTrip();
        Information information = fillInformation(trip);
        //Trip is created inside createInformation()
        String infoId = invMethods.createInformation(information, invoiceId, trip, this);
        finalInvoice = invMethods.getInvoice(Integer.parseInt(invoiceId), this);

        if(infoIds == null){
            infoIds = new ArrayList<>();
            infoIds.add(infoId);
        }
        else{
            infoIds.add(infoId);
        }
        ArrayList<Trip> trips = new ArrayList<>();

        ArrayList<Information> info = new ArrayList<>();
        for(String id : infoIds){
            Information inf = cm.getInfo(id, this);
            info.add(inf);
        }
        for(Information i : info){
            Trip t = invMethods.getTrip(i.getInf_trp_id(), this);
            trips.add(t);
        }

        invMethods.updateInformationForInvoice(infoIds, invoiceId, this);
        invMethods.updateTrip(infoIds, finalInvoice.getInv_number(), this);

    //    Customer customer = invMethods.getCustomerByInvoiceId(invoiceId, this);
    //    Address customerAddress = invMethods.getAddressById(customer.getCus_adr_id(), this);
        AddressTesting customerAddress = customerTesting.getAddress();
        Taxi_driver_account tda = invMethods.getTda(tdaId, this);
        Address tdaAddress = invMethods.getAddressById(tda.getTda_adr_id(), this);
        writePdf(finalInvoice, trips, info, customerAddress, tdaAddress, tda);
        cm.insertarLastFile(c.FACTURA_TYPE, Integer.valueOf(invoiceId), this);

        //createDialog(v, tda);
        finish();
        Intent i = new Intent(this, MenuPrincipal.class);
        startActivity(i);
    }

    public void createDialog(View v, Taxi_driver_account tda){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Factura3.this);
        //final Intent i = new Intent(this, MenuPrincipal.class);
        final Taxi_driver_account taxi_driver_account = tda;
        builder.setTitle(getString(R.string.dialog_enviarFacturaTitulo) + " " + finalInvoice.getInv_number());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.dialog_enviarFactura)
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviar(finalInvoice, taxi_driver_account);
                        //startActivity(i);
                        //finish();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                        finish();
                        //startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private Trip fillTrip(){
        InvoiceMethods im = new InvoiceMethodsImplementation();
        //Invoice invoice = im.getInvoice(Integer.parseInt(invoiceId), this);
        Trip trip = new Trip();
        trip.setTrp_price(tripFactura.get("precioFactura"));
        trip.setTrp_start(tripFactura.get("origenFactura"));
        trip.setTrp_finish(tripFactura.get("destinoFactura"));
        //trip.setTrp_number(invoice.getInv_number());
        return trip;
    }

    private Information fillInformation(Trip trip){
        CommonMethods cm = new CommonMethodsImplementation();
        Information information = new Information();
        information.setInf_additionalInfo(infoAdicionalFactura.getText().toString());

        if (TextUtils.isEmpty(trip.getTrp_price())) {
            information.setInf_vat_price("0.00");
            information.setInf_no_vat_price("0.00");
            information.setInf_vat("0.00");
            //  fd.setIva(0.0);
        } else {
            String vatPrice = trip.getTrp_price();
            Double price = Double.parseDouble(vatPrice);
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#.00", dfs);
            String pr = df.format(price);
            String sacariva = df.format(cm.sacarIva(price));
            String sacarSinIva = df.format(cm.sacarPrecioSinIva(price));
            information.setInf_vat_price(pr);
            String noVatPrice = sacarSinIva;
            information.setInf_no_vat_price(noVatPrice);
            String vat = sacariva;
            information.setInf_vat(vat);
            //   fd.setIva(sacarIva(Double.valueOf(txtPrecio.getText().toString())));
            //   imp = (int) Math.floor(fd.getIMPUESTOS()*100);
        }
        if(TextUtils.isEmpty(cantidadViajesFactura.getText().toString()))  //No se pueden dejar vacíos. Hay que evitar que dé al botón generarPDF
            information.setInf_quantity("0");
        else {
            information.setInf_quantity(cantidadViajesFactura.getText().toString());
        }
        String concepto = trip.getTrp_start() + " - " + trip.getTrp_finish();
        information.setInf_item(concepto);
        return information;
    }

    private InformationTesting fillInformationTimeBeing(){
        return null;
    }

    public void masDatosFactura(View v){
        Intent i = new Intent(this, Factura2.class);

        InvoiceMethods invMethods = new InvoiceMethodsImplementation();
        Trip trip = fillTrip();
        Information information = fillInformation(trip);
        //Trip is created inside createInformation()
        String infoId = invMethods.createInformation(information, null, trip, this);
        if(infoIds == null){
            infoIds = new ArrayList<>();
            infoIds.add(infoId);
        }
        else{
            infoIds.add(infoId);
        }

        i.putExtra("var_invoice", invoice);
        i.putExtra("tdaId", tdaId);
        i.putExtra("var_customerTesting", customerTesting);
        i.putExtra("var_infoIds", infoIds);
        i.putExtra("var_backFromFactura3", true);
        startActivity(i);
        finish();
    }

    public void enviar(Invoice invoice, Taxi_driver_account tda){
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();

        String emailNombreArchivo = invoice.getInv_numFactRed() + ".pdf";
        String emailNumFactura = invoice.getInv_number();
        String[] to = {tda.getTda_email()};
        String[] cc = {customerTesting.getEmail()};
        String mensaje = getString(R.string.invoiceEmailSubject) + invoice.getInv_date() + "";
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_FACTURAS
                + File.separator + emailNombreArchivo;

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, cc, "" + "Factura " + emailNumFactura, mensaje);
        finish();
        startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    private void writePdf(Invoice invoice, ArrayList<Trip> trips, ArrayList<Information> info,
                          AddressTesting custAddress,
                          Address tdaAddress, Taxi_driver_account tda) throws DocumentException, FileNotFoundException{
        String sdCardRoot = null;
        Constants c = null;
        CommonMethods cm = null;

        File pdfDir = new File(Environment.getExternalStorageDirectory()
                + File.separator + c.NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + c.GENERADOS_FACTURAS);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP
                + File.separator + c.GENERADOS_FACTURAS
                + File.separator + invoice.getInv_numFactRed() + ".pdf";

        File outputFile = new File(nombre_completo);

        try {
            pdfSubDir.mkdirs();
            c = new Constants();
            sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                    + c.NOMBRE_CARPETA_APP
                    + File.separator + c.GENERADOS_FACTURAS
                    + File.separator + invoice.getInv_numFactRed() + ".pdf";
            Document documento = new Document(PageSize.A4);

            PdfWriter pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(sdCardRoot));
            documento.open();   //Ancho de impresion = 48mm

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            String htmlToPdf = crearFactura(invoice, trips, info, custAddress, tdaAddress, tda).toString();
                    //"<html><head></head><body>hola</body></html>";

            worker.parseXHtml(pdfWriter, documento, new StringReader(htmlToPdf));
            documento.close();
            System.gc();
            Toast.makeText(this, "Factura" + invoice.getInv_number() + " generada.", Toast.LENGTH_LONG).show();
            InvoiceMethods im = new InvoiceMethodsImplementation();
            im.uploadPdfInvoice(String.valueOf(invoice.getInv_id()), sdCardRoot, this);
        }catch (IOException e){
            Log.e("IOEXCEPTION ", e.getMessage());
            e.printStackTrace();
        }
        catch (DocumentException de){
            Log.e("DOCUMENT EXCEPTION ", de.getMessage());
            de.printStackTrace();
        }
    }

    private StringBuffer crearFactura(Invoice invoice, ArrayList<Trip> trips, ArrayList<Information> info,
                                      AddressTesting custAddress, Address tdaAddress, Taxi_driver_account tda){
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
            sb.append("</big><br/><br/><br/><br/></td>");
        }
        else{
            sb.append("</big><br/><br/><br/><br/></td>");
        }
        sb.append("<td width=5% > &nbsp;</td>");
        //"<td width=60% ><big><b>Cliente:</b><br/>"  + "Juan Carlos Sánchez de Gracia" +
        //"<br/>NIF/CIF: " + "50077090-H" + "<br/>" + "C/ Eduardo Barreiros, 3" + "<br/>" +
        //"28041, Madrid" + "</big><br/><br/><br/><br/></td></tr>" +
        sb.append("<td width=50% ><big><b>Cliente:</b><br/>");
        sb.append(customerTesting.getName());
        sb.append("<br/>NIF/CIF: ");
        sb.append(customerTesting.getNif());
        sb.append("<br/>");
        if(!TextUtils.isEmpty(custAddress.getStreet())){
            if(!TextUtils.isEmpty(custAddress.getNumber())){
                sb.append(custAddress.getStreet() + ", " + custAddress.getNumber());
            }
            else{
                sb.append(custAddress.getStreet());
            }
        }
        sb.append("<br/>");
        if(!TextUtils.isEmpty(custAddress.getPostcode())){
            sb.append(custAddress.getPostcode());
        }
        if(!TextUtils.isEmpty(custAddress.getTown())){
            sb.append(", ");
            sb.append(custAddress.getTown());
        }
        if(!TextUtils.isEmpty(custAddress.getCounty())){
            sb.append(", ");
            sb.append(custAddress.getCounty());
        }
        sb.append("</big><br/><br/><br/><br/></td></tr>");
        sb.append("</table>");
        sb.append("</head>");

        sb.append("<body style=\"height:297mm; width:210mm; margin-left:auto; margin-right:auto;\">");
        sb.append("<table width=40% align=left cellspacing=2 cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append("<b>Nº FACTURA</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append("<b>FECHA</b></td></tr>");
        sb.append("<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append(invoice.getInv_number());
        sb.append("</td><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append(invoice.getInv_date());
        sb.append("</td></tr></table>");

        sb.append("<table width=100%><tr><td><br/><br/><br/></td></tr></table>");

        sb.append("<table width=100% cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td align=center width=65% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>CONCEPTO</b></td>");
        sb.append("<td align=center width=25% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>CANT.</b></td>");
        sb.append("<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>PRECIO</b></td>");
        sb.append("<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>IMPUESTOS</b></td>");
        sb.append("<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>IVA</b></td>");
        sb.append("<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>TOTAL</b></td></tr>");
        sb.append(rellenarFactura(info));
        sb.append("</table>");

        sb.append("<table width=100%><tr><td><br/><br/><br/></td></tr></table>");

        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>NETO</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>IVA</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>TOTAL FACTURA</b></td></tr>");
        sb.append(rellenarPreciosFinales(invoice, info));
        sb.append("</table>");

        sb.append("<table width=100%><tr><td><br/><br/><br/></td></tr></table>");

        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append("<b>INFORMACIÓN ADICIONAL</b></td></tr>");
        sb.append("<tr><td style=\"padding-left: 20px; padding-top: 5px; padding-bottom: 5px;\">");
        sb.append(rellenarInformacion(info));
        sb.append("</td></tr></table>");

        sb.append("</body></html>");

        return sb;
    }

    private static void cargarArchivoTxt(String archivo) throws FileNotFoundException {
        File file = new File(archivo);
        Scanner lector = new Scanner(file);
        nombres = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        while(lector.hasNext()){
            String linea = lector.nextLine();
            String[] lineaDividida = linea.split("\n\r");
            if(!linea.equals(null) || !linea.isEmpty()){
                String tick = lineaDividida[0];
                tick = quitarLast(tick);
                names.add(tick);
            }
        }
        for(String st : names){
            if(!st.isEmpty())
                nombres.add(st);
        }
    }

    private void crearFicheroAgain(File f){
        ArrayList<String> ast = new ArrayList<>();
        try {
            String cadena;
            FileReader fr = new FileReader(f);
            BufferedReader b = new BufferedReader(fr);
            while((cadena = b.readLine())!=null) {
                cadena = eliminarEspaciosEnBlanco(cadena);
                cadena = quitarLast(cadena);
                if(!cadena.isEmpty()) {
                    ast.add(cadena);
                }
            }
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        f.delete();

        File logTxt = new File(Environment.getExternalStorageDirectory() + File.separator
                + "Servicio Transporte" + File.separator + "Mis_Facturas" + File.separator + "(NO BORRAR)log_facturas.txt");
        //Creo el nuevo archivo txt
        OutputStreamWriter fout = null;
        for(String s : ast){
            try {
                fout = new OutputStreamWriter(
                        new FileOutputStream(f,true));
                fout.write(s);
                fout.write("\n");
                fout.close();

            } catch (Exception ex) {
                Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
            }
        }
    }

    private static String quitarLast(String st){
        return st.replace("(-last)", "");
    }

    public String eliminarEspaciosEnBlanco(String st){
        String sol = "";
        for (int x=0; x < st.length(); x++) {
            if (st.charAt(x) != ' ')
                sol += st.charAt(x);
        }
        return sol;
    }

    private String rellenarFactura(ArrayList<Information> info){
        String st = "";
        int contadorFilas = 0;
        for(Information inf : info){
            if(contadorFilas%2 != 0){  //Coloreamos filas con distintos colores
                st = st + "<tr style=\"background-color: lightgrey;\">" +
                        "<td align=center width=65% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_item() + "</b></td>" +
                        "<td align=center width=25% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_quantity() + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_no_vat_price() + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + "10.0%" + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_vat() + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_vat_price() + "</b></td></tr>";
            }
            else{
                st = st + "<tr>" +
                        "<td align=center width=65% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_item() + "</b></td>" +
                        "<td align=center width=25% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_quantity() + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_no_vat_price() + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + "10.0%" + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_vat() + "</b></td>" +
                        "<td align=center width=40% style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + inf.getInf_vat_price() + "</b></td></tr>";
            }
            contadorFilas++;
        }
        return st;
    }

    private String rellenarPreciosFinales(Invoice invoice, ArrayList<Information> info){
        double neto = 0.0;
        double iva = 0.0;
        double total = 0.0;
        for(Information inf : info){
            neto = neto + Double.parseDouble(inf.getInf_no_vat_price());
            iva = iva + Double.parseDouble(inf.getInf_vat());
            total = total + Double.parseDouble(inf.getInf_vat_price());
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
        InvoiceDML invoiceDML = new InvoiceDML(this);
        invoice.setInv_price(total1);
        invoice.setInv_deleted("0");
        invoiceDML.update(invoice, new DatabaseHelper(this).getWritableDatabase());

        return "<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + neto1 + "€</b></td>"
                + "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + iva1 + "€</b></td>" +
                "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>"+ total1 + "€</b></td></tr>";
    }

    private String rellenarInformacion(ArrayList<Information> info){
        CommonMethods cm = new CommonMethodsImplementation();
        String st = "";
        if(info.size() != 0) {
            for (Information inf : info) {
                st = st + cm.checkString(inf.getInf_additionalInfo()) + " <br/>";
            }
        }
        else {
            st = st + " <br/>";
        }
        return st;
    }

}
