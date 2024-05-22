package una.ac.cr.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.logic.*;

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
    public void create(@RequestBody Usuarios usuarios) {
        try {
            // Lee un usuario existente por identificación y contraseña
            Usuarios usuarioRead = service.usuariosRead(usuarios.getIdentificacion(), usuarios.getContrasena());
            User u = new User();
            // Si el usuario no existe, lo crea
            if (usuarioRead == null) {
                var encoder = new BCryptPasswordEncoder();
                usuarios.setContrasena("{bcrypt}"+encoder.encode(usuarios.getContrasena()));
                service.usuarioscreate(usuarios);
                //userRepository.addUser(usuarios.getIdentificacion(),usuarios.getContrasena(),usuarios.getRol());
                if (usuarios.getRol().equals("PROVEE")) {
                    Proveedores proveedor = new Proveedores();
                    proveedor.setCedula(usuarios.getIdentificacion());
                    proveedor.setNombre("");
                    proveedor.setCorreo("");
                    proveedor.setTelefono("");
                    proveedor.setEstado(false);
                    service.proveedorescreate(proveedor);
                }
                if (usuarios.getRol().equals("ADMIN")) {
                    Administradores admin = new Administradores();
                    admin.setCedula(usuarios.getIdentificacion());
                    admin.setNombre("");
                    service.administradorescreate(admin);
                }
            }
        } catch (Exception ex) {
            // Lanza una excepción HTTP 409 en caso de error
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

}
