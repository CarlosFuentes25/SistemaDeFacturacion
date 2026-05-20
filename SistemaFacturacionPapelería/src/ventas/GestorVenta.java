package ventas;

import facturacion.Cliente;
import pedido.Pedido;
import stock.CatalogoProducto;

import java.util.ArrayList;

public class GestorVenta {

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Pedido> listaPedidos;
    private ArrayList<Venta> listaVentas;
    private CatalogoProducto catalogo;

    public GestorVenta() {
        this.listaClientes = new ArrayList<>();
        this.listaPedidos = new ArrayList<>();
        this.listaVentas = new ArrayList<>();
    }

    public GestorVenta(ArrayList<Cliente> listaClientes, ArrayList<Pedido> listaPedidos, ArrayList<Venta> listaVentas, CatalogoProducto catalogo) {
        this.listaClientes = listaClientes;
        this.listaPedidos = listaPedidos;
        this.listaVentas = listaVentas;
        this.catalogo = catalogo;
    }

    public void registrarNuevoCliente(Cliente c) {
        this.listaClientes.add(c);
    }

    public Venta iniciarProcesoVenta(Pedido p) {
        this.listaPedidos.add(p);
        Venta nuevaVenta = new Venta();
        this.listaVentas.add(nuevaVenta);
        return nuevaVenta;
    }

    public ArrayList<Cliente> getListaClientes() { return listaClientes; }
    public void setListaClientes(ArrayList<Cliente> listaClientes) { this.listaClientes = listaClientes; }

    public ArrayList<Pedido> getListaPedidos() { return listaPedidos; }
    public void setListaPedidos(ArrayList<Pedido> listaPedidos) { this.listaPedidos = listaPedidos; }

    public ArrayList<Venta> getListaVentas() { return listaVentas; }
    public void setListaVentas(ArrayList<Venta> listaVentas) { this.listaVentas = listaVentas; }

    public CatalogoProducto getCatalogo() { return catalogo; }
    public void setCatalogo(CatalogoProducto catalogo) { this.catalogo = catalogo; }
}