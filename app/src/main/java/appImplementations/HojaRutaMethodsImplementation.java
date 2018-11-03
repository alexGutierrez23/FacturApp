package appImplementations;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import Constants.Constants;
import appInterfaces.CommonMethods;
import appInterfaces.HojaRutaMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;
import sqlite.model.Stop;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;
import sqlite.repo.ArrendatarioDML;
import sqlite.repo.ContratoDML;
import sqlite.repo.HojaRutaDML;
import sqlite.repo.StopDML;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TripDML;
import sqlite.repo.YearsDML;

/**
 * Created by Usuario on 16/01/2018.
 */

public class HojaRutaMethodsImplementation implements HojaRutaMethods {
    //Contratos
    public String insertarContrato(Contrato contrato, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        return contratoDML.insertarContrato(new DatabaseHelper(context), contrato , context);
    }

    public String insertarYearContrato(String dateYear, Context context){
        YearsDML yearsDML = new YearsDML(context);
        int yeaId = yearsDML.insertarContratoYear(new DatabaseHelper(context), dateYear, context);
        return String.valueOf(yeaId);
    }

    public ArrayList<Contrato> getContratosByYear(String year, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        String yeaId = getYeaId(year, context);
        return contratoDML.getContratosByYear(new DatabaseHelper(context), yeaId, context);
    }

    public ArrayList<Contrato> getContratosByArrendador(String ardName, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        String yeaId = getYeaId(ardName, context);
        return contratoDML.getContratosByArrendador(new DatabaseHelper(context), ardName, context);
    }

    public ArrayList<Contrato> getContratos(Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        return contratoDML.getContratos(new DatabaseHelper(context), context);
    }

    public String getYeaId(String dateYear, Context context){
        YearsDML yearsDML = new YearsDML(context);
        int yeaId = yearsDML.getContratoYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
        return String.valueOf(yeaId);
    }

    public Contrato getContrato(String contratoId, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        int conId = Integer.parseInt(contratoId);
        return contratoDML.getContratoById(new DatabaseHelper(context), conId, context);
    }

    public Contrato getContratoByAlias(String alias, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        return contratoDML.getContratoByAlias(new DatabaseHelper(context), alias, context);
    }

    public void borrarContrato(String contratoId, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        ArrayList<HojaRuta> hojasRuta = getHojasRuta(contratoId, context);
        for(HojaRuta hr : hojasRuta){
            hojaRutaDML.borrarHojaRuta(new DatabaseHelper(context), String.valueOf(hr.getHjr_id()), context);
        }
        contratoDML.borrarContrato(new DatabaseHelper(context), contratoId, context);
    }

    public void updateContrato(Contrato contrato, Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        contratoDML.update(contrato, new DatabaseHelper(context).getWritableDatabase());
    }

    public int getNumeroTotalContratos(Context context){
        ContratoDML contratoDML = new ContratoDML(context);
        return contratoDML.getContratosCount(new DatabaseHelper(context), context);
    }

    public String insertarArrendatario(Arrendatario arrendatario, Context context){
        ArrendatarioDML arrendatarioDML = new ArrendatarioDML(context);
        return arrendatarioDML.insertarArrendatario(new DatabaseHelper(context), arrendatario , context);
    }

    public ArrayList<Arrendatario> getAllArrendatarios(Context context){
        ArrendatarioDML arrendatarioDML = new ArrendatarioDML(context);
        return arrendatarioDML.getArrendatarios(new DatabaseHelper(context), context);
    }

    public void updateArrendatario(Arrendatario arrendatario, Context context){
        ArrendatarioDML arrendatarioDML = new ArrendatarioDML(context);
        arrendatarioDML.update(arrendatario, new DatabaseHelper(context).getWritableDatabase());
    }

    public Arrendatario getArrendatario(String arrendatarioId, Context context){
        ArrendatarioDML arrendatarioDML = new ArrendatarioDML(context);
        return arrendatarioDML.getArrendatario(new DatabaseHelper(context), Integer.valueOf(arrendatarioId), context);
    }

    public Arrendatario getArrendatarioByArdName(String ardName, Context context){
        ArrendatarioDML arrendatarioDML = new ArrendatarioDML(context);
        return arrendatarioDML.getArrendatarioByName(new DatabaseHelper(context), ardName, context);
    }

    public void borrarArrendatario(String ardId, Context context){
        ArrendatarioDML arrendatarioDML = new ArrendatarioDML(context);
        arrendatarioDML.borrarArrendatario(new DatabaseHelper(context), ardId, context);
    }

