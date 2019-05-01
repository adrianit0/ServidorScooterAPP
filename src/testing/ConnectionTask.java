/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

/**
 *
 * @author agarcia.gonzalez
 */
public interface ConnectionTask <T, V> {
    
    public abstract V onStart (T... argumentos);
    
    public abstract void onFinish (V result);
}
