package entidades;
// Generated 28-abr-2019 23:19:03 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Estadoscooter generated by hbm2java
 */
public class Estadoscooter  implements java.io.Serializable {


     private Integer id;
     private Alquiler alquiler;
     private Scooter scooter;
     private Date fecha;
     private double estadoBateria;
     private int velocidad;
     private Integer posicionLat;
     private Integer posicionAlt;

    public Estadoscooter() {
    }

	
    public Estadoscooter(Alquiler alquiler, Scooter scooter, Date fecha, double estadoBateria, int velocidad) {
        this.alquiler = alquiler;
        this.scooter = scooter;
        this.fecha = fecha;
        this.estadoBateria = estadoBateria;
        this.velocidad = velocidad;
    }
    public Estadoscooter(Alquiler alquiler, Scooter scooter, Date fecha, double estadoBateria, int velocidad, Integer posicionLat, Integer posicionAlt) {
       this.alquiler = alquiler;
       this.scooter = scooter;
       this.fecha = fecha;
       this.estadoBateria = estadoBateria;
       this.velocidad = velocidad;
       this.posicionLat = posicionLat;
       this.posicionAlt = posicionAlt;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Alquiler getAlquiler() {
        return this.alquiler;
    }
    
    public void setAlquiler(Alquiler alquiler) {
        this.alquiler = alquiler;
    }
    public Scooter getScooter() {
        return this.scooter;
    }
    
    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public double getEstadoBateria() {
        return this.estadoBateria;
    }
    
    public void setEstadoBateria(double estadoBateria) {
        this.estadoBateria = estadoBateria;
    }
    public int getVelocidad() {
        return this.velocidad;
    }
    
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
    public Integer getPosicionLat() {
        return this.posicionLat;
    }
    
    public void setPosicionLat(Integer posicionLat) {
        this.posicionLat = posicionLat;
    }
    public Integer getPosicionAlt() {
        return this.posicionAlt;
    }
    
    public void setPosicionAlt(Integer posicionAlt) {
        this.posicionAlt = posicionAlt;
    }




}


