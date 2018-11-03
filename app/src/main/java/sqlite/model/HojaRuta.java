package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class HojaRuta {

    public static final String TAG = HojaRuta.class.getSimpleName();
    public static final String TABLE = "HojaRuta";
    // Labels Table Columns names
    public static final String KEY_hjr_id = "hjr_id";
    public static final String KEY_hjr_deleted = "hjr_deleted";
    public static final String KEY_hjr_number = "hjr_number";
    public static final String KEY_hjr_date_inicio = "hjr_date_inicio";
    public static final String KEY_hjr_date_fin = "hjr_date_fin";
    public static final String KEY_hjr_time_inicio = "hjr_time_inicio";
    public static final String KEY_hjr_time_fin = "hjr_time_fin";
    public static final String KEY_hjr_trp_id = "hjr_trp_id";
    public static final String KEY_hjr_pasajeros = "hjr_pasajeros";
    public static final String KEY_hjr_observaciones = "hjr_observaciones";
    public static final String KEY_hjr_con_id = "hjr_con_id";
    public static final String KEY_hjr_ard_id = "hjr_ard_id";
    public static final String KEY_hjr_pathPdf = "hjr_pathPdf";
    public static final String KEY_hjr_alias = "hjr_alias";

    private int hjr_id;
    private String hjr_deleted;
    private String hjr_number;
    private String hjr_date_inicio;
    private String hjr_date_fin;
    private String hjr_time_inicio;
    private String hjr_time_fin;
    private String hjr_trp_id;
    private String hjr_pasajeros;
    private String hjr_observaciones;
    private String hjr_con_id;
    private String hjr_pathPdf;
    private String hjr_alias;
    private String hjr_ard_id;

    public HojaRuta(){

    }

    public HojaRuta(int hjr_id, String hjr_deleted, String hjr_number, String hjr_date_inicio,
                    String hjr_date_fin, String hjr_time_inicio, String hjr_time_fin, String hjr_trp_id,
                    String hjr_pasajeros, String hjr_observaciones, String hjr_con_id, String hjr_pathPdf,
                    String hjr_alias, String hjr_ard_id) {
        this.hjr_id = hjr_id;
        this.hjr_deleted = hjr_deleted;
        this.hjr_number = hjr_number;
        this.hjr_date_inicio = hjr_date_inicio;
        this.hjr_date_fin = hjr_date_fin;
        this.hjr_time_inicio = hjr_time_inicio;
        this.hjr_time_fin = hjr_time_fin;
        this.hjr_trp_id = hjr_trp_id;
        this.hjr_pasajeros = hjr_pasajeros;
        this.hjr_observaciones = hjr_observaciones;
        this.hjr_con_id = hjr_con_id;
        this.hjr_pathPdf = hjr_pathPdf;
        this.hjr_alias = hjr_alias;
        this.hjr_ard_id = hjr_ard_id;
    }

    public int getHjr_id() {
        return hjr_id;
    }

    public void setHjr_id(int hjr_id) {
        this.hjr_id = hjr_id;
    }

    public String getHjr_deleted() {
        return hjr_deleted;
    }

    public void setHjr_deleted(String hjr_deleted) {
        this.hjr_deleted = hjr_deleted;
    }

    public String getHjr_number() {
        return hjr_number;
    }

    public void setHjr_number(String hjr_number) {
        this.hjr_number = hjr_number;
    }

    public String getHjr_date_inicio() {
        return hjr_date_inicio;
    }

    public void setHjr_date_inicio(String hjr_date_inicio) {
        this.hjr_date_inicio = hjr_date_inicio;
    }

    public String getHjr_date_fin() {
        return hjr_date_fin;
    }

    public void setHjr_date_fin(String hjr_date_fin) {
        this.hjr_date_fin = hjr_date_fin;
    }

    public String getHjr_time_inicio() {
        return hjr_time_inicio;
    }

    public void setHjr_time_inicio(String hjr_time_inicio) {
        this.hjr_time_inicio = hjr_time_inicio;
    }

    public String getHjr_time_fin() {
        return hjr_time_fin;
    }

    public void setHjr_time_fin(String hjr_time_fin) {
        this.hjr_time_fin = hjr_time_fin;
    }

    public String getHjr_pasajeros() {
        return hjr_pasajeros;
    }

    public void setHjr_pasajeros(String hjr_pasajeros) {
        this.hjr_pasajeros = hjr_pasajeros;
    }

    public String getHjr_observaciones() {
        return hjr_observaciones;
    }

    public void setHjr_observaciones(String hjr_observaciones) {
        this.hjr_observaciones = hjr_observaciones;
    }

    public String getHjr_con_id() {
        return hjr_con_id;
    }

    public void setHjr_con_id(String hjr_con_id) {
        this.hjr_con_id = hjr_con_id;
    }

    public String getHjr_pathPdf() {
        return hjr_pathPdf;
    }

    public void setHjr_pathPdf(String hjr_pathPdf) {
        this.hjr_pathPdf = hjr_pathPdf;
    }

    public String getHjr_trp_id() {
        return hjr_trp_id;
    }

    public void setHjr_trp_id(String hjr_trp_id) {
        this.hjr_trp_id = hjr_trp_id;
    }

    public String getHjr_alias() {
        return hjr_alias;
    }

    public void setHjr_alias(String hjr_alias) {
        this.hjr_alias = hjr_alias;
    }

    public String getHjr_ard_id() {
        return hjr_ard_id;
    }

    public void setHjr_ard_id(String hjr_ard_id) {
        this.hjr_ard_id = hjr_ard_id;
    }
}
