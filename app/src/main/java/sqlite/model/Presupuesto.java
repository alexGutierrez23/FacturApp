package sqlite.model;

import java.io.Serializable;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Presupuesto implements Serializable{

    public static final String TAG = Presupuesto.class.getSimpleName();
    public static final String TABLE = "Presupuesto";
    // Labels Table Columns names
    public static final String KEY_pre_id = "pre_id";
    public static final String KEY_pre_deleted = "pre_deleted";
    public static final String KEY_pre_number = "pre_number";
    public static final String KEY_pre_presupuestoNumber = "pre_presupuestoNumber";
    public static final String KEY_pre_numPresupuestoRed= "pre_numPresupuestoRed";
    public static final String KEY_pre_date= "pre_date";
    public static final String KEY_pre_emailed= "pre_emailed";
    public static final String KEY_pre_time= "pre_time";
    public static final String KEY_pre_pdf= "pre_pdf";
    public static final String KEY_pre_tda_id= "pre_tda_id";
    public static final String KEY_pre_tck_id= "pre_tck_id";
    public static final String KEY_pre_cus_id= "pre_cus_id";
    public static final String KEY_pre_yea_id = "pre_yea_id";

    private int pre_id;
    private String pre_deleted;
    private String pre_number;
    private String pre_presupuestoNumber;
    private String pre_numPresupuestoRed;
    private String pre_date;
    private String pre_emailed;
    private String pre_time;
    private String pre_pdf;
    private String pre_tda_id;
    private String pre_tck_id;
    private String pre_cus_id;
    private String pre_yea_id;

    public Presupuesto(){

    }

    public Presupuesto(int pre_id, String pre_deleted, String pre_number, String pre_date,
                       String pre_emailed, String pre_time, String pre_pdf, String pre_tda_id, String pre_tck_id,
                       String pre_cus_id, String pre_numPresupuestoRed, String pre_yea_id, String pre_presupuestoNumber) {
        this.pre_id = pre_id;
        this.pre_deleted = pre_deleted;
        this.pre_number = pre_number;
        this.pre_presupuestoNumber = pre_presupuestoNumber;
        this.pre_date = pre_date;
        this.pre_emailed = pre_emailed;
        this.pre_time = pre_time;
        this.pre_tda_id = pre_tda_id;
        this.pre_tck_id = pre_tck_id;
        this.pre_cus_id = pre_cus_id;
        this.pre_numPresupuestoRed = pre_numPresupuestoRed;
        this.pre_yea_id = pre_yea_id;
        this.pre_pdf = pre_pdf;
    }

    public int getPre_id() {
        return pre_id;
    }

    public void setPre_id(int pre_id) {
        this.pre_id = pre_id;
    }

    public String getPre_deleted() {
        return pre_deleted;
    }

    public void setPre_deleted(String pre_deleted) {
        this.pre_deleted = pre_deleted;
    }

    public String getPre_number() {
        return pre_number;
    }

    public void setPre_number(String pre_number) {
        this.pre_number = pre_number;
    }

    public String getPre_numPresupuestoRed() {
        return pre_numPresupuestoRed;
    }

    public void setPre_numPresupuestoRed(String pre_numPresupuestoRed) {
        this.pre_numPresupuestoRed = pre_numPresupuestoRed;
    }

    public String getPre_date() {
        return pre_date;
    }

    public void setPre_date(String pre_date) {
        this.pre_date = pre_date;
    }

    public String getPre_emailed() {
        return pre_emailed;
    }

    public void setPre_emailed(String pre_emailed) {
        this.pre_emailed = pre_emailed;
    }

    public String getPre_time() {
        return pre_time;
    }

    public void setPre_time(String pre_time) {
        this.pre_time = pre_time;
    }

    public String getPre_tda_id() {
        return pre_tda_id;
    }

    public void setPre_tda_id(String pre_tda_id) {
        this.pre_tda_id = pre_tda_id;
    }

    public String getPre_tck_id() {
        return pre_tck_id;
    }

    public void setPre_tck_id(String pre_tck_id) {
        this.pre_tck_id = pre_tck_id;
    }

    public String getPre_cus_id() {
        return pre_cus_id;
    }

    public void setPre_cus_id(String pre_cus_id) {
        this.pre_cus_id = pre_cus_id;
    }

    public String getPre_yea_id() {
        return pre_yea_id;
    }

    public void setPre_yea_id(String pre_yea_id) {
        this.pre_yea_id = pre_yea_id;
    }

    public String getPre_presupuestoNumber() {
        return pre_presupuestoNumber;
    }

    public void setPre_presupuestoNumber(String pre_presupuestoNumber) {
        this.pre_presupuestoNumber = pre_presupuestoNumber;
    }

    public String getPre_pdf() {
        return pre_pdf;
    }

    public void setPre_pdf(String pre_pdf) {
        this.pre_pdf = pre_pdf;
    }
}
