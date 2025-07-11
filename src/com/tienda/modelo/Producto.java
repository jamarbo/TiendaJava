package com.tienda.modelo;

public class Producto {
    private int idProducto;
    private String nombre;
    private String tipo;
    private double precioBase;
    private int cantidadActual;
    private int cantidadMinima;

    public Producto() {}

    public Producto(int idProducto, String nombre, String tipo, double precioBase, int cantidadActual, int cantidadMinima) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioBase = precioBase;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
