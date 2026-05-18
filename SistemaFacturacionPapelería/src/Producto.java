
public class Producto {
    private String idProducto;
    private String nombre;
    private double precio;
    private int stock;

    // Constructor completo
    public Producto(String idProducto, String nombre, double precio, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public String getIdProducto() {
        return idProducto;
    }

    // Se omite el setter de idProducto para asegurar la inmutabilidad del ID
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean verificarDisponibilidad(int cantRequerida) {
        return this.stock >= cantRequerida;
    }

    public void descontarStock(int cantComprada) {
        if (verificarDisponibilidad(cantComprada)) {
            this.stock -= cantComprada;
        } else {
            System.out.println("Error: No hay suficiente stock para descontar.");
        }
    }
}