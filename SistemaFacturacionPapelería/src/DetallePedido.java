
public class DetallePedido {

    private int cantidad;
    private double precioUnitarioVenta;
    private double subtotal;

    public DetallePedido() {
    }

    public DetallePedido(int cantidad, double precioUnitarioVenta, double subtotal) {
        this.cantidad = cantidad;
        this.precioUnitarioVenta = precioUnitarioVenta;
        this.subtotal = subtotal;
    }

    public double calcularSubtotal(double precio, double cantidad) {
        this.subtotal = precio * cantidad;
        return this.subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(double precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
