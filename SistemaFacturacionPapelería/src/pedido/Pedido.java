package pedido;

import stock.DetalleProducto;
import stock.Producto;
import java.util.ArrayList;
import java.util.Date;

public class Pedido {
    private String idPedido;
    private Date fecha;
    private EstadoPedido estado;
    private double total;
    private ArrayList<DetalleProducto> listaDetalles;

    public Pedido(String idPedido) {
        this.idPedido = idPedido;
        this.fecha = new Date();
        this.estado = EstadoPedido.CREADO;
        this.listaDetalles = new ArrayList<>();
        this.total = 0.0;
    }

    public boolean agregarProducto(Producto p, int cant) {
        if (p.verificarDisponibilidad(cant)) {
            DetalleProducto detalle = new DetalleProducto(p, cant);
            listaDetalles.add(detalle);
            p.descontarStock(cant); // Aplicamos lógica real de inventario
            this.total = calcularTotalPedido();
            return true;
        }
        return false;
    }

    public double calcularTotalPedido() {
        double calculoTotal = 0.0;
        for (DetalleProducto detalle : listaDetalles) {
            calculoTotal += detalle.getSubtotal();
        }
        return calculoTotal;
    }
    
    // Getters y Setters
    public String getIdPedido() { return idPedido; }
    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public ArrayList<DetalleProducto> getListaDetalles() { return listaDetalles; }
    public void setListaDetalles(ArrayList<DetalleProducto> listaDetalles) { this.listaDetalles = listaDetalles; }
}