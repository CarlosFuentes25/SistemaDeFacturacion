package facturacion;

import ventas.Venta;
import stock.DetalleProducto;
import java.util.ArrayList;

public class GestorCompra {

    // LISTADOS DE PERSISTENCIA
    private ArrayList<Factura> historialFacturas;
    private ArrayList<Devolucion> historialDevoluciones;

    public GestorCompra() {
        this.historialFacturas = new ArrayList<>();
        this.historialDevoluciones = new ArrayList<>();
    }

    public Factura emitirFactura(Venta v, Cliente c) {
        Factura nuevaFactura = new Factura("FAC-" + System.currentTimeMillis(), v, c);
        this.historialFacturas.add(nuevaFactura); // Guardamos la factura
        return nuevaFactura;
    }

    public boolean procesarPago(Factura f, double montoPagado) {
        if(montoPagado >= f.getTotal() && f.getEstadoPago() == EstadoPago.PENDIENTE) {
            f.setEstadoPago(EstadoPago.PAGADO);
            return true;
        }
        return false; // Evita pagar si no alcanza el dinero o si ya está pagada/anulada
    }

    // CONTROL TOTAL: REEMBOLSO CON DEVOLUCIÓN DE STOCK REAL
    public double gestionarReembolso(Factura f, String motivo) {
        if (f.getEstadoPago() != EstadoPago.PAGADO) return 0.0; // Solo se reembolsa si se pagó
        
        Devolucion devolucion = new Devolucion("DEV-" + System.currentTimeMillis(), motivo);
        f.setDevolucion(devolucion);
        f.setEstadoPago(EstadoPago.ANULADO);
        
        // Magia de Logística: Leer la factura y devolver los productos al catálogo
        for(DetalleProducto dp : f.getVenta().getPedido().getListaDetalles()) {
            dp.getProducto().aumentarStock(dp.getCantidad());
        }
        
        this.historialDevoluciones.add(devolucion); // Guardar registro del movimiento
        return devolucion.calculaReembolso(f);
    }

    public Factura buscarFactura(String idFactura) {
        for(Factura f : historialFacturas) {
            if(f.getIdFactura().equals(idFactura)) return f;
        }
        return null;
    }
}