package una.ac.cr.presentation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.data.*;
import una.ac.cr.logic.*;
import una.ac.cr.security.UserDetailsImp;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class clientes {
    @Autowired
    private Service service;

    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Autowired
    ClientesRepository clientesRepository;

    @Autowired
    ProveedorClientesRepository proveedorClientesRepository;

    /*Metodos requeridos
    *-Mostrar clientesbyProveedor
    *-Buscar cliente
    *-Crear cliente
    *-Editar cliente
    *-Eliminiar cliente
    */

    @GetMapping("/cargar")
    public Iterable<Clientes> read(@AuthenticationPrincipal UserDetailsImp user){
        //Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
        //List<Clientes> lista = proveedorClientesRepository.searchByProveedor("slee");
        List<Clientes> lista = (List<Clientes>) clientesRepository.findAll();
        for (Clientes cliente:lista){
            cliente.setAdquieresByNumeroid(null);
            cliente.setPoseeByNumeroid(null);
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
    public Iterable<Clientes> findByName(@RequestParam String nombre){
        //Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
        //List<Clientes> lista = proveedorClientesRepository.searchByProveedor(pr.getCedula(),nombre);
        List<Clientes> lista = clientesRepository.clientesAll();
        for (Clientes cliente : lista){
            cliente.setAdquieresByNumeroid(null);
            cliente.setPoseeByNumeroid(null);
        }
        return lista;
    }

    @PostMapping("/create")
    public void create(@RequestBody Clientes cliente){
        try{
            //Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
            Clientes c = clientesRepository.clientesread(cliente.getCedula());
            if(c == null){
                c = clientesRepository.save(cliente);
                Posee posee = new Posee();
                posee.setNumeroclien(c.getNumeroid());
                //posee.setProveedoresByNumeroprovee(pr);
                proveedorClientesRepository.save(posee);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    //@AuthenticationPrincipal UserDetailsImp user,
    @PostMapping("/edit/{codigo}")
    public Clientes  edit (@PathVariable String codigo){
        try{
            Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
            Clientes c = proveedorClientesRepository.searchByProveedorAndCedula(pr.getCedula(),codigo);
            c.setAdquieresByNumeroid(null);
            c.setPoseeByNumeroid(null);

            return c;
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{codigo}")
    public void delete(@PathVariable String codigo){
        try{
            Proveedores pr = proveedoresRepository.findProveedoresByCedula("slee");
            Clientes c = proveedorClientesRepository.searchByProveedorAndCedula(pr.getCedula(),codigo);
            if(c == null && c.getNumeroid()==0){
                Posee posee = new Posee();
                posee.setNumeroclien(c.getNumeroid());
                //posee.setProveedoresByNumeroprovee(pr);
                proveedorClientesRepository.delete(posee);
                clientesRepository.delete(c);
            }
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
