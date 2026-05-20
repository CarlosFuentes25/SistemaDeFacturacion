import operaciones.PantallaPrincipalView;
import operaciones.PantallaPrincipalController;

import javax.swing.SwingUtilities;

public class App {
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            // Instanciar la Vista
            PantallaPrincipalView vista = new PantallaPrincipalView();
            
            // Instanciar el Controlador
            PantallaPrincipalController controlador = new PantallaPrincipalController(vista);
            
            // Mostrar la ventana
            vista.setVisible(true);
        });
    }
}