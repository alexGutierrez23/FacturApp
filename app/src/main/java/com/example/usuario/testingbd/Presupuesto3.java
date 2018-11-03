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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
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

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.InvoiceMethodsImplementation;
import appImplementations.PresupuestoMethodsImplementation;
import appImplementations.RegistrationMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import appInterfaces.PresupuestoMethods;
import appInterfaces.RegistrationMethods;
import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Presupuesto;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;

public class Presupuesto3 extends AppCompatActivity {

    private Button btnMasDatosFact, btnGenerarFact;
    private EditText cantidadViajesFactura, infoAdicionalFactura;

    private HashMap<String, String> presupuesto = null;
    private HashMap<String, String> tripPresupuesto = null;
    private String presupuestoId = null;
    private String tdaId = null;
    private CustomerTesting customerTesting = null;
    private ArrayList<InformationTesting> infoTesting = null;
    ArrayList<String> infoIds = null;

    private Presupuesto finalPresupuesto = null;
    private Presupuesto var_presupuesto = null;

    private static ArrayList<String> nombres = null;

    private boolean backFromPresupuesto3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto3);

        Intent i = getIntent();
        tripPresupuesto = (HashMap<String, String>) i.getSerializableExtra("tripPresupuesto");
        customerTesting = (CustomerTesting) i.getSerializableExtra("var_customerTesting");
        infoTesting = (ArrayList<InformationTesting>) i.getSerializableExtra("var_infoTesting");
        infoIds = (ArrayList<String>) i.getSerializableExtra("var_infoIds");
        var_presupuesto = (Presupuesto) i.getSerializableExtra("var_presupuesto");
        Bundle formulario = this.getIntent().getExtras();
        //presupuestoId = formulario.getString("var_presupuestoId");
        tdaId = formulario.getString("tdaId");
        backFromPresupuesto3 = formulario.getBoolean("var_backFromPresupuesto3");

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_presupuesto3);
        if(!backFromPresupuesto3){
            act.setDisplayHomeAsUpEnabled(true);
        }

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

        cantidadViajesFactura = (EditText) findViewById(R.id.cantidadViajes);
        infoAdicionalFactura = (EditText) findViewById(R.id.informacionAdicional);
        btnMasDatosFact = (Button) findViewById(R.id.masDatosPres);
        btnGenerarFact = (Button) findViewById(R.id.generarPres);
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
        if(!backFromPresupuesto3){
            Intent i = new Intent(this, Presupuesto2.class);
            i.putExtra("var_presupuesto", var_presupuesto);
            i.putExtra("tdaId", tdaId);
            i.putExtra("var_customerTesting", customerTesting);
            i.putExtra("var_infoIds", infoIds);
            i.putExtra("var_backFromPresupuesto3", false);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "No se puede volver atrás. Finalice el presupuesto, por favor.", Toast.LENGTH_LONG).show();
        }
    }

    public void generarPresupuesto(View v) throws FileNotFoundException, DocumentException {
        PresupuestoMethods preMethods = new PresupuestoMethodsImplementation();
        RegistrationMethods rm = new RegistrationMethodsImplementation();
        CommonMethods cm = new CommonMethodsImplementation();
        Constants c = new Constants();
        InvoiceMethods invMethods = new InvoiceMethodsImplementation();
        Log.e("Fecha PRE: ", var_presupuesto.getPre_date());

        presupuestoId = preMethods.createPresupuesto(this, var_presupuesto, tdaId, null, null, null);

        Trip trip = fillTrip();
        Information information = fillInformation(trip);
        //Trip is created inside createInformation()
        String infoId = preMethods.createInformation(information, presupuestoId, trip, this);

        finalPresupuesto = preMethods.getPresupuesto(Integer.parseInt(presupuestoId), this);
       // ArrayList<Information> info = preMethods.getInformationLinkedToPresupuestoId(presupuestoId, this);
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

        preMethods.updateInformationForPresupuesto(infoIds, presupuestoId, this);
        invMethods.updateTrip(infoIds, var_presupuesto.getPre_number(), this);

    //    Customer customer = preMethods.getCustomerByPresupuestoId(presupuestoId, this);
    //    Address customerAddress = invMethods.getAddressById(customer.getCus_adr_id(), this);
        AddressTesting customerAddress = customerTesting.getAddress();
        Taxi_driver_account tda = invMethods.getTda(tdaId, this);
        Address tdaAddress = invMethods.getAddressById(tda.getTda_adr_id(), this);
        writePdf(finalPresupuesto, trips, info, customerAddress, tdaAddress, tda);
        cm.insertarLastFile(c.PRESUPUESTO_TYPE, Integer.valueOf(presupuestoId), this);
        //enviarPresupuestoDialog(v, tda);
        Intent i = new Intent(this, MenuPrincipal.class);
        finish();
        startActivity(i);
    }

    public void enviarPresupuestoDialog(View v, Taxi_driver_account tda){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Presupuesto3.this);
        final Intent i = new Intent(this, MenuPrincipal.class);
        final Taxi_driver_account taxi_driver_account = tda;
        builder.setTitle(getString(R.string.dialog_enviarPresupuestoTitulo) + " " + finalPresupuesto.getPre_number());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.dialog_enviarPresupuesto)
                .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        enviar(finalPresupuesto, taxi_driver_account);
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                        finish();
                        startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private Trip fillTrip(){
        PresupuestoMethods preMethods = new PresupuestoMethodsImplementation();
        //Presupuesto presupuesto = preMethods.getPresupuesto(Integer.parseInt(presupuestoId), this);
        Trip trip = new Trip();
        trip.setTrp_price(tripPresupuesto.get("precioPresupuesto"));
        trip.setTrp_start(tripPresupuesto.get("origenPresupuesto"));
        trip.setTrp_finish(tripPresupuesto.get("destinoPresupuesto"));
        //trip.setTrp_number(presupuesto.getPre_number());
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

    public void masDatosPresupuesto(View v){
        Intent i = new Intent(this, Presupuesto2.class);

        PresupuestoMethods preMethods = new PresupuestoMethodsImplementation();
        Trip trip = fillTrip();
        Information information = fillInformation(trip);
        //Trip is created inside createInformation()
        String infoId = preMethods.createInformation(information, null, trip, this);

        if(infoIds == null){
            infoIds = new ArrayList<>();
            infoIds.add(infoId);
        }
        else{
            infoIds.add(infoId);
        }

        i.putExtra("var_presupuesto", var_presupuesto);
        i.putExtra("tdaId", tdaId);
        i.putExtra("var_customerTesting", customerTesting);
        i.putExtra("var_infoIds", infoIds);
        i.putExtra("var_backFromPresupuesto3", true);

        startActivity(i);
        finish();
    }

    public void enviar(Presupuesto presupuesto, Taxi_driver_account tda){
        Constants c = new Constants();
        CommonMethods cm = new CommonMethodsImplementation();

        String emailNumFactura = presupuesto.getPre_number();
        String[] to = {tda.getTda_email()};
        String[] cc = {customerTesting.getEmail()};
        String mensaje = getString(R.string.presupuestoEmailSubject) + presupuesto.getPre_date() + "";
        String sdCardRoot = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_PRESUPUESTOS + File.separator + presupuesto.getPre_numPresupuestoRed() + ".pdf";

        Intent emailIntent = cm.enviarPdfEmail(sdCardRoot, to, cc, "" + "Presupuesto " + emailNumFactura, mensaje);

        startActivity(Intent.createChooser(emailIntent, "Email "));

        Intent i = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(i);
    }

    private void writePdf(Presupuesto presupuesto, ArrayList<Trip> trips, ArrayList<Information> info,
                          AddressTesting custAddress, Address tdaAddress, Taxi_driver_account tda) throws DocumentException, FileNotFoundException{
        String sdCardRoot = null;
        Constants c = null;
        CommonMethods cm = null;

        File pdfDir = new File(Environment.getExternalStorageDirectory() + File.separator + c.NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + c.GENERADOS_PRESUPUESTOS);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_PRESUPUESTOS + File.separator + presupuesto.getPre_numPresupuestoRed() + ".pdf";

        File outputFile = new File(nombre_completo);

        try {
            cm = new CommonMethodsImplementation();
            pdfSubDir.mkdirs();
            c = new Constants();
            sdCardRoot = Environment.getExternalStorageDirectory()
                    + File.separator + c.NOMBRE_CARPETA_APP
                    + File.separator + presupuesto.getPre_numPresupuestoRed() + ".pdf";

            String sdCardRoot2 = nombre_completo;

            Document documento = new Document(PageSize.A4);

            PdfWriter pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(sdCardRoot));
            documento.open();   //Ancho de impresion = 48mm

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            String htmlToPdf = crearPresupuesto(presupuesto, trips, info, custAddress, tdaAddress, tda).toString();

                //"<html><head></head><body>hola</body></html>";

            worker.parseXHtml(pdfWriter, documento, new StringReader(htmlToPdf));
            documento.close();

            addWatermarkToPdf(sdCardRoot, sdCardRoot2);

            //Borramos el primer presupuesto creado en sdCardRoot
            cm.borrarPdf(sdCardRoot);

            System.gc();
            Toast.makeText(this, "Presupuesto" + presupuesto.getPre_number() + " generado.", Toast.LENGTH_LONG).show();
            PresupuestoMethods pm = new PresupuestoMethodsImplementation();
            pm.uploadPdfPresupuesto(String.valueOf(presupuesto.getPre_id()), sdCardRoot2, this);
        }catch (IOException e){
            Log.e("IOEXCEPTION ", e.getMessage());
            e.printStackTrace();
        }
        catch (DocumentException de){
            Log.e("DOCUMENT EXCEPTION ", de.getMessage());
            de.printStackTrace();
        }
    }

    private StringBuffer crearPresupuesto(Presupuesto presupuesto, ArrayList<Trip> trips, ArrayList<Information> info,
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
        sb.append("<b>Nº PRESUPUESTO</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append("<b>FECHA</b></td></tr>");
        sb.append("<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append(presupuesto.getPre_number());
        sb.append("</td><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\">");
        sb.append(presupuesto.getPre_date());
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
        sb.append(rellenarPresupuesto(info));
        sb.append("</table>");

        sb.append("<table width=100%><tr><td><br/><br/><br/></td></tr></table>");

        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse\">");
        sb.append("<tr style=\"background-color: black; color: white;\">");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>NETO</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>IVA</b></td>");
        sb.append("<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>TOTAL PRESUPUESTO</b></td></tr>");
        sb.append(rellenarPreciosFinales(info));
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

    private String rellenarPresupuesto(ArrayList<Information> info){
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

    private String rellenarPreciosFinales(ArrayList<Information> info){
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
            neto1 = "0.00";
        }
        String total1 = df.format(total);
        if(total1.equals(".00")){
            neto1 = "0.00";
        }

        return "<tr><td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + neto1 + "€</b></td>"
                + "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>" + iva1 + "€</b></td>" +
                "<td align=center style=\"padding-top: 5px; padding-bottom: 5px;\"><b>"+ total1 + "€</b></td></tr>";
    }

    private String rellenarInformacion(ArrayList<Information> info){
        CommonMethods cm = new CommonMethodsImplementation();
        String st = "";
        if(info.size() != 0) {
            for (Information inf : info) {
                st = st + cm.checkString(inf.getInf_additionalInfo()) + ". <br/>";
            }
        }
        else
            st = " <br/>";
        return st;
    }

    public void addWatermarkToPdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte under = stamper.getUnderContent(1);
        Font f = new Font(Font.FontFamily.HELVETICA, 30);
        PdfContentByte over = stamper.getOverContent(1);
        Phrase  p = new Phrase(getString(R.string.watermarkPresupuesto), f);
        over.saveState();
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.1f);
        over.setGState(gs1);
        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, 400, -45);
        over.restoreState();
        stamper.close();
        reader.close();
    }
}
