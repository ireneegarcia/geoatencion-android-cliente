package com.example.irene.geoatencion.Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.irene.geoatencion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irene on 26/8/2017.
 */

public class CategoriaAdapterListView extends BaseAdapter {
    private Context context;
    private List<CategoriaServicios> items;
    private String mId;
    public static ArrayList<CategoriaServicios> resultado = new ArrayList<CategoriaServicios>();
    public static ArrayList<String> organismos = new ArrayList<>();

    public CategoriaAdapterListView(Context context, String mId, List<CategoriaServicios> categorias, List<Solicitudes> solicitudes) {
        //super(context, 0, items);
        this.context = context;
        this.mId = mId;

        //Resultado
        this.items = filtrado(mId, categorias, solicitudes);
        // Logs.d("my tag", " "+this.items.size());
    }

    public ArrayList<CategoriaServicios> filtrado(String mId, List<CategoriaServicios> categorias, List<Solicitudes> solicitudes){
        // ArrayList<CategoriaServicios> resultado = new ArrayList<CategoriaServicios>();

        resultado = new ArrayList<CategoriaServicios>();
        //Se filtran de todas las categorías cuales puede consumir el usuario logueado
        for (int i = 0; i< solicitudes.size(); i++){
            //Solicitudes del usuario y que esten aceptadas = afiliaciones
            if(solicitudes.get(i).getUser().getId().equals(mId) && solicitudes.get(i).getStatus().equals("aceptado")){
                //Logs.d("my tag", solicitudes.get(i).toString());
                for (int j = 0; j < categorias.size(); j++){
                    if(solicitudes.get(i).getCategory().equals(categorias.get(j).getId())){
                        //Logs.d("my tag", ""+categorias.get(j));
                        resultado.add(categorias.get(j));
                        organismos.add(solicitudes.get(i).getOrganism());
                    }
                }

            }
        }
        Log.d("my tag", "resultado "+resultado);
        return resultado;
    }
    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public CategoriaServicios getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class Fila
    {
        TextView name;
        ImageView icon;
        String id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        Fila view;
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CategoriaServicios item = items.get(position);
        if (convertView == null) {
            view = new Fila();
            convertView = inflator.inflate(R.layout.layout_list_categoria_servicio, null);
            view.icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            //  Logs.d("mytag icon", Variables.getUrl()+item.getIconUrl() + " - " + position);
            Glide
                    .with(this.context)
                    .load(Variables.getUrl()+item.getIconUrl())
                    .error(R.drawable.logo_icono)
                    .into(view.icon);
            // view.icon. (item.getIconUrl());
            view.name = (TextView) convertView.findViewById(R.id.textViewName);
            view.name.setText(item.getCategory());
            view.id = mId;
            convertView.setTag(view);

        } else {
            view = (Fila) convertView.getTag();
            view.icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            // Logs.d("mytag icon", Variables.getUrl()+item.getIconUrl() + " - " + position);
            Glide
                    .with(this.context)
                    .load(Variables.getUrl()+item.getIconUrl())
                    .error(R.drawable.logo_icono)
                    .into(view.icon);
            view.name = (TextView) convertView.findViewById(R.id.textViewName);
            view.name.setText(item.getCategory());
            view.id = mId;
        }

        //Setear la imagen desde el recurso drawable

        return convertView;
    }


}