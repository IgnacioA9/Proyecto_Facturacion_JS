package una.ac.cr.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.Administradores;
import una.ac.cr.logic.Service;
import una.ac.cr.security.UserDetailsImp;

import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class administradores {

    @Autowired
    private Service service;

    /*Metodos requeridos
     *-Mostrar administradores
     *-Editar administradores
     */

    @GetMapping
    public Administradores read(@AuthenticationPrincipal UserDetailsImp user){
        return service.administradoresread(user.getUsername());
    }

    @PostMapping()
    public void edit(@RequestBody Administradores administrador) {
        try {
            Administradores administradoresread = service.administradoresread(administrador.getCedula());
            if (administradoresread != null) {
                administradoresread.setNumeroid(administrador.getNumeroid());
                administradoresread.setCedula(administrador.getCedula());
                administradoresread.setNombre(administrador.getNombre());
                service.administradorescreate(administradoresread);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
