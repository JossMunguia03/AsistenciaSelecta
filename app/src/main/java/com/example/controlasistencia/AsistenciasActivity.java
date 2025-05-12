package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import com.example.controlasistencia.adapter.AsistenciasAdapter;
import com.example.controlasistencia.dao.AsistenciaDAO;
import com.example.controlasistencia.modelo.Asistencia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ArrayAdapter;

public class AsistenciasActivity extends AppCompatActivity {
    private AsistenciaDAO asistenciaDAO;
    private ListView listViewAsistencias;
    private FloatingActionButton fabAgregarAsistencia;
    private Spinner spOrdenAsistencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencias);
        setTitle("Asistencias");

        asistenciaDAO = new AsistenciaDAO(this);
        asistenciaDAO.open();

        listViewAsistencias = findViewById(R.id.listViewAsistencias);
        fabAgregarAsistencia = findViewById(R.id.fabAgregarAsistencia);
        spOrdenAsistencias = findViewById(R.id.spOrdenAsistencias);

        fabAgregarAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AsistenciasActivity.this, AsistenciaFormActivity.class));
            }
        });

        cargarSpinnerOrden();
        spOrdenAsistencias.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                cargarAsistencias();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
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

    private void cargarSpinnerOrden() {
        String[] opciones = {"Más recientes primero", "Más antiguos primero"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrdenAsistencias.setAdapter(adapter);
    }

    private void cargarAsistencias() {
        asistenciaDAO.open();
        boolean descendente = spOrdenAsistencias.getSelectedItemPosition() == 0;
        List<Asistencia> asistencias = asistenciaDAO.getAsistenciasOrdenadasPorFechaHora(descendente);
        asistenciaDAO.close();
        AsistenciasAdapter adapter = new AsistenciasAdapter(this, asistencias);
        listViewAsistencias.setAdapter(adapter);
    }
} 