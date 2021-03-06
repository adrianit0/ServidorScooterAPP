package entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Descuento  implements java.io.Serializable {


     private Integer id;
     private Date desde;
     private Date hasta;
     private double descuento;
     private Set bonos = new HashSet(0);

    public Descuento() {
    }

	
    public Descuento(Date desde, Date hasta, double descuento) {
        this.desde = desde;
        this.hasta = hasta;
        this.descuento = descuento;
    }
    public Descuento(Date desde, Date hasta, double descuento, Set bonos) {
       this.desde = desde;
       this.hasta = hasta;
       this.descuento = descuento;
       this.bonos = bonos;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getDesde() {
        return this.desde;
    }
    
    public void setDesde(Date desde) {
        this.desde = desde;
    }
    public Date getHasta() {
        return this.hasta;
    }
    
    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }
    public double getDescuento() {
        return this.descuento;
    }
    
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    public Set getBonos() {
        return this.bonos;
    }
    
    public void setBonos(Set bonos) {
        this.bonos = bonos;
    }




}


