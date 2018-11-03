package appImplementations;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;

import Constants.Constants;
import appInterfaces.CommonMethods;
import appInterfaces.TicketMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Customer;
import sqlite.model.Ticket;
import sqlite.model.Trip;
import sqlite.repo.AddressDML;
import sqlite.repo.CustomerDML;
import sqlite.repo.TicketDML;
import sqlite.repo.TripDML;
import sqlite.repo.YearsDML;

/**
 * Created by Usuario on 26/11/2017.
 */

public class TicketMethodsImplementation implements TicketMethods {
    public String createTicket(Ticket tck, String tdaId, Trip trip, String cusId, String lastTicketNumber, Context context){
        String ticketId = null;
        TicketMethods tm = new TicketMethodsImplementation();
        CommonMethods cm = new CommonMethodsImplementation();
        Constants constants = new Constants();
        TicketDML ticketDML = new TicketDML(context);
        tck.setTck_tda_id(tdaId);
        int yeaId = -1;
        String numTicketRed = null;
        if(lastTicketNumber == null | TextUtils.isEmpty(lastTicketNumber)){
            YearsDML yearDML = new YearsDML(context);
            yeaId = yearDML.insertarTicketYear(new DatabaseHelper(context), cm.takeYearFromDate(tck.getTck_date()), context);
            numTicketRed = tm.buildTicketRed(tck.getTck_date(), tck, yeaId, context);
        }
        else{
            YearsDML yearDML = new YearsDML(context);
            yeaId = yearDML.insertarTicketYear(new DatabaseHelper(context), cm.sacarYear(lastTicketNumber), context);
            numTicketRed = lastTicketNumber;
        }

        tck.setTck_numTickRed(numTicketRed);
        tck.setTck_number(takeNumberFromNumTickRed(numTicketRed));
        int ticketNumber = cm.sacarEntero(tck.getTck_number(), constants.TICKET_TYPE);
        tck.setTck_ticketNumber(String.valueOf(ticketNumber));
        tck.setTck_yea_id(String.valueOf(yeaId));
        if(trip != null){
            trip.setTrp_number(tck.getTck_number());
            String tripId = tm.createTrip(trip, context);
            tck.setTck_trp_id(tripId);
            if(!trip.equals("0") || !trip.equals("-1") || trip != null){
                ticketId = ticketDML.insertarTicket(new DatabaseHelper(context), tck, context);
                //int tckId = ticketDML.getTicketByNumTicketRed(new DatabaseHelper(context).getWritableDatabase(), tck.getTck_numTickRed(), context);
            }
            else{
                ticketId = "-1";
            }
        }
        else{
            tck.setTck_trp_id(null);
            ticketId = ticketDML.insertarTicket(new DatabaseHelper(context), tck, context);
        }

        return ticketId;
    }

    public String createTrip(Trip trip, Context context){
        TripDML tripDML = new TripDML(context);
        return tripDML.insertarTrip(new DatabaseHelper(context), trip, context);
    }

    public String createCustomer(Customer customer, Address address, Context context){
        CustomerDML customerDML = new CustomerDML(context);
        String adrId = createAddress(address, context);
        return customerDML.insertarCustomer(new DatabaseHelper(context), customer, adrId, context);
    }

    public String createAddress(Address address, Context context){
        AddressDML addressDML = new AddressDML(context);
        return addressDML.insertarAddress(new DatabaseHelper(context), address, context);
    }

    public int getTotalInsertedRowsFromTable(DatabaseHelper dbHelper, String table){
        String query = " SELECT count(*) FROM " + table;
        Cursor countRows = dbHelper.getWritableDatabase().rawQuery(query, null);
        countRows.moveToFirst();
        int result = countRows.getInt(0);
        countRows.close();

        return result;
    }

    public String buildTicketRed(String date, Ticket ticket, int yeaId, Context context){
        String numTicketRed = null;
        int tickets = 0;
        TicketDML ticketDML = new TicketDML(context);
        CommonMethods cm = new CommonMethodsImplementation();
        String year = cm.takeYearFromDate(date);
        tickets = ticketDML.getTicketCountByYear(new DatabaseHelper(context), year, context);
        if( tickets == 0){  //DB vacia y creo ticket con valor 1
            //new ticket will be the result + 1
            tickets++;
        }
        else{  //Si no esta vacia, cojo ultimo ticket creado (esta en una lista ordenada) y creo con valor + 1.
            ArrayList<Integer> ticketNumbers = ticketDML.getTicketNumbers(new DatabaseHelper(context), yeaId , context);
            int tckLastValue = ticketNumbers.get(ticketNumbers.size()-1);
            tickets = tckLastValue + 1;
        }
        ticket.setTck_ticketNumber(String.valueOf(tickets));
        numTicketRed = "Ticket_R" + tickets + "-" + year;
        return numTicketRed;
    }

    private String takeNumberFromNumTickRed(String numTickRed){
        String delimiter = "Ticket_";
        String [] temp;
        temp = numTickRed.split(delimiter);
        return temp[temp.length-1];
    }

    public void uploadPdfTicket(String tckId, String ruta, Context context){
        TicketDML ticketDML = new TicketDML(context);
        ticketDML.uploadPdfTicket(new DatabaseHelper(context), tckId, ruta, context);
    }

    public String getTicketPath(int tckId, Context context){
        TicketDML ticketDML = new TicketDML(context);
        return ticketDML.getTicketPdfPath(new DatabaseHelper(context), tckId, context);
    }

    public Ticket getTicket(String ticketId, Context context){
        TicketDML ticketDML = new TicketDML(context);
        return ticketDML.getTicketById(new DatabaseHelper(context), ticketId, context);
    }

}
