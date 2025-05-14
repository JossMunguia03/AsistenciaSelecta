package com.example.controlasistencia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.controlasistencia.adapter.EmpleadosAdapter;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.dao.GoogleDAO;
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
    private GoogleDAO googleDAO;
    private int empleadoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_form);

        empleadoDAO = new EmpleadoDAO(this);
        departamentoDAO = new DepartamentoDAO(this);
        googleDAO = new GoogleDAO();

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
        /*
        departamentoDAO.open();
        List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
        departamentoDAO.close();

        ArrayAdapter<Departamento> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, departamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(adapter);

         */
        departamentoDAO.cargarDepartamentoGoogle(new DepartamentoDAO.DepartamentoCallback() {
            @Override
            public void onDepartamentosCargados(List<Departamento> departamentos) {
                List<String> nombresDepartamentos = new ArrayList<>();
                for (Departamento departamento : departamentos) {
                    nombresDepartamentos.add( departamento.getId() + " - "  + departamento.getNombre());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EmpleadoFormActivity.this,
                        android.R.layout.simple_spinner_item, nombresDepartamentos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDepartamento.setAdapter(adapter);
            }
            @Override
            public void onError(String mensajeError) {
                Toast.makeText(EmpleadoFormActivity.this, mensajeError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDepartamentoCreado(String mensajeExito) {

            }

            @Override
            public void onErrorDepartamento(String mensajeError) {

            }


        });

    }

    private void cargarEmpleado() {
        /*
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

         */




        empleadoDAO.cargarEmpleadoGoogle(new EmpleadoDAO.EmpleadoCallback() {

            @Override
            public void onEmpleadosCargados(List<Empleado> empleados) {
                for (Empleado empleado : empleados) {
                    if (empleado.getId() == empleadoId) {
                        etNombre.setText(empleado.getNombre());
                        etApellidos.setText(empleado.getApellidos());
                        etPuesto.setText(empleado.getPuesto());
                        etEmail.setText(empleado.getEmail());
                        etTelefono.setText(empleado.getTelefono());
                        break;
                    }
                }







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

    private void guardarEmpleado() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();

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


        String idDepartamento = spDepartamento.getSelectedItem().toString();
        String[] partes = idDepartamento.split(" - ");
        idDepartamento = partes[0];
        Integer.parseInt(idDepartamento);

        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setIdDepartamento(Integer.parseInt(idDepartamento));
        empleado.setPuesto(puesto);
        empleado.setEmail(email);
        empleado.setTelefono(telefono);


        //empleadoDAO.open();
        if (empleadoId != -1) {
            //empleadoDAO.updateEmpleado(empleado);
            //Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();

            Map<String, Object> camposAActializar = new HashMap<>();
            camposAActializar.put("nombre", nombre);
            camposAActializar.put("apellidos", apellidos);
            camposAActializar.put("idDepartamento", idDepartamento);
            camposAActializar.put("puesto", puesto);
            camposAActializar.put("email", email);
            camposAActializar.put("telefono", telefono);

            googleDAO.actualizarDataGoogle(empleadoId, "empleado", camposAActializar, new GoogleDAO.GoogleCallback() {

                @Override
                public void onDataUpdated(String mensajeExito) {
                    Toast.makeText(EmpleadoFormActivity.this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUpdateFailed(String mensajeError) {

                }
            });


        } else {
            /*
            empleadoDAO.insertEmpleado(empleado);
            Toast.makeText(this, "Empleado creado", Toast.LENGTH_SHORT).show();

             */
            empleadoDAO.nuevoEmpleado(nombre, apellidos, Integer.parseInt(idDepartamento), puesto, email, telefono, new EmpleadoDAO.EmpleadoCallback() {
                @Override
                public void onEmpleadosCargados(List<Empleado> empleados) {

                }

                @Override
                public void onError(String mensajeError) {

                }

                @Override
                public void onEmpleadoCreado(String mensajeExito) {
                    Toast.makeText(EmpleadoFormActivity.this, "Empleado creado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorEmpleado(String mensajeError) {
                    Toast.makeText(EmpleadoFormActivity.this, "Error al crear el empleado", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //empleadoDAO.close();

        finish();
    }
} 