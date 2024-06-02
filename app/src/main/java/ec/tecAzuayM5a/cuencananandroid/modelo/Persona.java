package ec.tecAzuayM5a.cuencananandroid.modelo;

import java.util.Date;

public class Persona {

    private String cedula;

    private String nombres;

    private String apellidos;

    private String correo;

    private String direccion;

    private String telf;

    private String foto;
    private Date fecha_nac;
    private String contrasena;
    private String tipoUsuario;


    public Persona(String cedula, String nombres, String apellidos, String correo, String direccion, String telf, String foto, Date fecha_nac, String contrasenia) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.direccion = direccion;
        this.telf = telf;
        this.foto = foto;
        this.fecha_nac = fecha_nac;
        this.contrasena = contrasenia;
        this.tipoUsuario = tipoUsuario;
    }

    public Persona() {

    }

    public Persona(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Date getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(Date fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getContrasenia() {
        return contrasena;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasena = contrasenia;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