    //Hojas de ruta
    public ArrayList<HojaRuta> getHojasRuta(String contratoId, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        return hojaRutaDML.getHojasRuta(new DatabaseHelper(context), contratoId, context);
    }

    public HojaRuta getHojaRuta(String hojaRutaId, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        int hrId = Integer.parseInt(hojaRutaId);
        return hojaRutaDML.getHojaRutaById(new DatabaseHelper(context), hrId, context);
    }

    public String crearHojaRuta(HojaRuta hr, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        return hojaRutaDML.insertarHojaRuta(new DatabaseHelper(context), hr, context);
    }

    public int getNumeroTotalHojasRuta(Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        return hojaRutaDML.getHojasRutaCount(new DatabaseHelper(context), context);
    }

    public ArrayList<HojaRuta> getHojasRutaByArrendatarioId(String arrendatarioId, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        return hojaRutaDML.getHojasRutaByArrendatarioId(new DatabaseHelper(context), arrendatarioId, context);
    }

    public String crearTrip(Trip trip, Context context){
        TripDML tripDML = new TripDML(context);
        return tripDML.insertarTrip(new DatabaseHelper(context), trip, context);
    }

    public Trip getTrip(String tripId, Context context){
        TripDML tripDML = new TripDML(context);
        return tripDML.getTripById(new DatabaseHelper(context).getWritableDatabase(), tripId, context);
    }

    public String crearStop(Stop stop, Context context){
        StopDML stopDML = new StopDML(context);
        return stopDML.insertarParada(new DatabaseHelper(context), stop, context);
    }

    public ArrayList<Stop> getStopsByHjrId(String hojaRutaId, Context context){
        StopDML stopDML = new StopDML(context);
        return stopDML.getStopsByHojaRutaId(new DatabaseHelper(context), hojaRutaId, context);
    }

    public void updateStop(Stop stop, Context context){
        StopDML stopDML = new StopDML(context);
        stopDML.update(stop, new DatabaseHelper(context).getWritableDatabase());
    }

    public void uploadHojaRutaPdf(String hojaRutaId, String ruta, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        hojaRutaDML.uploadPdfHojaRuta(new DatabaseHelper(context), hojaRutaId, ruta, context);
    }

    public String getHojaRutaPath(int hjrId, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        return hojaRutaDML.getHojaRutaPdfPath(new DatabaseHelper(context), hjrId, context);
    }

    public void borrarHojaRuta(String hjrId, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        hojaRutaDML.borrarHojaRuta(new DatabaseHelper(context), hjrId, context);
    }

    public void updateHojaRuta(HojaRuta hr, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        hojaRutaDML.update(hr, new DatabaseHelper(context).getWritableDatabase());
    }

    public void updateTrip(Trip trip, Context context){
        TripDML tripDML = new TripDML(context);
        tripDML.update(trip, new DatabaseHelper(context).getWritableDatabase());
    }

