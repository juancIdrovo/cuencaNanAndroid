package ec.tecAzuayM5a.cuencananandroid.modelo;
public class Foro {
    private Long idForo;
    private Long idUsuario;
    private String respuesta;
    private String titulo;

    public Foro() {
    }

    public Foro(Long idForo, Long idUsuario, String respuesta, String titulo) {
        this.idForo = idForo;
        this.idUsuario = idUsuario;
        this.respuesta = respuesta;
        this.titulo = titulo;
    }
    public Foro(Long idForo, Long idUsuario, String respuesta) {
        this.idForo = idForo;
        this.idUsuario = idUsuario;
        this.respuesta = respuesta;
    }

    public Long getIdForo() {
        return idForo;
    }

    public void setIdForo(Long idForo) {
        this.idForo = idForo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}