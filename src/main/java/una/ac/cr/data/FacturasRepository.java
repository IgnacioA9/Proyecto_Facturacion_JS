package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Facturas;

import java.util.List;

@Repository
public interface FacturasRepository extends CrudRepository<Facturas, String> {
    @Query("select f from Proveedores p, Tiene t, Facturas f " +
            "where p.numeroid=t.proveedoresByNumeroprovee.numeroid " +
            "and t.numerofac=f.numero and p.cedula=?1")
    List<Facturas> facturasBuscarPorProveedor(String cedula);

    @Query("select f from Facturas f where f.numero=?1")
    Facturas facturaSEARCH(int numero);
}
