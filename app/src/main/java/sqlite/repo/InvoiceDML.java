package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import sqlite.data.DatabaseHelper;
import sqlite.model.Customer;
import sqlite.model.Invoice;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.model.Years;


/**
 * Created by Usuario on 11/04/2017.
 */

public class InvoiceDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private InvoiceDML invoiceDML;

    private final String TAG = InvoiceDML.class.getSimpleName().toString();

    public InvoiceDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Invoice.TABLE  + "("
                + Invoice.KEY_inv_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Invoice.KEY_inv_deleted + " TEXT DEFAULT '0' , "
                + Invoice.KEY_inv_number  + " TEXT, "
                + Invoice.KEY_inv_invoiceNumber + " TEXT, "
                + Invoice.KEY_inv_numFactRed + " TEXT, "
                + Invoice.KEY_inv_date  + " TEXT, "
                + Invoice.KEY_inv_price + " TEXT, "
                + Invoice.KEY_inv_emailed  + " TEXT DEFAULT '0', "
                + Invoice.KEY_inv_time  + " TEXT, "
                + Invoice.KEY_inv_pdf + " TEXT, "
                + Invoice.KEY_inv_tda_id  + " TEXT , "
                + Invoice.KEY_inv_tck_id  + " TEXT , "
                + Invoice.KEY_inv_yea_id  + " TEXT , "
                + Invoice.KEY_inv_cus_id  + " TEXT , "
                + "FOREIGN KEY (" + Invoice.KEY_inv_tda_id + ") REFERENCES " + Taxi_driver_account.TABLE
                + " ( " + Taxi_driver_account.KEY_tda_id + " ), "  //es posible que no haga falta añadir otra vez key_adr_id
                + "FOREIGN KEY (" + Invoice.KEY_inv_tck_id + ") REFERENCES " + Ticket.TABLE
                + " ( " + Ticket.KEY_tck_id + " ), "  //es posible que no haga falta añadir otra vez key_adr_id
                + "FOREIGN KEY (" + Invoice.KEY_inv_yea_id + ") REFERENCES " + Years.TABLE
                + " ( " + Years.KEY_yea_id + " ), "
                + "FOREIGN KEY (" + Invoice.KEY_inv_cus_id + ") REFERENCES " + Customer.TABLE
                + " ( " + Customer.KEY_cus_id + " ) ) ";
    }

    public long insert(Invoice invoice, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Invoice.KEY_inv_number, invoice.getInv_number());
        datos.put(Invoice.KEY_inv_invoiceNumber, invoice.getInv_invoiceNumber());
        datos.put(Invoice.KEY_inv_date, invoice.getInv_date());
        if(!TextUtils.isEmpty(invoice.getInv_emailed())){
            datos.put(Invoice.KEY_inv_emailed, invoice.getInv_emailed());
        }
        datos.put(Invoice.KEY_inv_tda_id, invoice.getInv_tda_id());
        datos.put(Invoice.KEY_inv_tck_id, invoice.getInv_tck_id());
        datos.put(Invoice.KEY_inv_yea_id, invoice.getInv_yea_id());
        datos.put(Invoice.KEY_inv_time, invoice.getInv_time());
        datos.put(Invoice.KEY_inv_price, invoice.getInv_price());
        datos.put(Invoice.KEY_inv_pdf, invoice.getInv_pdf());
        datos.put(Invoice.KEY_inv_cus_id, invoice.getInv_cus_id());
        datos.put(Invoice.KEY_inv_numFactRed, invoice.getInv_numFactRed());

        // Inserting Row
        long id = db.insert(Invoice.TABLE, null, datos);
        return id;
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Invoice.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Invoice invoice, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Invoice.KEY_inv_id, invoice.getInv_id());
            datos.put(Invoice.KEY_inv_deleted, invoice.getInv_deleted());
            datos.put(Invoice.KEY_inv_number, invoice.getInv_number());
            datos.put(Invoice.KEY_inv_invoiceNumber, invoice.getInv_invoiceNumber());
            datos.put(Invoice.KEY_inv_date, invoice.getInv_date());
            datos.put(Invoice.KEY_inv_price, invoice.getInv_price());
            datos.put(Invoice.KEY_inv_emailed, invoice.getInv_emailed());
            datos.put(Invoice.KEY_inv_tda_id, invoice.getInv_tda_id());
            datos.put(Invoice.KEY_inv_tck_id, invoice.getInv_tck_id());
            datos.put(Invoice.KEY_inv_yea_id, invoice.getInv_yea_id());
            datos.put(Invoice.KEY_inv_time, invoice.getInv_time());
            datos.put(Invoice.KEY_inv_pdf, invoice.getInv_pdf());
            datos.put(Invoice.KEY_inv_cus_id, invoice.getInv_cus_id());
            datos.put(Invoice.KEY_inv_numFactRed, invoice.getInv_numFactRed());

            result = db.update(Invoice.TABLE, datos, "inv_id = ?", new String[]{String.valueOf(invoice.getInv_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateInvoice", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarInvoice(DatabaseHelper dbHelper, Invoice inv , Context context){
        SQLiteDatabase db = null;
        String invId = null;
        try{
            db = dbHelper.getWritableDatabase();
            long id = insert(inv, db);
            invId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("insertar invoice", "", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return invId;
    }

    public Invoice getInvoiceById(DatabaseHelper dbHelper, int invoiceId, Context context){
        SQLiteDatabase db = null;
        Invoice invoice = null;

        try{
            db = dbHelper.getWritableDatabase();
            String query = "SELECT inv_id, inv_number, inv_invoiceNumber, inv_date, inv_tda_id, " +
                    "inv_yea_id, inv_time, inv_cus_id, inv_numFactRed, inv_pdf " +
                    "FROM Invoice WHERE inv_id = '" + invoiceId + "' and inv_deleted = '0'";
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ex getInvoiceById()", "invoiceId = " + invoiceId);
                    invoice = new Invoice();
                    invoice.setInv_id(fila.getInt(fila.getColumnIndex("inv_id")));
                    invoice.setInv_number(fila.getString(fila.getColumnIndex("inv_number")));
                    invoice.setInv_invoiceNumber(fila.getString(fila.getColumnIndex("inv_invoiceNumber")));
                    invoice.setInv_date(fila.getString(fila.getColumnIndex("inv_date")));
                    invoice.setInv_tda_id(fila.getString(fila.getColumnIndex("inv_tda_id")));
                    invoice.setInv_yea_id(String.valueOf(fila.getString(fila.getColumnIndex("inv_yea_id"))));
                    invoice.setInv_time(fila.getString(fila.getColumnIndex("inv_time")));
                    invoice.setInv_cus_id(fila.getString(fila.getColumnIndex("inv_cus_id")));
                    invoice.setInv_numFactRed(fila.getString(fila.getColumnIndex("inv_numFactRed")));
                    invoice.setInv_pdf(fila.getString(fila.getColumnIndex("inv_pdf")));
                }
            }
            fila.close();
        }
        catch(SQLiteException sqlte){
            Log.e("getInvoiceById()", "invoiceId" + String.valueOf(invoiceId), sqlte.getCause());
        }
        finally {
            db.close();
        }
        return invoice;
    }

    public int getInvoiceByNumFactRed(SQLiteDatabase db, String numFactRed, Context context){
        int invId = 0;

        try {
            String query = "SELECT inv_id from Invoice where inv_numFactRed = '" + numFactRed + "' and inv_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    return invId = fila.getInt(fila.getColumnIndex("inv_id"));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getInvoiceByNumFactRed", "numFactRed: " + numFactRed, sqlte.getCause());
        } finally {
            db.close();
        }

        return invId;
    }

    public int getTotalInsertedRows(SQLiteDatabase db){
        int result = -1;

        try {
            String query = " SELECT count(*) FROM Invoice ";
            Cursor countRows = db.rawQuery(query, null);
            countRows.moveToFirst();
            result = countRows.getInt(0);
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTotalInsertedRows", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public int getInvoiceCountByYear(SQLiteDatabase db, String dateYear, Context context){
        int result = -1;

        try {
            YearsDML yearDML = new YearsDML(context);
            int yeaId = yearDML.getInvoiceYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            String query = " SELECT count(*) FROM Invoice where inv_yea_id = '" + yeaId + "' and inv_deleted ='0'";

            Cursor countRows = db.rawQuery(query, null);
            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getInvoiceCountByYear", "dateYear: " + dateYear, sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public ArrayList<Integer> getInvoiceNumbers(SQLiteDatabase db, int yeaId, Context context){
        ArrayList<Integer> invoiceNumbs = null;

        try {
            invoiceNumbs = new ArrayList<>();

            String query = " SELECT inv_invoiceNumber FROM Invoice, Years WHERE inv_deleted = '0' " +
                    " and yea_id = inv_yea_id " +
                    "and yea_deleted = '0' and yea_id = '" + String.valueOf(yeaId) + "' ";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            do{
                String invNum = cursor.getString(cursor.getColumnIndex("inv_invoiceNumber"));
                int invoiceNumber = Integer.parseInt(invNum);
                invoiceNumbs.add(invoiceNumber);
            }
            while(cursor.moveToNext());
            Collections.sort(invoiceNumbs); //Ordeno los valores para devolver la lista ordenada
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getInvoiceNumbers", "yeaId: " + String.valueOf(yeaId), sqlte.getCause());
        } finally {
            db.close();
        }

        return invoiceNumbs;
    }

    public void borrarInvoice(DatabaseHelper dbHelper, String invoiceId, Context context){
        try {
            String query = "UPDATE Invoice SET inv_deleted = '1' WHERE inv_id = '" + Integer.parseInt(invoiceId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("borrarInvoice", "invoiceId: " + invoiceId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public ArrayList<Invoice> getAllInvoices(DatabaseHelper dbHelper, Context context){
        ArrayList<Invoice> invoices = null;

        try {
            invoices = new ArrayList<>();

            String query = " SELECT inv_id, inv_deleted, inv_number, inv_invoiceNumber, inv_numFactRed, " +
                    " inv_date, inv_tda_id , inv_time, inv_pdf, inv_price " +
                    "FROM Invoice WHERE inv_deleted = '0' AND inv_numFactRed like '%Factura%' order by inv_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Invoice invoice = new Invoice();
                    invoice.setInv_id(cursor.getInt(cursor.getColumnIndex("inv_id")));
                    invoice.setInv_deleted(cursor.getString(cursor.getColumnIndex("inv_deleted")));
                    invoice.setInv_number(cursor.getString(cursor.getColumnIndex("inv_number")));
                    invoice.setInv_invoiceNumber(cursor.getString(cursor.getColumnIndex("inv_invoiceNumber")));
                    invoice.setInv_numFactRed(cursor.getString(cursor.getColumnIndex("inv_numFactRed")));
                    invoice.setInv_date(cursor.getString(cursor.getColumnIndex("inv_date")));
                    invoice.setInv_tda_id(cursor.getString(cursor.getColumnIndex("inv_tda_id")));
                    invoice.setInv_time(cursor.getString(cursor.getColumnIndex("inv_time")));
                    invoice.setInv_pdf(cursor.getString(cursor.getColumnIndex("inv_pdf")));
                    invoice.setInv_price(cursor.getString(cursor.getColumnIndex("inv_price")));
                    invoices.add(invoice);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getAllInvoices", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return invoices;
    }

    public ArrayList<Invoice> getAllRendimientoFacturas(DatabaseHelper dbHelper, Context context){
        ArrayList<Invoice> invoices = null;

        try {
            invoices = new ArrayList<>();

            String query = " SELECT inv_id, inv_deleted, inv_number, inv_invoiceNumber, inv_numFactRed, " +
                    " inv_date, inv_tda_id , inv_time, inv_pdf, inv_price " +
                    "FROM Invoice WHERE inv_deleted = '0' AND inv_numFactRed LIKE '%Rendimiento%' " +
                    " order by inv_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Invoice invoice = new Invoice();
                    invoice.setInv_id(cursor.getInt(cursor.getColumnIndex("inv_id")));
                    invoice.setInv_deleted(cursor.getString(cursor.getColumnIndex("inv_deleted")));
                    invoice.setInv_number(cursor.getString(cursor.getColumnIndex("inv_number")));
                    invoice.setInv_invoiceNumber(cursor.getString(cursor.getColumnIndex("inv_invoiceNumber")));
                    invoice.setInv_numFactRed(cursor.getString(cursor.getColumnIndex("inv_numFactRed")));
                    invoice.setInv_date(cursor.getString(cursor.getColumnIndex("inv_date")));
                    invoice.setInv_tda_id(cursor.getString(cursor.getColumnIndex("inv_tda_id")));
                    invoice.setInv_time(cursor.getString(cursor.getColumnIndex("inv_time")));
                    invoice.setInv_pdf(cursor.getString(cursor.getColumnIndex("inv_pdf")));
                    invoice.setInv_price(cursor.getString(cursor.getColumnIndex("inv_price")));
                    invoices.add(invoice);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getAllRendimientoFact", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return invoices;
    }

    public void uploadPdfInvoice(DatabaseHelper dbHelper, String invoiceId, String ruta, Context context){
        try {
            String query = "UPDATE Invoice SET inv_pdf = '" + ruta + "' WHERE inv_id = '" + Integer.parseInt(invoiceId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("uploadPdfInvoice", "invoiceId: " + invoiceId + ", ruta: " + ruta, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public String getInvPdfPath(DatabaseHelper dbHelper, int invId, Context context){
        String path = null;

        try {
            String query = " SELECT inv_pdf " +
                    "FROM Invoice WHERE inv_deleted = '0' and inv_id = '" + invId + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                path = (cursor.getString(cursor.getColumnIndex("inv_pdf")));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getInvPdfPath", "invId" + String.valueOf(invId), sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return path;
    }
}
