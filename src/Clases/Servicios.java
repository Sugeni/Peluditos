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
public class Servicios {
    public static int contador = 0;
    private  int id;
    private String nombre;
    private float costo;

    public Servicios(String nombre, float costo) {
        contador++;        
        this.nombre = nombre;
        this.costo = costo;
        this.id = contador;
    }
    
    public Servicios() {      
        this.nombre = null;
        this.costo = 0;
        this.id = 0;
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

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
}
