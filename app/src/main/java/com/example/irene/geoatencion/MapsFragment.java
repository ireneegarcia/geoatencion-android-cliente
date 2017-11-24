package com.example.irene.geoatencion;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.irene.geoatencion.Model.Alarma;
import com.example.irene.geoatencion.Model.Alarmas;
import com.example.irene.geoatencion.Model.CategoriaAdapterListView;
import com.example.irene.geoatencion.Model.CategoriaServicios;
import com.example.irene.geoatencion.Model.Logs;
import com.example.irene.geoatencion.Model.Networks;
import com.example.irene.geoatencion.Model.NotificationFirebase;
import com.example.irene.geoatencion.Model.RouteGet;
import com.example.irene.geoatencion.Model.RouteSet;
import com.example.irene.geoatencion.Model.Solicitudes;
import com.example.irene.geoatencion.Remote.APIService;
import com.example.irene.geoatencion.Remote.APIServiceRoute;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.irene.geoatencion.Model.CategoriaAdapterListView.organismos;
import static com.example.irene.geoatencion.Model.CategoriaAdapterListView.resultado;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {



    View mView;
    Context c;
    static Context cp;
    static String notificacion = "";
    static String notificacionUbicacion = "";
    ListView categorias;
    private static SetLocationPush slp = null;

    List<CategoriaServicios> categoriaServicio;
    List<Solicitudes> solicitudes;
    ArrayList<Alarmas> alarma = new ArrayList<>();
    Networks network;
    MapView mMapView;
    private static GoogleMap googleMap;

    //Coordenadas de ubicación
    public static Location mCurrentLocation;
    public static Location mCurrentLocationPush;

    //dirección de la ubicación
    public static String address;

    //Ultima vez en actualizar las coordenadas de ubicación
    //Hora
    private static String mLastUpdateTime;
    //fecha
    private static String mLastUpdateDate;

    // Objetos para traer el json de la api de google map y fragmentar el polyline
    RouteSet routeSet = new RouteSet();
    RouteGet routeGet = new RouteGet();

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_maps, container, false);
        c = (Context)getActivity();
        cp = (Context)getActivity();


        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        listarCategorias();
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.request);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alert = createSimpleDialog("");
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


        obtenerAlarmas();


        return mView;

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

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            if (!notificacion.equals("")) {
                AlertDialog alert = createSimpleDialog(notificacion);
                alert.show();
                notificacion = "";
            }
            if (!notificacionUbicacion.equals("esperando") && !notificacionUbicacion.equals("en atencion")) {

            }
            googleMap.clear();

            obtenerAlarmas();

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

    public void obtenerAlarmas(){
        //id del usuario logueado
        SharedPreferences settings = getActivity().getSharedPreferences("perfil", c.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        alarma.clear();
        APIService.Factory.getIntance().listAlarms().enqueue(new Callback<List<Alarmas>>() {

            @Override
            public void onResponse(Call<List<Alarmas>> call, Response<List<Alarmas>> response) {
                //Log.d("myTag", "--->bien " + call.request().url());

                if(response.isSuccessful()) {

                    for (int i = 0; i< response.body().size(); i++){
                        // si la alarma pertenece al usuario
                        if(response.body().get(i).getUser().getId().equals(mId) &&
                                (response.body().get(i).getStatus().equals("en atencion") ||
                                        response.body().get(i).getStatus().equals("esperando"))){
                            alarma.add(response.body().get(i));

                        }
                    }

                    if(alarma.size()!= 0 && alarma.get(alarma.size()-1).getStatus().equals("en atencion")) {
                        Log.d("tamano alarma", "onResponse: "+alarma.size());
                        obtenerUnidad();
                    }

                    //Log.d("AlarmaFragment", "--->on reponse " + response.body().toString());
                    //Log.d("myTag", "--->on reponse " + call.request().url());
                }
            }

            @Override
            public void onFailure(Call<List<Alarmas>> call, Throwable t) {
                Log.d("AlarmaFragment", "This is my message on failure " + call.request().url());
                Log.d("myTag", "This is my message on failure " + t.toString());
            }
        });
    }

    public void obtenerUnidad(){
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
                        if(response.body().get(i).get_id().equals(alarma.get(alarma.size()-1).getNetwork())){
                            network = response.body().get(i);
                            Log.d("my tag", "onResponse network: "+response.body().get(i).getCarCode());
                            MarkerOptions options = new MarkerOptions();
                            IconGenerator iconFactory = new IconGenerator(cp);
                            iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                            options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(DateFormat.getTimeInstance().format(new Date()))));
                            options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                            //options.title("Mi posición actual");
                            options.snippet(response.body().get(i).getAddress());

                            LatLng currentLatLng = new LatLng(Double.parseDouble(response.body().get(i).getLatitude()), Double.parseDouble(response.body().get(i).getLongitude()));
                            options.position(currentLatLng);
                            Marker mapMarker = googleMap.addMarker(options);
                            mapMarker.setTitle(response.body().get(i).getCarCode());
                            Log.d("my tag", "Marcador añadido.............................");
                            // For zooming automatically to the location of the marker
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                    14));
                            Log.d("my tag", "Zoom hecho.............................");

                            makeURL(Double.parseDouble(alarma.get(alarma.size()-1).getLatitude()),
                                    Double.parseDouble(alarma.get(alarma.size()-1).getLongitude()),
                                    Double.parseDouble(response.body().get(i).getLatitude()),
                                    Double.parseDouble(response.body().get(i).getLongitude())
                            );
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

    public void makeURL(final double destlat, final double destlog,
                        final double sourcelat, final double sourcelog) {

        routeSet.setAlternatives("true");
        routeSet.setDestination(destlat + "," + destlog);
        routeSet.setMode("driving");
        routeSet.setOrigin(sourcelat + "," + sourcelog);
        routeSet.setSensor("false");
        APIServiceRoute.FactoryRoute.getIntance().getRoute(routeSet.getOrigin(),routeSet.getDestination(),routeSet.getSensor(),routeSet.getMode(),routeSet.getAlternatives()).enqueue(new Callback<RouteGet>() {
            @Override
            public void onResponse(Call<RouteGet> call, Response<RouteGet> response) {

                //code == 200
                if(response.isSuccessful()) {
                    routeGet = response.body();

                    for (int i = 0; i< routeGet.getRoutes().get(0).getLegs().get(0).getSteps().size(); i++){
                        Log.d("routes", "onResponse: "+i);
                        /*Log.d("jsonroute", "Lat" + routeGet.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStartLocation().getLat().toString());
                        Log.d("jsonroute", "Long" + routeGet.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStartLocation().getLng().toString());*/

                        Polyline line = googleMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(Double.parseDouble(routeGet.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStartLocation().getLat()),
                                                Double.parseDouble(routeGet.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStartLocation().getLng())),
                                        new LatLng(Double.parseDouble(routeGet.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEndLocation().getLat()),
                                                Double.parseDouble(routeGet.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEndLocation().getLng())))
                                .width(5)
                                .color(Color.RED)
                                .geodesic(true));

                        // routeGet.getRoutes().get(0).getLegs().get(7).getSteps().toString();
                    }
                }
            }

            @Override
            public void onFailure(Call<RouteGet> call, Throwable t){
                //
                Log.d("myTag", "This is my message on failure " + call.request().url());
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
                    nuevaAlerta();
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
                "Ha sido cancelada la solicitud de atención " + alarma.get(0).get_id() +
                        " por el cliente " + alarma.get(0).getUser().getDisplayName(),
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
    public void nuevaAlerta(){
        alarma.clear();
        AlertDialog alert = createSimpleDialog("");
        alert.show();
    }

    public AlertDialog createSimpleDialog(String statusAtencion) {

        Log.d("nuevaAlerta: ", "nuevaAlerta: "+alarma.size());
        if (alarma.size() != 0 && statusAtencion.equals("")){
            statusAtencion = alarma.get(0).getStatus();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(cp);
        LayoutInflater inflater = (LayoutInflater)cp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //id del usuario logueado
        SharedPreferences settings = cp.getSharedPreferences("perfil", cp.MODE_PRIVATE);
        final String mId = settings.getString("id", null);

        View layout = inflater.inflate(R.layout.layout_request, null);
        builder.setView(layout);


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
        final TableRow row = (TableRow) layout.findViewById(R.id.row_status);
        final RelativeLayout message = layout.findViewById(R.id.message);
        final Button cancelar = (Button) layout.findViewById(R.id.cancelar);
        //final Button nueva_alarma = (Button) layout.findViewById(R.id.nueva_alarma);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarAlarma();
            }
        });

        categorias = (ListView) layout.findViewById(R.id.listViewCategorias);

        Log.d("estatus", "createSimpleDialog: "+statusAtencion);
        if (statusAtencion.equals("") || statusAtencion.equals("cancelado por el cliente")) {


            CategoriaAdapterListView adapter = new CategoriaAdapterListView(c, mId, categoriaServicio, solicitudes);
            categorias.setAdapter(adapter);

            categorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                    Log.i("posicion", "posicion " + position);
                    categorias.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                    status.setText("Enviando alarma");
                    final CategoriaServicios posActual = resultado.get(position);
                    Log.d("categoria", posActual.getCategory());
                    APIService.Factory.getIntance()
                            .createAlarm(posActual.getId(),
                                    "esperando",
                                    mCurrentLocation.getLatitude()+"",
                                    mCurrentLocation.getLongitude()+"",
                                    address,
                                    organismos.get(position),
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
        }
        else{
            if (statusAtencion.equals("esperando")){
                status.setText("Alarma enviada de manera exitosa");
                imageStatusA.setVisibility(View.GONE);
                imageStatusP.setVisibility(View.VISIBLE);
                progreso.setVisibility(View.GONE);
                categorias.setVisibility(View.GONE);
                cancelar.setVisibility(View.VISIBLE);
                message.setVisibility(View.VISIBLE);
            }
            else if (statusAtencion.equals("en atencion")){
                status.setText("Alarma enviada de manera exitosa");
                status1.setText("Unidad enviada");
                imageStatusA.setVisibility(View.GONE);
                imageStatusP.setVisibility(View.VISIBLE);
                imageStatusA1.setVisibility(View.GONE);
                imageStatusP1.setVisibility(View.VISIBLE);
                progreso.setVisibility(View.GONE);
                categorias.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                cancelar.setVisibility(View.VISIBLE);
            }
            else if (statusAtencion.equals("cancelado por el operador")){
                status.setText("Alarma enviada de manera exitosa");
                status1.setText("Atencion cancelada por el organismo");
                imageStatusA.setVisibility(View.GONE);
                imageStatusP.setVisibility(View.VISIBLE);
                imageStatusA1.setVisibility(View.GONE);
                imageStatusP1.setVisibility(View.VISIBLE);
                progreso.setVisibility(View.GONE);
                row.setVisibility(View.GONE);
                categorias.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                googleMap.clear();
                alarma.clear();
                builder.setPositiveButton("Nueva alarma", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        nuevaAlerta();
                        //dialog.cancel();
                    }}
                );

            }
            else if (statusAtencion.equals("rechazado")){
                status.setText("Alarma enviada de manera exitosa");
                status1.setText("Solicitud rechazada");
                imageStatusA.setVisibility(View.GONE);
                imageStatusP.setVisibility(View.VISIBLE);
                imageStatusA1.setVisibility(View.GONE);
                imageStatusP1.setVisibility(View.VISIBLE);
                progreso.setVisibility(View.GONE);
                row.setVisibility(View.GONE);
                categorias.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                googleMap.clear();
                builder.setPositiveButton("Nueva alarma", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        nuevaAlerta();
                        //dialog.cancel();
                    }}
                );
            }
            else if (statusAtencion.equals("atendido")){

                status.setText("Alarma atendida de manera exitosa");
                imageStatusA.setVisibility(View.GONE);
                imageStatusP.setVisibility(View.VISIBLE);
                progreso.setVisibility(View.GONE);
                row.setVisibility(View.GONE);
                categorias.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                obtenerAlarmas();
                googleMap.clear();

                if (alarma.get(0).getRating().equals("sin calificar")){
                    status1.setText("Pendiente por calificación");
                    imageStatusA1.setVisibility(View.VISIBLE);
                    imageStatusP1.setVisibility(View.GONE);
                }else{
                    status1.setText("Gracias por su calificación");
                    imageStatusA1.setVisibility(View.GONE);
                    imageStatusP1.setVisibility(View.VISIBLE);
                }

                builder.setPositiveButton("Nueva alarma", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        nuevaAlerta();
                        //dialog.cancel();
                    }}
                );

            }
        }
        return builder.create();
    }

    public static void AgregarMarcadorPush(NotificationFirebase noti){
        // For dropping a marker at a point on the Map
        // LatLng currentLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        slp = new SetLocationPush(noti);
        slp.execute((Void) null);
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
                14));
        Log.d("my tag", "Zoom hecho.............................");

        /*googleMap.addMarker(new MarkerOptions().position(currentLatLng)
                .title("Mi posición actual")
                .snippet(address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/

        // For zooming automatically to the location of the marker
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(12).build();
        // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static class SetLocationPush extends AsyncTask<Void, Void, Boolean> {
        NotificationFirebase noti;

        SetLocationPush(NotificationFirebase n){
            noti = n;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            /*noti.setNetwork(params[0].toString());
            noti.setNetworkLatitude(params[1].toString());
            noti.setNetworkLongitude(params[2].toString());
            noti.setStatus(params[3].toString());*/
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (noti.getStatus().equals("en atencion")) {
                MarkerOptions options = new MarkerOptions();
                IconGenerator iconFactory = new IconGenerator(cp);
                iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(DateFormat.getTimeInstance().format(new Date()))));
                options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
                //options.title("Mi posición actual");
                options.snippet(noti.getNetworkAddress());

                LatLng currentLatLng = new LatLng(Double.parseDouble(noti.getNetworkLatitude()), Double.parseDouble(noti.getNetworkLongitude()));
                options.position(currentLatLng);
                Marker mapMarker = googleMap.addMarker(options);
                mapMarker.setTitle(noti.getNetworkCode());
                Log.d("my tag", "Marcador añadido.............................");
                // For zooming automatically to the location of the marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                        14));
                Log.d("my tag", "Zoom hecho.............................");
            }
            notificacion = (noti.getStatus());
            //notificacionId = (noti.getI);
            notificacionUbicacion = (noti.getStatus());
            //createSimpleDialog(notificacion);
        }

        @Override
        protected void onCancelled() {
        }
    }
}
