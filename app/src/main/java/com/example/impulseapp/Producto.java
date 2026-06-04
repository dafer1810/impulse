package com.example.impulseapp;

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

    public String getCodigo() { return codigo; }
    public String getCategoria() { return categoria; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
    public double getPrecioCompra() { return precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
}