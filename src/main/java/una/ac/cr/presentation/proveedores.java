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
@RequestMapping("/api/proveedores")
public class proveedores {
    @Autowired
    private Service service;

    @Autowired
    ProveedoresRepository proveedoresRepository;

    /*Metodos requeridos
     *-Mostrar todos los proveedores
     *-Aceptar proveedor
     *-Mostrar todos los proveedores aceptados
     *-Rechazar proveedor
     *-Editar proveedor
     */

    @GetMapping("/cargar/proveedor")
    public Proveedores readProveedorA(@AuthenticationPrincipal UserDetailsImp user){
        Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
        pr.setAlmacenasByNumeroid(null);
        pr.setPoseesByNumeroid(null);
        pr.setTienesByNumeroid(null);
        return pr;
    }

    @GetMapping("/cargar/acepatdos")
    public Iterable<Proveedores> read(){
        List<Proveedores> lista = proveedoresRepository.findAllByEstadoTrue();
        for (Proveedores proveedor:lista){
            proveedor.setAlmacenasByNumeroid(null);
            proveedor.setPoseesByNumeroid(null);
            proveedor.setTienesByNumeroid(null);
        }
        return lista;
    }

    @GetMapping("/cargar/rechazados")
    public Iterable<Proveedores> readRechazados(){
        List<Proveedores> lista = proveedoresRepository.findAllByEstadoFalse();
        for (Proveedores proveedor:lista){
            proveedor.setAlmacenasByNumeroid(null);
            proveedor.setPoseesByNumeroid(null);
            proveedor.setTienesByNumeroid(null);
        }
        return lista;
    }

    @GetMapping("/{cedula}")
    public Proveedores read(@PathVariable String cedula){
        Proveedores pr = proveedoresRepository.findProveedoresByCedula(cedula);
        pr.setAlmacenasByNumeroid(null);
        pr.setPoseesByNumeroid(null);
        pr.setTienesByNumeroid(null);

        return pr;
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

    @PostMapping("/edit")
    public void edit(@AuthenticationPrincipal UserDetailsImp user, @RequestBody Proveedores proveedor) {
        try {
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            if (pr != null) {
                pr.setNombre(proveedor.getNombre());
                pr.setCorreo(proveedor.getCorreo());
                pr.setTelefono(proveedor.getTelefono());
                proveedoresRepository.save(pr);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}



