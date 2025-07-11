package com.tienda.controlador;

import com.tienda.modelo.Pedido;
import com.tienda.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean registrarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (id_producto, cantidad, fecha) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getIdProducto());
            stmt.setInt(2, pedido.getCantidad());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(pedido.getFecha()));

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar pedido: " + e.getMessage());
            return false;
        }
    }

    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha DESC";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                pedidos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }
}
