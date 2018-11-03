package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Trip {

    public static final String TAG = Trip.class.getSimpleName();
    public static final String TABLE = "Trip";
    // Labels Table Columns names
    public static final String KEY_trp_id = "trp_id";
    public static final String KEY_trp_deleted = "trp_deleted";
    public static final String KEY_trp_start = "trp_start";
    public static final String KEY_trp_finish= "trp_finish";
    public static final String KEY_trp_price= "trp_price";
    public static final String KEY_trp_number= "trp_number"; //Nº ticket o factura reducido
    public static final String KEY_trp_start_place = "trp_start_place";

    private int trp_id;
    private String trp_deleted;
    private String trp_start;  //origen trayecto
    private String trp_finish;  //destino trayecto
    private String trp_price;
    private String trp_number;
    private String trp_start_place;

    public Trip(){

    }

    public Trip(int trp_id, String trp_deleted, String trp_start, String trp_finish,
                String trp_price, String trp_number, String trp_start_place) {
        this.trp_id = trp_id;
        this.trp_deleted = trp_deleted;
        this.trp_start = trp_start;
        this.trp_finish = trp_finish;
        this.trp_price = trp_price;
        this.trp_number = trp_number;
        this.trp_start_place = trp_start_place;
    }

    public int getTrp_id() {
        return trp_id;
    }

    public void setTrp_id(int trp_id) {
        this.trp_id = trp_id;
    }

    public String getTrp_deleted() {
        return trp_deleted;
    }

    public void setTrp_deleted(String trp_deleted) {
        this.trp_deleted = trp_deleted;
    }

    public String getTrp_start() {
        return trp_start;
    }

    public void setTrp_start(String trp_start) {
        this.trp_start = trp_start;
    }

    public String getTrp_finish() {
        return trp_finish;
    }

    public void setTrp_finish(String trp_finish) {
        this.trp_finish = trp_finish;
    }

    public String getTrp_price() {
        return trp_price;
    }

    public void setTrp_price(String trp_price) {
        this.trp_price = trp_price;
    }

    public String getTrp_number() {
        return trp_number;
    }

    public void setTrp_number(String trp_number) {
        this.trp_number = trp_number;
    }

    public String getTrp_start_place() {
        return trp_start_place;
    }

    public void setTrp_start_place(String trp_start_place) {
        this.trp_start_place = trp_start_place;
    }
}
