package com.example.controlasistencia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.content.Intent;

import com.example.controlasistencia.dao.AsistenciaDAO;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.modelo.Asistencia;
import com.example.controlasistencia.modelo.Empleado;

public class AsistenciaFormActivity extends AppCompatActivity {
    private Spinner spEmpleado;
    private Spinner spTipoAsistencia;
    private EditText etFecha;
    private EditText etHora;
    private EditText etNotas;
    private Button btnGuardar;
    private Button btnSeleccionarFecha;
    private Button btnSeleccionarHora;

    private AsistenciaDAO asistenciaDAO;
    private EmpleadoDAO empleadoDAO;
    private int asistenciaId = -1;

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_form);

        asistenciaDAO = new AsistenciaDAO(this);
        empleadoDAO = new EmpleadoDAO(this);

        spEmpleado = findViewById(R.id.spEmpleado);
        spTipoAsistencia = findViewById(R.id.spTipoAsistencia);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etNotas = findViewById(R.id.etNotas);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);

        cargarEmpleados();
        cargarTiposAsistencia();

        asistenciaId = getIntent().getIntExtra("asistencia_id", -1);
        if (asistenciaId != -1) {
            setTitle("Editar Asistencia");
            cargarAsistencia();
        } else {
            setTitle("Nueva Asistencia");
        }

        btnSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });
        btnSeleccionarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker();
            }
        });
        etHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAsistencia();
            }
        });

        // Inicializar el launcher para permisos
        requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    iniciarEscaneoQR();
                } else {
                    Toast.makeText(this, "Se requiere permiso de cámara para escanear QR", Toast.LENGTH_SHORT).show();
                }
            }
        );

        // Agregar botón de escaneo QR
        Button btnEscanearQR = findViewById(R.id.btnEscanearQR);
        btnEscanearQR.setOnClickListener(v -> verificarPermisoCamara());
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

            etFecha.setText(asistencia.getFecha());
            etHora.setText(asistencia.getHora());
            etNotas.setText(asistencia.getNotas());
        }
    }

    private void guardarAsistencia() {
        Empleado empleado = (Empleado) spEmpleado.getSelectedItem();
        String tipoAsistencia = spTipoAsistencia.getSelectedItem().toString();
        String fecha = etFecha.getText().toString().trim();
        String hora = etHora.getText().toString().trim();
        String notas = etNotas.getText().toString().trim();

        if (empleado == null || fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Asistencia asistencia = new Asistencia();
        if (asistenciaId != -1) {
            asistencia.setId(asistenciaId);
        }
        asistencia.setIdEmpleado(empleado.getId());
        asistencia.setTipoAsistencia(tipoAsistencia);
        asistencia.setFecha(fecha);
        asistencia.setHora(hora);
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

    private void mostrarDatePicker() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String fecha = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
            etFecha.setText(fecha);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void mostrarTimePicker() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = calendar.get(java.util.Calendar.MINUTE);
        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String hora = String.format("%02d:%02d", hourOfDay, minute1);
            etHora.setText(hora);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            iniciarEscaneoQR();
        }
    }

    private void iniciarEscaneoQR() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Escanee el código QR del empleado");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                procesarResultadoQR(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void procesarResultadoQR(String contenido) {
        try {
            int idEmpleado = Integer.parseInt(contenido);
            empleadoDAO.open();
            Empleado empleado = empleadoDAO.getEmpleado(idEmpleado);
            empleadoDAO.close();

            if (empleado != null) {
                // Seleccionar el empleado en el spinner
                List<Empleado> empleados = empleadoDAO.getAllEmpleados();
                for (int i = 0; i < empleados.size(); i++) {
                    if (empleados.get(i).getId() == idEmpleado) {
                        spEmpleado.setSelection(i);
                        break;
                    }
                }

                // Establecer fecha y hora actual
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                
                etFecha.setText(dateFormat.format(calendar.getTime()));
                etHora.setText(timeFormat.format(calendar.getTime()));

                Toast.makeText(this, "Empleado encontrado: " + empleado.getNombre(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Empleado no encontrado", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Código QR inválido", Toast.LENGTH_SHORT).show();
        }
    }
} 