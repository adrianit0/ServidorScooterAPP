package entidades;

import anotaciones.Ignore;
import java.sql.Date;

public class Alquiler  implements java.io.Serializable {


    private Integer id;
    private Integer minutosConducidos;
    private double costeTotal;
    private Integer minutosConsumidos;
    
    @Ignore
    private Date fechaInicio;
    @Ignore
    private Date fechaFin;
    @Ignore
    private Scooter scooter;
    @Ignore
    private Cliente cliente;
    @Ignore
    private Estadoalquiler estadoalquiler;

    public Alquiler() {}
	
    public Alquiler(Scooter scooter, Cliente cliente, Estadoalquiler estadoalquiler, Date fechaInicio, Date fechaFin, double costeTotal) {
        this.scooter = scooter;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costeTotal = costeTotal;
        this.estadoalquiler = estadoalquiler;
    }

    public Estadoalquiler getEstadoalquiler() {
        return estadoalquiler;
    }

    public void setEstadoalquiler(Estadoalquiler estadoalquiler) {
        this.estadoalquiler = estadoalquiler;
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
    public double getCosteTotal() {
        return this.costeTotal;
    }
    
    public void setCosteTotal(double costeTotal) {
        this.costeTotal = costeTotal;
    }

    public Integer getMinutosConsumidos() {
        return minutosConsumidos;
    }

    public void setMinutosConsumidos(Integer minutosConsumidos) {
        this.minutosConsumidos = minutosConsumidos;
    }
    
    
}