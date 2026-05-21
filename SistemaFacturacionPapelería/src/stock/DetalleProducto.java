package stock;

public class DetalleProducto {
    private int cantidad;
    private double subtotal;
    private double precioUnitario;
    private Producto producto;

    public DetalleProducto(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        this.subtotal = calcularSubtotal(this.precioUnitario);
    }

    public double calcularSubtotal(double precio) {
        return precio * this.cantidad;
    }
    
    public double getSubtotal() { 
        return subtotal; 
        }
    public Producto getProducto() { 
        return producto; 
        }
    public int getCantidad() { 
        return cantidad; }
}