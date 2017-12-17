package com.example.irene.geoatencion;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.irene.geoatencion.Model.Alarma;
import com.example.irene.geoatencion.Model.Alarmas;
import com.example.irene.geoatencion.Model.Logs;
import com.example.irene.geoatencion.Model.Networks;
import com.example.irene.geoatencion.Remote.APIService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    View mView;
    Context c;
    ArrayList<Alarmas> alarma = new ArrayList<>();
    ArrayList<String> network = new ArrayList<>();
    Integer rating = 0;

    public RatingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rating, container, false);
        c = (Context)getActivity();

        obtenerAlarmas();
        return mView;
    }

    public void adaptarVista(Integer calification){

        rating = calification;

        final Button send_button = (Button) mView.findViewById(R.id.send_button);
        final TextView rating_text = (TextView) mView.findViewById(R.id.rating_text);
        rating_text.setVisibility(View.VISIBLE);

        switch (calification){
            case 1:{
                rating_text.setText("No me gustó en lo absoluto");
                break;
            }
            case 2:{
                rating_text.setText("No me gustó");
                break;
            }
            case 3:{
                rating_text.setText("Esta bien");
                break;
            }
            case 4:{
                rating_text.setText("Me gustó");
                break;
            }
            case 5:{
                rating_text.setText("Me encantó");
                break;
            }
        }
        send_button.setVisibility(View.VISIBLE);

    }

    public void obtenerUnidad(){

        network.clear();
        //id del usuario logueado
        SharedPreferences settings = c.getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        APIService.Factory.getIntance().listNetworks().enqueue(new Callback<List<Networks>>() {
            @Override
            public void onResponse(Call<List<Networks>> call, Response<List<Networks>> response) {

                //code == 200
                if(response.isSuccessful()) {
                    for (int i = 0; i< alarma.size(); i++){
                        for (int j = 0; j< response.body().size(); j++){
                            // si la unidad coincide con la unidad de las alarmas a calificar
                            if(alarma.get(i).getNetwork().equals(response.body().get(j).get_id())){
                                network.add(response.body().get(j).getCarCode());
                            }
                        }
                    }
                    mensaje();
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

    public void obtenerAlarmas(){

        alarma.clear();
        APIService.Factory.getIntance().listAlarms().enqueue(new Callback<List<Alarmas>>() {

            @Override
            public void onResponse(Call<List<Alarmas>> call, Response<List<Alarmas>> response) {
                //Logs.d("myTag", "--->bien " + call.request().url());

                if(response.isSuccessful()) {
                    //id del usuario logueado
                    SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
                    final String mId = settings.getString("id", null);

                    for (int i = 0; i< response.body().size(); i++){
                        // si la alarma pertenece al usuario
                        if(response.body().get(i).getUser().getId().equals(mId) &&
                                // la alarma ya atendida
                                response.body().get(i).getStatus().equals("atendido") &&
                                // la alarma aun no tiene calificación de atencion
                                response.body().get(i).getRating().equals("sin calificar")){
                            alarma.add(response.body().get(i));
                        }
                    }
                    obtenerUnidad();

                }
            }

            @Override
            public void onFailure(Call<List<Alarmas>> call, Throwable t) {
                //Logs.d("AlarmaFragment", "This is my message on failure " + call.request().url());
                //Logs.d("myTag", "This is my message on failure " + t.toString());
            }
        });
    }

    public void mensaje(){

        final TextView title = (TextView) mView.findViewById(R.id.title);
        final TextView date_alarm = (TextView) mView.findViewById(R.id.date_alarm);
        final TextView unit_alarm = (TextView) mView.findViewById(R.id.unit_alarm);

        final ImageButton rating1 = (ImageButton) mView.findViewById(R.id.rating1);
        final ImageButton rating2 = (ImageButton) mView.findViewById(R.id.rating2);
        final ImageButton rating3 = (ImageButton) mView.findViewById(R.id.rating3);
        final ImageButton rating4 = (ImageButton) mView.findViewById(R.id.rating4);
        final ImageButton rating5 = (ImageButton) mView.findViewById(R.id.rating5);

        final ImageButton rating1_1 = (ImageButton) mView.findViewById(R.id.rating1_1);
        final ImageButton rating2_2 = (ImageButton) mView.findViewById(R.id.rating2_2);
        final ImageButton rating3_3 = (ImageButton) mView.findViewById(R.id.rating3_3);
        final ImageButton rating4_4 = (ImageButton) mView.findViewById(R.id.rating4_4);
        final ImageButton rating5_5 = (ImageButton) mView.findViewById(R.id.rating5_5);
        final Button send_button = (Button) mView.findViewById(R.id.send_button);

        if( alarma.size() == 0) {
            title.setText("No tiene pendientes por calificar");

            final TextView rating_text = (TextView) mView.findViewById(R.id.rating_text);

            rating_text.setVisibility(View.GONE);
            date_alarm.setVisibility(View.GONE);
            unit_alarm.setVisibility(View.GONE);
            send_button.setVisibility(View.GONE);
            rating1.setVisibility(View.GONE);
            rating2.setVisibility(View.GONE);
            rating3.setVisibility(View.GONE);
            rating4.setVisibility(View.GONE);
            rating5.setVisibility(View.GONE);
            rating1_1.setVisibility(View.GONE);
            rating2_2.setVisibility(View.GONE);
            rating3_3.setVisibility(View.GONE);
            rating4_4.setVisibility(View.GONE);
            rating5_5.setVisibility(View.GONE);

        }else{

            SimpleDateFormat formatI = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss", Locale.US);
            SimpleDateFormat formatF = new SimpleDateFormat("yyyy-MM-dd, hh:mm aaa", Locale.US);

            try {
                date_alarm.setText("Fecha de atención: " + formatF.format(formatI.parse(alarma.get(0).getCreated().substring(0,18))));
                unit_alarm.setText("Unidad que realizo la atención: " + network.get(0));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            rating1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(1);

                    rating1.setVisibility(View.GONE);
                    rating1_1.setVisibility(View.VISIBLE);
                }
            });

            rating2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(2);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                }
            });

            rating3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(3);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);
                    rating3.setVisibility(View.GONE);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                    rating3_3.setVisibility(View.VISIBLE);
                }
            });

            rating4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(4);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);
                    rating3.setVisibility(View.GONE);
                    rating4.setVisibility(View.GONE);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                    rating3_3.setVisibility(View.VISIBLE);
                    rating4_4.setVisibility(View.VISIBLE);
                }
            });

            rating5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(5);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);
                    rating3.setVisibility(View.GONE);
                    rating4.setVisibility(View.GONE);
                    rating5.setVisibility(View.GONE);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                    rating3_3.setVisibility(View.VISIBLE);
                    rating4_4.setVisibility(View.VISIBLE);
                    rating5_5.setVisibility(View.VISIBLE);
                }
            });

            rating1_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(1);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.GONE);
                    rating3_3.setVisibility(View.GONE);
                    rating4_4.setVisibility(View.GONE);
                    rating5_5.setVisibility(View.GONE);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.VISIBLE);
                    rating3.setVisibility(View.VISIBLE);
                    rating4.setVisibility(View.VISIBLE);
                    rating5.setVisibility(View.VISIBLE);
                }
            });

            rating2_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(2);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                    rating3_3.setVisibility(View.GONE);
                    rating4_4.setVisibility(View.GONE);
                    rating5_5.setVisibility(View.GONE);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);
                    rating3.setVisibility(View.VISIBLE);
                    rating4.setVisibility(View.VISIBLE);
                    rating5.setVisibility(View.VISIBLE);
                }
            });

            rating3_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(3);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                    rating3_3.setVisibility(View.VISIBLE);
                    rating4_4.setVisibility(View.GONE);
                    rating5_5.setVisibility(View.GONE);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);
                    rating3.setVisibility(View.GONE);
                    rating4.setVisibility(View.VISIBLE);
                    rating5.setVisibility(View.VISIBLE);
                }
            });

            rating4_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptarVista(4);

                    rating1_1.setVisibility(View.VISIBLE);
                    rating2_2.setVisibility(View.VISIBLE);
                    rating3_3.setVisibility(View.VISIBLE);
                    rating4_4.setVisibility(View.VISIBLE);
                    rating5_5.setVisibility(View.GONE);

                    rating1.setVisibility(View.GONE);
                    rating2.setVisibility(View.GONE);
                    rating3.setVisibility(View.GONE);
                    rating4.setVisibility(View.GONE);
                    rating5.setVisibility(View.VISIBLE);
                }
            });

            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enviarCalification();
                }
            });
        }
    }

    public void enviarCalification(){

        String _network = alarma.get(0).getNetwork();
        Alarma enviarAlarma = new Alarma(alarma.get(0).get_id(),
                alarma.get(0).getUser().getId(),
                alarma.get(0).getCategoryService(),
                alarma.get(0).getStatus(),
                alarma.get(0).getLatitude(),
                alarma.get(0).getLongitude(),
                alarma.get(0).getAddress(),
                alarma.get(0).getCreated(),
                rating+"",
                alarma.get(0).getOrganism(),
                alarma.get(0).getIcon(),
                "");

        APIService.Factory.getIntance().updateAlarm(enviarAlarma.get_id(), enviarAlarma).enqueue(new Callback<Alarma>() {
            @Override
            public void onResponse(Call<Alarma> call, Response<Alarma> response) {

                //code == 200
                if(response.isSuccessful()) {
                    Log.d("my tag", "onResponse: todo fino");
                }
            }

            @Override
            public void onFailure(Call<Alarma> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
            }
        });
        // Creación de log
        APIService.Factory.getIntance().createLog(
                "El cliente " + alarma.get(0).getUser().getDisplayName() +
                        " ha dado una calificación de: "+ rating +" a la atención recibida",
                alarma.get(0).get_id(),
                alarma.get(0).getUser().getId(),
                network.get(0),
                alarma.get(0).getOrganism()).enqueue(new Callback<Logs>() {
            @Override
            public void onResponse(Call<Logs> call, Response<Logs> response) {

                //code == 200
                if(response.isSuccessful()) {
                    Log.d("my tag", "onResponse: todo fino DEL LOG");
                }
            }

            @Override
            public void onFailure(Call<Logs> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
            }
        });

        obtenerAlarmas();
    }

}
