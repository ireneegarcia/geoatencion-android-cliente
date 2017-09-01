package Remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.Alarma;
import Model.CategoriaServicios;
import Model.Solicitudes;
import Model.Users;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface APIService {

    String BASE_URL = "http://192.168.0.150:3000/";


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
                        @Field("user") String user);

    @GET("api/categoriaservicios")
    Call<List<CategoriaServicios>> listCategories();

    @GET("api/solicituds")
    Call<List<Solicitudes>> listSolicituds();


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