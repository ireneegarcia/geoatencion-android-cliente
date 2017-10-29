package com.example.irene.geoatencion.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irene on 27/10/2017.
 */

public class Routes {

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private User lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public User getLng() {
        return lng;
    }

    public void setLng(User lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Routes{" +
                "lat='" + lat + '\'' +
                ", lng=" + lng +
                '}';
    }
}
