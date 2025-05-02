package com.example.controlasistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.example.controlasistencia.db.DatabaseHelper;
import com.example.controlasistencia.modelo.Asistencia;

public class AsistenciaDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public AsistenciaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
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
        values.put(DatabaseHelper.COLUMN_FECHA_ENTRADA, asistencia.getFechaEntrada());
        values.put(DatabaseHelper.COLUMN_FECHA_SALIDA, asistencia.getFechaSalida());
        values.put(DatabaseHelper.COLUMN_NOTAS, asistencia.getNotas());

        return database.insert(DatabaseHelper.TABLE_ASISTENCIAS, null, values);
    }

    public int updateAsistencia(Asistencia asistencia) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID_EMPLEADO, asistencia.getIdEmpleado());
        values.put(DatabaseHelper.COLUMN_TIPO_ASISTENCIA, asistencia.getTipoAsistencia());
        values.put(DatabaseHelper.COLUMN_FECHA_ENTRADA, asistencia.getFechaEntrada());
        values.put(DatabaseHelper.COLUMN_FECHA_SALIDA, asistencia.getFechaSalida());
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

    private Asistencia cursorToAsistencia(Cursor cursor) {
        Asistencia asistencia = new Asistencia();
        asistencia.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        asistencia.setIdEmpleado(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_EMPLEADO)));
        asistencia.setTipoAsistencia(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIPO_ASISTENCIA)));
        asistencia.setFechaEntrada(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FECHA_ENTRADA)));
        asistencia.setFechaSalida(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FECHA_SALIDA)));
        asistencia.setNotas(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTAS)));
        return asistencia;
    }
} 