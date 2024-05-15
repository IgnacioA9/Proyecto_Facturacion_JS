package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Usuarios;

import java.util.List;

@Repository
public interface UsuariosRepository extends CrudRepository<Usuarios, String> {
    @Query("select u from Usuarios u where u.identificacion=?1 and u.contrasena=?2")
    Usuarios usuariosread(String identificacion, String contrasena);

    @Query("select u from Usuarios u where u.identificacion=?1")
    Usuarios findUsuariosByIdentificacion(String identificacion);

}
