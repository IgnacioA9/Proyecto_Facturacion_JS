package una.ac.cr.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.data.UserRepository;
import una.ac.cr.logic.*;
import una.ac.cr.security.UserDetailsImp;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class usuarios {
    @Autowired
    private Service service;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<Usuarios> read(){
        return service.findAll();
    }
    @PostMapping()
    public void create(@RequestBody Usuarios usuarios) {
        try {
            // Lee un usuario existente por identificación y contraseña
            Usuarios usuarioRead = service.usuariosRead(usuarios.getIdentificacion(), usuarios.getContrasena());
            User u = new User();
            // Si el usuario no existe, lo crea
            if (usuarioRead == null) {
                service.usuarioscreate(usuarios);
                userRepository.addUser(usuarios.getIdentificacion(),usuarios.getContrasena(),usuarios.getRol());
                if ("PROVEE".equals(usuarios.getRol())) {
                    Proveedores proveedor = Proveedores.builder()
                            .cedula(usuarios.getIdentificacion())
                            .nombre("")
                            .correo("")
                            .telefono("")
                            .estado(false)
                            .build();
                    service.proveedorescreate(proveedor);
                }
            }
        } catch (Exception ex) {
            // Lanza una excepción HTTP 409 en caso de error
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

}
