package com.tienda.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PanelEstadisticas panelEstadisticas;

    public VentanaPrincipal() {
        setTitle("Tienda Latinoamericana");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemGestionar = new JMenuItem("Gestionar Productos");
        menuProductos.add(itemGestionar);
        menuBar.add(menuProductos);

        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemRegistrarVenta = new JMenuItem("Registrar Venta");
        menuVentas.add(itemRegistrarVenta);
        menuBar.add(menuVentas);

        JMenu menuPedidos = new JMenu("Pedidos");
        JMenuItem itemHacerPedido = new JMenuItem("Hacer Pedido");
        menuPedidos.add(itemHacerPedido);
        menuBar.add(menuPedidos);

        JMenu menuClientes = new JMenu("Clientes");
        JMenuItem itemGestionarClientes = new JMenuItem("Gestionar Clientes");
        menuClientes.add(itemGestionarClientes);
        menuBar.add(menuClientes);

        JMenu menuEstadisticas = new JMenu("Estadísticas");
        JMenuItem itemVerEstadisticas = new JMenuItem("Ver Estadísticas");
        menuEstadisticas.add(itemVerEstadisticas);
        menuBar.add(menuEstadisticas);

        setJMenuBar(menuBar);

        JLabel label = new JLabel("Bienvenido a la Tienda Latinoamericana", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        itemGestionar.addActionListener(e -> {
            getContentPane().removeAll();
            add(new PanelProductos());
            revalidate();
            repaint();
        });

        itemRegistrarVenta.addActionListener(e -> {
            getContentPane().removeAll();
            add(new PanelVentas());
            revalidate();
            repaint();
        });

        itemHacerPedido.addActionListener(e -> {
            getContentPane().removeAll();
            add(new PanelPedidos());
            revalidate();
            repaint();
        });

        itemGestionarClientes.addActionListener(e -> {
            getContentPane().removeAll();
            add(new PanelClientes());
            revalidate();
            repaint();
        });

        itemVerEstadisticas.addActionListener(e -> {
            getContentPane().removeAll();
            if (panelEstadisticas == null) {
                panelEstadisticas = new PanelEstadisticas();
            }
            add(panelEstadisticas);
            panelEstadisticas.actualizarEstadisticas();
            revalidate();
            repaint();
        });

        setVisible(true);
    }


    public void actualizarEstadisticas() {
        if (panelEstadisticas != null) {
            panelEstadisticas.actualizarEstadisticas();
        }
    }
}
