package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import com.example.controlasistencia.adapter.DepartamentosAdapter;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.modelo.Departamento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DepartamentosActivity extends AppCompatActivity {
    private DepartamentoDAO departamentoDAO;
    private ListView listViewDepartamentos;
    private FloatingActionButton fabAgregarDepartamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamentos);
        setTitle("Departamentos");

        departamentoDAO = new DepartamentoDAO(this);
        departamentoDAO.open();

        listViewDepartamentos = findViewById(R.id.listViewDepartamentos);
        fabAgregarDepartamento = findViewById(R.id.fabAgregarDepartamento);

        fabAgregarDepartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepartamentosActivity.this, DepartamentoFormActivity.class));
            }
        });

        cargarDepartamentos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDepartamentos();
    }

    @Override
    protected void onDestroy() {
        departamentoDAO.close();
        super.onDestroy();
    }

    private void cargarDepartamentos() {
        List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
        DepartamentosAdapter adapter = new DepartamentosAdapter(this, departamentos);
        listViewDepartamentos.setAdapter(adapter);
    }
} 