package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Arrendatario {

    public static final String TAG = Arrendatario.class.getSimpleName();
    public static final String TABLE = "Arrendatario";
    // Labels Table Columns names
    public static final String KEY_ard_id = "ard_id";
    public static final String KEY_ard_deleted = "ard_deleted";
    public static final String KEY_ard_name = "ard_name"; //Datos del arrendatario
    public static final String KEY_ard_nifCif = "ard_nifCif";
    public static final String KEY_ard_fecha = "ard_fecha";
    public static final String KEY_ard_number = "ard_number";

    private int ard_id;
    private String ard_deleted;
    private String ard_name;
    private String ard_nifCif;
    private String ard_fecha;
    private String ard_number;

    public Arrendatario(){

    }

    public Arrendatario(int ard_id, String ard_deleted, String ard_name, String ard_nifCif, String ard_fecha, String ard_number) {
        this.ard_id =ard_id;
        this.ard_deleted = ard_deleted;
        this.ard_name = ard_name;
        this.ard_nifCif = ard_nifCif;
        this.ard_fecha = ard_fecha;
        this.ard_number = ard_number;
    }

    public int getArd_id() {
        return ard_id;
    }

    public void setArd_id(int ard_id) {
        this.ard_id = ard_id;
    }

    public String getArd_deleted() {
        return ard_deleted;
    }

    public void setArd_deleted(String ard_deleted) {
        this.ard_deleted = ard_deleted;
    }

    public String getArd_name() {
        return ard_name;
    }

    public void setArd_name(String ard_name) {
        this.ard_name = ard_name;
    }

    public String getArd_nifCif() {
        return ard_nifCif;
    }

    public void setArd_nifCif(String ard_nifCif) {
        this.ard_nifCif = ard_nifCif;
    }

    public String getArd_fecha() {
        return ard_fecha;
    }

    public void setArd_fecha(String ard_fecha) {
        this.ard_fecha = ard_fecha;
    }

    public String getArd_number() {
        return ard_number;
    }

    public void setArd_number(String ard_number) {
        this.ard_number = ard_number;
    }
}
