package ec.tecAzuayM5a.cuencananandroid.modelo;

public class TipoEventos {

    private Long id_tipoEvento;
    private String nombre_tipoEvento;
    private String descripcion;

    public TipoEventos() {
    }

    public TipoEventos(Long id_tipoEvento, String nombre_tipoEvento, String descripcion) {
        this.id_tipoEvento = id_tipoEvento;
        this.nombre_tipoEvento = nombre_tipoEvento;
        this.descripcion = descripcion;
    }

    public Long getId_tipoEvento() {
        return id_tipoEvento;
    }

    public void setId_tipoEvento(Long id_tipoEvento) {
        this.id_tipoEvento = id_tipoEvento;
    }

    public String getNombre_tipoEvento() {
        return nombre_tipoEvento;
    }

    public void setNombre_tipoEvento(String nombre_tipoEvento) {
        this.nombre_tipoEvento = nombre_tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
