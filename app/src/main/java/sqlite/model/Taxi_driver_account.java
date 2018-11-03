package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Taxi_driver_account {

    public static final String TAG = Taxi_driver_account.class.getSimpleName();
    public static final String TABLE = "Taxi_driver_account";
    // Labels Table Columns names
    public static final String KEY_tda_id = "tda_id";
    public static final String KEY_tda_deleted = "tda_deleted";
    public static final String KEY_tda_name = "tda_name";
    public static final String KEY_tda_activated= "tda_activated";
    public static final String KEY_tda_enabled= "tda_enabled";
    public static final String KEY_tda_license_number= "tda_license_number";
    public static final String KEY_tda_email= "tda_email";
    public static final String KEY_tda_nif= "tda_nif";
    public static final String KEY_tda_adr_id = "tda_adr_id";
    public static final String KEY_tda_printerDevice = "tda_printerDevice";
    public static final String KEY_tda_mobile = "tda_mobile";
    public static final String KEY_tda_logo = "tda_logo";
    public static final String KEY_tda_logo_path = "tda_logo_path";

    private int tda_id;
    private String tda_deleted;
    private String tda_name;
    private String tda_activated;
    private String tda_enabled;
    private String tda_license_number;
    private String tda_email;
    private String tda_nif;
    private String tda_adr_id;
    private String tda_printerDevice;
    private String tda_mobile;
    private byte[] tda_logo;
    private String tda_logo_path;

    public Taxi_driver_account(){

    }

    public Taxi_driver_account(int tda_id, String tda_deleted, String tda_name,
                               String tda_activated, String tda_enabled,
                               String tda_license_number,
                               String tda_email, String tda_nif, String tda_adr_id,
                               String tda_printerDevice, String tda_mobile,
                               byte[] tda_logo, String tda_logo_path) {
        this.tda_id = tda_id;
        this.tda_deleted = tda_deleted;
        this.tda_name = tda_name;
        this.tda_activated = tda_activated;
        this.tda_enabled = tda_enabled;
        this.tda_license_number = tda_license_number;
        this.tda_email = tda_email;
        this.tda_nif = tda_nif;
        this.tda_adr_id = tda_adr_id;
        this.tda_printerDevice = tda_printerDevice;
        this.tda_mobile = tda_mobile;
        this.tda_logo = tda_logo;
        this.tda_logo_path = tda_logo_path;
    }

    public int getTda_id() {
        return tda_id;
    }

    public void setTda_id(int tda_id) {
        this.tda_id = tda_id;
    }

    public String getTda_deleted() {
        return tda_deleted;
    }

    public void setTda_deleted(String tda_deleted) {
        this.tda_deleted = tda_deleted;
    }

    public String getTda_name() {
        return tda_name;
    }

    public void setTda_name(String tda_name) {
        this.tda_name = tda_name;
    }

    public String getTda_activated() {
        return tda_activated;
    }

    public void setTda_activated(String tda_activated) {
        this.tda_activated = tda_activated;
    }

    public String getTda_enabled() {
        return tda_enabled;
    }

    public void setTda_enabled(String tda_enabled) {
        this.tda_enabled = tda_enabled;
    }

    public String getTda_license_number() {
        return tda_license_number;
    }

    public void setTda_license_number(String tda_license_number) {
        this.tda_license_number = tda_license_number;
    }

    public String getTda_email() {
        return tda_email;
    }

    public void setTda_email(String tda_email) {
        this.tda_email = tda_email;
    }

    public String getTda_nif() {
        return tda_nif;
    }

    public void setTda_nif(String tda_nif) {
        this.tda_nif = tda_nif;
    }

    public String getTda_adr_id() {
        return tda_adr_id;
    }

    public void setTda_adr_id(String tda_adr_id) {
        this.tda_adr_id = tda_adr_id;
    }

    public String getTda_printerDevice() {
        return tda_printerDevice;
    }

    public void setTda_printerDevice(String tda_printerDevice) {
        this.tda_printerDevice = tda_printerDevice;
    }

    public String getTda_mobile() {
        return tda_mobile;
    }

    public void setTda_mobile(String tda_mobile) {
        this.tda_mobile = tda_mobile;
    }

    public byte[] getTda_logo() {
        return tda_logo;
    }

    public void setTda_logo(byte[] tda_logo) {
        this.tda_logo = tda_logo;
    }

    public String getTda_logo_path() {
        return tda_logo_path;
    }

    public void setTda_logo_path(String tda_logo_path) {
        this.tda_logo_path = tda_logo_path;
    }
}
