package sqlite.model;

/**
 * Created by Usuario on 06/04/2017.
 */

public class Stop {

    public static final String TAG = Stop.class.getSimpleName();
    public static final String TABLE = "Stop";
    // Labels Table Columns names
    public static final String KEY_stp_id = "stp_id";
    public static final String KEY_stp_deleted = "stp_deleted";
    public static final String KEY_stp_place = "stp_place";
    public static final String KEY_stp_hjr_id = "stp_hjr_id";

    private int stp_id;
    private String stp_deleted;
    private String stp_place;
    private String stp_hjr_id;

    public Stop(){

    }

    public Stop(int stp_id, String stp_deleted, String stp_place, String stp_hjr_id) {
        this.stp_id = stp_id;
        this.stp_deleted = stp_deleted;
        this.stp_place = stp_place;
        this.stp_hjr_id = stp_hjr_id;
    }

    public int getStp_id() {
        return stp_id;
    }

    public void setStp_id(int stp_id) {
        this.stp_id = stp_id;
    }

    public String getStp_deleted() {
        return stp_deleted;
    }

    public void setStp_deleted(String stp_deleted) {
        this.stp_deleted = stp_deleted;
    }

    public String getStp_place() {
        return stp_place;
    }

    public void setStp_place(String stp_place) {
        this.stp_place = stp_place;
    }

    public String getStp_hjr_id() {
        return stp_hjr_id;
    }

    public void setStp_hjr_id(String stp_hjr_id) {
        this.stp_hjr_id = stp_hjr_id;
    }
}
