package entidades;

import anotaciones.Ignore;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Scooter  implements java.io.Serializable {


     private Integer id;
     private String noSerie;
     private Float bateria;
     @Ignore
     private Modelo modelo;
     private String matricula;
     private boolean bloqueada;
     private int codigo;
     private Date fechaCompra;
     private double precioCompra;
     private Double posicionLat;
     private Double  posicionLon;
     @Ignore
     private Set estadoscooters = new HashSet(0);
     @Ignore
     private Set sedes = new HashSet(0);
     @Ignore
     private Set mantenimientos = new HashSet(0);

    public Scooter() {
        
    }

	
    public Scooter(Modelo modelo, String matricula, int codigo, Date fechaCompra, double precioCompra) {
        this.modelo = modelo;
        this.matricula = matricula;
        this.codigo = codigo;
        this.fechaCompra = fechaCompra;
        this.precioCompra = precioCompra;
    }

    public Scooter(String noSerie, Modelo modelo, String matricula, int codigo, Date fechaCompra, double precioCompra, Double  posicionLat, Double  posicionLon) {
        this.noSerie = noSerie;
        this.modelo = modelo;
        this.matricula = matricula;
        this.codigo = codigo;
        this.fechaCompra = fechaCompra;
        this.precioCompra = precioCompra;
        this.posicionLat = posicionLat;
        this.posicionLon = posicionLon;
    }

    public Float getBateria() {
        return bateria;
    }

    public void setBateria(Float bateria) {
        this.bateria = bateria;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }
    
    
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Modelo getModelo() {
        return this.modelo;
    }
    
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    public String getMatricula() {
        return this.matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public Date getFechaCompra() {
        return this.fechaCompra;
    }
    
    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    public double getPrecioCompra() {
        return this.precioCompra;
    }
    
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }
    public Double  getPosicionLat() {
        return this.posicionLat;
    }
    
    public void setPosicionLat(Double  posicionLat) {
        this.posicionLat = posicionLat;
    }

    public Double  getPosicionLon() {
        return posicionLon;
    }

    public void setPosicionLon(Double  posicionLon) {
        this.posicionLon = posicionLon;
    }
    
    public Set getEstadoscooters() {
        return this.estadoscooters;
    }
    
    public void setEstadoscooters(Set estadoscooters) {
        this.estadoscooters = estadoscooters;
    }
    public Set getSedes() {
        return this.sedes;
    }
    
    public void setSedes(Set sedes) {
        this.sedes = sedes;
    }
    public Set getMantenimientos() {
        return this.mantenimientos;
    }
    
    public void setMantenimientos(Set mantenimientos) {
        this.mantenimientos = mantenimientos;
    }




}


