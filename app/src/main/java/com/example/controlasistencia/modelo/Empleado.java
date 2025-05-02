package com.example.controlasistencia.modelo;

public class Empleado {
    private int id;
    private String nombre;
    private String apellidos;
    private int idDepartamento;
    private String puesto;
    private String email;
    private String telefono;

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
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public int getIdDepartamento() { return idDepartamento; }
    public String getPuesto() { return puesto; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setIdDepartamento(int idDepartamento) { this.idDepartamento = idDepartamento; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
} 