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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class productos {
    @Autowired
    private Service service;

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
        //Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
        //List<Productos> lista = proveedoresProductosRepository.findProductosByProveedorCedula("slee");
        List<Productos> lista = (List<Productos>) productosRepository.findAll();
        for (Productos producto:lista){
            producto.setAlmacenaByNumeroid(null);
            producto.setContienesByNumeroid(null);
        }
        return lista;
    }

    /*@GetMapping("/{codigo}")
    public Productos read(@PathVariable String codigo){
        try{
            return service.buscarProducto(codigo);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }*/

    @GetMapping("/search")
    public Iterable<Productos> findByName(@RequestParam String nombre){
        Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
        List<Productos> lista = proveedoresProductosRepository.searchByProveedorAndName(pr.getCedula(),nombre);
        for (Productos producto : lista){
            producto.setAlmacenaByNumeroid(null);
            producto.setContienesByNumeroid(null);
        }
        return lista;
    }

    @PostMapping("/create")
    public void create(@RequestBody Productos producto){
        try{
            //Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
            Productos p = productosRepository.obtenerProducto(producto.getCodigo());
            if(p == null){
                p = productosRepository.save(producto);
                Almacena almacena = new Almacena();
                almacena.setNumeroprod(p.getNumeroid());
                //almacena.setProveedoresByNumeroprovee(pr);
                proveedoresProductosRepository.save(almacena);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    //@AuthenticationPrincipal UserDetailsImp user,
    @PostMapping("/edit/{codigo}")
    public Productos edit (@PathVariable String codigo){
        try{
            Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
            Productos p = proveedoresProductosRepository.searchProductoByProveedorAndCodigo(pr.getCedula(),codigo);
            p.setAlmacenaByNumeroid(null);
            p.setContienesByNumeroid(null);

            return p;
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{codigo}")
    public void delete(@PathVariable String codigo){
        try{
            //Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
                Productos p = proveedoresProductosRepository.searchProductoByProveedorAndCodigo("slee",codigo);
            if(p == null && p.getNumeroid()==0){
                Almacena almacena = new Almacena();
                almacena.setNumeroprod(p.getNumeroid());
                //almacena.setProveedoresByNumeroprovee(pr);
                proveedoresProductosRepository.delete(almacena);
                productosRepository.delete(p);
            }
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}