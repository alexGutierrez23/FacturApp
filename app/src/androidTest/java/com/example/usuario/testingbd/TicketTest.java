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
public class TicketTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.usuario.testingbd", appContext.getPackageName());
    }
    int insertedYear = 0;
 /*   @Test
    public void testTicketNotEmpty(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        insertEv();
        TicketDML t = new TicketDML(appContext);
        int a = t.getTicketByNumber(new DatabaseHelper((appContext)).getWritableDatabase(), "123", appContext);
        assertEquals(a, 1);
    }
*/

 /*   @Test
    public void testEmptyTicket(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        TicketDML ticketDML = new TicketDML(appContext);
        TicketMethods t = new TicketMethodsImplementation();
        CommonMethods cm = new CommonMethodsImplementation();
   //     int a = ticketDML.getTicketByNumTicketRed(new DatabaseHelper(appContext).getWritableDatabase(), "R1-2017", appContext);
   //     insertEv();
        String s = cm.takeYearFromDate(" 10 / 12 / 2017 ");
        s = s.replaceAll("\\s+", "");
        assertEquals("2017", s);
        String delimiter = "Ticket_";
        String [] temp;
        temp = "Ticket_R12-2017".split(delimiter);
        assertEquals(temp[temp.length-1], "R12-2017");
    }

    private void insertEv(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        TicketMethods tm = new TicketMethodsImplementation();
        CommonMethods cm = new CommonMethodsImplementation();
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
        }

        if(insertado2){
            TripDML tripDML = new TripDML(appContext);
            TicketDML ticketDML = new TicketDML(appContext);
            tripDML.insertarTrip(new DatabaseHelper((appContext)), trip, appContext);
            int trp_id = tripDML.getTripByNumber(new DatabaseHelper((appContext)).getWritableDatabase(), trip.getTrp_number(), appContext);
            if(trp_id != 0) {
                tck.setTck_trp_id(String.valueOf(trp_id));
                tck.setTck_tda_id(String.valueOf(tdaId));
                YearsDML yearDML = new YearsDML(appContext);
                yearDML.insertarTicketYear(new DatabaseHelper((appContext)), cm.takeYearFromDate(tck.getTck_date()), appContext);
                String numTicketRed = tm.buildTicketRed(tck.getTck_date(), tck, appContext);
                tck.setTck_numTickRed(numTicketRed);
                System.out.println("Numero Ticket Reducido: " + numTicketRed);
                System.out.println("TDA id: " + tdaId);
                System.out.println("TDA id: " + ticketDML.getTicketCountByYear(new DatabaseHelper(appContext).getWritableDatabase(),"2017",appContext));
                ticketDML.insertarTicket(new DatabaseHelper((appContext)), tck, appContext);
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
//        tck.setTck_numTickRed("123");
        tck.setTck_price("30");
        //   tck.setContadorIteraciones(contadorIteraciones);
        tck.setTck_number(String.valueOf(1));
    //    tck.setTck_cus_id(null);
        return tck;
    }

/*    private Ticket buildTicket2(){
        Ticket tck = new Ticket();
        String fecha = "10/10/2017";
        tck.setTck_date(fecha);
        tck.setTck_time("18:50");
        tck.setTck_numTickRed("213");
        tck.setTck_price("30");
        //   tck.setContadorIteraciones(contadorIteraciones);
        tck.setTck_number(String.valueOf(1));
     //   tck.setTck_cus_id(null);
        return tck;
    }

    private Ticket buildTicket3(){
        Ticket tck = new Ticket();
        String fecha = "10/10/2017";
        tck.setTck_date(fecha);
        tck.setTck_time("18:50");
        tck.setTck_numTickRed("321");
        tck.setTck_price("30");
        //   tck.setContadorIteraciones(contadorIteraciones);
        tck.setTck_number(String.valueOf(1));
    //    tck.setTck_cus_id(null);
        return tck;
    }
*/
  /*  private Trip buildTrip(){
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

    private String eliminarEspaciosEnBlanco(String st){
        String sol = "";
        for (int x=0; x < st.length(); x++) {
            if (st.charAt(x) != ' ')
                sol += st.charAt(x);
        }
        return sol;
    }

    private int sacarEntero(String url){
        String delim2 = "/";  //File.separator;
        String[] temp1;
        temp1 = url.split(delim2);
        url = temp1[temp1.length-1];

        String del = "_";
        String[] temp2;
        temp2 = url.split(del);
        url = temp2[temp2.length-1];

        String delim21 = ".pdf";  //File.separator;
        String[] temp12;
        temp12 = url.split(delim21);
        url = temp12[temp12.length-1];

        int cont = 0;
        int ini = url.indexOf("R");
        int fin = url.indexOf("-");

        //System.out.println(url.substring(ini+1, fin));
        cont = Integer.valueOf(url.substring(ini+1, fin));
        return cont;
    }

    private int getYear(String nombre){
        String del = "_";
        String[] temp2;
        temp2 = nombre.split(del);
        nombre = temp2[temp2.length-1];

        int cont = 0;
        int ini = nombre.indexOf("-");
        int fin = nombre.indexOf(".pdf");

        //System.out.println(url.substring(ini+1, fin));
        cont = Integer.valueOf(nombre.substring(ini+1, fin));
        return cont;
    }

    private String takeYear(String fecha){
        String delimiter = "/";
        String[] temp;
        temp = fecha.split(delimiter);
        return temp[temp.length-1];
    }

    private String insertarYear(String fecha){
        String year = takeYear(fecha);
        /** First of all, we check if there is an inserted row with the year of this date.
         * If so, use the id of the year.
         * If not, insert a row in the Years table and use the id.
         *
         * Si en la tabla de tickets/invoices we have rows with this year id, get the count of inserted rows in the table
         *  so we can update the numTickRed o numFactRed.
         */
/*        return null;
    }
    */
}
