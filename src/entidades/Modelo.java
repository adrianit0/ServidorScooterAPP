package entidades;

import java.util.HashSet;
import java.util.Set;


public class Modelo  implements java.io.Serializable {


     private Integer id;
     private String marca;
     private String modelo;
     private Set scooters = new HashSet(0);

    public Modelo() {
    }

    public Modelo(Integer id) {
        this.id = id;
    }
	
    public Modelo(String marca, String modelo) {
        this.marca = marca;
        this.modelo = modelo;
    }
    public Modelo(String marca, String modelo, Set scooters) {
       this.marca = marca;
       this.modelo = modelo;
       this.scooters = scooters;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMarca() {
        return this.marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return this.modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public Set getScooters() {
        return this.scooters;
    }
    
    public void setScooters(Set scooters) {
        this.scooters = scooters;
    }

    @Override
    public String toString() {
        return marca + " " + modelo;
    }


}


