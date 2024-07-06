package ec.tecAzuayM5a.cuencananandroid.modelo;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalTime;
import java.util.Date;

import java.time.LocalTime;
import java.util.Date;

public class Eventos {

    private Long id_evento;
    private Long id_tipoEvento;
    private Long id_Administrador;
    private String nombre;
    private LocalTime hora_Inicio;
    private LocalTime hora_Fin;
    private Date fecha_Inicio;
    private Date fecha_Fin;
    private TipoEventos tipoEvento;
    private LatLng puntoInteresLocation;
    private String fotoUrl;




    // Getters y setters (incluyendo el nuevo campo tipoEvento)

    public TipoEventos getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEventos tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    public Eventos() {
    }

    public Eventos(Long id_evento, Long id_tipoEvento, Long id_Administrador, String nombre, LocalTime hora_Inicio, LocalTime hora_Fin, Date fecha_Inicio, Date fecha_Fin) {
        this.id_evento = id_evento;
        this.id_tipoEvento = id_tipoEvento;
        this.id_Administrador = id_Administrador;
        this.nombre = nombre;
        this.hora_Inicio = hora_Inicio;
        this.hora_Fin = hora_Fin;
        this.fecha_Inicio = fecha_Inicio;
        this.fecha_Fin = fecha_Fin;
    }

    // Getters y setters

    public Long getId_evento() {
        return id_evento;
    }

    public void setId_evento(Long id_evento) {
        this.id_evento = id_evento;
    }

    public Long getId_tipoEvento() {
        return id_tipoEvento;
    }

    public void setId_tipoEvento(Long id_tipoEvento) {
        this.id_tipoEvento = id_tipoEvento;
    }

    public Long getId_Administrador() {
        return id_Administrador;
    }

    public void setId_Administrador(Long id_Administrador) {
        this.id_Administrador = id_Administrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalTime getHora_Inicio() {
        return hora_Inicio;
    }

    public void setHora_Inicio(LocalTime hora_Inicio) {
        this.hora_Inicio = hora_Inicio;
    }

    public LocalTime getHora_Fin() {
        return hora_Fin;
    }

    public void setHora_Fin(LocalTime hora_Fin) {
        this.hora_Fin = hora_Fin;
    }

    public Date getFecha_Inicio() {
        return fecha_Inicio;
    }

    public void setFecha_Inicio(Date fecha_Inicio) {
        this.fecha_Inicio = fecha_Inicio;
    }

    public Date getFecha_Fin() {
        return fecha_Fin;
    }

    public void setFecha_Fin(Date fecha_Fin) {
        this.fecha_Fin = fecha_Fin;
    }

    public LatLng getPuntoInteresLocation() {
        return puntoInteresLocation;
    }

    public void setPuntoInteresLocation(LatLng puntoInteresLocation) {
        this.puntoInteresLocation = puntoInteresLocation;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

}

