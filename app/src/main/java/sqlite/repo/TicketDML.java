package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import sqlite.data.DatabaseHelper;
import sqlite.model.Customer;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Ticket;
import sqlite.model.Trip;
import sqlite.model.Years;


/**
 * Created by Usuario on 11/04/2017.
 */

public class TicketDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private TicketDML ticketDML;

    private final String TAG = TicketDML.class.getSimpleName().toString();

    public TicketDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Ticket.TABLE  + "("
                + Ticket.KEY_tck_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Ticket.KEY_tck_deleted + " TEXT DEFAULT '0' , "
                + Ticket.KEY_tck_number  + " TEXT, "
                + Ticket.KEY_tck_ticketNumber + " TEXT, "
                + Ticket.KEY_tck_numTickRed + " TEXT, "
                + Ticket.KEY_tck_date  + " TEXT, "
                + Ticket.KEY_tck_printed  + " TEXT DEFAULT '0' , "
                + Ticket.KEY_tck_emailed  + " TEXT DEFAULT '0' , "
                + Ticket.KEY_tck_time  + " TEXT, "
                + Ticket.KEY_tck_price  + " TEXT, "
                + Ticket.KEY_tck_pdf  + " TEXT, "
                + Ticket.KEY_tck_tda_id  + " TEXT , "
                + Ticket.KEY_tck_trp_id  + " TEXT , "
                + Ticket.KEY_tck_yea_id + " TEXT , "
                + Ticket.KEY_tck_cus_id  + " TEXT , "
                + "FOREIGN KEY (" + Ticket.KEY_tck_tda_id + ") REFERENCES " + Taxi_driver_account.TABLE
                + " ( " + Taxi_driver_account.KEY_tda_id + " ), "  //es posible que no haga falta añadir otra vez key_adr_id
                + "FOREIGN KEY (" + Ticket.KEY_tck_trp_id + ") REFERENCES " + Trip.TABLE
                + " ( " + Trip.KEY_trp_id + " ), "
                + "FOREIGN KEY (" + Ticket.KEY_tck_yea_id + ") REFERENCES " + Years.TABLE
                + " ( " + Years.KEY_yea_id + " ), "
                + "FOREIGN KEY (" + Ticket.KEY_tck_cus_id + ") REFERENCES " + Customer.TABLE
                + " ( " + Customer.KEY_cus_id + " ) ) ";
    }

    public long insert(Ticket ticket, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Ticket.KEY_tck_number, ticket.getTck_number());
        datos.put(Ticket.KEY_tck_ticketNumber, ticket.getTck_ticketNumber());
        datos.put(Ticket.KEY_tck_date, ticket.getTck_date());
        if(ticket.getTck_printed() != null){
            if(!ticket.getTck_printed().isEmpty()){
                datos.put(Ticket.KEY_tck_printed, ticket.getTck_printed());
            }
        }
        if(ticket.getTck_emailed() != null){
            if(!ticket.getTck_emailed().isEmpty()){
                datos.put(Ticket.KEY_tck_emailed, ticket.getTck_emailed());
            }
        }
        datos.put(Ticket.KEY_tck_tda_id, ticket.getTck_tda_id());
        datos.put(Ticket.KEY_tck_time, ticket.getTck_time());
        datos.put(Ticket.KEY_tck_trp_id, ticket.getTck_trp_id());
        datos.put(Ticket.KEY_tck_cus_id, ticket.getTck_cus_id());
        datos.put(Ticket.KEY_tck_price, ticket.getTck_price());
        datos.put(Ticket.KEY_tck_pdf, ticket.getTck_pdf());
        datos.put(Ticket.KEY_tck_numTickRed, ticket.getTck_numTickRed());
        datos.put(Ticket.KEY_tck_yea_id, ticket.getTck_yea_id());
        // Inserting Row
        long id = db.insert(Ticket.TABLE, null, datos);

        return id;
    }

    public void delete(SQLiteDatabase db) {
        try {
            db.delete(Ticket.TABLE, null,null);
        } catch (SQLiteException sqlte) {
            Log.e("delete", "Ticket", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int update(Ticket ticket, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Ticket.KEY_tck_id, ticket.getTck_id());
            datos.put(Ticket.KEY_tck_deleted , ticket.getTck_deleted());
            datos.put(Ticket.KEY_tck_number, ticket.getTck_number());
            datos.put(Ticket.KEY_tck_ticketNumber, ticket.getTck_ticketNumber());
            datos.put(Ticket.KEY_tck_date, ticket.getTck_date());
            datos.put(Ticket.KEY_tck_printed, ticket.getTck_printed());
            datos.put(Ticket.KEY_tck_emailed, ticket.getTck_emailed());
            datos.put(Ticket.KEY_tck_tda_id, ticket.getTck_tda_id());
            datos.put(Ticket.KEY_tck_time, ticket.getTck_time());
            datos.put(Ticket.KEY_tck_trp_id, ticket.getTck_trp_id());
            datos.put(Ticket.KEY_tck_cus_id, ticket.getTck_cus_id());
            datos.put(Ticket.KEY_tck_price, ticket.getTck_price());
            datos.put(Ticket.KEY_tck_pdf, ticket.getTck_pdf());
            datos.put(Ticket.KEY_tck_numTickRed, ticket.getTck_numTickRed());
            datos.put(Ticket.KEY_tck_yea_id, ticket.getTck_yea_id());

            result = db.update(Ticket.TABLE, datos, "tck_id = ?", new String[]{String.valueOf(ticket.getTck_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateTicket", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarTicket(DatabaseHelper dbHelper, Ticket tck , Context context){
        SQLiteDatabase db = null;
        String tckId = null;
        try{
            db = dbHelper.getWritableDatabase();
            TicketDML ticketDml = new TicketDML(context);
            long id = insert(tck, db);
            tckId = String.valueOf(id);
           // int tck_id = ticketDml.getTripByNumber(db, trp.getTrp_number(),context);
//            devolver el id de ticket luego
        }
        catch(SQLiteException sqlte){
            Log.e("insertarTicket", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return tckId;
    }

    public int getTicketByNumTicketRed(DatabaseHelper dbHelper, String numTickRed, Context context){
        int tckId = 0;

        try {
            String query = "SELECT tck_id from Ticket where tck_numTickRed = '" + numTickRed + "' and tck_deleted='0'";  //Get last element inserted
            Cursor fila = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    return tckId = fila.getInt(fila.getColumnIndex("tck_id"));
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTicketByNumTicketRed", "numTickRed: " + numTickRed, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return tckId;
    }

    public Ticket getTicketById(DatabaseHelper dbHelper, String tckId, Context context){
        Ticket t = null;

        try{
            t = new Ticket();
            String query = "SELECT tck_id, tck_number, tck_ticketNumber, tck_numTickRed, tck_date, tck_time, tck_pdf " +
                    "from Ticket where tck_id = '" + Integer.parseInt(tckId) + "' and tck_deleted='0' ";  //Get last element inserted
            Cursor fila = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    t.setTck_id(fila.getInt(fila.getColumnIndex("tck_id")));
                    t.setTck_number(fila.getString(fila.getColumnIndex("tck_number")));
                    t.setTck_ticketNumber(fila.getString(fila.getColumnIndex("tck_ticketNumber")));
                    t.setTck_numTickRed(fila.getString(fila.getColumnIndex("tck_numTickRed")));
                    t.setTck_date(fila.getString(fila.getColumnIndex("tck_date")));
                    t.setTck_time(fila.getString(fila.getColumnIndex("tck_time")));
                    t.setTck_pdf(fila.getString(fila.getColumnIndex("tck_pdf")));
                    return t;
                }
            }
            fila.close();
        }
        catch (SQLiteException sqle){
            Log.e("getTicketById", "Id" + tckId, sqle.getCause());
        }
        finally {
            dbHelper.getWritableDatabase().close();
        }
        return t;
    }

    public void clearTable(SQLiteDatabase db){
        try {
            db.execSQL(" delete from Ticket ");
        } catch (SQLiteException sqlte) {
            Log.e("clearTable", "Tickets", sqlte.getCause());
        } finally {
            db.close();
        }
    }

    public int getTicketCountByYear(DatabaseHelper dbHelper, String dateYear, Context context){
        SQLiteDatabase db = null;
        int result = -1;
        try {
            YearsDML yearDML = new YearsDML(context);
            db = dbHelper.getWritableDatabase();
            int yeaId = yearDML.getTicketYearByDateYear(new DatabaseHelper(context).getWritableDatabase(), dateYear, context);
            String query = " SELECT count(*) FROM Ticket where tck_yea_id = '" + yeaId + "' and tck_deleted ='0' ";
            Cursor countRows = db.rawQuery(query, null);

            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTicketCountByYear", "dateYear: " + dateYear, sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public ArrayList<Integer> getTicketNumbers(DatabaseHelper dbHelper, int yeaId, Context context){
        ArrayList<Integer> ticketNumbs = null;

        try {
            ticketNumbs = new ArrayList<>();

            String query = " SELECT tck_ticketNumber FROM Ticket, Years WHERE tck_deleted = '0' " +
                    "and yea_id = tck_yea_id " +
                    "and yea_deleted = '0' and yea_id = '" + String.valueOf(yeaId) + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            do{
                String tckNum = cursor.getString(cursor.getColumnIndex("tck_ticketNumber"));
                int ticketNumber = Integer.parseInt(tckNum);
                ticketNumbs.add(ticketNumber);
            }
            while(cursor.moveToNext());

            Collections.sort(ticketNumbs); //Ordeno los valores para devolver la lista ordenada
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTicketNumbers", "yeaId: " + String.valueOf(yeaId), sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return ticketNumbs;
    }

    /*
    * HashMap<ID, Ticket>
    * */
    public HashMap<Integer, Ticket> getTicketMap(DatabaseHelper dbHelper, int yeaId, Context context){
        HashMap<Integer, Ticket> ticketMap = null;

        try {
            ticketMap = new HashMap<>();
            Ticket tck = new Ticket();
            String query = " SELECT tck_id, tck_ticketNumber, tck_numTickRed FROM Ticket, Years WHERE tck_deleted = '0' " +
                    "and yea_id = tck_yea_id " +
                    "and yea_deleted = '0' and yea_id = '" + String.valueOf(yeaId) + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            do{
                int tckId = cursor.getInt(cursor.getColumnIndex("tck_id"));
                tck.setTck_ticketNumber(cursor.getString(cursor.getColumnIndex("tck_ticketNumber")));
                tck.setTck_id(tckId);
                tck.setTck_numTickRed(cursor.getString(cursor.getColumnIndex("tck_ticketNumber")));
                ticketMap.put(tckId, tck);
            }
            while(cursor.moveToNext());

            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTicketMap", "yeaId: " + String.valueOf(yeaId), sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return ticketMap;
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

    public ArrayList<Ticket> getAllTickets(DatabaseHelper dbHelper, Context context){
        ArrayList<Ticket> tickets = null;

        try {
            tickets = new ArrayList<>();

            String query = " SELECT tck_id, tck_deleted, tck_number, tck_ticketNumber, tck_numTickRed, " +
                    " tck_date, tck_tda_id , tck_time, tck_price, tck_pdf " +
                    "FROM Ticket WHERE tck_deleted = '0' AND tck_numTickRed like '%Ticket%' order by tck_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Ticket tck = new Ticket();
                    tck.setTck_id(cursor.getInt(cursor.getColumnIndex("tck_id")));
                    tck.setTck_deleted(cursor.getString(cursor.getColumnIndex("tck_deleted")));
                    tck.setTck_number(cursor.getString(cursor.getColumnIndex("tck_number")));
                    tck.setTck_ticketNumber(cursor.getString(cursor.getColumnIndex("tck_ticketNumber")));
                    tck.setTck_numTickRed(cursor.getString(cursor.getColumnIndex("tck_numTickRed")));
                    tck.setTck_date(cursor.getString(cursor.getColumnIndex("tck_date")));
                    tck.setTck_tda_id(cursor.getString(cursor.getColumnIndex("tck_tda_id")));
                    tck.setTck_time(cursor.getString(cursor.getColumnIndex("tck_time")));
                    tck.setTck_price(cursor.getString(cursor.getColumnIndex("tck_price")));
                    tck.setTck_pdf(cursor.getString(cursor.getColumnIndex("tck_pdf")));
                    tickets.add(tck);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getAllTickets", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return tickets;
    }

    public ArrayList<Ticket> getAllRendimientoTickets(DatabaseHelper dbHelper, Context context){
        ArrayList<Ticket> tickets = null;

        try {
            tickets = new ArrayList<>();

            String query = " SELECT tck_id, tck_deleted, tck_number, tck_ticketNumber, tck_numTickRed, " +
                    " tck_date, tck_tda_id , tck_time, tck_price, tck_pdf " +
                    "FROM Ticket WHERE tck_deleted = '0' AND tck_numTickRed like '%Rendimiento%' " +
                    " order by tck_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Ticket tck = new Ticket();
                    tck.setTck_id(cursor.getInt(cursor.getColumnIndex("tck_id")));
                    tck.setTck_deleted(cursor.getString(cursor.getColumnIndex("tck_deleted")));
                    tck.setTck_number(cursor.getString(cursor.getColumnIndex("tck_number")));
                    tck.setTck_ticketNumber(cursor.getString(cursor.getColumnIndex("tck_ticketNumber")));
                    tck.setTck_numTickRed(cursor.getString(cursor.getColumnIndex("tck_numTickRed")));
                    tck.setTck_date(cursor.getString(cursor.getColumnIndex("tck_date")));
                    tck.setTck_tda_id(cursor.getString(cursor.getColumnIndex("tck_tda_id")));
                    tck.setTck_time(cursor.getString(cursor.getColumnIndex("tck_time")));
                    tck.setTck_price(cursor.getString(cursor.getColumnIndex("tck_price")));
                    tck.setTck_pdf(cursor.getString(cursor.getColumnIndex("tck_pdf")));
                    tickets.add(tck);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getAllRendimientoTcks", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return tickets;
    }

    public void uploadPdfTicket(DatabaseHelper dbHelper, String tckId, String ruta, Context context){
        try {
            String query = "UPDATE Ticket SET tck_pdf = '" + ruta + "' WHERE tck_id = '" + Integer.parseInt(tckId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("uploadPdfTicket", "tckId: " + tckId + ", ruta: " + ruta, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public String getTicketPdfPath(DatabaseHelper dbHelper, int tckId, Context context){
        String path = null;

        try {
            String query = " SELECT tck_pdf " +
                    "FROM Ticket WHERE tck_deleted = '0' and tck_id = '" + tckId + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                path = (cursor.getString(cursor.getColumnIndex("tck_pdf")));
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getTicketPdfPath", "tckId: "+ String.valueOf(tckId), sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return path;
    }
}
