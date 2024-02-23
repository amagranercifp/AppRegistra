package com.example.appregistra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.appregistra.adapters.TareasRecyclerViewAdapter;
import com.example.appregistra.datos.Tarea;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListarTareasRegistradas extends AppCompatActivity {

    private TareasRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;

    private ArrayList<Tarea> practicasList = new ArrayList<>();

    FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_tareas_registradas);


        adapter = new TareasRecyclerViewAdapter(practicasList);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void recuperaPracticasFirebase(){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        LocalDate fechaActual = LocalDate.now();

        // Obtener el primer día del mes
        LocalDate primerDiaMes = fechaActual.withDayOfMonth(1);
        String primerDiaMesString = primerDiaMes.format(DateTimeFormatter.ISO_DATE);

        // Obtener el último día del mes
        LocalDate ultimoDiaMes = fechaActual.withDayOfMonth(fechaActual.lengthOfMonth());
        String ultimoDiaMesString = ultimoDiaMes.format(DateTimeFormatter.ISO_DATE);

        db.collection("tareas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CONSULTA_TAREAS", document.getId() + " => " + document.getData());
                                t = new Tarea(document.getData().get("idtarea"),tvTecnico.getText().toString(),uInicial,currentDateTime.toString());

                                practicasList.add(p);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("CONSULTA_TAREAS", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}