package com.example.irene.geoatencion;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Model.Alarma;
import Model.CategoriaAdapterListView;
import Model.CategoriaServicios;
import Model.Solicitudes;
import Remote.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {


    View mView;
    Context c;
    ListView categorias;

    List<CategoriaServicios> categoriaServicio;
    List<Solicitudes> solicitudes;
    MapView mMapView;
    private GoogleMap googleMap;

    //Coordenadas de ubicación
    public static Location mCurrentLocation;

    //dirección de la ubicación
    public static String address;

    //Ultima vez en actualizar las coordenadas de ubicación
    //Hora
    private String mLastUpdateTime;
    //fecha
    private String mLastUpdateDate;

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_maps, container, false);
        c = (Context)getActivity();

        //View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        listarCategorias();
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.request);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alert = createSimpleDialog();
                alert.show();
            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationStart();

                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);


            }
        });

        return mView;


       /* googleMap = mMapView.getMapAsync();
        // latitude and longitude
        double latitude = 17.385044;
        double longitude = 78.486671;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        return rootView;*/
    }

    private void agregarMarcador(String address){
        // For dropping a marker at a point on the Map
       // LatLng currentLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        MarkerOptions options = new MarkerOptions();
        IconGenerator iconFactory = new IconGenerator(c);
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        //options.title("Mi posición actual");
        options.snippet(address);

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        options.position(currentLatLng);
        Marker mapMarker = googleMap.addMarker(options);
        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        mapMarker.setTitle("Mi posición actual");
        Log.d("my tag", "Marcador añadido.............................");
        // For zooming automatically to the location of the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                17));
        Log.d("my tag", "Zoom hecho.............................");

        /*googleMap.addMarker(new MarkerOptions().position(currentLatLng)
                .title("Mi posición actual")
                .snippet(address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/

        // For zooming automatically to the location of the marker
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(12).build();
       // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
       // Local.setMainActivity(c);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

       // mensaje1.setText("Localizacion agregada");
       // mensaje2.setText("");
        Log.d("my tag", "Localizacion agregada");
        Log.d("my tag", "");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation() {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (mCurrentLocation.getLatitude() != 0.0 && mCurrentLocation.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(c, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    /*mensaje2.setText("Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));*/
                    Log.d("my tag", "Mi direccion es: \n"
                            + DirCalle.getAddressLine(0));
                    address = DirCalle.getAddressLine(0);
                    agregarMarcador(DirCalle.getAddressLine(0));
                    SharedPreferences sp = c.getSharedPreferences("perfil", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("latitude", String.valueOf(mCurrentLocation.getLatitude()));
                    editor.putString("longitude", String.valueOf(mCurrentLocation.getLongitude()));
                    editor.putString("address", String.valueOf(DirCalle.getAddressLine(0)));
                    editor.commit();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
       /* MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(Context mainActivity) {
            this. mainActivity = mainActivity;
        }*/

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            mCurrentLocation = loc;
            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + mCurrentLocation.getLatitude() + "\n Long = " + mCurrentLocation.getLongitude();
          //  mensaje1.setText(Text);
            Log.d("my tag", Text);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            mLastUpdateDate = DateFormat.getDateInstance().format(new Date());
            setLocation();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
            Log.d("my tag", "GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");
            Log.d("my tag", "GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void listarCategorias(){

       // Log.d("myTag", "API Solicitudes");
        APIService.Factory.getIntance().listSolicituds().enqueue(new Callback<List<Solicitudes>>() {
            @Override
            public void onResponse(Call<List<Solicitudes>> call, Response<List<Solicitudes>> response) {
                //Log.d("myTag", "--->bien " + call.request().url());

                if(response.isSuccessful()) {
                    solicitudes = response.body();
                   // Log.d("myTag", "--->on reponse " + response.body().toString());
                    //Log.d("myTag", "--->on reponse " + call.request().url());

                }
            }

            @Override
            public void onFailure(Call<List<Solicitudes>> call, Throwable t) {
               // Log.d("myTag", "This is my message on failure " + call.request().url());
                //Log.d("myTag", "This is my message on failure " + t.toString());
            }
        });

      //  Log.d("myTag", "API Categorías");
        APIService.Factory.getIntance().listCategories().enqueue(new Callback<List<CategoriaServicios>>() {
            @Override
            public void onResponse(Call<List<CategoriaServicios>> call, Response<List<CategoriaServicios>> response) {
               // Log.d("myTag", "--->bien " + call.request().url());

                if(response.isSuccessful()) {
                    categoriaServicio = response.body();
                    //Log.d("myTag", "--->on reponse " + response.body().toString());
                    //Log.d("myTag", "--->on reponse " + call.request().url());

                }
            }

            @Override
            public void onFailure(Call<List<CategoriaServicios>> call, Throwable t) {
               // Log.d("myTag", "This is my message on failure " + call.request().url());
               // Log.d("myTag", "This is my message on failure " + t.toString());
            }
        });
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //id del usuario logueado
        SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        View layout = inflater.inflate(R.layout.layout_request, null);
        builder.setView(layout);
        categorias = (ListView) layout.findViewById(R.id.listViewCategorias);
        final RelativeLayout message = layout.findViewById(R.id.message);
        CategoriaAdapterListView adapter = new CategoriaAdapterListView(c, mId, categoriaServicio, solicitudes);
        categorias.setAdapter(adapter);
        final TextView status = (TextView) layout.findViewById(R.id.textViewMessage);
        final TextView status1 = (TextView) layout.findViewById(R.id.textViewMessage1);
        final TextView status2 = (TextView) layout.findViewById(R.id.textViewMessage2);
        final ProgressBar progreso = (ProgressBar) layout.findViewById(R.id.progressBarMessage);
        final ImageView imageStatusA = (ImageView) layout.findViewById(R.id.imageViewAway);
        final ImageView imageStatusP = (ImageView) layout.findViewById(R.id.imageViewProcesed);
        final ImageView imageStatusA1 = (ImageView) layout.findViewById(R.id.imageViewAway1);
        final ImageView imageStatusP1 = (ImageView) layout.findViewById(R.id.imageViewProcesed1);
        final ImageView imageStatusA2 = (ImageView) layout.findViewById(R.id.imageViewAway2);
        final ImageView imageStatusP3 = (ImageView) layout.findViewById(R.id.imageViewProcesed2);

        categorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Log.i("posicion", "posicion " + position);
                categorias.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                status.setText("Enviando alarma");
                final CategoriaServicios posActual = categoriaServicio.get(position);
                APIService.Factory.getIntance()
                        .createAlarm(posActual.getId(),
                                "esperando",
                                mCurrentLocation.getLatitude()+"",
                                mCurrentLocation.getLongitude()+"",
                                address,
                                mId)

                        .enqueue(new Callback<Alarma>() {
                            @Override
                            public void onResponse(Call<Alarma> call, Response<Alarma> response) {

                                //code == 200
                                if(response.isSuccessful()) {
                                    Log.d("my tag", "onResponse: todo fino");
                                    status.setText("Alarma enviada de manera exitosa");
                                    imageStatusA.setVisibility(View.GONE);
                                    imageStatusP.setVisibility(View.VISIBLE);
                                    progreso.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Alarma> call, Throwable t){
                                //
                                Log.d("myTag", "This is my message on failure " + call.request().url());
                                status.setText("Error al enviar la alarma");
                            }
                        });
            }
        });

        return builder.create();
    }
}
