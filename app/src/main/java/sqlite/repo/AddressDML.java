package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import sqlite.data.DatabaseHelper;
import sqlite.model.Address;


/**
 * Created by Usuario on 11/04/2017.
 */

public class AddressDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private AddressDML addressDML;

    private final String TAG = AddressDML.class.getSimpleName().toString();

    public AddressDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Address.TABLE  + "("
                + Address.KEY_adr_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Address.KEY_adr_deleted + " TEXT DEFAULT '0',"
                + Address.KEY_adr_street + " TEXT, "
                + Address.KEY_adr_town  + " TEXT, "
                + Address.KEY_adr_number  + " TEXT, "
                + Address.KEY_adr_postcode  + " TEXT, "
                + Address.KEY_adr_county  + " TEXT, "
                + Address.KEY_adr_country  + " TEXT ) ";
    }

    public long insert(Address adr, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Address.KEY_adr_street, adr.getAdr_street());
        datos.put(Address.KEY_adr_town, adr.getAdr_town());
        datos.put(Address.KEY_adr_number, adr.getAdr_number());
        datos.put(Address.KEY_adr_postcode, adr.getAdr_postcode());
        datos.put(Address.KEY_adr_county, adr.getAdr_county());
        datos.put(Address.KEY_adr_country, adr.getAdr_country());
        long id = db.insert(Address.TABLE, null, datos);

        return id;
    }

    public int update(Address adr, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Address.KEY_adr_id, adr.getAdr_id());
            datos.put(Address.KEY_adr_deleted, adr.getAdr_deleted());
            datos.put(Address.KEY_adr_street, adr.getAdr_street());
            datos.put(Address.KEY_adr_town, adr.getAdr_town());
            datos.put(Address.KEY_adr_number, adr.getAdr_number());
            datos.put(Address.KEY_adr_postcode, adr.getAdr_postcode());
            datos.put(Address.KEY_adr_county, adr.getAdr_county());
            datos.put(Address.KEY_adr_country, adr.getAdr_country());

            result = db.update(Address.TABLE, datos, "adr_id = ?", new String[]{String.valueOf(adr.getAdr_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateAddress", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public void delete( SQLiteDatabase db) {
        db.delete(Address.TABLE, null,null);

    }

    public String insertarAddress(DatabaseHelper dbHelper, Address adr , Context context) {
        SQLiteDatabase db = null;
        String adrId = null;
        try {
            db = dbHelper.getWritableDatabase();
            long id = insert(adr, db);
            adrId = String.valueOf(id);
        } catch (SQLiteException sqlte) {
            Log.w("insertar address", "");
        }
        db.close();
        return adrId;
    }

    public Address getAddressByTdaId(DatabaseHelper dbHelper, String tdaId, Context context){
        Address adr = null;
        SQLiteDatabase db = null;
        try{
            adr = new Address();
            db = dbHelper.getWritableDatabase();
            //String query = "SELECT med_id from medicos order by med_id DESC limit 1";  //Get last element inserted
            String query = "SELECT adr_id, adr_deleted, adr_street, adr_number, adr_postcode, " +
                    "adr_town, adr_county, adr_country " +
                    " from Taxi_driver_account, Address where tda_adr_id = adr_id" +
                    " and tda_id = '" + Integer.parseInt(tdaId) + "' and tda_deleted='0' and adr_deleted = '0' ";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    adr.setAdr_id(fila.getInt(fila.getColumnIndex("adr_id")));
                    adr.setAdr_deleted(fila.getString(fila.getColumnIndex("adr_deleted")));
                    adr.setAdr_street(fila.getString(fila.getColumnIndex("adr_street")));
                    adr.setAdr_number(fila.getString(fila.getColumnIndex("adr_number")));
                    adr.setAdr_postcode(fila.getString(fila.getColumnIndex("adr_postcode")));
                    adr.setAdr_town(fila.getString(fila.getColumnIndex("adr_town")));
                    adr.setAdr_county(fila.getString(fila.getColumnIndex("adr_county")));
                    adr.setAdr_country(fila.getString(fila.getColumnIndex("adr_country")));
                }
            }
            fila.close();
        }
        catch (SQLiteException sqlte){
            Log.w("Get address", "tdaId: " + tdaId);
        }
        finally {
            db.close();
        }

        return adr;
    }

    public Address getAddressById(SQLiteDatabase db, String adrId, Context context){
        Address adr = null;

        try{
            adr = new Address();
            String query = "SELECT adr_id, adr_deleted, adr_street, adr_number, adr_postcode, " +
                    "adr_town, adr_county, adr_country " +
                    " from Address where adr_id = '" + adrId + "' and adr_deleted = '0' ";
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    adr.setAdr_id(fila.getInt(fila.getColumnIndex("adr_id")));
                    adr.setAdr_deleted(fila.getString(fila.getColumnIndex("adr_deleted")));
                    adr.setAdr_street(fila.getString(fila.getColumnIndex("adr_street")));
                    adr.setAdr_number(fila.getString(fila.getColumnIndex("adr_number")));
                    adr.setAdr_postcode(fila.getString(fila.getColumnIndex("adr_postcode")));
                    adr.setAdr_town(fila.getString(fila.getColumnIndex("adr_town")));
                    adr.setAdr_county(fila.getString(fila.getColumnIndex("adr_county")));
                    adr.setAdr_country(fila.getString(fila.getColumnIndex("adr_country")));
                }
            }
            fila.close();
        }
        catch (SQLiteException sqle){
            Log.w("Get address", "adrId" + adrId);
        }
        finally {
            db.close();
        }

        return adr;
    }



}
