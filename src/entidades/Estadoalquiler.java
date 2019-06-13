package entidades;

public class Estadoalquiler  implements java.io.Serializable {


     private Integer id;
     private String nombre;

    public Estadoalquiler() {
    }

    public Estadoalquiler (Integer id) {
        this.id=id;
    }
    
    public Estadoalquiler(String nombre) {
       this.nombre = nombre;
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




}


