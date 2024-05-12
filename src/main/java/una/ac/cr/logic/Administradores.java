package una.ac.cr.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Administradores {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "numeroid")
    private int numeroid;
    @Basic
    @Column(name = "cedula")
    private String cedula;
    @Basic
    @Column(name = "nombre")
    private String nombre;

    public int getNumeroid() {
        return numeroid;
    }

    public void setNumeroid(int numeroid) {
        this.numeroid = numeroid;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administradores that = (Administradores) o;
        return numeroid == that.numeroid && Objects.equals(cedula, that.cedula) && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroid, cedula, nombre);
    }
}
