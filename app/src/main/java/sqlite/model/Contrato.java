package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Contrato {

    public static final String TAG = Contrato.class.getSimpleName();
    public static final String TABLE = "Contrato";
    // Labels Table Columns names
    public static final String KEY_con_id = "con_id";
    public static final String KEY_con_deleted = "con_deleted";
    public static final String KEY_con_number = "con_number";
    public static final String KEY_con_date = "con_date";
    public static final String KEY_con_matricula_vehiculo = "con_matricula_vehiculo";
    public static final String KEY_con_tda_id = "con_tda_id";
    public static final String KEY_con_yea_id = "con_yea_id";
    public static final String KEY_con_alias = "con_alias";
    public static final String KEY_con_arrendador = "con_arrendador";
    public static final String KEY_con_nif_arrendador = "con_nif_arrendador";
    public static final String KEY_con_isTda_arrendador = "con_isTda_arrendador";
    public static final String KEY_con_arrendatario = "con_arrendatario"; //Estos datos del arrendatario no se usaran, solo para actualizacion de tabla y evitar constraint failed
    public static final String KEY_con_arrendatario_nifCif = "con_arrendatario_nifCif";

    private int con_id;
    private String con_deleted;
    private String con_number;
    private String con_date;
    private String con_tda_id;
    private String con_yea_id;
    private String con_matricula_vehiculo;
    private String con_alias;
    private String con_arrendador;
    private String con_nif_arrendador;
    private String con_isTda_arrendador;
    private String con_arrendatario;
    private String con_arrendatario_nifCif;

    public Contrato(){

    }

    public Contrato(int con_id, String con_deleted, String con_number, String con_date, String con_tda_id, String con_yea_id,
                    String con_matricula_vehiculo, String con_alias, String con_arrendador, String con_nif_arrendador, String con_isTda_arrendador) {
        this.con_id = con_id;
        this.con_deleted = con_deleted;
        this.con_number = con_number;
        this.con_date = con_date;
        this.con_tda_id = con_tda_id;
        this.con_yea_id = con_yea_id;
        this.con_matricula_vehiculo = con_matricula_vehiculo;
        this.con_alias = con_alias;
        this.con_arrendador = con_arrendador;
        this.con_nif_arrendador = con_nif_arrendador;
        this.con_isTda_arrendador = con_isTda_arrendador;
    }

    public int getCon_id() {
        return con_id;
    }

    public void setCon_id(int con_id) {
        this.con_id = con_id;
    }

    public String getCon_deleted() {
        return con_deleted;
    }

    public void setCon_deleted(String con_deleted) {
        this.con_deleted = con_deleted;
    }

    public String getCon_number() {
        return con_number;
    }

    public void setCon_number(String con_number) {
        this.con_number = con_number;
    }

    public String getCon_tda_id() {
        return con_tda_id;
    }

    public void setCon_tda_id(String con_tda_id) {
        this.con_tda_id = con_tda_id;
    }

    public String getCon_date() {
        return con_date;
    }

    public void setCon_date(String con_date) {
        this.con_date = con_date;
    }

    public String getCon_yea_id() {
        return con_yea_id;
    }

    public void setCon_yea_id(String con_yea_id) {
        this.con_yea_id = con_yea_id;
    }

    public String getCon_matricula_vehiculo() {
        return con_matricula_vehiculo;
    }

    public void setCon_matricula_vehiculo(String con_matricula_vehiculo) {
        this.con_matricula_vehiculo = con_matricula_vehiculo;
    }

    public String getCon_alias() {
        return con_alias;
    }

    public void setCon_alias(String con_alias) {
        this.con_alias = con_alias;
    }

    public String getCon_arrendador() {
        return con_arrendador;
    }

    public void setCon_arrendador(String con_arrendador) {
        this.con_arrendador = con_arrendador;
    }

    public String getCon_nif_arrendador() {
        return con_nif_arrendador;
    }

    public void setCon_nif_arrendador(String con_nif_arrendador) {
        this.con_nif_arrendador = con_nif_arrendador;
    }

    /*Estos dos getter/setter y constructor no se usan*/
    public String getCon_arrendatario() {
        return con_arrendatario;
    }

    public void setCon_arrendatario(String con_arrendatario) {
        this.con_arrendatario = con_arrendatario;
    }

    public String getCon_arrendatario_nifCif() {
        return con_arrendatario_nifCif;
    }

    public void setCon_arrendatario_nifCif(String con_arrendatario_nifCif) {
        this.con_arrendatario_nifCif = con_arrendatario_nifCif;
    }

    public String getCon_isTda_arrendador() {
        return con_isTda_arrendador;
    }

    public void setCon_isTda_arrendador(String con_isTda_arrendador) {
        this.con_isTda_arrendador = con_isTda_arrendador;
    }
}
