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
public class Especialidad {
    public static int contador = 0;
    private int id;
    private String nombre;

    public Especialidad(){
         this.id = 0;
         this.nombre = null;
    }
    public Especialidad( String nombre) {
        contador++;
        this.id = contador;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
