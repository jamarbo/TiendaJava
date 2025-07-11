package com.tienda.modelo;

public class Cliente {
    private int id;
    private TipoCliente tipoCliente;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private boolean activo;

    public enum TipoCliente {
        PERSONA_NATURAL,
        PERSONA_JURIDICA
    }

    // Constructores
    public Cliente() {
    }

    public Cliente(int id, TipoCliente tipoCliente, String nombre, String direccion, String telefono, String email, boolean activo) {
        this.id = id;
        this.tipoCliente = tipoCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

