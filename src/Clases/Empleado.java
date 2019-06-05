/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Utilidades.GeneradorPassword;

/**
 *
 * @author Efigenia
 */
public class Empleado extends Persona {
    protected int tEmpleado;
    protected float sueldo;
    protected String pass = null;
    
    public Empleado(int tEmpleado, float sueldo, String cedula, String nombre, String apellido) {
        super(cedula, nombre, apellido);
        this.tEmpleado = tEmpleado;
        this.sueldo = sueldo;
    }
    

    public Empleado(int tEmpleado, float sueldo, String cedula, String nombre, String apellido, String nContacto, String cElectronico) {
        super(cedula, nombre, apellido, nContacto, cElectronico);
        this.tEmpleado = tEmpleado;
        this.sueldo = sueldo;

    }
    
     //Setter y Getter
    public int gettEmpleado() {
        return tEmpleado;
    }

    public void setTEmpleado(int tEmpleado) {
        this.tEmpleado = tEmpleado;
    }

    public float getSueldo() {
        return sueldo;
    }

    public void setSueldo(float sueldo) {
        this.sueldo = sueldo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    
    public Factura FacturarServicio (String idCliente){

     Factura actual = new Factura(idCliente, this.cedula) ;
        return actual;
    
    }

}
