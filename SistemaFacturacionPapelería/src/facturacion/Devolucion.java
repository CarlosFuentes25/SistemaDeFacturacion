package facturacion;

public class Devolucion {
    private String idDevolucion;
    private String motivo;
    private double montoReembolsado;

    // Constructor
    public Devolucion(String idDevolucion, String motivo, double montoReembolsado) {
        this.idDevolucion = idDevolucion;
        this.motivo = motivo;
        this.montoReembolsado = montoReembolsado;
    }

    // Getters y Setters
    public String getIdDevolucion() {
        return idDevolucion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getMontoReembolsado() {
        return montoReembolsado;
    }

    public void setMontoReembolsado(double montoReembolsado) {
        this.montoReembolsado = montoReembolsado;
    }

    public double calculaReembolso(Factura f) {
        if(f != null){
            this.montoReembolsado = f.getTotal();
            return this.montoReembolsado;
        }
        return 0.0;
    }
}