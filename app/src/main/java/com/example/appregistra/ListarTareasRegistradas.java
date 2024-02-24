package com.example.appregistra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.appregistra.adapters.TareasRecyclerViewAdapter;
import com.example.appregistra.datos.Tarea;
import com.example.appregistra.datos.Ubicacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class ListarTareasRegistradas extends AppCompatActivity {

    private TareasRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;

    private ArrayList<Tarea> tareasList = new ArrayList<>();

    FirebaseFirestore  db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_tareas_registradas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new TareasRecyclerViewAdapter(tareasList);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recuperaPracticasFirebase();

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

        // Obtén la referencia de la colección "tareas"
        CollectionReference tareasRef = db.collection("tareas");

        // Ejecuta la consulta para recuperar todos los documentos
        tareasRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Itera sobre los documentos obtenidos
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            // El documento existe, puedes obtener la tarea
                            Map<String, Object> tareaMap = documentSnapshot.getData();
                            if (tareaMap != null) {
                                // Aquí puedes trabajar con los datos recuperados
                                String idTarea = (String) tareaMap.get("idTarea");
                                String nombreTecnico = (String) tareaMap.get("nombreTecnico");
                                // Recupera la ubicación inicial
                                Map<String, Object> uInicialMap = (Map<String, Object>) tareaMap.get("uInicial");
                                Double latitudInicial = (Double) uInicialMap.get("latitud");
                                Double longitudInicial = (Double) uInicialMap.get("longitud");
                                Ubicacion uInicial = new Ubicacion(latitudInicial,longitudInicial);
                                // Recupera la ubicación final
                                Map<String, Object> uFinalMap = (Map<String, Object>) tareaMap.get("uFinal");
                                Double latitudFinal = (Double) uFinalMap.get("latitud");
                                Double longitudFinal = (Double) uFinalMap.get("longitud");
                                Ubicacion uFinal = new Ubicacion(latitudFinal,longitudFinal);

                                String momentoInicial = (String) tareaMap.get("momentoInicial");
                                String momentoFinal = (String) tareaMap.get("momentoFinal");
                                String descripcion = (String) tareaMap.get("descripcion");

                                Tarea t = new Tarea(idTarea,nombreTecnico,uInicial,momentoInicial);

                                t.setMomentoFinal(momentoFinal);
                                t.setDescripcion(descripcion);
                                t.setuFinal(uFinal);

                                tareasList.add(t);

                                // ... Continúa con los demás campos según sea necesario
                                Log.d("Firestore", "Nombre del técnico: " + nombreTecnico);
                                Log.d("Firestore", "Ubicación inicial: (" + latitudInicial + ", " + longitudInicial + ")");
                                Log.d("Firestore", "Ubicación final: (" + latitudFinal + ", " + longitudFinal + ")");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Ocurrió un error al recuperar los documentos
                    Log.w("Firestore", "Error al recuperar documentos", e);
                });
    }
}