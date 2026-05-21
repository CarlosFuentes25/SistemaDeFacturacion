package operaciones;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;

/**
 * PantallaPrincipalView
 * * Rediseño completo a Tonos Pastel.
 * * Logotipo centralizado de Tinta & Trazo en sustitución del texto tradicional.
 * * Distribución en rejilla expandida para eliminar espacios muertos.
 */
public class PantallaPrincipalView extends JFrame {

    JButton btnIniciarPedido;
    JButton btnConsultarProducto;
    JButton btnGenerarProforma;
    JButton btnCambiosDevolucion;
    JButton btnSalir;
    JLabel  lblStatus;

    public PantallaPrincipalView() {
        configurarVentana();
        inicializarComponentes();
        construirLayout();
    }

    private void configurarVentana() {
        setTitle("Tinta & Trazo — Panel de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(UIThemeManager.ANCHO_VENTANA_MINIMO, UIThemeManager.ALTO_VENTANA_MINIMO));
        setSize(1050, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    private void inicializarComponentes() {
        btnIniciarPedido       = crearBotonModulo("INICIAR PEDIDO");
        btnConsultarProducto   = crearBotonModulo("CONSULTAR PRODUCTO");
        btnGenerarProforma     = crearBotonModulo("GENERAR PROFORMA");
        btnCambiosDevolucion   = crearBotonModulo("CAMBIOS/DEVOLUCION");
        btnSalir               = crearBotonModulo("SALIR");

        // Adaptar el estilo del botón Salir para denotar el cierre
        btnSalir.setBackground(new Color(0xE2E8F0));
        btnSalir.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);

        lblStatus = new JLabel("  Garantizando precisión en cada trazo. Sincronizado.");
        lblStatus.setFont(UIThemeManager.FUENTE_CAPTION);
        lblStatus.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
    }

    private void construirLayout() {
        setLayout(new BorderLayout());
        add(crearPanelLogoCentralizado(), BorderLayout.NORTH);
        add(crearPanelBloquesOperativos(), BorderLayout.CENTER);
        add(crearPanelStatus(),            BorderLayout.SOUTH);
    }

    /**
     * Reemplaza el encabezado antiguo. Centra la imagen de la marca o un 
     * renderizado tipográfico elegante si la imagen no se encuentra en la ruta.
     */
    private JPanel crearPanelLogoCentralizado() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UIThemeManager.COLOR_HEADER_FONDO);
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, UIThemeManager.COLOR_BORDE_PANEL));
        headerPanel.setPreferredSize(new Dimension(0, 130));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Panel personalizado para renderizar la imagen o el texto con CALIDAD MÁXIMA
        JPanel panelAltaResolucion = new JPanel() {
            private Image imagenLogo;

            {
                // Intentar cargar la imagen al inicializar el panel
                java.io.File archivoImagen = new java.io.File("src/resources/tinta_trazo_logo.png");
                if (archivoImagen.exists()) {
                    imagenLogo = new ImageIcon(archivoImagen.getAbsolutePath()).getImage();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // 1. ACTIVAR EL MOTOR GRÁFICO DE ALTA CALIDAD (La magia contra la pixelación)
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                if (imagenLogo != null) {
                    // 2. Calcular dimensiones manteniendo la relación de aspecto perfecta
                    int imgWidth = imagenLogo.getWidth(null);
                    int imgHeight = imagenLogo.getHeight(null);
                    int targetHeight = 90; // El alto máximo que queremos en el banner
                    int targetWidth = (int) ((double) imgWidth / imgHeight * targetHeight);

                    // 3. Centrar la imagen matemáticamente en el panel
                    int x = (getWidth() - targetWidth) / 2;
                    int y = (getHeight() - targetHeight) / 2;

                    // 4. Dibujar la imagen
                    g2d.drawImage(imagenLogo, x, y, targetWidth, targetHeight, null);
                } else {
                    // Fallback Tipográfico (Si no hay imagen, dibuja el texto súper nítido)
                    Font fuentePrin = new Font("Serif", Font.BOLD, 32); 
                    Font fuenteSub = UIThemeManager.FUENTE_CAPTION;

                    FontMetrics fmPrin = g2d.getFontMetrics(fuentePrin);
                    FontMetrics fmSub = g2d.getFontMetrics(fuenteSub);

                    String txtPrin = "TINTA & TRAZO";
                    String txtSub = "PAPELERÍA & CREATIVIDAD";

                    int xPrin = (getWidth() - fmPrin.stringWidth(txtPrin)) / 2;
                    int yPrin = (getHeight() / 2) + 5;
                    g2d.setFont(fuentePrin);
                    g2d.setColor(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
                    g2d.drawString(txtPrin, xPrin, yPrin);

                    int xSub = (getWidth() - fmSub.stringWidth(txtSub)) / 2;
                    int ySub = yPrin + 20;
                    g2d.setFont(fuenteSub);
                    g2d.setColor(new Color(0xF59E0B)); // Ámbar puro
                    g2d.drawString(txtSub, xSub, ySub);
                }
                g2d.dispose();
            }
        };

        panelAltaResolucion.setOpaque(false);
        // Le damos un ancho lo suficientemente grande para que la imagen quepa sin cortarse
        panelAltaResolucion.setPreferredSize(new Dimension(500, 110)); 

        headerPanel.add(panelAltaResolucion, gbc);
        return headerPanel;
    }
    /**
     * Organiza los 5 módulos operativos de manera que llenen la totalidad
     * del espacio vertical y horizontal de la ventana de forma balanceada.
     */
    private JPanel crearPanelBloquesOperativos() {
        JPanel panelRejilla = new JPanel(new GridBagLayout());
        panelRejilla.setOpaque(false);
        panelRejilla.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Cambia los insets y el peso de los botones para que se expandan más
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Expandir ambos ejes
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(12, 12, 12, 12); // Más separación para que se vean grandes
        // Fila 1 - Columna 0: Iniciar Pedido
        gbc.gridx = 0; gbc.gridy = 0;
        panelRejilla.add(crearTarjetaModulo("⚡", "MÓDULO DE VENTAS", 
            "", 
            btnIniciarPedido, UIThemeManager.PASTEL_BLUE, UIThemeManager.TEXTO_MODULO_BLUE), gbc);

        // Fila 1 - Columna 1: Consultar Producto
        gbc.gridx = 1; gbc.gridy = 0;
        panelRejilla.add(crearTarjetaModulo("📦", "LISTADO DE PRODUCTOS", 
            "", 
            btnConsultarProducto, UIThemeManager.PASTEL_MINT, UIThemeManager.TEXTO_MODULO_MINT), gbc);

        // Fila 1 - Columna 2: Generar Proforma
        gbc.gridx = 2; gbc.gridy = 0;
        panelRejilla.add(crearTarjetaModulo("📄", "COTIZACIONES", 
            "", 
            btnGenerarProforma, UIThemeManager.PASTEL_PEACH, UIThemeManager.TEXTO_MODULO_PEACH), gbc);

        // Fila 2 - Columna 0: Cambios y Devoluciones
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2; // Esta tarjeta se expande a lo ancho ocupando dos columnas
        panelRejilla.add(crearTarjetaModulo("🔄", "SOPORTE POST-VENTA", 
            "", 
            btnCambiosDevolucion, UIThemeManager.PASTEL_LAVENDER, UIThemeManager.TEXTO_MODULO_LAVENDER), gbc);

        // Fila 2 - Columna 2: Salir
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 1; // Restablecer el ancho de columna estándar
        panelRejilla.add(crearTarjetaModulo("❌", "SESIÓN", 
            "", 
            btnSalir, UIThemeManager.PASTEL_ROSE, UIThemeManager.TEXTO_MODULO_ROSE), gbc);

        return panelRejilla;
    }

    /**
     * Construye un contenedor estilizado para cada módulo con fondos pastel suaves
     * y alineaciones centradas para corregir el aspecto visual.
     */
    private JPanel crearTarjetaModulo(String iconoUnicode, String area, String descripcion, 
                                      JButton botonAccion, Color fondoPastel, Color colorTextoIcono) {
        JPanel contenedorTarjeta = new JPanel(new BorderLayout(10, 10));
        contenedorTarjeta.setBackground(fondoPastel);
        contenedorTarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIThemeManager.COLOR_BORDE_PANEL, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Panel de información estructurado verticalmente
        JPanel panelInformacion = new JPanel();
        panelInformacion.setLayout(new BoxLayout(panelInformacion, BoxLayout.Y_AXIS));
        panelInformacion.setOpaque(false);

        JLabel lblIcono = new JLabel(iconoUnicode);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 32));
        lblIcono.setForeground(colorTextoIcono);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblArea = new JLabel(area);
        lblArea.setFont(UIThemeManager.FUENTE_SUBTITULO);
        lblArea.setForeground(UIThemeManager.COLOR_TEXTO_PRINCIPAL);
        lblArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblArea.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblDesc = new JLabel("<html><body style='text-align: center;'><p style='width:220px;'>" + descripcion + "</p></body></html>");
        lblDesc.setFont(UIThemeManager.FUENTE_CAPTION);
        lblDesc.setForeground(UIThemeManager.COLOR_TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelInformacion.add(Box.createVerticalGlue());
        panelInformacion.add(lblIcono);
        panelInformacion.add(lblArea);
        panelInformacion.add(lblDesc);
        panelInformacion.add(Box.createVerticalGlue());

        // Forzar al botón a centrarse horizontalmente en la parte inferior de la tarjeta
        botonAccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonAccion.setMaximumSize(new Dimension(240, UIThemeManager.ALTO_BOTON_PRINCIPAL));
        
        JPanel panelBotonContenedor = new JPanel();
        panelBotonContenedor.setLayout(new BoxLayout(panelBotonContenedor, BoxLayout.Y_AXIS));
        panelBotonContenedor.setOpaque(false);
        panelBotonContenedor.add(botonAccion);

        contenedorTarjeta.add(panelInformacion, BorderLayout.CENTER);
        contenedorTarjeta.add(panelBotonContenedor, BorderLayout.SOUTH);

        return contenedorTarjeta;
    }

   // ... dentro de PantallaPrincipalView.java, reemplaza tu método crearBotonModulo ...
