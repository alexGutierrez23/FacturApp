package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Customer {

    public static final String TAG = Customer.class.getSimpleName();
    public static final String TABLE = "Customer";
    // Labels Table Columns names
    public static final String KEY_cus_id = "cus_id";
    public static final String KEY_cus_deleted = "cus_deleted";
    public static final String KEY_cus_email = "cus_email";
    public static final String KEY_cus_name= "cus_name";   //municipio
    public static final String KEY_cus_surname= "cus_surname";   //provincia
    public static final String KEY_cus_nif= "cus_nif";
    public static final String KEY_cus_adr_id= "cus_adr_id";

    private int cus_id;//Customer es el viajero
    private String cus_deleted;
    private String cus_email;
    private String cus_name;
    private String cus_surname;
    private String cus_nif;
    private String cus_adr_id;

    public Customer(){

    }

    public Customer(int cus_id, String cus_deleted, String cus_email, String cus_name,
                    String cus_surname, String cus_nif, String cus_adr_id) {
        this.cus_id = cus_id;
        this.cus_deleted = cus_deleted;
        this.cus_email = cus_email;
        this.cus_name = cus_name;
        this.cus_surname = cus_surname;
        this.cus_nif = cus_nif;
        this.cus_adr_id = cus_adr_id;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getCus_deleted() {
        return cus_deleted;
    }

    public void setCus_deleted(String cus_deleted) {
        this.cus_deleted = cus_deleted;
    }

    public String getCus_email() {
        return cus_email;
    }

    public void setCus_email(String cus_email) {
        this.cus_email = cus_email;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_surname() {
        return cus_surname;
    }

    public void setCus_surname(String cus_surname) {
        this.cus_surname = cus_surname;
    }

    public String getCus_nif() {
        return cus_nif;
    }

    public void setCus_nif(String cus_nif) {
        this.cus_nif = cus_nif;
    }

    public String getCus_adr_id() {
        return cus_adr_id;
    }

    public void setCus_adr_id(String cus_adr_id) {
        this.cus_adr_id = cus_adr_id;
    }
}
