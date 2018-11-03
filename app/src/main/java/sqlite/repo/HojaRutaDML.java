package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import sqlite.data.DatabaseHelper;
import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;
import sqlite.model.Trip;


/**
 * Created by Usuario on 11/04/2017.
 */

public class HojaRutaDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private HojaRutaDML hjrDML;

    private final String TAG = HojaRutaDML.class.getSimpleName().toString();

    public HojaRutaDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + HojaRuta.TABLE  + "("
                + HojaRuta.KEY_hjr_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + HojaRuta.KEY_hjr_deleted + " TEXT DEFAULT '0' , "
                + HojaRuta.KEY_hjr_number  + " TEXT, "
                + HojaRuta.KEY_hjr_date_inicio + " TEXT, "
                + HojaRuta.KEY_hjr_date_fin + " TEXT, "
                + HojaRuta.KEY_hjr_time_inicio  + " TEXT, "
                + HojaRuta.KEY_hjr_time_fin  + " TEXT , "
                + HojaRuta.KEY_hjr_trp_id  + " TEXT, "
                + HojaRuta.KEY_hjr_pasajeros  + " TEXT , "
                + HojaRuta.KEY_hjr_observaciones  + " TEXT , "
                + HojaRuta.KEY_hjr_pathPdf + " TEXT, "
                + HojaRuta.KEY_hjr_con_id + " TEXT , "
                + HojaRuta.KEY_hjr_ard_id + " TEXT , "
                + HojaRuta.KEY_hjr_alias + " TEXT , "
                + "FOREIGN KEY (" + HojaRuta.KEY_hjr_trp_id + ") REFERENCES " + Trip.TABLE
                + "(" + Trip.KEY_trp_id + " ), "
                + "FOREIGN KEY (" + HojaRuta.KEY_hjr_ard_id + ") REFERENCES " + Arrendatario.TABLE
                + "(" + Arrendatario.KEY_ard_id + " ), "
                + "FOREIGN KEY (" + HojaRuta.KEY_hjr_con_id + ") REFERENCES " + Contrato.TABLE
                + " ( " + Contrato.KEY_con_id + " ) ) ";
    }

    public long insert(HojaRuta hojaRuta, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(hojaRuta.KEY_hjr_number, hojaRuta.getHjr_number());
        datos.put(hojaRuta.KEY_hjr_date_inicio, hojaRuta.getHjr_date_inicio());
        datos.put(hojaRuta.KEY_hjr_date_fin, hojaRuta.getHjr_date_fin());
        datos.put(hojaRuta.KEY_hjr_time_inicio, hojaRuta.getHjr_time_inicio());
        datos.put(hojaRuta.KEY_hjr_time_fin, hojaRuta.getHjr_time_fin());
        datos.put(hojaRuta.KEY_hjr_trp_id, hojaRuta.getHjr_trp_id());
        datos.put(hojaRuta.KEY_hjr_pasajeros, hojaRuta.getHjr_pasajeros());
        datos.put(hojaRuta.KEY_hjr_observaciones, hojaRuta.getHjr_observaciones());
        datos.put(hojaRuta.KEY_hjr_con_id, hojaRuta.getHjr_con_id());
        datos.put(hojaRuta.KEY_hjr_pathPdf, hojaRuta.getHjr_pathPdf());
        datos.put(hojaRuta.KEY_hjr_alias, hojaRuta.getHjr_alias());
        datos.put(hojaRuta.KEY_hjr_ard_id, hojaRuta.getHjr_ard_id());

        long id = db.insert(HojaRuta.TABLE, null, datos);
        return id;
    }

    public void delete(SQLiteDatabase db) {
        db.delete(HojaRuta.TABLE, null,null);
    }

    public int update(HojaRuta hojaRuta, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(hojaRuta.KEY_hjr_id, hojaRuta.getHjr_id());
            datos.put(hojaRuta.KEY_hjr_deleted , hojaRuta.getHjr_deleted());
            datos.put(hojaRuta.KEY_hjr_number, hojaRuta.getHjr_number());
            datos.put(hojaRuta.KEY_hjr_date_inicio, hojaRuta.getHjr_date_inicio());
            datos.put(hojaRuta.KEY_hjr_date_fin, hojaRuta.getHjr_date_fin());
            datos.put(hojaRuta.KEY_hjr_time_inicio, hojaRuta.getHjr_time_inicio());
            datos.put(hojaRuta.KEY_hjr_time_fin, hojaRuta.getHjr_time_fin());
            datos.put(hojaRuta.KEY_hjr_trp_id, hojaRuta.getHjr_trp_id());
            datos.put(hojaRuta.KEY_hjr_pasajeros, hojaRuta.getHjr_pasajeros());
            datos.put(hojaRuta.KEY_hjr_observaciones, hojaRuta.getHjr_observaciones());
            datos.put(hojaRuta.KEY_hjr_con_id, hojaRuta.getHjr_con_id());
            datos.put(hojaRuta.KEY_hjr_pathPdf, hojaRuta.getHjr_pathPdf());
            datos.put(hojaRuta.KEY_hjr_alias, hojaRuta.getHjr_alias());
            datos.put(hojaRuta.KEY_hjr_ard_id, hojaRuta.getHjr_ard_id());

            result = db.update(HojaRuta.TABLE, datos, "hjr_id = ?", new String[]{String.valueOf(hojaRuta.getHjr_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateHojaRuta", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarHojaRuta(DatabaseHelper dbHelper, HojaRuta hjr, Context context){
        SQLiteDatabase db = null;
        String hjrId = null;
        try{
            db = dbHelper.getWritableDatabase();
            long id = insert(hjr, db);
            hjrId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("hoja ruta", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return hjrId;
    }

    public ArrayList<HojaRuta> getHojasRuta(DatabaseHelper dbHelper, String contratoId, Context context){
        ArrayList<HojaRuta> hojasRuta = null;

        try{
            hojasRuta = new ArrayList<>();
            // Revisar esta query! no devuelve nada
            String query = " SELECT hjr_id, hjr_deleted, hjr_number, hjr_date_inicio, hjr_date_fin, " +
                    " hjr_time_inicio, hjr_time_fin, hjr_pasajeros, hjr_observaciones, hjr_pathPdf, " +
                    "hjr_con_id, hjr_trp_id, hjr_alias, hjr_ard_id " +
                    "FROM HojaRuta, Arrendatario " +
                    " WHERE hjr_deleted = '0' and hjr_ard_id = ard_id and ard_deleted = '0' " +
                    " and hjr_con_id = '" + contratoId + "' order by hjr_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    HojaRuta hr = new HojaRuta();
                    hr.setHjr_id(cursor.getInt(cursor.getColumnIndex("hjr_id")));
                    hr.setHjr_deleted(cursor.getString(cursor.getColumnIndex("hjr_deleted")));
                    hr.setHjr_number(cursor.getString(cursor.getColumnIndex("hjr_number")));
                    hr.setHjr_date_inicio(cursor.getString(cursor.getColumnIndex("hjr_date_inicio")));
                    hr.setHjr_date_fin(cursor.getString(cursor.getColumnIndex("hjr_date_fin")));
                    hr.setHjr_time_inicio(cursor.getString(cursor.getColumnIndex("hjr_time_inicio")));
                    hr.setHjr_time_fin(cursor.getString(cursor.getColumnIndex("hjr_time_fin")));
                    hr.setHjr_pasajeros(cursor.getString(cursor.getColumnIndex("hjr_pasajeros")));
                    hr.setHjr_observaciones(cursor.getString(cursor.getColumnIndex("hjr_observaciones")));
                    hr.setHjr_pathPdf(cursor.getString(cursor.getColumnIndex("hjr_pathPdf")));
                    hr.setHjr_con_id(cursor.getString(cursor.getColumnIndex("hjr_con_id")));
                    hr.setHjr_trp_id(cursor.getString(cursor.getColumnIndex("hjr_trp_id")));
                    hr.setHjr_alias(cursor.getString(cursor.getColumnIndex("hjr_alias")));
                    hr.setHjr_ard_id(cursor.getString(cursor.getColumnIndex("hjr_ard_id")));

                    hojasRuta.add(hr);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(SQLiteException sqlte){
            Log.e("getHojasRuta", "contratoId: " + contratoId, sqlte.getCause());
        }
        finally{
            dbHelper.getWritableDatabase().close();
        }

        return hojasRuta;
    }

    public HojaRuta getHojaRutaById(DatabaseHelper dbHelper, int hojaRutaId, Context context){
        SQLiteDatabase db = null;
        HojaRuta hr = null;

        try{
            db = dbHelper.getWritableDatabase();
            String query = " SELECT hjr_id, hjr_deleted, hjr_number, hjr_date_inicio, hjr_date_fin, " +
                    " hjr_time_inicio, hjr_time_fin, hjr_pasajeros, hjr_observaciones, hjr_pathPdf, " +
                    "hjr_con_id, hjr_trp_id, hjr_alias, hjr_ard_id " +
                    "FROM HojaRuta WHERE hjr_deleted = '0' " +
                    " and hjr_id = '" + hojaRutaId + "' ";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() != 0 && cursor != null) {
                if (cursor.moveToFirst()) {
                    Log.v("Ex getHojaRutaById()", "hojaRutaId = " + hojaRutaId);
                    hr = new HojaRuta();
                    hr.setHjr_id(cursor.getInt(cursor.getColumnIndex("hjr_id")));
                    hr.setHjr_deleted(cursor.getString(cursor.getColumnIndex("hjr_deleted")));
                    hr.setHjr_number(cursor.getString(cursor.getColumnIndex("hjr_number")));
                    hr.setHjr_date_inicio(cursor.getString(cursor.getColumnIndex("hjr_date_inicio")));
                    hr.setHjr_date_fin(cursor.getString(cursor.getColumnIndex("hjr_date_fin")));
                    hr.setHjr_time_inicio(cursor.getString(cursor.getColumnIndex("hjr_time_inicio")));
                    hr.setHjr_time_fin(cursor.getString(cursor.getColumnIndex("hjr_time_fin")));
                    hr.setHjr_pasajeros(cursor.getString(cursor.getColumnIndex("hjr_pasajeros")));
                    hr.setHjr_observaciones(cursor.getString(cursor.getColumnIndex("hjr_observaciones")));
                    hr.setHjr_pathPdf(cursor.getString(cursor.getColumnIndex("hjr_pathPdf")));
                    hr.setHjr_con_id(cursor.getString(cursor.getColumnIndex("hjr_con_id")));
                    hr.setHjr_trp_id(cursor.getString(cursor.getColumnIndex("hjr_trp_id")));
                    hr.setHjr_alias(cursor.getString(cursor.getColumnIndex("hjr_alias")));
                    hr.setHjr_ard_id(cursor.getString(cursor.getColumnIndex("hjr_ard_id")));
                }
            }
            cursor.close();
        }
        catch(SQLiteException sqlte){
            Log.e("GetHojaRutaById()", "hojaRutaId: " + String.valueOf(hojaRutaId), sqlte.getCause());
        }
        finally {
            db.close();
        }
        return hr;
    }

    public void uploadPdfHojaRuta(DatabaseHelper dbHelper, String hojaRutaId, String ruta, Context context){
        try{
            String query = "UPDATE HojaRuta SET hjr_pathPdf = '" + ruta + "' WHERE hjr_id = '" + Integer.parseInt(hojaRutaId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        }
        catch (SQLiteException sqlte){
            Log.e("uploadPdfHojaRuta", "hojaRutaId: " + hojaRutaId + ", ruta: " + ruta, sqlte.getCause());
        }
        finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public String getHojaRutaPdfPath(DatabaseHelper dbHelper, int hojaRutaId, Context context){
        String path = null;

        try{
            String query = " SELECT hjr_pathPdf " +
                    "FROM HojaRuta WHERE hjr_deleted = '0' and hjr_id = '" + hojaRutaId + "' ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                path = (cursor.getString(cursor.getColumnIndex("hjr_pathPdf")));
            }
            cursor.close();
        }
        catch (SQLiteException sqlte){
            Log.e("getHojaRutaPdfPath", "hrId: " + String.valueOf(hojaRutaId), sqlte.getCause());
        }
        finally {
            dbHelper.getWritableDatabase().close();
        }

        return path;
    }

    public void borrarHojaRuta(DatabaseHelper dbHelper, String hjrId, Context context){
        try{
            String query = "UPDATE HojaRuta SET hjr_deleted = '1' WHERE hjr_id = '" + Integer.parseInt(hjrId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        }
        catch (SQLiteException sqlte){
            Log.e("borrarHojaRuta", "hjrId: " + hjrId, sqlte.getCause());
        }
        finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public int getHojasRutaCount(DatabaseHelper dbHelper, Context context){
        int result = -1;
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT count(*) FROM HojaRuta where hjr_deleted = '0' ";

            Cursor countRows = db.rawQuery(query, null);
            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getHojasRutaCount", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

    public ArrayList<HojaRuta> getHojasRutaByArrendatarioId(DatabaseHelper dbHelper, String arrendatarioId, Context context){
        ArrayList<HojaRuta> hojasRuta = null;

        try{
            hojasRuta = new ArrayList<>();

            String query = " SELECT hjr_id, hjr_deleted, hjr_number, hjr_date_inicio, hjr_date_fin, " +
                    " hjr_time_inicio, hjr_time_fin, hjr_pasajeros, hjr_observaciones, hjr_con_id, " +
                    " hjr_trp_id, hjr_alias, hjr_ard_id FROM HojaRuta, Arrendatario " +
                    " WHERE hjr_deleted = '0' and hjr_ard_id = ard_id and ard_deleted = '0' " +
                    " and hjr_ard_id = '" + arrendatarioId + "' order by hjr_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    HojaRuta hr = new HojaRuta();
                    hr.setHjr_id(cursor.getInt(cursor.getColumnIndex("hjr_id")));
                    hr.setHjr_deleted(cursor.getString(cursor.getColumnIndex("hjr_deleted")));
                    hr.setHjr_number(cursor.getString(cursor.getColumnIndex("hjr_number")));
                    hr.setHjr_date_inicio(cursor.getString(cursor.getColumnIndex("hjr_date_inicio")));
                    hr.setHjr_date_fin(cursor.getString(cursor.getColumnIndex("hjr_date_fin")));
                    hr.setHjr_time_inicio(cursor.getString(cursor.getColumnIndex("hjr_time_inicio")));
                    hr.setHjr_time_fin(cursor.getString(cursor.getColumnIndex("hjr_time_fin")));
                    hr.setHjr_pasajeros(cursor.getString(cursor.getColumnIndex("hjr_pasajeros")));
                    hr.setHjr_observaciones(cursor.getString(cursor.getColumnIndex("hjr_observaciones")));
                    hr.setHjr_con_id(cursor.getString(cursor.getColumnIndex("hjr_con_id")));
                    hr.setHjr_trp_id(cursor.getString(cursor.getColumnIndex("hjr_trp_id")));
                    hr.setHjr_alias(cursor.getString(cursor.getColumnIndex("hjr_alias")));
                    hr.setHjr_ard_id(cursor.getString(cursor.getColumnIndex("hjr_ard_id")));

                    hojasRuta.add(hr);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        }
        catch(SQLiteException sqlte){
            Log.e("getHojasRutaByArdId", "contratoId: " + arrendatarioId, sqlte.getCause());
        }
        finally{
            dbHelper.getWritableDatabase().close();
        }

        return hojasRuta;
    }

    public int getCountByDate(DatabaseHelper dbHelper, String date){
        int hojaRutaCount = 0;
        SQLiteDatabase db = null;
        HashMap<String, String> mapDates = getMonthYearFromDate(date);

        try{
            if(mapDates != null && !mapDates.isEmpty()){
                db = dbHelper.getWritableDatabase();
                String newDate = mapDates.get("month") + " / " + mapDates.get("year");

                String query = " SELECT count(*) FROM HojaRuta WHERE hjr_date_inicio like '" + date + "' AND hjr_deleted = '0' ";

                Cursor countRows = db.rawQuery(query, null);
                if(countRows.moveToFirst()){
                    hojaRutaCount = countRows.getInt(countRows.getColumnIndex("count(*)"));
                }
                countRows.close();
            }
        }
        catch(SQLiteException sqlte){
            Log.e("getCountByDate", "date: " + date, sqlte.getCause());
        }
        finally{
            dbHelper.getWritableDatabase().close();
        }
        if(hojaRutaCount <= 0){
            return 0;
        }
        else{
            return hojaRutaCount;
        }
    }

    private HashMap<String, String> getMonthYearFromDate(String date){
        HashMap<String, String> map = new HashMap<>();

        String delimitador = " / ";
        String[] dd = date.split(delimitador);
        for(int i = 0; i < dd.length; i++){
            if(i == 1){
                map.put("month", dd[i]);
            }
            else if(i == 2){
                map.put("year", dd[i]);
            }
        }

        return map;
    }

}
