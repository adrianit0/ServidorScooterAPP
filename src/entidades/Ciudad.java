package entidades;

import java.util.HashSet;
import java.util.Set;

public class Ciudad  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String provincia;
     private Set sedes = new HashSet(0);
     private Set empleados = new HashSet(0);

    public Ciudad() {
    }

    public Ciudad(Integer id) {
        this.id = id;
    }
	
    public Ciudad(String nombre, String provincia) {
        this.nombre = nombre;
        this.provincia = provincia;
    }
    public Ciudad(String nombre, String provincia, Set sedes, Set empleados) {
       this.nombre = nombre;
       this.provincia = provincia;
       this.sedes = sedes;
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
    public String getProvincia() {
        return this.provincia;
    }
    
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    public Set getSedes() {
        return this.sedes;
    }
    
    public void setSedes(Set sedes) {
        this.sedes = sedes;
    }
    public Set getEmpleados() {
        return this.empleados;
    }
    
    public void setEmpleados(Set empleados) {
        this.empleados = empleados;
    }

    @Override
    public String toString() {
        return nombre + " (" + provincia + ")";
    }
}


