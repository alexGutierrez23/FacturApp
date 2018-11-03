package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Ticket {

    public static final String TAG = Ticket.class.getSimpleName();
    public static final String TABLE = "Ticket";
    // Labels Table Columns names
    public static final String KEY_tck_id = "tck_id";
    public static final String KEY_tck_deleted = "tck_deleted";
    public static final String KEY_tck_number = "tck_number";
    public static final String KEY_tck_ticketNumber = "tck_ticketNumber";
    public static final String KEY_tck_numTickRed= "tck_numTickRed";
    public static final String KEY_tck_date= "tck_date";
    public static final String KEY_tck_printed= "tck_printed";
    public static final String KEY_tck_emailed= "tck_emailed";
    public static final String KEY_tck_tda_id= "tck_tda_id";
    public static final String KEY_tck_time= "tck_time";
    public static final String KEY_tck_pdf= "tck_pdf";
    public static final String KEY_tck_trp_id= "tck_trp_id";
    public static final String KEY_tck_cus_id= "tck_cus_id";
    public static final String KEY_tck_price= "tck_price";
    public static final String KEY_tck_yea_id = "tck_yea_id";

    private int tck_id;
    private String tck_deleted;
    private String tck_number;
    private String tck_date;
    private String tck_printed;
    private String tck_emailed;
    private String tck_tda_id;
    private String tck_time;
    private String tck_pdf;
    private String tck_trp_id;
    private String tck_cus_id;
    private String tck_price;
    private String tck_numTickRed;
    private String tck_yea_id;
    private String tck_ticketNumber;

    public Ticket(){

    }

    public Ticket(int tck_id, String tck_deleted, String tck_number, String tck_date,
                  String tck_printed, String tck_emailed, String tck_tda_id, String tck_time, String tck_pdf,
                  String tck_trp_id, String tck_cus_id, String tck_price, String tck_numTickRed,
                  String tck_yea_id, String tck_ticketNumber) {
        this.tck_id = tck_id;
        this.tck_deleted = tck_deleted;
        this.tck_number = tck_number;
        this.tck_date = tck_date;
        this.tck_printed = tck_printed;
        this.tck_emailed = tck_emailed;
        this.tck_tda_id = tck_tda_id;
        this.tck_time = tck_time;
        this.tck_trp_id = tck_trp_id;
        this.tck_cus_id = tck_cus_id;
        this.tck_price = tck_price;
        this.tck_numTickRed = tck_numTickRed;
        this.tck_yea_id = tck_yea_id;
        this.tck_ticketNumber = tck_ticketNumber;
        this.tck_pdf = tck_pdf;
    }

    public int getTck_id() {
        return tck_id;
    }

    public void setTck_id(int tck_id) {
        this.tck_id = tck_id;
    }

    public String getTck_deleted() {
        return tck_deleted;
    }

    public void setTck_deleted(String tck_deleted) {
        this.tck_deleted = tck_deleted;
    }

    public String getTck_number() {
        return tck_number;
    }

    public void setTck_number(String tck_number) {
        this.tck_number = tck_number;
    }

    public String getTck_date() {
        return tck_date;
    }

    public void setTck_date(String tck_date) {
        this.tck_date = tck_date;
    }

    public String getTck_printed() {
        return tck_printed;
    }

    public void setTck_printed(String tck_printed) {
        this.tck_printed = tck_printed;
    }

    public String getTck_emailed() {
        return tck_emailed;
    }

    public void setTck_emailed(String tck_emailed) {
        this.tck_emailed = tck_emailed;
    }

    public String getTck_tda_id() {
        return tck_tda_id;
    }

    public void setTck_tda_id(String tck_tda_id) {
        this.tck_tda_id = tck_tda_id;
    }

    public String getTck_time() {
        return tck_time;
    }

    public void setTck_time(String tck_time) {
        this.tck_time = tck_time;
    }

    public String getTck_trp_id() {
        return tck_trp_id;
    }

    public void setTck_trp_id(String tck_trp_id) {
        this.tck_trp_id = tck_trp_id;
    }

    public String getTck_cus_id() {
        return tck_cus_id;
    }

    public void setTck_cus_id(String tck_cus_id) {
        this.tck_cus_id = tck_cus_id;
    }

    public String getTck_price() {
        return tck_price;
    }

    public void setTck_price(String tck_price) {
        this.tck_price = tck_price;
    }

    public String getTck_numTickRed() {
        return tck_numTickRed;
    }

    public void setTck_numTickRed(String tck_numTickRed) {
        this.tck_numTickRed = tck_numTickRed;
    }

    public String getTck_yea_id() {
        return tck_yea_id;
    }

    public void setTck_yea_id(String tck_yea_id) {
        this.tck_yea_id = tck_yea_id;
    }

    public String getTck_ticketNumber() {
        return tck_ticketNumber;
    }

    public void setTck_ticketNumber(String tck_ticketNumber) {
        this.tck_ticketNumber = tck_ticketNumber;
    }

    public String getTck_pdf() {
        return tck_pdf;
    }

    public void setTck_pdf(String tck_pdf) {
        this.tck_pdf = tck_pdf;
    }
}
