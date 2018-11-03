package com.example.usuario.testingbd;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appImplementations.TicketMethodsImplementation;
import appInterfaces.CommonMethods;
import appInterfaces.TicketMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.model.Trip;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TicketDML;
import sqlite.repo.TripDML;

public class Ticket2 extends AppCompatActivity {

    private Button pickDate;
    private Button btnTicket;
    private EditText txtOrigen, txtDestino, txtPrecio, txtNombreCliente, txtNifCliente, txtCorreo;
    private String fecha;

    private TextView dateDisplay, hourDisplay;

    private int contFacturas = 1;
    private int contadorIteraciones = 0;
    private String yearOrigenApp = "2015";
    private String yearNuevo = "";
    private String emailNombreCarpeta, emailGenerados, emailTxtCorreo, emailFecha, emailNombreArchivo, emailNumFactura;
    private String origen, destino, precio;

    private String tda_id = null;
    private String tckId = null;
    private Trip trip = null;

    private static ArrayList<Ticket> tickets = new ArrayList<>();
    private static ArrayList<String> nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket2);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setTitle(R.string.title_activity_ticket2);
        act.setDisplayHomeAsUpEnabled(true);

        CommonMethodsImplementation commonMethodsImplementation = new CommonMethodsImplementation();
        commonMethodsImplementation.verifyStoragePermissions(this);

   /*     ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
*/
        CommonMethods cm = new CommonMethodsImplementation();
        txtNombreCliente = (EditText) findViewById(R.id.nombreClienteTicket);
        txtNifCliente = (EditText) findViewById(R.id.nifClienteTicket);
        txtCorreo = (EditText) findViewById(R.id.eCorreo);

        btnTicket = (Button) findViewById(R.id.button_GenTicket);
        cm.comprobarEstadoMemoria(); //Comprobamos si hay espacio en memoria externa

        dateDisplay = (TextView) findViewById(R.id.mostrarFecha2);
        pickDate = (Button) findViewById(R.id.selecFecha2);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mdatePicker;
                mdatePicker = new DatePickerDialog(Ticket2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth = selectedMonth + 1;
                        dateDisplay.setText( selectedDay + " / " + selectedMonth + " / " + selectedYear);
                    }
                }, year, month, day);//Yes 24 hour time
                mdatePicker.show();
            }
        });

        Bundle formulario = this.getIntent().getExtras();
        origen = formulario.getString("var_origenTicket");
        destino = formulario.getString("var_destinoTicket");
        precio = formulario.getString("var_precioTicket");
        tda_id = formulario.getString("tdaId");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                /*Intent i = new Intent(this, MenuPrincipal.class);
                startActivity(i);
                finish();*/
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Ticket1.class);
        startActivity(i);
        finish();
    }

    public void GenTicket(View v) {
        boolean emailOk = true;
        boolean fechaOk = false;

        if(!TextUtils.isEmpty(txtCorreo.getText().toString())){
            if(!Patterns.EMAIL_ADDRESS.matcher(txtCorreo.getText().toString()).matches()){
                txtCorreo.setError(null);
                txtCorreo.setError(getString(R.string.ticketEmailError));
                emailOk = false;
            }
            else{
                txtCorreo.setError(null);
                emailOk = true;
            }
        }
        if(TextUtils.isEmpty(dateDisplay.getText().toString()) || dateDisplay.getText().toString() == null){
            fechaOk = false;
            dateDisplay.setError("Seleccione una fecha");
        }
        else{
            fechaOk = true;
            dateDisplay.setError(null);
        }

        if(emailOk && fechaOk){
            Intent intentImprimir = new Intent(this, Imprimir.class);
            Intent intentMenuPrincipal = new Intent(this, MenuPrincipal.class);
/* tdaId da '0' -> Mirar por que */
            CommonMethods cm = new CommonMethodsImplementation();
            Constants c = new Constants();
            //Paso los parametros a la siguiente activity
            fecha = dateDisplay.getText().toString();
            yearOrigenApp = cm.takeYear(fecha);

            trip = new Trip();
            trip.setTrp_finish(destino);
            trip.setTrp_start(origen);
            trip.setTrp_price(precio);

            Ticket tck = new Ticket();
            tck.setTck_date(fecha);
            tck.setTck_time(cm.getHourPhone());
            tck.setTck_price(precio);
            tck.setTck_cus_id(null);

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            int tdaId = Integer.parseInt(tda_id);

            TicketMethods tckMethods = new TicketMethodsImplementation();
            // Create condition to check if we have a customer or not.

            String tda_id = String.valueOf(tdaId);
            tckId = tckMethods.createTicket(tck, tda_id, trip, null, null, this);

            if(!tckId.equals("-1")){  //Not "-1" means ticket has been added successfully
                generarTicket();
                cm.insertarLastFile(c.TICKET_TYPE, Integer.valueOf(tckId), this);
            }
            else{
                Toast.makeText(this, R.string.ticketErrorMessage, Toast.LENGTH_SHORT).show();
                startActivity(intentMenuPrincipal);
                finish();
            }

            intentImprimir.putExtra("tdaId", tda_id);
            intentImprimir.putExtra("tckId", tckId);
            intentImprimir.putExtra("txtCorreo", txtCorreo.getText().toString());
            intentImprimir.putExtra("nombreCliente", txtNombreCliente.getText().toString());
            intentImprimir.putExtra("nifCliente", txtNifCliente.getText().toString());
            startActivity(intentImprimir);
            finish();
        }
    }

    private boolean generarTicket() {
        boolean res = false;
        String sdCardRoot = null;
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        Constants c = null;
        CommonMethods cm = null;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        c = new Constants();
        TicketDML t = new TicketDML(this);
        Ticket tck = t.getTicketById(dbHelper, tckId, this);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(this);
        Taxi_driver_account tda = tdaDml.getTdaById(dbHelper, tda_id, this);
        Address adr = tdaDml.getAddressByTdaId(dbHelper, tda_id, this);
        TripDML tripDML = new TripDML(this);
        trip = tripDML.getTripByTicketId(dbHelper, tckId, this);

        File pdfDir = new File(tarjetaSD + File.separator + c.NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + c.GENERADOS_TICKETS);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                + c.NOMBRE_CARPETA_APP + File.separator
                + c.GENERADOS_TICKETS + File.separator + tck.getTck_numTickRed() + ".pdf";

        File outputFile = new File(nombre_completo);

        try {
            pdfSubDir.mkdirs();
            Document documento = new Document(PageSize.A6);
            PdfWriter pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(outputFile));

            documento.open();   //Ancho de impresion = 48mm

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            if(TextUtils.isEmpty(tda.getTda_logo_path()) && tda.getTda_logo().length > 0){
                Image imgLogo = Image.getInstance(tda.getTda_logo_path());
                documento.add(imgLogo);
            }

            String htmlToPdf = generateTicket(c, tck, adr, tda).toString();

            worker.parseXHtml(pdfWriter, documento, new StringReader(htmlToPdf));
            documento.close();
            Toast.makeText(this, "Ticket " + tck.getTck_number() + " creado.", Toast.LENGTH_LONG).show();
            res = true;
            TicketMethods tm = new TicketMethodsImplementation();
            tm.uploadPdfTicket(String.valueOf(tck.getTck_id()), nombre_completo, this);
        } catch (IOException e) {
            Log.e("IOEXCEPTION ", e.getMessage());
            e.printStackTrace();
        }
        catch (DocumentException de){
            Log.e("DOCUMENT EXCEPTION ", de.getMessage());
            de.printStackTrace();
        }
        return res;
    }

    private StringBuffer generateTicket(Constants c, Ticket tck, Address adr, Taxi_driver_account tda){
        CommonMethods cm = new CommonMethodsImplementation();
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head style=\"height:148mm; width:105mm; margin-left:auto; margin-right:auto;\">");
        sb.append("<img src=\"" + tda.getTda_logo_path() + "\" style=\"width:50px;height:50px;\"></img>");
        sb.append("<table border=0 width=100% cellspacing=0 cellpadding=0 style=\"text-align=center;\">");
        sb.append("<tr><td style=\"text-align=center;\">");
        sb.append(c.NOMBRE_CARPETA_APP);
        sb.append("</td></tr><br/><br/>");
        sb.append("<tr><td style=\"text-align=center;\">");
        sb.append(cm.checkString(tda.getTda_name()));
        sb.append("</td></tr>");
        sb.append("<tr><td style=\"text-align=center;\">");
        sb.append(cm.checkString(tda.getTda_nif()));
        sb.append("</td></tr>");
        if(!TextUtils.isEmpty(adr.getAdr_street())){
            sb.append("<tr><td style=\"text-align=center;\">");
            if(!TextUtils.isEmpty(adr.getAdr_number())){
                sb.append(adr.getAdr_street() + ", " + adr.getAdr_number());
            }
            else{
                sb.append(adr.getAdr_street());
            }
            sb.append("</td></tr>");
        }

        if(!TextUtils.isEmpty(adr.getAdr_town())){
            sb.append("<tr><td style=\"text-align=center;\">");
            sb.append(adr.getAdr_town());

            if(!TextUtils.isEmpty(adr.getAdr_postcode())){
                sb.append(", ");
                sb.append(adr.getAdr_postcode());

                if(!TextUtils.isEmpty(adr.getAdr_county())){
                    sb.append(", ");
                    sb.append(adr.getAdr_county());
                }
            }
            sb.append("</td></tr>");
        }

        if(!TextUtils.isEmpty(tda.getTda_license_number())){
            sb.append("<tr><td style=\"text-align=center;\">");
            sb.append("<br/>Licencia Taxi " + tda.getTda_license_number());
            sb.append("</td></tr>");
        }
        sb.append("</table></head>");

        sb.append("<div>");
        sb.append("<hr/>");
        sb.append("</div>");

        sb.append("<body style=\"height:148mm; width:105mm; margin-left:auto; margin-right:auto;\">");
        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse; text-align: left;\">");
        sb.append("<tr><td style=\"text-align=left;\">");
        sb.append(tck.getTck_date());
        sb.append("</td>");
        sb.append("<td style=\"text-align=right;\">");
        sb.append(tck.getTck_time());
        sb.append("</td></tr></table>");
        sb.append("<div>Factura reducida nº ");
        sb.append(tck.getTck_number());
        sb.append("</div>");

        sb.append("<div>");
        sb.append("<hr/>");
        sb.append("</div>");
