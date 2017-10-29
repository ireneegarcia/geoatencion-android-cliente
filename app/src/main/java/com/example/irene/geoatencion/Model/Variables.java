package com.example.irene.geoatencion.Model;

/**
 * Created by Irene on 26/8/2017.
 */

public class Variables {
    private static String url = "http://192.168.0.150:3000/";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Variables.url = url;
    }
}
