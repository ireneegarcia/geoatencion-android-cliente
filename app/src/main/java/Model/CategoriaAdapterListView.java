package Model;

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

import java.util.List;

/**
 * Created by Irene on 26/8/2017.
 */

public class CategoriaAdapterListView extends BaseAdapter {
    private Context context;
    private List<CategoriaServicios> items;

    public CategoriaAdapterListView(Context context, List<CategoriaServicios> items) {
        //super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
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
            Log.d("mytag icon", Variables.getUrl()+item.getIconUrl() + " - " + position);
            Glide
                    .with(this.context)
                    .load(Variables.getUrl()+item.getIconUrl())
                    .error(R.drawable.logo_icono)
                    .into(view.icon);
            // view.icon. (item.getIconUrl());
            view.name = (TextView) convertView.findViewById(R.id.textViewName);
            view.name.setText(item.getCategory());
            convertView.setTag(view);

        } else {
            view = (Fila) convertView.getTag();
            view.icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            Log.d("mytag icon", Variables.getUrl()+item.getIconUrl() + " - " + position);
            Glide
                    .with(this.context)
                    .load(Variables.getUrl()+item.getIconUrl())
                    .error(R.drawable.logo_icono)
                    .into(view.icon);
            view.name = (TextView) convertView.findViewById(R.id.textViewName);
            view.name.setText(item.getCategory());
        }

        //Setear la imagen desde el recurso drawable

        return convertView;
    }
}