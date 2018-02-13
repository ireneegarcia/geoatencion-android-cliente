package com.example.irene.geoatencion.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.irene.geoatencion.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.example.irene.geoatencion.MapsFragment.categoriaServicio;

/**
 * Created by Irene on 16/12/2017.
 */

public class HistoryAdapterList extends BaseAdapter {
    private Context context;
    private List<Alarmas> items;

    public HistoryAdapterList(Context context, List<Alarmas> alarmas) {
        //super(context, 0, items);
        this.context = context;
        items = alarmas;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Alarmas getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class Fila
    {
        TextView category;
        TextView status;
        TextView date;
    }

    public String getCategory(final String category){

        for (int i = 0; i< categoriaServicio.size(); i++){
            if (categoriaServicio.get(i).getId().equals(category)) {
                return categoriaServicio.get(i).getCategory();
            }
        }

        return "";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        Fila view;
        LayoutInflater inflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Alarmas item = items.get(position);
        SimpleDateFormat formatI = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat formatF = new SimpleDateFormat("yyyy-MM-dd, hh:mm aaa", Locale.US);
        if (convertView == null) {
            view = new Fila();
            convertView = inflator.inflate(R.layout.layout_list_history, null);
            view.status = (TextView) convertView.findViewById(R.id.textViewStatus);
            view.category = (TextView) convertView.findViewById(R.id.textViewCategory);
            view.date = (TextView) convertView.findViewById(R.id.textViewDate);
            view.status.setText(item.getStatus());
            view.category.setText(getCategory(item.getCategoryService()));
            try {
                view.date.setText(formatF.format(formatI.parse(item.getCreated().substring(0,18))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            convertView.setTag(view);

        } else {
            view = (Fila) convertView.getTag();
            view.status = (TextView) convertView.findViewById(R.id.textViewStatus);
            view.category = (TextView) convertView.findViewById(R.id.textViewCategory);
            view.date = (TextView) convertView.findViewById(R.id.textViewDate);
            view.status.setText(item.getStatus());
            view.category.setText(getCategory(item.getCategoryService()));
            try {
                view.date.setText(formatF.format(formatI.parse(item.getCreated().substring(0,18))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Setear la imagen desde el recurso drawable

        return convertView;
    }
}
