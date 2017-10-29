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
                //Logs.d("myTag", "--->bien " + call.request().url());

                if(response.isSuccessful()) {

                    filtrado(response.body());

                    //Logs.d("AlarmaFragment", "--->on reponse " + response.body().toString());
                    //Logs.d("myTag", "--->on reponse " + call.request().url());
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

        //id del usuario logueado
        SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        for (int i = 0; i< alarmasResponse.size(); i++){
            // si la alarma pertenece al usuario
            if(alarmasResponse.get(i).getUser().getId().equals(mId)){
                alarma.add(alarmasResponse.get(i));
            }
        }

        //Logs.d("AlarmaFragment", "" + alarma.toString());
        adaptarVista();
    }

    public void adaptarVista(){

        final TextView fecha1 = (TextView) mView.findViewById(R.id.fecha1);
        final TextView fecha2 = (TextView) mView.findViewById(R.id.fecha2);
        final TextView fecha3 = (TextView) mView.findViewById(R.id.fecha3);
        final TextView fecha4 = (TextView) mView.findViewById(R.id.fecha4);
        final TextView status1 = (TextView) mView.findViewById(R.id.status1);
        final TextView status2 = (TextView) mView.findViewById(R.id.status2);
        final TextView status3 = (TextView) mView.findViewById(R.id.status3);
        final TextView status4 = (TextView) mView.findViewById(R.id.status4);

        SimpleDateFormat formatI = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat formatF = new SimpleDateFormat("yyyy-MM-dd, hh:mm aaa", Locale.US);


        try {
            switch (alarma.size()){
                case 1:{
                    fecha1.setText(formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                    status1.setText(alarma.get(0).getStatus());

                    fecha1.setText(formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                    status1.setText(alarma.get(0).getStatus());

                    fecha2.setVisibility(View.GONE);
                    status2.setVisibility(View.GONE);

                    fecha3.setVisibility(View.GONE);
                    status3.setVisibility(View.GONE);

                    fecha4.setVisibility(View.GONE);
                    status4.setVisibility(View.GONE);
                    break;
                }case 2:{
                    fecha1.setText(formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                    status1.setText(alarma.get(0).getStatus());

                    fecha2.setText(formatF.format(formatI.parse(alarma.get(1).getCreated().substring(0,18))));
                    status2.setText(alarma.get(1).getStatus());

                    fecha3.setVisibility(View.GONE);
                    status3.setVisibility(View.GONE);

                    fecha4.setVisibility(View.GONE);
                    status4.setVisibility(View.GONE);
                    break;
                }case 3:{
                    fecha1.setText(formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                    status1.setText(alarma.get(0).getStatus());

                    fecha2.setText(formatF.format(formatI.parse(alarma.get(1).getCreated().substring(0,18))));
                    status2.setText(alarma.get(1).getStatus());

                    fecha3.setText(formatF.format(formatI.parse(alarma.get(2).getCreated().substring(0,18))));
                    status3.setText(alarma.get(2).getStatus());

                    fecha4.setVisibility(View.GONE);
                    status4.setVisibility(View.GONE);
                    break;
                }case 4:{
                    fecha1.setText(formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                    status1.setText(alarma.get(0).getStatus());

                    fecha2.setText(formatF.format(formatI.parse(alarma.get(1).getCreated().substring(0,18))));
                    status2.setText(alarma.get(1).getStatus());

                    fecha3.setText(formatF.format(formatI.parse(alarma.get(2).getCreated().substring(0,18))));
                    status3.setText(alarma.get(2).getStatus());

                    fecha4.setText(formatF.format(formatI.parse(alarma.get(3).getCreated().substring(0,18))));
                    status4.setText(alarma.get(3).getStatus());
                    break;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }





    }
}
