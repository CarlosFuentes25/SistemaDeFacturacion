package operaciones;

import java.awt.Color;
import java.awt.Font;

/**
 * UIThemeManager
 * Clase estática con paleta 100% armonizada en tonos pastel.
 */
public final class UIThemeManager {

    private UIThemeManager() {
        throw new UnsupportedOperationException("Clase de utilidad estática.");
    }

    // =========================================================================
    // PALETA DE COLORES - BASE PASTEL (Armonía General)
    // =========================================================================
    public static final Color COLOR_FONDO_PRINCIPAL      = new Color(0xF1F5F9); // Nube (Fondo limpio)
    public static final Color COLOR_SUPERFICIE_PANEL     = new Color(0x94A3B8); // Gris azulado pastel (Ideal para texto blanco)
    public static final Color COLOR_BORDE_PANEL          = new Color(0xCBD5E1); // Borde suave
    public static final Color COLOR_HEADER_FONDO         = new Color(0xE2E8F0); // Encabezado sutil

    // =========================================================================
    // PALETA SEMÁNTICA - PASTEL MÓDULOS (Suaves y coherentes)
    // =========================================================================
    public static final Color PASTEL_VIVIDO_AMBAR        = new Color(0xFCD34D); // Ámbar pastel
    public static final Color PASTEL_BLUE                = new Color(0xDBEAFE); // Blue pastel
    public static final Color PASTEL_MINT                = new Color(0xDCFCE7); // Mint pastel
    public static final Color PASTEL_PEACH               = new Color(0xFEE2E2); // Peach pastel
    public static final Color PASTEL_LAVENDER            = new Color(0xF3E8FF); // Lavender pastel
    public static final Color PASTEL_ROSE                = new Color(0xFFEDF1); // Rose pastel

    public static final Color TEXTO_MÓDULO_BLUE          = new Color(0x1E40AF);
    public static final Color TEXTO_MÓDULO_MINT          = new Color(0x166534);
    public static final Color TEXTO_MÓDULO_PEACH         = new Color(0x991B1B);
    public static final Color TEXTO_MÓDULO_LAVENDER      = new Color(0x6B21A8);
    public static final Color TEXTO_MÓDULO_ROSE          = new Color(0x9F1239);

    // =========================================================================
    // COLORES DE TEXTO (Ajustados a la paleta pastel)
    // =========================================================================
    public static final Color COLOR_TEXTO_PRINCIPAL      = new Color(0x334155); // Slate oscuro para lectura
    public static final Color COLOR_TEXTO_SECUNDARIO     = new Color(0x1E40AF); // Slate medio
    public static final Color COLOR_TEXTO_EN_ACENTO      = new Color(0xFFFFFF); // Blanco sobre botones

    // =========================================================================
    // BOTÓN - ARMÓNICO Y PASTEL
    // =========================================================================
    // Un tono lavanda profundo que resalta sobre el gris azulado sin romper la estética
    public static final Color COLOR_BOTON_FONDO          = new Color(0x7C3AED); 

    // =========================================================================
    // TIPOGRAFÍA Y MÉTRICAS (Mantenidas por consistencia)
    // =========================================================================
    private static final String FAMILIA_FUENTE = "SansSerif";
    public static final Font FUENTE_LOGO                 = new Font("Serif", Font.BOLD, 32);
    public static final Font FUENTE_TITULO               = new Font(FAMILIA_FUENTE, Font.BOLD, 22);
    public static final Font FUENTE_SUBTITULO            = new Font(FAMILIA_FUENTE, Font.BOLD, 14);
    public static final Font FUENTE_ETIQUETA             = new Font(FAMILIA_FUENTE, Font.PLAIN, 13);
    public static final Font FUENTE_BOTON_PRINCIPAL      = new Font(FAMILIA_FUENTE, Font.BOLD, 13);
    public static final Font FUENTE_CAPTION              = new Font(FAMILIA_FUENTE, Font.PLAIN, 11);
    public static final Font FUENTE_INPUT                = new Font(FAMILIA_FUENTE, Font.PLAIN, 13);

    public static final int MARGEN_PANEL             = 20;
    public static final int MARGEN_COMPONENTE        = 10;
    public static final int GAP_COMPONENTES          = 15;
    public static final int ALTO_BOTON_PRINCIPAL     = 42;
    public static final int ANCHO_VENTANA_MINIMO     = 1000;
    public static final int ALTO_VENTANA_MINIMO      = 650;
    public static final int RADIO_BORDE_BOTON        = 25;
}