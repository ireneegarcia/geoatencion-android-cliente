package com.example.irene.geoatencion.Services;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.irene.geoatencion.Model.FirebaseToken;
import com.example.irene.geoatencion.Remote.APIService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Irene on 2/10/2017.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        //id del usuario logueado
        SharedPreferences settings = getSharedPreferences("perfil", MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        //Se obtiene el token actualizado
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("firebase", "SSToken actualizado: " + refreshedToken);

        if (mId!=null && mId!="null") {
            registerToken(refreshedToken,mId);
        }
    }

    public static void registerToken(final String token, final String mId) {

        APIService.Factory.getIntance().registerToken(token, mId).enqueue(new Callback<FirebaseToken>() {
            @Override
            public void onResponse(Call<FirebaseToken> call, Response<FirebaseToken> response) {

                //code == 200
                if(response.isSuccessful()) {
                    Log.d("my tag", "onResponse: todo fino");
                }
            }

            @Override
            public void onFailure(Call<FirebaseToken> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
            }
        });
    }
}
