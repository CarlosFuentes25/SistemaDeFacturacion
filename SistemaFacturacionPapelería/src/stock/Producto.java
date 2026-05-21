package stock;

public class Producto {
    private String idProducto;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String idProducto, String nombre, double precio, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public boolean verificarDisponibilidad(int cantRequerida) {
        return this.stock >= cantRequerida;
    }

    public void descontarStock(int cantComprada) {
        if (verificarDisponibilidad(cantComprada)) {
            this.stock -= cantComprada;
        }
    }

    public void aumentarStock(int cant) {
        this.stock += cant;
    }

    // Getters y Setters completos
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "[" + idProducto + "] " + nombre + " - $" + precio + " (Stock: " + stock + ")";
    }
}