package pedido;

// Clase Padre DetalleProducto
public class DetalleProducto {
    protected int cantidad;
    protected double subtotal;

    public double calcularSubtotal(double precio) {
        return 0.0;
    }
}