import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PantallaPrincipalController
 * * Actualizado para consumir la nueva paleta pastel de Tinta & Trazo.
 */
public class PantallaPrincipalController {

    private final PantallaPrincipalView vista;
    
    private DefaultTableModel modeloTablaPedido;
    private double subtotalAcumulado = 0.0;
    private final double PORCENTAJE_IVA = 0.15; 

    public PantallaPrincipalController(PantallaPrincipalView vista) {
        this.vista = vista;
        registrarListenersMenu();
    }

    private void registrarListenersMenu() {
        vista.getBtnIniciarPedido().addActionListener(new ListenerAbrirFlujoPedido());
        
        vista.getBtnConsultarProducto().addActionListener(e -> mostrarDialogoRapido("Consultar Producto", "Módulo de consulta de inventario abierto. Catálogo sincronizado.", UIThemeManager.TEXTO_MÓDULO_MINT));
        vista.getBtnGenerarProforma().addActionListener(e -> mostrarDialogoRapido("Generar Proforma", "Generador de Proformas listo. Inicie un pedido para formalizar la cotización.", UIThemeManager.TEXTO_MÓDULO_PEACH));
        vista.getBtnCambiosDevolucion().addActionListener(e -> mostrarDialogoRapido("Cambios y Devoluciones", "Historial de transacciones cargado. Listo para procesar devoluciones.", UIThemeManager.TEXTO_MÓDULO_LAVENDER));
        vista.getBtnSalir().addActionListener(e -> System.exit(0));
    }

    // =========================================================================
    // FLUJO: INICIAR PEDIDO (DIÁLOGO INTERACTIVO)
    // =========================================================================
    private class ListenerAbrirFlujoPedido implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vista.actualizarStatus("Módulo de ventas activo...");
            subtotalAcumulado = 0.0;

            JDialog dialogPedido = new JDialog(vista, "Tinta & Trazo — Captura de Pedido", true);
            dialogPedido.setSize(750, 550);
            dialogPedido.setLocationRelativeTo(vista);
            dialogPedido.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
            dialogPedido.setLayout(new BorderLayout(UIThemeManager.GAP_COMPONENTES, UIThemeManager.GAP_COMPONENTES));

