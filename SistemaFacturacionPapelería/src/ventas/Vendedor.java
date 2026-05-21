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

    public void agregarVenta(Venta v) {
        this.listaVentasAtendidas.add(v);
    }

    // GETTERS Y SETTERS
    public String getIdVendedor() { return idVendedor; }
    public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public ArrayList<Venta> getListaVentasAtendidas() { return listaVentasAtendidas; }
    public void setListaVentasAtendidas(ArrayList<Venta> listaVentasAtendidas) {
        this.listaVentasAtendidas = listaVentasAtendidas;
    }
}