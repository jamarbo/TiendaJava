package com.tienda.vista;

import com.tienda.controlador.ClienteDAO;
import com.tienda.modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelClientes extends JPanel {
    private ClienteDAO clienteDAO;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public PanelClientes() {
        clienteDAO = new ClienteDAO();
        setLayout(new BorderLayout());

        // Título
        add(new JLabel("Gestión de Clientes", SwingConstants.CENTER), BorderLayout.NORTH);

        // Tabla de clientes
        tablaClientes = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        // Action Listeners
        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

        cargarClientes();
    }

    public void cargarClientes() {
        String[] columnas = {"ID", "Tipo", "Nombre", "Dirección", "Teléfono", "Email", "Activo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<Cliente> clientes = clienteDAO.listarClientes();
        for (Cliente cliente : clientes) {
            Object[] fila = {
                    cliente.getId(),
                    cliente.getTipoCliente(),
                    cliente.getNombre(),
                    cliente.getDireccion(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.isActivo() ? "Sí" : "No"
            };
            modeloTabla.addRow(fila);
        }
        tablaClientes.setModel(modeloTabla);
    }

    private void agregarCliente() {
        DialogoCliente dialogo = new DialogoCliente((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialogo.setVisible(true);
        cargarClientes();
    }

    private void modificarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCliente = (int) tablaClientes.getValueAt(filaSeleccionada, 0);

        Cliente clienteSeleccionado = clienteDAO.buscarPorId(idCliente);

        if (clienteSeleccionado != null) {
            DialogoCliente dialogo = new DialogoCliente((JFrame) SwingUtilities.getWindowAncestor(this), clienteSeleccionado);
            dialogo.setVisible(true);
            cargarClientes();
        }
    }

    private void eliminarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            int idCliente = (int) tablaClientes.getValueAt(filaSeleccionada, 0);
            clienteDAO.eliminarCliente(idCliente);
            cargarClientes();
        }
    }
}
