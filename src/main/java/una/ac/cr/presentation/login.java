package una.ac.cr.presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import una.ac.cr.data.ProveedoresRepository;
import una.ac.cr.data.UsuariosRepository;
import una.ac.cr.logic.Proveedores;
import una.ac.cr.logic.User;
import una.ac.cr.logic.Usuarios;
import una.ac.cr.logic.Service;
import una.ac.cr.security.UserDetailsImp;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@RestController
@RequestMapping("/api/login")
public class login {
    @Autowired
    private Service service;

    @Autowired
    UsuariosRepository usuariosRepository;

    @Autowired
    ProveedoresRepository proveedoresRepository;

    @PostMapping("/login")
    public Usuarios login(@RequestBody Usuarios form, HttpServletRequest request) {
        System.out.println("Identificación: " + form.getIdentificacion());
        System.out.println("Contraseña: " + form.getContrasena());
        String rolU;
        try {
            rolU = usuariosRepository.findRolByIdentificacion(form.getIdentificacion());
            Proveedores proveedor = proveedoresRepository.findProveedoresByCedula(form.getIdentificacion());

            if (Objects.equals(rolU, "PROVEE")) {
                if (proveedor == null || !proveedor.getEstado()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Proveedor no autorizado");
                }
            }
            request.login(form.getIdentificacion(), form.getContrasena());
        } catch (ServletException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        Usuarios user = ((UserDetailsImp) auth.getPrincipal()).getUser();
        user.setIdentificacion(form.getIdentificacion());
        user.setContrasena(null);
        user.setRol(rolU);
        return user;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
        }
    }

    @GetMapping("/current-user")
    public Usuarios getCurrentUser(@AuthenticationPrincipal UserDetailsImp user) {
        Usuarios users = new Usuarios();
        users.setIdentificacion(user.getUsername());
        users.setContrasena(null);
        users.setRol(user.getUser().getRol());
        return users;
    }
}