            // ---- Panel Superior ----
            JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, UIThemeManager.GAP_COMPONENTES, 15));
            panelSelector.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
            panelSelector.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));

            JLabel lblCombo = new JLabel("Producto:");
            lblCombo.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            lblCombo.setFont(UIThemeManager.FUENTE_ETIQUETA);

            String[] productosMock = {
                "Cuaderno Universitario ($2.50)", 
                "Paquete de Útiles Escolares ($25.00)", 
                "Caja de Marcadores ($5.00)"
            };
            JComboBox<String> comboProductos = new JComboBox<>(productosMock);
            comboProductos.setFont(UIThemeManager.FUENTE_INPUT);

            JLabel lblCant = new JLabel("Cantidad:");
            lblCant.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            lblCant.setFont(UIThemeManager.FUENTE_ETIQUETA);

            JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            spinnerCantidad.setFont(UIThemeManager.FUENTE_INPUT);

            JButton btnAgregarItem = new JButton("Aumentar Producto");
            btnAgregarItem.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
            btnAgregarItem.setBackground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
            btnAgregarItem.setForeground(UIThemeManager.COLOR_SUPERFICIE_PANEL);

            panelSelector.add(lblCombo);
            panelSelector.add(comboProductos);
            panelSelector.add(lblCant);
            panelSelector.add(spinnerCantidad);
            panelSelector.add(btnAgregarItem);

            // ---- Panel Central ----
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

            // ---- Panel Totales ----
            JPanel panelTotales = new JPanel();
            panelTotales.setLayout(new BoxLayout(panelTotales, BoxLayout.Y_AXIS));
            panelTotales.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
            panelTotales.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));

            JLabel lblSub = new JLabel("Subtotal: $0.00");
            JLabel lblIva = new JLabel("IVA (15%): $0.00");
            JLabel lblTot = new JLabel("Total General: $0.00");

            Font fuenteGrande = new Font("SansSerif", Font.BOLD, 14);
            lblSub.setFont(fuenteGrande); lblSub.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
            lblIva.setFont(fuenteGrande); lblIva.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
            lblTot.setFont(new Font("SansSerif", Font.BOLD, 18)); lblTot.setForeground(UIThemeManager.PASTEL_VIVIDO_AMBAR);

            panelTotales.add(lblSub); panelTotales.add(Box.createVerticalStrut(8));
            panelTotales.add(lblIva); panelTotales.add(Box.createVerticalStrut(12));
            panelTotales.add(lblTot);

            btnAgregarItem.addActionListener(ev -> {
                int index = comboProductos.getSelectedIndex();
                int cantidad = (int) spinnerCantidad.getValue();
                String nombreProd = "";
                double precioUnit = 0.0;

                if (index == 0) { nombreProd = "Cuaderno Universitario"; precioUnit = 2.50; }
                else if (index == 1) { nombreProd = "Paquete de Útiles Escolares"; precioUnit = 25.00; }
                else { nombreProd = "Caja de Marcadores"; precioUnit = 5.00; }

                double subtotalItem = precioUnit * cantidad;
                modeloTablaPedido.addRow(new Object[]{nombreProd, cantidad, String.format("$%.2f", precioUnit), String.format("$%.2f", subtotalItem)});

                subtotalAcumulado += subtotalItem;
                double calculoIva = subtotalAcumulado * PORCENTAJE_IVA;
                double calculoTotal = subtotalAcumulado + calculoIva;

                lblSub.setText(String.format("Subtotal: $%.2f", subtotalAcumulado));
                lblIva.setText(String.format("IVA (15%): $%.2f", calculoIva));
                lblTot.setText(String.format("Total General: $%.2f", calculoTotal));
            });

            // ---- Panel Inferior ----
            JPanel panelAccionesPOS = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIThemeManager.GAP_COMPONENTES, 15));
            panelAccionesPOS.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
            panelAccionesPOS.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIThemeManager.COLOR_BORDE_PANEL));

            JButton btnProcesar = new JButton("Procesar Venta");
            btnProcesar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
            btnProcesar.setBackground(UIThemeManager.TEXTO_MÓDULO_MINT);
            btnProcesar.setForeground(Color.WHITE);

            JButton btnCancelar = new JButton("Cancelar Pedido");
            btnCancelar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
            btnCancelar.setBackground(UIThemeManager.TEXTO_MÓDULO_ROSE);
            btnCancelar.setForeground(Color.WHITE);

            panelAccionesPOS.add(btnCancelar);
            panelAccionesPOS.add(btnProcesar);

            btnCancelar.addActionListener(ev -> {
                dialogPedido.dispose();
                vista.actualizarStatus("Pedido cancelado por el operador.");
            });

            btnProcesar.addActionListener(ev -> {
                if (modeloTablaPedido.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(dialogPedido, "El carrito de compras está vacío.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double totalActual = subtotalAcumulado * (1 + PORCENTAJE_IVA);
                int proceed = JOptionPane.showConfirmDialog(dialogPedido, 
                    "--- Resumen de Operación ---\n" +
                    "• Artículos en lote: " + modeloTablaPedido.getRowCount() + "\n" +
                    "• Monto total procesado: " + String.format("$%.2f", totalActual) + "\n\n" +
                    "¿Desea registrar el pago del pedido y proceder a GENERAR FACTURA?", 
                    "Procesar Venta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (proceed == JOptionPane.YES_OPTION) {
                    dialogPedido.dispose();
                    abrirFormularioCliente(totalActual);
                }
            });

            JPanel panelCentroContenedor = new JPanel(new BorderLayout(10, 10));
            panelCentroContenedor.setOpaque(false);
            panelCentroContenedor.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            panelCentroContenedor.add(scrollTabla, BorderLayout.CENTER);
            panelCentroContenedor.add(panelTotales, BorderLayout.SOUTH);

            dialogPedido.add(panelSelector, BorderLayout.NORTH);
            dialogPedido.add(panelCentroContenedor, BorderLayout.CENTER);
            dialogPedido.add(panelAccionesPOS, BorderLayout.SOUTH);

            dialogPedido.setVisible(true);
        }
    }

    // =========================================================================
    // FLUJO: CAPTURA DE DATOS BÁSICOS DEL CLIENTE
    // =========================================================================
    private void abrirFormularioCliente(double totalVenta) {
        JDialog dialogCliente = new JDialog(vista, "Registro Obligatorio de Datos", true);
        dialogCliente.setSize(450, 400);
        dialogCliente.setLocationRelativeTo(vista);
        dialogCliente.getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        dialogCliente.setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 20));
        panelCampos.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JTextField txtCedula = crearCampoFormulario(panelCampos, "Cédula / RUC:");
        JTextField txtNombre = crearCampoFormulario(panelCampos, "Nombre Completo:");
        JTextField txtDireccion = crearCampoFormulario(panelCampos, "Dirección:");
        JTextField txtTelefono = crearCampoFormulario(panelCampos, "Teléfono:");
        JTextField txtEmail = crearCampoFormulario(panelCampos, "Correo electrónico:");

        JButton btnEmitir = new JButton("Confirmar y Emitir Factura");
        btnEmitir.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnEmitir.setBackground(UIThemeManager.PASTEL_VIVIDO_AMBAR);
        btnEmitir.setForeground(Color.WHITE);
        btnEmitir.setPreferredSize(new Dimension(0, 50));

        btnEmitir.addActionListener(e -> {
            if(txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtDireccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialogCliente, "Por favor complete los datos obligatorios del cliente.", "Campos Incompletos", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dialogCliente.dispose(); 
            mostrarFacturaFinal(txtCedula.getText(), txtNombre.getText(), txtDireccion.getText(), txtTelefono.getText(), txtEmail.getText(), totalVenta);
        });

        dialogCliente.add(panelCampos, BorderLayout.CENTER);
        dialogCliente.add(btnEmitir, BorderLayout.SOUTH);
        dialogCliente.setVisible(true);
    }

    // =========================================================================
    // FLUJO: RENDERIZADO GRÁFICO DE LA FACTURA FINAL
    // =========================================================================
    private void mostrarFacturaFinal(String cedula, String nombre, String dir, String telf, String mail, double total) {
        JDialog dialogFactura = new JDialog(vista, "Comprobante Electrónico Autorizado", true);
        dialogFactura.setSize(500, 600);
        dialogFactura.setLocationRelativeTo(vista);
        dialogFactura.getContentPane().setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        dialogFactura.setLayout(new BorderLayout(15, 15));

        JTextArea areaFactura = new JTextArea();
        areaFactura.setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        areaFactura.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        areaFactura.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaFactura.setEditable(false);
        areaFactura.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("                  TINTA & TRAZO                   \n");
        sb.append("              PAPELERÍA & CREATIVIDAD             \n");
        sb.append("==================================================\n\n");
        sb.append("CLIENTE:   ").append(nombre.toUpperCase()).append("\n");
        sb.append("CÉDULA/RUC:").append(cedula).append("\n");
        sb.append("DIRECCIÓN: ").append(dir.toUpperCase()).append("\n");
        sb.append("TELÉFONO:  ").append(telf).append("\n");
        sb.append("EMAIL:     ").append(mail).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-25s %-5s %-8s\n", "PRODUCTO", "CANT", "SUBTOTAL"));
        sb.append("--------------------------------------------------\n");

        for (int i = 0; i < modeloTablaPedido.getRowCount(); i++) {
            String p = (String) modeloTablaPedido.getValueAt(i, 0);
            int c = (int) modeloTablaPedido.getValueAt(i, 1);
            String sub = (String) modeloTablaPedido.getValueAt(i, 3);
            if(p.length() > 22) p = p.substring(0, 21) + ".";
            sb.append(String.format("%-25s %-5d %-8s\n", p, c, sub));
        }

        sb.append("--------------------------------------------------\n");
        double subtotalNeto = total / (1 + PORCENTAJE_IVA);
        sb.append(String.format("SUBTOTAL:                          $%.2f\n", subtotalNeto));
        sb.append(String.format("IVA 15%%:                           $%.2f\n", total - subtotalNeto));
        sb.append(String.format("TOTAL DEFINITIVO CIV:              $%.2f\n", total));
        sb.append("==================================================\n");
        sb.append("       ¡Gracias por su compra en Tinta & Trazo!   \n");

        areaFactura.setText(sb.toString());

        JButton btnFinalizar = new JButton("Finalizar y Volver al Menú Principal");
        btnFinalizar.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        btnFinalizar.setBackground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        btnFinalizar.setForeground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        btnFinalizar.setPreferredSize(new Dimension(0, 50));
        btnFinalizar.addActionListener(e -> dialogFactura.dispose());

        dialogFactura.add(new JScrollPane(areaFactura), BorderLayout.CENTER);
        dialogFactura.add(btnFinalizar, BorderLayout.SOUTH);
        
        vista.actualizarStatus("Factura emitida. Ciclo de transacción cerrado con éxito.");
        dialogFactura.setVisible(true);
    }

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

    private void mostrarDialogoRapido(String titulo, String msg, Color acento) {
        JPanel panelContenido = new JPanel(new BorderLayout(0, 10));
        panelContenido.setBackground(UIThemeManager.COLOR_SUPERFICIE_PANEL);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblHeader = new JLabel("  Módulo: " + titulo);
        lblHeader.setForeground(acento);
        lblHeader.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, acento));

        JLabel lblMsg = new JLabel(msg);
        lblMsg.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);

        panelContenido.add(lblHeader, BorderLayout.NORTH);
        panelContenido.add(lblMsg, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(vista, panelContenido, "Información", JOptionPane.PLAIN_MESSAGE);
    }
}