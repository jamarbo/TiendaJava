package com.tienda.vista;

import com.tienda.controlador.ProductoDAO;
import com.tienda.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelProductos extends JPanel {
    private ProductoDAO productoDAO = new ProductoDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PanelProductos() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Tipo", "IVA", "Precio", "Stock", "Mínimo", "Pedido"}, 0
        );
        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarProductos();

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoDAO.listarProductos();

        for (Producto p : productos) {
            double iva = calcularIVA(p.getTipo());
            double precioFinal = p.getPrecioBase() * (1 + iva);

            String pedido = (p.getCantidadActual() <= p.getCantidadMinima()) ? "SI" : "NO";

            modeloTabla.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getTipo(),
                    String.format("%.1f%%", iva * 100),
                    String.format("$%.1f", precioFinal),
                    p.getCantidadActual(),
                    p.getCantidadMinima(),
                    pedido
            });
        }
    }

    private double calcularIVA(String tipo) {
        switch (tipo.toLowerCase()) {
            case "papelería": return 0.16;
            case "supermercado": return 0.04;
            case "droguería": return 0.12;
            default: return 0.0;
        }
    }

    private void agregarProducto() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        if (nombre == null || nombre.isEmpty()) return;

        String[] tipos = {"Papelería", "Supermercado", "Droguería"};
        JComboBox<String> comboTipos = new JComboBox<>(tipos);
        int opcion = JOptionPane.showConfirmDialog(this, comboTipos, "Selecciona el tipo", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) return;

        String tipo = (String) comboTipos.getSelectedItem();

        try {
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio base:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Cantidad actual:"));
            int minimo = Integer.parseInt(JOptionPane.showInputDialog("Cantidad mínima:"));

            Producto p = new Producto(0, nombre, tipo, precio, stock, minimo);
            if (productoDAO.agregarProducto(p)) {
                cargarProductos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida");
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = JOptionPane.showInputDialog("Nuevo nombre:", modeloTabla.getValueAt(fila, 1));

        String[] tipos = {"Papelería", "Supermercado", "Droguería"};
        JComboBox<String> comboTipos = new JComboBox<>(tipos);
        comboTipos.setSelectedItem(modeloTabla.getValueAt(fila, 2));
        int opcion = JOptionPane.showConfirmDialog(this, comboTipos, "Selecciona nuevo tipo", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) return;

        String tipo = (String) comboTipos.getSelectedItem();

        try {
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Nuevo precio base:", modeloTabla.getValueAt(fila, 4).toString().replace("$", "")));
            int stock = Integer.parseInt(JOptionPane.showInputDialog("Nueva cantidad actual:", modeloTabla.getValueAt(fila, 5)));
            int minimo = Integer.parseInt(JOptionPane.showInputDialog("Nueva cantidad mínima:", modeloTabla.getValueAt(fila, 6)));

            Producto p = new Producto(id, nombre, tipo, precio, stock, minimo);
            if (productoDAO.actualizarProducto(p)) {
                cargarProductos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida");
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        int id = (int) modeloTabla.getValueAt(fila, 0);

        // 👇 Validar si tiene ventas asociadas
        if (productoDAO.tieneVentasAsociadas(id)) {
            JOptionPane.showMessageDialog(this,
                    "❌ No se puede eliminar: el producto tiene ventas registradas.",
                    "Eliminación no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminarProducto(id)) {
                JOptionPane.showMessageDialog(this, "✅ Producto eliminado");
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar producto");
            }
        }
    }
}
