package ventas;

import java.util.ArrayList;

public class Vendedor {
    private String idVendedor;
    private String nombre;
    private ArrayList<Venta> listaVentasAtendidas;

    public Vendedor(String idVendedor, String nombre) {
        this.idVendedor = idVendedor;
        this.nombre = nombre;
        this.listaVentasAtendidas = new ArrayList<>();
    }
    
    public void agregarVenta(Venta v) { this.listaVentasAtendidas.add(v); }
    public String getNombre() { return nombre; }
}