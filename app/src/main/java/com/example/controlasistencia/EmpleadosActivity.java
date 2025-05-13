package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import com.example.controlasistencia.adapter.EmpleadosAdapter;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.modelo.Empleado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmpleadosActivity extends AppCompatActivity {
    private EmpleadoDAO empleadoDAO;
    private ListView listViewEmpleados;
    private FloatingActionButton fabAgregarEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);
        setTitle("Empleados");

        empleadoDAO = new EmpleadoDAO(this);
        empleadoDAO.open();

        listViewEmpleados = findViewById(R.id.listViewEmpleados);
        fabAgregarEmpleado = findViewById(R.id.fabAgregarEmpleado);

        fabAgregarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmpleadosActivity.this, EmpleadoFormActivity.class));
            }
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

    private void cargarEmpleados() {
        //List<Empleado> empleados = empleadoDAO.getAllEmpleados();
        //EmpleadosAdapter adapter = new EmpleadosAdapter(this, empleados);
        //listViewEmpleados.setAdapter(adapter);

        empleadoDAO.cargarEmpleadoGoogle(new EmpleadoDAO.EmpleadoCallback() {

            @Override
            public void onEmpleadosCargados(List<Empleado> empleados) {
                Log.d("EmpleadosCargados", "Empleados cargados: " + empleados.size());
                Log.d("EmpleadosCargados", "Empleados cargados: " + empleados);
                EmpleadosAdapter adapter = new EmpleadosAdapter(EmpleadosActivity.this, empleados);
                listViewEmpleados.setAdapter(adapter);
            }

            @Override
            public void onError(String mensajeError) {
                // Manejar el error si es necesario
            }

            @Override
            public void onEmpleadoCreado(String mensajeExito) {

            }

            @Override
            public void onErrorEmpleado(String mensajeError) {

            }
        });
    }
} 