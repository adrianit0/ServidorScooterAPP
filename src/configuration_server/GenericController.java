/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

import servidor.ScooterServerTCP;
import util.HibernateManager;

/**
 *
 *  Controlador generico del cual deberá extender el resto de controladores.
 * 
 * @author agarcia.gonzalez
 */
public class GenericController {
    
    private HibernateManager mh = new HibernateManager();
    private ScooterServerTCP server;

    public HibernateManager getHManager() {
        return mh;
    }

    public HibernateManager getMh() {
        return mh;
    }

    public void setMh(HibernateManager mh) {
        this.mh = mh;
    }

    public ScooterServerTCP getServer() {
        return server;
    }

    public void setServer(ScooterServerTCP server) {
        this.server = server;
    }
}
