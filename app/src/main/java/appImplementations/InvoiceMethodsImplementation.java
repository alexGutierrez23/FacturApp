package appImplementations;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

import Constants.Constants;
import appInterfaces.CommonMethods;
import appInterfaces.InvoiceMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Information;
import sqlite.model.Invoice;
import sqlite.model.Taxi_driver_account;
import sqlite.model.Trip;
import sqlite.repo.AddressDML;
import sqlite.repo.InformationDML;
import sqlite.repo.InvoiceDML;
import sqlite.repo.Taxi_driver_accountDML;
import sqlite.repo.TripDML;
import sqlite.repo.YearsDML;

/**
 * Created by Usuario on 26/11/2017.
 */

public class InvoiceMethodsImplementation implements InvoiceMethods {
    public String createInvoice(Context context, Invoice invoice, String tdaId, String tckId, String cusId, String lastInvoiceNumber){
        String invoiceId = null;
        InvoiceDML invoiceDML = new InvoiceDML(context);
        CommonMethods cm = new CommonMethodsImplementation();
        Constants constants = new Constants();
        InvoiceMethods im = new InvoiceMethodsImplementation();
        int yeaId = -1;
        String numFactRed = null;
        if(lastInvoiceNumber == null | TextUtils.isEmpty(lastInvoiceNumber)){
            YearsDML yearDML = new YearsDML(context);
            yeaId = yearDML.insertarInvoiceYear(new DatabaseHelper(context), cm.takeYearFromDate(invoice.getInv_date()), context);
            numFactRed = buildInvoiceRed(invoice.getInv_date(), invoice, yeaId, context);
        }
        else{
            YearsDML yearDML = new YearsDML(context);
            yeaId = yearDML.insertarInvoiceYear(new DatabaseHelper(context), cm.sacarYear(lastInvoiceNumber), context);
            numFactRed = lastInvoiceNumber;
        }
        invoice.setInv_numFactRed(numFactRed);
        invoice.setInv_number(takeNumberFromNumFactRed(numFactRed));
        invoice.setInv_yea_id(String.valueOf(yeaId));
        invoice.setInv_tda_id(tdaId);
        int invoiceNumber = cm.sacarEntero(invoice.getInv_number(), constants.FACTURA_TYPE);
        invoice.setInv_invoiceNumber(String.valueOf(invoiceNumber));
        if(tckId != null){
            invoice.setInv_tck_id(tckId);
        }
     //   invoice.setInv_cus_id(cusId);

        return invoiceDML.insertarInvoice(new DatabaseHelper(context), invoice, context);
    }

    public void deleteInvoice(Context context, Invoice invoice){

    }

    public String createInformation(Information information, String invoiceId, Trip trip, Context context){
        InformationDML informationDML = new InformationDML(context);
        information.setInf_inv_id(invoiceId);
        String trp_id = createTrip(new DatabaseHelper(context), trip, context);
        information.setInf_trp_id(trp_id);
        return informationDML.insertarInfo(new DatabaseHelper(context), information, context);
    }

    public String createTrip(DatabaseHelper dbHelper, Trip trip, Context context){
        TripDML tripDML = new TripDML(context);
        return tripDML.insertarTrip(dbHelper, trip, context);
    }

    public void updateTrip(ArrayList<String> infoIds, String number, Context context){
        TripDML tripDML = new TripDML(context);
        InformationDML informationDML = new InformationDML(context);
        if(infoIds != null){
            for (String info : infoIds) {
                Information information = informationDML.getInfo(new DatabaseHelper(context), info);
                Trip trip = tripDML.getTripById(new DatabaseHelper(context).getWritableDatabase(), information.getInf_trp_id(), context);
                trip.setTrp_number(number);
                tripDML.update(trip, new DatabaseHelper(context).getWritableDatabase());
            }
        }
    }

/*
    public String createCustomer(Customer customer, Address address, Context context){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        CustomerDML customerDML = new CustomerDML(context);
        String adrId = createAddress(dbHelper, address, context);
        return customerDML.insertarCustomer(dbHelper, customer, adrId, context);

    }
*/
    public String createAddress(DatabaseHelper dbHelepr, Address address, Context context){
        AddressDML addressDML = new AddressDML(context);
        return addressDML.insertarAddress(dbHelepr, address, context);
    }

