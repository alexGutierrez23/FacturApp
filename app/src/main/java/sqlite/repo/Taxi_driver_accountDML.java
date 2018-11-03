package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Taxi_driver_account;

//import com.example.usuario.facturapp.attributes.Persona;
//import com.example.usuario.sqlite.data.DatabaseHelper;
//import com.example.usuario.sqlite.data.DatabaseManager;


/**
 * Created by Usuario on 11/04/2017.
 */

public class Taxi_driver_accountDML extends DatabaseHelper{
    private final String TAG = Taxi_driver_accountDML.class.getSimpleName().toString();

    private Taxi_driver_accountDML taxi_driver_accountDML;

    public Taxi_driver_accountDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Taxi_driver_account.TABLE  + "("
                + Taxi_driver_account.KEY_tda_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Taxi_driver_account.KEY_tda_deleted + " TEXT DEFAULT '0' , "
                + Taxi_driver_account.KEY_tda_name  + " TEXT, "
                + Taxi_driver_account.KEY_tda_activated  + " TEXT DEFAULT '0' , "
                + Taxi_driver_account.KEY_tda_enabled  + " TEXT DEFAULT '0' , "
                + Taxi_driver_account.KEY_tda_license_number  + " TEXT, "
                + Taxi_driver_account.KEY_tda_email  + " TEXT, "
                + Taxi_driver_account.KEY_tda_nif  + " TEXT, "
                + Taxi_driver_account.KEY_tda_adr_id  + " TEXT , "
                + Taxi_driver_account.KEY_tda_printerDevice + " TEXT, "
                + Taxi_driver_account.KEY_tda_mobile + " TEXT, "
                + Taxi_driver_account.KEY_tda_logo + " BLOB, "
                + Taxi_driver_account.KEY_tda_logo_path + " TEXT, "
                + "FOREIGN KEY (" + Taxi_driver_account.KEY_tda_adr_id + ") REFERENCES " + Address.TABLE
                + " ( " + Address.KEY_adr_id + " ) ) ";
    }

    public long insert(Taxi_driver_account taxi_driver_account, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        //datos.put(Taxi_driver_account.KEY_tda_id, taxi_driver_account.getTda_id());
        if(!TextUtils.isEmpty(taxi_driver_account.getTda_deleted())){
            datos.put(Taxi_driver_account.KEY_tda_deleted , taxi_driver_account.getTda_deleted());
        }
        datos.put(Taxi_driver_account.KEY_tda_name, taxi_driver_account.getTda_name());
        if(!TextUtils.isEmpty(taxi_driver_account.getTda_activated())){
            datos.put(Taxi_driver_account.KEY_tda_activated, taxi_driver_account.getTda_activated());
        }
        if(!TextUtils.isEmpty(taxi_driver_account.getTda_enabled())){
            datos.put(Taxi_driver_account.KEY_tda_enabled, taxi_driver_account.getTda_enabled());
        }
        datos.put(Taxi_driver_account.KEY_tda_license_number, taxi_driver_account.getTda_license_number());
        datos.put(Taxi_driver_account.KEY_tda_email, taxi_driver_account.getTda_email());
        datos.put(Taxi_driver_account.KEY_tda_nif, taxi_driver_account.getTda_nif());
        datos.put(Taxi_driver_account.KEY_tda_adr_id, taxi_driver_account.getTda_adr_id());
        datos.put(Taxi_driver_account.KEY_tda_printerDevice, taxi_driver_account.getTda_printerDevice());
        datos.put(Taxi_driver_account.KEY_tda_mobile, taxi_driver_account.getTda_mobile());
        if(taxi_driver_account.getTda_logo() != null){
            datos.put(Taxi_driver_account.KEY_tda_logo, taxi_driver_account.getTda_logo());
        }
        if(taxi_driver_account.getTda_logo_path() != null){
            datos.put(Taxi_driver_account.KEY_tda_logo_path, taxi_driver_account.getTda_logo_path());
        }

        return db.insert(Taxi_driver_account.TABLE, null, datos);
    }

    public void delete(SQLiteDatabase db) {
        try{
            db.delete(Taxi_driver_account.TABLE, null,null);
        }
        catch (SQLiteException sqlte){
            Log.e("Delete TDA", "", sqlte.getCause());
        }
        finally {
            db.close();
        }
    }

    public int update(Taxi_driver_account taxi_driver_account, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Taxi_driver_account.KEY_tda_id, taxi_driver_account.getTda_id());
            datos.put(Taxi_driver_account.KEY_tda_deleted , taxi_driver_account.getTda_deleted());
            datos.put(Taxi_driver_account.KEY_tda_name, taxi_driver_account.getTda_name());
            datos.put(Taxi_driver_account.KEY_tda_activated, taxi_driver_account.getTda_activated());
            datos.put(Taxi_driver_account.KEY_tda_enabled, taxi_driver_account.getTda_enabled());
            datos.put(Taxi_driver_account.KEY_tda_license_number, taxi_driver_account.getTda_license_number());
            datos.put(Taxi_driver_account.KEY_tda_email, taxi_driver_account.getTda_email());
            datos.put(Taxi_driver_account.KEY_tda_nif, taxi_driver_account.getTda_nif());
            datos.put(Taxi_driver_account.KEY_tda_adr_id, taxi_driver_account.getTda_adr_id());
            datos.put(Taxi_driver_account.KEY_tda_printerDevice, taxi_driver_account.getTda_printerDevice());
            datos.put(Taxi_driver_account.KEY_tda_mobile, taxi_driver_account.getTda_mobile());
            if(taxi_driver_account.getTda_logo() != null){
                datos.put(Taxi_driver_account.KEY_tda_logo, taxi_driver_account.getTda_logo());
            }
            if(taxi_driver_account.getTda_logo_path() != null){
                datos.put(Taxi_driver_account.KEY_tda_logo_path, taxi_driver_account.getTda_logo_path());
            }

            result = db.update(Taxi_driver_account.TABLE, datos, "tda_id = ?", new String[]{String.valueOf(taxi_driver_account.getTda_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateTDA", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarTaxiDriverAccount(DatabaseHelper dbHelper, Taxi_driver_accountDML taxi_driver_accountDML,
                                          Taxi_driver_account tda, Context context){
        SQLiteDatabase db = null;
        String tdaId = null;
        long id = -1;

        try{
            db = dbHelper.getWritableDatabase();
            id = taxi_driver_accountDML.insert(tda, db);
            tdaId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("Taxi_driver_account", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return tdaId;
    }

    public int getTaxiDriverAccountByNIF(SQLiteDatabase db, String nif, Context context){
        int tdaId = 0;

        try {
            String query = "SELECT tda_id from Taxi_driver_account where tda_nif = '" + nif + "' and tda_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    return tdaId = fila.getInt(fila.getColumnIndex("tda_id"));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTDAByNIF", "nif: " + nif, sqlte.getCause());
        } finally {
            db.close();
        }
        return tdaId;

    }

    public int getTdaIdByTxdId(DatabaseHelper dbHelper, String txdId, Context context){
        int tdaId = 0;
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = "SELECT tda_id from Taxi_driver_account, Taxi_Driver where tda_id = txd_tda_id" +
                    " and txd_id = '" + Integer.parseInt(txdId) + "' and tda_deleted='0' and txd_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    return tdaId = fila.getInt(fila.getColumnIndex("tda_id"));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTdaIdByTxdId", "txdId: " + txdId, sqlte.getCause());
        } finally {
            db.close();
        }
        return tdaId;
    }

    public Taxi_driver_account getTdaById(DatabaseHelper dbHelper, String tdaId, Context context){
        SQLiteDatabase db = null;
        Taxi_driver_account tda = null;

        try {
            tda = new Taxi_driver_account();
            db = dbHelper.getWritableDatabase();

            String query = "SELECT tda_id, tda_deleted, tda_name, tda_activated, tda_enabled, " +
                    "tda_license_number, tda_email, tda_nif, tda_adr_id, tda_printerDevice, " +
                    " tda_mobile, tda_logo, tda_logo_path " +
                    " from Taxi_driver_account where tda_id = '" + Integer.parseInt(tdaId) + "' and tda_deleted='0' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    tda.setTda_id(fila.getInt(fila.getColumnIndex("tda_id")));
                    tda.setTda_deleted(fila.getString(fila.getColumnIndex("tda_deleted")));
                    tda.setTda_name(fila.getString(fila.getColumnIndex("tda_name")));
                    tda.setTda_activated(fila.getString(fila.getColumnIndex("tda_activated")));
                    tda.setTda_enabled(fila.getString(fila.getColumnIndex("tda_enabled")));
                    tda.setTda_license_number(fila.getString(fila.getColumnIndex("tda_license_number")));
                    tda.setTda_email(fila.getString(fila.getColumnIndex("tda_email")));
                    tda.setTda_nif(fila.getString(fila.getColumnIndex("tda_nif")));
                    tda.setTda_adr_id(fila.getString(fila.getColumnIndex("tda_adr_id")));
                    tda.setTda_printerDevice(fila.getString(fila.getColumnIndex("tda_printerDevice")));
                    tda.setTda_mobile(fila.getString(fila.getColumnIndex("tda_mobile")));
                    tda.setTda_logo_path(fila.getString(fila.getColumnIndex("tda_logo_path")));
                    tda.setTda_logo(fila.getBlob(fila.getColumnIndex("tda_logo")));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTdaById", "tdaId: " + tdaId, sqlte.getCause());
        } finally {
            db.close();
        }
        return tda;
    }

    public Address getAddressByTdaId(DatabaseHelper dbHelper, String tdaId, Context context){
        SQLiteDatabase db = null;
        Address adr = null;

        try {
            adr = new Address();
            db = dbHelper.getWritableDatabase();
            //String query = "SELECT med_id from medicos order by med_id DESC limit 1";  //Get last element inserted
            String query = "SELECT adr_street, adr_number, adr_postcode, " +
                    "adr_town, adr_county, adr_country " +
                    " from Taxi_driver_account, Address where tda_adr_id = adr_id" +
                    " and tda_id = '" + Integer.parseInt(tdaId) + "' and tda_deleted='0' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    adr.setAdr_street(fila.getString(fila.getColumnIndex("adr_street")));
                    adr.setAdr_number(fila.getString(fila.getColumnIndex("adr_number")));
                    adr.setAdr_postcode(fila.getString(fila.getColumnIndex("adr_postcode")));
                    adr.setAdr_town(fila.getString(fila.getColumnIndex("adr_town")));
                    adr.setAdr_county(fila.getString(fila.getColumnIndex("adr_county")));
                    adr.setAdr_country(fila.getString(fila.getColumnIndex("adr_country")));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getAddressByTdaId", "tdaId: " + tdaId, sqlte.getCause());
        } finally {
            db.close();
        }
        return adr;
    }

    public int getTaxiDriverAccountCount(DatabaseHelper dbHelper, Context context){
        SQLiteDatabase db = null;
        int result = -1;

        try {
            db = dbHelper.getWritableDatabase();

            String query = " SELECT count(*) FROM Taxi_driver_account where tda_deleted = '0' ";
            Cursor countRows = db.rawQuery(query, null);
            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTDACount", "", sqlte.getCause());
        } finally {
            db.close();
        }
        return result;
    }

    public boolean isActivated(Context context, SQLiteDatabase db, String tdaId){
        boolean activated = false;

        try {

            String query = " SELECT tda_activated, tda_id, tda_name, tda_enabled, tda_printerDevice FROM Taxi_driver_account where tda_id = '" + tdaId
                    + "' and tda_deleted = '0' ";
            Cursor cursor = db.rawQuery(query, null);
            int result = -1;
            if(cursor.moveToFirst()){
                result = Integer.parseInt(cursor.getString(cursor.getColumnIndex("tda_activated")));
            }
            if(result == 1){
                activated = true;
            }
            else if(result == 0 || result == -1){
                activated = false;
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("isActivated", "tdaId: " + tdaId, sqlte.getCause());
        } finally {
            db.close();
        }
        return activated;
    }

    public void activateAccount(DatabaseHelper dbHelper, String tdaId, Context context){
        try {
            String query = "UPDATE Taxi_driver_account SET tda_activated = '1' " +
                    "WHERE tda_id = '" + Integer.parseInt(tdaId) + "' and tda_deleted ='0' ";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("activateAccount", "tdaId: " + tdaId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public HashMap<String, byte[]> getLogo(DatabaseHelper dbHelper, String tdaId, Context context){
        SQLiteDatabase db = null;
        byte[] image = null;
        String path = null;
        HashMap<String, byte[]> logoInformation = null;

        try{
            String query = "SELECT tda_logo, tda_logo_path FROM Taxi_driver_account " +
                    "WHERE tda_id = '" + tdaId + "' and tda_deleted = '0' ";

            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                logoInformation = new HashMap<>();
                image = cursor.getBlob(cursor.getColumnIndex("tda_logo"));
                path = cursor.getString(cursor.getColumnIndex("tda_logo_path"));
                logoInformation.put(path, image);
            }
        }
        catch(SQLiteException sqlte) {
            Log.e("getLogo", "tdaId: " + tdaId, sqlte.getCause());
        }
        finally {
            db.close();
        }
        return logoInformation;
    }

}
