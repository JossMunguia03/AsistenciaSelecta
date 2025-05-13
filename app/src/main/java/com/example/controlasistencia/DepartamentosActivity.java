package com.example.controlasistencia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.controlasistencia.adapter.DepartamentosAdapter;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.modelo.Departamento;
import com.example.controlasistencia.modelo.GoogleSheetsResponse;
import com.example.controlasistencia.modelo.IGoogleSheets;
import com.example.controlasistencia.utils.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DepartamentosActivity extends AppCompatActivity {
    private DepartamentoDAO departamentoDAO;
    private ListView listViewDepartamentos;
    private FloatingActionButton fabAgregarDepartamento;
    private IGoogleSheets iGoogleSheets;



    private List<Departamento> departamentoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamentos);
        setTitle("Departamentos");
        departamentoList = new ArrayList<>();
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

    /*
    private void cargarDepartamentos() {
        List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
        //List<Departamento> departamentos = departamentoDAO.cargarDepartamentoGoogle();
        Log.i("uwu","departamentos" + departamentos);
        DepartamentosAdapter adapter = new DepartamentosAdapter(this, departamentos);
        listViewDepartamentos.setAdapter(adapter);

    }


     */
    private void cargarDepartamentos() {
        //List<Departamento> departamentos = departamentoDAO.getAllDepartamentos();
        //List<Departamento> departamentos = departamentoDAO.cargarDepartamentoGoogle();
        //Log.i("uwu","departamentos" + departamentos);
        //DepartamentosAdapter adapter = new DepartamentosAdapter(this, departamentos);
        //listViewDepartamentos.setAdapter(adapter);

        departamentoDAO.cargarDepartamentoGoogle(new DepartamentoDAO.DepartamentoCallback() {
            @Override
            public void onDepartamentosCargados(List<Departamento> departamentos) {
                DepartamentosAdapter adapter = new DepartamentosAdapter(DepartamentosActivity.this, departamentos);
                listViewDepartamentos.setAdapter(adapter);
            }
            @Override
            public void onError(String mensajeError) {
                Toast.makeText(DepartamentosActivity.this, mensajeError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDepartamentoCreado(String mensajeExito) {

            }

            @Override
            public void onErrorDepartamento(String mensajeError) {

            }
        });

        }
    }


