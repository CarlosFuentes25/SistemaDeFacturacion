import java.util.Date;


public class Compra {
    private String idCompra;
    private Date fechaPago;
    private double montoPagado;
    private String metodoPago;

    // Constructor
    public Compra(String idCompra, Date fechaPago, double montoPagado, String metodoPago) {
        this.idCompra = idCompra;
        this.fechaPago = fechaPago;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public String getIdCompra() {
        return idCompra;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public boolean validarMontoCompleto(double totalFactura) {
        return this.montoPagado >= totalFactura; //si el monto pagado es exactamente igual o mayor al total de la factura
    }
}