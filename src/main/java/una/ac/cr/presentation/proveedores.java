package una.ac.cr.presentation;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class proveedores {
    @Autowired
    private Service service;

    /*Metodos requeridos
     *-Mostrar todos los proveedores
     *-Aceptar proveedor
     *-Mostrar todos los proveedores aceptados
     *-Rechazar proveedor
     *-Editar proveedor
     */

    @GetMapping
    public List<Proveedores> read(){
        return service.proveedoresAll();
    }

    @GetMapping("/{cedula}")
    public Proveedores read(@PathVariable String cedula){
        try{
            return service.proveedoresread(cedula);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/aceptar")
    public void aceptar(@RequestBody Proveedores proveedor){
        try{
            Proveedores proveedoresread = service.proveedoresread(proveedor.getCedula());
            proveedoresread.setEstado(true);
            service.proveedorescreate(proveedoresread);
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/aceptados")
    public List<Proveedores> aceptados(@PathVariable String cedula){
        try{
            return service.proveedoresAceptados();

        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/rechazar")
    public void rechazar(@RequestBody Proveedores proveedor){
        try{
            Proveedores proveedoresread = service.proveedoresread(proveedor.getCedula());
            proveedoresread.setEstado(false);
            service.proveedorescreate(proveedoresread);
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    @PostMapping()
    public void edit(@RequestBody Proveedores proveedor) {
        try {
            Proveedores proveedoresread = service.proveedoresread(proveedor.getCedula());
            if (proveedoresread != null) {
                proveedoresread.setNombre(proveedor.getNombre());
                proveedoresread.setCorreo(proveedor.getCorreo());
                proveedoresread.setTelefono(proveedor.getTelefono());
                service.proveedorescreate(proveedoresread);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
