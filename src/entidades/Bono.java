package entidades;
// Generated 22-abr-2019 23:38:06 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Bono generated by hbm2java
 */
public class Bono  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String descripcion;
     private int minutos;
     private double precio;
     private Set historialCompras = new HashSet(0);
     private Set descuentos = new HashSet(0);

    public Bono() {
    }

	
    public Bono(String nombre, String descripcion, int minutos, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.minutos = minutos;
        this.precio = precio;
    }
    public Bono(String nombre, String descripcion, int minutos, double precio, Set historialCompras, Set descuentos) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.minutos = minutos;
       this.precio = precio;
       this.historialCompras = historialCompras;
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
    public Set getHistorialCompras() {
        return this.historialCompras;
    }
    
    public void setHistorialCompras(Set historialCompras) {
        this.historialCompras = historialCompras;
    }
    public Set getDescuentos() {
        return this.descuentos;
    }
    
    public void setDescuentos(Set descuentos) {
        this.descuentos = descuentos;
    }




}

