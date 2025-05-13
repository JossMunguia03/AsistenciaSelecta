package com.example.controlasistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.controlasistencia.DepartamentosActivity;
import com.example.controlasistencia.adapter.DepartamentosAdapter;
import com.example.controlasistencia.db.DatabaseHelper;
import com.example.controlasistencia.modelo.DataPost;
import com.example.controlasistencia.modelo.Departamento;
import com.example.controlasistencia.modelo.Empleado;
import com.example.controlasistencia.modelo.IGoogleSheets;
import com.example.controlasistencia.utils.Common;
import com.example.controlasistencia.dao.DepartamentoDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private IGoogleSheets iGoogleSheets;
    private DepartamentoDAO departamentoDAO;
    public EmpleadoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL);
    }

    public interface EmpleadoCallback {
        void onEmpleadosCargados(List<Empleado> empleados);
        void onError(String mensajeError);
        void onEmpleadoCreado(String mensajeExito);
        void onErrorEmpleado(String mensajeError);
    }


    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertEmpleado(Empleado empleado) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, empleado.getNombre());
        values.put(DatabaseHelper.COLUMN_APELLIDOS, empleado.getApellidos());
        values.put(DatabaseHelper.COLUMN_ID_DEPARTAMENTO, empleado.getIdDepartamento());
        values.put(DatabaseHelper.COLUMN_PUESTO, empleado.getPuesto());
        values.put(DatabaseHelper.COLUMN_EMAIL, empleado.getEmail());
        values.put(DatabaseHelper.COLUMN_TELEFONO, empleado.getTelefono());

        return database.insert(DatabaseHelper.TABLE_EMPLEADOS, null, values);
    }

    public int updateEmpleado(Empleado empleado) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, empleado.getNombre());
        values.put(DatabaseHelper.COLUMN_APELLIDOS, empleado.getApellidos());
        values.put(DatabaseHelper.COLUMN_ID_DEPARTAMENTO, empleado.getIdDepartamento());
        values.put(DatabaseHelper.COLUMN_PUESTO, empleado.getPuesto());
        values.put(DatabaseHelper.COLUMN_EMAIL, empleado.getEmail());
        values.put(DatabaseHelper.COLUMN_TELEFONO, empleado.getTelefono());

        return database.update(DatabaseHelper.TABLE_EMPLEADOS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(empleado.getId())});
    }

    public void deleteEmpleado(int id) {
        database.delete(DatabaseHelper.TABLE_EMPLEADOS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void cargarEmpleadoGoogle(EmpleadoCallback callback) {
        List<Empleado> empleadoList;
        empleadoList = new ArrayList<>();
        departamentoDAO = new DepartamentoDAO(null);
        departamentoDAO.cargarDepartamentoGoogle(new DepartamentoDAO.DepartamentoCallback() {

            @Override
            public void onDepartamentosCargados(List<Departamento> departamentos) {

                Log.i("uwu","departamentos" + departamentos);
                try {
                    iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL); // Usar la función de Common

                    // Define los parámetros que necesitas enviar
                    Map<String, String> queryParams = new HashMap<>();
                    queryParams.put("spreadsheetId", Common.GOOGLE_SHEET_ID);
                    queryParams.put("sheet", "empleado");

                    Log.d("urlUsada", "Llamando a: " + Common.BASE_URL + "exec con parámetros: " + queryParams);



                    iGoogleSheets.getData(queryParams).enqueue(new Callback<String>() { // Usamos el método con @QueryMap
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                String body = response.body();
                                Log.d("RespuestaGoogle", "Respuesta: " + body);
                                if (body == null) {
                                    callback.onError("Respuesta vacía del servidor");
                                    return;
                                }
                                assert response.body() != null;
                                JSONObject responseObject = new JSONObject(response.body());
                                JSONArray datosArray = responseObject.getJSONArray("datos");

                                empleadoList.clear(); // Limpia la lista antes de agregar nuevos elementos
                                for (int i = 0; i < datosArray.length(); i++) {
                                    JSONObject object = datosArray.getJSONObject(i);
                                    int id = object.getInt("id");
                                    String nombre = object.getString("nombre");
                                    String apellidos = object.getString("apellidos");
                                    int idDepartamento = object.getInt("id departamento");
                                    String puesto = object.getString("puesto");
                                    String email = object.getString("email");
                                    String telefono = object.getString("telefono");

                                    Empleado empleado = new Empleado(id, nombre, apellidos, idDepartamento, puesto, email, telefono);
                                    Log.i("departamentos","departamento: " + i + " " + empleado.getNombre());
                                    empleadoList.add(empleado);
                                }
                                //recorremos la lista de departamentos para obtener el nombre del departamento
                                for (int i = 0; i < empleadoList.size(); i++) {
                                    Empleado empleado = empleadoList.get(i);
                                    int idDepartamento = empleado.getIdDepartamento();
                                    for (int j = 0; j < departamentos.size(); j++) {
                                        Departamento departamento = departamentos.get(j);
                                        if (departamento.getId() == idDepartamento) {
                                            empleado.setNombreDepartamento(departamento.getNombre());
                                            break;
                                        }
                                    }
                                }




                                callback.onEmpleadosCargados(empleadoList);


                            } catch (JSONException e) {
                                Log.e("JSONError", "Error al parsear JSON: " + e.getMessage(), e);
                                callback.onError("Error al parsear JSON: " + e.getMessage());


                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("Retrofit", "Fallo al conectar: " + t.getMessage());
                            callback.onError("Fallo al conectar: " + t.getMessage());


                        }
                    });
                } catch (Exception e) {
                    Log.e("cargaDepartamento", "Error: " + e.getMessage(), e);

                }

            }
            @Override
            public void onError(String mensajeError) {

            }

            @Override
            public void onDepartamentoCreado(String mensajeExito) {

            }

            @Override
            public void onErrorDepartamento(String mensajeError) {

            }
        });

    }
    public void nuevoEmpleado(String nombre, String apellidos, int idDepartamento, String puesto, String email, String telefono, EmpleadoCallback callback){
        List<List<String>> rows = new ArrayList<>();
        List<String> rowData = new ArrayList<>();
        rowData.add(nombre);
        rowData.add(apellidos);
        rowData.add(String.valueOf(idDepartamento));
        rowData.add(puesto);
        rowData.add(email);
        rowData.add(telefono);
        rows.add(rowData);

        DataPost dataPost = new DataPost(
                Common.GOOGLE_SHEET_ID,
                "empleado",
                rows
        );

        iGoogleSheets.sendData(dataPost).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("crearEmpleado", "Respuesta POST: " + response.body());
                    callback.onEmpleadoCreado(response.body()); // Llama al callback con la respuesta
                } else {
                    String errorMessage = "Error al enviar datos. Código: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Mensaje: " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onErrorEmpleado(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("CrearDepartamento", "Fallo al conectar (POST): " + t.getMessage());
                callback.onErrorEmpleado("Error de conexión al enviar datos.");
            }
        });

    }

    public void cargarEmpleadosIdNombre(EmpleadoCallback callback) {
        List<Empleado> empleadoList;
        empleadoList = new ArrayList<>();
        try {
            iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL); // Usar la función de Common
            // Define los parámetros que necesitas enviar
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("spreadsheetId", Common.GOOGLE_SHEET_ID);
            queryParams.put("sheet", "empleado");
            Log.d("urlUsada", "Llamando a: " + Common.BASE_URL + "exec con parámetros: " + queryParams);
            iGoogleSheets.getData(queryParams).enqueue(new Callback<String>() { // Usamos el método con @QueryMap
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        String body = response.body();
                        Log.d("RespuestaGoogle", "Respuesta: " + body);
                        if (body == null) {
                            callback.onError("Respuesta vacía del servidor");
                            return;
                        }
                        assert response.body() != null;
                        JSONObject responseObject = new JSONObject(response.body());
                        JSONArray datosArray = responseObject.getJSONArray("datos");
                        empleadoList.clear(); // Limpia la lista antes de agregar nuevos elementos
                        for (int i = 0; i < datosArray.length(); i++) {
                            JSONObject object = datosArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String nombre = object.getString("nombre");
                            String apellidos = object.getString("apellidos");
                            empleadoList.add(new Empleado(id, nombre, apellidos));

                        }

                        callback.onEmpleadosCargados(empleadoList);

                    } catch (JSONException e) {
                        Log.e("JSONError", "Error al parsear JSON: " + e.getMessage(), e);
                        callback.onError("Error al parsear JSON: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Retrofit", "Fallo al conectar: " + t.getMessage());
                    callback.onError("Fallo al conectar: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("cargaDepartamento", "Error: " + e.getMessage(), e);
        }
    }
    public Empleado getEmpleado(int id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_EMPLEADOS,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Empleado empleado = cursorToEmpleado(cursor);
            cursor.close();
            return empleado;
        }
        return null;
    }

    public List<Empleado> getAllEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_EMPLEADOS,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                empleados.add(cursorToEmpleado(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return empleados;
    }

    private Empleado cursorToEmpleado(Cursor cursor) {
        Empleado empleado = new Empleado();
        empleado.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        empleado.setNombre(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE)));
        empleado.setApellidos(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_APELLIDOS)));
        empleado.setIdDepartamento(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_DEPARTAMENTO)));
        empleado.setPuesto(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PUESTO)));
        empleado.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
        empleado.setTelefono(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TELEFONO)));
        return empleado;
    }
} 