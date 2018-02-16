package com.example.irene.geoatencion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.irene.geoatencion.Model.Alarmas;
import com.example.irene.geoatencion.Model.HistoryAdapterList;
import com.example.irene.geoatencion.Model.Networks;
import com.example.irene.geoatencion.Remote.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    View mView;
    Context c;
    ArrayList<Alarmas> alarma;
    Networks network;

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

    public void obtenerNetwork(final Alarmas _alarma){

        final TextView unidad = (TextView) mView.findViewById(R.id.textView9);
        final TextView placa = (TextView) mView.findViewById(R.id.textView19);
        final TextView marca = (TextView) mView.findViewById(R.id.textView21);
        final TextView modelo = (TextView) mView.findViewById(R.id.textView23);
        final TextView color = (TextView) mView.findViewById(R.id.textView16);
        final CardView cardView = (CardView) mView.findViewById(R.id.cardView);
        final ProgressBar progreso = (ProgressBar) mView.findViewById(R.id.progressBarMessage);

        //id del usuario logueado
        SharedPreferences settings = c.getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        APIService.Factory.getIntance().listNetworks().enqueue(new Callback<List<Networks>>() {
            @Override
            public void onResponse(Call<List<Networks>> call, Response<List<Networks>> response) {

                //code == 200
                if(response.isSuccessful()) {

                    for (int i = 0; i< response.body().size(); i++){
                        // si la unidad pertenece al usuario
                        if(response.body().get(i).get_id().equals(_alarma.getNetwork())){
                            network = response.body().get(i);
                        }
                    }

                    if (network != null) {
                        // Log.d("unidad", "unidad: "+network.getCarCode());
                        cardView.setVisibility(View.VISIBLE);
                        unidad.setText(network.getCarCode());
                        placa.setText(network.getCarPlate());
                        marca.setText(network.getCarBrand());
                        modelo.setText(network.getCarModel());
                        color.setText(network.getCarColor());
                    } else {
                        cardView.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Networks>> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
                Log.d("myTag", "This is my message on failure " + t.toString());
            }
        });

    }

    public void adaptarVista(){

        final CardView cardView = (CardView) mView.findViewById(R.id.cardView);
        final ProgressBar progreso = (ProgressBar) mView.findViewById(R.id.progressBarMessage);
        progreso.setVisibility(View.GONE);

        if ( alarma.size() != 0) {
            ListView l = (ListView) mView.findViewById(R.id.listViewHistory);
            l.setVisibility(View.VISIBLE);
            l.setAdapter( new HistoryAdapterList(c,alarma));
            if (alarma.get(0).getStatus().equals("en atencion")) {
                obtenerNetwork(alarma.get(0));
            } else {
                cardView.setVisibility(View.GONE);
            }

        }

    }
}
