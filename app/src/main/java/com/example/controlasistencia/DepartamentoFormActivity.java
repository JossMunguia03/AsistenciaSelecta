package com.example.controlasistencia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.modelo.Departamento;

public class DepartamentoFormActivity extends AppCompatActivity {
    private EditText etNombre;
    private EditText etDescripcion;
    private Button btnGuardar;

    private DepartamentoDAO departamentoDAO;
    private int departamentoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamento_form);

        departamentoDAO = new DepartamentoDAO(this);
        departamentoDAO.open();

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        departamentoId = getIntent().getIntExtra("departamento_id", -1);
        if (departamentoId != -1) {
            setTitle("Editar Departamento");
            cargarDepartamento();
        } else {
            setTitle("Nuevo Departamento");
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDepartamento();
            }
        });
    }

    @Override
    protected void onDestroy() {
        departamentoDAO.close();
        super.onDestroy();
    }

    private void cargarDepartamento() {
        Departamento departamento = departamentoDAO.getDepartamento(departamentoId);
        if (departamento != null) {
            etNombre.setText(departamento.getNombre());
            etDescripcion.setText(departamento.getDescripcion());
        }
    }

    private void guardarDepartamento() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el nombre del departamento", Toast.LENGTH_SHORT).show();
            return;
        }

        Departamento departamento = new Departamento();
        if (departamentoId != -1) {
            departamento.setId(departamentoId);
        }
        departamento.setNombre(nombre);
        departamento.setDescripcion(descripcion);

        if (departamentoId != -1) {
            departamentoDAO.updateDepartamento(departamento);
            Toast.makeText(this, "Departamento actualizado", Toast.LENGTH_SHORT).show();
        } else {
            departamentoDAO.insertDepartamento(departamento);
            Toast.makeText(this, "Departamento creado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
} 