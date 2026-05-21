package ventas;

import facturacion.Cliente;
import pedido.Pedido;
import pedido.Proforma;
import stock.CatalogoProducto;
import stock.Producto;
import java.util.ArrayList;

public class GestorVenta {
    private CatalogoProducto catalogo;
    
    // LISTADOS DE PERSISTENCIA (Las tablas de tu Base de Datos en Memoria)
    private ArrayList<Cliente> directorioClientes;
    private ArrayList<Venta> historialVentas;
    private ArrayList<Proforma> historialProformas;

    public GestorVenta(CatalogoProducto catalogo) {
        this.catalogo = catalogo;
        this.directorioClientes = new ArrayList<>();
        this.historialVentas = new ArrayList<>();
        this.historialProformas = new ArrayList<>();
    }

    public void registrarNuevoCliente(Cliente c) {
        // Evitar duplicados por cédula
        for(Cliente existente : directorioClientes) {
            if(existente.getCedula().equals(c.getCedula())) return;
        }
        this.directorioClientes.add(c);
    }

    public Venta iniciarProcesoVenta(Pedido p) {
        Venta nuevaVenta = new Venta("VNT-" + System.currentTimeMillis(), p);
        this.historialVentas.add(nuevaVenta); // Se guarda en el registro
        return nuevaVenta;
    }

    public Proforma generarProforma(Pedido p) {
        Proforma prof = new Proforma("PRF-" + System.currentTimeMillis(), p.getListaDetalles());
        this.historialProformas.add(prof); // Se guarda en el registro de cotizaciones
        return prof;
    }
    
    // Método para recuperar una proforma pasada
    public Proforma buscarProforma(String id) {
        for(Proforma prof : historialProformas) {
            if(prof.getIdProforma().equals(id)) return prof;
        }
        return null;
    }

    public CatalogoProducto getCatalogo() { return catalogo; }
}