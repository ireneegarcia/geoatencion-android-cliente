package com.example.irene.geoatencion.Model;

/**
 * Created by Irene on 26/8/2017.
 */

public class Variables {
    private static String url = "http://xpectra-geoatt.southcentralus.cloudapp.azure.com/";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Variables.url = url;
    }
}