    public int getTotalInsertedRows(SQLiteDatabase db, String tableName){
        String query = " SELECT count(*) FROM " + tableName;
        Cursor countRows = db.rawQuery(query, null);
        countRows.moveToFirst();
        int result = countRows.getInt(0);
        countRows.close();

        return result;
    }

    public String buildInvoiceRed(String date, Invoice invoice, int yeaId, Context context){
        InvoiceDML invoiceDML = new InvoiceDML(context);
        CommonMethods cm = new CommonMethodsImplementation();
        int invoices = 0;
        String year = cm.takeYearFromDate(date);
        String numFactRed = null;
        invoices = invoiceDML.getInvoiceCountByYear(new DatabaseHelper(context).getWritableDatabase(), year, context);
        if( invoices == 0){  //DB vacia y creo ticket con valor 1
            //new ticket will be the result + 1
            invoices++;
        }
        else{  //Si no esta vacia, cojo ultimo ticket creado (esta en una lista ordenada) y creo con valor + 1.
            ArrayList<Integer> invoiceNumbers = invoiceDML.getInvoiceNumbers(new DatabaseHelper(context).getWritableDatabase(), yeaId, context);
            int invLastValue = invoiceNumbers.get(invoiceNumbers.size()-1);
            invoices = invLastValue + 1;
        }
        invoice.setInv_invoiceNumber(String.valueOf(invoices));
        numFactRed = "Factura_F" + invoices + "-" + year;
        return numFactRed;
    }

    private String takeNumberFromNumFactRed(String numFactRed){
        String delimiter = "Factura_";
        String [] temp;
        temp = numFactRed.split(delimiter);
        return temp[temp.length-1];
    }

    public Invoice getInvoice(int invoiceId, Context context){
        InvoiceDML invoiceDML = new InvoiceDML(context);
        return invoiceDML.getInvoiceById(new DatabaseHelper(context), invoiceId, context);
    }

    public ArrayList<Information> getInformationLinkedToInvoiceId(String invId, Context context){
        InformationDML i = new InformationDML(context);
        return i.getFullInformationByInvoiceId(new DatabaseHelper(context).getWritableDatabase(), invId, context);
    }

    public Trip getTrip(String trpId, Context context){
        TripDML trp = new TripDML(context);
        return trp.getTripById(new DatabaseHelper(context).getWritableDatabase(), trpId, context);
    }
/*
    public Customer getCustomerByInvoiceId(String invId, Context context){
        CustomerDML c = new CustomerDML(context);
        return c.getCustomerByInvoiceId(new DatabaseHelper(context).getWritableDatabase(), invId, context);
    }
*/
    public Address getAddressById(String adrId, Context context){
        AddressDML a = new AddressDML(context);
        return a.getAddressById(new DatabaseHelper(context).getWritableDatabase(), adrId, context);
    }

    public Taxi_driver_account getTda(String tdaId, Context context){
        Taxi_driver_accountDML tda = new Taxi_driver_accountDML(context);
        return tda.getTdaById(new DatabaseHelper(context), tdaId, context);
    }

    public void borrarInvoice(String invoiceId, Context context){
        InvoiceDML invoiceDML = new InvoiceDML(context);
        invoiceDML.borrarInvoice(new DatabaseHelper(context), invoiceId, context);
    }

    public void uploadPdfInvoice(String invoiceId, String ruta, Context context){
        InvoiceDML invoiceDML = new InvoiceDML(context);
        invoiceDML.uploadPdfInvoice(new DatabaseHelper(context), invoiceId, ruta, context);
    }

    public String getInvoicePath(int invoiceId, Context context){
        InvoiceDML invoiceDML = new InvoiceDML(context);
        return invoiceDML.getInvPdfPath(new DatabaseHelper(context), invoiceId, context);
    }

    public Invoice getInvoice(String invId, Context context){
        InvoiceDML invoiceDML = new InvoiceDML(context);
        return invoiceDML.getInvoiceById(new DatabaseHelper(context), Integer.parseInt(invId), context);
    }

    public void updateInformationForInvoice(ArrayList<String> infoIds, String invoiceId, Context context){
        InformationDML informationDML = new InformationDML(context);
        if(infoIds != null){
            for(String info : infoIds){
                Information information = informationDML.getInfo(new DatabaseHelper(context), info);
                information.setInf_inv_id(invoiceId);
                informationDML.update(information, new DatabaseHelper(context).getWritableDatabase());
            }
        }
    }

}
