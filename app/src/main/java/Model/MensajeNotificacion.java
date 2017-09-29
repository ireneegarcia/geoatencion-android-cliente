package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irene on 29/9/2017.
 */

public class MensajeNotificacion {

    @SerializedName("networkID")
    @Expose
    private String networkID;
    @SerializedName("networkLatitude")
    @Expose
    private String networkLatitude;
    @SerializedName("networkLongitude")
    @Expose
    private String networkLongitude;

    public String getNetworkID() {
        return networkID;
    }

    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    public String getNetworkLatitude() {
        return networkLatitude;
    }

    public void setNetworkLatitude(String networkLatitude) {
        this.networkLatitude = networkLatitude;
    }

    public String getNetworkLongitude() {
        return networkLongitude;
    }

    public void setNetworkLongitude(String networkLongitude) {
        this.networkLongitude = networkLongitude;
    }

    @Override
    public String toString() {
        return "MensajeNotificacion{" +
                "networkID='" + networkID + '\'' +
                ", networkLatitude='" + networkLatitude + '\'' +
                ", networkLongitude='" + networkLongitude + '\'' +
                '}';
    }
}
