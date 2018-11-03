package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import sqlite.data.DatabaseHelper;
import sqlite.model.Taxi_Driver;
import sqlite.model.Taxi_driver_account;


/**
 * Created by Usuario on 11/04/2017.
 */

public class Taxi_DriverDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private Taxi_DriverDML taxiDriverDML;

    private final String TAG = Taxi_DriverDML.class.getSimpleName().toString();

    public Taxi_DriverDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Taxi_Driver.TABLE  + "("
                + Taxi_Driver.KEY_txd_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Taxi_Driver.KEY_txd_deleted + " TEXT DEFAULT '0' , "
                + Taxi_Driver.KEY_txd_email  + " TEXT, "
                + Taxi_Driver.KEY_txd_confirmationCode + " TEXT, "
                + Taxi_Driver.KEY_txd_confirmationCodeEncoded + " TEXT, "
                + Taxi_Driver.KEY_txd_logLoaded + " TEXT DEFAULT '0' , "
                + Taxi_Driver.KEY_txd_tda_id  + " TEXT , "
                + " FOREIGN KEY (" + Taxi_Driver.KEY_txd_tda_id + ") REFERENCES " + Taxi_driver_account.TABLE
                + " ( " + Taxi_driver_account.KEY_tda_id + " ) ) ";
    }

    public String insert(String email, String confirmationCode, String confirmationCodeEncoded,
                         int txd_tda_id, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Taxi_Driver.KEY_txd_email, email);
        datos.put(Taxi_Driver.KEY_txd_confirmationCode, confirmationCode);
        datos.put(Taxi_Driver.KEY_txd_confirmationCodeEncoded, confirmationCodeEncoded);
        datos.put(Taxi_Driver.KEY_txd_tda_id, txd_tda_id);
        long txdId = db.insert(Taxi_Driver.TABLE, null, datos);

        return String.valueOf(txdId);
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Taxi_Driver.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Taxi_Driver taxiDriver, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Taxi_Driver.KEY_txd_id, taxiDriver.getTxd_id());
            datos.put(Taxi_Driver.KEY_txd_deleted , taxiDriver.getTxd_deleted());
            datos.put(Taxi_Driver.KEY_txd_email, taxiDriver.getTxd_email());
            datos.put(Taxi_Driver.KEY_txd_confirmationCode, taxiDriver.getTxd_confirmationCode());
            datos.put(Taxi_Driver.KEY_txd_confirmationCodeEncoded, taxiDriver.getTxd_confirmationCodeEncoded());
            datos.put(Taxi_Driver.KEY_txd_logLoaded, taxiDriver.getTxd_logLoaded());
            datos.put(Taxi_Driver.KEY_txd_tda_id, taxiDriver.getTxd_tda_id());

            result = db.update(Taxi_Driver.TABLE, datos, "txd_id = ?", new String[]{String.valueOf(taxiDriver.getTxd_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateTXD", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarTaxiDriver(DatabaseHelper dbHelper, String email, String confirmationCode,
                                     String confirmationCodeEncoded, int tdaId, Context context){
        SQLiteDatabase db = null;
        String txdId = null;
        try{
            db = dbHelper.getWritableDatabase();
            //Primero, insertamos taxi_driver_account y buscamos ese ID
            txdId = insert(email, confirmationCode, confirmationCodeEncoded, tdaId, db);
            return txdId;
        }
        catch(SQLiteException sqlte){
            Log.e("Insertando", "Taxi driver", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return txdId;
    }

    public Taxi_Driver getTaxiDriverById(SQLiteDatabase db, String txdId, String logLoaded, Context context){
        Taxi_Driver txd = null;

        try {
            String query = "SELECT txd_id, txd_deleted, txd_email, txd_confirmationCode, " +
                    "txd_confirmationCodeEncoded, txd_tda_id, txd_logLoaded" +
                    " FROM Taxi_Driver WHERE txd_id = '" + Integer.parseInt(txdId) + "' and txd_deleted ='0' ";
            Cursor cursor = db.rawQuery(query, null);
            txd = new Taxi_Driver();
            if(cursor.moveToFirst()){
                txd.setTxd_id(cursor.getInt(cursor.getColumnIndex("txd_id")));
                txd.setTxd_deleted(cursor.getString(cursor.getColumnIndex("txd_deleted")));
                txd.setTxd_email(cursor.getString(cursor.getColumnIndex("txd_email")));
                txd.setTxd_confirmationCode(cursor.getString(cursor.getColumnIndex("txd_confirmationCode")));
                txd.setTxd_confirmationCodeEncoded(cursor.getString(cursor.getColumnIndex("txd_confirmationCodeEncoded")));
                txd.setTxd_tda_id(cursor.getString(cursor.getColumnIndex("txd_tda_id")));
                txd.setTxd_logLoaded(logLoaded);
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTaxiDriverById", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return txd;
    }

    public Taxi_Driver getTaxiDriverById(SQLiteDatabase db, String txdId, Context context){
        Taxi_Driver txd = null;

        try {
            String query = "SELECT txd_id, txd_deleted, txd_email, txd_confirmationCode, " +
                    "txd_confirmationCodeEncoded, txd_tda_id, txd_logLoaded" +
                    " FROM Taxi_Driver WHERE txd_id = '" + Integer.parseInt(txdId) + "' and txd_deleted ='0' ";
            Cursor cursor = db.rawQuery(query, null);
            txd = new Taxi_Driver();
            if(cursor.moveToFirst()){
                txd.setTxd_id(cursor.getInt(cursor.getColumnIndex("txd_id")));
                txd.setTxd_deleted(cursor.getString(cursor.getColumnIndex("txd_deleted")));
                txd.setTxd_email(cursor.getString(cursor.getColumnIndex("txd_email")));
                txd.setTxd_confirmationCode(cursor.getString(cursor.getColumnIndex("txd_confirmationCode")));
                txd.setTxd_confirmationCodeEncoded(cursor.getString(cursor.getColumnIndex("txd_confirmationCodeEncoded")));
                txd.setTxd_tda_id(cursor.getString(cursor.getColumnIndex("txd_tda_id")));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTaxiDriverById", "txdId: " + txdId, sqlte.getCause());
        } finally {
            db.close();
        }

        return txd;
    }

    public boolean updateLogLoaded(DatabaseHelper dbHelper, String txdId, Context context){
        SQLiteDatabase db = null;
        boolean updated = false;

        try {
            db = dbHelper.getWritableDatabase();
            Taxi_Driver txd = getTaxiDriverById(new DatabaseHelper(context).getWritableDatabase(), txdId, "1", context);  // "1" means UPDATED AND LOG HAS BEEN READ!
            int rowsUpdated = update(txd, db);
            if(rowsUpdated == 1){
                updated = true;
            }
        } catch (SQLiteException sqlte) {
            Log.e("updateLogLoaded", "txdId: " + txdId, sqlte.getCause());
        }

        return updated;
    }

    public boolean areLogLoaded(DatabaseHelper dbHelper, String txdId, Context context){
        SQLiteDatabase db = null;
        boolean loaded = false;

        try {
            db = dbHelper.getWritableDatabase();
            String query = "SELECT txd_logLoaded FROM Taxi_Driver WHERE txd_id = '" + Integer.parseInt(txdId) + "' and txd_deleted ='0' ";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                String logLoaded = cursor.getString(cursor.getColumnIndex("txd_logLoaded"));
                if(logLoaded.equals("1")){
                    loaded = true;
                }
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("areLogLoaded", "txdId: " + txdId, sqlte.getCause());
        } finally {
            db.close();
        }

        return loaded;
    }

    public int getTaxiDriverCount(DatabaseHelper dbHelper, Context context){
        int result = -1;
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT count(*) FROM Taxi_Driver where txd_deleted = '0' ";
            Cursor countRows = db.rawQuery(query, null);

            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTaxiDriverCount", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public Taxi_Driver getTaxiDriver(SQLiteDatabase db, Context context){
        Taxi_Driver txd = null;

        try {
            String query = "SELECT txd_id, txd_deleted, txd_email, txd_confirmationCode, txd_confirmationCodeEncoded, " +
                    " txd_tda_id, txd_logLoaded " +
                    " FROM Taxi_Driver WHERE txd_deleted ='0' ";
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                String deleted = cursor.getString(cursor.getColumnIndex("txd_deleted"));
                if(deleted.equals("0")){
                    txd = new Taxi_Driver();
                    txd.setTxd_id(cursor.getInt(cursor.getColumnIndex("txd_id")));
                    txd.setTxd_deleted(deleted);
                    txd.setTxd_email(cursor.getString(cursor.getColumnIndex("txd_email")));
                    txd.setTxd_confirmationCode(cursor.getString(cursor.getColumnIndex("txd_confirmationCode")));
                    txd.setTxd_confirmationCodeEncoded(cursor.getString(cursor.getColumnIndex("txd_confirmationCodeEncoded")));
                    txd.setTxd_tda_id(cursor.getString(cursor.getColumnIndex("txd_tda_id")));
                    txd.setTxd_logLoaded(cursor.getString(cursor.getColumnIndex("txd_logLoaded")));
                }
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTaxiDriver", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return txd;
    }

}
