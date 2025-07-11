package com.tienda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement; // <-- AÑADE ESTA LÍNEA

public class ConexionDB {
    private static final String URL = "jdbc:h2:./data/tienda";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            // Cargar el driver de H2 explícitamente
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo encontrar el driver de H2. Asegúrate de que el JAR está en el classpath.", e);
        }
    }

    public static Connection conectar() {
        try {
            // SIMPLEMENTE CONECTAR, SIN INICIALIZAR NADA AQUÍ
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }

    public static void inicializarBaseDeDatos() {
        // Sentencias DROP usando CASCADE para forzar el borrado de dependencias.
        // El orden ya no es tan crítico con CASCADE, pero es buena práctica mantenerlo.
        String dropVentaItems = "DROP TABLE IF EXISTS venta_items CASCADE";
        String dropPedidos = "DROP TABLE IF EXISTS pedidos CASCADE";
        String dropVentas = "DROP TABLE IF EXISTS ventas CASCADE";
        String dropProductos = "DROP TABLE IF EXISTS productos CASCADE";
        String dropClientes = "DROP TABLE IF EXISTS clientes CASCADE";

        // Sentencias CREATE en el orden correcto de dependencias
        String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes ("
                + "id_cliente INT AUTO_INCREMENT PRIMARY KEY,"
                + "tipo_cliente VARCHAR(50) NOT NULL,"
                + "nombre VARCHAR(255) NOT NULL,"
                + "direccion VARCHAR(255),"
                + "telefono VARCHAR(50),"
                + "email VARCHAR(100),"
                + "activo BOOLEAN NOT NULL"
                + ");";
        
        String sqlProductos = "CREATE TABLE IF NOT EXISTS productos ("
                + "id_producto INT AUTO_INCREMENT PRIMARY KEY,"
                + "nombre VARCHAR(100) NOT NULL,"
                + "tipo VARCHAR(50) NOT NULL,"
                + "precio_base DOUBLE NOT NULL,"
                + "cantidad_actual INT NOT NULL,"
                + "cantidad_minima INT NOT NULL"
                + ");";

        String sqlVentas = "CREATE TABLE IF NOT EXISTS ventas ("
                + "id_venta INT AUTO_INCREMENT PRIMARY KEY,"
                + "id_cliente INT,"
                + "fecha TIMESTAMP,"
                + "total_venta DOUBLE,"
                + "FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)"
                + ");";
        
        String sqlVentaItems = "CREATE TABLE IF NOT EXISTS venta_items ("
                + "id_venta_item INT AUTO_INCREMENT PRIMARY KEY,"
                + "id_venta INT,"
                + "id_producto INT,"
                + "cantidad_vendida INT,"
                + "precio_unitario DOUBLE,"
                + "subtotal DOUBLE,"
                + "FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE," // Añadido ON DELETE CASCADE
                + "FOREIGN KEY (id_producto) REFERENCES productos(id_producto)"
                + ");";

        String sqlPedidos = "CREATE TABLE IF NOT EXISTS pedidos ("
                + "id_pedido INT AUTO_INCREMENT PRIMARY KEY,"
                + "id_producto INT,"
                + "cantidad INT,"
                + "fecha TIMESTAMP,"
                + "FOREIGN KEY (id_producto) REFERENCES productos(id_producto)"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); // Conexión directa para inicializar
             Statement stmt = conn.createStatement()) {
            
            // 1. Ejecutar DROPs. Gracias a CASCADE, el orden es menos propenso a errores.
            stmt.execute(dropVentaItems);
            stmt.execute(dropPedidos);
            stmt.execute(dropVentas);
            stmt.execute(dropProductos);
            stmt.execute(dropClientes);

            // 2. Ejecutar CREATEs en el orden correcto
            stmt.execute(sqlClientes);
            stmt.execute(sqlProductos);
            stmt.execute(sqlVentas);
            stmt.execute(sqlVentaItems);
            stmt.execute(sqlPedidos);
            
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            throw new RuntimeException("Error al inicializar la base de datos.", e);
        }
    }
}
