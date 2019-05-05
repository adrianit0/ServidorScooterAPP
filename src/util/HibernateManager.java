/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author agarcia.gonzalez
 */
public class HibernateManager {

    public List getObjectsCriterio(String tabla, Map<String, String> criterios) {
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
                condicion += " AND ";
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List objetos = null;

        try {
            tx = session.beginTransaction();
            objetos = session.createQuery("FROM " + tabla).list();
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

}
