package com.example.controlasistencia;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.modelo.Departamento;
import com.example.controlasistencia.modelo.Empleado;

public class EmpleadoFormActivity extends AppCompatActivity {
    private EditText etNombre;
    private EditText etApellidos;
    private Spinner spDepartamento;
    private EditText etPuesto;
    private EditText etEmail;
    private EditText etTelefono;
    private Button btnGuardar;

    private EmpleadoDAO empleadoDAO;
    private DepartamentoDAO departamentoDAO;
    private int empleadoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_form);

        empleadoDAO = new EmpleadoDAO(this);
        departamentoDAO = new DepartamentoDAO(this);

        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        spDepartamento = findViewById(R.id.spDepartamento);
        etPuesto = findViewById(R.id.etPuesto);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);

        cargarDepartamentos();

        empleadoId = getIntent().getIntExtra("empleado_id", -1);
        if (empleadoId != -1) {
            setTitle("Editar Empleado");
            cargarEmpleado();
        } else {
            setTitle("Nuevo Empleado");
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEmpleado();
            }
        });
    }

    private void cargarDepartamentos() {
        departamentoDAO.open();
        List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
        departamentoDAO.close();

        ArrayAdapter<Departamento> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, departamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(adapter);
    }

    private void cargarEmpleado() {
        empleadoDAO.open();
        Empleado empleado = empleadoDAO.getEmpleado(empleadoId);
        empleadoDAO.close();

        if (empleado != null) {
            etNombre.setText(empleado.getNombre());
            etApellidos.setText(empleado.getApellidos());
            etPuesto.setText(empleado.getPuesto());
            etEmail.setText(empleado.getEmail());
            etTelefono.setText(empleado.getTelefono());

            // Seleccionar el departamento en el spinner
            for (int i = 0; i < spDepartamento.getCount(); i++) {
                Departamento dept = (Departamento) spDepartamento.getItemAtPosition(i);
                if (dept.getId() == empleado.getIdDepartamento()) {
                    spDepartamento.setSelection(i);
                    break;
                }
            }
        }
    }

    private void guardarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        Departamento departamento = (Departamento) spDepartamento.getSelectedItem();
        String puesto = etPuesto.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || puesto.isEmpty()) {
            Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Empleado empleado = new Empleado();
        if (empleadoId != -1) {
            empleado.setId(empleadoId);
        }
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setIdDepartamento(departamento.getId());
        empleado.setPuesto(puesto);
        empleado.setEmail(email);
        empleado.setTelefono(telefono);

        empleadoDAO.open();
        if (empleadoId != -1) {
            empleadoDAO.updateEmpleado(empleado);
            Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
        } else {
            long id = empleadoDAO.insertEmpleado(empleado);
            Toast.makeText(this, "Empleado creado. ID: " + id, Toast.LENGTH_LONG).show();
        }
        empleadoDAO.close();

        finish();
    }
} 