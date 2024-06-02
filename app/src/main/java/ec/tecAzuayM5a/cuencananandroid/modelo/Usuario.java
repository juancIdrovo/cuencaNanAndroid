package ec.tecAzuayM5a.cuencananandroid.modelo;

import java.util.Date;

public class Usuario extends  Persona {

    private String cedula_estudiante_fk;
    private String interes;

    public String getInteres() {
        return interes;
    }

    public Usuario(String cedula, String nombres, String apellidos, String correo, String direccion, String telf, String foto, Date fecha_nac, String contrasenia, String cedula_estudiante_fk, String interes) {
        super(cedula, nombres, apellidos, correo, direccion, telf, foto, fecha_nac, contrasenia);
        this.cedula_estudiante_fk = cedula_estudiante_fk;
        this.interes = interes;
    }

    public Usuario(String cedula_estudiante_fk, String interes) {
        this.cedula_estudiante_fk = cedula_estudiante_fk;
        this.interes = interes;
    }

    public Usuario(String cedula, String cedula_estudiante_fk, String interes) {
        super(cedula);
        this.cedula_estudiante_fk = cedula_estudiante_fk;
        this.interes = interes;
    }

    public void setInteres(String interes) {
        this.interes = interes;
    }

    public Usuario(String cedula_estudiante_fk) {

        this.cedula_estudiante_fk = cedula_estudiante_fk;
    }

    public Usuario() {

    }

    public Usuario(String cedula, String nombres, String apellidos, String correo, String direccion, String telf, String foto, Date fecha_nac, String contrasenia, String cedula_estudiante_fk) {
        super(cedula, nombres, apellidos, correo, direccion, telf, foto, fecha_nac, contrasenia);
        this.cedula_estudiante_fk = cedula_estudiante_fk;
    }

    public String getCedula_estudiante_fk() {
        return cedula_estudiante_fk;
    }

    public void setCedula_estudiante_fk(String cedula_estudiante_fk) {
        this.cedula_estudiante_fk = cedula_estudiante_fk;
    }
}
