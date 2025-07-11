package com.tienda.vista;

import com.tienda.controlador.ClienteDAO;
import com.tienda.modelo.Cliente;

import javax.swing.*;
import java.awt.*;

public class DialogoCliente extends JDialog {
    private ClienteDAO clienteDAO;
    private Cliente cliente;
    private boolean esNuevo;

    private JComboBox<Cliente.TipoCliente> comboTipoCliente;
    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JCheckBox chkActivo;

    public DialogoCliente(Frame owner, Cliente cliente) {
        super(owner, true);
        this.clienteDAO = new ClienteDAO();
        this.cliente = cliente;
        this.esNuevo = (cliente == null);

        setTitle(esNuevo ? "Agregar Cliente" : "Modificar Cliente");
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(owner);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboTipoCliente = new JComboBox<>(Cliente.TipoCliente.values());
        txtNombre = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        chkActivo = new JCheckBox("Activo", true);

        panelFormulario.add(new JLabel("Tipo de Cliente:"));
        panelFormulario.add(comboTipoCliente);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(chkActivo);

        add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Action Listeners
        btnGuardar.addActionListener(e -> guardarCliente());
        btnCancelar.addActionListener(e -> dispose());

        if (!esNuevo) {
            cargarDatosCliente();
        }
    }

    private void cargarDatosCliente() {
        comboTipoCliente.setSelectedItem(cliente.getTipoCliente());
        txtNombre.setText(cliente.getNombre());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
        txtEmail.setText(cliente.getEmail());
        chkActivo.setSelected(cliente.isActivo());
    }

    private void guardarCliente() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (esNuevo) {
            cliente = new Cliente();
        }

        cliente.setTipoCliente((Cliente.TipoCliente) comboTipoCliente.getSelectedItem());
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setEmail(txtEmail.getText().trim());
        cliente.setActivo(chkActivo.isSelected());

        if (esNuevo) {
            clienteDAO.agregarCliente(cliente);
        } else {
            clienteDAO.actualizarCliente(cliente);
        }

        dispose();
    }
}

