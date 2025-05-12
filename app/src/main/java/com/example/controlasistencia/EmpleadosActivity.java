package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import com.example.controlasistencia.adapter.EmpleadosAdapter;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.modelo.Empleado;
import com.example.controlasistencia.modelo.Departamento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ArrayAdapter;

public class EmpleadosActivity extends AppCompatActivity {
    private EmpleadoDAO empleadoDAO;
    private ListView listViewEmpleados;
    private FloatingActionButton fabAgregarEmpleado;
    private Spinner spFiltroDepartamento;
    private DepartamentoDAO departamentoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);
        setTitle("Empleados");

        empleadoDAO = new EmpleadoDAO(this);
        empleadoDAO.open();

        listViewEmpleados = findViewById(R.id.listViewEmpleados);
        fabAgregarEmpleado = findViewById(R.id.fabAgregarEmpleado);
        departamentoDAO = new DepartamentoDAO(this);
        spFiltroDepartamento = findViewById(R.id.spFiltroDepartamento);

        fabAgregarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmpleadosActivity.this, EmpleadoFormActivity.class));
            }
        });

        cargarFiltroDepartamentos();
        spFiltroDepartamento.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                cargarEmpleados();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        cargarEmpleados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEmpleados();
    }

    @Override
    protected void onDestroy() {
        empleadoDAO.close();
        super.onDestroy();
    }

    private void cargarFiltroDepartamentos() {
        departamentoDAO.open();
        List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
        departamentoDAO.close();
        List<String> opciones = new ArrayList<>();
        opciones.add("Todos");
        for (Departamento d : departamentos) {
            opciones.add(d.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltroDepartamento.setAdapter(adapter);
    }

    private void cargarEmpleados() {
        empleadoDAO.open();
        List<Empleado> empleados;
        int pos = spFiltroDepartamento.getSelectedItemPosition();
        if (pos <= 0) {
            empleados = empleadoDAO.getAllEmpleadosOrdenados();
        } else {
            departamentoDAO.open();
            List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
            departamentoDAO.close();
            int idDepto = departamentos.get(pos - 1).getId();
            empleados = empleadoDAO.getEmpleadosPorDepartamento(idDepto);
        }
        empleadoDAO.close();
        EmpleadosAdapter adapter = new EmpleadosAdapter(this, empleados);
        listViewEmpleados.setAdapter(adapter);
    }
} 