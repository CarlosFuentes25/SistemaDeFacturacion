import java.util.Date;
import java.util.ArrayList;

public class Proforma {
    private String idProforma;
    private double totalProforma;
    private Date fecha;
    
   
    private ArrayList<DetalleProforma> listaDetallesProforma;

    // Constructor
    public Proforma(String idProforma, Date fecha) {
        this.idProforma = idProforma;
        this.fecha = fecha;
        this.totalProforma = 0.0;
        this.listaDetallesProforma = new ArrayList<>();
    }

    // --- GETTERS ---
    public String getIdProforma() {
        return idProforma;
    }

    public double getTotalProforma() {
        return totalProforma;
    }

    public Date getFecha() {
        return fecha;
    }

    public ArrayList<DetalleProforma> getListaDetalles() {
        return listaDetallesProforma;
    }

    // --- SETTERS ---
    // (Omitimos setter de idProforma por inmutabilidad, y de totalProforma porque se calcula solo)

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

  
    public double calcularTotalProforma() {
        double sumaTotal = 0.0;
        
        
        for (DetalleProforma detalle : this.listaDetallesProforma) {
            sumaTotal += detalle.getSubtotal(); 
        }

        this.totalProforma = sumaTotal;
        return this.totalProforma;
    }

   
    public String imprimirBorrador() {
        StringBuilder borrador = new StringBuilder();
        
        borrador.append("=== BORRADOR DE PROFORMA ===\n");
        borrador.append("ID Proforma: ").append(this.idProforma).append("\n");
        borrador.append("Fecha: ").append(this.fecha.toString()).append("\n");
        borrador.append("Cantidad de items: ").append(this.listaDetallesProforma.size()).append("\n");
        borrador.append("TOTAL ESTIMADO: $").append(this.totalProforma).append("\n");
        borrador.append("============================\n");
        
        return borrador.toString();
    }
    
    public void agregarDetalle(DetalleProforma detalle) {
        if (detalle != null) {
            this.listaDetallesProforma.add(detalle);
            this.calcularTotalProforma(); // Asegura que el total siempre esté actualizado
        }
    }
}