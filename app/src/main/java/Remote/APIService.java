package Remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.CategoriaServicios;
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

    @GET("api/categoriaservicios")
    //@FormUrlEncoded
    Call<List<CategoriaServicios>> list();


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

   /* Call<Users> mlogin(@Field("_id") String id,
                         @Field("displayName") String displayName,
                         @Field("username") String username,
                         @Field("ci") String ci,
                         @Field("roles") String roles,
                         @Field("profileImageURL") String profileImageURL,
                         @Field("phone") String phone,
                         @Field("country") String country,
                         @Field("email") String email,
                         @Field("lastName") String lastName,
                         @Field("firstName") String firstName);*/
}