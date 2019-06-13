package entidades;


import java.util.HashSet;
import java.util.Set;


public class Puesto  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String descripcion;
     private Set empleados = new HashSet(0);

    public Puesto() {
    }
    
    public Puesto (Integer id) {
        this.id = id;
    }

	
    public Puesto(String nombre) {
        this.nombre = nombre;
    }
    public Puesto(String nombre, String descripcion, Set empleados) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.empleados = empleados;
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
    public Set getEmpleados() {
        return this.empleados;
    }
    
    public void setEmpleados(Set empleados) {
        this.empleados = empleados;
    }




}


