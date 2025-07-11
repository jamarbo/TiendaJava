package com.tienda.modelo;

import java.time.LocalDateTime;

public class Pedido {
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private LocalDateTime fecha;

    public Pedido() {}

    public Pedido(int idPedido, int idProducto, int cantidad, LocalDateTime fecha) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
