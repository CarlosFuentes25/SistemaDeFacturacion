public class DetalleProforma {
    private int cantidad;
    private double subtotal;

    public DetalleProforma(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = 0.0; // Inicializado en cero
    }

    public double calcularSubtotal(double precioUnitario) {
        this.subtotal = this.cantidad * precioUnitario;
        return this.subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}