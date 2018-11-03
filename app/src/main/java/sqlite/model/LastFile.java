package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class LastFile {

    public static final String TAG = LastFile.class.getSimpleName();
    public static final String TABLE = "LastFile";
    // Labels Table Columns names
    public static final String KEY_lfi_id = "lfi_id";
    public static final String KEY_lfi_deleted = "lfi_deleted";
    public static final String KEY_lfi_tck_id = "lfi_tck_id";
    public static final String KEY_lfi_inv_id = "lfi_inv_id";
    public static final String KEY_lfi_pre_id = "lfi_pre_id";
//    public static final String KEY_lfi_hojaRuta = "lfi_hojaRuta";

    private int lfi_id;
    private String lfi_deleted;
    private String lfi_tck_id;
    private String lfi_inv_id;
    private String lfi_pre_id;
 //   private String lfi_hojaRuta;

    public LastFile(){

    }

    public LastFile(int lfi_id, String lfi_deleted, String lfi_tck_id, String lfi_inv_id, String lfi_presupuesto) {
        this.lfi_id = lfi_id;
        this.lfi_deleted = lfi_deleted;
        this.lfi_tck_id = lfi_tck_id;
        this.lfi_inv_id = lfi_inv_id;
        this.lfi_pre_id = lfi_pre_id;
     //   this.lfi_hojaRuta = lfi_hojaRuta;
    }

    public int getLfi_id() {
        return lfi_id;
    }

    public void setLfi_id(int lfi_id) {
        this.lfi_id = lfi_id;
    }

    public String getLfi_deleted() {
        return lfi_deleted;
    }

    public void setLfi_deleted(String lfi_deleted) {
        this.lfi_deleted = lfi_deleted;
    }

    public String getLfi_tck_id() {
        return lfi_tck_id;
    }

    public void setLfi_tck_id(String lfi_tck_id) {
        this.lfi_tck_id = lfi_tck_id;
    }

    public String getLfi_inv_id() {
        return lfi_inv_id;
    }

    public void setLfi_inv_id(String lfi_inv_id) {
        this.lfi_inv_id = lfi_inv_id;
    }

    public String getLfi_pre_id() {
        return lfi_pre_id;
    }

    public void setLfi_pre_id(String lfi_pre_id) {
        this.lfi_pre_id = lfi_pre_id;
    }

    /*   public String getLfi_hojaRuta() {
        return lfi_hojaRuta;
    }

    public void setLfi_hojaRuta(String lfi_hojaRuta) {
        this.lfi_hojaRuta = lfi_hojaRuta;
    }  */
}
