package com.example.controlasistencia.modelo;

public class Asistencia {
    private int id;
    private int idEmpleado;
    private String tipoAsistencia;
    private String fechaEntrada;
    private String fechaSalida;
    private String notas;

    public Asistencia() {
    }

    public Asistencia(int id, int idEmpleado, String tipoAsistencia, String fechaEntrada, String fechaSalida, String notas) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.tipoAsistencia = tipoAsistencia;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.notas = notas;
    }

    // Getters
    public int getId() { return id; }
    public int getIdEmpleado() { return idEmpleado; }
    public String getTipoAsistencia() { return tipoAsistencia; }
    public String getFechaEntrada() { return fechaEntrada; }
    public String getFechaSalida() { return fechaSalida; }
    public String getNotas() { return notas; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public void setTipoAsistencia(String tipoAsistencia) { this.tipoAsistencia = tipoAsistencia; }
    public void setFechaEntrada(String fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida = fechaSalida; }
    public void setNotas(String notas) { this.notas = notas; }
} 