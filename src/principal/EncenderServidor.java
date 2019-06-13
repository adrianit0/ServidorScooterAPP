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