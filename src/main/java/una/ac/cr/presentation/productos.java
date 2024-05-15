package una.ac.cr.presentation;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.*;
import una.ac.cr.security.UserDetailsImp;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class productos {
    @Autowired
    private Service service;

    /*Metodos requeridos
     *-Mostrar productosbyProveedor
     *-Buscar producto
     *-Crear producto
     *-Editar producto
     *-Eliminiar producto
     */

    @GetMapping
    public List<Productos> read(@AuthenticationPrincipal UserDetailsImp user){
        return service.findProductosByProveedorCedula(user.getUsername());
    }

    @GetMapping("/{codigo}")
    public Productos read(@PathVariable String codigo){
        try{
            return service.buscarProducto(codigo);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public List<Productos> findByName(@AuthenticationPrincipal UserDetailsImp user, @RequestParam String nombre){
        return service.productosSearchByName(user.getUsername(),nombre);
    }

    @PostMapping()
    public void create(@AuthenticationPrincipal UserDetailsImp user,@RequestBody Productos producto){
        try{
            Productos buscarProducto = service.buscarProducto(producto.getCodigo());
            if(buscarProducto == null){
                service.productoscreate(producto);
                buscarProducto = service.buscarProducto(producto.getCodigo());
                Almacena almacena = new Almacena();
                almacena.setNumeroprod(buscarProducto.getNumeroid());
                almacena.setProveedoresByNumeroprovee(service.proveedoresread(user.getUsername()));

                service.almacenacreate(almacena);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/edit")
    public void edit(@AuthenticationPrincipal UserDetailsImp user,@RequestBody Productos producto){
        try{
            Productos buscarProducto = service.buscarProducto(producto.getCodigo());
            buscarProducto.setPrecio(producto.getPrecio());
            buscarProducto.setNombre(producto.getNombre());
            buscarProducto.setCodigo(producto.getCodigo());
            service.productoscreate(buscarProducto);
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{codigo}")
    public void delete(@PathVariable String codigo){
        try{
            Productos buscarProducto = service.buscarProducto(codigo);
            Almacena almacenaread = service.almacenaread(buscarProducto.getNumeroid());
            service.almacenaDelete(almacenaread);
            service.productosdelete(buscarProducto);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}