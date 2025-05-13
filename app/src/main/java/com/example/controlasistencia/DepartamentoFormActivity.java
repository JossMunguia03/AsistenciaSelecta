package com.example.controlasistencia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlasistencia.adapter.DepartamentosAdapter;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.modelo.Departamento;
import com.example.controlasistencia.modelo.IGoogleSheets;
import com.example.controlasistencia.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartamentoFormActivity extends AppCompatActivity {
    private EditText etNombre;
    private EditText etDescripcion;
    private Button btnGuardar;

    private List<Departamento> departamentoList;
    private DepartamentoDAO departamentoDAO;
    private int departamentoId = -1;
    IGoogleSheets iGoogleSheets;
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

        /*
        Departamento departamento = departamentoDAO.getDepartamento(departamentoId);
        if (departamento != null) {
            etNombre.setText(departamento.getNombre());
            etDescripcion.setText(departamento.getDescripcion());
        }

         */

        departamentoDAO.cargarDepartamentoGoogle(new DepartamentoDAO.DepartamentoCallback() {
            @Override
            public void onDepartamentosCargados(List<Departamento> departamentos) {
                for (Departamento departamento : departamentos) {
                    if (departamento.getId() == departamentoId) {
                        etNombre.setText(departamento.getNombre());
                        etDescripcion.setText(departamento.getDescripcion());
                        break;
                    }
                }

            }
            @Override
            public void onError(String mensajeError) {
                Toast.makeText(DepartamentoFormActivity.this, mensajeError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDepartamentoCreado(String mensajeExito) {

            }

            @Override
            public void onErrorDepartamento(String mensajeError) {

            }
        });
    }
    private void cargarDepartamentoGoogle(){
        String pathUrl;

/*
        try {
            pathUrl = "?spreadsheetId" + Common.GOOGLE_SHEET_ID + "$sheet=departamento";
            iGoogleSheets.getData(pathUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        assert response.body() != null;
                        JSONObject responseObject = new JSONObject(response.body());
                        JSONArray datosArray = responseObject.getJSONArray("datos");

                        for (int i = 0; i < datosArray.length(); i++){
                            JSONObject object = datosArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String nombre = object.getString("nombre");
                            String descripcion = object.getString("descripcion");

                            Departamento departamento = new Departamento(id, nombre, descripcion);
                            departamentoList.add(departamento);

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

 */
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
            /*
            departamentoDAO.insertDepartamento(departamento);
            Toast.makeText(this, "Departamento creado", Toast.LENGTH_SHORT).show();

             */
            departamentoDAO.nuevoDepartamento(nombre, descripcion, new DepartamentoDAO.DepartamentoCallback() {
                @Override
                public void onDepartamentosCargados(List<Departamento> departamentos) {

                }

                @Override
                public void onError(String mensajeError) {

                }

                @Override
                public void onDepartamentoCreado(String mensajeExito) {
                    Toast.makeText(DepartamentoFormActivity.this, mensajeExito, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorDepartamento(String mensajeError) {
                    Toast.makeText(DepartamentoFormActivity.this, mensajeError, Toast.LENGTH_SHORT).show();
                }
            });
        }

        finish();
    }
} 