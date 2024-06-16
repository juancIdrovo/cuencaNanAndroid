package ec.tecAzuayM5a.cuencananandroid.modelo;

public class TipoPuntoInteres {

    private int idtipospuntosinteres;
    private String nombre;
    private String descripcion;
    private String categoria;

    public TipoPuntoInteres() {
    }

    public TipoPuntoInteres(int idtipospuntosinteres, String nombre, String descripcion, String categoria) {
        this.idtipospuntosinteres = idtipospuntosinteres;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }


    public int getIdtipospuntosinteres() {
        return idtipospuntosinteres;
    }

    public void setIdtipospuntosinteres(int idtipospuntosinteres) {
        this.idtipospuntosinteres = idtipospuntosinteres;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
