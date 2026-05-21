package pedido;

import stock.DetalleProducto;
import java.util.ArrayList;
import java.util.Date;

public class Proforma {
    private String idProforma;
    private double totalProforma;
    private Date fecha;
    private ArrayList<DetalleProducto> listaDetalleProforma;

    public Proforma(String idProforma, ArrayList<DetalleProducto> detalles) {
        this.idProforma = idProforma;
        this.fecha = new Date();
        this.listaDetalleProforma = detalles;
        this.totalProforma = calcularTotalProforma();
    }

    public double calcularTotalProforma() {
        double total = 0.0;
        for (DetalleProducto dp : listaDetalleProforma) {
            total += dp.getSubtotal();
        }
        return total;
    }

    public String imprimirBorrador() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PROFORMA TINTA & TRAZO [").append(idProforma).append("] ===\n");
        for(DetalleProducto dp : listaDetalleProforma) {
            sb.append(dp.getCantidad()).append("x ").append(dp.getProducto().getNombre())
              .append(" - $").append(String.format("%.2f", dp.getSubtotal())).append("\n");
        }
        sb.append("---------------------------------\n");
        sb.append("TOTAL ESTIMADO: $").append(String.format("%.2f", totalProforma));
        return sb.toString();
    }

    // Getters y Setters
    public String getIdProforma() { return idProforma; }
    public double getTotalProforma() { return totalProforma; }
    public Date getFecha() { return fecha; }
    public ArrayList<DetalleProducto> getListaDetalleProforma() { return listaDetalleProforma; }
}