package ec.tecAzuayM5a.cuencananandroid.modelo;

public class Foro {

    private int iconoperfil;
    private int imagen;
    private String titulo;
    private String mensaje;

    public Foro(int iconoperfil, int imagen, String titulo, String mensaje) {
        this.iconoperfil = iconoperfil;
        this.imagen = imagen;
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    public Foro() {
    }

    public int getIconoperfil() {
        return iconoperfil;
    }

    public void setIconoperfil(int iconoperfil) {
        this.iconoperfil = iconoperfil;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
