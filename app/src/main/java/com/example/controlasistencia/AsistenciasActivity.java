package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import com.example.controlasistencia.adapter.AsistenciasAdapter;
import com.example.controlasistencia.dao.AsistenciaDAO;
import com.example.controlasistencia.modelo.Asistencia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AsistenciasActivity extends AppCompatActivity {
    private AsistenciaDAO asistenciaDAO;
    private ListView listViewAsistencias;
    private FloatingActionButton fabAgregarAsistencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencias);
        setTitle("Asistencias");

        asistenciaDAO = new AsistenciaDAO(this);
        asistenciaDAO.open();

        listViewAsistencias = findViewById(R.id.listViewAsistencias);
        fabAgregarAsistencia = findViewById(R.id.fabAgregarAsistencia);

        fabAgregarAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AsistenciasActivity.this, AsistenciaFormActivity.class));
            }
        });

        cargarAsistencias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarAsistencias();
    }

    @Override
    protected void onDestroy() {
        asistenciaDAO.close();
        super.onDestroy();
    }

    private void cargarAsistencias() {
        List<Asistencia> asistencias = asistenciaDAO.getAllAsistencias();
        AsistenciasAdapter adapter = new AsistenciasAdapter(this, asistencias);
        listViewAsistencias.setAdapter(adapter);
    }
} 