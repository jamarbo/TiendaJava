package com.tienda.vista;

import com.tienda.controlador.PedidoDAO;
import com.tienda.controlador.ProductoDAO;
import com.tienda.modelo.Pedido;
import com.tienda.modelo.Producto;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class PanelPedidos extends JPanel {
    private ProductoDAO productoDAO = new ProductoDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad;

    public PanelPedidos() {
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Producto:"));
        comboProductos = new JComboBox<>();
        cargarProductos();
        add(comboProductos);

        add(new JLabel("Cantidad a pedir:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        JButton btnHacerPedido = new JButton("Hacer Pedido");
        add(new JLabel());
        add(btnHacerPedido);

        btnHacerPedido.addActionListener(e -> hacerPedido());
    }

    private void cargarProductos() {
        List<Producto> productos = productoDAO.listarProductos();
        for (Producto p : productos) {
            comboProductos.addItem(p);
        }
    }

    private void hacerPedido() {
        Producto producto = (Producto) comboProductos.getSelectedItem();
        if (producto == null) return;

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());

            if (producto.getCantidadActual() > producto.getCantidadMinima()) {
                JOptionPane.showMessageDialog(this, "No se puede registrar el pedido: el stock actual es SUFICIENTE!");
                return;
            }

            Pedido pedido = new Pedido(0, producto.getIdProducto(), cantidad, LocalDateTime.now());
            if (pedidoDAO.registrarPedido(pedido)) {

                producto.setCantidadActual(producto.getCantidadActual() + cantidad);
                productoDAO.actualizarProducto(producto);

                JOptionPane.showMessageDialog(this, "✅ Pedido registrado y stock actualizado");
                txtCantidad.setText("");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar pedido");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
        }
    }
}
