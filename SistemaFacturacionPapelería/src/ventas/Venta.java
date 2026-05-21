package ventas;

import pedido.Pedido;
import java.util.Date;

public class Venta {
    private String idVenta;
    private Date fecha;
    private double totalVenta;
    private double montoPagado;
    private String metodoPago;

    private Pedido pedido;
    private Vendedor vendedoor;

    public Venta(String idVenta, Pedido pedido) {
        this.idVenta = idVenta;
        this.fecha = new Date();
        this.pedido = pedido;
        if (pedido != null) {
            this.totalVenta = pedido.getTotal();
        }
    }

    public void confirmarVenta(Pedido pedido, Vendedor vend) {
        this.pedido = pedido;
        this.vendedoor = vend;
        if (vend != null) {
            vend.agregarVenta(this);
        }
    }

    public boolean validarMontoCompleto(double totalFactura) {
        return this.montoPagado >= totalFactura;
    }

    // GETTERS Y SETTERS
    public String getIdVenta() { return idVenta; }
    public void setIdVenta(String idVenta) { this.idVenta = idVenta; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }

    public double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Vendedor getVendedoor() { return vendedoor; }
    public void setVendedoor(Vendedor vendedoor) { this.vendedoor = vendedoor; }
}