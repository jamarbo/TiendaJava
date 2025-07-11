package com.tienda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    private static final String URL = "jdbc:h2:./data/tienda";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            inicializarBD(conn);
            return conn;
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }

    private static void inicializarBD(Connection conn) {
        String sqlProductos = """
            CREATE TABLE IF NOT EXISTS productos (
                id_producto INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                tipo VARCHAR(50) NOT NULL,
                precio_base DOUBLE NOT NULL,
                cantidad_actual INT NOT NULL,
                cantidad_minima INT NOT NULL
            );
        """;

        String sqlDropVentas = "DROP TABLE IF EXISTS ventas;";

        String sqlVentas = """
            CREATE TABLE IF NOT EXISTS ventas (
                id_venta INT AUTO_INCREMENT PRIMARY KEY,
                id_cliente INT,
                fecha TIMESTAMP,
                total_venta DOUBLE,
                FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
            );
        """;

        String sqlVentaItems = """
            CREATE TABLE IF NOT EXISTS venta_items (
                id_venta_item INT AUTO_INCREMENT PRIMARY KEY,
                id_venta INT,
                id_producto INT,
                cantidad_vendida INT,
                precio_unitario DOUBLE,
                subtotal DOUBLE,
                FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
                FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
            );
        """;

        String sqlPedidos = """
            CREATE TABLE IF NOT EXISTS pedidos (
                id_pedido INT AUTO_INCREMENT PRIMARY KEY,
                id_producto INT,
                cantidad INT,
                fecha TIMESTAMP,
                FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
            );
        """;

        String sqlClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                tipo_cliente VARCHAR(50) NOT NULL,
                nombre VARCHAR(255) NOT NULL,
                direccion VARCHAR(255),
                telefono VARCHAR(50),
                email VARCHAR(100),
                activo BOOLEAN NOT NULL
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlDropVentas); // Eliminar la tabla de ventas anterior
            stmt.execute(sqlProductos);
            stmt.execute(sqlVentas);
            stmt.execute(sqlVentaItems);
            stmt.execute(sqlPedidos);
            stmt.execute(sqlClientes);
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }
}
