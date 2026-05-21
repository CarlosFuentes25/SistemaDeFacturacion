package operaciones;

import facturacion.Cliente;
import facturacion.Devolucion;
import facturacion.EstadoPago;
import facturacion.Factura;
import facturacion.GestorCompra;
import pedido.Pedido;
import stock.CatalogoProducto;
import stock.DetalleProducto;
import stock.Producto;
import ventas.GestorVenta;
import ventas.Vendedor;
import ventas.Venta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PantallaPrincipalController {

    private final PantallaPrincipalView vista;

    private final CatalogoProducto catalogo;
    private final GestorVenta gVenta;
    private final GestorCompra gCompra;
    private final Vendedor vendedor;

    private DefaultTableModel modeloTablaPedido;
    private Pedido pedidoActual;
    private final double PORCENTAJE_IVA = 0.15;

    public PantallaPrincipalController(PantallaPrincipalView vista, CatalogoProducto catalogo,
                                       GestorVenta gVenta, GestorCompra gCompra, Vendedor vendedor) {
        this.vista    = vista;
        this.catalogo = catalogo;
        this.gVenta   = gVenta;
        this.gCompra  = gCompra;
        this.vendedor = vendedor;
        registrarListenersMenu();
    }

    private void registrarListenersMenu() {
        vista.getBtnIniciarPedido()    .addActionListener(new ListenerAbrirFlujoPedido());
        vista.getBtnConsultarProducto().addActionListener(e -> mostrarModuloStock());
        vista.getBtnGenerarProforma()  .addActionListener(e -> ejecutarFlujoProforma());
        vista.getBtnCambiosDevolucion().addActionListener(e -> ejecutarFlujoCambioDevolucion());
        vista.getBtnSalir()            .addActionListener(e -> System.exit(0));
    }

    // =========================================================================
    // MÓDULO: CONTROL TOTAL DE STOCK
    // Tabla con código / nombre / precio / stock / estado.
    // Botón para añadir nuevos productos al catálogo en tiempo real.
    // El stock se descuenta automáticamente en cada venta (lógica en Pedido.agregarProducto).
    // =========================================================================
    private void mostrarModuloStock() {
        vista.actualizarStatus("Módulo de inventario activo...");

        JDialog dialogStock = new JDialog(vista, "Tinta & Trazo — Control de Inventario", true);
        dialogStock.setSize(820, 520);
        dialogStock.setLocationRelativeTo(vista);
        dialogStock.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        dialogStock.setLayout(new BorderLayout(UIThemeManager.GAP_COMPONENTES, UIThemeManager.GAP_COMPONENTES));

        // ── Encabezado ──────────────────────────────────────────────────────
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIThemeManager.MARGEN_PANEL, 12));
        panelTitulo.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelTitulo.setBorder(new MatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));
        JLabel lblTitulo = new JLabel("  Listado de Productos en Stock");
        lblTitulo.setFont(UIThemeManager.FUENTE_TITULO);
        lblTitulo.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        panelTitulo.add(lblTitulo);

        // ── Tabla de inventario ──────────────────────────────────────────────
        String[] columnas = {"Código", "Nombre del Producto", "Precio Unit.", "Stock", "Estado"};
        DefaultTableModel modeloStock = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable tablaStock = new JTable(modeloStock);
        tablaStock.setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        tablaStock.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        tablaStock.setFont(UIThemeManager.FUENTE_ETIQUETA);
        tablaStock.setRowHeight(26);
        tablaStock.setGridColor(UIThemeManager.COLOR_BORDE_PANEL);
        tablaStock.getTableHeader().setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        tablaStock.getTableHeader().setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        tablaStock.getTableHeader().setFont(UIThemeManager.FUENTE_SUBTITULO);

        // Renderizador de la columna Estado con colores semánticos
        tablaStock.getColumnModel().getColumn(4).setCellRenderer(
            (table, value, isSelected, hasFocus, row, col) -> {
                JLabel lbl = new JLabel(value != null ? value.toString() : "");
                lbl.setOpaque(true);
                lbl.setFont(UIThemeManager.FUENTE_CAPTION);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                String estado = value != null ? value.toString() : "";
                if (estado.contains("CRITICO")) {
                    lbl.setBackground(UIThemeManager.PASTEL_PEACH);
                    lbl.setForeground(UIThemeManager.TEXTO_MODULO_PEACH);
                } else {
                    lbl.setBackground(UIThemeManager.PASTEL_MINT);
                    lbl.setForeground(UIThemeManager.TEXTO_MODULO_MINT);
                }
                return lbl;
            });

        // Anchos de columna
        tablaStock.getColumnModel().getColumn(0).setPreferredWidth(90);
        tablaStock.getColumnModel().getColumn(1).setPreferredWidth(280);
        tablaStock.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaStock.getColumnModel().getColumn(3).setPreferredWidth(70);
        tablaStock.getColumnModel().getColumn(4).setPreferredWidth(140);

        // Runnable que recarga la tabla leyendo el catálogo en memoria
        Runnable recargarTabla = () -> {
            modeloStock.setRowCount(0);
            for (Producto p : catalogo.getListaProducto()) {
                String estado = p.getStock() <= 5 ? "STOCK CRITICO" : "Disponible";
                modeloStock.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    String.format("$%.2f", p.getPrecio()),
                    p.getStock(),
                    estado
                });
            }
        };
        recargarTabla.run();

        JScrollPane scroll = new JScrollPane(tablaStock);
        scroll.setBorder(BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL));
        scroll.getViewport().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(0, UIThemeManager.MARGEN_PANEL,
                                             0, UIThemeManager.MARGEN_PANEL));
        panelTabla.add(scroll, BorderLayout.CENTER);

        // ── Barra de acciones inferior ───────────────────────────────────────
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT,
                UIThemeManager.GAP_COMPONENTES, 12));
        panelAcciones.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelAcciones.setBorder(new MatteBorder(1, 0, 0, 0, UIThemeManager.COLOR_BORDE_PANEL));

        JButton btnAgregarProducto = new JButton("+ Anadir Nuevo Producto");
        btnAgregarProducto.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnAgregarProducto.setBackground(UIThemeManager.TEXTO_MODULO_MINT);
        btnAgregarProducto.setForeground(Color.WHITE);
        btnAgregarProducto.setFocusPainted(false);
        btnAgregarProducto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregarProducto.setPreferredSize(new Dimension(210, UIThemeManager.ALTO_BOTON_PRINCIPAL));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnCerrar.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        btnCerrar.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(110, UIThemeManager.ALTO_BOTON_PRINCIPAL));
        btnCerrar.addActionListener(e -> dialogStock.dispose());

        btnAgregarProducto.addActionListener(e -> abrirFormularioNuevoProducto(dialogStock, recargarTabla));

        panelAcciones.add(btnAgregarProducto);
        panelAcciones.add(btnCerrar);

        dialogStock.add(panelTitulo,   BorderLayout.NORTH);
        dialogStock.add(panelTabla,    BorderLayout.CENTER);
        dialogStock.add(panelAcciones, BorderLayout.SOUTH);
        dialogStock.setVisible(true);

        vista.actualizarStatus("Inventario consultado. "
                + catalogo.getListaProducto().size() + " productos registrados.");
    }

    /**
     * Sub-diálogo para registrar un nuevo producto en el catálogo.
     * Valida tipos, unicidad de código y recarga la tabla del padre al guardar.
     */
    private void abrirFormularioNuevoProducto(JDialog padre, Runnable recargarTabla) {
        JDialog dialogForm = new JDialog(padre, "Registrar Nuevo Producto", true);
        dialogForm.setSize(420, 340);
        dialogForm.setLocationRelativeTo(padre);
        dialogForm.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        dialogForm.setLayout(new BorderLayout(10, 10));

        JPanel panelEnc = new JPanel(new FlowLayout(FlowLayout.LEFT, UIThemeManager.MARGEN_PANEL, 10));
        panelEnc.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelEnc.setBorder(new MatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));
        JLabel lblEnc = new JLabel("Ingreso de Mercaderia al Catalogo");
        lblEnc.setFont(UIThemeManager.FUENTE_SUBTITULO);
        lblEnc.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        panelEnc.add(lblEnc);

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 15));
        panelCampos.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelCampos.setBorder(new EmptyBorder(20, 25, 20, 25));

        JTextField txtCodigo = crearCampoFormulario(panelCampos, "Codigo del Producto *:");
        JTextField txtNombre = crearCampoFormulario(panelCampos, "Nombre del Producto *:");
        JTextField txtPrecio = crearCampoFormulario(panelCampos, "Precio Unitario ($) *:");
        JTextField txtStock  = crearCampoFormulario(panelCampos, "Cantidad Inicial *:");

        JButton btnGuardar = new JButton("Guardar en Catalogo");
        btnGuardar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnGuardar.setBackground(UIThemeManager.TEXTO_MODULO_BLUE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(0, 48));

        btnGuardar.addActionListener(e -> {
            if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()
                    || txtPrecio.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialogForm,
                        "Todos los campos marcados con * son obligatorios.",
                        "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stockInicial = Integer.parseInt(txtStock.getText().trim());

                if (precio <= 0 || stockInicial < 0) {
                    JOptionPane.showMessageDialog(dialogForm,
                            "El precio debe ser mayor a cero y el stock no puede ser negativo.",
                            "Valor Invalido", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (catalogo.buscarProducto(txtCodigo.getText().trim()) != null) {
                    JOptionPane.showMessageDialog(dialogForm,
                            "Ya existe un producto con ese codigo en el catalogo.",
                            "Codigo Duplicado", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Producto nuevo = new Producto(
                    txtCodigo.getText().trim().toUpperCase(),
                    txtNombre.getText().trim(),
                    precio,
                    stockInicial
                );
                catalogo.agregarNuevoProducto(nuevo);
                recargarTabla.run();

                JOptionPane.showMessageDialog(dialogForm,
                        "Producto \"" + nuevo.getNombre() + "\" registrado exitosamente.",
                        "Producto Anadido", JOptionPane.INFORMATION_MESSAGE);
                dialogForm.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogForm,
                        "El precio y el stock deben ser valores numericos validos.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialogForm.add(panelEnc,    BorderLayout.NORTH);
        dialogForm.add(panelCampos, BorderLayout.CENTER);
        dialogForm.add(btnGuardar,  BorderLayout.SOUTH);
        dialogForm.setVisible(true);
    }

    // =========================================================================
    // FLUJO: REGISTRAR PEDIDO (sin modificaciones respecto al original)
    // =========================================================================
    private class ListenerAbrirFlujoPedido implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vista.actualizarStatus("Modulo de ventas activo...");
            pedidoActual = new Pedido("PED-" + System.currentTimeMillis());

            JDialog dialogPedido = new JDialog(vista, "Tinta & Trazo - Captura de Pedido", true);
            dialogPedido.setSize(800, 550);
            dialogPedido.setLocationRelativeTo(vista);
            dialogPedido.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
            dialogPedido.setLayout(new BorderLayout(UIThemeManager.GAP_COMPONENTES, UIThemeManager.GAP_COMPONENTES));

            JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, UIThemeManager.GAP_COMPONENTES, 15));
            panelSelector.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
            panelSelector.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));

            JLabel lblBuscar = new JLabel("Buscar (Codigo/Nombre):");
            lblBuscar.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            lblBuscar.setFont(UIThemeManager.FUENTE_ETIQUETA);

            JTextField txtCriterio = new JTextField(12);
            txtCriterio.setFont(UIThemeManager.FUENTE_INPUT);

            JButton btnBuscar = new JButton("Buscar");
            btnBuscar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);

            JLabel lblInfoProducto = new JLabel("Escriba un criterio para iniciar la busqueda.");
            lblInfoProducto.setFont(UIThemeManager.FUENTE_CAPTION);
            lblInfoProducto.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);

            JLabel lblCant = new JLabel("Cantidad:");
            lblCant.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            lblCant.setFont(UIThemeManager.FUENTE_ETIQUETA);

            JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
            spinnerCantidad.setFont(UIThemeManager.FUENTE_INPUT);

            JButton btnAgregarItem = new JButton("Aumentar Producto");
            btnAgregarItem.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
            btnAgregarItem.setBackground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            btnAgregarItem.setForeground(UIThemeManager.COLOR_SUPERFICIE_PANEL);

            panelSelector.add(lblBuscar); panelSelector.add(txtCriterio);
            panelSelector.add(btnBuscar); panelSelector.add(lblCant);
            panelSelector.add(spinnerCantidad); panelSelector.add(btnAgregarItem);

            JPanel panelInfoAux = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
            panelInfoAux.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
            panelInfoAux.add(lblInfoProducto);

            JPanel panelNorte = new JPanel(new BorderLayout());
            panelNorte.add(panelSelector, BorderLayout.CENTER);
            panelNorte.add(panelInfoAux,  BorderLayout.SOUTH);

            String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
            modeloTablaPedido = new DefaultTableModel(columnas, 0);
            JTable tablaPedido = new JTable(modeloTablaPedido);
            tablaPedido.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
            tablaPedido.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            tablaPedido.setFont(UIThemeManager.FUENTE_ETIQUETA);
            tablaPedido.setRowHeight(24);
            JScrollPane scrollTabla = new JScrollPane(tablaPedido);
            scrollTabla.getViewport().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
            scrollTabla.setBorder(BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL));

            JPanel panelTotales = new JPanel();
            panelTotales.setLayout(new BoxLayout(panelTotales, BoxLayout.Y_AXIS));
            panelTotales.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
            panelTotales.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

            JLabel lblSub = new JLabel("Subtotal: $0.00");
            JLabel lblIva = new JLabel("IVA (15%): $0.00");
            JLabel lblTot = new JLabel("Total General: $0.00");
            Font fBig = new Font("SansSerif", Font.BOLD, 14);
            lblSub.setFont(fBig); lblSub.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
            lblIva.setFont(fBig); lblIva.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
            lblTot.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblTot.setForeground(UIThemeManager.PASTEL_VIVIDO_AMBAR);

            panelTotales.add(lblSub); panelTotales.add(Box.createVerticalStrut(8));
            panelTotales.add(lblIva); panelTotales.add(Box.createVerticalStrut(12));
            panelTotales.add(lblTot);

            final Producto[] productoEncontrado = {null};

            btnBuscar.addActionListener(ev -> {
                Producto p = catalogo.buscarProducto(txtCriterio.getText());
                if (p != null) {
                    productoEncontrado[0] = p;
                    String msgStock = p.getStock() <= 5 ? " STOCK CRITICO" : "";
                    lblInfoProducto.setText(String.format("Seleccionado: %s | Precio: $%.2f | Stock: %d%s",
                            p.getNombre(), p.getPrecio(), p.getStock(), msgStock));
                } else {
                    productoEncontrado[0] = null;
                    lblInfoProducto.setText("No se permite registrar productos que no existan en el listado.");
                }
            });

            btnAgregarItem.addActionListener(ev -> {
                if (productoEncontrado[0] == null) {
                    JOptionPane.showMessageDialog(dialogPedido, "Debe buscar y seleccionar un producto valido primero.",
                            "Busqueda Requerida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int cantidad = (int) spinnerCantidad.getValue();
                if (!productoEncontrado[0].verificarDisponibilidad(cantidad)) {
                    JOptionPane.showMessageDialog(dialogPedido,
                            "No se puede agregar esa cantidad. Stock insuficiente.\nDisponible: "
                            + productoEncontrado[0].getStock(), "Limite de Stock", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                pedidoActual.agregarProducto(productoEncontrado[0], cantidad);
                modeloTablaPedido.setRowCount(0);
                for (DetalleProducto dp : pedidoActual.getListaDetalles()) {
                    modeloTablaPedido.addRow(new Object[]{
                        dp.getProducto().getNombre(), dp.getCantidad(),
                        String.format("$%.2f", dp.getPrecioUnitario()),
                        String.format("$%.2f", dp.getSubtotal())});
                }
                double sub = pedidoActual.getTotal();
                double iva = sub * PORCENTAJE_IVA;
                lblSub.setText(String.format("Subtotal: $%.2f", sub));
                lblIva.setText(String.format("IVA (15%%): $%.2f", iva));
                lblTot.setText(String.format("Total General: $%.2f", sub + iva));
                lblInfoProducto.setText(String.format("Seleccionado: %s | Precio: $%.2f | Stock Actualizado: %d",
                        productoEncontrado[0].getNombre(), productoEncontrado[0].getPrecio(),
                        productoEncontrado[0].getStock()));
            });

            JPanel panelAccionesPOS = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIThemeManager.GAP_COMPONENTES, 15));
            panelAccionesPOS.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
            panelAccionesPOS.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIThemeManager.COLOR_BORDE_PANEL));

            JButton btnProcesar = new JButton("Procesar Venta");
            btnProcesar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
            btnProcesar.setBackground(UIThemeManager.TEXTO_MODULO_MINT);
            btnProcesar.setForeground(Color.WHITE);

            JButton btnCancelar = new JButton("Cancelar Pedido");
            btnCancelar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
            btnCancelar.setBackground(UIThemeManager.TEXTO_MODULO_ROSE);
            btnCancelar.setForeground(Color.WHITE);

            panelAccionesPOS.add(btnCancelar);
            panelAccionesPOS.add(btnProcesar);

            btnCancelar.addActionListener(ev -> {
                for (DetalleProducto dp : pedidoActual.getListaDetalles())
                    dp.getProducto().aumentarStock(dp.getCantidad());
                dialogPedido.dispose();
                vista.actualizarStatus("Pedido cancelado. Inventario devuelto.");
            });

            btnProcesar.addActionListener(ev -> {
                if (pedidoActual.getListaDetalles().isEmpty()) {
                    JOptionPane.showMessageDialog(dialogPedido, "El pedido no contiene ningun articulo.",
                            "Carrito Vacio", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                dialogPedido.dispose();
                abrirFormularioCliente();
            });

            JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
            panelCentro.setOpaque(false);
            panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            panelCentro.add(scrollTabla,  BorderLayout.CENTER);
            panelCentro.add(panelTotales, BorderLayout.SOUTH);

            dialogPedido.add(panelNorte,  BorderLayout.NORTH);
            dialogPedido.add(panelCentro, BorderLayout.CENTER);
            dialogPedido.add(panelAccionesPOS, BorderLayout.SOUTH);
            dialogPedido.setVisible(true);
        }
    }

    // =========================================================================
    // FLUJO: COTIZACIÓN / PROFORMA CON FLUJO PROPIO
    // El operador arma el presupuesto desde cero. NO descuenta stock.
    // Al finalizar puede: Descartar o Proceder a Venta Real.
    // =========================================================================
    private void ejecutarFlujoProforma() {
        vista.actualizarStatus("Modulo de cotizaciones activo...");

        // Pedido temporal exclusivo de la proforma; no toca pedidoActual de ventas
        final Pedido pedidoProf = new Pedido("PROF-TMP-" + System.currentTimeMillis());

        JDialog dlg = new JDialog(vista, "Tinta & Trazo - Generar Cotizacion / Proforma", true);
        dlg.setSize(820, 580);
        dlg.setLocationRelativeTo(vista);
        dlg.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        dlg.setLayout(new BorderLayout(UIThemeManager.GAP_COMPONENTES, UIThemeManager.GAP_COMPONENTES));

        // ── Encabezado ──────────────────────────────────────────────────────
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, UIThemeManager.MARGEN_PANEL, 12));
        panelTitulo.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelTitulo.setBorder(new MatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));
        JLabel lblTitulo = new JLabel("  Nueva Cotizacion - Sin validez tributaria");
        lblTitulo.setFont(UIThemeManager.FUENTE_TITULO);
        lblTitulo.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        panelTitulo.add(lblTitulo);

        // ── Buscador ─────────────────────────────────────────────────────────
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIThemeManager.GAP_COMPONENTES, 12));
        panelBusqueda.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelBusqueda.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));

        JLabel lblBuscar = new JLabel("Buscar Producto:");
        lblBuscar.setFont(UIThemeManager.FUENTE_ETIQUETA);
        lblBuscar.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);

        JTextField txtBuscar = new JTextField(14);
        txtBuscar.setFont(UIThemeManager.FUENTE_INPUT);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);

        JLabel lblResultado = new JLabel("Ingrese el nombre o codigo del articulo.");
        lblResultado.setFont(UIThemeManager.FUENTE_CAPTION);
        lblResultado.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);

        JLabel lblCantLabel = new JLabel("Cant.:");
        lblCantLabel.setFont(UIThemeManager.FUENTE_ETIQUETA);
        lblCantLabel.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);

        JSpinner spinCant = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        spinCant.setFont(UIThemeManager.FUENTE_INPUT);

        JButton btnAgregar = new JButton("Agregar a Cotizacion");
        btnAgregar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnAgregar.setBackground(UIThemeManager.TEXTO_MODULO_PEACH);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        panelBusqueda.add(lblBuscar); panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar); panelBusqueda.add(lblCantLabel);
        panelBusqueda.add(spinCant);  panelBusqueda.add(btnAgregar);

        JPanel panelResultadoBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 4));
        panelResultadoBar.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelResultadoBar.add(lblResultado);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelTitulo,       BorderLayout.NORTH);
        panelNorte.add(panelBusqueda,     BorderLayout.CENTER);
        panelNorte.add(panelResultadoBar, BorderLayout.SOUTH);

        // ── Tabla de ítems de la proforma ─────────────────────────────────
        String[] cols = {"Codigo", "Producto", "Cant.", "Precio Unit.", "Subtotal"};
        DefaultTableModel modeloProf = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tablaProf = new JTable(modeloProf);
        tablaProf.setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        tablaProf.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        tablaProf.setFont(UIThemeManager.FUENTE_ETIQUETA);
        tablaProf.setRowHeight(25);
        tablaProf.setGridColor(UIThemeManager.COLOR_BORDE_PANEL);
        tablaProf.getTableHeader().setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        tablaProf.getTableHeader().setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        tablaProf.getTableHeader().setFont(UIThemeManager.FUENTE_SUBTITULO);

        JScrollPane scrollProf = new JScrollPane(tablaProf);
        scrollProf.setBorder(BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL));
        scrollProf.getViewport().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);

        // ── Totales de la proforma ────────────────────────────────────────
        JPanel panelTotalesProf = new JPanel();
        panelTotalesProf.setLayout(new BoxLayout(panelTotalesProf, BoxLayout.Y_AXIS));
        panelTotalesProf.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelTotalesProf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        JLabel lblNota   = new JLabel("* Precios referenciales. IVA al 15%.");
        JLabel lblSubP   = new JLabel("Subtotal: $0.00");
        JLabel lblIvaP   = new JLabel("IVA (15%): $0.00");
        JLabel lblTotP   = new JLabel("Total Estimado: $0.00");

        lblNota.setFont(UIThemeManager.FUENTE_CAPTION);
        lblNota.setForeground(UIThemeManager.TEXTO_MODULO_PEACH);
        Font fB = new Font("SansSerif", Font.BOLD, 13);
        lblSubP.setFont(fB); lblSubP.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
        lblIvaP.setFont(fB); lblIvaP.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
        lblTotP.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotP.setForeground(UIThemeManager.PASTEL_VIVIDO_AMBAR);

        panelTotalesProf.add(lblNota);
        panelTotalesProf.add(Box.createVerticalStrut(8));
        panelTotalesProf.add(lblSubP);
        panelTotalesProf.add(Box.createVerticalStrut(5));
        panelTotalesProf.add(lblIvaP);
        panelTotalesProf.add(Box.createVerticalStrut(8));
        panelTotalesProf.add(lblTotP);

        // Runnable para refrescar los totales tras cada adición
        Runnable actualizarTotales = () -> {
            double sub = pedidoProf.calcularTotalPedido();
            double iva = sub * PORCENTAJE_IVA;
            lblSubP.setText(String.format("Subtotal: $%.2f", sub));
            lblIvaP.setText(String.format("IVA (15%%): $%.2f", iva));
            lblTotP.setText(String.format("Total Estimado: $%.2f", sub + iva));
        };

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(10, 15, 10, 15));
        panelCentro.add(scrollProf,       BorderLayout.CENTER);
        panelCentro.add(panelTotalesProf, BorderLayout.SOUTH);

        // ── Botones: Descartar y Proceder a Venta ────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT,
                UIThemeManager.GAP_COMPONENTES, 12));
        panelBotones.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelBotones.setBorder(new MatteBorder(1, 0, 0, 0, UIThemeManager.COLOR_BORDE_PANEL));

        JButton btnEliminar = new JButton("Descartar Proforma");
        btnEliminar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnEliminar.setBackground(UIThemeManager.TEXTO_MODULO_ROSE);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(200, UIThemeManager.ALTO_BOTON_PRINCIPAL));

        JButton btnProceder = new JButton("Proceder a la Venta");
        btnProceder.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnProceder.setBackground(UIThemeManager.TEXTO_MODULO_MINT);
        btnProceder.setForeground(Color.WHITE);
        btnProceder.setFocusPainted(false);
        btnProceder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProceder.setPreferredSize(new Dimension(200, UIThemeManager.ALTO_BOTON_PRINCIPAL));

        panelBotones.add(btnEliminar);
        panelBotones.add(btnProceder);

        // Producto seleccionado por la búsqueda
        final Producto[] prodSel = {null};

        btnBuscar.addActionListener(ev -> {
            Producto p = catalogo.buscarProducto(txtBuscar.getText().trim());
            if (p != null) {
                prodSel[0] = p;
                lblResultado.setText(String.format("Encontrado: %s — $%.2f | Stock: %d uds.",
                        p.getNombre(), p.getPrecio(), p.getStock()));
                lblResultado.setForeground(UIThemeManager.TEXTO_MODULO_MINT);
            } else {
                prodSel[0] = null;
                lblResultado.setText("Producto no encontrado en el catalogo.");
                lblResultado.setForeground(UIThemeManager.TEXTO_MODULO_PEACH);
            }
        });

        // Agregar ítem a la proforma SIN descontar stock (es cotización referencial)
        btnAgregar.addActionListener(ev -> {
            if (prodSel[0] == null) {
                JOptionPane.showMessageDialog(dlg,
                        "Busque y seleccione un producto valido antes de agregar.",
                        "Sin Seleccion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int cant = (int) spinCant.getValue();

            // Solo avisar si no hay stock suficiente; la proforma no bloquea por esto
            if (!prodSel[0].verificarDisponibilidad(cant)) {
                int resp = JOptionPane.showConfirmDialog(dlg,
                    "La cantidad (" + cant + ") supera el stock actual (" + prodSel[0].getStock() + ").\n"
                    + "Puede incluirla en la cotizacion de todas formas. Desea continuar?",
                    "Stock Insuficiente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (resp != JOptionPane.YES_OPTION) return;
            }

            // Crear detalle manualmente para NO modificar el inventario
            DetalleProducto detalle = new DetalleProducto(prodSel[0], cant);
            pedidoProf.getListaDetalles().add(detalle);

            modeloProf.addRow(new Object[]{
                prodSel[0].getIdProducto(),
                prodSel[0].getNombre(),
                cant,
                String.format("$%.2f", prodSel[0].getPrecio()),
                String.format("$%.2f", detalle.getSubtotal())
            });

            actualizarTotales.run();
            lblResultado.setText("Articulo agregado a la cotizacion: " + prodSel[0].getNombre());
            lblResultado.setForeground(UIThemeManager.TEXTO_MODULO_BLUE);
        });

        // Descartar la proforma con confirmación
        btnEliminar.addActionListener(ev -> {
            if (pedidoProf.getListaDetalles().isEmpty()) {
                dlg.dispose();
                vista.actualizarStatus("Cotizacion descartada.");
                return;
            }
            int conf = JOptionPane.showConfirmDialog(dlg,
                    "Confirma que desea eliminar esta cotizacion?\nTodos los items se descartaran.",
                    "Descartar Cotizacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf == JOptionPane.YES_OPTION) {
                dlg.dispose();
                vista.actualizarStatus("Cotizacion descartada.");
            }
        });

        // Convertir la proforma en venta real
        btnProceder.addActionListener(ev -> {
            if (pedidoProf.getListaDetalles().isEmpty()) {
                JOptionPane.showMessageDialog(dlg,
                        "La cotizacion no tiene articulos. Agregue productos primero.",
                        "Cotizacion Vacia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar que haya stock real para todos los ítems
            StringBuilder sinStock = new StringBuilder();
            for (DetalleProducto dp : pedidoProf.getListaDetalles()) {
                if (!dp.getProducto().verificarDisponibilidad(dp.getCantidad())) {
                    sinStock.append("  - ").append(dp.getProducto().getNombre())
                            .append(" (disponible: ").append(dp.getProducto().getStock())
                            .append(", requerido: ").append(dp.getCantidad()).append(")\n");
                }
            }
            if (sinStock.length() > 0) {
                JOptionPane.showMessageDialog(dlg,
                        "No se puede proceder. Stock insuficiente para:\n\n" + sinStock,
                        "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int conf = JOptionPane.showConfirmDialog(dlg,
                    "Desea convertir esta cotizacion en una venta real?\n"
                    + "Se descontara el stock y se procedera a la facturacion.",
                    "Confirmar Venta desde Proforma", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (conf == JOptionPane.YES_OPTION) {
                // Registrar proforma en el historial del GestorVenta
                gVenta.generarProforma(pedidoProf);

                // Crear pedido real descontando stock
                Pedido pedidoReal = new Pedido("PED-" + System.currentTimeMillis());
                for (DetalleProducto dp : pedidoProf.getListaDetalles()) {
                    pedidoReal.agregarProducto(dp.getProducto(), dp.getCantidad());
                }
                pedidoActual = pedidoReal;
                dlg.dispose();
                abrirFormularioCliente();
                vista.actualizarStatus("Proforma convertida a venta. Proceda a la facturacion.");
            }
        });

        dlg.add(panelNorte,   BorderLayout.NORTH);
        dlg.add(panelCentro,  BorderLayout.CENTER);
        dlg.add(panelBotones, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // =========================================================================
    // FLUJO: CAMBIOS Y DEVOLUCIONES POR ARTÍCULO
    // El operador ingresa el ID de la factura. Se muestra la lista de productos
    // vendidos. Por cada ítem puede elegir: Cambio (si hay stock) o Reembolso.
    // =========================================================================
    private void ejecutarFlujoCambioDevolucion() {
        vista.actualizarStatus("Modulo de soporte post-venta activo...");

        String idFactura = JOptionPane.showInputDialog(vista,
                "Ingrese el codigo de la factura (ej. FAC-123456789):",
                "Soporte Post-Venta - Cambios y Devoluciones", JOptionPane.QUESTION_MESSAGE);
        if (idFactura == null || idFactura.trim().isEmpty()) return;

        Factura factura = gCompra.buscarFactura(idFactura.trim());

        if (factura == null) {
            JOptionPane.showMessageDialog(vista,
                    "No se encontro ninguna factura con el codigo: " + idFactura.trim(),
                    "Factura No Encontrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (factura.getEstadoPago() == EstadoPago.ANULADO) {
            JOptionPane.showMessageDialog(vista,
                    "Esta factura ya fue anulada y procesada previamente.",
                    "Factura Anulada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (factura.getEstadoPago() == EstadoPago.PENDIENTE) {
            JOptionPane.showMessageDialog(vista,
                    "No se puede gestionar devoluciones en facturas pendientes de pago.",
                    "Factura Pendiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        abrirDialogGestionProductos(factura);
    }

    /**
     * Diálogo que muestra todos los productos de la factura.
     * Por fila seleccionada el operador puede elegir Cambio o Reembolso parcial.
     */
    private void abrirDialogGestionProductos(Factura factura) {
        JDialog dlgGestion = new JDialog(vista, "Gestion de Devolucion - " + factura.getIdFactura(), true);
        dlgGestion.setSize(820, 560);
        dlgGestion.setLocationRelativeTo(vista);
        dlgGestion.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        dlgGestion.setLayout(new BorderLayout(10, 10));

        // ── Encabezado con datos de la factura ──────────────────────────────
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelHeader.setBorder(new MatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));

        JPanel panelInfoFact = new JPanel(new GridLayout(2, 3, 15, 4));
        panelInfoFact.setOpaque(false);
        panelInfoFact.setBorder(new EmptyBorder(12, UIThemeManager.MARGEN_PANEL, 12, UIThemeManager.MARGEN_PANEL));
        panelInfoFact.add(crearEtiquetaInfo("N Factura:", factura.getIdFactura()));
        panelInfoFact.add(crearEtiquetaInfo("Cliente:", factura.getCliente().getNombre()));
        panelInfoFact.add(crearEtiquetaInfo("CI/RUC:", factura.getCliente().getCedula()));
        panelInfoFact.add(crearEtiquetaInfo("Total Facturado:", String.format("$%.2f", factura.getTotal())));
        panelInfoFact.add(crearEtiquetaInfo("Estado:", factura.getEstadoPago().toString()));
        panelInfoFact.add(crearEtiquetaInfo("IVA Incluido:", String.format("$%.2f", factura.getValorIVA())));

        JLabel lblInstruccion = new JLabel(
            "  Seleccione un producto y elija la accion: Cambio (requiere stock) o Reembolso en efectivo.");
        lblInstruccion.setFont(UIThemeManager.FUENTE_CAPTION);
        lblInstruccion.setForeground(UIThemeManager.TEXTO_MODULO_BLUE);
        lblInstruccion.setBorder(new EmptyBorder(0, UIThemeManager.MARGEN_PANEL, 8, 0));

        panelHeader.add(panelInfoFact, BorderLayout.CENTER);
        panelHeader.add(lblInstruccion, BorderLayout.SOUTH);

        // ── Tabla de productos de la factura ────────────────────────────────
        String[] cols = {"#", "Codigo", "Producto", "Cant.", "P. Unit.", "Subtotal", "Stock Actual"};
        DefaultTableModel modeloDev = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        ArrayList<DetalleProducto> detalles = factura.getVenta().getPedido().getListaDetalles();
        for (int i = 0; i < detalles.size(); i++) {
            DetalleProducto dp = detalles.get(i);
            modeloDev.addRow(new Object[]{
                i + 1,
                dp.getProducto().getIdProducto(),
                dp.getProducto().getNombre(),
                dp.getCantidad(),
                String.format("$%.2f", dp.getPrecioUnitario()),
                String.format("$%.2f", dp.getSubtotal()),
                dp.getProducto().getStock() + " uds."
            });
        }

        JTable tablaDev = new JTable(modeloDev);
        tablaDev.setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        tablaDev.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        tablaDev.setFont(UIThemeManager.FUENTE_ETIQUETA);
        tablaDev.setRowHeight(26);
        tablaDev.setGridColor(UIThemeManager.COLOR_BORDE_PANEL);
        tablaDev.setSelectionBackground(UIThemeManager.PASTEL_BLUE);
        tablaDev.setSelectionForeground(UIThemeManager.TEXTO_MODULO_BLUE);
        tablaDev.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDev.getTableHeader().setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        tablaDev.getTableHeader().setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        tablaDev.getTableHeader().setFont(UIThemeManager.FUENTE_SUBTITULO);

        JScrollPane scrollDev = new JScrollPane(tablaDev);
        scrollDev.setBorder(BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL));
        scrollDev.getViewport().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(0, UIThemeManager.MARGEN_PANEL, 0, UIThemeManager.MARGEN_PANEL));
        panelTabla.add(scrollDev, BorderLayout.CENTER);

        // ── Panel de acciones sobre el ítem seleccionado ─────────────────────
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER,
                UIThemeManager.GAP_COMPONENTES * 2, 14));
        panelAcciones.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        panelAcciones.setBorder(new MatteBorder(1, 0, 0, 0, UIThemeManager.COLOR_BORDE_PANEL));

        JLabel lblSeleccion = new JLabel("Seleccione una fila para habilitar las acciones.");
        lblSeleccion.setFont(UIThemeManager.FUENTE_CAPTION);
        lblSeleccion.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);

        JButton btnCambio = new JButton("Cambiar Producto");
        btnCambio.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnCambio.setBackground(UIThemeManager.TEXTO_MODULO_BLUE);
        btnCambio.setForeground(Color.WHITE);
        btnCambio.setFocusPainted(false);
        btnCambio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCambio.setPreferredSize(new Dimension(190, UIThemeManager.ALTO_BOTON_PRINCIPAL));
        btnCambio.setEnabled(false);

        JButton btnReembolso = new JButton("Reembolsar Articulo");
        btnReembolso.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnReembolso.setBackground(UIThemeManager.TEXTO_MODULO_PEACH);
        btnReembolso.setForeground(Color.WHITE);
        btnReembolso.setFocusPainted(false);
        btnReembolso.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReembolso.setPreferredSize(new Dimension(190, UIThemeManager.ALTO_BOTON_PRINCIPAL));
        btnReembolso.setEnabled(false);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnCerrar.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        btnCerrar.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(110, UIThemeManager.ALTO_BOTON_PRINCIPAL));
        btnCerrar.addActionListener(e -> dlgGestion.dispose());

        panelAcciones.add(lblSeleccion);
        panelAcciones.add(btnCambio);
        panelAcciones.add(btnReembolso);
        panelAcciones.add(btnCerrar);

        // Activar/desactivar botones según la fila seleccionada
        tablaDev.getSelectionModel().addListSelectionListener(ev -> {
            int fila = tablaDev.getSelectedRow();
            if (fila >= 0 && fila < detalles.size()) {
                DetalleProducto dp = detalles.get(fila);
                boolean hayStock = dp.getProducto().getStock() >= dp.getCantidad();
                btnCambio.setEnabled(hayStock);
                btnReembolso.setEnabled(true);
                String info = hayStock
                    ? "Stock suficiente para cambio (" + dp.getProducto().getStock() + " uds.)"
                    : "Sin stock para cambio (" + dp.getProducto().getStock() + " uds. disponibles)";
                lblSeleccion.setText("Seleccionado: " + dp.getProducto().getNombre() + " - " + info);
                lblSeleccion.setForeground(hayStock
                        ? UIThemeManager.TEXTO_MODULO_MINT : UIThemeManager.TEXTO_MODULO_PEACH);
            } else {
                btnCambio.setEnabled(false);
                btnReembolso.setEnabled(false);
                lblSeleccion.setText("Seleccione una fila para habilitar las acciones.");
                lblSeleccion.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
            }
        });

        // ── ACCIÓN: CAMBIO DEL PRODUCTO ─────────────────────────────────────
        // Repone el stock del artículo devuelto y lo descuenta nuevamente
        // (cambio por el mismo producto en buen estado).
        btnCambio.addActionListener(ev -> {
            int fila = tablaDev.getSelectedRow();
            if (fila < 0) return;
            DetalleProducto dpOrig = detalles.get(fila);

            int conf = JOptionPane.showConfirmDialog(dlgGestion,
                "<html><body style='width:300px;'>"
                + "<b>Confirmar Cambio de Producto</b><br><br>"
                + "Producto: <b>" + dpOrig.getProducto().getNombre() + "</b><br>"
                + "Cantidad: <b>" + dpOrig.getCantidad() + " unidades</b><br><br>"
                + "El producto sera devuelto al inventario y retirado nuevamente."
                + "</body></html>",
                "Confirmar Cambio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (conf == JOptionPane.YES_OPTION) {
                // Reponer stock + volver a descontar (el cliente recibe un artículo igual)
                dpOrig.getProducto().aumentarStock(dpOrig.getCantidad());
                dpOrig.getProducto().descontarStock(dpOrig.getCantidad());

                Devolucion dev = new Devolucion("DEV-" + System.currentTimeMillis(),
                        "Cambio de producto: " + dpOrig.getProducto().getNombre());
                factura.setDevolucion(dev);

                // Actualizar columna Stock en la tabla visual
                modeloDev.setValueAt(dpOrig.getProducto().getStock() + " uds.", fila, 6);

                JOptionPane.showMessageDialog(dlgGestion,
                    "<html><body style='width:280px;'>"
                    + "<b>Cambio registrado correctamente.</b><br><br>"
                    + "Producto: " + dpOrig.getProducto().getNombre() + "<br>"
                    + "Stock actualizado: " + dpOrig.getProducto().getStock() + " uds.<br>"
                    + "El cliente recibe el mismo articulo en buen estado."
                    + "</body></html>",
                    "Cambio Exitoso", JOptionPane.INFORMATION_MESSAGE);

                vista.actualizarStatus("Cambio aplicado: " + dpOrig.getProducto().getNombre());
                btnCambio.setEnabled(false);
                btnReembolso.setEnabled(false);
                tablaDev.clearSelection();
                lblSeleccion.setText("Cambio aplicado. Seleccione otra fila o cierre.");
                lblSeleccion.setForeground(UIThemeManager.TEXTO_MODULO_MINT);
            }
        });

        // ── ACCIÓN: REEMBOLSO PARCIAL DEL ARTÍCULO ─────────────────────────
        // Repone el stock del ítem, calcula el valor con IVA y emite el comprobante.
        btnReembolso.addActionListener(ev -> {
            int fila = tablaDev.getSelectedRow();
            if (fila < 0) return;
            DetalleProducto dpOrig = detalles.get(fila);

            double montoConIva = dpOrig.getSubtotal() * (1 + PORCENTAJE_IVA);

            int conf = JOptionPane.showConfirmDialog(dlgGestion,
                "<html><body style='width:300px;'>"
                + "<b>Confirmar Reembolso en Efectivo</b><br><br>"
                + "Producto: <b>" + dpOrig.getProducto().getNombre() + "</b><br>"
                + "Cantidad: <b>" + dpOrig.getCantidad() + " unidades</b><br>"
                + "Valor a devolver (con IVA): <b>$" + String.format("%.2f", montoConIva) + "</b><br><br>"
                + "El producto regresara al inventario."
                + "</body></html>",
                "Confirmar Reembolso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (conf == JOptionPane.YES_OPTION) {
                // Reponer stock del artículo reembolsado
                dpOrig.getProducto().aumentarStock(dpOrig.getCantidad());

                Devolucion dev = new Devolucion("DEV-" + System.currentTimeMillis(),
                        "Reembolso parcial: " + dpOrig.getProducto().getNombre());
                factura.setDevolucion(dev);

                modeloDev.setValueAt(dpOrig.getProducto().getStock() + " uds.", fila, 6);

                // Comprobante de reembolso
                StringBuilder sb = new StringBuilder();
                sb.append("==============================================\n");
                sb.append("        COMPROBANTE DE REEMBOLSO PARCIAL      \n");
                sb.append("                TINTA & TRAZO                 \n");
                sb.append("==============================================\n");
                sb.append("Factura Origen : ").append(factura.getIdFactura()).append("\n");
                sb.append("Cliente        : ").append(factura.getCliente().getNombre()).append("\n");
                sb.append("----------------------------------------------\n");
                sb.append("Articulo       : ").append(dpOrig.getProducto().getNombre()).append("\n");
                sb.append("Cantidad       : ").append(dpOrig.getCantidad()).append(" uds.\n");
                sb.append("Precio Unit.   : $").append(String.format("%.2f", dpOrig.getPrecioUnitario())).append("\n");
                sb.append("IVA (15%)      : $").append(String.format("%.2f", dpOrig.getSubtotal() * PORCENTAJE_IVA)).append("\n");
                sb.append("----------------------------------------------\n");
                sb.append("EFECTIVO A DEVOLVER: $").append(String.format("%.2f", montoConIva)).append("\n");
                sb.append("==============================================\n");
                sb.append("Stock repuesto : ").append(dpOrig.getProducto().getNombre())
                  .append(" - ").append(dpOrig.getProducto().getStock()).append(" uds.\n");

                JTextArea areaComp = new JTextArea(sb.toString());
                areaComp.setEditable(false);
                areaComp.setFont(new Font("Monospaced", Font.PLAIN, 12));
                areaComp.setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
                areaComp.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
                JScrollPane scrollComp = new JScrollPane(areaComp);
                scrollComp.setPreferredSize(new Dimension(420, 270));

                JOptionPane.showMessageDialog(dlgGestion, scrollComp,
                        "Reembolso Parcial Autorizado", JOptionPane.INFORMATION_MESSAGE);

                vista.actualizarStatus(String.format("Reembolso de $%.2f aplicado - %s",
                        montoConIva, dpOrig.getProducto().getNombre()));
                btnCambio.setEnabled(false);
                btnReembolso.setEnabled(false);
                tablaDev.clearSelection();
                lblSeleccion.setText("Reembolso aplicado. Seleccione otra fila o cierre.");
                lblSeleccion.setForeground(UIThemeManager.TEXTO_MODULO_MINT);
            }
        });

        dlgGestion.add(panelHeader,   BorderLayout.NORTH);
        dlgGestion.add(panelTabla,    BorderLayout.CENTER);
        dlgGestion.add(panelAcciones, BorderLayout.SOUTH);
        dlgGestion.setVisible(true);
    }

    // =========================================================================
    // FLUJO: FACTURACIÓN Y COBRO (idéntico al original)
    // =========================================================================
    private void abrirFormularioCliente() {
        JDialog dialogCliente = new JDialog(vista, "Datos Obligatorios del Comprobante", true);
        dialogCliente.setSize(450, 400);
        dialogCliente.setLocationRelativeTo(vista);
        dialogCliente.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        dialogCliente.setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 20));
        panelCampos.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JTextField txtCedula    = crearCampoFormulario(panelCampos, "Cedula / RUC *:");
        JTextField txtNombre    = crearCampoFormulario(panelCampos, "Nombre Completo *:");
        JTextField txtDireccion = crearCampoFormulario(panelCampos, "Direccion *:");
        JTextField txtTelefono  = crearCampoFormulario(panelCampos, "Telefono:");
        JTextField txtEmail     = crearCampoFormulario(panelCampos, "Correo electronico:");

        JButton btnEmitir = new JButton("Sellar Transaccion e Imprimir");
        btnEmitir.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnEmitir.setBackground(UIThemeManager.PASTEL_VIVIDO_AMBAR);
        btnEmitir.setForeground(Color.WHITE);
        btnEmitir.setPreferredSize(new Dimension(0, 50));

        btnEmitir.addActionListener(e -> {
            if (txtCedula.getText().trim().isEmpty()
                    || txtNombre.getText().trim().isEmpty()
                    || txtDireccion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialogCliente,
                        "No se permite finalizar la transaccion si los datos obligatorios estan incompletos.",
                        "Error de Validacion", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente c = new Cliente(txtCedula.getText(), txtNombre.getText(),
                    txtDireccion.getText(), txtTelefono.getText(), txtEmail.getText());
            gVenta.registrarNuevoCliente(c);

            Venta venta = gVenta.iniciarProcesoVenta(pedidoActual);
            venta.confirmarVenta(pedidoActual, vendedor);
            Factura factura = gCompra.emitirFactura(venta, c);

            String inputPago = JOptionPane.showInputDialog(dialogCliente,
                    "Factura Emitida en Estado PENDIENTE.\nTotal con IVA: $"
                    + String.format("%.2f", factura.getTotal())
                    + "\nIngrese el monto recibido de pago:");
            try {
                if (inputPago != null) {
                    double pago = Double.parseDouble(inputPago);
                    if (gCompra.procesarPago(factura, pago)) {
                        venta.setMontoPagado(pago);
                        venta.setMetodoPago("Efectivo");
                        double cambio = pago - factura.getTotal();
                        JOptionPane.showMessageDialog(dialogCliente,
                                "Cobro exitoso. Cambio en efectivo: $" + String.format("%.2f", cambio),
                                "Transaccion Finalizada", JOptionPane.INFORMATION_MESSAGE);
                        dialogCliente.dispose();
                        mostrarFacturaFinal(factura);
                    } else {
                        JOptionPane.showMessageDialog(dialogCliente,
                                "El monto ingresado es insuficiente para liquidar la factura.",
                                "Pago Denegado", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogCliente,
                        "Monto invalido ingresado.", "Error Financiero", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialogCliente.add(panelCampos, BorderLayout.CENTER);
        dialogCliente.add(btnEmitir,   BorderLayout.SOUTH);
        dialogCliente.setVisible(true);
    }

    private void mostrarFacturaFinal(Factura f) {
        JDialog dlgFact = new JDialog(vista, "Cierre de Operacion - Comprobante Autorizado", true);
        dlgFact.setSize(500, 550);
        dlgFact.setLocationRelativeTo(vista);

        JTextArea area = new JTextArea(f.imprimirFactura());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnCerrar = new JButton("Volver al Panel Principal");
        btnCerrar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnCerrar.setBackground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        btnCerrar.setForeground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        btnCerrar.setPreferredSize(new Dimension(0, 45));
        btnCerrar.addActionListener(e -> dlgFact.dispose());

        dlgFact.add(new JScrollPane(area), BorderLayout.CENTER);
        dlgFact.add(btnCerrar,             BorderLayout.SOUTH);
        dlgFact.setVisible(true);
    }

    // =========================================================================
    // UTILIDADES COMPARTIDAS
    // =========================================================================

    private JTextField crearCampoFormulario(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        label.setFont(UIThemeManager.FUENTE_ETIQUETA);
        JTextField campo = new JTextField();
        campo.setFont(UIThemeManager.FUENTE_INPUT);
        campo.setBackground(Color.WHITE);
        campo.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        campo.setCaretColor(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        panel.add(label);
        panel.add(campo);
        return campo;
    }

    /** Mini-panel de dos líneas: etiqueta gris arriba, valor en negrita abajo. */
    private JPanel crearEtiquetaInfo(String etiqueta, String valor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(UIThemeManager.FUENTE_CAPTION);
        lbl.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
        JLabel val = new JLabel(valor);
        val.setFont(UIThemeManager.FUENTE_SUBTITULO);
        val.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        p.add(lbl);
        p.add(val);
        return p;
    }
}