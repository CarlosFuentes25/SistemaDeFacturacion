import java.util.Date;

public class Comprobante {
    private String idComprobante;
    private Date fechaEmision;
    private double montoTotal;


    public Comprobante() {
        this.fechaEmision = new Date();
    }


    public Comprobante(String idComprobante, Date fechaEmision, double montoTotal) {
        this.idComprobante = idComprobante;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
    }

    public String generarCuerpoRecibo() {
        return "ID Comprobante: " + this.idComprobante + " - Total: $" + this.montoTotal;
    }

    public String getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(String idComprobante) {
        this.idComprobante = idComprobante;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}