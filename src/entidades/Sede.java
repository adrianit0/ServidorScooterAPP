package entidades;


import java.util.HashSet;
import java.util.Set;


public class Sede  implements java.io.Serializable {


     private Integer id;
     private Ciudad ciudad;
     private String nombre;
     private String direccion;
     private Set scooters = new HashSet(0);
     private Set empleados = new HashSet(0);

    public Sede() {
    }

    public Sede(Integer id) {
        this.id = id;
    }
	
    public Sede(Ciudad ciudad, String nombre, String direccion) {
        this.ciudad = ciudad;
        this.nombre = nombre;
        this.direccion = direccion;
    }
    public Sede(Ciudad ciudad, String nombre, String direccion, Set scooters, Set empleados) {
       this.ciudad = ciudad;
       this.nombre = nombre;
       this.direccion = direccion;
       this.scooters = scooters;
       this.empleados = empleados;
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
    public Set getEmpleados() {
        return this.empleados;
    }
    
    public void setEmpleados(Set empleados) {
        this.empleados = empleados;
    }




}


