package com.example.impulseapp;

import java.util.List;

public class Venta {
    private String id;
    private double total;
    private String fecha;
    private List<ItemFactura> items;

    public Venta() {}

    public Venta(String id, double total, String fecha, List<ItemFactura> items) {
        this.id = id;
        this.total = total;
        this.fecha = fecha;
        this.items = items;
    }

    public String getId() { return id; }
    public double getTotal() { return total; }
    public String getFecha() { return fecha; }
    public List<ItemFactura> getItems() { return items; }

    public void setId(String id) { this.id = id; }
    public void setTotal(double total) { this.total = total; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setItems(List<ItemFactura> items) { this.items = items; }
}