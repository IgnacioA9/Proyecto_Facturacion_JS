package una.ac.cr.data;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import una.ac.cr.logic.Adquiere;
import una.ac.cr.logic.Clientes;

@Repository
public interface ClienteFacturaRepository extends CrudRepository<Adquiere, String> {
    @Query("select a from Facturas f, Adquiere a where f.numero=a.numerofac and f.numero=?1")
    Adquiere seleccionarFacturaPorCliente(int numero);
    @Query("select c from Adquiere a, Clientes c " +
            "where a.clientesByNumeroclien.numeroid=c.numeroid and a.numerofac=?1")
    Clientes seleccionaClientesPorFactura(int numero);
}
