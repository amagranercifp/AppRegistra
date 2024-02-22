package com.example.appregistra;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class GeoLocalizaTarea extends AppCompatActivity{

    public static final int REQUEST_CODE = 1;

    TextView tvUbicacionGPS;

    MapView map;

    FusedLocationProviderClient fusedLocationProviderClient;

    ProgressBar progressBar;

    double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_localiza_tarea);


        tvUbicacionGPS = findViewById(R.id.tvUbicacionGPS);

        progressBar = findViewById(R.id.progressBar);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(GeoLocalizaTarea.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        } else {

            getCoordenada();
        }

        // Configurar osmdroid antes de inicializar el mapa
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Obtener una referencia al MapView desde tu diseño XML
        map = findViewById(R.id.mapview);

        // Configurar el mapa
        map.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(true);
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
                getCoordenada();
            } else {
                Toast.makeText(this, "Permiso Denegado ..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCoordenada() {

        try {
            progressBar.setVisibility(View.VISIBLE);
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
                        longitud = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                        tvUbicacionGPS.setText(String.valueOf(latitud)+" "+String.valueOf(longitud));

                        // Llamar a la función CreaUbicacion con latitud y longitud específicas (por ejemplo, Nueva York)
                        CreaUbicacion(map, latitud, longitud);

                        Log.d("UBICACION",""+latitud+","+longitud);
                    }
                    progressBar.setVisibility(View.GONE);
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