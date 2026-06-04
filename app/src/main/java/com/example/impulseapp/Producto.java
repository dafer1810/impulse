package com.example.impulseapp;

import com.google.firebase.firestore.PropertyName;

public class Producto {
    private String codigo;
    private String categoria;
    private String nombre;
    private int stock;
    private double precioCompra;
    private double precioVenta;

    public Producto() {} // Necesario para Firebase

    public Producto(String codigo, String categoria, String nombre, int stock, double precioCompra, double precioVenta) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.nombre = nombre;
        this.stock = stock;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    @PropertyName("codigo")
    public String getCodigo() { return codigo; }

    @PropertyName("codigo")
    public void setCodigo(String codigo) { this.codigo = codigo; }

    @PropertyName("categoria")
    public String getCategoria() { return categoria; }

    @PropertyName("categoria")
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @PropertyName("nombre")
    public String getNombre() { return nombre; }

    @PropertyName("nombre")
    public void setNombre(String nombre) { this.nombre = nombre; }

    @PropertyName("stock")
    public int getStock() { return stock; }

    @PropertyName("stock")
    public void setStock(int stock) { this.stock = stock; }

    @PropertyName("precioCompra")
    public double getPrecioCompra() { return precioCompra; }

    @PropertyName("precioCompra")
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    @PropertyName("precioVenta")
    public double getPrecioVenta() { return precioVenta; }

    @PropertyName("precioVenta")
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
}