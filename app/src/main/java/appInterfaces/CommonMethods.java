package appInterfaces;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;

import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;

/**
 * Created by Usuario on 15/01/2018.
 */

public interface CommonMethods {
    /**
     * Metodo para buscar tickets,facturas, etc. que esten guardados previamente.
     * @return devolveremos el ultimo ticket, factura, etc. creado en ese fichero
     */
    String buscarArchivosGuardados(String tipo, String nombreCarpeta);

    String getHourPhone();

    String getDatePhone();

    void comprobarEstadoMemoria();

    String quitarLast(String st);

    String takeYear(String fecha);

    int getYear(String nombre);

    int sacarEntero(String url, String tipo);

    String sacarNumeroReducido(String url);

    String sacarYear(String url);

    String takeYearFromDate(String fecha);

    String eliminarEspaciosEnBlanco(String st);

    Double sacarIva(Double precio);

    Double sacarPrecioSinIva(Double precio);

    double getDecimal(int numeroDecimales,double decimal);

    Intent enviarPdfEmail(String sdCardRoot, String[] emailTo, String[] emailCC,
                                 String asunto, String mensaje);

    String checkString(String st);

    Information getInfo(String infoId, Context context);

    boolean borrarPdf(String rutaPdf) ;

    void insertarLastFile(String tipo, int id, Context context);

    String borrarFile(String tipo, Context context);

    String getLastTicket(Context context);

    String getLastInvoice(Context context);

    String getLastPresupuesto(Context context);

    Taxi_Driver getTxd(Context context);

    String getTdaIdGivenTxdId(Context context, String txdId);

    Address getAddressById(String adrId, Context context);

    void updateAddress(Address adr, Context context);

    void updateTaxiDriverAccount(Taxi_driver_account tda, Context context);

    String guessMonth(String monthNumber);

    String getMonthFromDate(String date);

    String getYearFromDate(String date);

    String[] splitDate(String date);

    byte[] getBytes(Bitmap bitmap);

    Bitmap getImage(byte[] image);

    HashMap<String, byte[]> getFullLogo(String tdaId, Context context);

    String getExtensionFromLogoPath(String logoPath);

    void copyAssetFiles(InputStream in, OutputStream out, int BUFFER_SIZE);

    /**
     * 0 if equals;
      less than 0 if fecha inicio is before fecha fin;
      greater than 0 if fecha inicio is after fecha fin.
     * @param dateInicio
     * @param dateFin
     * @return int
     * @throws ParseException
     */
    int compararFechas(String dateInicio, String dateFin) throws ParseException;

    boolean isValidData(String enteredData);
}
