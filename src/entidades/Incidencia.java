package entidades;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Incidencia  implements java.io.Serializable {

     private Integer id;
     private String descripcion;
     private Double posicionLat;
     private Double posicionLon;
     private Timestamp fechaCreacion;
     
     private Scooter scooter;
     private Alquiler alquiler;
     private Cliente cliente;
     private Mantenimiento mantenimiento;
     private Tipoincidencia tipoincidencia;
     private Set imagens = new HashSet(0);

    public Incidencia() {
    }

	
    public Incidencia(Cliente cliente, Tipoincidencia tipoincidencia) {
        this.cliente = cliente;
        this.tipoincidencia = tipoincidencia;
    }
    public Incidencia(Alquiler alquiler, Cliente cliente, Scooter scooter, Mantenimiento mantenimiento, Tipoincidencia tipoincidencia, String descripcion, Double posicionLat, Double posicionLon, Set imagens) {
       this.alquiler = alquiler;
       this.cliente = cliente;
       this.mantenimiento = mantenimiento;
       this.tipoincidencia = tipoincidencia;
       this.descripcion = descripcion;
       this.posicionLat = posicionLat;
       this.posicionLon = posicionLon;
       this.imagens = imagens;
       this.scooter = scooter;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
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
    public Mantenimiento getMantenimiento() {
        return this.mantenimiento;
    }
    
    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }
    public Tipoincidencia getTipoincidencia() {
        return this.tipoincidencia;
    }
    
    public void setTipoincidencia(Tipoincidencia tipoincidencia) {
        this.tipoincidencia = tipoincidencia;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Double getPosicionLat() {
        return this.posicionLat;
    }
    
    public void setPosicionLat(Double posicionLat) {
        this.posicionLat = posicionLat;
    }
    public Double getPosicionLon() {
        return this.posicionLon;
    }
    
    public void setPosicionLon(Double posicionLon) {
        this.posicionLon = posicionLon;
    }
    public Set getImagens() {
        return this.imagens;
    }
    
    public void setImagens(Set imagens) {
        this.imagens = imagens;
    }
}


