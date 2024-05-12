package una.ac.cr.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Productos {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "numeroid")
    private int numeroid;
    @Basic
    @Column(name = "codigo")
    private String codigo;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "precio")
    private double precio;
    @OneToOne(mappedBy = "productosByNumeroprod")
    private Almacena almacenaByNumeroid;
    @OneToMany(mappedBy = "productosByNumeroprod")
    private Collection<Contiene> contienesByNumeroid;

    public int getNumeroid() {
        return numeroid;
    }

    public void setNumeroid(int numeroid) {
        this.numeroid = numeroid;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Productos productos = (Productos) o;
        return numeroid == productos.numeroid && Double.compare(precio, productos.precio) == 0 && Objects.equals(codigo, productos.codigo) && Objects.equals(nombre, productos.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroid, codigo, nombre, precio);
    }

    public Almacena getAlmacenaByNumeroid() {
        return almacenaByNumeroid;
    }

    public void setAlmacenaByNumeroid(Almacena almacenaByNumeroid) {
        this.almacenaByNumeroid = almacenaByNumeroid;
    }

    public Collection<Contiene> getContienesByNumeroid() {
        return contienesByNumeroid;
    }

    public void setContienesByNumeroid(Collection<Contiene> contienesByNumeroid) {
        this.contienesByNumeroid = contienesByNumeroid;
    }
}
