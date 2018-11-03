package appInterfaces;

import android.content.Context;

import sqlite.data.DatabaseHelper;
import sqlite.model.Address;
import sqlite.model.Ticket;
import sqlite.model.Trip;

/**
 * Created by Usuario on 26/11/2017.
 */

public interface TicketMethods {
    /** Crear e insertar ticket nuevo y devolver su ID.
     *
     * @param tck Ticket
     * @param
     * @param
     * @param
     * @param context
     * @return Ticket ID inserted.
     */
    String createTicket(Ticket tck, String tdaId, Trip trip, String cusId, String lastTicketNumber, Context context);

 //   void deleteTicket(Ticket tck, Context context);

    String createTrip(Trip trip, Context context);

  //  String createCustomer(Customer customer, Address address, Context context);

    String createAddress(Address address, Context context);

    String buildTicketRed(String date, Ticket ticket, int yeaId, Context context);

    int getTotalInsertedRowsFromTable(DatabaseHelper dbHelper, String table);

    void uploadPdfTicket(String tckId, String ruta, Context context);

    String getTicketPath(int tckId, Context context);

    Ticket getTicket(String ticketId, Context context);
}
