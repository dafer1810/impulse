package com.example.impulseapp;

public class Tarea {
    private String titulo;
    private String descripcion;
    private String estado; // "Pendiente", "En progreso", "Completada"
    private String fecha;

    public Tarea() {} // Necesario para Firebase

    public Tarea(String titulo, String descripcion, String estado, String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public String getFecha() { return fecha; }
}