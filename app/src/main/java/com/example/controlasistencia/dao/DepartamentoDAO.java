package com.example.controlasistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.example.controlasistencia.db.DatabaseHelper;
import com.example.controlasistencia.modelo.Departamento;

public class DepartamentoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DepartamentoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
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

    public List<Departamento> getAllDepartamentos() {
        List<Departamento> departamentos = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_DEPARTAMENTOS,
                null, null, null, null, null, DatabaseHelper.COLUMN_NOMBRE + " ASC");

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