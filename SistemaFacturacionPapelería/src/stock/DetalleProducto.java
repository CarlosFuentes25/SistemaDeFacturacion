package stock;

// Clase Padre DetalleProducto
public class DetalleProducto {
    protected int cantidad;
    protected double subtotal;

public DetalleProducto() {

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

    public double calcularSubtotal(double precio) {
        return 0.0;
    }
}