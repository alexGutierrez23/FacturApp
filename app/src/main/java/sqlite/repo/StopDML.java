package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import sqlite.data.DatabaseHelper;
import sqlite.model.HojaRuta;
import sqlite.model.Stop;


/**
 * Created by Usuario on 11/04/2017.
 */

public class StopDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private StopDML stopDML;

    private final String TAG = StopDML.class.getSimpleName().toString();

    public StopDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Stop.TABLE  + "("
                + Stop.KEY_stp_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Stop.KEY_stp_deleted + " TEXT DEFAULT '0' , "
                + Stop.KEY_stp_place  + " TEXT, "
                + Stop.KEY_stp_hjr_id  + " TEXT, "
                + "FOREIGN KEY (" + Stop.KEY_stp_hjr_id + ") REFERENCES " + HojaRuta.TABLE
                + " ( " + HojaRuta.KEY_hjr_id + " ) ) ";
    }

    public long insert(Stop stop, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Stop.KEY_stp_place, stop.getStp_place());
        datos.put(Stop.KEY_stp_hjr_id, stop.getStp_hjr_id());

        long id = db.insert(Stop.TABLE, null, datos);

        return id;
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Stop.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Stop stop, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Stop.KEY_stp_id, stop.getStp_id());
            datos.put(Stop.KEY_stp_deleted, stop.getStp_deleted());
            datos.put(Stop.KEY_stp_place, stop.getStp_place());
            datos.put(Stop.KEY_stp_hjr_id, stop.getStp_hjr_id());

            result = db.update(Stop.TABLE, datos, "stp_id = ?", new String[]{String.valueOf(stop.getStp_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateStop", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarParada(DatabaseHelper dbHelper, Stop stop, Context context){
        SQLiteDatabase db = null;
        String stpId = null;
        try{
            db = dbHelper.getWritableDatabase();
            StopDML stpDml = new StopDML(context);
            long id = insert(stop, db);
            stpId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("stop", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return stpId;
    }


    public ArrayList<Stop> getStopsByHojaRutaId(DatabaseHelper dbHelper, String hojaRutaId, Context context){
        ArrayList<Stop> stops = null;

        try {
            stops = new ArrayList<>();

            String query = " SELECT stp_id, stp_deleted, stp_place, stp_hjr_id " +
                    " FROM Stop WHERE stp_deleted = '0' and stp_hjr_id = '" + hojaRutaId + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Stop stop = new Stop();
                    stop.setStp_id(cursor.getInt(cursor.getColumnIndex("stp_id")));
                    stop.setStp_deleted(cursor.getString(cursor.getColumnIndex("stp_deleted")));
                    stop.setStp_place(cursor.getString(cursor.getColumnIndex("stp_place")));
                    stop.setStp_hjr_id(cursor.getString(cursor.getColumnIndex("stp_hjr_id")));

                    stops.add(stop);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getStopsByHojaRutaId", "hojaRutaId: " + hojaRutaId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return stops;
    }


    public void borrarTicket(DatabaseHelper dbHelper, String tckId, Context context){
        try {
            String query = "UPDATE Ticket SET tck_deleted = '1' WHERE tck_id = '" + Integer.parseInt(tckId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("borrarTicket", "tckId: " + tckId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

}
