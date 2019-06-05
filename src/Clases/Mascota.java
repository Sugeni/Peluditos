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
public class Mascota {

    //Atributos
    static  public int contador = 0;
    private  int id;
    private String nombre;
    private String raza;
    private int year, mes;
    private boolean Estado; //Vivo o Muerto
            

    //Clave Foranea
    private int idEspecie;
    private String idCliente;
    
    
    //Constructor
    public Mascota( String nombre, String raza, int year,int mes , boolean Estado, int idEspecie, String idCliente) {
        this.nombre = nombre;
        this.raza = raza;
        this.year = year;
        this.mes  = mes;
        this.Estado = Estado;
        this.idEspecie = idEspecie;
        this.idCliente = idCliente;
        Mascota.contador++;
        id = contador;
        
    }
    
    
    //Setter y Getters
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

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }


    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean Estado) {
        this.Estado = Estado;
    }

    public int getIdEspecie() {
        return idEspecie;
    }

    public void setIdEspecie(int idEspecie) {
        this.idEspecie = idEspecie;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    
}
