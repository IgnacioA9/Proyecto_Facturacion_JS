package una.ac.cr.presentation;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.data.*;
import una.ac.cr.logic.*;
import una.ac.cr.security.UserDetailsImp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class facturas {
    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Autowired
    ClientesRepository clientesRepository;

    @Autowired
    ProveedorClientesRepository proveedorClientesRepository;

    @Autowired
    ProveedoresProductosRepository proveedoresProductosRepository;

    @Autowired
    FacturasRepository facturasRepository;

    @Autowired
    FacturasProveedoresRepository facturasProveedoresRepository;

    @Autowired
    ClienteFacturaRepository clienteFacturaRepository;

    @Autowired
    AllRepository productosFacturaRepository;

    /*Metodos requeridos
     *-Mostrar facturas
     *-Crear factura
     */

    @GetMapping("/cargar")
    public Iterable<Facturas> read(@AuthenticationPrincipal UserDetailsImp user){
        Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
        List<Facturas> lista = facturasRepository.facturasBuscarPorProveedor(user.getUsername());
        //List<Facturas> lista = (List<Facturas>) facturasRepository.findAll();
        for (Facturas factura:lista){
            factura.setAdquiereByNumero(null);
            factura.setContienesByNumero(null);
            factura.setTieneByNumero(null);
        }
        return lista;
    }


    @PostMapping("/create")
    public void create(@AuthenticationPrincipal UserDetailsImp user, @RequestBody FacturaClienteDTO facturaClienteDTO) {
        try {
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            pr.setAlmacenasByNumeroid(null);
            pr.setPoseesByNumeroid(null);
            pr.setTienesByNumeroid(null);

            Facturas factura = facturaClienteDTO.getFactura();
            Clientes clienteF = facturaClienteDTO.getCliente();
            List<ContieneDTO> contieneDTOList = facturaClienteDTO.getContiene();

            Facturas fa = new Facturas();
            fa.setCantidad(factura.getCantidad());
            fa.setMonto(factura.getMonto());
            fa.setFecha(factura.getFecha());

            Facturas F = facturasRepository.save(fa);

            Tiene tiene = new Tiene();
            tiene.setNumerofac(F.getNumero());
            tiene.setProveedoresByNumeroprovee(pr);
            facturasProveedoresRepository.save(tiene);

            Clientes c = clientesRepository.save(clienteF);
            c.setAdquieresByNumeroid(null);
            c.setPoseeByNumeroid(null);

            Adquiere adquiere = new Adquiere();
            adquiere.setNumerofac(F.getNumero());
            adquiere.setClientesByNumeroclien(c);

            clienteFacturaRepository.save(adquiere);

            /*for (ContieneDTO contieneDTO : contieneDTOList) {
                Contiene contiene = new Contiene();
                Productos aux = proveedoresProductosRepository.searchProductoByProveedorAndCodigo(pr.getCedula(),contieneDTO.getNumeroprod());
                contiene.setNumeroprod(aux.getNumeroid());
                contiene.setNumerofac(F.getNumero());
                contiene.setCantidadproducto(contieneDTO.getCantidadproducto());
                productosFacturaRepository.save(contiene);
            }*/


        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }



}
