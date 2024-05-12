package una.ac.cr.presentation;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.Facturas;
import una.ac.cr.logic.Productos;
import una.ac.cr.logic.Proveedores;
import una.ac.cr.logic.Service;
import una.ac.cr.security.UserDetailsImp;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class facturas {
    @Autowired
    private Service service;

    /*Metodos requeridos
     *-Mostrar facturas
     */

    @GetMapping
    public List<Facturas> read(@AuthenticationPrincipal UserDetailsImp user){
        return service.facturassearchbyproveedor(user.getUsername());
    }
}
