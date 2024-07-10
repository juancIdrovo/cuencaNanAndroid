package ec.tecAzuayM5a.cuencananandroid.modelo;

public class Comentario {
    private final String userName;
    private final String comment;
    private final String userPhotoUrl;

    public Comentario(String userName, String comment, String userPhotoUrl) {
        this.userName = userName;
        this.comment = comment;
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }
}
