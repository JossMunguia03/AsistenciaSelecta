package com.example.controlasistencia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AsistenciaSelecta.db";
    private static final int DATABASE_VERSION = 1;

    // Tablas
    public static final String TABLE_EMPLEADOS = "tblEmpleados";
    public static final String TABLE_DEPARTAMENTOS = "tblDepartamentos";
    public static final String TABLE_ASISTENCIAS = "tblAsistencias";

    // Columnas comunes
    public static final String COLUMN_ID = "id";
    
    // Columnas Empleados
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDOS = "apellidos";
    public static final String COLUMN_ID_DEPARTAMENTO = "idDepartamento";
    public static final String COLUMN_PUESTO = "puesto";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_TELEFONO = "telefono";

    // Columnas Departamentos
    public static final String COLUMN_DESCRIPCION = "descripcion";

    // Columnas Asistencias
    public static final String COLUMN_ID_EMPLEADO = "idEmpleado";
    public static final String COLUMN_TIPO_ASISTENCIA = "tipoAsistencia";
    public static final String COLUMN_FECHA_ENTRADA = "fechaEntrada";
    public static final String COLUMN_FECHA_SALIDA = "fechaSalida";
    public static final String COLUMN_NOTAS = "notas";

    // Crear tablas
    private static final String CREATE_TABLE_EMPLEADOS = 
        "CREATE TABLE " + TABLE_EMPLEADOS + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_NOMBRE + " TEXT NOT NULL, " +
        COLUMN_APELLIDOS + " TEXT NOT NULL, " +
        COLUMN_ID_DEPARTAMENTO + " INTEGER, " +
        COLUMN_PUESTO + " TEXT, " +
        COLUMN_EMAIL + " TEXT, " +
        COLUMN_TELEFONO + " TEXT, " +
        "FOREIGN KEY(" + COLUMN_ID_DEPARTAMENTO + ") REFERENCES " + 
        TABLE_DEPARTAMENTOS + "(" + COLUMN_ID + "))";

    private static final String CREATE_TABLE_DEPARTAMENTOS = 
        "CREATE TABLE " + TABLE_DEPARTAMENTOS + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_NOMBRE + " TEXT NOT NULL, " +
        COLUMN_DESCRIPCION + " TEXT)";

    private static final String CREATE_TABLE_ASISTENCIAS = 
        "CREATE TABLE " + TABLE_ASISTENCIAS + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_ID_EMPLEADO + " INTEGER, " +
        COLUMN_TIPO_ASISTENCIA + " TEXT NOT NULL, " +
        COLUMN_FECHA_ENTRADA + " TEXT, " +
        COLUMN_FECHA_SALIDA + " TEXT, " +
        COLUMN_NOTAS + " TEXT, " +
        "FOREIGN KEY(" + COLUMN_ID_EMPLEADO + ") REFERENCES " + 
        TABLE_EMPLEADOS + "(" + COLUMN_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEPARTAMENTOS);
        db.execSQL(CREATE_TABLE_EMPLEADOS);
        db.execSQL(CREATE_TABLE_ASISTENCIAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASISTENCIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLEADOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTAMENTOS);
        onCreate(db);
    }
} 