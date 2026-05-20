package operaciones;

import facturacion.Factura;
import pedido.Proforma;

import java.util.ArrayList;

public class GestorCompra {

	private ArrayList<Factura> listaClientes;
    private ArrayList<Compra> listaClientes2;     
    private ArrayList<Devolucion> listaClientes3;
    private ArrayList<Proforma> listaClientes4;
    private ArrayList<Comprobante> listaClientes5;

    public GestorCompra() {
        this.listaClientes = new ArrayList<>();
        this.listaClientes2 = new ArrayList<>();
        this.listaClientes3 = new ArrayList<>();
        this.listaClientes4 = new ArrayList<>();
        this.listaClientes5 = new ArrayList<>();
    }

    public Factura emitirFactura(Venta v) {
        return new Factura(v.getFecha(), v.getTotalVenta()); 
    }

    public void procesarPago(Factura pagoCompra) {
    }

    public void gestionarDevolucion(Factura motivo) {
    }

    public ArrayList<Factura> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(ArrayList<Factura> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public ArrayList<Compra> getListaClientes2() {
        return listaClientes2;
    }

    public void setListaClientes2(ArrayList<Compra> listaClientes2) {
        this.listaClientes2 = listaClientes2;
    }

    public ArrayList<Devolucion> getListaClientes3() {
        return listaClientes3;
    }

    public void setListaClientes3(ArrayList<Devolucion> listaClientes3) {
        this.listaClientes3 = listaClientes3;
    }

    public ArrayList<Proforma> getListaClientes4() {
        return listaClientes4;
    }

    public void setListaClientes4(ArrayList<Proforma> listaClientes4) {
        this.listaClientes4 = listaClientes4;
    }

    public ArrayList<Comprobante> getListaClientes5() {
        return listaClientes5;
    }

    public void setListaClientes5(ArrayList<Comprobante> listaClientes5) {
        this.listaClientes5 = listaClientes5;
    }
}
