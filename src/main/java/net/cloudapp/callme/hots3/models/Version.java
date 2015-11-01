package net.cloudapp.callme.hots3.models;

/**
 * Created by JT on 10/31/2015.
 */
public class Version {
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private int version;
    private String notes;
    private String timestamp;
}
