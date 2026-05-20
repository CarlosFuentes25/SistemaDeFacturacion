import javax.swing.SwingUtilities;

public class App {
   public static void main(String[] args) {
        // SwingUtilities.invokeLater garantiza que la GUI se construya
        // en el Event Dispatch Thread (EDT), como exige Swing.
        SwingUtilities.invokeLater(() -> {
            PantallaPrincipalView    vista       = new PantallaPrincipalView();
            PantallaPrincipalController controller = new PantallaPrincipalController(vista);
            vista.setVisible(true);
        });
    }
}
