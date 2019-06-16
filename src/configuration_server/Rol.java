/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration_server;

/**
 *
 * @author agarcia.gonzalez
 */
public enum Rol {
    CLIENTE("cliente"), EMPLEADO("tecnico", "empleado", "trabajador"), ADMINISTRADOR("administrador", "admin"), SCOOTER("scooter", "moto"), DIRECTIVO("directivo");
    
    private String[] nombres;
    
    Rol (String... nombres) {
        this.nombres = nombres;
    }
    
    public boolean esRole (String rol) {
        for(String n : nombres) {
            if (n.equalsIgnoreCase(rol))
                return true;
        }
        return false;
    }
}