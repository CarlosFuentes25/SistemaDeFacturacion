package operaciones;

import java.awt.Color;
import java.awt.Font;

public final class UIThemeManager {

    private UIThemeManager() {
        throw new UnsupportedOperationException("Clase de utilidad estática.");
    }

    // =========================================================================
    // PALETA DE COLORES - BASE PASTEL (Sin tildes para evitar errores de compilación)
    // =========================================================================
    public static final Color COLOR_FONDO_PRINCIPAL      = new Color(0xF1F5F9);
    public static final Color COLOR_SUPERFICIE_PANEL     = new Color(0x94A3B8);
    public static final Color COLOR_BORDE_PANEL          = new Color(0xCBD5E1);
    public static final Color COLOR_HEADER_FONDO         = new Color(0xE2E8F0);

    // Paleta Semántica
    public static final Color PASTEL_VIVIDO_AMBAR        = new Color(0xFCD34D);
    public static final Color PASTEL_BLUE                = new Color(0xDBEAFE);
    public static final Color PASTEL_MINT                = new Color(0xDCFCE7);
    public static final Color PASTEL_PEACH               = new Color(0xFEE2E2);
    public static final Color PASTEL_LAVENDER            = new Color(0xF3E8FF);
    public static final Color PASTEL_ROSE                = new Color(0xFFEDF1);

    // Colores de Texto Módulos (Renombrados sin tildes)
    public static final Color TEXTO_MODULO_BLUE          = new Color(0x1E40AF);
    public static final Color TEXTO_MODULO_MINT          = new Color(0x166534);
    public static final Color TEXTO_MODULO_PEACH         = new Color(0x991B1B);
    public static final Color TEXTO_MODULO_LAVENDER      = new Color(0x6B21A8);
    public static final Color TEXTO_MODULO_ROSE          = new Color(0x9F1239);

    public static final Color COLOR_TEXTO_PRINCIPAL      = new Color(0x334155);
    public static final Color COLOR_TEXTO_SECUNDARIO     = new Color(0x1E40AF);
    public static final Color COLOR_BOTON_FONDO          = new Color(0x7C3AED); 

    // Tipografía
    public static final Font FUENTE_TITULO               = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FUENTE_SUBTITULO            = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FUENTE_ETIQUETA             = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_BOTON_PRINCIPAL      = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FUENTE_CAPTION              = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FUENTE_INPUT                = new Font("SansSerif", Font.PLAIN, 13);

    public static final int MARGEN_PANEL             = 20;
    public static final int GAP_COMPONENTES          = 15;
    public static final int ALTO_BOTON_PRINCIPAL     = 42;
    public static final int ANCHO_VENTANA_MINIMO     = 1000;
    public static final int ALTO_VENTANA_MINIMO      = 650;
    public static final int RADIO_BORDE_BOTON        = 25;
}