package com.example.controlasistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import com.example.controlasistencia.db.DatabaseHelper;
import com.example.controlasistencia.modelo.Departamento;
import com.example.controlasistencia.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.controlasistencia.modelo.IGoogleSheets;
public class DepartamentoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private IGoogleSheets iGoogleSheets;
    public DepartamentoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public interface DepartamentoCallback {
        void onDepartamentosCargados(List<Departamento> departamentos);
        void onError(String mensajeError);
    }


    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public long insertDepartamento(Departamento departamento) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, departamento.getNombre());
        values.put(DatabaseHelper.COLUMN_DESCRIPCION, departamento.getDescripcion());

        return database.insert(DatabaseHelper.TABLE_DEPARTAMENTOS, null, values);
    }

    public int updateDepartamento(Departamento departamento) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, departamento.getNombre());
        values.put(DatabaseHelper.COLUMN_DESCRIPCION, departamento.getDescripcion());

        return database.update(DatabaseHelper.TABLE_DEPARTAMENTOS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(departamento.getId())});
    }

    public void deleteDepartamento(int id) {
        database.delete(DatabaseHelper.TABLE_DEPARTAMENTOS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public Departamento getDepartamento(int id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_DEPARTAMENTOS,
                null,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Departamento departamento = cursorToDepartamento(cursor);
            cursor.close();
            return departamento;
        }
        return null;
    }
    public void cargarDepartamentoGoogle(DepartamentoCallback callback) {
        List<Departamento> departamentoList;
        departamentoList = new ArrayList<>();

        try {
            iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL); // Usar la función de Common

            // Define los parámetros que necesitas enviar
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("spreadsheetId", Common.GOOGLE_SHEET_ID);
            queryParams.put("sheet", "departamento");

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

                        departamentoList.clear(); // Limpia la lista antes de agregar nuevos elementos
                        for (int i = 0; i < datosArray.length(); i++) {
                            JSONObject object = datosArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String nombre = object.getString("nombre");
                            String descripcion = object.getString("descripcion");

                            Departamento departamento = new Departamento(id, nombre, descripcion);
                            Log.i("departamentos","departamento: " + i + " " + departamento.getNombre());
                            departamentoList.add(departamento);
                        }

                        callback.onDepartamentosCargados(departamentoList);


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

    public List<Departamento> getAllDepartamentos() {
        List<Departamento> departamentos = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_DEPARTAMENTOS,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                departamentos.add(cursorToDepartamento(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return departamentos;
    }

    private Departamento cursorToDepartamento(Cursor cursor) {
        Departamento departamento = new Departamento();
        departamento.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        departamento.setNombre(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE)));
        departamento.setDescripcion(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPCION)));
        return departamento;
    }
} 