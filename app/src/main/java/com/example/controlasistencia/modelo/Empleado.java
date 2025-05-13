package com.example.controlasistencia.modelo;

public class Empleado {
    private int id;
    private String nombre;
    private String apellidos;
    private int idDepartamento;
    private String puesto;
    private String email;
    private String telefono;
    private String nombreDepartamento; // Nuevo atributo para el nombre del departamento

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellidos, int idDepartamento, String puesto, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idDepartamento = idDepartamento;
        this.puesto = puesto;
        this.email = email;
        this.telefono = telefono;
        this.nombreDepartamento = "sin departamento"; // Inicializa el nombre del departamento
    }
    public Empleado(int id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;

    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public int getIdDepartamento() { return idDepartamento; }
    public String getPuesto() { return puesto; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getNombreDepartamento() { return nombreDepartamento; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setIdDepartamento(int idDepartamento) { this.idDepartamento = idDepartamento; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNombreDepartamento(String nombreDepartamento) { this.nombreDepartamento = nombreDepartamento; }


} 