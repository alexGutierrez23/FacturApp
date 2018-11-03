package appInterfaces;

import android.content.Context;

import java.util.ArrayList;

import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;
import sqlite.model.Stop;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;

/**
 * Created by Usuario on 16/01/2018.
 */

public interface HojaRutaMethods {
    //Contratos
    String insertarContrato(Contrato con, Context context);
    String insertarYearContrato(String dateYear, Context context);
    ArrayList<Contrato> getContratosByYear(String yeaId, Context context);
    String getYeaId(String dateYear, Context context);
    void borrarContrato(String contratoId, Context context);
    void updateContrato(Contrato contrato, Context context);
    ArrayList<Contrato> getContratos(Context context);
    Contrato getContratoByAlias(String alias, Context context);
    int getNumeroTotalContratos(Context context);
    ArrayList<Contrato> getContratosByArrendador(String ardName, Context context);

    String insertarArrendatario(Arrendatario arrendatario, Context context);
    ArrayList<Arrendatario> getAllArrendatarios(Context context);
    void updateArrendatario(Arrendatario arrendatario, Context context);
    Arrendatario getArrendatario(String arrendatarioId, Context context);
    Arrendatario getArrendatarioByArdName(String ardName, Context context);
    void borrarArrendatario(String ardId, Context context);
    boolean getBooleanIsTdaArrendador(String isTda_arrendador);
    //int getNumeroTotalArrendatarios(Context context);

    //Hojas de ruta
    String crearHojaRuta(HojaRuta hr, Context context);
    String crearTrip(Trip trip, Context context);
    Trip getTrip(String tripId, Context context);
    String crearStop(Stop stop, Context context);
    ArrayList<Stop> getStopsByHjrId(String hojaRutaId, Context context);
    void updateStop(Stop stop, Context context);
    ArrayList<HojaRuta> getHojasRuta(String contratoId, Context context);
    HojaRuta getHojaRuta(String hojaRutaId, Context context);
    int getNumeroTotalHojasRuta(Context context);
    ArrayList<HojaRuta> getHojasRutaByArrendatarioId(String arrendatarioId, Context context);

    void uploadHojaRutaPdf(String hojaRutaId, String ruta, Context context);
    Contrato getContrato(String contratoId, Context context);
    String getHojaRutaPath(int hjrId, Context context);
    void borrarHojaRuta(String hjrId, Context context);
    void updateHojaRuta(HojaRuta hr, Context context);
    void updateTrip(Trip trip, Context context);
    void generatePDF(String hojaRutaId, sqlite.model.HojaRuta hr, Context context);
    Taxi_driver_account getTaxiDriverAccount(Context context);
    String getDateStringFormat(String date);

    String[] fillArrayArrendatarios(ArrayList<Arrendatario> arrendatarios);
    String[] fillArrayStops(HojaRuta hojaRuta, Context context);

    int getCountNumHojaRuta(String date, Context context);
    String crearNumHojaRuta(int numHojasRutaPorMesAnyo, String date);
    String setHrFolder(HojaRuta hr);
}
