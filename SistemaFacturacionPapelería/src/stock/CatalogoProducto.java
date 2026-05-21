package stock;

import java.util.ArrayList;

public class CatalogoProducto {

    private ArrayList<Producto> listaProducto;

    public CatalogoProducto() {
        this.listaProducto = new ArrayList<>();
    }

    public void actualizarExistencias(Producto p, int cant) {
        p.setStock(p.getStock() + cant);
    }

    public Producto buscarProductoPorCodigo(String codigo) {
        for (Producto p : listaProducto) {
            if (p.getIdProducto().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    public void agregarNuevoProducto(Producto p) {
        this.listaProductos.add(p);
    }
}