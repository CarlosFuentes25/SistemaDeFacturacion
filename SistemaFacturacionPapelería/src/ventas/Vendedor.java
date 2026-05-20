package ventas;

public class Vendedor {
    private String idVendedor;
    private String nombre;

    // Constructor
    public Vendedor(String idVendedor, String nombre) {
        this.idVendedor = idVendedor;
        this.nombre = nombre;
    }

    // --- GETTERS ---
    public String getIdVendedor() {
        return idVendedor;
    }

    public String getNombre() {
        return nombre;
    }

    // --- SETTERS ---

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}