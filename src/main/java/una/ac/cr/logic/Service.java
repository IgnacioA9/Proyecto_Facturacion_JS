package una.ac.cr.logic;

import una.ac.cr.data.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private ProveedoresRepository proveedoresRepository;
    @Autowired
    private AdministradoresRepository administradoresRepository;
    @Autowired
    private ClientesRepository clientesRepository;
    @Autowired
    private ProveedorClientesRepository proveedorClientesRepository;
    @Autowired
    private ProveedoresProductosRepository proveedoresProductosRepository;
    @Autowired
    private AllRepository allRepository;
    @Autowired
    private FacturasRepository facturasRepository;
    @Autowired
    private FacturasProveedoresRepository facturasProveedoresRepository;
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private ClienteFacturaRepository clienteFacturaRepository;

    //USUARIOS

    public Usuarios usuariosRead(String identificacion, String contresena){
        return usuariosRepository.usuariosread(identificacion, contresena);
    }
    public List<Usuarios> findAll(){
        return (List<Usuarios>) usuariosRepository.findAll();
    }
    public void usuarioscreate(Usuarios usuarios){
        usuariosRepository.save(usuarios);
    }

    public Usuarios usuarioById(String id){
        return usuariosRepository.findUsuariosByIdentificacion(id);
    }

    //PROVEEDORES

    public Proveedores proveedoresread(String identificacion){
        return proveedoresRepository.findProveedoresByCedula(identificacion);
    }
    public void proveedorescreate(Proveedores proveedores){
        proveedoresRepository.save(proveedores);
    }
    public List<Proveedores> proveedoresAll(){
        return proveedoresRepository.proveedoresAll("");
    }
    /*public List<Proveedores> proveedoresAceptados(){
        return proveedoresRepository.proveedoresAcepatados();
    }*/

    //ADMINISTRADORES
    public Administradores administradoresread(String identificacion){
        return administradoresRepository.administradoresRead(identificacion);
    }
    public void administradorescreate(Administradores administradores){
        administradoresRepository.save(administradores);
    }

    //CLIENTES
    public void clientescreate(Clientes cliente){
        clientesRepository.save(cliente);
    }
    public Clientes clientesread(String cedula){
        return clientesRepository.clientesread(cedula);
    }
    public List<Clientes> clientessearchbyproveedor(String idProveedor){
        return proveedorClientesRepository.searchByProveedor(idProveedor);
    }
    public List<Clientes> clientessearchbyname(String idProveedor, String clientename){
        return proveedorClientesRepository.searchByProveedorANDName(idProveedor, clientename);
    }
    public void clientesdelete(Clientes cliente){
        clientesRepository.delete(cliente);
    }
    public Clientes clientesSearchByCedula(String idProveedor, String cedula){
        return proveedorClientesRepository.searchByProveedorAndCedula(idProveedor, cedula);
    }
    public Productos productosread(String idProveedor, String codigoProducto){
        return proveedoresProductosRepository.searchProductoByProveedorAndCodigo(idProveedor, codigoProducto);
    }
    public Clientes clientesPorFactura(int numeroRelacion){
        return clienteFacturaRepository.seleccionaClientesPorFactura(numeroRelacion);
    }

    //FACTURAS
    public void facturasCreate(Facturas facturas){
        facturasRepository.save(facturas);
    }
    public List<Facturas> facturassearchbyproveedor(String cedula){
        return facturasRepository.facturasBuscarPorProveedor(cedula);
    }
    public Facturas facturassearch(int numero){
        return facturasRepository.facturaSEARCH(numero);
    }

    public double calculosTotalFactura(List<Productos> list){
        double precio=0;
        for(int i = 0; i < list.size(); i++){
            precio+=list.get(i).getPrecio();
        }
        return precio;
    }
    public void relacionFacturaProductos(Facturas factura, List<Productos> list){
        for(int i = 0; i < list.size(); i++){
            Productos productos = buscarProducto(list.get(i).getCodigo());
            Facturas facturas = facturassearch(factura.getNumero());
            Contiene contiene = allRepository.readContiene(productos.getNumeroid(), facturas.getNumero());

            if(contiene==null){
                allRepository.createContiene(productos.getNumeroid(),facturas.getNumero(), 0);
            }
        }
    }

    //PRODUCTOS
    public void productoscreate(Productos productos){
        productosRepository.save(productos);
    }
    public List<Productos> productosPorFactura(int numero){
        return allRepository.productosporfactura(numero);
    }
    public List<Productos> productosSearchByName(String idProveedor, String productoname){
        return proveedoresProductosRepository.searchByProveedorAndName(idProveedor, productoname);
    }
    public Productos buscarProducto(String codigo){
        return productosRepository.obtenerProducto(codigo);
    }
    public List<Productos> findProductosByProveedorCedula(String cedula){
        return proveedoresProductosRepository.findProductosByProveedorCedula(cedula);
    }
    public void productosdelete(Productos productos){
        productosRepository.delete(productos);
    }

    //PROVEEDOR-PRODUCTOS
    public void almacenacreate(Almacena almacena){
        proveedoresProductosRepository.save(almacena);
    }
    public Almacena almacenaread(int numero){return proveedoresProductosRepository.almacenaSearch(numero);}
    public void almacenaDelete(Almacena almacena){
        proveedoresProductosRepository.delete(almacena);
    }

    //CLIENTE-FACTURAS
    public void adquiereCreate(Adquiere adquiere){
        clienteFacturaRepository.save(adquiere);
    }
    public Adquiere relacionPorFactura(int numero){
        return clienteFacturaRepository.seleccionarFacturaPorCliente(numero);
    }

    //PROVEEDOR-CLIENTES
    public void poseecreate(Posee posee){
        proveedorClientesRepository.save(posee);
    }
    public Posee poseeread(int numero){
        return proveedorClientesRepository.poseeSearch(numero);
    }
    public void poseeDelete(Posee posee){
        proveedorClientesRepository.delete(posee);
    }

    //FACTURAS-PROVEEDORES
    public void tieneCreate(Tiene tiene){
        facturasProveedoresRepository.save(tiene);
    }

    public Tiene tieneRead(int numero){return facturasProveedoresRepository.tieneSearch(numero);}

}
