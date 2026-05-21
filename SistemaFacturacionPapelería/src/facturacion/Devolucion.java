package facturacion;

public class Devolucion {
    private String idDevolucion;
    private String motivo;
    private double montoReembolsado;

    public Devolucion(String idDevolucion, String motivo) {
        this.idDevolucion = idDevolucion;
        this.motivo = motivo;
    }

    public double calculaReembolso(Factura f) {
        this.montoReembolsado = f.getTotal() * -1;
        return this.montoReembolsado;
    }

    // Getters y Setters
    public String getIdDevolucion() { return idDevolucion; }
    public String getMotivo() { return motivo; }
    public double getMontoReembolsado() { return montoReembolsado; }
}