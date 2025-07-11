package com.tienda.controlador;

import com.tienda.modelo.Cliente;
import com.tienda.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (tipo_cliente, nombre, direccion, telefono, email, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getTipoCliente().name());
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getEmail());
            pstmt.setBoolean(6, cliente.isActivo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setTipoCliente(Cliente.TipoCliente.valueOf(rs.getString("tipo_cliente")));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setActivo(rs.getBoolean("activo"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        Cliente cliente = null;
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getInt("id_cliente"));
                    cliente.setTipoCliente(Cliente.TipoCliente.valueOf(rs.getString("tipo_cliente")));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setActivo(rs.getBoolean("activo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
        }
        return cliente;
    }

    public void actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET tipo_cliente = ?, nombre = ?, direccion = ?, telefono = ?, email = ?, activo = ? WHERE id_cliente = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getTipoCliente().name());
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getEmail());
            pstmt.setBoolean(6, cliente.isActivo());
            pstmt.setInt(7, cliente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    public void eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }
}
