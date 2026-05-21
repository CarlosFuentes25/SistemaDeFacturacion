package facturacion;

import pedido.Pedido;
import pedido.Proforma;
import java.util.ArrayList;

public class Cliente {
    private String cedula;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private ArrayList<Pedido> listaPedido;
    private ArrayList<Proforma> listaProforma;

    public Cliente(String cedula, String nombre, String direccion, String telefono, String email) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.listaPedido = new ArrayList<>();
        this.listaProforma = new ArrayList<>();
    }

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public ArrayList<Pedido> getListaPedido() { return listaPedido; }
    public ArrayList<Proforma> getListaProforma() { return listaProforma; }
}