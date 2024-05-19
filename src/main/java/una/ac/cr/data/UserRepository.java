package una.ac.cr.data;

import org.springframework.beans.factory.annotation.Autowired;
import una.ac.cr.logic.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import una.ac.cr.logic.Usuarios;

import java.util.ArrayList;
import java.util.List;

@Component("userRepository")
public class UserRepository {

    @Autowired
    private UsuariosRepository usuariosRepository;

    private List<User> list;
    private BCryptPasswordEncoder encoder;

    public UserRepository() {
        list = new ArrayList<>();
        encoder = new BCryptPasswordEncoder();
        list.add(new User("jsanchez","{bcrypt}"+encoder.encode("1"),"ADMIN"));
        list.add(new User("slee","{bcrypt}"+encoder.encode("1"),"PROVEE"));
    }

    public User findById(String id) throws Exception {
        User r = list.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElseThrow(() -> new Exception("User not found"));
        return r.clone();
    }

    public void addUser(String id, String password, String rol) {
        list.add(new User(id, "{bcrypt}" + encoder.encode(password), rol));
    }

    public void readUsuarios() {
        for (Usuarios u : usuariosRepository.usuariosAll()) {
            list.add(new User(u.getIdentificacion(), u.getContrasena(), u.getRol()));
        }
    }
}
