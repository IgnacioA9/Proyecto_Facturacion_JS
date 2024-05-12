package una.ac.cr.logic;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class PK implements Serializable {
    @Column(name = "numeroprod")

    @Id

    private int numeroprod;
    @Column(name = "numerofac")

    @Id

    private int numerofac;

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
        PK that = (PK) o;
        return numeroprod == that.numeroprod && numerofac == that.numerofac;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroprod, numerofac);
    }
}
