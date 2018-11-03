package sqlite.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;
import sqlite.model.Stop;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;
import sqlite.model.Years;
import sqlite.repo.AddressDML;
import sqlite.repo.ArrendatarioDML;
import sqlite.repo.ContratoDML;
import sqlite.repo.CustomerDML;
import sqlite.repo.HojaRutaDML;
import sqlite.repo.InformationDML;
import sqlite.repo.InvoiceDML;
import sqlite.repo.LastFileDML;
import sqlite.repo.PresupuestoDML;
import sqlite.repo.StopDML;
import sqlite.repo.Taxi_DriverDML;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TicketDML;
import sqlite.repo.TripDML;
import sqlite.repo.YearsDML;


/**
 * Created by Usuario on 06/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 30;  // ULTIMA VERSION LANZADA A CLIENTE ES LA 30.
    /**
     *  PRUEBAS HECHAS POR CARLOS EN LA VERSION 28!!!!
     */

    // Database Name
    private static final String DATABASE_NAME = "DBforTaxiApp.db";
    private static final String TAG = DatabaseHelper.class.getSimpleName().toString();

    /*public DatabaseHelper( ) {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }*/

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        //db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL(Taxi_DriverDML.createTable());
        db.execSQL(Taxi_driver_accountDML.createTable());
        db.execSQL(CustomerDML.createTable());
        db.execSQL(AddressDML.createTable());
        db.execSQL(TicketDML.createTable());
        db.execSQL(InvoiceDML.createTable());
        db.execSQL(TripDML.createTable());
        db.execSQL(InformationDML.createTable());
        db.execSQL(YearsDML.createTable());
        db.execSQL(PresupuestoDML.createTable());
        db.execSQL(LastFileDML.createTable());
        // 3 new tables for last version of FacturApp
        db.execSQL(ContratoDML.createTable());
        db.execSQL(HojaRutaDML.createTable());
        db.execSQL(StopDML.createTable());
        db.execSQL(ArrendatarioDML.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        // Drop table if existed, all data will be gone!!!
    /*    db.execSQL("DROP TABLE IF EXISTS " + Taxi_Driver.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Taxi_driver_account.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Customer.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Address.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Ticket.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Invoice.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Trip.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Information.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Years.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Presupuesto.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LastFile.TABLE);  */
        switch (newVersion){
            case 20:  //TODO ESTO ES OBLIGATORIO DE EJECUTAR PARA LOS CLIENTES!!!!!!!
                Log.d(TAG, "SQLiteDatabase upgraded to version 20 " + newVersion);
                //db.execSQL(DB_ALTER_ARD2);
                //db.execSQL(DB_ALTER_ARD);
                //db.execSQL(DB_ALTER_CONTRATO);
                boolean alterYear = columnExists(Years.TABLE, Years.KEY_yea_contrato, db);
                if(!alterYear){
                    db.execSQL(DB_ALTER_YEARS);
                }
                boolean alterTrip = columnExists(Trip.TABLE, Trip.KEY_trp_start_place, db);
                if(!alterTrip){
                    db.execSQL(DB_ALTER_TRIP);
                }
                boolean alterTda = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_mobile, db);
                if(!alterTda){
                    db.execSQL(DB_ALTER_TDA);
                }
                boolean alterTdaBlob = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_logo, db);
                if(!alterTdaBlob){
                    db.execSQL(DB_ALTER_TDA_ADD_BLOB);
                }
                boolean alterTdaLogoPath = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_logo_path, db);
                if(!alterTdaLogoPath){
                    db.execSQL(DB_ALTER_TDA_ADD_LOGO_PATH);
                }

                db.execSQL("ALTER TABLE " + Arrendatario.TABLE + " RENAME TO Arrendatario_temporalNo_Validooooo");
                db.execSQL(ArrendatarioDML.createTable());

                db.execSQL("ALTER TABLE " + Contrato.TABLE + " RENAME TO Contrato_temporalNo_Validoooo");
                db.execSQL(ContratoDML.createTable());

                db.execSQL("ALTER TABLE " + HojaRuta.TABLE + " RENAME TO HojaRuta_temporalNo_Validooooo");
                db.execSQL(HojaRutaDML.createTable());

                db.execSQL("ALTER TABLE " + Stop.TABLE + " RENAME TO Stop_temporalNo_Validoooo");
                db.execSQL(StopDML.createTable());
                break;

            case 25:
                db.execSQL("ALTER TABLE " + Arrendatario.TABLE + " RENAME TO Arrendatario_temporalNoValidoooooo");
                db.execSQL(ArrendatarioDML.createTable());

                db.execSQL("ALTER TABLE " + Contrato.TABLE + " RENAME TO Contrato_temporalNoValidoooooo");
                db.execSQL(ContratoDML.createTable());

                db.execSQL("ALTER TABLE " + HojaRuta.TABLE + " RENAME TO HojaRuta_temporalNoValidoooooo");
                db.execSQL(HojaRutaDML.createTable());

                db.execSQL("ALTER TABLE " + Stop.TABLE + " RENAME TO Stop_temporalNoValidoooooo");
                db.execSQL(StopDML.createTable());
                break;
            case 26:
                db.execSQL(DB_DELETE_ARDS);
                break;
            case 28:
                boolean alterYear2 = columnExists(Years.TABLE, Years.KEY_yea_contrato, db);
                if(!alterYear2){
                    db.execSQL(DB_ALTER_YEARS);
                }
                boolean alterTrip2 = columnExists(Trip.TABLE, Trip.KEY_trp_start_place, db);
                if(!alterTrip2){
                    db.execSQL(DB_ALTER_TRIP);
                }
                boolean alterTda2 = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_mobile, db);
                if(!alterTda2){
                    db.execSQL(DB_ALTER_TDA);
                }
                boolean alterTdaBlob2 = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_logo, db);
                if(!alterTdaBlob2){
                    db.execSQL(DB_ALTER_TDA_ADD_BLOB);
                }
                boolean alterTdaLogoPath2 = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_logo_path, db);
                if(!alterTdaLogoPath2){
                    db.execSQL(DB_ALTER_TDA_ADD_LOGO_PATH);
                }

                boolean ardExists = tableExists(db, Arrendatario.TABLE);
                if(ardExists){
                    db.execSQL("ALTER TABLE " + Arrendatario.TABLE + " RENAME TO Arrendatario_temporalNo_Validoooooooo");
                    db.execSQL(ArrendatarioDML.createTable());
                }
                else{
                    db.execSQL(ArrendatarioDML.createTable());
                }

                boolean contratoExists = tableExists(db, Contrato.TABLE);
                if(contratoExists){
                    db.execSQL("ALTER TABLE " + Contrato.TABLE + " RENAME TO Contrato_temporalNo_Validooooooo");
                    db.execSQL(ContratoDML.createTable());
                }
                else{
                    db.execSQL(ContratoDML.createTable());
                }

                boolean hrExists = tableExists(db, HojaRuta.TABLE);
                if(hrExists){
                    db.execSQL("ALTER TABLE " + HojaRuta.TABLE + " RENAME TO HojaRuta_temporalNo_Validoooooooo");
                    db.execSQL(HojaRutaDML.createTable());
                }
                else{
                    db.execSQL(HojaRutaDML.createTable());
                }

                boolean stopExists = tableExists(db, Stop.TABLE);
                if(stopExists) {
                    db.execSQL("ALTER TABLE " + Stop.TABLE + " RENAME TO Stop_temporalNo_Validooooooo");
                    db.execSQL(StopDML.createTable());
                }
                else{
                    db.execSQL(StopDML.createTable());
                }

                break;

            case 30:
                boolean alterYear3 = columnExists(Years.TABLE, Years.KEY_yea_contrato, db);
                if(!alterYear3){
                    db.execSQL(DB_ALTER_YEARS);
                }
                boolean alterTrip3 = columnExists(Trip.TABLE, Trip.KEY_trp_start_place, db);
                if(!alterTrip3){
                    db.execSQL(DB_ALTER_TRIP);
                }
                boolean alterTda3 = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_mobile, db);
                if(!alterTda3){
                    db.execSQL(DB_ALTER_TDA);
                }
                boolean alterTdaBlob3 = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_logo, db);
                if(!alterTdaBlob3){
                    db.execSQL(DB_ALTER_TDA_ADD_BLOB);
                }
                boolean alterTdaLogoPath3 = columnExists(Taxi_driver_account.TABLE, Taxi_driver_account.KEY_tda_logo_path, db);
                if(!alterTdaLogoPath3){
                    db.execSQL(DB_ALTER_TDA_ADD_LOGO_PATH);
                }

                boolean ardExists1 = tableExists(db, Arrendatario.TABLE);
                if(ardExists1){
                    db.execSQL("ALTER TABLE " + Arrendatario.TABLE + " RENAME TO Arrendatario_temporalNo_Validoooooooooo");
                    db.execSQL(ArrendatarioDML.createTable());
                }
                else{
                    db.execSQL(ArrendatarioDML.createTable());
                }

                boolean contratoExists1 = tableExists(db, Contrato.TABLE);
                if(contratoExists1){
                    db.execSQL("ALTER TABLE " + Contrato.TABLE + " RENAME TO Contrato_temporalNo_Validooooooooo");
                    db.execSQL(ContratoDML.createTable());
                }
                else{
                    db.execSQL(ContratoDML.createTable());
                }

                boolean hrExists1 = tableExists(db, HojaRuta.TABLE);
                if(hrExists1){
                    db.execSQL("ALTER TABLE " + HojaRuta.TABLE + " RENAME TO HojaRuta_temporalNo_Validoooooooooo");
                    db.execSQL(HojaRutaDML.createTable());
                }
                else{
                    db.execSQL(HojaRutaDML.createTable());
                }

                boolean stopExists1 = tableExists(db, Stop.TABLE);
                if(stopExists1) {
                    db.execSQL("ALTER TABLE " + Stop.TABLE + " RENAME TO Stop_temporalNo_Validooooooooo");
                    db.execSQL(StopDML.createTable());
                }
                else{
                    db.execSQL(StopDML.createTable());
                }

                break;

            default:
                throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion
                        + " and newVersion " + newVersion);
        }

        //onCreate(db);
    }

    //TO-DO poner
    public static final String DB_ALTER_TDA = "ALTER TABLE " + Taxi_driver_account.TABLE
            + " ADD COLUMN " + Taxi_driver_account.KEY_tda_mobile + " TEXT ; ";
    public static final String DB_ALTER_YEARS = "ALTER TABLE " + Years.TABLE
            + " ADD COLUMN " + Years.KEY_yea_contrato + " TEXT DEFAULT '0' ; ";
    public static final String DB_ALTER_TRIP = "ALTER TABLE " + Trip.TABLE
            + " ADD COLUMN " + Trip.KEY_trp_start_place + " TEXT ; ";
    public static final String DB_ALTER_TDA_ADD_BLOB = "ALTER TABLE " + Taxi_driver_account.TABLE
            + " ADD COLUMN " + Taxi_driver_account.KEY_tda_logo + " BLOB ; ";
    public static final String DB_ALTER_TDA_ADD_LOGO_PATH = "ALTER TABLE " + Taxi_driver_account.TABLE
            + " ADD COLUMN " + Taxi_driver_account.KEY_tda_logo_path + " TEXT ; ";
    public static final String DB_ALTER_ARD = "ALTER TABLE " + Arrendatario.TABLE
            + " ADD COLUMN " + Arrendatario.KEY_ard_fecha + " TEXT ; ";
    public static final String DB_ALTER_CONTRATO = "ALTER TABLE " + Contrato.TABLE
            + " ADD COLUMN " + Contrato.KEY_con_isTda_arrendador + " TEXT DEFAULT '0' ";
    public static final String DB_ALTER_ARD2 = "ALTER TABLE " + Arrendatario.TABLE
            + " ADD COLUMN " + Arrendatario.KEY_ard_number + " TEXT ";

    public static final String DB_CREATE_ARRENDATARIO_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS "
            + Arrendatario.TABLE + "("
            + Arrendatario.KEY_ard_id  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Arrendatario.KEY_ard_deleted + " TEXT DEFAULT '0' , "
            + Arrendatario.KEY_ard_name  + " TEXT, "
            + Arrendatario.KEY_ard_nifCif  + " TEXT, "
            + Arrendatario.KEY_ard_fecha + " TEXT, "
            + Arrendatario.KEY_ard_number + " TEXT ) ";

    private boolean columnExists(String table, String column, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + table + ")", null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(column.equalsIgnoreCase(name)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tableExists(SQLiteDatabase db, String table){
        if (table == null || TextUtils.isEmpty(table)){
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = '" + table + "' ", null);
        if (!cursor.moveToFirst()){
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        if(count > 0){
            return true;
        }
        return false;
    }

    public static final String DB_DELETE_ARDS = "UPDATE " + Arrendatario.TABLE + " SET ard_deleted = '1' WHERE ard_deleted = '0' ";

    //PDF Proteccion datos
    //PDF guia uso
    //PDF agradecimientos

}
