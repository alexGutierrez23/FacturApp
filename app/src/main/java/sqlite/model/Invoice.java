package sqlite.model;

import java.io.Serializable;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Invoice implements Serializable{

    public static final String TAG = Invoice.class.getSimpleName();
    public static final String TABLE = "Invoice";
    // Labels Table Columns names
    public static final String KEY_inv_id = "inv_id";
    public static final String KEY_inv_deleted = "inv_deleted";
    public static final String KEY_inv_number = "inv_number";
    public static final String KEY_inv_invoiceNumber = "inv_invoiceNumber";
    public static final String KEY_inv_numFactRed= "inv_numFactRed";
    public static final String KEY_inv_date= "inv_date";
    public static final String KEY_inv_price= "inv_price";
    public static final String KEY_inv_emailed= "inv_emailed";
    public static final String KEY_inv_time= "inv_time";
    public static final String KEY_inv_pdf = "inv_pdf";
    public static final String KEY_inv_tda_id= "inv_tda_id";
    public static final String KEY_inv_tck_id= "inv_tck_id";
    public static final String KEY_inv_cus_id= "inv_cus_id";
    public static final String KEY_inv_yea_id = "inv_yea_id";

    private int inv_id;
    private String inv_deleted;
    private String inv_number;
    private String inv_invoiceNumber;
    private String inv_numFactRed;
    private String inv_date;
    private String inv_price;
    private String inv_emailed;
    private String inv_time;
    private String inv_pdf;
    private String inv_tda_id;
    private String inv_tck_id;
    private String inv_cus_id;
    private String inv_yea_id;

    public Invoice(){

    }

    public Invoice(int inv_id, String inv_deleted, String inv_number, String inv_date, String inv_price,
                   String inv_emailed, String inv_time, String inv_pdf, String inv_tda_id, String inv_tck_id,
                   String inv_cus_id, String inv_numFactRed, String inv_yea_id, String inv_invoiceNumber) {
        this.inv_id = inv_id;
        this.inv_deleted = inv_deleted;
        this.inv_number = inv_number;
        this.inv_date = inv_date;
        this.inv_price = inv_price;
        this.inv_emailed = inv_emailed;
        this.inv_time = inv_time;
        this.inv_pdf = inv_pdf;
        this.inv_tda_id = inv_tda_id;
        this.inv_tck_id = inv_tck_id;
        this.inv_cus_id = inv_cus_id;
        this.inv_numFactRed = inv_numFactRed;
        this.inv_yea_id = inv_yea_id;
        this.inv_invoiceNumber = inv_invoiceNumber;
    }

    public int getInv_id() {
        return inv_id;
    }

    public void setInv_id(int inv_id) {
        this.inv_id = inv_id;
    }

    public String getInv_deleted() {
        return inv_deleted;
    }

    public void setInv_deleted(String inv_deleted) {
        this.inv_deleted = inv_deleted;
    }

    public String getInv_number() {
        return inv_number;
    }

    public void setInv_number(String inv_number) {
        this.inv_number = inv_number;
    }

    public String getInv_date() {
        return inv_date;
    }

    public void setInv_date(String inv_date) {
        this.inv_date = inv_date;
    }

    public String getInv_emailed() {
        return inv_emailed;
    }

    public void setInv_emailed(String inv_emailed) {
        this.inv_emailed = inv_emailed;
    }

    public String getInv_time() {
        return inv_time;
    }

    public void setInv_time(String inv_time) {
        this.inv_time = inv_time;
    }

    public String getInv_tda_id() {
        return inv_tda_id;
    }

    public void setInv_tda_id(String inv_tda_id) {
        this.inv_tda_id = inv_tda_id;
    }

    public String getInv_tck_id() {
        return inv_tck_id;
    }

    public void setInv_tck_id(String inv_tck_id) {
        this.inv_tck_id = inv_tck_id;
    }

    public String getInv_cus_id() {
        return inv_cus_id;
    }

    public void setInv_cus_id(String inv_cus_id) {
        this.inv_cus_id = inv_cus_id;
    }

    public String getInv_numFactRed() {
        return inv_numFactRed;
    }

    public void setInv_numFactRed(String inv_numFactRed) {
        this.inv_numFactRed = inv_numFactRed;
    }

    public String getInv_yea_id() {
        return inv_yea_id;
    }

    public void setInv_yea_id(String inv_yea_id) {
        this.inv_yea_id = inv_yea_id;
    }

    public String getInv_invoiceNumber() {
        return inv_invoiceNumber;
    }

    public void setInv_invoiceNumber(String inv_invoiceNumber) {
        this.inv_invoiceNumber = inv_invoiceNumber;
    }

    public String getInv_pdf() {
        return inv_pdf;
    }

    public void setInv_pdf(String inv_pdf) {
        this.inv_pdf = inv_pdf;
    }

    public String getInv_price() {
        return inv_price;
    }

    public void setInv_price(String inv_price) {
        this.inv_price = inv_price;
    }
}
