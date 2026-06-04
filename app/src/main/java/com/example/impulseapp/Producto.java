package com.example.impulseapp;

import com.google.firebase.firestore.PropertyName;

public class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private int stock;
    private double precioVenta;

    public Producto() {} // Necesario para Firebase

    @PropertyName("Código")
    public String getCodigo() { return codigo; }

    @PropertyName("Código")
    public void setCodigo(Object codigo) { 
        this.codigo = String.valueOf(codigo); 
    }

    @PropertyName("Nombre")
    public String getNombre() { return nombre; }

    @PropertyName("Nombre")
    public void setNombre(String nombre) { this.nombre = nombre; }

    @PropertyName("Categoría")
    public String getCategoria() { return categoria; }

    @PropertyName("Categoría")
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @PropertyName("Stock actual")
    public int getStock() { return stock; }

    @PropertyName("Stock actual")
    public void setStock(int stock) { this.stock = stock; }

    @PropertyName("Precio venta")
    public double getPrecioVenta() { return precioVenta; }

    @PropertyName("Precio venta")
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
}