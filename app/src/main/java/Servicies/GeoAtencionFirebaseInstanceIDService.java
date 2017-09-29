package Servicies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Irene on 29/9/2017.
 */

public class GeoAtencionFirebaseInstanceIDService extends FirebaseInstanceIdService {

    Context c;

    //id del usuario logueado
    SharedPreferences settings = c.getSharedPreferences("perfil", c.MODE_PRIVATE);
    String usrID = settings.getString("id", null);
    String token = FirebaseInstanceId.getInstance().getToken();

        if (usrID!=null && usrID!="null") {
        registerToken(token,usrID);
    }

    public static void registerToken(final String token, final String usu_id) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("token", token)
                        .add("usu_id", usu_id)
                        .build();

                Request request = new Request.Builder()
                        .url("http://api.evataxi.com.ve/api/firebase_token")
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("EVATAXI_D", "usu_id: " + usu_id);
                    Log.d("EVATAXI_D", "registerToken: " + response.code());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
