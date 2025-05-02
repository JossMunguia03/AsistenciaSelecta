package com.example.controlasistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.example.controlasistencia.db.DatabaseHelper;
import com.example.controlasistencia.modelo.Empleado;

public class EmpleadoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public EmpleadoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
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