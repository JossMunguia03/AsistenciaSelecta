package com.example.controlasistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.controlasistencia.db.DatabaseHelper;
import com.example.controlasistencia.modelo.Asistencia;
import com.example.controlasistencia.modelo.Departamento;
import com.example.controlasistencia.modelo.Empleado;
import com.example.controlasistencia.modelo.IGoogleSheets;
import com.example.controlasistencia.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsistenciaDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private IGoogleSheets iGoogleSheets;
    public AsistenciaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL);
    }

    public interface AsistenciaCallback {
        void onAsistenciasCargadas(List<Asistencia> asistencias);
        void onError(String mensajeError);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertAsistencia(Asistencia asistencia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID_EMPLEADO, asistencia.getIdEmpleado());
        values.put(DatabaseHelper.COLUMN_TIPO_ASISTENCIA, asistencia.getTipoAsistencia());
        values.put(DatabaseHelper.COLUMN_FECHA, asistencia.getFecha());
        values.put(DatabaseHelper.COLUMN_HORA, asistencia.getHora());
        values.put(DatabaseHelper.COLUMN_NOTAS, asistencia.getNotas());

        return database.insert(DatabaseHelper.TABLE_ASISTENCIAS, null, values);
    }

    public int updateAsistencia(Asistencia asistencia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID_EMPLEADO, asistencia.getIdEmpleado());
        values.put(DatabaseHelper.COLUMN_TIPO_ASISTENCIA, asistencia.getTipoAsistencia());
        values.put(DatabaseHelper.COLUMN_FECHA, asistencia.getFecha());
        values.put(DatabaseHelper.COLUMN_HORA, asistencia.getHora());
        values.put(DatabaseHelper.COLUMN_NOTAS, asistencia.getNotas());

        return database.update(DatabaseHelper.TABLE_ASISTENCIAS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(asistencia.getId())});
    }

    public void deleteAsistencia(int id) {
        database.delete(DatabaseHelper.TABLE_ASISTENCIAS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public Asistencia getAsistencia(int id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_ASISTENCIAS,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Asistencia asistencia = cursorToAsistencia(cursor);
            cursor.close();
            return asistencia;
        }
        return null;
    }

    public List<Asistencia> getAllAsistencias() {
        List<Asistencia> asistencias = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ASISTENCIAS,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                asistencias.add(cursorToAsistencia(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return asistencias;
    }

    public void cargarAsistenciaGoogle(AsistenciaCallback callback) {
        List<Asistencia> asistenciaList;
        asistenciaList = new ArrayList<>();

        EmpleadoDAO empleadoDAO = new EmpleadoDAO(null);
        empleadoDAO.cargarEmpleadosIdNombre(new EmpleadoDAO.EmpleadoCallback() {

            @Override
            public void onEmpleadosCargados(List<Empleado> empleados) {
                Log.i("uwu","empleados" + empleados);


                try {
                    iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL); // Usar la función de Common

                    // Define los parámetros que necesitas enviar
                    Map<String, String> queryParams = new HashMap<>();
                    queryParams.put("spreadsheetId", Common.GOOGLE_SHEET_ID);
                    queryParams.put("sheet", "asistencias");

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

                                asistenciaList.clear(); // Limpia la lista antes de agregar nuevos elementos
                                for (int i = 0; i < datosArray.length(); i++) {
                                    JSONObject object = datosArray.getJSONObject(i);
                                    int id = object.getInt("id");
                                    int idEmpleado = object.getInt("empleado");
                                    String asistencia = object.getString("asistencia");
                                    String fecha = object.getString("fecha");
                                    String hora = object.getString("hora");
                                    String descripcion = object.getString("notas");

                                    Asistencia asistenciaObject = new Asistencia(id, idEmpleado, asistencia, fecha, hora, descripcion);
                                    Log.i("Asistencias","Asistencia: " + i + " " + asistenciaObject.getTipoAsistencia() + " " + asistenciaObject.getFecha() + " " + asistenciaObject.getHora() + " " + asistenciaObject.getNotas());

                                    asistenciaList.add(asistenciaObject);

                                }
                                for (int i = 0; i < asistenciaList.size(); i++) {
                                    Asistencia asistencia = asistenciaList.get(i);
                                    int idEmpleado = asistencia.getIdEmpleado();
                                    for (int j = 0; j < empleados.size(); j++) {
                                        Empleado empleado = empleados.get(j);
                                        if (empleado.getId() == idEmpleado) {
                                            asistencia.setEmpleado(empleado.getNombre());
                                            asistencia.setApellidoEmpleado(empleado.getApellidos());

                                            break;
                                        }
                                    }
                                }

                                callback.onAsistenciasCargadas(asistenciaList);


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
            public void onEmpleadoCreado(String mensajeExito) {

            }

            @Override
            public void onErrorEmpleado(String mensajeError) {

            }
        });







    }

    private Asistencia cursorToAsistencia(Cursor cursor) {
        Asistencia asistencia = new Asistencia();
        asistencia.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        asistencia.setIdEmpleado(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_EMPLEADO)));
        asistencia.setTipoAsistencia(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIPO_ASISTENCIA)));
        asistencia.setFecha(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FECHA)));
        asistencia.setHora(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_HORA)));
        asistencia.setNotas(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTAS)));
        return asistencia;
    }
} 