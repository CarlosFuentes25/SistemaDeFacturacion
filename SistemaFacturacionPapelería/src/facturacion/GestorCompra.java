package facturacion;

//import pedido.Proforma;\
import facturacion.Cliente;
import facturacion.Devolucion;
import facturacion.EstadoPago;
import facturacion.Factura;
import facturacion.Venta;
import pedido.Proforma;

//import java.util.ArrayList;

public class GestorCompra {

    public GestorCompra() {
        
    }

    // metodo para emitir factura a partir de una venta
    public Factura emitirFactura(Venta venta, Cliente cliente) {
        if (venta == null || cliente == null) {
            throw new IllegalArgumentException("La Venta y el Cliente son dependencias obligatorias.");
        }
        
        Factura nuevaFactura = new Factura(venta.getFecha(), venta.getTotalVenta(), cliente);
        return nuevaFactura;
    }

    //metodo para procesar el pago de una factura
    public void procesarPago(Factura factura, double montoPagado) {
        if (factura == null) return;
        
        if (montoPagado >= factura.getTotal()) {
            factura.setEstadoPago(EstadoPago.PAGADO);
        }
    }

    //metodo para gestionar devoluciones
    public Devolucion gestionarReembolso(Factura factura, String motivo) {
        if (factura == null) {
            throw new IllegalArgumentException("Debe proporcionar una factura válida.");
        }
        if (factura.getEstadoPago() != EstadoPago.PAGADO) {
            throw new IllegalStateException("Transacción denegada: Solo se admiten reembolsos en facturas pagadas.");
        }
        
        String idDevolucion = "DEV-" + System.currentTimeMillis();
        Devolucion devolucion = new Devolucion(idDevolucion, motivo);
        
        // Ejecución del cálculo cruzado
        devolucion.calculaReembolso(factura);
        
        // Regla de negocio: la factura original pierde validez tributaria
        factura.setEstadoPago(EstadoPago.ANULADO);
        
        return devolucion;
    }
    
}
