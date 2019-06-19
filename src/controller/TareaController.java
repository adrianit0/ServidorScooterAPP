/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import configuration_server.GenericController;
import entidades.Empleado;
import entidades.Estadotarea;
import entidades.Incidencia;
import entidades.Mantenimiento;
import entidades.Scooter;
import entidades.Tarea;
import entidades.Tipotarea;
import excepciones.ServerExecutionException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import servidor.ClienteInfo;
import util.Enums.*;
import util.HibernateManager;
import util.Util;

/**
 *
 * @author Adrián
 */
public class TareaController extends GenericController {
    
    public Map<String,String> generarTareasAutomaticamente () throws ServerExecutionException {
        
        // Integer -> ID de la scooter.
        // Mantenimiento -> mantenimiento creado. 
        Map<Integer,Mantenimiento> mantenimientosCreados = new HashMap<>();
        
        List<Incidencia> incidenciasAbiertas = getMh().getObjectsWithoutLazyObjects("Incidencia", "WHERE mantenimiento IS NULL", "getTipoincidencia", "getScooter");
        if (incidenciasAbiertas==null || incidenciasAbiertas.isEmpty())
            throw new ServerExecutionException ("No hay tareas disponibles");
        
        // Seleccionar una hora correcta en lugar de la actual para el mantenimiento
        Date fechaInicio = new Date(System.currentTimeMillis());
        
        // Por defecto la fecha fin seran 8h despues de la fecha inicio
        Long tiempoMantenimiento = fechaInicio.getTime() + 28800000;
        Date fechaFin = new Date(tiempoMantenimiento);
        
        // Todas las tareas serán asignadas al administrador a espera que sean asignadas a otros trabajadores
        Empleado empleado = (Empleado) getMh().getObject(Empleado.class, 1);
        
        // Estimacion inicial
        Integer estimacionInicial = 30;
        
        for (Incidencia i : incidenciasAbiertas) {
            Scooter scooter = i.getScooter();
            if (!mantenimientosCreados.containsKey(scooter.getId())) {
                Mantenimiento mantenimiento = new Mantenimiento();
                mantenimiento.setFechaInicio(fechaInicio);
                mantenimiento.setFechaFin(fechaFin);
                mantenimiento.setScooter(scooter);
                Integer idMantenimiento = getMh().addObject(mantenimiento);
                mantenimiento.setId(idMantenimiento);
                
                mantenimientosCreados.put(scooter.getId(), mantenimiento);
                
                // Creamos la tarea a asignar, se la daremos al administrador, y luego se reparten entre los trabajadores con el otro método.
                Tarea tarea = generarTarea (TipoTareaEnum.LLEVAR_SCOOTER_TALLER, null);
                tarea.setEmpleado(empleado);
                tarea.setObservaciones("");
                tarea.setNombre("["+idMantenimiento+"] Mantenimiento para la Scooter " + scooter.getNoSerie());
                tarea.setMantenimiento(mantenimiento);
                
                Integer idTarea = getMh().addObject(tarea);
            }
            
            i.setMantenimiento(mantenimientosCreados.get(scooter.getId()));
            getMh().updateObject(i);
        }
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        
        return resultado;
    }
    
    public Map<String,String> asignarTareasAutomaticamente () throws ServerExecutionException {
        
        HibernateManager hm = this.getHManager();
        
        List<Tarea> tareasSinAsignar = hm.getObjectsWithoutLazyObjects("Tarea", "WHERE empleado.id=1 AND estadotarea.id=1", "getEstadotarea");
        
        if (tareasSinAsignar==null || tareasSinAsignar.isEmpty()) 
            throw new ServerExecutionException("No hay tareas para asignar.");
        
        Map<String,String> criterios = new HashMap<>();
        criterios.put("puesto.id", "3");
        List<Empleado> tecnicosRecogida = hm.getObjectsCriterio("Empleado", criterios);
        
        criterios.put("puesto.id", "4");
        List<Empleado> tecnicosMantenimiento = hm.getObjectsCriterio("Empleado", criterios);
        
        criterios.put("puesto.id", "5");
        List<Empleado> tecnicosMecanicos = hm.getObjectsCriterio("Empleado", criterios);
        
        
        for (Tarea t : tareasSinAsignar) {
            Integer id = t.getTipotarea().getId();
            Empleado tecnico = null;
            int i = 0;
            
            switch (id) {
                case 1:
                case 2:
                    if (tecnicosRecogida==null || tecnicosRecogida.isEmpty()) 
                        throw new ServerExecutionException("No hay suficientes tecnicos de recogida.");
                    tecnico=tecnicosRecogida.get(i%tecnicosRecogida.size());
                    break;
                case 3:
                case 4:
                    if (tecnicosMantenimiento==null || tecnicosMantenimiento.isEmpty()) 
                        throw new ServerExecutionException("No hay suficientes tecnicos de mantenimiento.");
                    tecnico=tecnicosMantenimiento.get(i%tecnicosMantenimiento.size());
                    break;
                case 5:
                case 6:
                case 7:
                    if (tecnicosMecanicos==null || tecnicosMecanicos.isEmpty()) 
                        throw new ServerExecutionException("No hay suficientes tecnicos mecánicos.");
                    tecnico=tecnicosMecanicos.get(i%tecnicosMecanicos.size());
                    break;
                    
                default:
                    //No pasa nada
            }
            
            t.setEmpleado(tecnico);
            hm.updateObject(t);
            
            i++;
        }
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        return resultado;
    }
    
