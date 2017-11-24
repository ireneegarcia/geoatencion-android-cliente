package com.example.irene.geoatencion.Remote;

import com.example.irene.geoatencion.Model.RouteGet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Irene on 28/10/2017.
 */

public interface APIServiceRoute {
     String BASE_URL = "http://maps.googleapis.com/";
    //String BASE_URL = "http://10.0.0.19:3000/";

    @GET("maps/api/directions/json")
    Call<RouteGet> getRoute(@Query("origin") String origin,
                            @Query("destination") String destination,
                            @Query("sensor") String sensor,
                            @Query("mode") String mode,
                            @Query("alternatives") String alternatives);


    class FactoryRoute {
        private static APIServiceRoute service;

        public static APIServiceRoute getIntance() {
            if (service == null) {
                //inicializacion Retrofit
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL).client(client).build();
                service = retrofit.create(APIServiceRoute.class);
                return service;
            } else {
                return service;
            }
        }

    }
}
