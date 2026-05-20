package ventas;

import facturacion.Factura;
import pedido.Pedido;

import java.util.Date;

public class Venta {

    private String idVenta;
    private Date fecha;
    private double totalVenta;
    private Vendedor vendedor;
    private Factura factura;
    private Pedido pedido;

    public Venta() {
        this.fecha = new Date();
    }

    public Venta(String idVenta, Date fecha, double totalVenta, Pedido pedido, Vendedor vendedor) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.totalVenta = totalVenta;
        this.pedido = pedido;
        this.vendedor = vendedor;
    }

    public void confirmarVenta(Pedido pedido, Vendedor vend) {
        this.pedido = pedido;     
        this.vendedor = vend;     
        this.totalVenta = pedido.calcularTotalPedido(); 
        this.factura = new Factura(this.fecha, this.totalVenta); 
    }

    public String getIdVenta() { return idVenta; }
    public void setIdVenta(String idVenta) { this.idVenta = idVenta; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }

    public Vendedor getVendedor() { return vendedor; }
    public void setVendedor(Vendedor vendedor) { this.vendedor = vendedor; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}