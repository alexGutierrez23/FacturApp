package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import sqlite.data.DatabaseHelper;
import sqlite.model.LastFile;


/**
 * Created by Usuario on 11/04/2017.
 */

public class LastFileDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private LastFileDML lastFileDML;

    private final String TAG = LastFileDML.class.getSimpleName().toString();

    public LastFileDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + LastFile.TABLE  + "("
                + LastFile.KEY_lfi_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LastFile.KEY_lfi_deleted + " TEXT DEFAULT '0', "
                + LastFile.KEY_lfi_tck_id + " TEXT , "
                + LastFile.KEY_lfi_inv_id + " TEXT , "
                + LastFile.KEY_lfi_pre_id + " TEXT ) ";
                //+ LastFile.KEY_lfi_hojaRuta + " TEXT ) ";
    }

    public long insert(LastFile lfi, SQLiteDatabase db) {  //dateYear is the year: 2017, 2018, 2019
        ContentValues datos = new ContentValues();
        datos.put(LastFile.KEY_lfi_tck_id, lfi.getLfi_tck_id());
        datos.put(LastFile.KEY_lfi_inv_id, lfi.getLfi_inv_id());
        datos.put(LastFile.KEY_lfi_pre_id, lfi.getLfi_pre_id());
     //   datos.put(LastFile.KEY_lfi_hojaRuta, lfi.getLfi_hojaRuta());
        // Inserting Row
        long lfiId = db.insert(LastFile.TABLE, null, datos);

        return lfiId;
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(LastFile.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete last file", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(LastFile lfi, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(LastFile.KEY_lfi_tck_id, lfi.getLfi_tck_id());
            datos.put(LastFile.KEY_lfi_inv_id, lfi.getLfi_inv_id());
            datos.put(LastFile.KEY_lfi_pre_id, lfi.getLfi_pre_id());
            //   datos.put(LastFile.KEY_lfi_hojaRuta, lfi.getLfi_hojaRuta());

            result = db.update(LastFile.TABLE, datos, "lfi_id = ?", new String[]{String.valueOf(lfi.getLfi_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateLastFile", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public int getLastTicket(DatabaseHelper dbHelper, Context context){
        SQLiteDatabase db = null;
        int result = -1;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT lfi_id FROM LastFile where lfi_deleted ='0' and lfi_tck_id IS NOT NULL";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                result = cursor.getInt(cursor.getColumnIndex("lfi_id"));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getLastTicket", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public String getLastTicketId(DatabaseHelper dbHelper, Context context){
        SQLiteDatabase db = null;
        String result = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT lfi_tck_id FROM LastFile where lfi_deleted ='0' and lfi_tck_id IS NOT NULL";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                result = cursor.getString(cursor.getColumnIndex("lfi_tck_id"));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getLastTicketId", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public void lastFileTicket(DatabaseHelper dbHelper, int lfiId,
                               String tckId, Context context){
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = null;
            if(lfiId > 0 ){
                query = "UPDATE LastFile SET lfi_tck_id = '" + tckId + "' WHERE lfi_id = '" + lfiId + "' and lfi_deleted = '0'";
                db.execSQL(query);
            }
            else{
                LastFile lf = new LastFile();
                lf.setLfi_tck_id(tckId);
                insert(lf, db);
            }
        } catch (SQLiteException sqlte) {
            Log.e("lastFileTicket", "tckId: " + tckId + ", lfiId: " + String.valueOf(lfiId), sqlte.getCause());
        } finally {
            db.close();
        }

        db.close();
    }

    public void deleteFile(DatabaseHelper dbHelper, int lfiId, Context context){
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = "UPDATE LastFile SET lfi_deleted = '1' WHERE lfi_id = '" + lfiId + "' ";
            db.execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("deleteFile", "lfiId: " + String.valueOf(lfiId), sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int getLastInvoice(DatabaseHelper dbHelper, Context context){
        int result = -1;
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT lfi_id FROM LastFile where lfi_deleted ='0' and lfi_inv_id IS NOT NULL";
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                result = cursor.getInt(cursor.getColumnIndex("lfi_id"));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getLastInvoice", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public String getLastInvoiceId(DatabaseHelper dbHelper, Context context){
        String result = null;
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT lfi_inv_id FROM LastFile where lfi_deleted ='0' and lfi_inv_id IS NOT NULL";
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                result = cursor.getString(cursor.getColumnIndex("lfi_inv_id"));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getLastInvoiceId", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public void lastFileInvoice(DatabaseHelper dbHelper, int lfiId,
                                String invId, Context context){
        SQLiteDatabase db = null;
        String query = null;

        try {
            db = dbHelper.getWritableDatabase();
            if(lfiId > 0 ){
                query = "UPDATE LastFile SET lfi_inv_id = '" + invId + "' WHERE lfi_id = '" + lfiId + "' and lfi_deleted = '0'";
                db.execSQL(query);
            }
            else{
                LastFile lf = new LastFile();
                lf.setLfi_inv_id(invId);
                insert(lf, db);
            }
        } catch (SQLiteException sqlte) {
            Log.e("lastFileInvoice", "invId: " + invId + ", lfiId: " + String.valueOf(lfiId), sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int getLastPresupuesto(DatabaseHelper dbHelper, Context context){
        SQLiteDatabase db = null;
        int result = -1;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT lfi_id FROM LastFile where lfi_deleted ='0' and lfi_pre_id IS NOT NULL";
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                result = cursor.getInt(cursor.getColumnIndex("lfi_id"));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getLastPresupuesto", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public void lastFilePresupuesto(DatabaseHelper dbHelper, int lfiId,
                               String preId, Context context){
        SQLiteDatabase db = null;
        String query = null;

        try {
            db = dbHelper.getWritableDatabase();
            if(lfiId > 0 ){
                query = "UPDATE LastFile SET lfi_pre_id = '" + preId + "' WHERE lfi_id = '" + lfiId + "' and lfi_deleted = '0'";
                db.execSQL(query);
            }
            else{
                LastFile lf = new LastFile();
                lf.setLfi_pre_id(preId);
                insert(lf, db);
            }
        } catch (SQLiteException sqlte) {
            Log.e("lastFilePresupuesto", "preId: " + preId + ", lfiId: " + String.valueOf(lfiId), sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public String getLastPresupuestoId(DatabaseHelper dbHelper, Context context){
        SQLiteDatabase db = null;
        String result = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT lfi_pre_id FROM LastFile where lfi_deleted ='0' and lfi_pre_id IS NOT NULL";
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                result = cursor.getString(cursor.getColumnIndex("lfi_pre_id"));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getLastPresupuestoId", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

}
