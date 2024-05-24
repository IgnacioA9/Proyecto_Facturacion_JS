package una.ac.cr.presentation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.data.*;
import una.ac.cr.logic.*;
import una.ac.cr.logic.clasesFacturas.ContieneDTO;
import una.ac.cr.logic.clasesFacturas.FacturaClienteDTO;
import una.ac.cr.security.UserDetailsImp;

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
    ProductosRepository productosRepository;

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
    public Iterable<FacturaClienteDTO> read(@AuthenticationPrincipal UserDetailsImp user){
        Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
        List<FacturaClienteDTO> lista = new ArrayList<>();
        List<Facturas> f = facturasRepository.facturasBuscarPorProveedor(user.getUsername());
        for (Facturas factura:f){
            factura.setAdquiereByNumero(null);
            factura.setContienesByNumero(null);
            factura.setTieneByNumero(null);

            Clientes c = clienteFacturaRepository.seleccionaClientesPorFactura(factura.getNumero());
            c.setAdquieresByNumeroid(null);
            c.setPoseeByNumeroid(null);

            FacturaClienteDTO aux = new FacturaClienteDTO();
            aux.setFactura(factura);
            aux.setCliente(c);

            List<ContieneDTO> contieneDTOList = new ArrayList<>();
            List<Productos> productos = productosFacturaRepository.productosporfactura(factura.getNumero());
            for (Productos p : productos) {
                p.setAlmacenaByNumeroid(null);
                p.setContienesByNumeroid(null);

                ContieneDTO contieneDTOaux = new ContieneDTO();
                contieneDTOaux.setCodigo(p.getCodigo());
                contieneDTOaux.setCantidadproducto(productosFacturaRepository.findCantidadProductoByNumeroprodAndNumerofac(p.getNumeroid(),factura.getNumero()));
                contieneDTOList.add(contieneDTOaux);
            }
            aux.setContiene(contieneDTOList);
            lista.add(aux);
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

            for (ContieneDTO contieneDTO : contieneDTOList) {
                Productos aux = productosRepository.obtenerProducto(contieneDTO.getCodigo());
                aux.setAlmacenaByNumeroid(null);
                aux.setContienesByNumeroid(null);
                productosFacturaRepository.createContiene(aux.getNumeroid(),F.getNumero(),contieneDTO.getCantidadproducto());
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
}
