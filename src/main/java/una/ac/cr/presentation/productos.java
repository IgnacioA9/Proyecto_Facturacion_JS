package una.ac.cr.presentation;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.data.ProductosRepository;
import una.ac.cr.data.ProveedoresProductosRepository;
import una.ac.cr.data.ProveedoresRepository;
import una.ac.cr.logic.*;
import una.ac.cr.security.UserDetailsImp;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class productos {
    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Autowired
    ProductosRepository productosRepository;

    @Autowired
    ProveedoresProductosRepository proveedoresProductosRepository;

    /*Metodos requeridos
     *-Mostrar productosbyProveedor
     *-Buscar producto
     *-Crear producto
     *-Editar producto
     *-Eliminiar producto
     */

    @GetMapping("/cargar")
    public Iterable<Productos> read(@AuthenticationPrincipal UserDetailsImp user){
        Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
        List<Productos> lista = proveedoresProductosRepository.findProductosByProveedorCedula(user.getUsername());
        //List<Productos> lista = (List<Productos>) productosRepository.findAll();
        for (Productos producto:lista){
            producto.setAlmacenaByNumeroid(null);
            producto.setContienesByNumeroid(null);
        }
        return lista;
    }

    @GetMapping("/search/{codigo}")
    public List<Productos> searchByCodigo(@RequestParam String codigo) {
        try {
            List<Productos> productos = productosRepository.buscarPorNombre(codigo);
            if (!productos.isEmpty()) {
                return productos;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron productos con ese código");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al buscar los productos", e);
        }
    }

    @PostMapping("/create")
    public void create(@AuthenticationPrincipal UserDetailsImp user ,@RequestBody Productos producto){
        try{
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            Productos p = productosRepository.obtenerProducto(producto.getCodigo());
            if(p == null && producto.getNumeroid() == 0){
                p = productosRepository.save(producto);
                Almacena almacena = new Almacena();
                almacena.setNumeroprod(p.getNumeroid());
                almacena.setProveedoresByNumeroprovee(pr);
                proveedoresProductosRepository.save(almacena);
            } else if (producto.getNumeroid() > 0 && p != null) {
                productosRepository.save(producto);
            }else {
                throw  new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    //@AuthenticationPrincipal UserDetailsImp user,
    @PostMapping("/edit")
    public void edit(@AuthenticationPrincipal UserDetailsImp user,@RequestBody Productos producto) {
        try {
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            Productos p = proveedoresProductosRepository.searchProductoByProveedorAndCodigo(user.getUsername(),producto.getCodigo());
            if (p != null) {
                p.setNombre(producto.getNombre());
                p.setPrecio(producto.getPrecio());
                productosRepository.save(p);

                Almacena almacena = new Almacena();
                almacena.setNumeroprod(p.getNumeroid());
                almacena.setProveedoresByNumeroprovee(pr);
                proveedoresProductosRepository.save(almacena);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al editar el producto", e);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{codigo}")
    public void delete(@PathVariable String codigo) {
        try {
            Productos p = productosRepository.obtenerProducto(codigo);
            if (p != null) {
                // Eliminar las relaciones asociadas al producto
                proveedoresProductosRepository.deleteByNumeroprod(p.getNumeroid());

                // Eliminar el producto
                productosRepository.delete(p);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el producto", e);
        }
    }
}