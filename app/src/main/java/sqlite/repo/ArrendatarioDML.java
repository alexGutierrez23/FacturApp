package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import sqlite.data.DatabaseHelper;
import sqlite.model.Arrendatario;


/**
 * Created by Usuario on 11/04/2017.
 */

public class ArrendatarioDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private ArrendatarioDML arrendatarioDML;

    private final String TAG = ArrendatarioDML.class.getSimpleName().toString();

    public ArrendatarioDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Arrendatario.TABLE  + "("
                + Arrendatario.KEY_ard_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Arrendatario.KEY_ard_deleted + " TEXT DEFAULT '0' , "
                + Arrendatario.KEY_ard_name  + " TEXT, "
                + Arrendatario.KEY_ard_nifCif  + " TEXT, "
                + Arrendatario.KEY_ard_fecha + " TEXT, "
                + Arrendatario.KEY_ard_number + " TEXT ) ";
    }

    public long insert(Arrendatario arrendatario, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Arrendatario.KEY_ard_name, arrendatario.getArd_name());
        datos.put(Arrendatario.KEY_ard_nifCif, arrendatario.getArd_nifCif());
        datos.put(Arrendatario.KEY_ard_fecha, arrendatario.getArd_fecha());
        datos.put(Arrendatario.KEY_ard_number, arrendatario.getArd_number());
        // Inserting Row
        long id = db.insert(Arrendatario.TABLE, null, datos);

        return id;
    }

    public void delete(SQLiteDatabase db) {
        db.delete(Arrendatario.TABLE, null,null);
    }

    public int update(Arrendatario arrendatario, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Arrendatario.KEY_ard_id, arrendatario.getArd_id());
            datos.put(Arrendatario.KEY_ard_deleted, arrendatario.getArd_deleted());
            datos.put(Arrendatario.KEY_ard_name, arrendatario.getArd_name());
            datos.put(Arrendatario.KEY_ard_nifCif, arrendatario.getArd_nifCif());
            datos.put(Arrendatario.KEY_ard_fecha, arrendatario.getArd_fecha());
            datos.put(Arrendatario.KEY_ard_number, arrendatario.getArd_number());

            result = db.update(Arrendatario.TABLE, datos, "ard_id = ?", new String[]{String.valueOf(arrendatario.getArd_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateArrendatario", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public String insertarArrendatario(DatabaseHelper dbHelper, Arrendatario ard , Context context){
        SQLiteDatabase db = null;
        String ardId = null;

        try{
            db = dbHelper.getWritableDatabase();
            ArrendatarioDML ardDml = new ArrendatarioDML(context);
            long id = insert(ard, db);
            ardId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("arrendatario", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return ardId;
    }

    public ArrayList<Arrendatario> getArrendatarios(DatabaseHelper dbHelper, Context context){
        ArrayList<Arrendatario> arrendatarios = null;

        try {
            arrendatarios = new ArrayList<>();

            String query = " SELECT ard_id, ard_deleted, ard_name, ard_nifCif, ard_fecha, ard_number " +
                    "FROM Arrendatario WHERE ard_deleted = '0' order by ard_name asc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Arrendatario arrendatario = new Arrendatario();
                    arrendatario.setArd_id(cursor.getInt(cursor.getColumnIndex("ard_id")));
                    arrendatario.setArd_deleted(cursor.getString(cursor.getColumnIndex("ard_deleted")));
                    arrendatario.setArd_name(cursor.getString(cursor.getColumnIndex("ard_name")));
                    arrendatario.setArd_nifCif(cursor.getString(cursor.getColumnIndex("ard_nifCif")));
                    arrendatario.setArd_fecha(cursor.getString(cursor.getColumnIndex("ard_fecha")));
                    arrendatario.setArd_number(cursor.getString(cursor.getColumnIndex("ard_number")));

                    arrendatarios.add(arrendatario);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getArrendatarios", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return arrendatarios;
    }

    public void borrarArrendatario(DatabaseHelper dbHelper, String ardId, Context context){
        try {
            String query = "UPDATE Arrendatario SET ard_deleted = '1' WHERE ard_id = '" + Integer.parseInt(ardId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("borrarArrendatario", "ardId: " + ardId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public Arrendatario getArrendatario(DatabaseHelper dbHelper, int arrendatarioId, Context context){
        Arrendatario arrendatario = null;

        try {
            arrendatario = new Arrendatario();

            String query = " SELECT ard_id, ard_deleted, ard_name, ard_nifCif, ard_fecha, ard_number " +
                    "FROM Arrendatario WHERE ard_deleted = '0' and ard_id = '" + arrendatarioId + "' ";

            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.getCount() != 0 && cursor != null) {
                if (cursor.moveToFirst()) {
                    arrendatario.setArd_id(cursor.getInt(cursor.getColumnIndex("ard_id")));
                    arrendatario.setArd_deleted(cursor.getString(cursor.getColumnIndex("ard_deleted")));
                    arrendatario.setArd_name(cursor.getString(cursor.getColumnIndex("ard_name")));
                    arrendatario.setArd_nifCif(cursor.getString(cursor.getColumnIndex("ard_nifCif")));
                    arrendatario.setArd_fecha(cursor.getString(cursor.getColumnIndex("ard_fecha")));
                    arrendatario.setArd_number(cursor.getString(cursor.getColumnIndex("ard_number")));
                }
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getArrendatarioById", "id: " + String.valueOf(arrendatarioId), sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return arrendatario;
    }

    public Arrendatario getArrendatarioByName(DatabaseHelper dbHelper, String ardName, Context context){
        Arrendatario arrendatario = null;

        try {
            arrendatario = new Arrendatario();

            String query = " SELECT ard_id, ard_deleted, ard_name, ard_nifCif, ard_fecha, ard_number " +
                    "FROM Arrendatario WHERE ard_deleted = '0' and ard_name = '" + ardName + "' ";

            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.getCount() != 0 && cursor != null) {
                if (cursor.moveToFirst()) {
                    arrendatario.setArd_id(cursor.getInt(cursor.getColumnIndex("ard_id")));
                    arrendatario.setArd_deleted(cursor.getString(cursor.getColumnIndex("ard_deleted")));
                    arrendatario.setArd_name(cursor.getString(cursor.getColumnIndex("ard_name")));
                    arrendatario.setArd_nifCif(cursor.getString(cursor.getColumnIndex("ard_nifCif")));
                    arrendatario.setArd_fecha(cursor.getString(cursor.getColumnIndex("ard_fecha")));
                    arrendatario.setArd_number(cursor.getString(cursor.getColumnIndex("ard_number")));
                }
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getArrendatarioByName", "name: " + ardName, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return arrendatario;
    }
}
