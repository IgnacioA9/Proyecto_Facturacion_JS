package una.ac.cr.presentation;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import una.ac.cr.data.FacturasRepository;
import una.ac.cr.data.ProductosRepository;
import una.ac.cr.data.ProveedoresProductosRepository;
import una.ac.cr.data.ProveedoresRepository;
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
    FacturasRepository facturasRepository;

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
    public void create(@AuthenticationPrincipal UserDetailsImp user ,@RequestBody Facturas facturas){
        try{
            Proveedores pr = proveedoresRepository.findProveedoresByCedula(user.getUsername());
            Facturas f;
            if(facturas.getNumero() == 0) {
                facturasRepository.save(facturas);
                /*Adquiere adquiere = new Adquiere();
                adquiere.setFacturasByNumerofac(f);
                adquiere.setClientesByNumeroclien(facturas.getAdquiereByNumero().getClientesByNumeroclien());
                Contiene contiene = new Contiene();
                contiene.setNumerofac(f.getNumero());
                contiene.setProductosByNumeroprod();
                contiene.getCantidadproducto();*/
            } else if (facturas.getNumero() > 0) {  //&& f != null
                facturasRepository.save(facturas);
            }else {
                throw  new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }catch (Exception ex){
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }



}
