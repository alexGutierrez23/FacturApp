package sqlite.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Customer;


/**
 * Created by Usuario on 11/04/2017.
 */

public class CustomerDML extends DatabaseHelper{
    //Class for adding all the functionality in the DB
    private CustomerDML customerDML;

    private final String TAG = CustomerDML.class.getSimpleName().toString();

    public CustomerDML(Context context) {
        super(context);
    }

    public static String createTable(){
        return "CREATE TABLE " + Customer.TABLE  + "("
                + Customer.KEY_cus_id  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Customer.KEY_cus_deleted + " TEXT DEFAULT '0', "
                + Customer.KEY_cus_email  + " TEXT, "
                + Customer.KEY_cus_name  + " TEXT, "
                + Customer.KEY_cus_surname  + " TEXT, "
                + Customer.KEY_cus_nif  + " TEXT, "
                + Customer.KEY_cus_adr_id  + " TEXT , "
                + "FOREIGN KEY (" + Customer.KEY_cus_adr_id + ") REFERENCES " + Address.TABLE
                + " ( " + Address.KEY_adr_id + " ) ) ";  //es posible que no haga falta añadir otra vez key_adr_id
    }

    public long insert(Customer customer, SQLiteDatabase db) {
        ContentValues datos = new ContentValues();
        datos.put(Customer.KEY_cus_email, customer.getCus_email());
        datos.put(Customer.KEY_cus_name, customer.getCus_name());
        datos.put(Customer.KEY_cus_surname, customer.getCus_surname());
        datos.put(Customer.KEY_cus_nif, customer.getCus_nif());
        datos.put(Customer.KEY_cus_adr_id, customer.getCus_adr_id());

        long id = db.insert(Customer.TABLE, null, datos);

        return id;
    }

    public int update(Customer cus, SQLiteDatabase db){
        int result = -1;
        try{
            ContentValues datos = new ContentValues();
            datos.put(Customer.KEY_cus_id, cus.getCus_id());
            datos.put(Customer.KEY_cus_deleted, cus.getCus_deleted());
            datos.put(Customer.KEY_cus_email, cus.getCus_email());
            datos.put(Customer.KEY_cus_name, cus.getCus_name());
            datos.put(Customer.KEY_cus_surname, cus.getCus_surname());
            datos.put(Customer.KEY_cus_nif, cus.getCus_nif());
            datos.put(Customer.KEY_cus_adr_id, cus.getCus_adr_id());

            result = db.update(Customer.TABLE, datos, "cus_id = ?", new String[]{String.valueOf(cus.getCus_id())});
        }
        catch(SQLiteException sqlte){
            Log.e("updateCustomer", "actualizando", sqlte.getCause());
        }
        finally {
            db.close();
        }

        return result;
    }

    public void delete( SQLiteDatabase db) {
        try{
            db.delete(Customer.TABLE, null,null);
        }
        catch (SQLiteException sqlte){
            Log.e("delete", "deleting customer", sqlte.getCause());
        }
        finally {
            db.close();
        }
    }

    public String insertarCustomer(DatabaseHelper dbHelper, Customer cus, String adrId, Context context) {
        SQLiteDatabase db = null;
        String cusId = null;
        try {
            db = dbHelper.getWritableDatabase();
            cus.setCus_adr_id(adrId);
            long id = insert(cus, db);
            cusId = String.valueOf(id);
        } catch (SQLiteException sqlte) {
            Log.e("insertarCustomer", "customer and adrId", sqlte.getCause());
        } finally {
            db.close();
        }
        return cusId;
    }
/*
    public int getCustomer(SQLiteDatabase db, String invId, Context context){
        int cusId = 0;
        String query = "SELECT cus_id from Customer where inv_cus_id = '" + invId + "' and cus_deleted='0'";
        Cursor fila = db.rawQuery(query, null);
        if(fila.getCount() != 0 && fila != null) {
            if (fila.moveToFirst()) {
                Log.v("Ejecutando query = 1...", "");
                return cusId = fila.getInt(fila.getColumnIndex("cus_id"));
            }
        }
        db.close();
        return cusId;
    }
*/
    public Customer getCustomerByInvoiceId(SQLiteDatabase db, String invId, Context context){
        Customer cus = null;
        try{
            cus = new Customer();
            String query = "SELECT cus_id, cus_email, cus_name, cus_surname, cus_nif from Customer, Invoice " +
                    "where inv_id = '" + Integer.parseInt(invId) + "' and inv_cus_id = cus_id " +
                    "and inv_deleted ='0' and cus_deleted='0'";
            Cursor fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    cus.setCus_id(fila.getInt(fila.getColumnIndex("cus_id")));
                    cus.setCus_email(fila.getString(fila.getColumnIndex("cus_email")));
                    cus.setCus_name(fila.getString(fila.getColumnIndex("cus_name")));
                    cus.setCus_surname(fila.getString(fila.getColumnIndex("cus_surname")));
                    cus.setCus_nif(fila.getString(fila.getColumnIndex("cus_nif")));
                }
            }
            fila.close();
        }
        catch (SQLiteException sqlte){
            Log.e("getCustomerByInvoiceId", "invId: " + invId, sqlte.getCause());
        }
        finally {
            db.close();
        }

        return cus;
    }

    public Customer getCustomerByPresupuestoId(SQLiteDatabase db, String preId, Context context){
        Customer cus = null;
        Cursor fila = null;
        try {
            cus = new Customer();
            String query = "SELECT cus_id, cus_email, cus_nombre, cus_apellidos, cus_nif from Customer, Presupuesto " +
                    "where pre_id = '" + preId + "' and pre_cus_id = cus_id " +
                    "and pre_deleted ='0' and cus_deleted='0'";
            fila = db.rawQuery(query, null);
            if(fila.getCount() != 0 && fila != null) {
                if (fila.moveToFirst()) {
                    Log.v("Ejecutando query = 1...", "");
                    cus.setCus_id(fila.getInt(fila.getColumnIndex("cus_id")));
                    cus.setCus_email(fila.getString(fila.getColumnIndex("cus_email")));
                    cus.setCus_name(fila.getString(fila.getColumnIndex("cus_name")));
                    cus.setCus_surname(fila.getString(fila.getColumnIndex("cus_surname")));
                    cus.setCus_nif(fila.getString(fila.getColumnIndex("cus_nif")));
                }
            }
        } catch (SQLiteException sqlte) {
            Log.e("getCusByPresupuestoId", "preId: " + preId, sqlte.getCause());
        } finally {
            fila.close();
            db.close();
        }

        return cus;
    }
}
