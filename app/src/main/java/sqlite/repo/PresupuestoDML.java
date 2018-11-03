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
import sqlite.model.Presupuesto;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.model.Years;


/**
 * Created by Usuario on 11/04/2017.
 */

public class PresupuestoDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private PresupuestoDML presupuestoDML;

    private final String TAG = PresupuestoDML.class.getSimpleName().toString();

    public PresupuestoDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Presupuesto.TABLE  + "("
                + Presupuesto.KEY_pre_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Presupuesto.KEY_pre_deleted + " TEXT DEFAULT '0' , "
                + Presupuesto.KEY_pre_number  + " TEXT, "
                + Presupuesto.KEY_pre_presupuestoNumber + " TEXT, "
                + Presupuesto.KEY_pre_numPresupuestoRed + " TEXT, "
                + Presupuesto.KEY_pre_date  + " TEXT, "
                + Presupuesto.KEY_pre_emailed  + " TEXT DEFAULT '0', "
                + Presupuesto.KEY_pre_time  + " TEXT, "
                + Presupuesto.KEY_pre_pdf  + " TEXT, "
                + Presupuesto.KEY_pre_tda_id  + " TEXT , "
                + Presupuesto.KEY_pre_tck_id  + " TEXT , "
                + Presupuesto.KEY_pre_yea_id  + " TEXT , "
                + Presupuesto.KEY_pre_cus_id  + " TEXT , "
                + "FOREIGN KEY (" + Presupuesto.KEY_pre_tda_id + ") REFERENCES " + Taxi_driver_account.TABLE
                + " ( " + Taxi_driver_account.KEY_tda_id + " ), "  //es posible que no haga falta añadir otra vez key_adr_id
                + "FOREIGN KEY (" + Presupuesto.KEY_pre_tck_id + ") REFERENCES " + Ticket.TABLE
                + " ( " + Ticket.KEY_tck_id + " ), "  //es posible que no haga falta añadir otra vez key_adr_id
                + "FOREIGN KEY (" + Presupuesto.KEY_pre_yea_id + ") REFERENCES " + Years.TABLE
                + " ( " + Years.KEY_yea_id + " ), "
                + "FOREIGN KEY (" + Presupuesto.KEY_pre_cus_id + ") REFERENCES " + Customer.TABLE
                + " ( " + Customer.KEY_cus_id + " ) ) ";
    }

    public long insert(Presupuesto presupuesto, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Presupuesto.KEY_pre_number, presupuesto.getPre_number());
        datos.put(Presupuesto.KEY_pre_presupuestoNumber, presupuesto.getPre_presupuestoNumber());
        datos.put(Presupuesto.KEY_pre_date, presupuesto.getPre_date());
        if(!TextUtils.isEmpty(presupuesto.getPre_emailed())){
            datos.put(Presupuesto.KEY_pre_emailed, presupuesto.getPre_emailed());
        }
        datos.put(Presupuesto.KEY_pre_tda_id, presupuesto.getPre_tda_id());
        datos.put(Presupuesto.KEY_pre_tck_id, presupuesto.getPre_tck_id());
        datos.put(Presupuesto.KEY_pre_yea_id, presupuesto.getPre_yea_id());
        datos.put(Presupuesto.KEY_pre_time, presupuesto.getPre_time());
        datos.put(Presupuesto.KEY_pre_pdf, presupuesto.getPre_pdf());
        datos.put(Presupuesto.KEY_pre_cus_id, presupuesto.getPre_cus_id());
        datos.put(Presupuesto.KEY_pre_numPresupuestoRed, presupuesto.getPre_numPresupuestoRed());

        // Inserting Row
        long id = db.insert(Presupuesto.TABLE, null, datos);

        return id;
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Presupuesto.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete Presupuesto", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Presupuesto presupuesto, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Presupuesto.KEY_pre_id, presupuesto.getPre_id());
            datos.put(Presupuesto.KEY_pre_deleted, presupuesto.getPre_deleted());
            datos.put(Presupuesto.KEY_pre_number, presupuesto.getPre_number());
            datos.put(Presupuesto.KEY_pre_presupuestoNumber, presupuesto.getPre_presupuestoNumber());
            datos.put(Presupuesto.KEY_pre_date, presupuesto.getPre_date());
            datos.put(Presupuesto.KEY_pre_emailed, presupuesto.getPre_emailed());
            datos.put(Presupuesto.KEY_pre_tda_id, presupuesto.getPre_tda_id());
            datos.put(Presupuesto.KEY_pre_tck_id, presupuesto.getPre_tck_id());
            datos.put(Presupuesto.KEY_pre_yea_id, presupuesto.getPre_yea_id());
            datos.put(Presupuesto.KEY_pre_time, presupuesto.getPre_time());
            datos.put(Presupuesto.KEY_pre_pdf, presupuesto.getPre_pdf());
            datos.put(Presupuesto.KEY_pre_cus_id, presupuesto.getPre_cus_id());
            datos.put(Presupuesto.KEY_pre_numPresupuestoRed, presupuesto.getPre_numPresupuestoRed());

            result = db.update(Presupuesto.TABLE, datos, "pre_id = ?", new String[]{String.valueOf(presupuesto.getPre_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updatePresupuesto", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarPresupuesto(DatabaseHelper dbHelper, Presupuesto pre , Context context){
        SQLiteDatabase db = null;
        String preId = null;
        try{
            db = dbHelper.getWritableDatabase();
            long id = insert(pre, db);
            preId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("insertarPresupuesto", "", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return preId;
    }

    public Presupuesto getPresupuestoById(DatabaseHelper dbHelper, int presupuestoId, Context context){
        SQLiteDatabase db = null;
        Presupuesto presupuesto = null;

        try{
            db = dbHelper.getWritableDatabase();
            String query = "SELECT pre_id, pre_number, pre_presupuestoNumber, pre_date, pre_tda_id, " +
                    "pre_yea_id, pre_time, pre_cus_id, pre_numPresupuestoRed, pre_pdf " +
                    "FROM Presupuesto WHERE pre_id = '" + presupuestoId + "' and pre_deleted = '0'";
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ex getPresupuestoById()", "presupuestoId = " + presupuestoId);
                    presupuesto = new Presupuesto();
                    presupuesto.setPre_id(fila.getInt(fila.getColumnIndex("pre_id")));
                    presupuesto.setPre_number(fila.getString(fila.getColumnIndex("pre_number")));
                    presupuesto.setPre_date(fila.getString(fila.getColumnIndex("pre_date")));
                    presupuesto.setPre_tda_id(fila.getString(fila.getColumnIndex("pre_tda_id")));
                    presupuesto.setPre_yea_id(String.valueOf(fila.getString(fila.getColumnIndex("pre_yea_id"))));
                    presupuesto.setPre_time(fila.getString(fila.getColumnIndex("pre_time")));
                    presupuesto.setPre_cus_id(fila.getString(fila.getColumnIndex("pre_cus_id")));
                    presupuesto.setPre_numPresupuestoRed(fila.getString(fila.getColumnIndex("pre_numPresupuestoRed")));
                    presupuesto.setPre_pdf(fila.getString(fila.getColumnIndex("pre_pdf")));
                }
            }
            fila.close();
        }
        catch(SQLiteException sqlte){
            Log.e("GetPresupuestoById()", "presupuestoId: " + String.valueOf(presupuestoId), sqlte.getCause());
        }
        finally {
            db.close();
        }
        return presupuesto;
    }

    public int getPresupuestoByNumPreRed(SQLiteDatabase db, String numPresupuestoRed, Context context){
        int preId = 0;

        try {
            String query = "SELECT pre_id from Presupuesto where pre_numPresupuestoRed = '" + numPresupuestoRed + "' and pre_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    return preId = fila.getInt(fila.getColumnIndex("pre_id"));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getPreByNumPreRed", "numPresupuestoRed" + numPresupuestoRed, sqlte.getCause());
        } finally {
            db.close();
        }
        return preId;
    }

    public int getTotalInsertedRows(SQLiteDatabase db){
        int result = -1;

        try {
            String query = " SELECT count(*) FROM Presupuesto ";
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

    public int getPresupuestoCountByYear(SQLiteDatabase db, String dateYear, Context context){
        int result = -1;

        try {
            YearsDML yearDML = new YearsDML(context);
            int yeaId = yearDML.getPresupuestoYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            String query = " SELECT count(*) FROM Presupuesto where pre_yea_id = '" + yeaId + "' and pre_deleted ='0'";

            Cursor countRows = db.rawQuery(query, null);

            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getPreCountByYear", "dateYear: " + dateYear, sqlte.getCause());
        } finally {
            db.close();
        }
        return result;
    }

    public ArrayList<Integer> getPresupuestoNumbers(SQLiteDatabase db, int yeaId, Context context){
        ArrayList<Integer> presupuestoNumbs = null;

        try {
            presupuestoNumbs = new ArrayList<>();

            String query = " SELECT pre_presupuestoNumber FROM Presupuesto, Years " +
                    "WHERE pre_deleted = '0' and yea_id = pre_yea_id " +
                    "and yea_deleted = '0' and yea_id = '" + String.valueOf(yeaId) + "' ";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            do{
                String preNum = cursor.getString(cursor.getColumnIndex("pre_presupuestoNumber"));
                int presupuestoNumber = Integer.parseInt(preNum);
                presupuestoNumbs.add(presupuestoNumber);
            }
            while(cursor.moveToNext());
            Collections.sort(presupuestoNumbs); //Ordeno los valores para devolver la lista ordenada
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getPresupuestoNumbers", "yeaId: " + String.valueOf(yeaId), sqlte.getCause());
        } finally {
            db.close();
        }
        return presupuestoNumbs;
    }

    public void borrarPresupuesto(DatabaseHelper dbHelper, String presupuestoId, Context context){
        try {
            String query = "UPDATE Presupuesto SET pre_deleted = '1' WHERE pre_id = '" + Integer.parseInt(presupuestoId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public ArrayList<Presupuesto> getAllPresupuestos(DatabaseHelper dbHelper, Context context){
        ArrayList<Presupuesto> presupuestos = null;

        try {
            presupuestos = new ArrayList<>();
            String query = " SELECT pre_id, pre_deleted, pre_number, pre_presupuestoNumber, pre_numPresupuestoRed, " +
                    " pre_date, pre_tda_id , pre_time, pre_pdf " +
                    "FROM Presupuesto WHERE pre_deleted = '0' order by pre_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Presupuesto presupuesto = new Presupuesto();
                    presupuesto.setPre_id(cursor.getInt(cursor.getColumnIndex("pre_id")));
                    presupuesto.setPre_deleted(cursor.getString(cursor.getColumnIndex("pre_deleted")));
                    presupuesto.setPre_number(cursor.getString(cursor.getColumnIndex("pre_number")));
                    presupuesto.setPre_presupuestoNumber(cursor.getString(cursor.getColumnIndex("pre_presupuestoNumber")));
                    presupuesto.setPre_numPresupuestoRed(cursor.getString(cursor.getColumnIndex("pre_numPresupuestoRed")));
                    presupuesto.setPre_date(cursor.getString(cursor.getColumnIndex("pre_date")));
                    presupuesto.setPre_tda_id(cursor.getString(cursor.getColumnIndex("pre_tda_id")));
                    presupuesto.setPre_time(cursor.getString(cursor.getColumnIndex("pre_time")));
                    presupuesto.setPre_pdf(cursor.getString(cursor.getColumnIndex("pre_pdf")));
                    presupuestos.add(presupuesto);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getAllPresupuestos", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
        return presupuestos;
    }

    public void uploadPdfPresupuesto(DatabaseHelper dbHelper, String preId, String ruta, Context context){
        try {
            String query = "UPDATE Presupuesto SET pre_pdf = '" + ruta + "' WHERE pre_id = '" + Integer.parseInt(preId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("uploadPdfPresupuesto", "preId: " + preId + "ruta: " + ruta, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public String getPresupuestoPdfPath(DatabaseHelper dbHelper, int preId, Context context){
        String path = null;

        try {
            String query = " SELECT pre_pdf " +
                    "FROM Presupuesto WHERE pre_deleted = '0' and pre_id = '" + preId + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                path = (cursor.getString(cursor.getColumnIndex("pre_pdf")));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getPresupuestoPdfPath", "preId: " + String.valueOf(preId), sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return path;
    }

}
