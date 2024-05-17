package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Clientes;

import java.util.List;

@Repository
public interface ClientesRepository extends CrudRepository<Clientes, String> {
    @Query("select c from Clientes c where c.cedula=?1")
    Clientes clientesread(String cedula);

    @Query("select c from Clientes c")
    List<Clientes> clientesAll();
}
