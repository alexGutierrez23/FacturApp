package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import sqlite.data.DatabaseHelper;
import sqlite.model.Years;


/**
 * Created by Usuario on 11/04/2017.
 */

public class YearsDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private YearsDML yearDML;

    private final String TAG = YearsDML.class.getSimpleName().toString();

    public YearsDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Years.TABLE  + "("
                + Years.KEY_yea_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Years.KEY_yea_deleted + " TEXT DEFAULT '0', "
                + Years.KEY_yea_dateYear + " TEXT ,"
                + Years.KEY_yea_ticket + " TEXT DEFAULT '0' , "
                + Years.KEY_yea_invoice + " TEXT DEFAULT '0' , "
                + Years.KEY_yea_presupuesto + " TEXT DEFAULT '0' , "
                + Years.KEY_yea_hojaRuta + " TEXT DEFAULT '0', "
                + Years.KEY_yea_contrato + " TEXT DEFAULT '0' ) ";
    }

    public long insert(String dateYear, String ticket, String invoice, String presupuesto,
                       String hojaRuta, String contrato, SQLiteDatabase db) {  //dateYear is the year: 2017, 2018, 2019
        ContentValues datos = new ContentValues();
        datos.put(Years.KEY_yea_dateYear, dateYear);
        datos.put(Years.KEY_yea_ticket, ticket);
        datos.put(Years.KEY_yea_invoice, invoice);
        datos.put(Years.KEY_yea_presupuesto, presupuesto);
        datos.put(Years.KEY_yea_hojaRuta, hojaRuta);
        datos.put(Years.KEY_yea_contrato, contrato);
        // Inserting Row
        long yeaId = db.insert(Years.TABLE, null, datos);

        return yeaId;
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Years.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete years", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Years years, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Years.KEY_yea_id, years.getYea_id());
            datos.put(Years.KEY_yea_deleted , years.getYea_deleted());
            datos.put(Years.KEY_yea_dateYear, years.getYea_dateYear());
            datos.put(Years.KEY_yea_ticket, years.getYea_ticket());
            datos.put(Years.KEY_yea_invoice, years.getYea_invoice());
            datos.put(Years.KEY_yea_presupuesto, years.getYea_presupuesto());
            datos.put(Years.KEY_yea_hojaRuta, years.getYea_hojaRuta());
            datos.put(Years.KEY_yea_contrato, years.getYea_contrato());

            result = db.update(Years.TABLE, datos, "yea_id = ?", new String[]{String.valueOf(years.getYea_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateYears", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public int insertarTicketYear(DatabaseHelper dbHelper, String dateYear, Context context){
        SQLiteDatabase db = null;
        int yeaId = 0;
        try{
            db = dbHelper.getWritableDatabase();
            YearsDML yearDml = new YearsDML(context);
            yeaId = getTicketYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            if(yeaId == -1){  // Si no esta ese anyo insertado, lo metemos
                yearDml.insert(dateYear, "1", "0", "0", "0", "0", db);
                yeaId = getTicketYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            }
        }
        catch(SQLiteException sqlte){
            Log.e("Years", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return yeaId;
    }

    public int getTicketYearByDateYear(SQLiteDatabase db, String dateYear, Context context){
        int yeaId = 0;

        try {
            String query = "SELECT yea_id from Years where yea_dateYear = '" + dateYear + "' " +
                    "and yea_deleted='0' and yea_ticket = '1' and yea_invoice ='0' " +
                    "and yea_presupuesto='0' and yea_hojaRuta='0' and yea_contrato='0' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ej getTicketYear" , "");
                    return yeaId = fila.getInt(fila.getColumnIndex("yea_id"));
                }
            }
            else{
                yeaId = -1;
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTicketYearByDateYear", "dateYear: " + dateYear, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return yeaId;
    }

    public int insertarInvoiceYear(DatabaseHelper dbHelper, String dateYear, Context context){
        SQLiteDatabase db = null;
        int yeaId = 0;
        try{
            db = dbHelper.getWritableDatabase();
            YearsDML yearDml = new YearsDML(context);
            yeaId = getInvoiceYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            if(yeaId == -1){  // Si no esta ese anyo insertado, lo metemos
                yearDml.insert(dateYear, "0", "1", "0", "0", "0", db);
                yeaId = getInvoiceYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            }
        }
        catch(SQLiteException sqlte){
            Log.e("Years", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return yeaId;
    }

    public int getInvoiceYearByDateYear(SQLiteDatabase db, String dateYear, Context context){
        int yeaId = 0;

        try {
            String query = "SELECT yea_id from Years where yea_dateYear = '" + dateYear + "' " +
                    "and yea_deleted='0' and yea_ticket = '0' and yea_invoice ='1' and yea_contrato='0' " +
                    "and yea_presupuesto='0' and yea_hojaRuta='0'  ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("InvoiceYearByDateYear", "");
                    return yeaId = fila.getInt(fila.getColumnIndex("yea_id"));
                }
            }
            else{
                yeaId = -1;
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getInvYearByDateYear", "dateYear: " + dateYear, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return yeaId;
    }

    public int getPresupuestoYearByDateYear(SQLiteDatabase db, String dateYear, Context context){
        int yeaId = 0;

        try {
            String query = "SELECT yea_id from Years where yea_dateYear = '" + dateYear + "' " +
                    "and yea_deleted='0' and yea_ticket = '0' and yea_invoice ='0' and yea_contrato='0' " +
                    "and yea_presupuesto='1' and yea_hojaRuta='0' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ej ", query);
                    return yeaId = fila.getInt(fila.getColumnIndex("yea_id"));
                }
            }
            else{
                yeaId = -1;
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getPreYearByDateYear", "dateYear: " + dateYear, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return yeaId;
    }

    public int insertarPresupuestoYear(DatabaseHelper dbHelper, String dateYear, Context context){
        SQLiteDatabase db = null;
        int yeaId = 0;
        try{
            db = dbHelper.getWritableDatabase();
            YearsDML yearDml = new YearsDML(context);
            yeaId = getPresupuestoYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            if(yeaId == -1){  // Si no esta ese anyo insertado, lo metemos
                yearDml.insert(dateYear, "0", "0", "1", "0", "0", db);
                yeaId = getPresupuestoYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            }
        }
        catch(SQLiteException sqlte){
            Log.e("Years", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return yeaId;
    }

    public int getHojaRutaYearByDateYear(SQLiteDatabase db, String dateYear, Context context){
        int yeaId = 0;

        try {
            String query = "SELECT yea_id from Years where yea_dateYear = '" + dateYear + "' " +
                    "and yea_deleted='0' and yea_ticket = '0' and yea_invoice ='0' " +
                    "and yea_presupuesto='0' and yea_hojaRuta='1' and yea_contrato='0' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando ",query);
                    return yeaId = fila.getInt(fila.getColumnIndex("yea_id"));
                }
            }
            else{
                yeaId = -1;
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getHRYearByDateYear", "dateYear: " + dateYear, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return yeaId;
    }

    public int insertarHojaRutaYear(DatabaseHelper dbHelper, String dateYear, Context context){
        SQLiteDatabase db = null;
        int yeaId = 0;
        try{
            db = dbHelper.getWritableDatabase();
            YearsDML yearDml = new YearsDML(context);
            yeaId = getHojaRutaYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            if(yeaId == -1){  // Si no esta ese anyo insertado, lo metemos
                yearDml.insert(dateYear, "0", "0", "0", "1", "0", db);
                yeaId = getHojaRutaYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            }
        }
        catch(SQLiteException sqlte){
            Log.e("Years", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return yeaId;
    }

    public int getContratoYearByDateYear(SQLiteDatabase db, String dateYear, Context context){
        int yeaId = 0;

        try {
            String query = "SELECT yea_id from Years where yea_dateYear = '" + dateYear + "' " +
                    "and yea_deleted='0' and yea_ticket = '0' and yea_invoice ='0' " +
                    "and yea_presupuesto='0' and yea_hojaRuta='0' and yea_contrato='1' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando ",query);
                    return yeaId = fila.getInt(fila.getColumnIndex("yea_id"));
                }
            }
            else{
                yeaId = -1;
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getConYearByDateYear", "dateYear: " + dateYear, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return yeaId;
    }

    public int insertarContratoYear(DatabaseHelper dbHelper, String dateYear, Context context){
        SQLiteDatabase db = null;
        int yeaId = 0;
        try{
            db = dbHelper.getWritableDatabase();
            YearsDML yearDml = new YearsDML(context);
            yeaId = getContratoYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            if(yeaId == -1){  // Si no esta ese anyo insertado, lo metemos
                yearDml.insert(dateYear, "0", "0", "0", "0", "1", db);
                yeaId = getContratoYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            }
        }
        catch(SQLiteException sqlte){
            Log.w("Years", "insertando");
        }
        finally {
            db.close();
        }
        return yeaId;
    }

    public ArrayList<String> getContratoYears(SQLiteDatabase db, Context context){
        ArrayList<String> years = null;

        try {
            String query = "SELECT yea_dateYear from Years where yea_deleted='0' and yea_ticket = '0' and yea_invoice ='0' " +
                    "and yea_presupuesto='0' and yea_hojaRuta='0' and yea_contrato='1' ";
            years = new ArrayList<>();
            Cursor fila = db.rawQuery(query, null);
            fila.moveToFirst();
            do{
                if(fila.getCount() != 0 && fila != null) {
                    Log.v("Ejecutando ",query);
                    years.add(fila.getString(fila.getColumnIndex("yea_dateYear")));
                }
            }
            while(fila.moveToNext());

            if(years.isEmpty()){
                years = null;
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getContratoYears", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return years;
    }
}