    public void generatePDF(String hojaRutaId, sqlite.model.HojaRuta hr, Context context){
        Constants c = new Constants();
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        String hojaRutaName = "HojaRuta_" + hr.getHjr_number();

        Contrato contrato = hrm.getContrato(hr.getHjr_con_id(),context);
        Trip trip = hrm.getTrip(hr.getHjr_trp_id(), context);
        ArrayList<Stop> stops = hrm.getStopsByHjrId(String.valueOf(hr.getHjr_id()), context);
        Taxi_driver_account tda = getTaxiDriverAccount(context);
        Arrendatario ard = hrm.getArrendatario(hr.getHjr_ard_id(), context);
        boolean isTdaArrendador = hrm.getBooleanIsTdaArrendador(contrato.getCon_isTda_arrendador());

        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + c.NOMBRE_CARPETA_APP);
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + c.GENERADOS_HOJASRUTA);
        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        File pdfMontYearDir = new File(pdfSubDir.getPath() + File.separator + setHrFolder(hr));
        if (!pdfMontYearDir.exists()) {
            pdfMontYearDir.mkdir();
        }
        String hrName = "HojaRuta_" + hr.getHjr_alias() + ".pdf";
        String nombre_completo = pdfMontYearDir + File.separator + hrName;

        File outputFile = new File(nombre_completo);

        try {
            pdfMontYearDir.mkdirs();
            Document documento = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(documento, new FileOutputStream(outputFile));

            documento.open();   //Ancho de impresion = 48mm

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            String htmlToPdf = generateHojaRuta(hr, tda, stops, trip, contrato, ard, isTdaArrendador).toString();

            worker.parseXHtml(pdfWriter, documento, new StringReader(htmlToPdf));
            documento.close();
            hrm.uploadHojaRutaPdf(hojaRutaId, nombre_completo, context);
        } catch (IOException e) {
            Log.e("IOEXCEPTION ", e.getMessage());
            e.printStackTrace();
        }
        catch (DocumentException de){
            Log.e("DOCUMENT EXCEPTION ", de.getMessage());
            de.printStackTrace();
        }
    }

    public Taxi_driver_account getTaxiDriverAccount(Context context){
        CommonMethods cm  = new CommonMethodsImplementation();
        Taxi_Driver txd = cm.getTxd(context);
        String tdaId = cm.getTdaIdGivenTxdId(context, String.valueOf(txd.getTxd_id()));
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(context);
        return tdaDml.getTdaById(new DatabaseHelper(context), tdaId, context);
    }

    private StringBuffer generateHojaRuta(sqlite.model.HojaRuta hr, Taxi_driver_account tda,
                                          ArrayList<Stop> stops, Trip trip, Contrato contrato,
                                          Arrendatario ard, boolean isTdaArrendador){
        CommonMethods cm = new CommonMethodsImplementation();
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head style=\"height:297mm; width:210mm; margin-left:auto; margin-right:auto;\"></head>");
        sb.append("<body style=\"height:297mm; width:210mm; margin-left:auto; margin-right:auto;\">");
        sb.append("<div style=\"font-size:14px;\">");
        sb.append("<b>HOJA DE RUTA ADSCRITA AL CONTRATO FIRMADO EN MADRID ");
        if(isTdaArrendador){
            sb.append(getDateStringFormat(ard.getArd_fecha()));
            sb.append("N° " + ard.getArd_number());
        }
        else{
            sb.append(getDateStringFormat(contrato.getCon_date()));
            sb.append("N° " + contrato.getCon_number());
        }
        sb.append("</b>");
        sb.append("</div><div style=\"font-size:10px;\">(Conforme Orden Fom /2799/2015, de 18 de Diciembre)</div><br/>");
        sb.append("<div style=\"font-size:14px;\">");
        sb.append("<p><b>TRANSPORTISTA: D. </b>" + tda.getTda_name() + ". ");
        sb.append("<b>NIF:</b> " + tda.getTda_nif() + ". ");
        sb.append("<b>MATRICULA  VEHICULO:</b> " + contrato.getCon_matricula_vehiculo() + ". </p>");
        sb.append("<p><b>ARRENDADOR:</b> " + contrato.getCon_arrendador() + ". ");
        sb.append("<b>CIF:</b> " + contrato.getCon_nif_arrendador() + ". </p>");
        sb.append("<p><b>ARRENDATARIO:</b> " + ard.getArd_name() + ". ");
        sb.append("<b>NIF/CIF:</b> " + ard.getArd_nifCif() + ".</p>");
        sb.append("<p><b>FECHA INICIO DEL SERVICIO:</b> " + hr.getHjr_date_inicio() + ". <b>FECHA FIN DEL SERVICIO:</b> " +  hr.getHjr_date_fin() + ".</p>");
        sb.append("<p><b>HORA INICIO:</b> " + hr.getHjr_time_inicio() + ". <b>HORA FIN:</b> " + hr.getHjr_time_fin() + ".</p>");
        sb.append("<p><b>LUGAR DE INICIO DEL SERVICIO:</b> " + trip.getTrp_start_place() + ".</p>");
        sb.append("<p><b>INICIO DEL SERVICIO:</b> " + trip.getTrp_start() + ".</p>");
        String paradasHtml = getParadasStringFormat(stops);
        if(!paradasHtml.equals("")){
            sb.append(paradasHtml);
        }
        sb.append("<p><b>DESTINO DEL SERVICIO:</b> " + trip.getTrp_finish() + ".</p>");
        sb.append("<p><b>PASAJEROS:</b> " + hr.getHjr_pasajeros() + ".</p>");
        sb.append("<p><b>OBSERVACIONES:</b>" + hr.getHjr_observaciones() + "</p>");
        sb.append("</div><br/><div style=\"font-size:10px;\">");
        sb.append("<b>NOTA: EL SERVICIO DE TRANSPORTE SE REALIZA A JORNADA COMPLETA DIARIA PARA EL ARRENDATARIO, SALVO AUTORIZACION EXPRESA PARA REALIZAR OTROS DIFERENTES.</b>");
        sb.append("</div>");
        if(isTdaArrendador){
            sb.append("<div style=\"float:right;\">");
            sb.append("<span>");
            sb.append(hr.getHjr_number());
            sb.append("</span>");
            sb.append("</div>");
        }
        sb.append("</body></html>");

        return sb;
    }

    public String getDateStringFormat(String date){
        if(date != null && !date.isEmpty()){
            CommonMethods cm = new CommonMethodsImplementation();
            String[] dateSplit = cm.splitDate(date); //revisar hora fin
            String month = cm.guessMonth(dateSplit[1]);
            return "EL " + dateSplit[0] + " DE " + month.toUpperCase() + " DE " + dateSplit[2] + ", ";
        }
        return date;
    }

    private String getParadasStringFormat(ArrayList<Stop> stops){
        String inicioTextoParada = "<p><b>PARADA INTERMEDIA Y ESPERA:</b> ";
        String finTextoParada = ".</p>";
        StringBuffer sb = new StringBuffer();
        if(!stops.isEmpty()){
            for(Stop stop: stops){
                String parada = inicioTextoParada + stop.getStp_place() + finTextoParada;
                sb.append(parada);
            }
        }
        else{
            sb.append("");
        }

        return sb.toString();
    }

    public boolean getBooleanIsTdaArrendador(String isTda_arrendador){
        if(isTda_arrendador.isEmpty() || isTda_arrendador == null){
            return false;
        }
        else{
            if(isTda_arrendador.equals("1")){ //Solo puede ser '1' o '0'
                return true;
            }
            else{
                return false;
            }
        }
    }

    public String[] fillArrayArrendatarios(ArrayList<Arrendatario> arrendatarios){
        ArrayList<String> ardsName = new ArrayList<>();
        String[] arrayArrendatarios = null;
        if(arrendatarios != null && arrendatarios.size() > 0){
            for(Arrendatario ard : arrendatarios){
                ardsName.add(ard.getArd_name());
            }
            if(ardsName != null && ardsName.size() > 0){
                Set<String> ardNames = new LinkedHashSet<>(ardsName);
                ArrayList<String> names = new ArrayList<>();
                names.addAll(ardNames);
                int arrendatariosSize = names.size() + 1;
                arrayArrendatarios = new String[arrendatariosSize];
                arrayArrendatarios[0] = "Por favor seleccione un arrendatario...";
                for(int i = 1; i < arrendatariosSize; i++){
                    arrayArrendatarios[i] = names.get(i-1);
                }
            }
        }
        else{
            arrayArrendatarios = new String[1];
            arrayArrendatarios[0] = "Por favor seleccione un arrendatario...";
        }

        return arrayArrendatarios;
    }

    public String[] fillArrayStops(HojaRuta hojaRuta, Context context){
        HojaRutaMethods hrm = new HojaRutaMethodsImplementation();
        ArrayList<Stop> stops = hrm.getStopsByHjrId(String.valueOf(hojaRuta.getHjr_id()), context);

        ArrayList<String> nombreParada = new ArrayList<>();
        for(Stop stop: stops){
            nombreParada.add(stop.getStp_place());
        }
        String[] arrayStops = null;
        if(nombreParada != null){
            int contratosAliasSize = nombreParada.size() + 1;
            arrayStops = new String[contratosAliasSize];
            arrayStops[0] = "Por favor, seleccione una parada...";
            for(int i = 1; i < contratosAliasSize; i++){
                arrayStops[i] = nombreParada.get(i-1);
            }
        }
        return arrayStops;
    }

    public int getCountNumHojaRuta(String date, Context context){
        HojaRutaDML hojaRutaDML = new HojaRutaDML(context);
        return hojaRutaDML.getCountByDate(new DatabaseHelper(context), date);
    }

    public String crearNumHojaRuta(int numHojasRutaPorMesAnyo, String date){
        CommonMethods cm = new CommonMethodsImplementation();

        numHojasRutaPorMesAnyo = numHojasRutaPorMesAnyo + 1;  //Siempre incrementamos este valor una unidad para que sea uno mas del total de lineas devueltas por la BBDD

        String delimitador = " / ";
        String[] dd = date.split(delimitador);
        String mes = cm.guessMonth(dd[1]);

        return "H-" + numHojasRutaPorMesAnyo + "_" + mes;
    }

    public String setHrFolder(HojaRuta hr){
        CommonMethods cm = new CommonMethodsImplementation();
        String delimitador = " / ";
        String[] dd = hr.getHjr_date_inicio().split(delimitador);
        String mes = cm.guessMonth(dd[1]);

        return mes + "_" + dd[2];
    }

}
