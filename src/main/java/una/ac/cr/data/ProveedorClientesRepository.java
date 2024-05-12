package una.ac.cr.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Clientes;
import una.ac.cr.logic.Posee;

import java.util.List;

@Repository
public interface ProveedorClientesRepository extends CrudRepository<Posee, String> {
    @Query("select c from Proveedores pO, Posee pE, Clientes c where " +
            "pO.cedula=pE.proveedoresByNumeroprovee.cedula" +
            " and pE.clientesByNumeroclien.numeroid=c.numeroid and" +
            " pO.cedula=?1")
    List<Clientes> searchByProveedor(String cedula);

    @Query("select c from Proveedores pO, Posee pE, Clientes c where " +
            "pO.cedula=pE.proveedoresByNumeroprovee.cedula" +
            " and pE.clientesByNumeroclien.numeroid=c.numeroid and" +
            " pO.cedula=?1 and c.nombre like %?2%")
    List<Clientes> searchByProveedorANDName(String pCedula, String cNombre);

    @Query("select c from Proveedores pO, Posee pE, Clientes c where " +
            "pO.cedula=pE.proveedoresByNumeroprovee.cedula" +
            " and pE.clientesByNumeroclien.numeroid=c.numeroid and" +
            " pO.cedula=?1 and c.cedula=?2")
    Clientes searchByProveedorAndCedula(String idProveedor, String cedula);
    @Query("select p from Posee p where p.numeroclien=?1")
    Posee poseeSearch(int numero);
}
