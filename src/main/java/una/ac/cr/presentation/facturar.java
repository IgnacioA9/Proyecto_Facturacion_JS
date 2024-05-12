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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/api/facturar")
public class facturar {
    @Autowired
    private Service service;

    /*Metodos requeridos
     *-Mostrar info*
     *-Buscar cliente
     *-Buscar producto
     *-Crear factura (guardar factura)
     *-Eliminar producto
     */

    @GetMapping
    public List<Productos> read(@AuthenticationPrincipal UserDetailsImp user){
        return service.productossearchbyproveedor(user.getUsername());
    }

    @GetMapping("/{numero}")
    public Facturas read(@PathVariable Integer numero){
        try{
            return service.facturassearch(numero);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cedula}")
    public Clientes findClientesByProveedor(@AuthenticationPrincipal UserDetailsImp user, @RequestParam String cedula){
        return service.clientesSearchByCedula(user.getUsername(),cedula);
    }

    @GetMapping("/{codigo}")
    public Productos findProductosByProveedor(@AuthenticationPrincipal UserDetailsImp user, @RequestParam String codigo){
        return service.productosread(user.getUsername(),codigo);
    }


    // Preguntar parametro para hacer esa busqueda
    @GetMapping("/search")
    public List<Facturas> findByProveedor(@RequestParam String cedula){
        return service.facturassearchbyproveedor(cedula);
    }

    @PostMapping()
    public void create(@RequestBody Facturas facturas){
        try{
            service.facturasCreate(facturas);
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
