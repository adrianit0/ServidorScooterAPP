
package servidorbd;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase para abrir facilmente un flujo entre la aplicación y la BD
 * 
 * 
 * @author adrian
 */
public class ConexionBBDD {
    
    enum SGBD { 
        auto,
        MySQL,
        SQLite3,
        ApacheDerby,
        HSQLDB,
        H2,
        DB04
        /* INSERTAR EL RESTO:
            [ ] SQLite3
            [ ] Apache Derby
            [ ] HSQLDB
            [ ] H2
            [ ] DB04
            [X] MySQL
        */ 
    };
    
    // Valor de la consulta
    private String consulta;
    private String[] valores;
    
    private String driver = "com.mysql.jdbc.Driver";
    
    private String preUrl = "jdbc:mysql:";
    private String url = "//localhost:3306/";
    
    private String user = "root"; 
    private String password = "root";
    private String bbdd = "scooterapp";
    
    private Connection connection=null;
    private CallableStatement callConnection=null;
    
    public ConexionBBDD () {
        this ("", "");
    }
    
    public ConexionBBDD (String comando, String bbdd, String... valores) {
        this.bbdd = bbdd;
        this.consulta = comando;
        this.valores = valores;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection==null) {
            connection = DriverManager.getConnection(getURL (), user, password);
        }
        
        return connection;
    }
    
    // Realiza un test de conexión
    public void test () {
        try {
            Connection c = getConnection();
        } catch (SQLException e) {
            System.out.println("ERROR: "+e.getMessage());
        }
    }
    
    public ResultSet realizarQuery () {
        // Al realizar el query cargamos el driver
        // Si nos falla por alguna razón permitiremos que el programa continue
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Servidor::realizarQuery ERROR: " + e.getMessage());
            
        }
        
        // Ahora realizamos la conexión 
        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(consulta);
            
            for (int i = 1; i <= valores.length; i++) {
                ps.setString(i, valores[i-1]);
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("No se ha podido realizar la consulta:, ERROR: " + e.getMessage());
        }
        
        return null;
    }
    
    public int realizarUpdate () {
        // Al realizar el query cargamos el driver
        // Si nos falla por alguna razón permitiremos que el programa continue
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        
        // Ahora realizamos la conexión 
        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(consulta);
            
            for (int i = 1; i <= valores.length; i++) {
                ps.setString(i, valores[i-1]);
            }
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("No se ha podido realizar la solicitud:, ERROR: " + e.getMessage());
            e.printStackTrace();
        } 
        
        return -1;
    }
    
    public ResultSet realizarProcedure() {
        // Al realizar el query cargamos el driver
        // Si nos falla por alguna razón permitiremos que el programa continue
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        
        // Ahora realizamos la conexión 
        try {
            connection = getConnection();
            CallableStatement call = connection.prepareCall(consulta);
            
            for (int i = 1; i <= valores.length; i++) {
                call.setString(i, valores[i-1]);
            }
            return call.executeQuery();
        } catch (SQLException e) {
            System.err.println("No se ha podido realizar la consulta:, ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public int realizarFunction () {
        // Al realizar el query cargamos el driver
        // Si nos falla por alguna razón permitiremos que el programa continue
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        
        // Ahora realizamos la conexión 
        try {
            connection = getConnection();
            CallableStatement call = connection.prepareCall(consulta);
            
            for (int i = 1; i <= valores.length; i++) {
                call.setString(i, valores[i-1]);
            }

            return call.executeUpdate();
        } catch (SQLException e) {
            System.err.println("No se ha podido realizar la solicitud:, ERROR: " + e.getMessage());
            e.printStackTrace();
        } 
        
        return -1;
    }
    
    public CallableStatement getCallable () {
        return callConnection;
    }
    
    public DatabaseMetaData getMetaData () {
        try {
            Connection c = getConnection();
            
            return c.getMetaData();
        } catch (SQLException e) {
            System.out.println("Servidor::getMetadaData ERROR: " +e.getMessage());
        }
        
        return null;
    }
    
    public void close() throws SQLException {
        if (connection!=null)
            connection.close();
        
        if (callConnection!=null)
            callConnection.close();
    }
    
    /**
     * Cierra sin propagar el error
     */
    public boolean closeWithoutError() {
        try {
            close();
            return false;
        } catch(SQLException e) {
            return true;
        }
    }
    /**
     * Cambia el drive del servidor, debe usarse antes de realizar la consulta
     */
    public void changeDrive (String driver, String preUrl, String url) {
        this.driver = driver;
        this.preUrl = preUrl;
        this.url = url;
    }
    
    public void changeDrive (SGBD sgbd) {
        switch (sgbd) {
            case MySQL:
                driver = "com.mysql.jdbc.Driver";
                preUrl = "jdbc:mysql:";
                break;
            default:
                System.out.println("SGBD no soportado aún");
        }
    }
    
    public void changeUserPass (String user, String pass) {
        this.user = user;
        this.password = pass;
    }
    
    public String getURL () {
        return preUrl + url + bbdd;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public String[] getValores() {
        return valores;
    }

    public void setValores(String... valores) {
        this.valores = valores;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