private JButton crearBotonModulo(String texto) {
        BotonRedondeado btn = new BotonRedondeado(texto);
        btn.setFont(UIThemeManager.FUENTE_BOTON_PRINCIPAL);
        
        // --- CAMBIA EL COLOR AQUÍ ---
        btn.setBackground(UIThemeManager.COLOR_BOTON_FONDO); 
        btn.setForeground(Color.WHITE); // Texto blanco para buen contraste
        // ----------------------------
        
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(280, 60)); 

        // Efecto visual al pasar el mouse
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { 
                btn.setBackground(UIThemeManager.PASTEL_VIVIDO_AMBAR); // Cambia al ámbar de tu marca al pasar el mouse
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { 
                btn.setBackground(UIThemeManager.COLOR_BOTON_FONDO); 
            }
        });

        return btn;
    }

    // Clase interna para manejar el dibujo redondeado
    private static class BotonRedondeado extends JButton {
        public BotonRedondeado(String label) {
            super(label);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Pintar fondo redondeado
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), UIThemeManager.RADIO_BORDE_BOTON, UIThemeManager.RADIO_BORDE_BOTON);
            
            // Pintar texto centrado
            FontMetrics fm = g2.getFontMetrics();
            Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
            int x = (rect.width - fm.stringWidth(getText())) / 2;
            int y = (rect.height - fm.getHeight()) / 2 + fm.getAscent();
            
            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    private JPanel crearPanelStatus() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        panel.setBackground(UIThemeManager.COLOR_FONDO_PRINCIPAL);
        panel.setBorder(new MatteBorder(1, 0, 0, 0, UIThemeManager.COLOR_BORDE_PANEL));
        panel.setPreferredSize(new Dimension(0, 35));

        panel.add(Box.createHorizontalStrut(UIThemeManager.MARGEN_PANEL));
        panel.add(lblStatus);
        return panel;
    }

    public void actualizarStatus(String mensaje) { lblStatus.setText("  " + mensaje); }
    public JButton getBtnIniciarPedido() { return btnIniciarPedido; }
    public JButton getBtnConsultarProducto() { return btnConsultarProducto; }
    public JButton getBtnGenerarProforma() { return btnGenerarProforma; }
    public JButton getBtnCambiosDevolucion() { return btnCambiosDevolucion; }
    public JButton getBtnSalir() { return btnSalir; }
}