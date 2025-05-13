package com.example.controlasistencia.modelo;

public class Asistencia {
    private int id;
    private int idEmpleado;
    private String tipoAsistencia;
    private String fecha;
    private String hora;
    private String notas;

    private String empleado;

    private String apellidoEmpleado;

    public Asistencia() {
    }

    public Asistencia(int id, int idEmpleado, String tipoAsistencia, String fecha, String hora, String notas) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.tipoAsistencia = tipoAsistencia;
        this.fecha = fecha;
        this.hora = hora;
        this.notas = notas;
        this.empleado = "Error";
        this.apellidoEmpleado = "Error";
    }

    // Getters
    public int getId() { return id; }
    public int getIdEmpleado() { return idEmpleado; }
    public String getTipoAsistencia() { return tipoAsistencia; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getNotas() { return notas; }

    public String getEmpleado() { return empleado; }

    public String getApellidoEmpleado() { return apellidoEmpleado; }



    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public void setTipoAsistencia(String tipoAsistencia) { this.tipoAsistencia = tipoAsistencia; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setHora(String hora) { this.hora = hora; }
    public void setNotas(String notas) { this.notas = notas; }

    public void setEmpleado(String empleado) { this.empleado = empleado; }

    public void setApellidoEmpleado(String apellidoEmpleado) { this.apellidoEmpleado = apellidoEmpleado; }

} 