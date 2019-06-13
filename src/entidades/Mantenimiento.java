package entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Mantenimiento  implements java.io.Serializable {


     private Integer id;
     private Scooter scooter;
     private Date fechaInicio;
     private Date fechaFin;
     private Set incidencias = new HashSet(0);

    public Mantenimiento() {
    }

	
    public Mantenimiento(Scooter scooter, Date fechaInicio, Date fechaFin) {
        this.scooter = scooter;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    public Mantenimiento(Scooter scooter, Date fechaInicio, Date fechaFin, Set incidencias) {
       this.scooter = scooter;
       this.fechaInicio = fechaInicio;
       this.fechaFin = fechaFin;
       this.incidencias = incidencias;
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
    public Set getIncidencias() {
        return this.incidencias;
    }
    
    public void setIncidencias(Set incidencias) {
        this.incidencias = incidencias;
    }




}


