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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.irene.geoatencion.Model.Alarma;
import com.example.irene.geoatencion.Model.Alarmas;
import com.example.irene.geoatencion.Model.Logs;
import com.example.irene.geoatencion.Model.Networks;
import com.example.irene.geoatencion.Remote.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {

    View mView;
    Context c;
    ArrayList<Alarmas> alarma;
    Networks network;
    //List<Alarma> alarma;

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_alarm, container, false);
        c = (Context)getActivity();

        obtenerAlarmas();

        return mView;
    }

    public void obtenerAlarmas(){

        alarma = new ArrayList<>();
        //id del usuario logueado
        SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        APIService.Factory.getIntance().listAlarms().enqueue(new Callback<List<Alarmas>>() {

            @Override
            public void onResponse(Call<List<Alarmas>> call, Response<List<Alarmas>> response) {
                //Logs.d("myTag", "--->bien " + call.request().url());

                if(response.isSuccessful()) {

                    Log.d("myTag", "--->on reponse " + response.body().size());
                    for (int i = 0; i< response.body().size(); i++){
                        // si la alarma pertenece al usuario

                        if(response.body().get(i).getUser().getId().equals(mId)){
                            alarma.add(response.body().get(i));
                        }
                    }

                    if(alarma.get(0).getStatus().equals("en atencion")) {
                        obtenerNetwork(alarma.get(0));
                    }else{
                        statusAtencion();
                    }

                    //filtrado(response.body());

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

    public void obtenerNetwork(final Alarmas _alarma){
        //id del usuario logueado
        SharedPreferences settings = c.getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        APIService.Factory.getIntance().listNetworks().enqueue(new Callback<List<Networks>>() {
            @Override
            public void onResponse(Call<List<Networks>> call, Response<List<Networks>> response) {

                //code == 200
                if(response.isSuccessful()) {
                    Log.d("my tag", "onResponse: todo fino");
                    for (int i = 0; i< response.body().size(); i++){
                        // si la unidad pertenece al usuario
                        if(response.body().get(i).get_id().equals(_alarma.getNetwork())){
                            network = response.body().get(i);
                            statusAtencion();
                        }
                    }
                }
                Log.d("my tag", "onResponse: todo fino "+ network);
            }

            @Override
            public void onFailure(Call<List<Networks>> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
                Log.d("myTag", "This is my message on failure " + t.toString());
            }
        });

    }

    public void actualizarAlarma(){

        Alarma enviarAlarma = new Alarma(alarma.get(0).get_id(),
                alarma.get(0).getUser().getId(),
                alarma.get(0).getCategoryService(),
                "cancelado por el cliente",
                alarma.get(0).getLatitude(),
                alarma.get(0).getLongitude(),
                alarma.get(0).getAddress(),
                alarma.get(0).getCreated(),
                alarma.get(0).getRating(),
                alarma.get(0).getOrganism(),
                "/modules/panels/client/img/cancelbyclient.png",
                "");

        // Actualización de alarma

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

        network.setStatus("activo");

        // Actualizar la unidad de atencion
        APIService.Factory.getIntance().updateNetwork(network.get_id(), network).enqueue(new Callback<Networks>() {
            @Override
            public void onResponse(Call<Networks> call, Response<Networks> response) {

                //code == 200
                if(response.isSuccessful()) {
                    Log.d("my tag", "onResponse: todo fino DEL LOG");
                }
            }

            @Override
            public void onFailure(Call<Networks> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
            }
        });

        // Creación de log
        APIService.Factory.getIntance().createLog(
                "La solicitud de atención ha sido cancelada por el cliente",
                alarma.get(0).get_id(),
                alarma.get(0).getUser().getId(),
                "",
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
    }

    public void statusAtencion(){

        final TextView status = (TextView) mView.findViewById(R.id.textViewMessage);
        final TextView status1 = (TextView) mView.findViewById(R.id.textViewMessage1);
        final TextView status2 = (TextView) mView.findViewById(R.id.textViewMessage2);
        final ImageView imageStatusA = (ImageView) mView.findViewById(R.id.imageViewAway);
        final ImageView imageStatusP = (ImageView) mView.findViewById(R.id.imageViewProcesed);
        final ImageView imageStatusA1 = (ImageView) mView.findViewById(R.id.imageViewAway1);
        final ImageView imageStatusP1 = (ImageView) mView.findViewById(R.id.imageViewProcesed1);
        final ImageView imageStatusA2 = (ImageView) mView.findViewById(R.id.imageViewAway2);
        final ImageView imageStatusP3 = (ImageView) mView.findViewById(R.id.imageViewProcesed2);
        final TableRow row = (TableRow) mView.findViewById(R.id.row_status);
        final RelativeLayout message = mView.findViewById(R.id.message);
        final Button cancelar = (Button) mView.findViewById(R.id.cancelar);

        final TableLayout tabla_unidad = (TableLayout) mView.findViewById(R.id.tabla_unidad);
        final TextView datos_unidad = (TextView) mView.findViewById(R.id.textView7);
        final ImageView imageView3 = (ImageView) mView.findViewById(R.id.imageView3);
        final TextView placa = (TextView) mView.findViewById(R.id.placa);
        final TextView modelo = (TextView) mView.findViewById(R.id.modelo);
        final TextView marca = (TextView) mView.findViewById(R.id.marca);
        final TextView color = (TextView) mView.findViewById(R.id.color);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarAlarma();
            }
        });
        Log.d("AlarmaFragment", "statusAtencion: "+alarma.get(0));

        if (alarma.get(0).getStatus().equals("esperando")){
            status.setText("Alarma enviada de manera exitosa");
            imageStatusA.setVisibility(View.GONE);
            imageStatusP.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            cancelar.setVisibility(View.VISIBLE);
        }
        else if (alarma.get(0).getStatus().equals("en atencion")){
            status.setText("Alarma enviada de manera exitosa");
            status1.setText("Unidad enviada");
            imageStatusA.setVisibility(View.GONE);
            imageStatusP.setVisibility(View.VISIBLE);
            imageStatusA1.setVisibility(View.GONE);
            imageStatusP1.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            cancelar.setVisibility(View.VISIBLE);

            datos_unidad.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.VISIBLE);
            datos_unidad.setText("Unidad enviada '"+network.getCarCode()+"'");
            tabla_unidad.setVisibility(View.VISIBLE);
            placa.setText(network.getCarPlate());
            modelo.setText(network.getCarModel());
            marca.setText(network.getCarBrand());
            color.setText(network.getCarColor());


        }
        else if (alarma.get(0).getStatus().equals("cancelado por el operador")){
            status.setText("Alarma enviada de manera exitosa");
            status1.setText("Atencion cancelada por el organismo");
            imageStatusA.setVisibility(View.GONE);
            imageStatusP.setVisibility(View.VISIBLE);
            imageStatusA1.setVisibility(View.GONE);
            imageStatusP1.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
        else if (alarma.get(0).getStatus().equals("cancelado por el cliente")){
            status.setText("Alarma enviada de manera exitosa");
            status1.setText("Usted ha cancelado esta alarma");
            imageStatusA.setVisibility(View.GONE);
            imageStatusP.setVisibility(View.VISIBLE);
            imageStatusA1.setVisibility(View.GONE);
            imageStatusP1.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
        else if (alarma.get(0).getStatus().equals("rechazado")){
            status.setText("Alarma enviada de manera exitosa");
            status1.setText("Solicitud rechazada");
            imageStatusA.setVisibility(View.GONE);
            imageStatusP.setVisibility(View.VISIBLE);
            imageStatusA1.setVisibility(View.GONE);
            imageStatusP1.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
        else if (alarma.get(0).getStatus().equals("atendido")){

            status.setText("Alarma atendida de manera exitosa");
            imageStatusA.setVisibility(View.GONE);
            imageStatusP.setVisibility(View.VISIBLE);


            row.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);

            if (alarma.get(0).getRating().equals("sin calificar")){
                status1.setText("Pendiente por calificación");
                imageStatusA1.setVisibility(View.VISIBLE);
                imageStatusP1.setVisibility(View.GONE);
            }else{
                status1.setText("Gracias por su calificación");
                imageStatusA1.setVisibility(View.GONE);
                imageStatusP1.setVisibility(View.VISIBLE);
            }

        }

    }

}
