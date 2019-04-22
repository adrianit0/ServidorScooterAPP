package entidades;
// Generated 22-abr-2019 23:38:06 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Ciudad generated by hbm2java
 */
public class Ciudad  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String provincia;
     private Set sedes = new HashSet(0);

    public Ciudad() {
    }

	
    public Ciudad(String nombre, String provincia) {
        this.nombre = nombre;
        this.provincia = provincia;
    }
    public Ciudad(String nombre, String provincia, Set sedes) {
       this.nombre = nombre;
       this.provincia = provincia;
       this.sedes = sedes;
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




}


