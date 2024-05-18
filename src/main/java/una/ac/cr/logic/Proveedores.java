package una.ac.cr.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Collection;
import java.util.Objects;

@Builder
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
@Entity
public class Proveedores {
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
    @Basic
    @Column(name = "estado")
    private boolean estado;
    @OneToMany(mappedBy = "proveedoresByNumeroprovee")
    private Collection<Almacena> almacenasByNumeroid;
    @OneToMany(mappedBy = "proveedoresByNumeroprovee")
    private Collection<Posee> poseesByNumeroid;
    @OneToMany(mappedBy = "proveedoresByNumeroprovee")
    private Collection<Tiene> tienesByNumeroid;

    public Proveedores() {

    }

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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean aprovado) {
        this.estado = aprovado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedores that = (Proveedores) o;
        return numeroid == that.numeroid && estado == that.estado && Objects.equals(cedula, that.cedula) && Objects.equals(nombre, that.nombre) && Objects.equals(correo, that.correo) && Objects.equals(telefono, that.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroid, cedula, nombre, correo, telefono, estado);
    }

    public Collection<Almacena> getAlmacenasByNumeroid() {
        return almacenasByNumeroid;
    }

    public void setAlmacenasByNumeroid(Collection<Almacena> almacenasByNumeroid) {
        this.almacenasByNumeroid = almacenasByNumeroid;
    }

    public Collection<Posee> getPoseesByNumeroid() {
        return poseesByNumeroid;
    }

    public void setPoseesByNumeroid(Collection<Posee> poseesByNumeroid) {
        this.poseesByNumeroid = poseesByNumeroid;
    }

    public Collection<Tiene> getTienesByNumeroid() {
        return tienesByNumeroid;
    }

    public void setTienesByNumeroid(Collection<Tiene> tienesByNumeroid) {
        this.tienesByNumeroid = tienesByNumeroid;
    }
}
