package una.ac.cr.logic;

public class ContieneDTO {
    private String codigo;
    private int cantidadproducto;

    // Getters y Setters
    public String getNumeroprod() {
        return codigo;
    }

    public void setNumeroprod(String numeroprod) {
        this.codigo = numeroprod;
    }

    public int getCantidadproducto() {
        return cantidadproducto;
    }

    public void setCantidadproducto(int cantidadproducto) {
        this.cantidadproducto = cantidadproducto;
    }
}

