package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnEmpleados;
    private Button btnDepartamentos;
    private Button btnAsistencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Asistencia Selecta");

        btnEmpleados = findViewById(R.id.btnEmpleados);
        btnDepartamentos = findViewById(R.id.btnDepartamentos);
        btnAsistencias = findViewById(R.id.btnAsistencias);

        btnEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EmpleadosActivity.class));
            }
        });

        btnDepartamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DepartamentosActivity.class));
            }
        });

        btnAsistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AsistenciasActivity.class));
            }
        });
    }
} 