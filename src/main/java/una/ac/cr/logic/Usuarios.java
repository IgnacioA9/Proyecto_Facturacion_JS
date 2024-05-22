package una.ac.cr.logic;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;


import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
@Entity
public class Usuarios {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "numeroid")
    private int numeroid;
    @Basic
    @Column(name = "identificacion")
    private String identificacion;
    @Basic
    @Column(name = "contrasena")
    private String contrasena;
    @Basic
    @Column(name = "rol")
    private String rol;

    public int getNumeroid() {
        return numeroid;
    }

    public void setNumeroid(int numeroid) {
        this.numeroid = numeroid;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String tipo) {
        this.rol = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuarios usuarios = (Usuarios) o;
        return numeroid == usuarios.numeroid && Objects.equals(identificacion, usuarios.identificacion) && Objects.equals(contrasena, usuarios.contrasena) && Objects.equals(rol, usuarios.rol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroid, identificacion, contrasena, rol);
    }
}
