package com.example.impulseapp;

import com.google.firebase.firestore.Exclude;

public class Tarea {
    private String id;
    private String titulo;
    private String descripcion;
    private String estado; // "Pendiente", "En progreso", "Completada"
    private String fecha;
    private String asignadoA;

    public Tarea() {} // Necesario para Firebase

    public Tarea(String titulo, String descripcion, String estado, String fecha, String asignadoA) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha = fecha;
        this.asignadoA = asignadoA;
    }

    @Exclude
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getAsignadoA() { return asignadoA; }
    public void setAsignadoA(String asignadoA) { this.asignadoA = asignadoA; }
}