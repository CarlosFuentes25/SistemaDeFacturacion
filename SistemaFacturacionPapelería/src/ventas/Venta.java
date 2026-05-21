package ventas;

import pedido.EstadoPedido;
import pedido.Pedido;
import java.util.Date;

public class Venta {
    private String idVenta;
    private Date fecha;
    private double totalVenta;
    private double montoPagado;
    private String metodoPago; 
    
    private Pedido pedido;
    private Vendedor vendedor;

    public Venta(String idVenta, Pedido pedido) {
        this.idVenta = idVenta;
        this.fecha = new Date();
        this.pedido = pedido;
        this.totalVenta = pedido.getTotal();
        this.montoPagado = 0.0;
        this.metodoPago = "PENDIENTE";
    }

    public void confirmarVenta(Pedido pedido, Vendedor vend) {
        this.pedido = pedido;
        this.vendedor = vend;
        this.pedido.setEstado(EstadoPedido.REGISTRADO);
        vend.agregarVenta(this);
    }

    public boolean validarMontoCompleto(double totalFactura) {
        return this.montoPagado >= totalFactura;
    }
    
    public double getTotalVenta() { return totalVenta; }
    public Pedido getPedido() { return pedido; }
    public double getMontoPagado() { return montoPagado; }
    public String getMetodoPago() { return metodoPago; }

    // 👇 ESTOS SON LOS DOS MÉTODOS QUE FALTABAN 👇
    public void setMontoPagado(double monto) { this.montoPagado = monto; }
    public void setMetodoPago(String metodo) { this.metodoPago = metodo; }
}