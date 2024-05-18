package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Productos;
import java.util.List;

@Repository
public interface ProductosRepository extends CrudRepository<Productos, String> {
    @Query("select p from Productos p where p.codigo=?1")
    Productos obtenerProducto(String codigo);

    @Query("select p from Productos p where p.nombre LIKE %:nombre%")
    List<Productos> buscarPorNombre(String nombre);


    void deleteByCodigo(String codigo);
}
