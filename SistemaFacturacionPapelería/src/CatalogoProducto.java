import java.util.ArrayList;

public class CatalogoProducto {

    private ArrayList<Producto> listaProductos;

    public CatalogoProducto() {
        this.listaProductos = new ArrayList<>();
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    
    public void actualizarExistencias(Producto p, int cant) {
       if(p == null) return;

       Producto productoEnCatalogo = buscarProductoPorCodigo(p.getIdProducto());
         if(productoEnCatalogo != null) {
              productoEnCatalogo.setStock(cant);
         }
    }

  
    public Producto buscarProductoPorCodigo(String codigo){
        for(Producto p : listaProductos){
            if(p.getIdProducto().equals(codigo)){
                return p;
            }
        }
        return null; // Si no se encuentra el producto, se retorna null
    }

   
    public void agregarProductoNuevo(Producto p) {
        if (p != null) {
            this.listaProductos.add(p);
        }
    }


}