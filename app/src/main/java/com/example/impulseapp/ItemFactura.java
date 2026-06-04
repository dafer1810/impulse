package com.example.impulseapp;

public class ItemFactura {
    private String nombre;
    private String codigo;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public ItemFactura() {}

    public ItemFactura(String nombre, String codigo, int cantidad, double precioUnitario) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
}