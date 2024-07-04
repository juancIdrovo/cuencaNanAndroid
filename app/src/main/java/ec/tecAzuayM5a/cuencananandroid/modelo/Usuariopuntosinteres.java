package ec.tecAzuayM5a.cuencananandroid.modelo;

public class Usuariopuntosinteres {

    private Long idusuariopuntosinteres;
    private Long idusuario;
    private Long idpuntosinteres;
    private int calificacion;
    private String comentarios;

    public Long getIdusuariopuntosinteres() {
        return idusuariopuntosinteres;
    }

    public void setIdusuariopuntosinteres(Long idusuariopuntosinteres) {
        this.idusuariopuntosinteres = idusuariopuntosinteres;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public Long getIdpuntosinteres() {
        return idpuntosinteres;
    }

    public void setIdpuntosinteres(Long idpuntosinteres) {
        this.idpuntosinteres = idpuntosinteres;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Usuariopuntosinteres() {
    }

    public Usuariopuntosinteres(Long idusuariopuntosinteres, Long idusuario, Long idpuntosinteres, int calificacion, String comentarios) {
        this.idusuariopuntosinteres = idusuariopuntosinteres;
        this.idusuario = idusuario;
        this.idpuntosinteres = idpuntosinteres;
        this.calificacion = calificacion;
        this.comentarios = comentarios;
    }
}
