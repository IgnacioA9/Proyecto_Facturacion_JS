package una.ac.cr.logic;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(PK.class)
public class Contiene implements Serializable {

    @Id
    @Column(name = "numeroprod")
    private int numeroprod;
    @Id
    @Column(name = "numerofac")
    private int numerofac;
    @ManyToOne
    @JoinColumn(name = "numeroprod", referencedColumnName = "numeroid", nullable = false)
    private Productos productosByNumeroprod;
    @ManyToOne
    @JoinColumn(name = "numerofac", referencedColumnName = "numero", nullable = false)
    private Facturas facturasByNumerofac;
    @Basic
    @Column(name = "cantidadproducto")
    private Integer cantidadproducto;

    public int getNumeroprod() {
        return numeroprod;
    }

    public void setNumeroprod(int numeroprod) {
        this.numeroprod = numeroprod;
    }

    public int getNumerofac() {
        return numerofac;
    }

    public void setNumerofac(int numerofac) {
        this.numerofac = numerofac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contiene contiene = (Contiene) o;
        return numeroprod == contiene.numeroprod && numerofac == contiene.numerofac;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroprod, numerofac);
    }

    public Productos getProductosByNumeroprod() {
        return productosByNumeroprod;
    }

    public void setProductosByNumeroprod(Productos productosByNumeroprod) {
        this.productosByNumeroprod = productosByNumeroprod;
    }

    public Facturas getFacturasByNumerofac() {
        return facturasByNumerofac;
    }

    public void setFacturasByNumerofac(Facturas facturasByNumerofac) {
        this.facturasByNumerofac = facturasByNumerofac;
    }

    public Integer getCantidadproducto() {
        return cantidadproducto;
    }

    public void setCantidadproducto(Integer cantidadproducto) {
        this.cantidadproducto = cantidadproducto;
    }
}
