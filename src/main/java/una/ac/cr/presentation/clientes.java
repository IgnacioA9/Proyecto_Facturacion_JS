package una.ac.cr.presentation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.Clientes;
import una.ac.cr.logic.Posee;
import una.ac.cr.logic.Service;
import una.ac.cr.security.UserDetailsImp;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class clientes {
    @Autowired
    private Service service;

    /*Metodos requeridos
    *-Mostrar clientesbyProveedor
    *-Buscar cliente
    *-Crear cliente
    *-Editar cliente
    *-Eliminiar cliente
    */

    @GetMapping
    public List<Clientes> read(@AuthenticationPrincipal UserDetailsImp user){
        return service.clientessearchbyproveedor(user.getUsername());
    }

    @GetMapping("/{cedula}")
    public Clientes read(@AuthenticationPrincipal UserDetailsImp user,@PathVariable String cedula){
        try{
            return service.clientesSearchByCedula(user.getUsername(),cedula);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public List<Clientes> findByName(@AuthenticationPrincipal UserDetailsImp user, @RequestParam String nombre){
        return service.clientessearchbyname(user.getUsername(),nombre);
    }

    @PostMapping()
    public void create(@AuthenticationPrincipal UserDetailsImp user,@RequestBody Clientes cliente){
        try{
            Clientes clientesread = service.clientesread(cliente.getCedula());
            if(clientesread == null){
                service.clientescreate(cliente);
                clientesread = service.clientesread(cliente.getCedula());
                Posee posee = new Posee();
                posee.setNumeroclien(clientesread.getNumeroid());
                posee.setProveedoresByNumeroprovee(service.proveedoresread(user.getUsername()));
                service.poseecreate(posee);
            }
            else{
                clientesread.setCorreo(cliente.getCorreo());
                clientesread.setNombre(cliente.getNombre());
                clientesread.setTelefono(cliente.getTelefono());
                service.clientescreate(clientesread);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/edit")
    public void edit(@AuthenticationPrincipal UserDetailsImp user,@RequestBody Clientes cliente){
        try{
            Clientes clientesread = service.clientesread(cliente.getCedula());
            clientesread.setCorreo(cliente.getCorreo());
            clientesread.setNombre(cliente.getNombre());
            clientesread.setTelefono(cliente.getTelefono());
            service.clientescreate(clientesread);
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{cedula}")
    public void delete(@PathVariable String cedula){
        try{
            Clientes clientesread = service.clientesread(cedula);
            Posee poseeread = service.poseeread(clientesread.getNumeroid());
            service.poseeDelete(poseeread);
            service.clientesdelete(clientesread);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }}
