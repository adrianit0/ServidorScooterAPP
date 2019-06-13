package entidades;

import anotaciones.Ignore;
import java.util.HashSet;
import java.util.Set;

public class Bono  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String descripcion;
     private int minutos;
     private double precio;
     @Ignore
     private Set historialcompras = new HashSet(0);
     @Ignore
     private Set descuentos = new HashSet(0);

    public Bono() {
    }

	
    public Bono(String nombre, String descripcion, int minutos, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.minutos = minutos;
        this.precio = precio;
    }
    public Bono(String nombre, String descripcion, int minutos, double precio, Set historialcompras, Set descuentos) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.minutos = minutos;
       this.precio = precio;
       this.historialcompras = historialcompras;
       this.descuentos = descuentos;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getMinutos() {
        return this.minutos;
    }
    
    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }
    public double getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public Set getHistorialcompras() {
        return this.historialcompras;
    }
    
    public void setHistorialcompras(Set historialcompras) {
        this.historialcompras = historialcompras;
    }
    public Set getDescuentos() {
        return this.descuentos;
    }
    
    public void setDescuentos(Set descuentos) {
        this.descuentos = descuentos;
    }




}


