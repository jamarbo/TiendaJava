package com.tienda.vista;

import com.tienda.controlador.ClienteDAO;
import com.tienda.controlador.ProductoDAO;
import com.tienda.controlador.VentaDAO;
import com.tienda.modelo.Cliente;
import com.tienda.modelo.Producto;
import com.tienda.modelo.Venta;
import com.tienda.modelo.VentaItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PanelVentas extends JPanel {

    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;

    private JComboBox<Cliente> comboClientes;
    private JTextField txtNombre, txtDireccion, txtTelefono, txtEmail;
    private JComboBox<Producto> comboProductos;
    private JSpinner spinnerCantidad;
    private JButton btnAgregarProducto, btnEliminarProducto, btnFinalizarVenta, btnCancelar;
    private JTable tablaItemsVenta;
    private DefaultTableModel modeloTablaItems;
    private JLabel lblTotalVenta;

    private List<VentaItem> carrito;
    private DecimalFormat formatoMoneda = new DecimalFormat("$###,###,###.00");

    public PanelVentas() {
        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        ventaDAO = new VentaDAO();
        carrito = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Cliente
        add(crearPanelCliente(), BorderLayout.NORTH);

        // Panel Central (Agregar Producto y Tabla)
        add(crearPanelCentral(), BorderLayout.CENTER);

        // Panel Sur (Total y Botones Principales)
        add(crearPanelSur(), BorderLayout.SOUTH);

        cargarClientes();
        cargarProductos();
        actualizarTotal();
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("1. Seleccionar Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Selector de cliente
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        comboClientes = new JComboBox<>();
        comboClientes.addActionListener(e -> mostrarDatosCliente());
        panel.add(comboClientes, gbc);

        // Fila 2: Datos del cliente
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        txtNombre.setEditable(false);
        panel.add(txtNombre, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 3;
        txtDireccion = new JTextField(20);
        txtDireccion.setEditable(false);
        panel.add(txtDireccion, gbc);

        // Fila 3: Más datos del cliente
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        txtTelefono.setEditable(false);
        panel.add(txtTelefono, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(20);
        txtEmail.setEditable(false);
        panel.add(txtEmail, gbc);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new TitledBorder("2. Agregar Productos"));

        // Panel para agregar productos
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregar.add(new JLabel("Producto:"));
        comboProductos = new JComboBox<>();
        comboProductos.setPreferredSize(new Dimension(200, 25));
        panelAgregar.add(comboProductos);
        panelAgregar.add(new JLabel("Cantidad:"));
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panelAgregar.add(spinnerCantidad);
        btnAgregarProducto = new JButton("Agregar");
        btnAgregarProducto.addActionListener(e -> agregarProductoAlCarrito());
        panelAgregar.add(btnAgregarProducto);
        btnEliminarProducto = new JButton("Eliminar Seleccionado");
        btnEliminarProducto.addActionListener(e -> eliminarProductoDelCarrito());
        panelAgregar.add(btnEliminarProducto);

        panel.add(panelAgregar, BorderLayout.NORTH);

        // Tabla de items
        String[] columnas = {"Producto", "Cantidad", "Precio Unit.", "Subtotal"};
        modeloTablaItems = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaItemsVenta = new JTable(modeloTablaItems);
        panel.add(new JScrollPane(tablaItemsVenta), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelSur() {
        JPanel panel = new JPanel(new BorderLayout());

        // Total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTotal.add(new JLabel("Total Venta:"));
        lblTotalVenta = new JLabel();
        lblTotalVenta.setFont(new Font("Arial", Font.BOLD, 20));
        panelTotal.add(lblTotalVenta);
        panel.add(panelTotal, BorderLayout.WEST);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFinalizarVenta = new JButton("Finalizar Venta");
        btnFinalizarVenta.addActionListener(e -> finalizarVenta());
        panelBotones.add(btnFinalizarVenta);
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cancelarVenta());
        panelBotones.add(btnCancelar);
        panel.add(panelBotones, BorderLayout.EAST);

        return panel;
    }

    private void cargarClientes() {
        comboClientes.removeAllItems();
        List<Cliente> clientes = clienteDAO.listarClientes();
        // Filtrar solo clientes activos
        clientes.stream()
                .filter(Cliente::isActivo)
                .forEach(comboClientes::addItem);
        mostrarDatosCliente();
    }

    private void cargarProductos() {
        comboProductos.removeAllItems();
        List<Producto> productos = productoDAO.listarProductos();
        productos.forEach(comboProductos::addItem);
    }

    private void mostrarDatosCliente() {
        Cliente clienteSeleccionado = (Cliente) comboClientes.getSelectedItem();
        if (clienteSeleccionado != null) {
            txtNombre.setText(clienteSeleccionado.getNombre());
            txtDireccion.setText(clienteSeleccionado.getDireccion());
            txtTelefono.setText(clienteSeleccionado.getTelefono());
            txtEmail.setText(clienteSeleccionado.getEmail());
        } else {
            txtNombre.setText("");
            txtDireccion.setText("");
            txtTelefono.setText("");
            txtEmail.setText("");
        }
    }

    private void agregarProductoAlCarrito() {
        Producto productoSeleccionado = (Producto) comboProductos.getSelectedItem();
        int cantidad = (int) spinnerCantidad.getValue();

        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantidad > productoSeleccionado.getCantidadActual()) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + productoSeleccionado.getCantidadActual(), "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si el producto ya está en el carrito para actualizar la cantidad
        for (VentaItem item : carrito) {
            if (item.getIdProducto() == productoSeleccionado.getIdProducto()) {
                item.setCantidadVendida(item.getCantidadVendida() + cantidad);
                item.setSubtotal(item.getCantidadVendida() * item.getPrecioUnitario());
                actualizarTablaCarrito();
                actualizarTotal();
                spinnerCantidad.setValue(1);
                return;
            }
        }

        // Si no está en el carrito, se agrega nuevo
        VentaItem nuevoItem = new VentaItem();
        nuevoItem.setIdProducto(productoSeleccionado.getIdProducto());
        nuevoItem.setProducto(productoSeleccionado);
        nuevoItem.setCantidadVendida(cantidad);
        nuevoItem.setPrecioUnitario(productoSeleccionado.getPrecioBase()); // Asumiendo precio base, sin IVA aquí
        nuevoItem.setSubtotal(cantidad * productoSeleccionado.getPrecioBase());
        carrito.add(nuevoItem);

        actualizarTablaCarrito();
        actualizarTotal();
        spinnerCantidad.setValue(1);
    }

    private void eliminarProductoDelCarrito() {
        int filaSeleccionada = tablaItemsVenta.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la lista para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        carrito.remove(filaSeleccionada);
        actualizarTablaCarrito();
        actualizarTotal();
    }

    private void actualizarTablaCarrito() {
        modeloTablaItems.setRowCount(0);
        for (VentaItem item : carrito) {
            Vector<Object> fila = new Vector<>();
            fila.add(item.getProducto().getNombre());
            fila.add(item.getCantidadVendida());
            fila.add(formatoMoneda.format(item.getPrecioUnitario()));
            fila.add(formatoMoneda.format(item.getSubtotal()));
            modeloTablaItems.addRow(fila);
        }
    }

    private void actualizarTotal() {
        double total = carrito.stream().mapToDouble(VentaItem::getSubtotal).sum();
        lblTotalVenta.setText(formatoMoneda.format(total));
    }

    private void finalizarVenta() {
        Cliente clienteSeleccionado = (Cliente) comboClientes.getSelectedItem();
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío. Agregue al menos un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setIdCliente(clienteSeleccionado.getId());
        nuevaVenta.setFecha(LocalDateTime.now());
        nuevaVenta.setItems(carrito);
        nuevaVenta.setTotalVenta(carrito.stream().mapToDouble(VentaItem::getSubtotal).sum());

        boolean exito = ventaDAO.registrarVenta(nuevaVenta);

        if (exito) {
            JOptionPane.showMessageDialog(this, "✅ Venta registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cancelarVenta(); // Limpia el formulario para una nueva venta
            // Actualizar estadísticas si es necesario
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof VentanaPrincipal) {
                VentanaPrincipal vp = (VentanaPrincipal) window;
                vp.actualizarEstadisticas();
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Ocurrió un error al registrar la venta. La operación fue cancelada.", "Error de Transacción", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarVenta() {
        carrito.clear();
        actualizarTablaCarrito();
        actualizarTotal();
        comboClientes.setSelectedIndex(0);
        spinnerCantidad.setValue(1);
    }
}
