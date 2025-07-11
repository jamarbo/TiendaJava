package com.tienda;

import com.tienda.util.ConexionDB;
import com.tienda.vista.VentanaPrincipal;

import javax.swing.*;

public class AppTienda {
    public static void main(String[] args) {
        // Llama al método para crear las tablas antes de iniciar la UI
        ConexionDB.inicializarBaseDeDatos();
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}
