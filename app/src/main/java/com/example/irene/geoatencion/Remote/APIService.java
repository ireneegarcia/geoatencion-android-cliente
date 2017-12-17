package com.example.irene.geoatencion.Remote;

import com.example.irene.geoatencion.Model.Alarma;
import com.example.irene.geoatencion.Model.Alarmas;
import com.example.irene.geoatencion.Model.CategoriaServicios;
import com.example.irene.geoatencion.Model.FirebaseToken;
import com.example.irene.geoatencion.Model.Logs;
import com.example.irene.geoatencion.Model.Networks;
import com.example.irene.geoatencion.Model.Solicitudes;
import com.example.irene.geoatencion.Model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface APIService {

    String BASE_URL = "http://192.168.0.150:3000/";
    //String BASE_URL = "http://10.0.0.19:3000/";


    //@Headers("Content-Type: application/json")
    @POST("api/auth/signin")
    @FormUrlEncoded
    Call<Users> login(@Field("usernameOrEmail") String username,
                      @Field("password") String password);

    @POST("api/alarms")
    @FormUrlEncoded
    Call<Alarma> createAlarm(@Field("categoryService") String categoryService,
                             @Field("status") String status,
                             @Field("latitude") String latitude,
                             @Field("longitude") String longitude,
                             @Field("address") String address,
                             @Field("organism") String organism,
                             @Field("user") String user);

    @POST("/api/logs")
    @FormUrlEncoded
    Call<Logs> createLog(@Field("description") String description,
                         @Field("alarm") String alarm,
                         @Field("client") String client,
                         @Field("user") String user,
                         @Field("organism") String organism);

    @POST("/api/firebasetokens")
    @FormUrlEncoded
    Call<FirebaseToken> registerToken(@Field("token") String token,
                                      @Field("userId") String user);

    @GET("api/categoriaservicios")
    Call<List<CategoriaServicios>> listCategories();

    @GET("api/solicituds")
    Call<List<Solicitudes>> listSolicituds();

    @GET("api/alarms")
    Call<List<Alarmas>> listAlarms();

    @GET("api/networks")
    Call<List<Networks>> listNetworks();

    @PUT("/api/alarms/{alarmId}")
    Call<Alarma> updateAlarm(@Path("alarmId") String alarmId,
                             @Body Alarma alarma);


    @PUT("/api/networks/{networkId}")
    Call<Networks> updateNetwork(@Path("networkId") String networkId,
                                 @Body Networks network);



    class Factory {
        private static APIService service;

        public static APIService getIntance() {
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
                service = retrofit.create(APIService.class);
                return service;
            } else {
                return service;
            }
        }

    }

}