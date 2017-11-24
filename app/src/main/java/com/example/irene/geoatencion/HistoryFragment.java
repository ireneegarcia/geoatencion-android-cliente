package com.example.irene.geoatencion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.irene.geoatencion.Model.Alarmas;
import com.example.irene.geoatencion.Remote.APIService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    View mView;
    Context c;
    ArrayList<Alarmas> alarma;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_history, container, false);
        c = (Context)getActivity();

        obtenerAlarmas();

        return mView;
    }

    public void obtenerAlarmas(){

        APIService.Factory.getIntance().listAlarms().enqueue(new Callback<List<Alarmas>>() {

            @Override
            public void onResponse(Call<List<Alarmas>> call, Response<List<Alarmas>> response) {

                if(response.isSuccessful()) {
                    filtrado(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Alarmas>> call, Throwable t) {
                Log.d("AlarmaFragment", "This is my message on failure " + call.request().url());
                Log.d("myTag", "This is my message on failure " + t.toString());
            }
        });
    }

    public void filtrado(List<Alarmas> alarmasResponse){

        alarma = new ArrayList<>();

        SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        for (int i = 0; i< alarmasResponse.size(); i++){
            if(alarmasResponse.get(i).getUser().getId().equals(mId)){
                alarma.add(alarmasResponse.get(i));
            }
        }
        adaptarVista();
    }

    public void adaptarVista(){

        final TextView historial = (TextView) mView.findViewById(R.id.textView4);

        SimpleDateFormat formatI = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat formatF = new SimpleDateFormat("yyyy-MM-dd, hh:mm aaa", Locale.US);

        try {

            for (int i = 0; i< alarma.size(); i++){

                Log.d("history", ""+ alarma.get(0).getStatus());
                if(i == 0){
                    historial.setText("\n- "+formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                    historial.setText(historial.getText() + ": " + alarma.get(0).getStatus());
                } else {
                    historial.setText(historial.getText() + "\n\n- "+formatF.format(formatI.parse(alarma.get(i).getCreated().substring(0,18))));
                    historial.setText(historial.getText() + ": " + alarma.get(i).getStatus());
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
