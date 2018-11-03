package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import sqlite.data.DatabaseHelper;
import sqlite.model.Trip;


/**
 * Created by Usuario on 11/04/2017.
 */

public class TripDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private TripDML tripDML;

    private final String TAG = TripDML.class.getSimpleName().toString();

    public TripDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Trip.TABLE  + "("
                + Trip.KEY_trp_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Trip.KEY_trp_deleted + " TEXT DEFAULT '0' , "
                + Trip.KEY_trp_start  + " TEXT, "
                + Trip.KEY_trp_finish  + " TEXT, "
                + Trip.KEY_trp_price  + " TEXT, "
                + Trip.KEY_trp_start_place + " TEXT, "
                + Trip.KEY_trp_number + " TEXT ) ";
    }

    public long insert(Trip trip, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Trip.KEY_trp_start, trip.getTrp_start());
        datos.put(Trip.KEY_trp_finish, trip.getTrp_finish());
        datos.put(Trip.KEY_trp_price, trip.getTrp_price());
        datos.put(Trip.KEY_trp_number, trip.getTrp_number());
        datos.put(Trip.KEY_trp_start_place, trip.getTrp_start_place());

        return db.insert(Trip.TABLE, null, datos);
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Trip.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete trip", "", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Trip trip, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Trip.KEY_trp_id, trip.getTrp_id());
            datos.put(Trip.KEY_trp_deleted , trip.getTrp_deleted());
            datos.put(Trip.KEY_trp_start, trip.getTrp_start());
            datos.put(Trip.KEY_trp_finish, trip.getTrp_finish());
            datos.put(Trip.KEY_trp_price, trip.getTrp_price());
            datos.put(Trip.KEY_trp_number, trip.getTrp_number());
            datos.put(Trip.KEY_trp_start_place, trip.getTrp_start_place());

            result = db.update(Trip.TABLE, datos, "trp_id = ?", new String[]{String.valueOf(trip.getTrp_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateTrip", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarTrip(DatabaseHelper dbHelper, Trip trp , Context context){
        SQLiteDatabase db = null;
        String trpId = null;
        try{
            db = dbHelper.getWritableDatabase();
            TripDML tripDml = new TripDML(context);
            long id = tripDml.insert(trp, db);
            trpId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("insertarTrip", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return trpId;
    }

    public int getTripByNumber(SQLiteDatabase db, String tripNumber, Context context){
        int trpId = 0;

        try {

            String query = "SELECT trp_id from Trip where trp_number = '" + tripNumber + "' and trp_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    return trpId = fila.getInt(fila.getColumnIndex("trp_id"));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTripByNumber", "tripNumber: " + tripNumber, sqlte.getCause());
        } finally {
            db.close();
        }

        return trpId;
    }

    public Trip getTripByTicketId(DatabaseHelper dbHelper, String tckId, Context context){
        Trip t = null;

        try{
            t = new Trip();
            String query = "SELECT trp_start, trp_finish, trp_price " +
                    "from Ticket, Trip where tck_id = '" + Integer.parseInt(tckId) + "' and tck_deleted='0' and trp_deleted = '0'" +
                    "and tck_trp_id = trp_id ";  //Get last element inserted
            Cursor fila = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    t.setTrp_start(fila.getString(fila.getColumnIndex("trp_start")));
                    t.setTrp_finish(fila.getString(fila.getColumnIndex("trp_finish")));
                    t.setTrp_price(fila.getString(fila.getColumnIndex("trp_price")));
                }
            }
            fila.close();
        }
        catch (SQLiteException sqle){
            Log.e("getTripByTicketId", "Id" + tckId);
        }
        finally {
            dbHelper.getWritableDatabase().close();
        }
        return t;
    }

    public Trip getTripById(SQLiteDatabase db, String trpId, Context context){
        Trip t = null;

        try{
            t = new Trip();
            String query = "SELECT trp_id, trp_deleted, trp_start, trp_finish, trp_price, trp_number, trp_start_place " +
                    "from Trip where trp_id = '" + Integer.parseInt(trpId) + "' and trp_deleted = '0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    t.setTrp_id(fila.getInt(fila.getColumnIndex("trp_id")));
                    t.setTrp_deleted(fila.getString(fila.getColumnIndex("trp_deleted")));
                    t.setTrp_start(fila.getString(fila.getColumnIndex("trp_start")));
                    t.setTrp_finish(fila.getString(fila.getColumnIndex("trp_finish")));
                    t.setTrp_price(fila.getString(fila.getColumnIndex("trp_price")));
                    t.setTrp_number(fila.getString(fila.getColumnIndex("trp_number")));
                    t.setTrp_start_place(fila.getString(fila.getColumnIndex("trp_start_place")));
                }
            }
            fila.close();
        }
        catch (SQLiteException sqle){
            Log.e("GetTrip", "Id" + trpId, sqle.getCause());
        }
        finally {
            db.close();
        }
        return t;
    }

}
