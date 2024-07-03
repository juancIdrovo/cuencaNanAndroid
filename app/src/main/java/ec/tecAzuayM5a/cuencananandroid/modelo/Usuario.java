package ec.tecAzuayM5a.cuencananandroid.modelo;

import java.util.Date;

public class Usuario {
    private Long id_usuario;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String mail;
    private String direccion;
    private Date fecha_nacimiento;
    private String contrasena;
    private String celular;
    private String fotoUrl;
    private String fotoPath;


    public Usuario(Long id_usuario, String cedula, String nombres, String apellidos, String mail, String direccion, Date fecha_nacimiento, String contrasena, String celular, String fotoUrl, String fotoPath) {
        this.id_usuario = id_usuario;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.mail = mail;
        this.direccion = direccion;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.celular = celular;
        this.fotoUrl = fotoUrl;
        this.fotoPath = fotoPath;
    }

    public Usuario(Long id_usuario, String cedula, String nombres, String apellidos, String mail, String direccion, Date fecha_nacimiento, String contrasena, String celular, String fotoUrl) {
        this.id_usuario = id_usuario;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.mail = mail;
        this.direccion = direccion;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.celular = celular;
        this.fotoUrl = fotoUrl;
    }
    public Usuario(String cedula, String nombres, String apellidos, String mail, String direccion, Date fecha_nacimiento, String contrasena, String celular, String fotoUrl) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.mail = mail;
        this.direccion = direccion;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.celular = celular;
        this.fotoUrl = fotoUrl;
    }

    public Usuario() {
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
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

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }
}