package ventas;

import stock.CatalogoProducto;
import stock.Producto;
import pedido.Pedido;

public class GestorVenta {
    private CatalogoProducto catalogo;

    public GestorVenta(CatalogoProducto catalogo) {
        this.catalogo = catalogo;
    }

    public void registrarNuevoCliente(facturacion.Cliente c) {
    }

    public Venta iniciarProcesoVenta(Pedido p) {
        return new Venta("VENTA-" + p.getTotal(), p);
    }

    public String generarProforma() {
        return "Proforma inicializada";
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