package ec.tecAzuayM5a.cuencananandroid.modelo;

import java.util.Date;

public class Usuario {
    private String idUsuario;
    private String cedula;
    private String interes;

    public Usuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(String idUsuario, String cedula, String interes) {
        this.idUsuario = idUsuario;
        this.cedula = cedula;
        this.interes = interes;
    }

    public Usuario() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getInteres() {
        return interes;
    }

    public void setInteres(String interes) {
        this.interes = interes;
    }
}
