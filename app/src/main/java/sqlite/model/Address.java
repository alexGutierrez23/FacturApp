package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Address {

    public static final String TAG = Address.class.getSimpleName();
    public static final String TABLE = "Address";
    // Labels Table Columns names
    public static final String KEY_adr_id = "adr_id";
    public static final String KEY_adr_deleted = "adr_deleted";
    public static final String KEY_adr_street = "adr_street";
    public static final String KEY_adr_town= "adr_town";   //municipio
    public static final String KEY_adr_county= "adr_county";   //provincia
    public static final String KEY_adr_number= "adr_number";
    public static final String KEY_adr_postcode= "adr_postcode";
    public static final String KEY_adr_country= "adr_country";

    private int adr_id;
    private String adr_deleted;
    private String adr_street;
    private String adr_town;   //municipio
    private String adr_county;   //provincia
    private String adr_number;
    private String adr_postcode;
    private String adr_country;

    public Address(){

    }

    public Address(int adr_id, String adr_deleted, String adr_street, String adr_town,
                   String adr_county, String adr_number, String adr_postcode, String adr_country) {
        this.adr_id = adr_id;
        this.adr_deleted = adr_deleted;
        this.adr_street = adr_street;
        this.adr_town = adr_town;
        this.adr_county = adr_county;
        this.adr_number = adr_number;
        this.adr_postcode = adr_postcode;
        this.adr_country = adr_country;
    }

    public int getAdr_id() {
        return adr_id;
    }

    public void setAdr_id(int adr_id) {
        this.adr_id = adr_id;
    }

    public String getAdr_deleted() {
        return adr_deleted;
    }

    public void setAdr_deleted(String adr_deleted) {
        this.adr_deleted = adr_deleted;
    }

    public String getAdr_street() {
        return adr_street;
    }

    public void setAdr_street(String adr_street) {
        this.adr_street = adr_street;
    }

    public String getAdr_town() {
        return adr_town;
    }

    public void setAdr_town(String adr_town) {
        this.adr_town = adr_town;
    }

    public String getAdr_county() {
        return adr_county;
    }

    public void setAdr_county(String adr_county) {
        this.adr_county = adr_county;
    }

    public String getAdr_number() {
        return adr_number;
    }

    public void setAdr_number(String adr_number) {
        this.adr_number = adr_number;
    }

    public String getAdr_postcode() {
        return adr_postcode;
    }

    public void setAdr_postcode(String adr_postcode) {
        this.adr_postcode = adr_postcode;
    }

    public String getAdr_country() {
        return adr_country;
    }

    public void setAdr_country(String adr_country) {
        this.adr_country = adr_country;
    }

}
