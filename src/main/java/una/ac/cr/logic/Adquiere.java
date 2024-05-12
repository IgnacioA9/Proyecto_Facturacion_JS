package una.ac.cr.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Adquiere {

    @Id
    @Column(name = "numerofac")
    private int numerofac;
    @ManyToOne
    @JoinColumn(name = "numeroclien", referencedColumnName = "numeroid")
    private Clientes clientesByNumeroclien;
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
        Adquiere adquiere = (Adquiere) o;
        return numerofac == adquiere.numerofac;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerofac);
    }

    public Clientes getClientesByNumeroclien() {
        return clientesByNumeroclien;
    }

    public void setClientesByNumeroclien(Clientes clientesByNumeroclien) {
        this.clientesByNumeroclien = clientesByNumeroclien;
    }

    public Facturas getFacturasByNumerofac() {
        return facturasByNumerofac;
    }

    public void setFacturasByNumerofac(Facturas facturasByNumerofac) {
        this.facturasByNumerofac = facturasByNumerofac;
    }
}
