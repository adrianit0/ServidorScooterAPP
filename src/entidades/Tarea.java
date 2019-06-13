package entidades;

import java.util.Date;


public class Tarea  implements java.io.Serializable {


     private Integer id;
     private Empleado empleado;
     private Estadotarea estadotarea;
     private Tipotarea tipotarea;
     private String nombre;
     private Date fechaAsignacion;
     private int estimacion;

    public Tarea() {
    }

    public Tarea(Empleado empleado, Estadotarea estadotarea, Tipotarea tipotarea, String nombre, Date fechaAsignacion, int estimacion) {
       this.empleado = empleado;
       this.estadotarea = estadotarea;
       this.tipotarea = tipotarea;
       this.nombre = nombre;
       this.fechaAsignacion = fechaAsignacion;
       this.estimacion = estimacion;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Empleado getEmpleado() {
        return this.empleado;
    }
    
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    public Estadotarea getEstadotarea() {
        return this.estadotarea;
    }
    
    public void setEstadotarea(Estadotarea estadotarea) {
        this.estadotarea = estadotarea;
    }
    public Tipotarea getTipotarea() {
        return this.tipotarea;
    }
    
    public void setTipotarea(Tipotarea tipotarea) {
        this.tipotarea = tipotarea;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Date getFechaAsignacion() {
        return this.fechaAsignacion;
    }
    
    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    public int getEstimacion() {
        return this.estimacion;
    }
    
    public void setEstimacion(int estimacion) {
        this.estimacion = estimacion;
    }




}


