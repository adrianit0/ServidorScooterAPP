package entidades;


import java.util.HashSet;
import java.util.Set;


public class Horario  implements java.io.Serializable {


     private Integer id;
     private int diaSemana;
     private int horaInicio;
     private int horaFin;
     private Set empleados = new HashSet(0);

    public Horario() {
    }

	
    public Horario(int diaSemana, int horaInicio, int horaFin) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    public Horario(int diaSemana, int horaInicio, int horaFin, Set empleados) {
       this.diaSemana = diaSemana;
       this.horaInicio = horaInicio;
       this.horaFin = horaFin;
       this.empleados = empleados;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public int getDiaSemana() {
        return this.diaSemana;
    }
    
    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }
    public int getHoraInicio() {
        return this.horaInicio;
    }
    
    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }
    public int getHoraFin() {
        return this.horaFin;
    }
    
    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }
    public Set getEmpleados() {
        return this.empleados;
    }
    
    public void setEmpleados(Set empleados) {
        this.empleados = empleados;
    }




}


