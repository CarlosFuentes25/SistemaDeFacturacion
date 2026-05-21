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
    
    // Getters y Setters
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
        this.subtotal = calcularSubtotal(this.precioUnitario);
    }
    public double getSubtotal() { return subtotal; }
    public double getPrecioUnitario() { return precioUnitario; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}