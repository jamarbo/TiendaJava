package com.tienda.modelo;

import java.time.LocalDateTime;

public class VentaItem {
    private int idVentaItem;
    private int idVenta;
    private int idProducto;
    private int cantidadVendida;
    private double precioUnitario;
    private double subtotal;
    private Producto producto; // Para tener la info del producto a mano
    private LocalDateTime fechaVenta; // Campo no persistido, para estadísticas

    public VentaItem() {
    }

    public VentaItem(int idVentaItem, int idVenta, int idProducto, int cantidadVendida, double precioUnitario, double subtotal) {
        this.idVentaItem = idVentaItem;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidadVendida = cantidadVendida;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getIdVentaItem() {
        return idVentaItem;
    }

    public void setIdVentaItem(int idVentaItem) {
        this.idVentaItem = idVentaItem;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
}