    public Map<String,String> cogerTareasDelServidor (Map<String,String> parametros) throws ServerExecutionException {
        String token = parametros.get("token");
        ClienteInfo info = getServer().getClient(token);
        if (info==null) 
            throw new ServerExecutionException("No hay usuario conectado.");
        
        List<Tarea> tareas = getMh().getObjectsWithoutLazyObjects("Tarea", "WHERE empleado.id=" + info.getId() + " AND estadotarea.id=1", "getMantenimiento", "getEstadotarea", "getTipotarea");
        Map<String,String> result = util.Util.convertListToMap(tareas);
        
        int i = 0;
        result.put("length", tareas.size()+"");
        for (Tarea t : tareas) {
            Mantenimiento m = (Mantenimiento) getMh().getObjectWithoutLazyObjects(Mantenimiento.class, t.getMantenimiento().getId(), "getScooter");
            Scooter scooter = (Scooter) getMh().getObject(Scooter.class, m.getScooter().getId());
           
            Map<String,String> contenidoTarea = util.Util.convertObjectToMap(t, "[" + i + "]");
            Map<String,String> contenidoScooter = util.Util.convertObjectToMap(scooter, "[" + i + "]");
            
            result.putAll(contenidoScooter);
            result.putAll(contenidoTarea);
            result.put("tipoTareaId[" + i + "]", t.getTipotarea().getId().toString());
            result.put("estadoTarea[" + i + "]", t.getEstadotarea().getId().toString());
            result.put("tipoTarea[" + i + "]", t.getTipotarea().getNombre());
            result.put("fechaAsignacion[" + i + "]", t.getFechaAsignacion().getTime()+"");
            
            i++;
        }
        
        return result;
    }
    
    public Map<String,String> cogerTodasLasTareasDelServidor () throws ServerExecutionException {
        List<Tarea> tareas = getMh().getObjects("Tarea");
        if (tareas==null||tareas.isEmpty())
            throw new ServerExecutionException("No hay tareas en el servidor");
        Map<String,String> result = util.Util.convertListToMap(tareas);
        int i=0;
        for (Tarea t : tareas) {
            result.put("fechaAsignacion["+i+"]", t.getFechaAsignacion().getTime()+"");
            i++;
        }
        return result;
    }
    
    public Map<String, String> getTarea(Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        Tarea tarea = (Tarea) this.getHManager().getObjectWithoutLazyObjects(Tarea.class, id, "getTipotarea", "getEstadotarea", "getEmpleado");
        
        if (tarea==null) 
            throw new ServerExecutionException ("No se ha encontrado la tarea con ID " + id);
        
        Map<String, String> result = util.Util.convertObjectToMap(tarea);
        result.put("tipoTareaId", tarea.getTipotarea().getId()+"");
        result.put("empleadoId", tarea.getEmpleado().getId()+"");
        result.put("estadoTareaId", tarea.getEstadotarea().getId()+"");
        result.put("fechaAsignacion", tarea.getFechaAsignacion().getTime()+"");
        return result;
    }
    
