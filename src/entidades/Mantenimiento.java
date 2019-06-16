package entidades;

import java.util.Date;

public class Mantenimiento  implements java.io.Serializable {

     private Integer id;
     private Scooter scooter;
     private Date fechaInicio;
     private Date fechaFin;

    public Mantenimiento() {
    }

	
    public Mantenimiento(Scooter scooter, Date fechaInicio, Date fechaFin) {
        this.scooter = scooter;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Scooter getScooter() {
        return this.scooter;
    }
    
    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }
    public Date getFechaInicio() {
        return this.fechaInicio;
    }
    
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public Date getFechaFin() {
        return this.fechaFin;
    }
    
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}