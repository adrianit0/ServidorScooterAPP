package entidades;

import java.util.HashSet;
import java.util.Set;


public class Tipoincidencia  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String descripcion;
     private Set incidencias = new HashSet(0);

    public Tipoincidencia() {
    }

    public Tipoincidencia (Integer id) {
        this.id = id;
    }
	
    public Tipoincidencia(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Tipoincidencia(String nombre, String descripcion, Set incidencias) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.incidencias = incidencias;
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
    public Set getIncidencias() {
        return this.incidencias;
    }
    
    public void setIncidencias(Set incidencias) {
        this.incidencias = incidencias;
    }




}


