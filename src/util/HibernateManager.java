/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author agarcia.gonzalez
 */
public class HibernateManager {

    private List getObjectsCriterio(String tabla, Map<String, String> criterios, String condicional) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List objetos = null;

        String condicion = " WHERE ";
        int size = criterios.size();
        int actual = 0;

        for (Map.Entry<String, String> entry : criterios.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            condicion += key + "= '" + value + "'";

            actual++;

            if (actual != size) {
                condicion += " "+condicional+" ";
            }
        }

        try {
            tx = session.beginTransaction();
            objetos = session.createQuery("FROM " + tabla + condicion).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objetos;
    }
    
    public List getObjectsCriterio(String tabla, Map<String, String> criterios) {
        return getObjectsCriterio (tabla, criterios, "AND");
    }
    
    public List getObjectsCriterioOR (String tabla, Map<String,String> criterios) {
        return getObjectsCriterio (tabla, criterios, "OR");
    }

    public Object getObjectCriterio(String tabla, Map<String, String> criterios) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Object objeto = null;

        String condicion = " WHERE ";
        int size = criterios.size();
        int actual = 0;

        for (Map.Entry<String, String> entry : criterios.entrySet()) {
            String key = entry.getKey();
            condicion += key + "=:param" + actual;
            actual++;
            if (actual != size) {
                condicion += " AND ";
            }
        }

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM " + tabla + condicion);
            actual=0;
            for (Map.Entry<String, String> entry : criterios.entrySet()) {
                String value = entry.getValue();
                query.setString("param"+actual, value);
                actual++;
            }
            objeto = query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objeto;
    }

    public List getObjects(String tabla) {
        return getObjects(tabla, "");
    }
    
    public List getObjects (String tabla, String consulta) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List objetos = null;

        try {
            tx = session.beginTransaction();
            objetos = session.createQuery("FROM " + tabla + " " + consulta).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objetos;
    }
    
    public List getObjectsWithoutLazyObjects (String tabla, String consulta, String... methods) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List objetos = null;

        try {
            tx = session.beginTransaction();
            objetos = session.createQuery("FROM " + tabla + " " + consulta).list();
            
            for (Object o : objetos) {
                // Forzamos a Hibernate a ejecutar ciertos métodos para tomarlo del servidor
                for (String m : methods) {
                    Method meth = o.getClass().getMethod(m);
                    Object t = meth.invoke(o);
                    
                    if (t!=null) 
                        t.toString();
                }
            }
            tx.commit();
        } catch (HibernateException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objetos;
    }

    public Object getObject(Class clase, Integer primaryKey) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Object objeto = null;

        try {
            tx = session.beginTransaction();
            objeto = session.get(clase, primaryKey);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objeto;
    }
    
    public Object getObjectWithoutLazyObjects(Class clase, Integer primaryKey, String... methods) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Object objeto = null;

        try {
            tx = session.beginTransaction();
            objeto = session.get(clase, primaryKey);
            
            // Forzamos a Hibernate a ejecutar ciertos métodos para tomarlo del servidor
            for (String m : methods) {
                Method meth = objeto.getClass().getMethod(m);
                Object t = meth.invoke(objeto);
                t.toString();
            }
            
            tx.commit();
        } catch (HibernateException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }

        return objeto;
    }

    public Integer addObject(Object objeto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Integer objectID = null;

        try {
            tx = session.beginTransaction();
            objectID = (Integer) session.save(objeto);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objectID;
    }

    public void addObjects(Object... objetos) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            for (Object o : objetos) {
                session.save(o);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    /**
     * Actualiza el contenido de un objeto que exista en la tabla.
     */
    public boolean updateObject (Object object){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null; 
        
        try{ 
            tx = session.beginTransaction(); 
            session.update(object);
            tx.commit();
            return true;
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
            return false;
        }finally { 
            session.close();
        } 
    }
    
    /**
     * Elimina un objeto de la base de datos que exista en la tabla
     */
    public boolean deleteObject (Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession(); 
        Transaction tx = null;
        try{
            tx = session.beginTransaction(); 
            //Empleado employee = (Empleado)session.get(Empleado.class, EmployeeID);
            session.delete(object);
            tx.commit();
            return true;
        }catch (HibernateException e) { 
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return false;
        }finally {
            session.close(); 
        }
    }

}
