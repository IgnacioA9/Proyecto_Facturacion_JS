package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Administradores;

@Repository
public interface AdministradoresRepository extends CrudRepository<Administradores, String> {
    @Query("select a from Administradores a where a.cedula=?1")
    Administradores administradoresRead(String cedulaAdmin);
}