/*
        sb.append("<div>Cliente: ");
        sb.append(txtNombreCliente.getText().toString());
        sb.append("<br/>N.I.F./C.I.F.: ");
        sb.append(txtNifCliente.getText().toString());
        sb.append("<br/>Origen: ");
        sb.append(trip.getTrp_start());
        sb.append("<br/>Destino: ");
        sb.append(trip.getTrp_finish());
        sb.append("</div>");
*/
        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse;\">");
        sb.append("<tr><td style=\"text-align=left;\">");
        sb.append("Cliente: </td>");
        sb.append("<td style=\"text-align=right;\">" + txtNombreCliente.getText().toString());
        sb.append("</td></tr>");
        sb.append("<tr><td style=\"text-align=left;\">N.I.F./C.I.F.: </td>");
        sb.append("<td style=\"text-align=right;\">" + txtNifCliente.getText().toString());
        sb.append("</td></tr>");
        sb.append("<tr><td style=\"text-align=left;\">Origen: </td>");
        sb.append("<td style=\"text-align=right;\">" + trip.getTrp_start());
        sb.append("</td></tr>");
        sb.append("<tr><td style=\"text-align=left;\">Destino: </td>");
        sb.append("<td style=\"text-align=right;\">" + trip.getTrp_finish() + "</td></tr>");
        sb.append("</table>");

        sb.append("<div>");
        sb.append("<hr/>");
        sb.append("</div>");

        sb.append("<table width=100% cellspacing=2 cellpadding=2 style=\"border-collapse: collapse;\">");
        sb.append("<tr><td style=\"text-align=left;\">");
        sb.append("<b>Total: </b>");
        sb.append("</td>");
        sb.append("<td style=\"text-align=right;\"><b>");
        sb.append(trip.getTrp_price());
        sb.append("€ (10% IVA incluido)</b>");
        sb.append("</td></tr></table>");

        sb.append("<br/><br/>");

        sb.append("<div style=\"text-align=center;\">");
        sb.append("Gracias por utilizar nuestro servicio.");
        sb.append("</div>");
        sb.append("<br/>");
        sb.append("<div style=\"text-align=center;\">");
        sb.append("Thank you for using our service.");
        sb.append("</div>");

        sb.append("</body></html>");

        return sb;
    }
}
