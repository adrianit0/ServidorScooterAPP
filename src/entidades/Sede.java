package entidades;
// Generated 22-abr-2019 23:38:06 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Sede generated by hbm2java
 */
public class Sede  implements java.io.Serializable {


     private Integer id;
     private Ciudad ciudad;
     private String nombre;
     private String direccion;
     private Set scooters = new HashSet(0);

    public Sede() {
    }

	
    public Sede(Ciudad ciudad, String nombre, String direccion) {
        this.ciudad = ciudad;
        this.nombre = nombre;
        this.direccion = direccion;
    }
    public Sede(Ciudad ciudad, String nombre, String direccion, Set scooters) {
       this.ciudad = ciudad;
       this.nombre = nombre;
       this.direccion = direccion;
       this.scooters = scooters;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Ciudad getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public Set getScooters() {
        return this.scooters;
    }
    
    public void setScooters(Set scooters) {
        this.scooters = scooters;
    }




}

