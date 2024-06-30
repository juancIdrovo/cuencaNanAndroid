package ec.tecAzuayM5a.cuencananandroid.modelo;

public class Foto {
    private Long fotoid;
    private String url;

    public Foto(Long fotoid, String url) {
        this.fotoid = fotoid;
        this.url = url;
    }

    public Long getFotoid() {
        return fotoid;
    }

    public void setFotoid(Long fotoid) {
        this.fotoid = fotoid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

