package entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Alquiler  implements java.io.Serializable {


     private Integer id;
     private Alquiler alquiler;
     private Cliente cliente;
     private Date fechaInicio;
     private Date fechaFin;
     private Integer minutosConducidos;
     private Integer minutosParada;
     private double costeTotal;
     private Set alquilers = new HashSet(0);
     private Set estadoscooters = new HashSet(0);
     private Set incidencias = new HashSet(0);

    public Alquiler() {
    }

	
    public Alquiler(Alquiler alquiler, Cliente cliente, Date fechaInicio, Date fechaFin, double costeTotal) {
        this.alquiler = alquiler;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costeTotal = costeTotal;
    }
    public Alquiler(Alquiler alquiler, Cliente cliente, Date fechaInicio, Date fechaFin, Integer minutosConducidos, Integer minutosParada, double costeTotal, Set alquilers, Set estadoscooters, Set incidencias) {
       this.alquiler = alquiler;
       this.cliente = cliente;
       this.fechaInicio = fechaInicio;
       this.fechaFin = fechaFin;
       this.minutosConducidos = minutosConducidos;
       this.minutosParada = minutosParada;
       this.costeTotal = costeTotal;
       this.alquilers = alquilers;
       this.estadoscooters = estadoscooters;
       this.incidencias = incidencias;
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
    public Cliente getCliente() {
        return this.cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
    public Integer getMinutosConducidos() {
        return this.minutosConducidos;
    }
    
    public void setMinutosConducidos(Integer minutosConducidos) {
        this.minutosConducidos = minutosConducidos;
    }
    public Integer getMinutosParada() {
        return this.minutosParada;
    }
    
    public void setMinutosParada(Integer minutosParada) {
        this.minutosParada = minutosParada;
    }
    public double getCosteTotal() {
        return this.costeTotal;
    }
    
    public void setCosteTotal(double costeTotal) {
        this.costeTotal = costeTotal;
    }
    public Set getAlquilers() {
        return this.alquilers;
    }
    
    public void setAlquilers(Set alquilers) {
        this.alquilers = alquilers;
    }
    public Set getEstadoscooters() {
        return this.estadoscooters;
    }
    
    public void setEstadoscooters(Set estadoscooters) {
        this.estadoscooters = estadoscooters;
    }
    public Set getIncidencias() {
        return this.incidencias;
    }
    
    public void setIncidencias(Set incidencias) {
        this.incidencias = incidencias;
    }




}


