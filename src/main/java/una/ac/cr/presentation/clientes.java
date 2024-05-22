package una.ac.cr.presentation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
        Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
        List<Clientes> lista = proveedorClientesRepository.searchByProveedor(user.getUsername());
        for (Clientes cliente:lista){
            cliente.setAdquieresByNumeroid(null);
            cliente.setPoseeByNumeroid(null);
        }
        return lista;
    }

    @GetMapping("/search/{cedula}")
    public Clientes searchByCedula(@RequestParam String cedula) {
        try {
            Clientes clientes = clientesRepository.clientesread(cedula);
            if (clientes != null) {
                return clientes;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron productos con ese código");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al buscar los productos", e);
        }
    }

    @PostMapping("/create")
    public void create(@AuthenticationPrincipal UserDetailsImp user ,@RequestBody Clientes cliente){
        try{
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            Clientes c = clientesRepository.clientesread(cliente.getCedula());
            if(c == null && cliente.getNumeroid() == 0){
                c = clientesRepository.save(cliente);
                Posee posee = new Posee();
                posee.setNumeroclien(c.getNumeroid());
                posee.setProveedoresByNumeroprovee(pr);
                proveedorClientesRepository.save(posee);
            } else if (cliente.getNumeroid() > 0 && c != null) {
                clientesRepository.save(cliente);
            }else {
                throw  new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    //@AuthenticationPrincipal UserDetailsImp user,
    @PostMapping("/edit")
    public void edit(@AuthenticationPrincipal UserDetailsImp user,@RequestBody Clientes cliente) {
        try {
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            Clientes c = proveedorClientesRepository.searchByProveedorAndCedula(user.getUsername(),cliente.getCedula());
            if (c != null) {
                c.setNombre(cliente.getNombre());
                c.setCorreo(cliente.getCorreo());
                c.setTelefono(cliente.getTelefono());
                clientesRepository.save(c);

                Posee posee = new Posee();
                posee.setNumeroclien(c.getNumeroid());
                posee.setProveedoresByNumeroprovee(pr);
                proveedorClientesRepository.save(posee);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al editar el producto", e);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{codigo}")
    public void delete(@AuthenticationPrincipal UserDetailsImp user, @PathVariable String codigo) {
        try {
            Clientes c = proveedorClientesRepository.searchByProveedorAndCedula(user.getUsername(),codigo);
            if (c != null) {
                // Eliminar las relaciones asociadas al producto
                proveedorClientesRepository.deleteByNumeroclien(c.getNumeroid());

                // Eliminar el producto
                clientesRepository.delete(c);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el producto", e);
        }
    }
}
