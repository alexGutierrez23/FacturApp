package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import sqlite.data.DatabaseHelper;
import sqlite.model.Contrato;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Years;


/**
 * Created by Usuario on 11/04/2017.
 */

public class ContratoDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private ContratoDML contratoDML;

    private final String TAG = ContratoDML.class.getSimpleName().toString();

    public ContratoDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Contrato.TABLE  + "("
                + Contrato.KEY_con_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Contrato.KEY_con_deleted + " TEXT DEFAULT '0' , "
                + Contrato.KEY_con_number  + " TEXT, "
                + Contrato.KEY_con_date  + " TEXT, "
                + Contrato.KEY_con_tda_id  + " TEXT, "
                + Contrato.KEY_con_yea_id + " TEXT, "
                + Contrato.KEY_con_matricula_vehiculo + " TEXT, "
                + Contrato.KEY_con_alias + " TEXT, "
                + Contrato.KEY_con_arrendador + " TEXT, "
                + Contrato.KEY_con_nif_arrendador + " TEXT, "
                + Contrato.KEY_con_isTda_arrendador + " TEXT DEFAULT '0', "
                + "FOREIGN KEY (" + Contrato.KEY_con_yea_id + ") REFERENCES " + Years.TABLE
                + " ( " + Years.KEY_yea_id + " ), "
                + "FOREIGN KEY (" + Contrato.KEY_con_tda_id + ") REFERENCES " + Taxi_driver_account.TABLE
                + " ( " + Taxi_driver_account.KEY_tda_id + " ) ) ";
    }

    public long insert(Contrato contrato, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Contrato.KEY_con_number, contrato.getCon_number());
        datos.put(Contrato.KEY_con_date, contrato.getCon_date());
        datos.put(Contrato.KEY_con_tda_id, contrato.getCon_tda_id());
        datos.put(Contrato.KEY_con_yea_id, contrato.getCon_yea_id());
        datos.put(Contrato.KEY_con_matricula_vehiculo, contrato.getCon_matricula_vehiculo());
        datos.put(Contrato.KEY_con_alias, contrato.getCon_alias());
        datos.put(Contrato.KEY_con_arrendador, contrato.getCon_arrendador().toUpperCase());
        datos.put(Contrato.KEY_con_nif_arrendador, contrato.getCon_nif_arrendador());
        if(contrato.getCon_isTda_arrendador() != null){
            datos.put(Contrato.KEY_con_isTda_arrendador, contrato.getCon_isTda_arrendador());
        }
        // Inserting Row
        long id = db.insert(Contrato.TABLE, null, datos);

        return id;
    }

    public void delete(SQLiteDatabase db) {
        db.delete(Contrato.TABLE, null,null);
    }

    public int update(Contrato contrato, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Contrato.KEY_con_id, contrato.getCon_id());
            datos.put(Contrato.KEY_con_deleted, contrato.getCon_deleted());
            datos.put(Contrato.KEY_con_number, contrato.getCon_number());
            datos.put(Contrato.KEY_con_date, contrato.getCon_date());
            datos.put(Contrato.KEY_con_tda_id, contrato.getCon_tda_id());
            datos.put(Contrato.KEY_con_yea_id, contrato.getCon_yea_id());
            datos.put(Contrato.KEY_con_matricula_vehiculo, contrato.getCon_matricula_vehiculo());
            datos.put(Contrato.KEY_con_alias, contrato.getCon_alias());
            datos.put(Contrato.KEY_con_arrendador, contrato.getCon_arrendador().toUpperCase());
            datos.put(Contrato.KEY_con_nif_arrendador, contrato.getCon_nif_arrendador());
            datos.put(Contrato.KEY_con_isTda_arrendador, contrato.getCon_isTda_arrendador());

            result = db.update(Contrato.TABLE, datos, "con_id = ?", new String[]{String.valueOf(contrato.getCon_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateContrato", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }
        return result;
    }

    public String insertarContrato(DatabaseHelper dbHelper, Contrato con , Context context){
        SQLiteDatabase db = null;
        String conId = null;

        try{
            db = dbHelper.getWritableDatabase();
            ContratoDML conDml = new ContratoDML(context);
            long id = insert(con, db);
            conId = String.valueOf(id);
        }
        catch(SQLiteException sqlte){
            Log.e("contrato", "insertando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return conId;
    }

    public ArrayList<Contrato> getContratos(DatabaseHelper dbHelper, Context context){
        ArrayList<Contrato> contratos = null;

        try {
            contratos = new ArrayList<>();

            String query = " SELECT con_arrendador, con_id, con_deleted, con_number, con_date, con_tda_id, " +
                    " con_yea_id, con_matricula_vehiculo, " +
                    " con_alias, con_nif_arrendador, con_isTda_arrendador " +
                    "FROM Contrato WHERE con_deleted = '0' order by con_alias asc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Contrato contrato = new Contrato();
                    contrato.setCon_id(cursor.getInt(cursor.getColumnIndex("con_id")));
                    contrato.setCon_deleted(cursor.getString(cursor.getColumnIndex("con_deleted")));
                    contrato.setCon_number(cursor.getString(cursor.getColumnIndex("con_number")));
                    contrato.setCon_date(cursor.getString(cursor.getColumnIndex("con_date")));
                    contrato.setCon_tda_id(cursor.getString(cursor.getColumnIndex("con_tda_id")));
                    contrato.setCon_yea_id(cursor.getString(cursor.getColumnIndex("con_yea_id")));
                    contrato.setCon_matricula_vehiculo(cursor.getString(cursor.getColumnIndex("con_matricula_vehiculo")));
                    contrato.setCon_alias(cursor.getString(cursor.getColumnIndex("con_alias")));
                    contrato.setCon_arrendador(cursor.getString(cursor.getColumnIndex("con_arrendador")));
                    contrato.setCon_nif_arrendador(cursor.getString(cursor.getColumnIndex("con_nif_arrendador")));
                    contrato.setCon_isTda_arrendador(cursor.getString(cursor.getColumnIndex("con_isTda_arrendador")));

                    contratos.add(contrato);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getContratos", "", sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return contratos;
    }

    public ArrayList<Contrato> getContratosByYear(DatabaseHelper dbHelper, String yeaId, Context context){
        ArrayList<Contrato> contratos = null;
        try {
            contratos = new ArrayList<>();
            String query = " SELECT con_id, con_deleted, con_number, con_date, con_tda_id, " +
                    " con_yea_id, con_matricula_vehiculo, " +
                    " con_alias, con_arrendador, con_nif_arrendador, con_isTda_arrendador " +
                    "FROM Contrato WHERE con_deleted = '0' and con_yea_id = '" + yeaId + "' order by con_id desc ";
            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(query, null);
            if(cursor.moveToFirst()){
                do{
                    Contrato contrato = new Contrato();
                    contrato.setCon_id(cursor.getInt(cursor.getColumnIndex("con_id")));
                    contrato.setCon_deleted(cursor.getString(cursor.getColumnIndex("con_deleted")));
                    contrato.setCon_number(cursor.getString(cursor.getColumnIndex("con_number")));
                    contrato.setCon_date(cursor.getString(cursor.getColumnIndex("con_date")));
                    contrato.setCon_tda_id(cursor.getString(cursor.getColumnIndex("con_tda_id")));
                    contrato.setCon_yea_id(cursor.getString(cursor.getColumnIndex("con_yea_id")));
                    contrato.setCon_matricula_vehiculo(cursor.getString(cursor.getColumnIndex("con_matricula_vehiculo")));
                    contrato.setCon_alias(cursor.getString(cursor.getColumnIndex("con_alias")));
                    contrato.setCon_arrendador(cursor.getString(cursor.getColumnIndex("con_arrendador")));
                    contrato.setCon_nif_arrendador(cursor.getString(cursor.getColumnIndex("con_nif_arrendador")));
                    contrato.setCon_isTda_arrendador(cursor.getString(cursor.getColumnIndex("con_isTda_arrendador")));

                    contratos.add(contrato);
                }
                while(cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException sqlte) {
            Log.e("getContratosByYear", "yeaId: " + yeaId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }

        return contratos;
    }

    public Contrato getContratoById(DatabaseHelper dbHelper, int contratoId, Context context){
        SQLiteDatabase db = null;
        Contrato contrato = null;

        try{
            db = dbHelper.getWritableDatabase();
            String query = " SELECT con_id, con_deleted, con_number, con_date, con_tda_id, " +
                    " con_yea_id, con_matricula_vehiculo, " +
                    " con_alias, con_arrendador, con_nif_arrendador, con_isTda_arrendador " +
                    "FROM Contrato WHERE con_deleted = '0' and con_id = '" + contratoId + "' ";

            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() != 0 && cursor != null) {
                if (cursor.moveToFirst()) {
                    Log.v("Ex getContratoById()", "contratoId = " + contratoId);
                    contrato = new Contrato();
                    contrato.setCon_id(cursor.getInt(cursor.getColumnIndex("con_id")));
                    contrato.setCon_deleted(cursor.getString(cursor.getColumnIndex("con_deleted")));
                    contrato.setCon_number(cursor.getString(cursor.getColumnIndex("con_number")));
                    contrato.setCon_date(cursor.getString(cursor.getColumnIndex("con_date")));
                    contrato.setCon_tda_id(cursor.getString(cursor.getColumnIndex("con_tda_id")));
                    contrato.setCon_yea_id(cursor.getString(cursor.getColumnIndex("con_yea_id")));
                    contrato.setCon_matricula_vehiculo(cursor.getString(cursor.getColumnIndex("con_matricula_vehiculo")));
                    contrato.setCon_alias(cursor.getString(cursor.getColumnIndex("con_alias")));
                    contrato.setCon_arrendador(cursor.getString(cursor.getColumnIndex("con_arrendador")));
                    contrato.setCon_nif_arrendador(cursor.getString(cursor.getColumnIndex("con_nif_arrendador")));
                    contrato.setCon_isTda_arrendador(cursor.getString(cursor.getColumnIndex("con_isTda_arrendador")));
                }
            }
            cursor.close();
        }
        catch(SQLiteException sqlte){
            Log.e("getContratoById", "contratoId: " + String.valueOf(contratoId), sqlte.getCause());
        }
        finally {
            db.close();
        }
        return contrato;
    }

    public Contrato getContratoByAlias(DatabaseHelper dbHelper, String alias, Context context){
        SQLiteDatabase db = null;
        Contrato contrato = null;

        try{
            db = dbHelper.getWritableDatabase();
            String query = " SELECT con_id, con_deleted, con_number, con_date, con_tda_id, " +
                    " con_yea_id, con_matricula_vehiculo, " +
                    " con_alias, con_arrendador, con_nif_arrendador, con_isTda_arrendador " +
                    "FROM Contrato WHERE con_deleted = '0' and con_alias = '" + alias + "' ";

            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() != 0 && cursor != null) {
                if (cursor.moveToFirst()) {
                    Log.v("Ex getContratoById()", "contratoAlias = " + alias);
                    contrato = new Contrato();
                    contrato.setCon_id(cursor.getInt(cursor.getColumnIndex("con_id")));
                    contrato.setCon_deleted(cursor.getString(cursor.getColumnIndex("con_deleted")));
                    contrato.setCon_number(cursor.getString(cursor.getColumnIndex("con_number")));
                    contrato.setCon_date(cursor.getString(cursor.getColumnIndex("con_date")));
                    contrato.setCon_tda_id(cursor.getString(cursor.getColumnIndex("con_tda_id")));
                    contrato.setCon_yea_id(cursor.getString(cursor.getColumnIndex("con_yea_id")));
                    contrato.setCon_matricula_vehiculo(cursor.getString(cursor.getColumnIndex("con_matricula_vehiculo")));
                    contrato.setCon_alias(cursor.getString(cursor.getColumnIndex("con_alias")));
                    contrato.setCon_arrendador(cursor.getString(cursor.getColumnIndex("con_arrendador")));
                    contrato.setCon_nif_arrendador(cursor.getString(cursor.getColumnIndex("con_nif_arrendador")));
                    contrato.setCon_isTda_arrendador(cursor.getString(cursor.getColumnIndex("con_isTda_arrendador")));
                }
            }
            cursor.close();
        }
        catch(SQLiteException sqlte){
            Log.e("getContratoByAlias", "alias: " + alias, sqlte.getCause());
        }
        finally {
            db.close();
        }
        return contrato;
    }


    public ArrayList<Contrato> getContratosByArrendador(DatabaseHelper dbHelper, String arrendador, Context context){
        SQLiteDatabase db = null;
        ArrayList<Contrato> contratos = new ArrayList<>();

        try{
            db = dbHelper.getWritableDatabase();
            String query = " SELECT con_id, con_deleted, con_number, con_date, con_tda_id, " +
                    " con_yea_id, con_matricula_vehiculo, " +
                    " con_alias, con_arrendador, con_nif_arrendador, con_isTda_arrendador " +
                    "FROM Contrato WHERE con_deleted = '0' and con_arrendador = '" + arrendador.toUpperCase() + "' ";

            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() != 0 && cursor != null) {
                if (cursor.moveToFirst()) {
                    do{
                        Log.v("Ex getContratoByArre()", "contratoArrendador = " + arrendador);
                        Contrato contrato = new Contrato();
                        contrato.setCon_id(cursor.getInt(cursor.getColumnIndex("con_id")));
                        contrato.setCon_deleted(cursor.getString(cursor.getColumnIndex("con_deleted")));
                        contrato.setCon_number(cursor.getString(cursor.getColumnIndex("con_number")));
                        contrato.setCon_date(cursor.getString(cursor.getColumnIndex("con_date")));
                        contrato.setCon_tda_id(cursor.getString(cursor.getColumnIndex("con_tda_id")));
                        contrato.setCon_yea_id(cursor.getString(cursor.getColumnIndex("con_yea_id")));
                        contrato.setCon_matricula_vehiculo(cursor.getString(cursor.getColumnIndex("con_matricula_vehiculo")));
                        contrato.setCon_alias(cursor.getString(cursor.getColumnIndex("con_alias")));
                        contrato.setCon_arrendador(cursor.getString(cursor.getColumnIndex("con_arrendador")));
                        contrato.setCon_nif_arrendador(cursor.getString(cursor.getColumnIndex("con_nif_arrendador")));
                        contrato.setCon_isTda_arrendador(cursor.getString(cursor.getColumnIndex("con_isTda_arrendador")));
                        contratos.add(contrato);
                    }
                    while(cursor.moveToNext());
                }
            }
            cursor.close();
        }
        catch(SQLiteException sqlte){
            Log.e("getContratoByArrdd", "arrendador: " + arrendador, sqlte.getCause());
        }
        finally {
            db.close();
        }
        return contratos;
    }

    public void borrarContrato(DatabaseHelper dbHelper, String conId, Context context){
        try {
            String query = "UPDATE Contrato SET con_deleted = '1' WHERE con_id = '" + Integer.parseInt(conId) + "'";
            dbHelper.getWritableDatabase().execSQL(query);
        } catch (SQLiteException sqlte) {
            Log.e("borrarContrato", "conId: " + conId, sqlte.getCause());
        } finally {
            dbHelper.getWritableDatabase().close();
        }
    }

    public int getContratosCount(DatabaseHelper dbHelper, Context context){
        int result = -1;
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            String query = " SELECT count(*) FROM Contrato WHERE con_deleted = '0' ";

            Cursor countRows = db.rawQuery(query, null);
            if(countRows.moveToFirst()){
                result = countRows.getInt(countRows.getColumnIndex("count(*)"));
            }
            countRows.close();
        } catch (SQLiteException sqlte) {
            Log.e("getContratosCount", "", sqlte.getCause());
        } finally {
            db.close();
        }

        return result;
    }

}
