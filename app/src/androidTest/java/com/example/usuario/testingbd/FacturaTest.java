package com.example.usuario.testingbd;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class FacturaTest {
    int ticketId;
    int ticketIns;
    int tda_id;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.usuario.testingbd", appContext.getPackageName());
    }

    @Test
    public void testDb(){


    }

    /*
    @Test
    public void testYear(){
        CommonMethods cm = new CommonMethodsImplementation();
        String year = cm.takeYearFromDate("12/02/2018");
        System.out.println("year: " + year);
        System.out.println();
        System.out.println("----------------------------");
        System.out.println();
        String year1 = cm.takeYear("12/02/2018");
        System.out.println("year1: " + year1);
    }

    @Test
    public void testEmptyTicket(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Invoice invoice = buildInvoice();
        insertEv(invoice);
        InvoiceDML invoiceDML = new InvoiceDML(appContext);
        Information info = buildInfo();
        InformationDML informationDML = new InformationDML(appContext);
        int fact = invoiceDML.getInvoiceByNumFactRed(new DatabaseHelper(appContext).getWritableDatabase(), invoice.getInv_numFactRed(), appContext);
        info.setInf_inv_id(String.valueOf(fact));
        informationDML.insertarInfo(new DatabaseHelper(appContext), info, appContext);
        InvoiceMethods invMethods = new InvoiceMethodsImplementation();
        invMethods.getTotalInsertedRows(new DatabaseHelper(appContext).getWritableDatabase(), "Information");
        int inse = invoiceDML.getTotalInsertedRows(new DatabaseHelper(appContext).getWritableDatabase());
        TicketDML tck = new TicketDML(appContext);
    //    int tckIns = tck.getTotalInsertedRows(new DatabaseHelper(appContext).getWritableDatabase());
     //   ticketIns = tckIns;
    /*    assertEquals(a, 1);
        assertEquals(b, 2);
        assertEquals(c, 3);   */
     //   assertEquals(insertedRows, 1);

/*        assertEquals(inse, 1);
        assertEquals(tda_id, 1);
        assertEquals(ticketIns, 1);
        assertEquals(ticketId, 1);
    }

    private void insertEv(Invoice invoice){
        Context appContext = InstrumentationRegistry.getTargetContext();

        Trip trip = buildTrip();
        Taxi_driver_account tda = buildTda();
        Ticket tck = buildTicket();
        Taxi_DriverDML txdDml = new Taxi_DriverDML(appContext);
        Taxi_driver_accountDML tdaDml = new Taxi_driver_accountDML(appContext);
        tdaDml.insertarTaxiDriverAccount(new DatabaseHelper((appContext)), tdaDml, tda, appContext);
        int tdaId = tdaDml.getTaxiDriverAccountByNIF(new DatabaseHelper((appContext)).getWritableDatabase(), tda.getTda_nif(), appContext);
        boolean insertado2 = false;
        if (tdaId != 0){
            txdDml.insertarTaxiDriver(new DatabaseHelper((appContext)),"alejandro.gutierrez.car@gmail.com", "hola", tdaId, appContext);
            tda_id = tdaId;
            System.out.println("TDAId no vale 0");
        }

        if(insertado2){
            TripDML tripDML = new TripDML(appContext);
            TicketDML ticketDML = new TicketDML(appContext);
            tripDML.insertarTrip(new DatabaseHelper((appContext)), trip, appContext);
            int trp_id = tripDML.getTripByNumber(new DatabaseHelper((appContext)).getWritableDatabase(), trip.getTrp_number(), appContext);
            if(trp_id != 0) {
                tck.setTck_trp_id(String.valueOf(trp_id));
                tck.setTck_tda_id(String.valueOf(tdaId));
                System.out.println("TDA id: " + tdaId);
                ticketDML.insertarTicket(new DatabaseHelper((appContext)), tck, appContext);
                int tckId = ticketDML.getTicketByNumTicketRed(dbHelper, tck.getTck_numTickRed(), appContext);
                ticketId = tckId;
                System.out.println("Ticket id: " + tckId);

                invoice.setInv_tda_id(String.valueOf(tdaId));
                invoice.setInv_tck_id(String.valueOf(tckId));
                InvoiceDML invoiceDML = new InvoiceDML(appContext);
                invoiceDML.insertarInvoice(new DatabaseHelper(appContext), invoice, appContext);

                System.out.println("Trip id: " + trp_id);
                System.out.println("Todo insertado correctamente");
            }
            else{
                System.out.println("No se ha insertado correctamente trip");
            }
            //Faltaria por insertar customer para unirlo a Ticket tb

        }
        else{
            System.out.println("No se ha insertado correctamente taxiDriver");
        }
    }

    private Ticket buildTicket(){
        Ticket tck = new Ticket();
        String fecha = "10/10/2017";
        tck.setTck_date(fecha);
        tck.setTck_time("18:50");
        tck.setTck_numTickRed("123");
        tck.setTck_price("30");
        //   tck.setContadorIteraciones(contadorIteraciones);
        tck.setTck_number(String.valueOf(1));
        //    tck.setTck_cus_id(null);
        return tck;
    }

    private Invoice buildInvoice(){
        Invoice inv = new Invoice();
        String fecha = "10/10/2017";
        inv.setInv_number("R12_2019");
        inv.setInv_date(fecha);
        inv.setInv_numFactRed("R12_2019");
        inv.setInv_time("13.00");

        return inv;
    }

    private Trip buildTrip(){
        Trip trip = new Trip();
        trip.setTrp_finish("toledo");
        trip.setTrp_start("madrid");
        trip.setTrp_price("30");
        trip.setTrp_number(String.valueOf(1));
        return trip;
    }

    private Taxi_driver_account buildTda(){
        Taxi_driver_account tda = new Taxi_driver_account();
        tda.setTda_surname("Gutierrez");
        tda.setTda_nif("50128034V");
        tda.setTda_name("Alejandro");
        tda.setTda_daytime_number("915183237");
        tda.setTda_mobile("659785158");
        tda.setTda_email("alexguti9@gmail.com");
        tda.setTda_license_number("50128034");
        return tda;
    }

    private Information buildInfo(){
        Information info = new Information();
        info.setInf_additionalInfo("Hola me llamo Alejandro y me cago en tu puta vieja!");
        return info;
    }

*/
}
