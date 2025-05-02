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

import com.example.controlasistencia.dao.AsistenciaDAO;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.modelo.Asistencia;
import com.example.controlasistencia.modelo.Empleado;

public class AsistenciaFormActivity extends AppCompatActivity {
    private Spinner spEmpleado;
    private Spinner spTipoAsistencia;
    private EditText etFechaEntrada;
    private EditText etFechaSalida;
    private EditText etNotas;
    private Button btnGuardar;

    private AsistenciaDAO asistenciaDAO;
    private EmpleadoDAO empleadoDAO;
    private int asistenciaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_form);

        asistenciaDAO = new AsistenciaDAO(this);
        empleadoDAO = new EmpleadoDAO(this);

        spEmpleado = findViewById(R.id.spEmpleado);
        spTipoAsistencia = findViewById(R.id.spTipoAsistencia);
        etFechaEntrada = findViewById(R.id.etFechaEntrada);
        etFechaSalida = findViewById(R.id.etFechaSalida);
        etNotas = findViewById(R.id.etNotas);
        btnGuardar = findViewById(R.id.btnGuardar);

        cargarEmpleados();
        cargarTiposAsistencia();

        asistenciaId = getIntent().getIntExtra("asistencia_id", -1);
        if (asistenciaId != -1) {
            setTitle("Editar Asistencia");
            cargarAsistencia();
        } else {
            setTitle("Nueva Asistencia");
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAsistencia();
            }
        });
    }

    private void cargarEmpleados() {
        empleadoDAO.open();
        List<Empleado> empleados = empleadoDAO.getAllEmpleados();
        empleadoDAO.close();

        ArrayAdapter<Empleado> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, empleados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmpleado.setAdapter(adapter);
    }

    private void cargarTiposAsistencia() {
        String[] tipos = {"Entrada", "Salida", "Incidencia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoAsistencia.setAdapter(adapter);
    }

    private void cargarAsistencia() {
        asistenciaDAO.open();
        Asistencia asistencia = asistenciaDAO.getAsistencia(asistenciaId);
        asistenciaDAO.close();

        if (asistencia != null) {
            // Seleccionar el empleado en el spinner
            empleadoDAO.open();
            List<Empleado> empleados = empleadoDAO.getAllEmpleados();
            empleadoDAO.close();
            for (int i = 0; i < empleados.size(); i++) {
                if (empleados.get(i).getId() == asistencia.getIdEmpleado()) {
                    spEmpleado.setSelection(i);
                    break;
                }
            }

            // Seleccionar el tipo de asistencia
            String[] tipos = {"Entrada", "Salida", "Incidencia"};
            for (int i = 0; i < tipos.length; i++) {
                if (tipos[i].equals(asistencia.getTipoAsistencia())) {
                    spTipoAsistencia.setSelection(i);
                    break;
                }
            }

            etFechaEntrada.setText(asistencia.getFechaEntrada());
            etFechaSalida.setText(asistencia.getFechaSalida());
            etNotas.setText(asistencia.getNotas());
        }
    }

    private void guardarAsistencia() {
        Empleado empleado = (Empleado) spEmpleado.getSelectedItem();
        String tipoAsistencia = spTipoAsistencia.getSelectedItem().toString();
        String fechaEntrada = etFechaEntrada.getText().toString().trim();
        String fechaSalida = etFechaSalida.getText().toString().trim();
        String notas = etNotas.getText().toString().trim();

        if (empleado == null || fechaEntrada.isEmpty()) {
            Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Asistencia asistencia = new Asistencia();
        if (asistenciaId != -1) {
            asistencia.setId(asistenciaId);
        }
        asistencia.setIdEmpleado(empleado.getId());
        asistencia.setTipoAsistencia(tipoAsistencia);
        asistencia.setFechaEntrada(fechaEntrada);
        asistencia.setFechaSalida(fechaSalida);
        asistencia.setNotas(notas);

        asistenciaDAO.open();
        if (asistenciaId != -1) {
            asistenciaDAO.updateAsistencia(asistencia);
            Toast.makeText(this, "Asistencia actualizada", Toast.LENGTH_SHORT).show();
        } else {
            asistenciaDAO.insertAsistencia(asistencia);
            Toast.makeText(this, "Asistencia creada", Toast.LENGTH_SHORT).show();
        }
        asistenciaDAO.close();

        finish();
    }
} 