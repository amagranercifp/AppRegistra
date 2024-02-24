package com.example.appregistra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appregistra.datos.Tarea;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResumenRegistroTarea extends AppCompatActivity {

    TextView tvIdTarea, tvTecnico, tvUbicaciónIni, tvUbicaciónFin, tvMomentoInicial, tvMomentoFinal;//, tvDescripcion;

    TextInputEditText tvDescripcion;

    Button btnValidar;

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    Tarea tarea;

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
            tarea = (Tarea) intent.getSerializableExtra("tarea");

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



                //En este punto, registramos los datos en Firebase.
                guardarDocumentoFirebase(v.getContext(),tarea);




            }
        });

    }

    public void guardarDocumentoFirebase(Context c, Tarea t){

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Crear un nuevo usuario con información adicional
        // Crea un nuevo mapa para la tarea
        Map<String, Object> tareaMap = new HashMap<>();
        tareaMap.put("idTarea", t.getIdTarea());
        tareaMap.put("nombreTecnico", t.getNombreTecnico());

        // Crea un nuevo mapa para la ubicación inicial
        Map<String, Object> uInicialMap = new HashMap<>();
        uInicialMap.put("latitud", t.getuInicial().getLatitud());
        uInicialMap.put("longitud", t.getuInicial().getLongitud());

        // Agrega el mapa de ubicación inicial al mapa de la tarea
        tareaMap.put("uInicial", uInicialMap);

        // Crea un nuevo mapa para la ubicación final
        Map<String, Object> uFinalMap = new HashMap<>();
        uFinalMap.put("latitud", t.getuFinal().getLatitud());
        uFinalMap.put("longitud", t.getuFinal().getLongitud());

        // Agrega el mapa de ubicación final al mapa de la tarea
        tareaMap.put("uFinal", uFinalMap);

        tareaMap.put("momentoInicial", t.getMomentoInicial());
        tareaMap.put("momentoFinal", t.getMomentoFinal());
        tareaMap.put("descripcion", t.getDescripcion());


        mFirestore.collection("tareas")
                .add(tareaMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("RESUMEN - TAREA REGISTRADA", "Tarea almacenada correctamente en Firestore");


                        Toast.makeText(c,"Tarea registrada correctamente",Toast.LENGTH_LONG).show();

                        Intent i = new Intent(c, MainActivity.class);

                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("RESUMEN - TAREA REGISTRADA", "Error al registrar la práctica en Firestore", e);
                        Toast.makeText(c,"Error al registrar la práctica. Motivo: "+e,Toast.LENGTH_LONG).show();
                    }
                });

    }
}