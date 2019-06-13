package entidades;

import java.util.Date;


public class Estadoscooter  implements java.io.Serializable {


     private Integer id;
     private Alquiler alquiler;
     private Scooter scooter;
     private Date fecha;
     private double estadoBateria;
     private int velocidad;
     private Double posicionLat;
     private Double posicionLon;

    public Estadoscooter() {
    }

	
    public Estadoscooter(Alquiler alquiler, Scooter scooter, Date fecha, double estadoBateria, int velocidad) {
        this.alquiler = alquiler;
        this.scooter = scooter;
        this.fecha = fecha;
        this.estadoBateria = estadoBateria;
        this.velocidad = velocidad;
    }
    public Estadoscooter(Alquiler alquiler, Scooter scooter, Date fecha, double estadoBateria, int velocidad, Double posicionLat, Double posicionLon) {
       this.alquiler = alquiler;
       this.scooter = scooter;
       this.fecha = fecha;
       this.estadoBateria = estadoBateria;
       this.velocidad = velocidad;
       this.posicionLat = posicionLat;
       this.posicionLon = posicionLon;
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
    public Double getPosicionLat() {
        return this.posicionLat;
    }
    
    public void setPosicionLat(Double posicionLat) {
        this.posicionLat = posicionLat;
    }
    public Double getPosicionAlt() {
        return this.posicionLon;
    }
    
    public void setPosicionAlt(Double posicionLon) {
        this.posicionLon = posicionLon;
    }
}


