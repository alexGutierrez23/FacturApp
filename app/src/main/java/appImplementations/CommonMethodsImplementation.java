package appImplementations;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import Constants.Constants;
import appInterfaces.CommonMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.repo.AddressDML;
import sqlite.repo.InformationDML;
import sqlite.repo.LastFileDML;
import sqlite.repo.Taxi_DriverDML;
import sqlite.repo.Taxi_driver_accountDML;

/**
 * Created by Usuario on 15/01/2018.
 */

public class CommonMethodsImplementation implements CommonMethods {
    Constants constants = null;

    public String buscarArchivosGuardados(String tipo, String nombreCarpeta){
        constants = new Constants();
        String name = null;
        String nombreFicheroLogs = null;
        String generados = null;

        if(tipo.equals(constants.TICKET_TYPE)){
            generados = constants.GENERADOS_TICKETS;
            nombreFicheroLogs = constants.TICKETS_LOGS;
        }
        else if(tipo.equals(constants.FACTURA_TYPE)){
            generados = constants.GENERADOS_FACTURAS;
            nombreFicheroLogs = constants.FACTURAS_LOGS;
        }
        else if(tipo.equals(constants.PRESUPUESTO_TYPE)){
            generados = constants.GENERADOS_PRESUPUESTOS;
            nombreFicheroLogs = constants.PRESUPUESTOS_LOGS;
        }
        else if(tipo.equals(constants.HOJARUTA_TYPE)){
            generados = constants.GENERADOS_HOJASRUTA;
            nombreFicheroLogs = constants.HOJASRUTA_LOGS;
        }

        String tarjetaSD = Environment.getExternalStorageDirectory().toString();

        File pdfDir = new File(tarjetaSD + File.separator + nombreCarpeta);

        if(pdfDir.exists()){
            File pdfSubDir = new File(pdfDir.getPath() + File.separator + generados);
            if(pdfSubDir.exists()){

                String rutaLogTxt = pdfSubDir.getPath() + File.separator + nombreFicheroLogs;
                File logTxt = new File(rutaLogTxt);

                try {
                    if(logTxt.exists()) {
                        File file = new File(rutaLogTxt);
                        Scanner lector = new Scanner(file);
                        while(lector.hasNext()){
                            String linea = lector.nextLine();
                            String[] lineaDividida = linea.split("\n\r");
                            if(linea != null || !linea.isEmpty()){
                                String tick = lineaDividida[0];
                                if(tick.contains("-last")){
                                    name = tick;
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        name = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return name;
    }

    public String getHourPhone(){
        java.util.Date dt = new java.util.Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formateHour = df.format(dt.getTime());
        return formateHour;
    }

    public String getDatePhone(){
        Calendar cal = new GregorianCalendar();
        java.util.Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatDate = df.format(date);
        return formatDate;
    }

    public String eliminarEspaciosEnBlanco(String st){
        String sol = "";
        for (int x=0; x < st.length(); x++) {
            if (st.charAt(x) != ' ')
                sol += st.charAt(x);
        }
        return sol;
    }

    public int sacarEntero(String url, String tipo){
        constants = new Constants();
        url = sacarNumeroReducido(url);

        int cont = 0;
        int ini = 0;
        if(tipo.equals(constants.TICKET_TYPE)){
            ini= url.indexOf("R");
        }
        else if(tipo.equals(constants.FACTURA_TYPE)){
            ini= url.indexOf("F");
        }
        else if(tipo.equals(constants.PRESUPUESTO_TYPE)){
            ini= url.indexOf("P");
        }
        int fin = url.indexOf("-");

        //System.out.println(url.substring(ini+1, fin));
        cont = Integer.valueOf(url.substring(ini+1, fin));
        return cont;
    }

    public String sacarNumeroReducido(String url){
        String delim2 = "/";  //File.separator;
        String[] temp1;
        temp1 = url.split(delim2);
        url = temp1[temp1.length-1];

        String del = "_";
        String[] temp2;
        temp2 = url.split(del);
        url = temp2[temp2.length-1];

        String delim21 = ".pdf";  //File.separator;
        String[] temp12;
        temp12 = url.split(delim21);
        url = temp12[temp12.length-1];
        return url;
    }

    public String sacarYear(String url){
        String delim2 = "-";  //File.separator;
        String[] temp1;
        temp1 = url.split(delim2);
        url = temp1[temp1.length-1];
        return url;
    }

    public int getYear(String nombre){
        String del = "_";
        String[] temp2;
        temp2 = nombre.split(del);
        nombre = temp2[temp2.length-1];

        int cont = 0;
        int ini = nombre.indexOf("-");
        int fin = nombre.indexOf(".pdf");

        //System.out.println(url.substring(ini+1, fin));
        cont = Integer.valueOf(nombre.substring(ini+1, fin));
        return cont;
    }

    public String takeYear(String fecha){
        String delimiter = "/";
        String[] temp;
        temp = fecha.split(delimiter);
        return temp[temp.length-1];
    }

    public String takeYearFromDate(String fecha){
        String delimiter = "/";
        String[] temp;
        temp = fecha.split(delimiter);
        String year = temp[temp.length-1];
        year = year.replaceAll("\\s+", "");
        return year;
    }

    public void comprobarEstadoMemoria() {
        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;
        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
    }

    public String quitarLast(String st){
        return st.replace("(-last)", "");
    }

    public Double sacarIva(Double precio){
        double sol = 0;
        double impuestos = 1.10;
        sol = precio / impuestos;

        double sol1 = precio - sol;
        //sol1 = getDecimal(2,sol1);
        return getDecimal(2,sol1);
    }

    public Double sacarPrecioSinIva(Double precio){
        double sol = 0;
        double impuestos = 1.10;
        sol = precio / impuestos;
        return  getDecimal(2, sol);
    }

    public double getDecimal(int numeroDecimales,double decimal){
        decimal = decimal*(Math.pow(10, numeroDecimales));
        decimal = Math.round(decimal);
        decimal = decimal/ Math.pow(10, numeroDecimales);
        return decimal;
    }

    public Intent enviarPdfEmail(String sdCardRoot, String[] emailTo, String[] emailCC,
                                 String asunto, String mensaje){

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTo);
        if(emailCC != null){
            emailIntent.putExtra(Intent.EXTRA_CC, emailCC);
        }
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        if(!TextUtils.isEmpty(sdCardRoot)){
            Uri uri = Uri.fromFile(new File(sdCardRoot));
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        emailIntent.setType("message/rfc822");

        return emailIntent;
    }

    public String checkString(String st){
        if(st.equals("") || st == null)
            return "";
        else
            return st;
    }

    /**
     * Checks if the app has permission to write the device storage
     *
     * If the app does not have permission, then the user will be prompted to grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity){
        //Check if we have write permission
        Constants constants = new Constants();
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            //We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    constants.PERMISSIONS_STORAGE,
                    constants.REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public Information getInfo(String infoId, Context context){
        InformationDML i = new InformationDML(context);
        return i.getInfo(new DatabaseHelper(context), infoId);
    }

    public boolean borrarPdf(String rutaPdf){
        boolean borrado = false;
        try{
            Log.i("ruta pdf: ", "" + rutaPdf);
            File file = new File(rutaPdf);

            if(file.delete()){
                borrado = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return borrado;
    }

    public void insertarLastFile(String tipo, int id, Context context){
        LastFileDML lfiDml = new LastFileDML(context);

        if(tipo.equals(constants.TICKET_TYPE)){
            int lastTicket = lfiDml.getLastTicket(new DatabaseHelper(context), context);
            lfiDml.lastFileTicket(new DatabaseHelper(context), lastTicket, String.valueOf(id), context);
        }
        else if(tipo.equals(constants.FACTURA_TYPE)){
            int lastInvoice = lfiDml.getLastInvoice(new DatabaseHelper(context), context);
            lfiDml.lastFileInvoice(new DatabaseHelper(context), lastInvoice, String.valueOf(id), context);
        }
        else if(tipo.equals(constants.PRESUPUESTO_TYPE)){
            int lastPresupuesto = lfiDml.getLastPresupuesto(new DatabaseHelper(context), context);
            lfiDml.lastFilePresupuesto(new DatabaseHelper(context), lastPresupuesto, String.valueOf(id), context);
        }
    /*    else if(tipo.equals(constants.HOJARUTA_TYPE)){

        } */
        //Comprobar si hay una fila creada para este tipo: ticket, factura, presupuesto u hoja de ruta
        // Si no existe ninguna, anyadir nueva fila.
        // Si existe, update en la fila del ticket, factura, presupuesto u hoja de ruta

        // Para borrar, coger el id correspondiente de la fila que queramos (siempre sera lo ultimo creado).
    }

    public String borrarFile(String tipo, Context context){
        LastFileDML lfiDml = new LastFileDML(context);
        String idToDelete = null;
        if(tipo.equals(constants.TICKET_TYPE)){
            String lastTicket = lfiDml.getLastTicketId(new DatabaseHelper(context), context);
            int lfiId = lfiDml.getLastTicket(new DatabaseHelper(context), context);
            lfiDml.deleteFile(new DatabaseHelper(context), lfiId, context);
            idToDelete = lastTicket;
        }
        else if(tipo.equals(constants.FACTURA_TYPE)){
            String lastInvoice = lfiDml.getLastInvoiceId(new DatabaseHelper(context), context);
            int lfiId = lfiDml.getLastInvoice(new DatabaseHelper(context), context);
            lfiDml.deleteFile(new DatabaseHelper(context), lfiId, context);
            idToDelete = lastInvoice;
        }
        else if(tipo.equals(constants.PRESUPUESTO_TYPE)){
            String lastPresupuesto = lfiDml.getLastPresupuestoId(new DatabaseHelper(context), context);
            int lfiId = lfiDml.getLastPresupuesto(new DatabaseHelper(context), context);
            lfiDml.deleteFile(new DatabaseHelper(context), lfiId, context);
            idToDelete = lastPresupuesto;
        }
        return idToDelete;
    }

    public String getLastTicket(Context context){
        LastFileDML lfiDml = new LastFileDML(context);
        return lfiDml.getLastTicketId(new DatabaseHelper(context), context);
    }

    public String getLastInvoice(Context context){
        LastFileDML lfiDml = new LastFileDML(context);
        return lfiDml.getLastInvoiceId(new DatabaseHelper(context), context);
    }

    public String getLastPresupuesto(Context context){
        LastFileDML lfiDml = new LastFileDML(context);
        return lfiDml.getLastPresupuestoId(new DatabaseHelper(context), context);
    }

    public Taxi_Driver getTxd(Context context){
        Taxi_DriverDML txdDml = new Taxi_DriverDML(context);
        return txdDml.getTaxiDriver(new DatabaseHelper(context).getWritableDatabase(), context);
    }

    public String getTdaIdGivenTxdId(Context context, String txdId){
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(context);
        int tdaId = tdaDml.getTdaIdByTxdId(new DatabaseHelper(context), txdId, context);
        return String.valueOf(tdaId);
    }

    public Address getAddressById(String adrId, Context context){
        AddressDML a = new AddressDML(context);
        return a.getAddressById(new DatabaseHelper(context).getWritableDatabase(), adrId, context);
    }

    public void updateAddress(Address adr, Context context){
        AddressDML adrDML = new AddressDML(context);
        adrDML.update(adr, new DatabaseHelper(context).getWritableDatabase());
    }

    public void updateTaxiDriverAccount(Taxi_driver_account tda, Context context){
        Taxi_driver_accountDML tdaDML = new Taxi_driver_accountDML(context);
        tdaDML.update(tda, new DatabaseHelper(context).getWritableDatabase());
    }

    /* Devuelve el mes a partir del numero de mes. Si pasas un 2, te devolvera Febrero*/
    public String guessMonth(String monthNumber){
        String mes = null;

        switch (monthNumber){
            case "1":
                mes = "Enero";
                break;
            case "2":
                mes = "Febrero";
                break;
            case "3":
                mes = "Marzo";
                break;
            case "4":
                mes = "Abril";
                break;
            case "5":
                mes = "Mayo";
                break;
            case "6":
                mes = "Junio";
                break;
            case "7":
                mes = "Julio";
                break;
            case "8":
                mes = "Agosto";
                break;
            case "9":
                mes = "Septiembre";
                break;
            case "10":
                mes = "Octubre";
                break;
            case "11":
                mes = "Noviembre";
                break;
            case "12":
                mes = "Diciembre";
                break;
        }

        return mes;
    }

    public String getMonthFromDate(String date){
        String[] temp = splitDate(date);
        date = temp[temp.length-2];
        return date;
    }

    public String getYearFromDate(String date){
        String[] temp = splitDate(date);
        date = temp[temp.length-1];
        return date;
    }

    public String[] splitDate(String date){
        String delimitador = " / ";  //File.separator;
        return date.split(delimitador);
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public HashMap<String, byte[]> getFullLogo(String tdaId, Context context){
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(context);
        return tdaDml.getLogo(new DatabaseHelper(context), tdaId, context);
    }

    public String getExtensionFromLogoPath(String logoPath){
        return MimeTypeMap.getFileExtensionFromUrl(logoPath);
    }

    public void copyAssetFiles(InputStream in, OutputStream out, int BUFFER_SIZE){
        try {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int compararFechas(String dateInicio, String dateFin) throws ParseException{
        Calendar calendarInicio = null;
        Calendar calendarFin = null;

        try{
            calendarInicio = Calendar.getInstance();
            calendarFin = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd / M / yyyy", Locale.ENGLISH);
            calendarInicio.setTime(sdf.parse(dateInicio));
            calendarFin.setTime(sdf.parse(dateFin));
        }
        catch (ParseException pe){
            Log.e("comparar fechas",
                    "Error al comparar las fechas de inicio: '" + dateInicio + "' y la fecha de fin: '" + dateFin + "'",
                    pe);
        }

        /*
        the value 0 if the time represented by the argument is equal to the time represented by this Calendar;
        a value less than 0 if the time of this Calendar is before the time represented by the argument;
        and a value greater than 0 if the time of this Calendar is after the time represented by the argument.
        */

        return calendarInicio.compareTo(calendarFin);
    }

    public boolean isValidData(String enteredData){
        return Pattern.compile("[a-zA-ZñÑáéíóúÁÉÍÓÚ_0-9 \\t\\n\\x0B\\f\\r ,./-]*").matcher(enteredData).matches();
    }
}

