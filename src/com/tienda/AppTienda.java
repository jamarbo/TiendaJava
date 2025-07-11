package com.tienda;

import com.tienda.vista.VentanaPrincipal;

import javax.swing.*;

public class AppTienda {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}
