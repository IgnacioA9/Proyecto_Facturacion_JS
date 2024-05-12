package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Tiene;

@Repository
public interface FacturasProveedoresRepository extends CrudRepository<Tiene, String> {

    @Query("select p from Tiene p where p.numerofac=?1")
    Tiene tieneSearch(int numero);

}
