package entidades;
// Generated 22-abr-2019 23:38:06 by Hibernate Tools 4.3.1



/**
 * Imagen generated by hbm2java
 */
public class Imagen  implements java.io.Serializable {


     private Integer id;
     private Incidencia incidencia;
     private String nombre;
     private String url;

    public Imagen() {
    }

    public Imagen(Incidencia incidencia, String nombre, String url) {
       this.incidencia = incidencia;
       this.nombre = nombre;
       this.url = url;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Incidencia getIncidencia() {
        return this.incidencia;
    }
    
    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }




}

