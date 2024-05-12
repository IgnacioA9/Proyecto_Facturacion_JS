package una.ac.cr.data;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.logic.Contiene;
import una.ac.cr.logic.Productos;

import java.util.List;

@Repository
public interface AllRepository extends CrudRepository<Contiene, String> {
    @Query("select p from Facturas f, Contiene c, Productos p " +
            "where f.numero=c.numerofac and c.numeroprod=p.numeroid and f.numero=?1")
    List<Productos> productosporfactura(int numero);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Contiene (numeroprod, numerofac, cantidadproducto) VALUES (:numeroprod, :numerofac, :cantidadproducto)", nativeQuery = true)
    void createContiene(@Param("numeroprod") int numeroprod, @Param("numerofac") int numerofac, @Param("cantidadproducto") int cantidadproducto);

    @Query("select c from Contiene c where c.numeroprod=?1 and c.numerofac=?2")
    Contiene readContiene(int numeropro, int numerofac);
}
