package entidades;

import java.util.Date;


public class Historialcompra  implements java.io.Serializable {


     private Integer id;
     private Bono bono;
     private Cliente cliente;
     private Date fecha;
     private double precio;
     private int minutos;

    public Historialcompra() {
    }

    public Historialcompra(Bono bono, Cliente cliente, Date fecha, double precio, int minutos) {
       this.bono = bono;
       this.cliente = cliente;
       this.fecha = fecha;
       this.precio = precio;
       this.minutos = minutos;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Bono getBono() {
        return this.bono;
    }
    
    public void setBono(Bono bono) {
        this.bono = bono;
    }
    public Cliente getCliente() {
        return this.cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public double getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getMinutos() {
        return this.minutos;
    }
    
    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }




}


