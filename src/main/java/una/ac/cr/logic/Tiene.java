package una.ac.cr.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
@Entity
public class Tiene {

    @Id
    @Column(name = "numerofac")
    private int numerofac;
    @ManyToOne
    @JoinColumn(name = "numeroprovee", referencedColumnName = "numeroid")
    private Proveedores proveedoresByNumeroprovee;
    @OneToOne
    @JoinColumn(name = "numerofac", referencedColumnName = "numero", nullable = false)
    private Facturas facturasByNumerofac;

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
        Tiene tiene = (Tiene) o;
        return numerofac == tiene.numerofac;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerofac);
    }

    public Proveedores getProveedoresByNumeroprovee() {
        return proveedoresByNumeroprovee;
    }

    public void setProveedoresByNumeroprovee(Proveedores proveedoresByNumeroprovee) {
        this.proveedoresByNumeroprovee = proveedoresByNumeroprovee;
    }

    public Facturas getFacturasByNumerofac() {
        return facturasByNumerofac;
    }

    public void setFacturasByNumerofac(Facturas facturasByNumerofac) {
        this.facturasByNumerofac = facturasByNumerofac;
    }
}
