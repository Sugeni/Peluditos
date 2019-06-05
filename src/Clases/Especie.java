/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Efigenia
 */
public class Especie {
    static  public int contador = 0;
    private int id;
    private String nombre;

    public Especie(String nombre) {
        this.nombre = nombre;
        Especie.contador++;
        id = contador;
    }
    
     public Especie(){
         this.id = 0;
         this.nombre = null;
    }
     public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

}
