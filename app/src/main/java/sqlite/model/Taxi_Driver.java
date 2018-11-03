package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Taxi_Driver {

    public static final String TAG = Taxi_Driver.class.getSimpleName();
    public static final String TABLE = "Taxi_Driver";
    // Labels Table Columns names
    public static final String KEY_txd_id = "txd_id";
    public static final String KEY_txd_deleted = "txd_deleted";
    public static final String KEY_txd_email = "txd_email";
    public static final String KEY_txd_confirmationCode = "txd_confirmationCode";
    public static final String KEY_txd_confirmationCodeEncoded = "txd_confirmationCodeEncoded";
    public static final String KEY_txd_logLoaded = "txd_logLoaded";
    public static final String KEY_txd_tda_id = "txd_tda_id";

    private int txd_id;
    private String txd_deleted;
    private String txd_email;
    private String txd_confirmationCode;
    private String txd_confirmationCodeEncoded;
    private String txd_logLoaded;
    private String txd_tda_id;

    public Taxi_Driver(){

    }

    public Taxi_Driver(int txd_id, String txd_deleted, String txd_email, String txd_confirmationCode,
                       String txd_confirmationCodeEncoded, String txd_activated,
                       String txd_logLoaded, String txd_tda_id) {
        this.txd_id = txd_id;
        this.txd_deleted = txd_deleted;
        this.txd_email = txd_email;
        this.txd_confirmationCode = txd_confirmationCode;
        this.txd_confirmationCodeEncoded = txd_confirmationCodeEncoded;
        this.txd_logLoaded = txd_logLoaded;
        this.txd_tda_id = txd_tda_id;
    }

    public int getTxd_id() {
        return txd_id;
    }

    public void setTxd_id(int txd_id) {
        this.txd_id = txd_id;
    }

    public String getTxd_deleted() {
        return txd_deleted;
    }

    public void setTxd_deleted(String txd_deleted) {
        this.txd_deleted = txd_deleted;
    }

    public String getTxd_email() {
        return txd_email;
    }

    public void setTxd_email(String txd_email) {
        this.txd_email = txd_email;
    }

    public String getTxd_confirmationCode() {
        return txd_confirmationCode;
    }

    public void setTxd_confirmationCode(String txd_confirmationCode) {
        this.txd_confirmationCode = txd_confirmationCode;
    }

    public String getTxd_tda_id() {
        return txd_tda_id;
    }

    public void setTxd_tda_id(String txd_tda_id) {
        this.txd_tda_id = txd_tda_id;
    }

    public String getTxd_logLoaded() {
        return txd_logLoaded;
    }

    public void setTxd_logLoaded(String txd_logLoaded) {
        this.txd_logLoaded = txd_logLoaded;
    }

    public String getTxd_confirmationCodeEncoded() {
        return txd_confirmationCodeEncoded;
    }

    public void setTxd_confirmationCodeEncoded(String txd_confirmationCodeEncoded) {
        this.txd_confirmationCodeEncoded = txd_confirmationCodeEncoded;
    }
}
