package entidades;

import anotaciones.Ignore;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Cliente  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private String apellido1;
     private String apellido2;
     private String nick;
     private String email;
     @Ignore
     private String pass;
     private int minutos;
     private byte activada;
     private Date fechaCreacion;
     private Set incidencias = new HashSet(0);
     private Set alquilers = new HashSet(0);
     private Set historialcompras = new HashSet(0);

    public Cliente() {
    }
    
    public Cliente (Integer id) {
        this.id = id;
    }

    public Cliente(String nombre, String apellido1, String nick, String email, String pass, int minutos, byte activada, Date fechaCreacion) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.nick = nick;
        this.email = email;
        this.pass = pass;
        this.minutos = minutos;
        this.activada = activada;
        this.fechaCreacion = fechaCreacion;
    }
    public Cliente(String nombre, String apellido1, String apellido2, String nick, String email, String pass, int minutos, byte activada, Date fechaCreacion, Set incidencias, Set alquilers, Set historialcompras) {
       this.nombre = nombre;
       this.apellido1 = apellido1;
       this.apellido2 = apellido2;
       this.nick = nick;
       this.email = email;
       this.pass = pass;
       this.minutos = minutos;
       this.activada = activada;
       this.fechaCreacion = fechaCreacion;
       this.incidencias = incidencias;
       this.alquilers = alquilers;
       this.historialcompras = historialcompras;
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
    public String getApellido1() {
        return this.apellido1;
    }
    
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    public String getApellido2() {
        return this.apellido2;
    }
    
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    public String getNick() {
        return this.nick;
    }
    
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPass() {
        return this.pass;
    }
    
    public void setPass(String pass) {
        this.pass = pass;
    }
    public int getMinutos() {
        return this.minutos;
    }
    
    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }
    public byte getActivada() {
        return this.activada;
    }
    
    public void setActivada(byte activada) {
        this.activada = activada;
    }
    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public Set getIncidencias() {
        return this.incidencias;
    }
    
    public void setIncidencias(Set incidencias) {
        this.incidencias = incidencias;
    }
    public Set getAlquilers() {
        return this.alquilers;
    }
    
    public void setAlquilers(Set alquilers) {
        this.alquilers = alquilers;
    }
    public Set getHistorialcompras() {
        return this.historialcompras;
    }
    
    public void setHistorialcompras(Set historialcompras) {
        this.historialcompras = historialcompras;
    }




}


