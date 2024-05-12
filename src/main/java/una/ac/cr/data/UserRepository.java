package una.ac.cr.data;

import una.ac.cr.logic.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userRepository")
public class UserRepository {
    List<User> list;

    public User findById(String id) throws Exception{
        User r = list.stream()
                .filter( e-> e.getId().equals(id))
                .findFirst().get();
        return r.clone();
    }

    public UserRepository() {
        list = new ArrayList<User>();
        var encoder = new BCryptPasswordEncoder();
        list.add(new User("jsanchez","{bcrypt}"+encoder.encode("1"),"ADM"));
        list.add(new User("slee","{bcrypt}"+encoder.encode("1"),"CLI"));
    }

}
