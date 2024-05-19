package una.ac.cr.logic;

import java.util.List;

public class FacturaClienteDTO {
    private Facturas factura;
    private Clientes cliente;
    private List<ContieneDTO> contiene;

    // Getters y Setters
    public Facturas getFactura() {
        return factura;
    }

    public void setFactura(Facturas factura) {
        this.factura = factura;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public List<ContieneDTO> getContiene() {
        return contiene;
    }

    public void setContiene(List<ContieneDTO> contiene) {
        this.contiene = contiene;
    }
}

