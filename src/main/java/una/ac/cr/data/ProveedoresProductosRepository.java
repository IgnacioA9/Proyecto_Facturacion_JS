package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Almacena;
import una.ac.cr.logic.Productos;

import java.util.List;

@Repository
public interface ProveedoresProductosRepository extends CrudRepository<Almacena, String> {
    @Query("select p from Proveedores pO, Almacena aM, Productos p where " +
            "pO.numeroid=aM.proveedoresByNumeroprovee.numeroid and " +
            "aM.numeroprod=p.numeroid and pO.cedula=?1 and p.codigo=?2")
    Productos searchProductoByProveedorAndCodigo(String idProveedor, String codigo);

    @Query("select c from Proveedores pO, Almacena pE, Productos c where " +
            "pO.cedula=pE.proveedoresByNumeroprovee.cedula" +
            " and pE.productosByNumeroprod.numeroid=c.numeroid and" +
            " pO.cedula=?1 and c.nombre like %?2%")
    List<Productos> searchByProveedorAndName(String pCedula, String pNombre);
    @Query("select p from Productos p join Almacena a on p.numeroid = a.productosByNumeroprod.numeroid " +
            "join Proveedores pr on pr.numeroid = a.proveedoresByNumeroprovee.numeroid " +
            "where pr.cedula = ?1")
    List<Productos> findProductosByProveedorCedula(String cedula);

    @Query("select p from Almacena p where p.numeroprod=?1")
    Almacena almacenaSearch(int numero);

    void deleteByNumeroprod(int numeroprod);
}

