/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import util.HibernateManager;

/**
 *
 *  Controlador generico del cual deberá extender el resto de controladores.
 * 
 * @author agarcia.gonzalez
 */
public class GenericController {
    
    private HibernateManager mh = new HibernateManager();

    public HibernateManager getHManager() {
        return mh;
    }
}
