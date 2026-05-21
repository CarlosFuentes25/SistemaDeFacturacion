package facturacion;

import ventas.Venta;
import stock.DetalleProducto;
import java.util.Date;

public class Factura {
    private String idFactura;
    private Date fechaEmision; 
    private double total;
    private double valorIVA;
    private EstadoPago estadoPago;
    private double baseImponible;
    private Devolucion devolucion;
    
    private Venta venta;
    private Cliente cliente;

    public Factura(String idFactura, Venta venta, Cliente cliente) {
        this.idFactura = idFactura;
        this.fechaEmision = new Date();
        this.venta = venta;
        this.cliente = cliente;
        this.baseImponible = venta.getTotalVenta();
        this.estadoPago = EstadoPago.PENDIENTE;
        calcularImpuesto();
    }

    public void calcularImpuesto() {
        this.valorIVA = this.baseImponible * 0.15; // 15% IVA
        this.total = this.baseImponible + this.valorIVA;
    }

    public String generarNroFactura() {
        return this.idFactura;
    }

    public String imprimirFactura() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== FACTURA ").append(idFactura).append(" ==========\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append(" | CI: ").append(cliente.getCedula()).append("\n");
        sb.append("---------------------------------------\n");
        for(DetalleProducto dp : venta.getPedido().getListaDetalles()) {
            sb.append(dp.getCantidad()).append("x ").append(dp.getProducto().getNombre())
              .append(" - $").append(String.format("%.2f", dp.getSubtotal())).append("\n");
        }
        sb.append("---------------------------------------\n");
        sb.append(String.format("Subtotal: $%.2f\n", baseImponible));
        sb.append(String.format("IVA (15%%): $%.2f\n", valorIVA));
        sb.append(String.format("TOTAL A PAGAR: $%.2f\n", total));
        sb.append("Estado: ").append(estadoPago).append("\n");
        sb.append("=======================================\n");
        return sb.toString();
    }
    
    // Getters y Setters
    public String getIdFactura() { return idFactura; }
    public Date getFechaEmision() { return fechaEmision; }
    public double getTotal() { return total; }
    public double getValorIVA() { return valorIVA; }
    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }
    public double getBaseImponible() { return baseImponible; }
    public Devolucion getDevolucion() { return devolucion; }
    public void setDevolucion(Devolucion devolucion) { this.devolucion = devolucion; }
    public Venta getVenta() { return venta; }
    public Cliente getCliente() { return cliente; }
}