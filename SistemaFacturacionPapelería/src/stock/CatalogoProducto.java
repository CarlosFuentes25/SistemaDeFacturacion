package stock;

import java.util.ArrayList;

public class CatalogoProducto {
    private ArrayList<Producto> listaProducto;

    public CatalogoProducto() {
        this.listaProducto = new ArrayList<>();
        // Inicializamos con algunos productos para prueba
        cargarInventarioInicial();
    }

    private void cargarInventarioInicial() {
        agregarNuevoProducto(new Producto("CUA001", "Cuaderno Parvulario", 1.50, 100));
        agregarNuevoProducto(new Producto("LAP001", "Lápices HB", 0.50, 80));
    }

    // BUSCADOR INTELIGENTE: Permite buscar por código O nombre (dinámico)
    public Producto buscarProducto(String criterio) {
        if (criterio == null || criterio.isEmpty()) return null;
        
        for (Producto p : listaProducto) {
            if (p.getIdProducto().equalsIgnoreCase(criterio) || 
                p.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                return p;
            }
        }
        return null;
    }

    // AUMENTAR STOCK: Modifica directamente la lista interna
    public void aumentarStock(String idProducto, int cantidad) {
        Producto p = buscarProducto(idProducto);
        if (p != null && cantidad > 0) {
            p.setStock(p.getStock() + cantidad);
        }
    }

    public void agregarNuevoProducto(Producto p) {
        this.listaProducto.add(p);
    }

    public ArrayList<Producto> getListaProducto() {
        return listaProducto;
    }
}