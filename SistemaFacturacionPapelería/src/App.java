import facturacion.GestorCompra;
import operaciones.PantallaPrincipalController;
import operaciones.PantallaPrincipalView;
import stock.CatalogoProducto;
import ventas.GestorVenta;
import ventas.Vendedor;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Inicializar la "Base de Datos" (Modelo)
            CatalogoProducto catalogo = new CatalogoProducto();
            GestorVenta gVenta = new GestorVenta(catalogo);
            GestorCompra gCompra = new GestorCompra();
            Vendedor cajero = new Vendedor("V-01", "Carlos Fuentes");

            // 2. Inicializar la Interfaz Gráfica (Vista)
            PantallaPrincipalView vista = new PantallaPrincipalView();
            
            // 3. Conectar la Interfaz con el Backend (Controlador)
            PantallaPrincipalController control = new PantallaPrincipalController(vista, catalogo, gVenta, gCompra, cajero);
            
            vista.setVisible(true);
        });
    }
}