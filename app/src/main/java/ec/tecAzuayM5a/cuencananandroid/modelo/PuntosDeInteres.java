package ec.tecAzuayM5a.cuencananandroid.modelo;

public class PuntosDeInteres {
    private Long id;
    private Long idAdministrador;
    private Long idTipoPuntoInteres;
    private Long idFoto;
    private String nombre;
    private double latitud;
    private double longitud;
    private String categoria;
    private String descripcion;

    public PuntosDeInteres() {
    }

    public PuntosDeInteres(Long id, Long idAdministrador, Long idTipoPuntoInteres, Long idFoto, String nombre, double latitud, double longitud, String categoria, String descripcion) {
        this.id = id;
        this.idAdministrador = idAdministrador;
        this.idTipoPuntoInteres = idTipoPuntoInteres;
        this.idFoto = idFoto;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public PuntosDeInteres(int idPuntoInteres, String nombre, double latitud, double longitud) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(Long idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public Long getIdTipoPuntoInteres() {
        return idTipoPuntoInteres;
    }

    public void setIdTipoPuntoInteres(Long idTipoPuntoInteres) {
        this.idTipoPuntoInteres = idTipoPuntoInteres;
    }

    public Long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
