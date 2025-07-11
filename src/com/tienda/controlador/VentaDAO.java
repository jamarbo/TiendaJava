package com.tienda.controlador;

import com.tienda.modelo.Venta;
import com.tienda.modelo.VentaItem;
import com.tienda.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public boolean registrarVenta(Venta venta) {
        Connection conn = null;
        String sqlVenta = "INSERT INTO ventas (id_cliente, fecha, total_venta) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO venta_items (id_venta, id_producto, cantidad_vendida, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET cantidad_actual = cantidad_actual - ? WHERE id_producto = ?";

        try {
            conn = ConexionDB.conectar();
            conn.setAutoCommit(false); // Iniciar transacción

            // Insertar la venta principal
            try (PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                pstmtVenta.setInt(1, venta.getIdCliente());
                pstmtVenta.setTimestamp(2, Timestamp.valueOf(venta.getFecha()));
                pstmtVenta.setDouble(3, venta.getTotalVenta());
                pstmtVenta.executeUpdate();

                // Obtener el ID de la venta generada
                try (ResultSet generatedKeys = pstmtVenta.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venta.setIdVenta(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la venta, la creación falló.");
                    }
                }
            }

            // Insertar los items de la venta y actualizar stock
            try (PreparedStatement pstmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock)) {

                for (VentaItem item : venta.getItems()) {
                    // Insertar item
                    pstmtItem.setInt(1, venta.getIdVenta());
                    pstmtItem.setInt(2, item.getIdProducto());
                    pstmtItem.setInt(3, item.getCantidadVendida());
                    pstmtItem.setDouble(4, item.getPrecioUnitario());
                    pstmtItem.setDouble(5, item.getSubtotal());
                    pstmtItem.addBatch();

                    // Actualizar stock
                    pstmtUpdateStock.setInt(1, item.getCantidadVendida());
                    pstmtUpdateStock.setInt(2, item.getIdProducto());
                    pstmtUpdateStock.addBatch();
                }
                pstmtItem.executeBatch();
                pstmtUpdateStock.executeBatch();
            }

            conn.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.err.println("Error en la transacción de registro de venta: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir transacción en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    public List<Venta> listarVentas() {
        List<Venta> ventas = new ArrayList<>();
        // Esta consulta ya no es suficiente, pero la dejamos como placeholder.
        // La lógica de estadísticas necesitará su propia consulta optimizada.
        String sql = "SELECT * FROM ventas ORDER BY fecha DESC";
        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setIdVenta(rs.getInt("id_venta"));
                v.setIdCliente(rs.getInt("id_cliente"));
                v.setTotalVenta(rs.getDouble("total_venta"));
                v.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                // NOTA: Para un listado completo, necesitaríamos cargar los items aquí.
                // Por ahora, para las estadísticas, lo manejaremos de forma diferente.
                ventas.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }
        return ventas;
    }

    public List<VentaItem> listarVentaItemsParaEstadisticas() {
        List<VentaItem> items = new ArrayList<>();
        String sql = """
            SELECT 
                vi.id_venta_item, vi.id_venta, vi.id_producto, vi.cantidad_vendida, vi.precio_unitario, vi.subtotal,
                v.fecha,
                p.nombre as nombre_producto
            FROM venta_items vi
            JOIN ventas v ON vi.id_venta = v.id_venta
            JOIN productos p ON vi.id_producto = p.id_producto
            ORDER BY v.fecha DESC
        """;

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VentaItem item = new VentaItem();
                item.setIdVentaItem(rs.getInt("id_venta_item"));
                item.setIdVenta(rs.getInt("id_venta"));
                item.setIdProducto(rs.getInt("id_producto"));
                item.setCantidadVendida(rs.getInt("cantidad_vendida"));
                item.setPrecioUnitario(rs.getDouble("precio_unitario"));
                item.setSubtotal(rs.getDouble("subtotal"));

                // Crear un objeto Producto temporal para almacenar el nombre
                com.tienda.modelo.Producto p = new com.tienda.modelo.Producto();
                p.setNombre(rs.getString("nombre_producto"));
                item.setProducto(p);

                // Asignar la fecha de la venta al campo temporal
                item.setFechaVenta(rs.getTimestamp("fecha").toLocalDateTime());

                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar items de venta para estadísticas: " + e.getMessage());
        }
        return items;
    }
}
