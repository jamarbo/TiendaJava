package com.tienda.vista;

import com.tienda.controlador.ProductoDAO;
import com.tienda.controlador.VentaDAO;
import com.tienda.modelo.VentaItem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PanelEstadisticas extends JPanel {
    private VentaDAO ventaDAO = new VentaDAO();
    private ProductoDAO productoDAO = new ProductoDAO();

    private JLabel lblMasVendido;
    private JLabel lblMenosVendido;
    private JLabel lblTotalDinero;
    private JLabel lblPromedioVenta;

    private JTable tablaVentas;
    private DecimalFormat formatoMoneda = new DecimalFormat("$###,###,###");
    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PanelEstadisticas() {
        setLayout(new BorderLayout());

        // Panel superior con estadísticas
        JPanel panelResumen = new JPanel(new GridLayout(2, 2, 10, 10));

        Font fontGrande = new Font("Arial", Font.BOLD, 16);

        lblMasVendido = new JLabel("Producto más vendido: -");
        lblMasVendido.setFont(fontGrande);

        lblMenosVendido = new JLabel("Producto menos vendido: -");
        lblMenosVendido.setFont(fontGrande);

        lblTotalDinero = new JLabel("Total dinero: $0.00");
        lblTotalDinero.setFont(fontGrande);

        lblPromedioVenta = new JLabel("Promedio de venta: $0.00");
        lblPromedioVenta.setFont(fontGrande);

        panelResumen.add(lblMasVendido);
        panelResumen.add(lblMenosVendido);
        panelResumen.add(lblTotalDinero);
        panelResumen.add(lblPromedioVenta);

        add(panelResumen, BorderLayout.NORTH);


        tablaVentas = new JTable();
        tablaVentas.setShowGrid(false);
        tablaVentas.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = tablaVentas.getTableHeader();
        header.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollVentas = new JScrollPane(tablaVentas);
        add(scrollVentas, BorderLayout.CENTER);

        actualizarEstadisticas();
    }

    public void actualizarEstadisticas() {
        cargarVentas();
        calcularResumen();
    }

    private void cargarVentas() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Fecha", "Producto", "Cantidad", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<VentaItem> itemsVendidos = ventaDAO.listarVentaItemsParaEstadisticas();

        for (VentaItem item : itemsVendidos) {
            modelo.addRow(new Object[]{
                    item.getFechaVenta().format(formatoFecha),
                    item.getProducto() != null ? item.getProducto().getNombre() : "Desconocido",
                    item.getCantidadVendida(),
                    formatoMoneda.format(item.getSubtotal())
            });
        }
        tablaVentas.setModel(modelo);

        // Alinear columnas
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaVentas.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaVentas.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    private void calcularResumen() {
        List<VentaItem> itemsVendidos = ventaDAO.listarVentaItemsParaEstadisticas();

        if (itemsVendidos.isEmpty()) {
            lblMasVendido.setText("Producto más vendido: -");
            lblMenosVendido.setText("Producto menos vendido: -");
            lblTotalDinero.setText("Total dinero: $0.00");
            lblPromedioVenta.setText("Promedio de venta: $0.00");
            return;
        }

        var conteoProductos = itemsVendidos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        VentaItem::getIdProducto,
                        java.util.stream.Collectors.summingInt(VentaItem::getCantidadVendida)
                ));

        Optional<Integer> idMasVendido = conteoProductos.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey);

        Optional<Integer> idMenosVendido = conteoProductos.entrySet().stream()
                .min(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey);

        double totalDinero = itemsVendidos.stream()
                .mapToDouble(VentaItem::getSubtotal)
                .sum();

        int totalUnidades = itemsVendidos.stream()
                .mapToInt(VentaItem::getCantidadVendida)
                .sum();

        double promedio = totalUnidades > 0 ? totalDinero / totalUnidades : 0;

        lblMasVendido.setText("Producto más vendido: " +
                (idMasVendido.map(id -> productoDAO.buscarPorId(id).getNombre()).orElse("Desconocido")));

        lblMenosVendido.setText("Producto menos vendido: " +
                (idMenosVendido.map(id -> productoDAO.buscarPorId(id).getNombre()).orElse("Desconocido")));

        lblTotalDinero.setText("Total dinero: " + formatoMoneda.format(totalDinero));
        lblPromedioVenta.setText("Promedio de venta: " + formatoMoneda.format(promedio));
    }
}
