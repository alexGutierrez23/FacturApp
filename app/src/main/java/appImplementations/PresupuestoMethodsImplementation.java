package appImplementations;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

import Constants.Constants;
import appInterfaces.CommonMethods;
import appInterfaces.PresupuestoMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Presupuesto;
import sqlite.model.Trip;
import sqlite.repo.AddressDML;
import sqlite.repo.InformationDML;
import sqlite.repo.PresupuestoDML;
import sqlite.repo.TripDML;
import sqlite.repo.YearsDML;


/**
 * Created by Usuario on 16/01/2018.
 */

public class PresupuestoMethodsImplementation implements PresupuestoMethods {
    public String createPresupuesto(Context context, Presupuesto presupuesto, String tdaId, String tckId, String cusId, String lastPresupuestoNumber){
        Constants constants = new Constants();
        PresupuestoDML presupuestoDML = new PresupuestoDML(context);
        CommonMethods cm = new CommonMethodsImplementation();
        int yeaId = -1;
        String numFactRed = null;
        if(lastPresupuestoNumber == null | TextUtils.isEmpty(lastPresupuestoNumber)){
            YearsDML yearDML = new YearsDML(context);
            yeaId = yearDML.insertarPresupuestoYear(new DatabaseHelper((context)), cm.takeYearFromDate(presupuesto.getPre_date()), context);
            numFactRed = buildPresupuestoRed(presupuesto.getPre_date(), presupuesto, yeaId, context);
        }
        else{
            YearsDML yearDML = new YearsDML(context);
            yeaId = yearDML.insertarPresupuestoYear(new DatabaseHelper((context)), cm.sacarYear(lastPresupuestoNumber), context);
            numFactRed = lastPresupuestoNumber;
        }
        presupuesto.setPre_numPresupuestoRed(numFactRed);
        presupuesto.setPre_number(takeNumberFromNumFactRed(numFactRed));
        presupuesto.setPre_yea_id(String.valueOf(yeaId));
        presupuesto.setPre_tda_id(tdaId);
        int presupuestoNumber = cm.sacarEntero(presupuesto.getPre_number(), constants.PRESUPUESTO_TYPE);
        presupuesto.setPre_presupuestoNumber(String.valueOf(presupuestoNumber));
        if(tckId != null){
            presupuesto.setPre_tck_id(tckId);
        }
       // presupuesto.setPre_cus_id(cusId);

        return presupuestoDML.insertarPresupuesto(new DatabaseHelper(context), presupuesto, context);
    }

    public void deletePresupuesto(Context context, Presupuesto presupuesto){

    }

    public String createInformation(Information information, String presupuestoId, Trip trip, Context context){
        InformationDML informationDML = new InformationDML(context);
        information.setInf_pre_id(presupuestoId);
        String trp_id = createTrip(trip, context);
        information.setInf_trp_id(trp_id);
        return informationDML.insertarInfo(new DatabaseHelper(context), information, context);
    }

    public void updateInformationForPresupuesto(ArrayList<String> infoIds, String presupuestoId, Context context){
        InformationDML informationDML = new InformationDML(context);
        if(infoIds != null){
            for(String info : infoIds){
                Information information = informationDML.getInfo(new DatabaseHelper(context), info);
                information.setInf_pre_id(presupuestoId);
                informationDML.update(information, new DatabaseHelper(context).getWritableDatabase());
            }
        }
    }

    public String createTrip(Trip trip, Context context){
        TripDML tripDML = new TripDML(context);
        return tripDML.insertarTrip(new DatabaseHelper((context)), trip, context);
    }
/*
    public String createCustomer(Customer customer, Address address, Context context){
        CustomerDML customerDML = new CustomerDML(context);
        String adrId = createAddress(address, context);
        return customerDML.insertarCustomer(new DatabaseHelper(context), customer, adrId, context);
    }
*/
    public String createAddress(Address address, Context context){
        AddressDML addressDML = new AddressDML(context);
        return addressDML.insertarAddress(new DatabaseHelper(context), address, context);
    }

