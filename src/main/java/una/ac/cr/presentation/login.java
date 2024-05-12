package una.ac.cr.presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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

/*@RestController
@RequestMapping("/api/login")
public class login {
    @Autowired
    private Service service;

    @PostMapping("/login")
    public Usuarios login(@RequestBody Usuarios form,  HttpServletRequest request) {
        try {
            request.login(form.getIdentificacion(), form.getContrasena());
        } catch (ServletException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        Usuarios user = ((UserDetailsImp) auth.getPrincipal()).getUser();
        return new Usuarios(user.getIdentificacion(), null, user.getRol());
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
        return new Usuarios(user.getUser().getIdentificacion(), null, user.getUser().getRol());
    }
}*/

@RestController
@RequestMapping("/api/login")
public class login {
    @PostMapping("/login")
    public User login(@RequestBody User form, HttpServletRequest request) {
        try {
            request.login(form.getId(), form.getPassword());
        } catch (ServletException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        User user = ((UserDetailsImp) auth.getPrincipal()).getUser();
        return new User(user.getId(), null, user.getRol());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
        }
    }

    @GetMapping("/current-user")
    public User getCurrentUser(@AuthenticationPrincipal UserDetailsImp user) {
        return new User(user.getUser().getId(), null, user.getUser().getRol());
    }
}