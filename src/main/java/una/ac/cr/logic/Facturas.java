package una.ac.cr.logic;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Facturas {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "numero")
    private int numero;
    @Basic
    @Column(name = "cantidad")
    private Integer cantidad;
    @Basic
    @Column(name = "monto")
    private Double monto;
    @Basic
    @Column(name = "fecha")
    private Date fecha;
    @OneToOne(mappedBy = "facturasByNumerofac")
    private Adquiere adquiereByNumero;
    @OneToMany(mappedBy = "facturasByNumerofac")
    private Collection<Contiene> contienesByNumero;
    @OneToOne(mappedBy = "facturasByNumerofac")
    private Tiene tieneByNumero;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facturas facturas = (Facturas) o;
        return numero == facturas.numero && Objects.equals(cantidad, facturas.cantidad) && Objects.equals(monto, facturas.monto) && Objects.equals(fecha, facturas.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, cantidad, monto, fecha);
    }

    public Adquiere getAdquiereByNumero() {
        return adquiereByNumero;
    }

    public void setAdquiereByNumero(Adquiere adquiereByNumero) {
        this.adquiereByNumero = adquiereByNumero;
    }

    public Collection<Contiene> getContienesByNumero() {
        return contienesByNumero;
    }

    public void setContienesByNumero(Collection<Contiene> contienesByNumero) {
        this.contienesByNumero = contienesByNumero;
    }

    public Tiene getTieneByNumero() {
        return tieneByNumero;
    }

    public void setTieneByNumero(Tiene tieneByNumero) {
        this.tieneByNumero = tieneByNumero;
    }
}
