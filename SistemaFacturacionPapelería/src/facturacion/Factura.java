package facturacion;

import java.util.Date;
//import java.util.ArrayList;

public class Factura {
    private String idFactura;
    private Date fecha;
    private double total;
    private double valorIVA;
    private EstadoPago estadoPago;
    private double baseImponible;
    private Cliente cliente;
    //private ArrayList<Factura> FacturaPagada;

    // Constructor
    public Factura(Date fecha, double baseImponible, Cliente cliente){
       
        this.idFactura = generarNroFactura(); 
        this.fecha = fecha;
        this.baseImponible = baseImponible;
        this.cliente = cliente;
        this.estadoPago = EstadoPago.PENDIENTE; //factura nace con estado pendiente
        //this.FacturaPagada = new ArrayList<>();
        
        this.calcularImpuesto();
    }

    // Método para calcular el IVA y el total de la factura
    public void calcularImpuesto() {
        double porcentajeIVA = 0.15; 
        this.valorIVA = this.baseImponible * porcentajeIVA;
        this.total = this.baseImponible + this.valorIVA;
    }

    // Método para generar un número de factura único
    private String generarNroFactura() {
        return "FAC-" + System.currentTimeMillis();
    }

    //geters y setters
    public String getIdFactura() {
        return idFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public double getValorIVA() {
        return valorIVA;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public double getBaseImponible() {
        return baseImponible;
    }

    /*public ArrayList<Factura> getFacturaPagada() {
        return FacturaPagada;
    } */
    

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public void setBaseImponible(double baseImponible) {
        this.baseImponible = baseImponible;
        
        this.calcularImpuesto();
    }
}