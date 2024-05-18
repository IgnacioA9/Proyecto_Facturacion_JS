package una.ac.cr.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
public class Almacena {
    @Id
    @Column(name = "numeroprod")
    private int numeroprod;
    @ManyToOne
    @JoinColumn(name = "numeroprovee", referencedColumnName = "numeroid")
    private Proveedores proveedoresByNumeroprovee;
    @OneToOne
    @JoinColumn(name = "numeroprod", referencedColumnName = "numeroid", nullable = false)
    private Productos productosByNumeroprod;

    public int getNumeroprod() {
        return numeroprod;
    }

    public void setNumeroprod(int numeroprod) {
        this.numeroprod = numeroprod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Almacena almacena = (Almacena) o;
        return numeroprod == almacena.numeroprod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroprod);
    }

    public Proveedores getProveedoresByNumeroprovee() {
        return proveedoresByNumeroprovee;
    }

    public void setProveedoresByNumeroprovee(Proveedores proveedoresByNumeroprovee) {
        this.proveedoresByNumeroprovee = proveedoresByNumeroprovee;
    }

    public Productos getProductosByNumeroprod() {
        return productosByNumeroprod;
    }

    public void setProductosByNumeroprod(Productos productosByNumeroprod) {
        this.productosByNumeroprod = productosByNumeroprod;
    }
}
