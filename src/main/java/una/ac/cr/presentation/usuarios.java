package una.ac.cr.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.Clientes;
import una.ac.cr.logic.Posee;
import una.ac.cr.logic.Service;
import una.ac.cr.logic.Usuarios;
import una.ac.cr.security.UserDetailsImp;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class usuarios {
    @Autowired
    private Service service;

    @GetMapping
    public List<Usuarios> read(){
        return service.findAll();
    }
    @PostMapping()
    public void create(@RequestBody Usuarios usuarios){
        try{
            Usuarios usuarioRead = service.usuariosRead(usuarios.getIdentificacion(),usuarios.getContrasena()   );
            if(usuarioRead == null){
                service.usuarioscreate(usuarios);
            }
            service.usuarioscreate(usuarios);
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

}
