package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Years {

    public static final String TAG = Years.class.getSimpleName();
    public static final String TABLE = "Years";
    // Labels Table Columns names
    public static final String KEY_yea_id = "yea_id";
    public static final String KEY_yea_deleted = "yea_deleted";
    public static final String KEY_yea_dateYear = "yea_dateYear";
    public static final String KEY_yea_ticket = "yea_ticket";
    public static final String KEY_yea_invoice = "yea_invoice";
    public static final String KEY_yea_presupuesto = "yea_presupuesto";
    public static final String KEY_yea_hojaRuta = "yea_hojaRuta";
    public static final String KEY_yea_contrato = "yea_contrato";

    private int yea_id;
    private String yea_deleted;
    private String yea_dateYear;
    private String yea_ticket;
    private String yea_invoice;
    private String yea_presupuesto;
    private String yea_hojaRuta;
    private String yea_contrato;

    public Years(){

    }

    public Years(int yea_id, String yea_deleted, String yea_dateYear, String yea_ticket,
                 String yea_invoice, String yea_presupuesto, String yea_hojaRuta, String yea_contrato) {
        this.yea_id = yea_id;
        this.yea_deleted = yea_deleted;
        this.yea_dateYear = yea_dateYear;
        this.yea_invoice = yea_invoice;
        this.yea_ticket = yea_ticket;
        this.yea_presupuesto = yea_presupuesto;
        this.yea_hojaRuta = yea_hojaRuta;
        this.yea_contrato = yea_contrato;
    }

    public int getYea_id() {
        return yea_id;
    }

    public void setYea_id(int yea_id) {
        this.yea_id = yea_id;
    }

    public String getYea_deleted() {
        return yea_deleted;
    }

    public void setYea_deleted(String yea_deleted) {
        this.yea_deleted = yea_deleted;
    }

    public String getYea_dateYear() {
        return yea_dateYear;
    }

    public void setYea_dateYear(String yea_dateYear) {
        this.yea_dateYear = yea_dateYear;
    }

    public String getYea_ticket() {
        return yea_ticket;
    }

    public void setYea_ticket(String yea_ticket) {
        this.yea_ticket = yea_ticket;
    }

    public String getYea_invoice() {
        return yea_invoice;
    }

    public void setYea_invoice(String yea_invoice) {
        this.yea_invoice = yea_invoice;
    }

    public String getYea_presupuesto() {
        return yea_presupuesto;
    }

    public void setYea_presupuesto(String yea_presupuesto) {
        this.yea_presupuesto = yea_presupuesto;
    }

    public String getYea_hojaRuta() {
        return yea_hojaRuta;
    }

    public void setYea_hojaRuta(String yea_hojaRuta) {
        this.yea_hojaRuta = yea_hojaRuta;
    }

    public String getYea_contrato() {
        return yea_contrato;
    }

    public void setYea_contrato(String yea_contrato) {
        this.yea_contrato = yea_contrato;
    }
}
