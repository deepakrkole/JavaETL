package utils;

/**
 * Created by deepakkole on 6/15/16.
 */
public class PlainPojo {


    private String iso8601_timestamp;
    private String transaction_id;
    private String connection_type;
    private String device_type;
    private int imps;
    private int clicks;

    public synchronized String getTimestamp() {
        return iso8601_timestamp;
    }

    public synchronized void setTimestamp(String iso8601_timestamp) {
        this.iso8601_timestamp = iso8601_timestamp;
    }

    public synchronized String getTransaction_id() {
        return transaction_id;
    }

    public synchronized void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public synchronized String getConnection_type() {
        return connection_type;
    }

    public synchronized void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public synchronized String getDevice_type() {
        return device_type;
    }

    public synchronized void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public synchronized int getImps() {
        return imps;
    }

    public synchronized void setimps(int imps) {
        this.imps = imps;
    }

    public synchronized int getClicks() {
        return clicks;
    }

    public synchronized void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
