package Constants;

import android.Manifest;

/**
 * Created by Usuario on 16/01/2018.
 */

public class Constants {
    public static final String NOMBRE_CARPETA_APP = "Servicio Transporte";

    public static final String GENERADOS_FACTURAS = "Mis_Facturas";
    public static final String GENERADOS_TICKETS = "Mis_Tickets";
    public static final String GENERADOS_PRESUPUESTOS = "Mis_Presupuestos";
    public static final String GENERADOS_RENDIMIENTOS = "Mis_Rendimientos";
    public static final String GENERADOS_HOJASRUTA = "Mis_HojasDeRuta";

    public static final String GUIAS = "Guias";

    public static final String TICKET_TYPE = "Ticket";
    public static final String FACTURA_TYPE = "Factura";
    public static final String PRESUPUESTO_TYPE = "Presupuesto";
    public static final String HOJARUTA_TYPE = "HojaRuta";
    public static final String RENDIMIENTO_TICKET_TYPE = "RendimientoTicket";
    public static final String RENDIMIENTO_FACTURA_TYPE = "RendimientoFactura";

    public static final String TICKETS_LOGS = "(NO BORRAR)log_tickets.txt";
    public static final String FACTURAS_LOGS = "(NO BORRAR)log_facturas.txt";
    public static final String PRESUPUESTOS_LOGS = "(NO BORRAR)log_presupuestos.txt";
    public static final String HOJASRUTA_LOGS = "(NO BORRAR)log_hojasRuta.txt";

    //Storage permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String TICKET_TAB = "TICKETS";
    public static final String FACTURA_TAB = "FACTURAS";
    public static final String PRESUPUESTO_TAB = "PRESUPUESTOS";
}
