package appInterfaces;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Invoice;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;

/**
 * Created by Usuario on 26/11/2017.
 */

public interface InvoiceMethods {
    /** Crear e insertar factura nueva y devolver su ID.
     *
     * @param context
     * @param invoice
     * @param tdaId
     * @param tckId
     * @param cusId
     * @return Invoice ID inserted.
     */
    String createInvoice(Context context, Invoice invoice, String tdaId, String tckId, String cusId, String lastInvoiceNumber);

    void deleteInvoice(Context context, Invoice invoice);

    String createInformation(Information information, String invoiceId, Trip trip, Context context);

    String createTrip(DatabaseHelper dbHelper, Trip trip, Context context);

  //  String createCustomer(Customer customer, Address address, Context context);

    String createAddress(DatabaseHelper dbHelper, Address address, Context context);

    int getTotalInsertedRows(SQLiteDatabase db, String tableName);

    String buildInvoiceRed(String date, Invoice invoice, int yeaId, Context context);

    Invoice getInvoice(int invoiceId, Context context);


    ArrayList<Information> getInformationLinkedToInvoiceId(String invId, Context context);

    Trip getTrip(String trpId, Context context);

    //Customer getCustomerByInvoiceId(String invId, Context context);

    Address getAddressById(String adrId, Context context);

    Taxi_driver_account getTda(String tdaId, Context context);

    void borrarInvoice(String invoiceId, Context context);

    void uploadPdfInvoice(String invoiceId, String ruta, Context context);

    String getInvoicePath(int invoiceId, Context context);

    Invoice getInvoice(String invId, Context context);

    void updateInformationForInvoice(ArrayList<String> infoIds, String invoiceId, Context context);

    void updateTrip(ArrayList<String> infoIds, String number, Context context);
}
