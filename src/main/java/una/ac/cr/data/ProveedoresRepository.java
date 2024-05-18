package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Productos;
import una.ac.cr.logic.Proveedores;

import java.util.List;

@Repository
public interface ProveedoresRepository extends CrudRepository<Proveedores, String> {
    @Query("select p from Proveedores p where p.cedula=?1")
    Proveedores findProveedoresByCedula(String cedula);

    @Query("select p from Proveedores p where p.cedula like %?1% and p.estado=false")
    List<Proveedores> proveedoresAll(String input);
    // Método para obtener todos los proveedores con estado false
    @Query("select p from Proveedores p where p.estado=false")
    List<Proveedores> findAllByEstadoFalse();

    // Método para obtener todos los proveedores con estado true
    @Query("select p from Proveedores p where p.estado=true")
    List<Proveedores> findAllByEstadoTrue();
}
