package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import sqlite.data.DatabaseHelper;
import sqlite.model.Information;
import sqlite.model.Invoice;
import sqlite.model.Presupuesto;
import sqlite.model.Trip;


/**
 * Created by Usuario on 11/04/2017.
 */

public class InformationDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private InformationDML informationDML;

    private final String TAG = InformationDML.class.getSimpleName().toString();

    public InformationDML(Context context) {
        super(context);
    }

    public static String createTable() {
        return "CREATE TABLE " + Information.TABLE + "("
                + Information.KEY_inf_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Information.KEY_inf_deleted + " TEXT DEFAULT '0',"
                + Information.KEY_inf_additionalInfo + " TEXT, "
                + Information.KEY_inf_no_vat_price + " TEXT, "
                + Information.KEY_inf_vat_price + " TEXT, "
                + Information.KEY_inf_vat + " TEXT, "
                + Information.KEY_inf_item + " TEXT, "
                + Information.KEY_inf_quantity + " TEXT, "
                + Information.KEY_inf_trp_id + " TEXT, "
                + Information.KEY_inf_inv_id + " TEXT, "
                + Information.KEY_inf_pre_id + " TEXT, "
                + "FOREIGN KEY (" + Information.KEY_inf_trp_id + ") REFERENCES " + Trip.TABLE
                + " ( " + Trip.KEY_trp_id + " ), "
                + "FOREIGN KEY (" + Information.KEY_inf_inv_id + ") REFERENCES " + Invoice.TABLE
                + " ( " + Invoice.KEY_inv_id + " ), "
                + "FOREIGN KEY (" + Information.KEY_inf_pre_id + ") REFERENCES " + Presupuesto.TABLE
                + " ( " + Presupuesto.KEY_pre_id + " ) ) ";
    }

    public long insert(Information info, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Information.KEY_inf_additionalInfo, info.getInf_additionalInfo());
        datos.put(Information.KEY_inf_inv_id, info.getInf_inv_id());
        datos.put(Information.KEY_inf_pre_id, info.getInf_pre_id());
        datos.put(Information.KEY_inf_vat_price, info.getInf_vat_price());
        datos.put(Information.KEY_inf_no_vat_price, info.getInf_no_vat_price());
        datos.put(Information.KEY_inf_item, info.getInf_item());
        datos.put(Information.KEY_inf_quantity, info.getInf_quantity());
        datos.put(Information.KEY_inf_trp_id, info.getInf_trp_id());
        datos.put(Information.KEY_inf_vat, info.getInf_vat());
        long id = db.insert(Information.TABLE, null, datos);

        return id;
    }

    public int update(Information info, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Information.KEY_inf_id, info.getInf_id());
            datos.put(Information.KEY_inf_deleted, info.getInf_deleted());
            datos.put(Information.KEY_inf_additionalInfo, info.getInf_additionalInfo());
            datos.put(Information.KEY_inf_inv_id, info.getInf_inv_id());
            datos.put(Information.KEY_inf_pre_id, info.getInf_pre_id());
            datos.put(Information.KEY_inf_vat_price, info.getInf_vat_price());
            datos.put(Information.KEY_inf_no_vat_price, info.getInf_no_vat_price());
            datos.put(Information.KEY_inf_item, info.getInf_item());
            datos.put(Information.KEY_inf_quantity, info.getInf_quantity());
            datos.put(Information.KEY_inf_trp_id, info.getInf_trp_id());
            datos.put(Information.KEY_inf_vat, info.getInf_vat());

            result = db.update(Information.TABLE, datos, "inf_id = ?", new String[]{String.valueOf(info.getInf_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateInformation", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public void delete(SQLiteDatabase db) {
        try{
            db.delete(Information.TABLE, null,null);
        }
        catch (SQLiteException sqlte){
            Log.e("delete Information", "", sqlte.getCause());
        }
        finally {
           db.close();
        }
    }

    public String insertarInfo(DatabaseHelper dbHelper, Information inf , Context context) {
        SQLiteDatabase db = null;
        String infoId = null;
        try {
            db = dbHelper.getWritableDatabase();
            long id = insert(inf, db);
            infoId = String.valueOf(id);
        } catch (SQLiteException sqlte) {
            Log.e("insertarInfo", "", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return infoId;
    }

    public ArrayList<Integer> getInformationByInvoiceId(SQLiteDatabase db, String invoiceId, Context context){
        ArrayList<Integer> infoIds = null;

        try{
            infoIds = new ArrayList<>();

            String query = "SELECT inf_id from Information where inf_inv_id = '" + invoiceId + "' and inf_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);
            if(fila.moveToFirst()){
                while(!fila.isAfterLast()){
                    Log.v("Ejecutando query = 1...", "");
                    infoIds.add(fila.getInt(fila.getColumnIndex("inf_id")));
                    fila.moveToNext();
                }
            }
            Collections.sort(infoIds);
            fila.close();
        }
        catch (SQLiteException sqlte){
            Log.e("getInfoByInvId", "invId: "+ invoiceId, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return infoIds;
    }

    public ArrayList<Information> getFullInformationByInvoiceId(SQLiteDatabase db, String invoiceId, Context context){
        Information info = new Information();
        ArrayList<Information> infos = new ArrayList<>();

        try{
            info = new Information();
            infos = new ArrayList<>();

            String query = "SELECT inf_id, inf_additionalInfo, inf_no_vat_price, inf_vat_price, " +
                    "inf_vat, inf_item, inf_quantity, inf_trp_id " +
                    "from Information " +
                    "where inf_inv_id = '" + invoiceId + "' " +
                    " and inf_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);

            if(fila.getCount() > 1 ){
                do{
                    info.setInf_id(fila.getInt(fila.getColumnIndex("inf_id")));
                    info.setInf_vat(fila.getString(fila.getColumnIndex("inf_vat")));
                    info.setInf_additionalInfo(fila.getString(fila.getColumnIndex("inf_additionalInfo")));
                    info.setInf_no_vat_price(fila.getString(fila.getColumnIndex("inf_no_vat_price")));
                    info.setInf_vat_price(fila.getString(fila.getColumnIndex("inf_vat_price")));
                    info.setInf_item(fila.getString(fila.getColumnIndex("inf_item")));
                    info.setInf_quantity(fila.getString(fila.getColumnIndex("inf_quantity")));
                    info.setInf_trp_id(fila.getString(fila.getColumnIndex("inf_trp_id")));
                    infos.add(info);
                }
                while(fila.moveToNext());
            }
            else{
                if(fila.getCount() == 1 && fila.moveToNext()){
                    info.setInf_id(fila.getInt(fila.getColumnIndex("inf_id")));
                    info.setInf_vat(fila.getString(fila.getColumnIndex("inf_vat")));
                    info.setInf_additionalInfo(fila.getString(fila.getColumnIndex("inf_additionalInfo")));
                    info.setInf_no_vat_price(fila.getString(fila.getColumnIndex("inf_no_vat_price")));
                    info.setInf_vat_price(fila.getString(fila.getColumnIndex("inf_vat_price")));
                    info.setInf_item(fila.getString(fila.getColumnIndex("inf_item")));
                    info.setInf_quantity(fila.getString(fila.getColumnIndex("inf_quantity")));
                    info.setInf_trp_id(fila.getString(fila.getColumnIndex("inf_trp_id")));
                    infos.add(info);
                }
            }
            fila.close();
        }
        catch (SQLiteException sqlte){
            Log.e("getFullInfoByInvId", "invoiceId: " + invoiceId, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return infos;
    }

    public ArrayList<Integer> getInformationByPresupuestoId(SQLiteDatabase db, String presupuestoId, Context context){
        ArrayList<Integer> infoIds = null;

        try{
            infoIds = new ArrayList<>();

            String query = "SELECT inf_id from Information where inf_pre_id = '" + presupuestoId + "' and inf_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);

            do{
                Log.v("Ejecutando query = 1...", "");
                infoIds.add(fila.getInt(fila.getColumnIndex("inf_id")));
            }
            while(fila.moveToNext());
            Collections.sort(infoIds);
            fila.close();
        }
        catch (SQLiteException sqlte){
            Log.e("getInfoByPresupuestoId", "presupuestoId: " + presupuestoId, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return infoIds;
    }

    public ArrayList<Information> getFullInformationByPresupuestoId(SQLiteDatabase db,
                                                                           String presupuestoId, Context context){
        Information info = null;
        ArrayList<Information> infos = null;

        try {
            infos = new ArrayList<>();

            String query = "SELECT inf_id, inf_additionalInfo, inf_no_vat_price, inf_vat_price, " +
                    "inf_vat, inf_item, inf_quantity, inf_trp_id " +
                    "from Information " +
                    "where inf_pre_id = '" + presupuestoId + "' " +
                    " and inf_deleted='0'";  //Get last element inserted
            Cursor fila = db.rawQuery(query, null);

            if(fila.getCount() > 1 ){

                do{
                    info = new Information();
                    info.setInf_id(fila.getInt(fila.getColumnIndex("inf_id")));
                    info.setInf_vat(fila.getString(fila.getColumnIndex("inf_vat")));
                    info.setInf_additionalInfo(fila.getString(fila.getColumnIndex("inf_additionalInfo")));
                    info.setInf_no_vat_price(fila.getString(fila.getColumnIndex("inf_no_vat_price")));
                    info.setInf_vat_price(fila.getString(fila.getColumnIndex("inf_vat_price")));
                    info.setInf_item(fila.getString(fila.getColumnIndex("inf_item")));
                    info.setInf_quantity(fila.getString(fila.getColumnIndex("inf_quantity")));
                    info.setInf_trp_id(fila.getString(fila.getColumnIndex("inf_trp_id")));
                    infos.add(info);
                }
                while(fila.moveToNext());
            }
            else{
                if(fila.getCount() == 1 && fila.moveToNext()){

                    info.setInf_id(fila.getInt(fila.getColumnIndex("inf_id")));
                    info.setInf_vat(fila.getString(fila.getColumnIndex("inf_vat")));
                    info.setInf_additionalInfo(fila.getString(fila.getColumnIndex("inf_additionalInfo")));
                    info.setInf_no_vat_price(fila.getString(fila.getColumnIndex("inf_no_vat_price")));
                    info.setInf_vat_price(fila.getString(fila.getColumnIndex("inf_vat_price")));
                    info.setInf_item(fila.getString(fila.getColumnIndex("inf_item")));
                    info.setInf_quantity(fila.getString(fila.getColumnIndex("inf_quantity")));
                    info.setInf_trp_id(fila.getString(fila.getColumnIndex("inf_trp_id")));
                    infos.add(info);
                }
            }
            fila.close();
        } catch (SQLiteException sqlte) {
            Log.e("getFullInfoByPreId", "preId: " + presupuestoId, sqlte.getCause());
        } finally {
            db.close();
        }

        return infos;
    }

    public Information getInfo(DatabaseHelper dbHelper, String infoId){
        Information info = null;
        SQLiteDatabase db = null;

        try{
            info = new Information();
            db = dbHelper.getWritableDatabase();
            String query = " SELECT inf_additionalInfo, inf_no_vat_price, inf_vat_price, " +
                    "inf_vat, inf_item, inf_quantity, inf_trp_id, inf_inv_id, inf_pre_id, inf_deleted from Information " +
                    "where inf_id = '" + Integer.parseInt(infoId) + "' " +
                    " and inf_deleted='0' ";
            Cursor fila = db.rawQuery(query, null);
            if (fila.moveToFirst()) {
                info.setInf_id(Integer.parseInt(infoId));
                info.setInf_additionalInfo(fila.getString(0));
                info.setInf_no_vat_price(fila.getString(1));
                info.setInf_vat_price(fila.getString(2));
                info.setInf_vat(fila.getString(3));
                info.setInf_item(fila.getString(4));
                info.setInf_quantity(fila.getString(5));
                info.setInf_trp_id(fila.getString(6));
                info.setInf_inv_id(fila.getString(7));
                info.setInf_pre_id(fila.getString(8));
                info.setInf_deleted(fila.getString(9));

                return info;
            }
            fila.close();
        }
        catch (SQLiteException sqle){
            Log.e("GetInfo", "infoId: " + infoId, sqle.getCause());
        }
        finally {
            db.close();
        }
        return info;
    }


}
