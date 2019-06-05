/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *Clase base con los atributos tipicos de una persona normal
 * @author Efigenia
 */
public class Persona {
    protected String cedula;
    protected String nombre;
    protected String apellido;
    protected String nContacto;
    protected String cElectronico;

      //Constructores
    public Persona(String cedula, String nombre, String apellido, String nContacto, String cElectronico) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nContacto = nContacto;
        this.cElectronico = cElectronico;
    }
    
     public Persona(String cedula, String nombre, String apellido) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nContacto = "";
        this.cElectronico = "";
    }
     
    //Setter y Getter
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getnContacto() {
        return nContacto;
    }

    public void setnContacto(String nContacto) {
        this.nContacto = nContacto;
    }

    public String getcElectronico() {
        return cElectronico;
    }

    public void setcElectronico(String cElectronico) {
        this.cElectronico = cElectronico;
    }

    
  

}
