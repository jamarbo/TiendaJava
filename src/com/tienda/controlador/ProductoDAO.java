package com.tienda.controlador;

import com.tienda.modelo.Producto;
import com.tienda.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, tipo, precio_base, cantidad_actual, cantidad_minima) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getTipo());
            stmt.setDouble(3, producto.getPrecioBase());
            stmt.setInt(4, producto.getCantidadActual());
            stmt.setInt(5, producto.getCantidadMinima());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setTipo(rs.getString("tipo"));
                p.setPrecioBase(rs.getDouble("precio_base"));
                p.setCantidadActual(rs.getInt("cantidad_actual"));
                p.setCantidadMinima(rs.getInt("cantidad_minima"));
                productos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, tipo=?, precio_base=?, cantidad_actual=?, cantidad_minima=? WHERE id_producto=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getTipo());
            stmt.setDouble(3, producto.getPrecioBase());
            stmt.setInt(4, producto.getCantidadActual());
            stmt.setInt(5, producto.getCantidadMinima());
            stmt.setInt(6, producto.getIdProducto());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public Producto buscarPorId(int idProducto) {
        String sql = "SELECT * FROM productos WHERE id_producto=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getString("tipo"),
                            rs.getDouble("precio_base"),
                            rs.getInt("cantidad_actual"),
                            rs.getInt("cantidad_minima")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    // ✅ NUEVO: Valida si el producto tiene ventas asociadas
    public boolean tieneVentasAsociadas(int idProducto) {
        String sql = "SELECT COUNT(*) FROM ventas WHERE id_producto=?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // true si hay ventas asociadas
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar ventas asociadas: " + e.getMessage());
        }
        return false;
    }
}
