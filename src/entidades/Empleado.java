package entidades;

import anotaciones.Ignore;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Empleado  implements java.io.Serializable {


     private Integer id;
     private Ciudad ciudad;
     private Puesto puesto;
     private Sede sede;
     private String nombre;
     private String apellido1;
     private String apellido2;
     private String dni;
     private String direccion;
     private String email;
     private String pass;
     private double sueldo;
     private Date fechaAlta;
     private Date fechaBaja;
     @Ignore
     private Set tareas = new HashSet(0);
     @Ignore
     private Set horarios = new HashSet(0);

    public Empleado() {
    }

    public Empleado(Integer id) {
        this.id = id;
    }
	
    public Empleado(Ciudad ciudad, Puesto puesto, Sede sede, String nombre, String apellido1, String dni, String direccion, String email, String pass, double sueldo) {
        this.ciudad = ciudad;
        this.puesto = puesto;
        this.sede = sede;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.dni = dni;
        this.direccion = direccion;
        this.email = email;
        this.pass = pass;
        this.sueldo = sueldo;
    }
    public Empleado(Ciudad ciudad, Puesto puesto, Sede sede, String nombre, String apellido1, String apellido2, String dni, String direccion, String email, String pass, double sueldo, Set tareas, Set horarios) {
       this.ciudad = ciudad;
       this.puesto = puesto;
       this.sede = sede;
       this.nombre = nombre;
       this.apellido1 = apellido1;
       this.apellido2 = apellido2;
       this.dni = dni;
       this.direccion = direccion;
       this.email = email;
       this.pass = pass;
       this.sueldo = sueldo;
       this.tareas = tareas;
       this.horarios = horarios;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
    
    
    public Ciudad getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    public Puesto getPuesto() {
        return this.puesto;
    }
    
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }
    public Sede getSede() {
        return this.sede;
    }
    
    public void setSede(Sede sede) {
        this.sede = sede;
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
    public String getDni() {
        return this.dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
    public double getSueldo() {
        return this.sueldo;
    }
    
    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }
    public Set getTareas() {
        return this.tareas;
    }
    
    public void setTareas(Set tareas) {
        this.tareas = tareas;
    }
    public Set getHorarios() {
        return this.horarios;
    }
    
    public void setHorarios(Set horarios) {
        this.horarios = horarios;
    }




}