    public Map<String,String> enviarParteTarea (Map<String,String> parametros) throws ServerExecutionException {
        String token = parametros.get("token");
        ClienteInfo info = getServer().getClient(token);
        if (info==null) 
            throw new ServerExecutionException("No hay usuario conectado.");
        
        Integer id = Integer.parseInt(parametros.get("id"));
        String observaciones = parametros.get("observaciones");
        if (observaciones==null)
            observaciones="";
        boolean opcion = parametros.get("opcion").equals("si");
        
        Tarea tarea = (Tarea) getMh().getObjectWithoutLazyObjects(Tarea.class, id, "getEstadotarea", "getMantenimiento");
        if(tarea.getEstadotarea().getId()!=1) {
            switch (tarea.getEstadotarea().getId()) {
                case 2: throw new ServerExecutionException("Esta tarea ha sido aplazada, intentelo más tarde.");
                case 3: throw new ServerExecutionException("Esta tarea ha sido transferia a otro empleado.");
                case 4: throw new ServerExecutionException("Esta tarea ya ha sido finalizada.");
                case 5: throw new ServerExecutionException("Esta tarea no se puede finalizar.");
                case 6: throw new ServerExecutionException("Esta tarea ha sido cancelada.");
            }
        }
        tarea.setObservaciones(observaciones);
        tarea.setEstadotarea(new Estadotarea(4));
        
        Tarea nuevaTarea = generarSiguienteTarea(tarea, opcion);
        
        boolean actualizado = getMh().updateObject(tarea);
        if (!actualizado)
            throw new ServerExecutionException("No se ha podido completar la tarea");
        
        if (nuevaTarea!=null) {
            Integer idNuevaTarea = getMh().addObject(nuevaTarea);
            
            if (idNuevaTarea==null)
                throw new ServerExecutionException("No se ha podido crear la nueva tarea.");
        }
        
        Map<String,String> resultado = new HashMap<>();
        resultado.put("status", "ok");
        return resultado;
    }
    
    // Genera una tarea a partir del tipo que sea
    private Tarea generarTarea (TipoTareaEnum tipoTarea, Tarea oldTarea) {
        Tarea tarea = new Tarea();
        tarea.setFechaAsignacion(new Date(System.currentTimeMillis()));
        tarea.setEstadotarea(new Estadotarea(1)); // Empieza la tarea como asignada
        
        if (oldTarea!=null) {
            tarea.setMantenimiento(oldTarea.getMantenimiento());
            tarea.setObservaciones(oldTarea.getObservaciones());
            tarea.setNombre(oldTarea.getNombre());
            
            // Si la siguiente tarea la hace la persona con mismo rol lo hará la misma persona
            int idTarea = tipoTarea.getId();
            if (tipoTarea.getId()==idTarea) {
                tarea.setEmpleado(oldTarea.getEmpleado());
            }
        }
        
        // 30 minutos base
        int estimacionMinutos = 30;
        
        // Hacer los cambios pertinentes según el tipo de tarea que sea
        // Solo si es necesario
        switch (tipoTarea) {
            case LLEVAR_SCOOTER_TALLER:
                break;
            case DEVOLVER_SCOOTER_CALLE:
                break;
            case CAMBIAR_BATERIAS:
                estimacionMinutos = 10;
                break;
            case APARCAR_BIEN:
                break;
            case COMPROBAR_FUNCIONA:
                break;
            case REPARAR_SCOOTER:
                estimacionMinutos = 120;
                break;
            case ESTIMAR_GASTOS:
                break;
            default:
                break;
        }
        
        
        tarea.setTipotarea(new Tipotarea(tipoTarea.getId()));
        tarea.setEstimacion(estimacionMinutos);
        
        return tarea;
    }
    
    // Genera la siguiente tarea a partir del ciclo de vida del mantenimiento de la moto
    private Tarea generarSiguienteTarea (Tarea oldTarea, boolean terminadoCorrectamente) {
        TipoTareaEnum tipoTarea = TipoTareaEnum.getById(oldTarea.getTipotarea().getId());
        TipoTareaEnum nuevaTarea = null;
        switch (tipoTarea) {
            case LLEVAR_SCOOTER_TALLER:
                nuevaTarea = TipoTareaEnum.COMPROBAR_FUNCIONA;
                break;
            case DEVOLVER_SCOOTER_CALLE:
                // DAR EL MANTENIMIENTO COMO TERMINADO
                break;
            case CAMBIAR_BATERIAS:
                if (terminadoCorrectamente)
                    nuevaTarea = TipoTareaEnum.DEVOLVER_SCOOTER_CALLE;
                // SI LA SCOOTER ESTUVIERA YA EN LA CALLE, DAR COMO TERMINADO
                break;
            case APARCAR_BIEN:
                nuevaTarea = TipoTareaEnum.CAMBIAR_BATERIAS;
                break;
            case COMPROBAR_FUNCIONA:
                nuevaTarea = terminadoCorrectamente ? TipoTareaEnum.CAMBIAR_BATERIAS : TipoTareaEnum.REPARAR_SCOOTER;
                break;
            case REPARAR_SCOOTER:
                nuevaTarea = terminadoCorrectamente ? TipoTareaEnum.CAMBIAR_BATERIAS : TipoTareaEnum.ESTIMAR_GASTOS;
                break;
            case ESTIMAR_GASTOS:
                if (terminadoCorrectamente) 
                    nuevaTarea = TipoTareaEnum.REPARAR_SCOOTER;
                else {
                    // Si no es rentable economicamente arreglar la scooter la damos de baja
                    Mantenimiento m = (Mantenimiento) getMh().getObjectWithoutLazyObjects(Mantenimiento.class, oldTarea.getMantenimiento().getId(), "getScooter");
                    Scooter scooter = (Scooter) getMh().getObject(Scooter.class, m.getScooter().getId());
                    
                    scooter.setFechaBaja(new Date(System.currentTimeMillis()));
                    getMh().updateObject(scooter);
                }
                break;
        }
        
        if (nuevaTarea==null)
            return null;
        
        return generarTarea (nuevaTarea, oldTarea);
    }
    
