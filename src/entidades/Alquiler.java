package entidades;

import java.sql.Date;

public class Alquiler  implements java.io.Serializable {


     private Integer id;
     private Scooter scooter;
     private Cliente cliente;
     private Date fechaInicio;
     private Date fechaFin;
     private Integer minutosConducidos;
     private Integer minutosParada;
     private double costeTotal;

    public Alquiler() {}
	
    public Alquiler(Scooter scooter, Cliente cliente, Date fechaInicio, Date fechaFin, double costeTotal) {
        this.scooter = scooter;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costeTotal = costeTotal;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
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
}