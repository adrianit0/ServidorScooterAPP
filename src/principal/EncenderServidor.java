/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.IOException;
import servidor.ScooterServerTCP;

/**
 *
 * @author agarcia.gonzalez
 */
public class EncenderServidor {
    public static void main(String[] args) throws IOException  {
        (new ScooterServerTCP(4444)).start();
    }
}
