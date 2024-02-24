package com.example.appregistra;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appregistra.datos.Tarea;
import com.example.appregistra.datos.Ubicacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeoLocalizaTarea extends AppCompatActivity implements OnCoordenadaObtenidaListener {

    public static final int REQUEST_CODE = 1;

    TextView tvUbicacionGPSInicial, tvUbicacionGPSFinal;

    MapView map;

    FusedLocationProviderClient fusedLocationProviderClient;

    ProgressBar progressBarInicial, progressBarFinal;

    TextView tvMomentoInicial,tvDescripcion,tvTecnico;
    TextInputLayout textInputLayout;
    EditText etDescripcion;

    Button btnIniciar;

    Tarea t;

    Ubicacion uInicial = new Ubicacion(0.0,0.0), uFinal = new Ubicacion(0.0,0.0);

    Intent i;


    double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_localiza_tarea);


        tvUbicacionGPSInicial = findViewById(R.id.tvUbicacionGPSInicial);
        //tvUbicacionGPSFinal = findViewById(R.id.tvUbicacionGPSFinal);

        tvTecnico = findViewById(R.id.tvTecnico);

        textInputLayout = findViewById(R.id.textInputLayout);

        etDescripcion = findViewById(R.id.etDescripcion);

        progressBarInicial = findViewById(R.id.progressBarInicial);
        //progressBarFinal = findViewById(R.id.progressBarFinal);

        btnIniciar = findViewById(R.id.btnIniciar);

        // Solicitar permisos y conseguir coordenadas
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(GeoLocalizaTarea.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        } else {

             getCoordenada(uInicial,false,this);
        }

        // Configurar mapa OpenMapStreet

        // Configurar osmdroid antes de inicializar el mapa
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Obtener una referencia al MapView desde tu diseño XML
        map = findViewById(R.id.mapview);

        // Configurar el mapa
        map.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(true);

        tvMomentoInicial = findViewById(R.id.tvMomentoInicial);

        //Establecer momento de inicio de registro de actividad
        String currentDateTime = getCurrentDateTime();

        // Muestra la fecha y hora en el TextView
        tvMomentoInicial.setText("Fecha y hora de acceso: " + currentDateTime);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnIniciar.getText().equals("Iniciar")){
                    textInputLayout.setVisibility(View.VISIBLE);
                    etDescripcion.setVisibility(View.VISIBLE);
                    btnIniciar.setText("Registrar");

                    t = new Tarea("ID1",tvTecnico.getText().toString(),uInicial,currentDateTime.toString());
                }
                else{
                    if (btnIniciar.getText().equals("Registrar")){
                        t.setDescripcion(etDescripcion.getText().toString());

                        getCoordenada(uFinal, true,GeoLocalizaTarea.this);

                        t.setuFinal(uFinal);

                        //Establecer momento de inicio de registro de actividad
                        String currentDateTime = getCurrentDateTime();

                        t.setMomentoFinal(currentDateTime);

                        i = new Intent(v.getContext(), ResumenRegistroTarea.class);

                        i.putExtra("tarea",t);

                        //startActivity(i);
                        //Se hace en el listener onCoordenadaObtenida que espera a recuperar las coordenadas

                    }
                }


            }
        });
    }

    @Override
    public void onCoordenadaObtenida(Ubicacion ubicacion) {
        // Coordenadas obtenidas, puedes realizar la transición de actividad aquí

        Toast.makeText(this,"Coordenadas obtenidas",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCoordenadaObtenidaTransicion() {
        // Coordenadas obtenidas, puedes realizar la transición de actividad aquí

        startActivity(i);
    }

    private String getCurrentDateTime() {
        // Obtén la fecha y hora actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void CreaUbicacion(MapView mapView, double latitud, double longitud) {
        // Configurar una ubicación específica
        GeoPoint ubicacion = new GeoPoint(latitud, longitud);

        // Establecer el zoom manualmente (marcado como obsoleto, pero aún funciona)

        // Agregar un marcador en la ubicación
        Marker ubicacionMarker = new Marker(mapView);
        ubicacionMarker.setPosition(ubicacion);
        ubicacionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(ubicacionMarker);

        mapView.getController().setZoom(15);
        mapView.getController().setCenter(ubicacion);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCoordenada(uInicial,false,this);
            } else {
                Toast.makeText(this, "Permiso Denegado ..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCoordenada(Ubicacion u, Boolean transicion, OnCoordenadaObtenidaListener listener) {

        try {
            progressBarInicial.setVisibility(View.VISIBLE);
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(GeoLocalizaTarea.this).removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        latitud = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        u.setLatitud(latitud);
                        longitud = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        u.setLongitud(longitud);

                        // Llamar al listener con las coordenadas obtenidas
                        if (listener != null && !transicion ) {
                            tvUbicacionGPSInicial.setText("("+String.valueOf(latitud)+","+String.valueOf(longitud)+")");

                            // Llamar a la función CreaUbicacion con latitud y longitud específicas (por ejemplo, Nueva York)
                            CreaUbicacion(map, latitud, longitud);

                            Log.d("UBICACION",""+latitud+","+longitud);

                            listener.onCoordenadaObtenida(u);
                        }
                        else{
                            if(listener != null && transicion){
                                listener.onCoordenadaObtenidaTransicion();
                            }
                        }
                    }
                    progressBarInicial.setVisibility(View.GONE);
                }

            }, Looper.myLooper());

        }catch (Exception ex){
            System.out.println("Error es :" + ex);
        }

    }





    public  void Exit(View view){
        this.finish();
    }

}