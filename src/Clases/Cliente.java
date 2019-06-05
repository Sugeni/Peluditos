/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 * Cliente: Persona que posee una mascota y solicita un servicio
 * @author Efigenia
 */
public class Cliente extends Persona{
    //Juridico o Natural
    protected int tCliente;

    //Constructores
    public Cliente(int tCliente, String cedula, String nombre, String apellido, String nContacto, String cElectronico) {
        super(cedula, nombre, apellido, nContacto, cElectronico);
        this.tCliente = tCliente;
    }

    public Cliente(int tCliente, String cedula, String nombre, String apellido) {
        super(cedula, nombre, apellido);
        this.tCliente = tCliente;
    }

    //Setter y Getter
    public int gettCliente() {
        return tCliente;
    }

    public void settCliente(int tCliente) {
        this.tCliente = tCliente;
    }
    
    public String obtenerCliente(){
        if( this.tCliente == 1){
            return "Natural" ;
        }
        else
            return "Juridico";
    }
    
}
