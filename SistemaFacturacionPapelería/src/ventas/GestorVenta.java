package ventas;

import facturacion.Cliente;
import pedido.Pedido;
import stock.CatalogoProducto;
import stock.Producto;

public class GestorVenta {
    private CatalogoProducto catalogo;

    public GestorVenta(CatalogoProducto catalogo) {
        this.catalogo = catalogo;
    }

    public void registrarNuevoCliente(Cliente c) {

    }

    public Venta iniciarProcesoVenta(Pedido p) {
        double totalPedido = (p != null) ? p.getTotal() : 0.0;
        return new Venta("VENTA-" + totalPedido, p);
    }

    public String generarProforma() {
        return "Proforma generada";
    }

    public void gestionarStock(Producto p, int cant) {
        if (catalogo != null && p != null) {
            catalogo.actualizarExistencias(p, cant);
        }
    }

    // GETTERS Y SETTERS
    public CatalogoProducto getCatalogo() { return catalogo; }
    public void setCatalogo(CatalogoProducto catalogo) { this.catalogo = catalogo; }
}