
import java.util.ArrayList;
import java.util.Date;

public class Pedido {

	private String idPedido;
    private Date fecha;
    private EstadoPedido estado; 
    private double total;
    private ArrayList<DetallePedido> listaDetalles; 

    // Constructor básico
    public Pedido() {
        this.listaDetalles = new ArrayList<>();
    }

    public Pedido(String idPedido, Date fecha, EstadoPedido estado, double total) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.listaDetalles = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cant) {
    }

    public double calcularTotalPedido() {
        return this.total;
    }

    public String generarProforma() {
        return "";
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<DetallePedido> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(ArrayList<DetallePedido> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }
}
