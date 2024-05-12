package una.ac.cr.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Clientes {
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
    @Basic
    @Column(name = "correo")
    private String correo;
    @Basic
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(mappedBy = "clientesByNumeroclien")
    private Collection<Adquiere> adquieresByNumeroid;
    @OneToOne(mappedBy = "clientesByNumeroclien")
    private Posee poseeByNumeroid;

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clientes clientes = (Clientes) o;
        return numeroid == clientes.numeroid && Objects.equals(cedula, clientes.cedula) && Objects.equals(nombre, clientes.nombre) && Objects.equals(correo, clientes.correo) && Objects.equals(telefono, clientes.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroid, cedula, nombre, correo, telefono);
    }

    public Collection<Adquiere> getAdquieresByNumeroid() {
        return adquieresByNumeroid;
    }

    public void setAdquieresByNumeroid(Collection<Adquiere> adquieresByNumeroid) {
        this.adquieresByNumeroid = adquieresByNumeroid;
    }

    public Posee getPoseeByNumeroid() {
        return poseeByNumeroid;
    }

    public void setPoseeByNumeroid(Posee poseeByNumeroid) {
        this.poseeByNumeroid = poseeByNumeroid;
    }
}