    // CRUD TAREA
    public Map<String,String> createTarea (Map<String,String> parametros) throws ServerExecutionException {
        Tarea tarea = (Tarea) util.Util.convertMapToObject(Tarea.class, parametros);
        
        HibernateManager hm = this.getHManager();
        
        Integer estadoTareaId = Util.parseInt(parametros.get("estadoTareaId"));
        Integer tipoTareaId = Util.parseInt(parametros.get("tipoTareaId"));
        Integer empleadoId = Util.parseInt(parametros.get("empleadoId"));
        Long fechaAsignacion = Long.parseLong(parametros.get("fechaAsignacion"));
        
        Estadotarea estadoTarea = (Estadotarea) hm.getObject(Estadotarea.class, estadoTareaId);
        Tipotarea tipoTarea = (Tipotarea) hm.getObject(Tipotarea.class, tipoTareaId);
        Empleado empleado = (Empleado) hm.getObject(Empleado.class, empleadoId);
        
        tarea.setEstadotarea(estadoTarea);
        tarea.setTipotarea(tipoTarea);
        tarea.setEmpleado(empleado);
        tarea.setFechaAsignacion(new Date(fechaAsignacion));
        
        Integer id = hm.addObject(tarea);
        
        if (id==-1)
            throw new ServerExecutionException ("No se ha podido crear la tarea", parametros);
        
        
        tarea.setId(id);
        Map<String,String> result = util.Util.convertObjectToMap(tarea);
        return result;
    }
    
    public Map<String,String> updateTarea (Map<String,String> parametros) throws ServerExecutionException {
        Tarea tarea = (Tarea) util.Util.convertMapToObject(Tarea.class, parametros);
        
        HibernateManager hm = this.getHManager();
        Tarea lastTarea = (Tarea) hm.getObjectWithoutLazyObjects(Tarea.class, tarea.getId(), "getEstadotarea", "getTipotarea", "getEmpleado");
        
        Integer estadoTareaId = Util.parseInt(parametros.get("estadoTareaId"));
        Integer tipoTareaId = Util.parseInt(parametros.get("tipoTareaId"));
        Integer empleadoId = Util.parseInt(parametros.get("EmpleadoId"));
        Long fechaAsignacion = Long.parseLong(parametros.get("fechaAsignacion"));
        Date fechaA = new Date(fechaAsignacion);
        
        if (tarea.getObservaciones()==null) 
            tarea.setObservaciones(lastTarea.getObservaciones());
        
        tarea.setEstadotarea(estadoTareaId!=null? new Estadotarea(estadoTareaId) : lastTarea.getEstadotarea());
        tarea.setTipotarea(tipoTareaId!=null? new Tipotarea(tipoTareaId) : lastTarea.getTipotarea());
        tarea.setEmpleado(empleadoId!=null? new Empleado(empleadoId) : lastTarea.getEmpleado());
        tarea.setFechaAsignacion(fechaA);
        tarea.setMantenimiento(lastTarea.getMantenimiento());
        
        Boolean editado = hm.updateObject(tarea);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido editar la tarea", parametros);
        
        Map<String,String> result = util.Util.convertObjectToMap(tarea);
        return result;
    }
    
    public Map<String,String> deleteTarea (Map<String,String> parametros) throws ServerExecutionException {
        Integer id = Integer.parseInt(parametros.get("id"));
        
        HibernateManager hm = this.getHManager();
        Tarea tarea = (Tarea) hm.getObject(Tarea.class, id);
        Boolean editado = hm.deleteObject(tarea);
        
        if (!editado)
            throw new ServerExecutionException ("No se ha podido eliminar la tarea", parametros);
        
        Map<String,String> result = new HashMap<>();
        result.put("status", "ok");
        return result;
    }
}