package com.example.appregistra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnRegistrarActividad, btnVisualizarTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnRegistrarActividad = findViewById(R.id.btnRegistrarActividad);
        btnVisualizarTareas = findViewById(R.id.btnVisualizarTareas);


        btnRegistrarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GeoLocalizaTarea.class);

                startActivity(i);
            }
        });

        btnVisualizarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ListarTareasRegistradas.class);

                startActivity(i);
            }
        });

    }
}