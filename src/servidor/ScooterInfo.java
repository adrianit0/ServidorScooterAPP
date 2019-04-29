/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * Contiene la información del cliente que tiene a partir de su token.
 * 
 * @author Adrián García
 */
public class ScooterInfo {
    private long id;
    private String matricula;
    private int codigo;

    public ScooterInfo(long id, String matricula, int codigo) {
        this.id = id;
        this.matricula = matricula;
        this.codigo = codigo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
