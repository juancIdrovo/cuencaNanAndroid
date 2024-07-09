package ec.tecAzuayM5a.cuencananandroid.modelo;
public class Foro {
    private Long idForo;
    private Long idUsuario;
    private String respuesta;
    private String titulo;
    private Long idFoto;
    private Foto foto;
    private String usuarioFotoUrl;
    private String comentario;


    public Foro() {
    }

    public Foro(Long idForo, Long idUsuario, String respuesta, String titulo, Long idFoto) {
        this.idForo = idForo;
        this.idUsuario = idUsuario;
        this.respuesta = respuesta;
        this.titulo = titulo;
        this.idFoto = idFoto;
    }

    public Foro(Long idForo, Long idUsuario, String respuesta, String titulo, Long idFoto, Foto foto) {
        this.idForo = idForo;
        this.idUsuario = idUsuario;
        this.respuesta = respuesta;
        this.titulo = titulo;
        this.idFoto = idFoto;
        this.foto = foto;
    }

    public Foro(Long idForo, Long idUsuario, String respuesta, String titulo, Long idFoto, Foto foto, String usuarioFotoUrl) {
        this.idForo = idForo;
        this.idUsuario = idUsuario;
        this.respuesta = respuesta;
        this.titulo = titulo;
        this.idFoto = idFoto;
        this.foto = foto;
        this.usuarioFotoUrl = usuarioFotoUrl;
    }
    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
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

    public Long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
    }
    public String getUsuarioFotoUrl() {
        return usuarioFotoUrl;
    }

    public void setUsuarioFotoUrl(String usuarioFotoUrl) {
        this.usuarioFotoUrl = usuarioFotoUrl;
    }
}