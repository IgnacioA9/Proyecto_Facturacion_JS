package una.ac.cr.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Posee {
    @Id
    @Column(name = "numeroclien")
    private int numeroclien;
    @ManyToOne
    @JoinColumn(name = "numeroprovee", referencedColumnName = "numeroid")
    private Proveedores proveedoresByNumeroprovee;
    @OneToOne
    @JoinColumn(name = "numeroclien", referencedColumnName = "numeroid", nullable = false)
    private Clientes clientesByNumeroclien;

    public int getNumeroclien() {
        return numeroclien;
    }

    public void setNumeroclien(int numeroclien) {
        this.numeroclien = numeroclien;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posee posee = (Posee) o;
        return numeroclien == posee.numeroclien;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroclien);
    }

    public Proveedores getProveedoresByNumeroprovee() {
        return proveedoresByNumeroprovee;
    }

    public void setProveedoresByNumeroprovee(Proveedores proveedoresByNumeroprovee) {
        this.proveedoresByNumeroprovee = proveedoresByNumeroprovee;
    }

    public Clientes getClientesByNumeroclien() {
        return clientesByNumeroclien;
    }

    public void setClientesByNumeroclien(Clientes clientesByNumeroclien) {
        this.clientesByNumeroclien = clientesByNumeroclien;
    }
}
