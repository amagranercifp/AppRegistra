package com.example.appregistra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appregistra.datos.Tarea;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class ResumenRegistroTarea extends AppCompatActivity {

    TextView tvIdTarea, tvTecnico, tvUbicaciónIni, tvUbicaciónFin, tvMomentoInicial, tvMomentoFinal;//, tvDescripcion;

    TextInputEditText tvDescripcion;

    Button btnValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_registro_tarea);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvIdTarea = findViewById(R.id.tvIdTarea);
        tvTecnico = findViewById(R.id.tvTecnico);
        tvUbicaciónIni = findViewById(R.id.tvUbicaciónIni);
        tvUbicaciónFin = findViewById(R.id.tvUbicaciónFin);
        tvMomentoInicial = findViewById(R.id.tvMomentoInicial);
        tvMomentoFinal = findViewById(R.id.tvMomentoFinal);
        tvDescripcion = findViewById(R.id.tvDescripcion);

        btnValidar = findViewById(R.id.btnValidar);



        // Recibe la instancia de Tarea desde el Intent
        Intent intent = getIntent();
        if (intent != null) {
            Tarea tarea = (Tarea) intent.getSerializableExtra("tarea");

            // Ahora puedes usar la instancia de Tarea como desees
            if (tarea != null) {
                // Realiza operaciones con la tarea

                tvIdTarea.setText(tarea.getIdTarea());
                tvTecnico.setText(tarea.getNombreTecnico());
                String ubicacion = "("+tarea.getuInicial().getLatitud()+","+tarea.getuInicial().getLongitud()+")";
                tvUbicaciónIni.setText(ubicacion);
                ubicacion = "("+tarea.getuFinal().getLatitud()+","+tarea.getuFinal().getLongitud()+")";
                tvUbicaciónFin.setText( ubicacion);

                tvMomentoInicial.setText(tarea.getMomentoInicial());
                tvMomentoFinal.setText( tarea.getMomentoFinal());

                tvDescripcion.setText(tarea.getDescripcion());



            }
        }

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(),"Tarea registrada correctamente",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(v.getContext(), MainActivity.class);

                startActivity(i);

            }
        });

    }
}