    public int getTotalInsertedRows(SQLiteDatabase db, String tableName){
        String query = " SELECT count(*) FROM " + tableName;
        Cursor countRows = db.rawQuery(query, null);
        countRows.moveToFirst();
        int result = countRows.getInt(0);
        countRows.close();
        db.close();
        return result;
    }

    public String buildPresupuestoRed(String date, Presupuesto presupuesto, int yeaId, Context context){
        PresupuestoDML presupuestoDML = new PresupuestoDML(context);
        CommonMethods cm = new CommonMethodsImplementation();
        int presupuestos = 0;
        String year = cm.takeYearFromDate(date);
        String numPreRed = null;
        presupuestos = presupuestoDML.getPresupuestoCountByYear(new DatabaseHelper(context).getWritableDatabase(), year, context);
        if( presupuestos == 0){  //DB vacia y creo ticket con valor 1
            //new ticket will be the result + 1
            presupuestos++;
        }
        else{  //Si no esta vacia, cojo ultimo ticket creado (esta en una lista ordenada) y creo con valor + 1.
            ArrayList<Integer> presupuestoNumbers = presupuestoDML.getPresupuestoNumbers(new DatabaseHelper(context).getWritableDatabase(), yeaId, context);
            int preLastValue = presupuestoNumbers.get(presupuestoNumbers.size()-1);
            presupuestos = preLastValue + 1;
        }
        presupuesto.setPre_presupuestoNumber(String.valueOf(presupuestos));
        numPreRed = "Presupuesto_P" + presupuestos + "-" + year;
        return numPreRed;
    }

    private String takeNumberFromNumFactRed(String numFactRed){
        String delimiter = "Presupuesto_";
        String [] temp;
        temp = numFactRed.split(delimiter);
        return temp[temp.length-1];
    }

    public Presupuesto getPresupuesto(int presupuestoId, Context context){
        PresupuestoDML presupuestoDML = new PresupuestoDML(context);
        return presupuestoDML.getPresupuestoById(new DatabaseHelper(context), presupuestoId, context);
    }

    public ArrayList<Information> getInformationLinkedToPresupuestoId(String preId, Context context){
        InformationDML i = new InformationDML(context);
        return i.getFullInformationByPresupuestoId(new DatabaseHelper(context).getWritableDatabase(), preId, context);
    }

    public Trip getTrip(String trpId, Context context){
        TripDML trp = new TripDML(context);
        return trp.getTripById(new DatabaseHelper(context).getWritableDatabase(), trpId, context);
    }

 /*   public Customer getCustomerByPresupuestoId(String preId, Context context){
        CustomerDML c = new CustomerDML(context);
        return c.getCustomerByPresupuestoId(new DatabaseHelper(context).getWritableDatabase(), preId, context);
    }
*/
    public Address getAddressById(String adrId, Context context){
        AddressDML a = new AddressDML(context);
        return a.getAddressById(new DatabaseHelper(context).getWritableDatabase(), adrId, context);
    }

    public void borrarPresupuesto(String presupuestoId, Context context){
        PresupuestoDML presupuestoDML = new PresupuestoDML(context);
        presupuestoDML.borrarPresupuesto(new DatabaseHelper(context), presupuestoId, context);
    }

    public void uploadPdfPresupuesto(String presupuestoId, String ruta, Context context){
        PresupuestoDML presupuestoDML = new PresupuestoDML(context);
        presupuestoDML.uploadPdfPresupuesto(new DatabaseHelper(context), presupuestoId, ruta, context);
    }

    public String getPresupuestoPath(int preId, Context context){
        PresupuestoDML presupuestoDML = new PresupuestoDML(context);
        return presupuestoDML.getPresupuestoPdfPath(new DatabaseHelper(context), preId, context);
    }
}
