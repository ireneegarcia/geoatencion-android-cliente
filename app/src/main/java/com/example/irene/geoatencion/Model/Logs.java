package com.example.irene.geoatencion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irene on 15/10/2017.
 */

public class Logs {
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("alarm")
    @Expose
    private String alarm;

    @SerializedName("network")
    @Expose
    private String network;

    @SerializedName("client")
    @Expose
    private String client;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("organism")
    @Expose
    private String organism;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "description='" + description + '\'' +
                ", alarm='" + alarm + '\'' +
                ", network='" + network + '\'' +
                ", client='" + client + '\'' +
                ", user='" + user + '\'' +
                ", organism='" + organism + '\'' +
                '}';
    }
}
