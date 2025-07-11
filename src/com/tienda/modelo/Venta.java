package com.tienda.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int idVenta;
    private int idCliente;
    private double totalVenta;
    private LocalDateTime fecha;
    private List<VentaItem> items;

    public Venta() {
        this.items = new ArrayList<>();
    }

    public Venta(int idVenta, int idCliente, double totalVenta, LocalDateTime fecha) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.totalVenta = totalVenta;
        this.fecha = fecha;
        this.items = new ArrayList<>();
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<VentaItem> getItems() {
        return items;
    }

    public void setItems(List<VentaItem> items) {
        this.items = items;
    }

    public void addItem(VentaItem item) {
        this.items.add(item);
    }
}
