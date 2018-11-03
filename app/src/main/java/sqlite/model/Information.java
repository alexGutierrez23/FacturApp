package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Information {

    public static final String TAG = Information.class.getSimpleName();
    public static final String TABLE = "Information";
    // Labels Table Columns names
    public static final String KEY_inf_id = "inf_id";
    public static final String KEY_inf_deleted = "inf_deleted";
    public static final String KEY_inf_additionalInfo = "inf_additionalInfo";
    public static final String KEY_inf_inv_id = "inf_inv_id";
    public static final String KEY_inf_pre_id = "inf_pre_id";
    public static final String KEY_inf_trp_id = "inf_trp_id";
    public static final String KEY_inf_vat_price= "inf_vat_price";
    public static final String KEY_inf_no_vat_price= "inf_no_vat_price";
    public static final String KEY_inf_vat= "inf_vat";
    public static final String KEY_inf_quantity= "inf_quantity";
    public static final String KEY_inf_item= "inf_item";

    private int inf_id;
    private String inf_deleted;
    private String inf_additionalInfo;
    private String inf_trp_id;
    private String inf_inv_id;
    private String inf_pre_id;
    private String inf_vat_price;
    private String inf_no_vat_price;
    private String inf_quantity;
    private String inf_item;
    private String inf_vat;

    public Information(){

    }

    public Information(int inf_id, String inf_deleted, String inf_additionalInfo, String inf_trp_id,
                       String inf_inv_id, String inf_pre_id, String inf_vat_price, String inf_no_vat_price,
                       String inf_quantity, String inf_item, String inf_vat) {
        this.inf_id = inf_id;
        this.inf_deleted = inf_deleted;
        this.inf_additionalInfo = inf_additionalInfo;
        this.inf_trp_id = inf_trp_id;
        this.inf_inv_id = inf_inv_id;
        this.inf_pre_id = inf_pre_id;
        this.inf_vat_price = inf_vat_price;
        this.inf_no_vat_price = inf_no_vat_price;
        this.inf_quantity = inf_quantity;
        this.inf_item = inf_item;
        this.inf_vat = inf_vat;
    }

    public int getInf_id() {
        return inf_id;
    }

    public void setInf_id(int inf_id) {
        this.inf_id = inf_id;
    }

    public String getInf_deleted() {
        return inf_deleted;
    }

    public void setInf_deleted(String inf_deleted) {
        this.inf_deleted = inf_deleted;
    }

    public String getInf_additionalInfo() {
        return inf_additionalInfo;
    }

    public void setInf_additionalInfo(String inf_additionalInfo) {
        this.inf_additionalInfo = inf_additionalInfo;
    }

    public String getInf_inv_id() {
        return inf_inv_id;
    }

    public void setInf_inv_id(String inf_inv_id) {
        this.inf_inv_id = inf_inv_id;
    }

    public String getInf_trp_id() {
        return inf_trp_id;
    }

    public void setInf_trp_id(String inf_trp_id) {
        this.inf_trp_id = inf_trp_id;
    }

    public String getInf_vat_price() {
        return inf_vat_price;
    }

    public void setInf_vat_price(String inf_vat_price) {
        this.inf_vat_price = inf_vat_price;
    }

    public String getInf_no_vat_price() {
        return inf_no_vat_price;
    }

    public void setInf_no_vat_price(String inf_no_vat_price) {
        this.inf_no_vat_price = inf_no_vat_price;
    }

    public String getInf_quantity() {
        return inf_quantity;
    }

    public void setInf_quantity(String inf_quantity) {
        this.inf_quantity = inf_quantity;
    }

    public String getInf_item() {
        return inf_item;
    }

    public void setInf_item(String inf_item) {
        this.inf_item = inf_item;
    }

    public String getInf_vat() {
        return inf_vat;
    }

    public void setInf_vat(String inf_vat) {
        this.inf_vat = inf_vat;
    }

    public String getInf_pre_id() {
        return inf_pre_id;
    }

    public void setInf_pre_id(String inf_pre_id) {
        this.inf_pre_id = inf_pre_id;
    }
}
