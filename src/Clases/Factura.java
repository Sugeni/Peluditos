/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Utilidades.Calendario;
import java.util.ArrayList;

/**
 *
 * @author Efigenia
 */
public class Factura {
    
    static public int contador = 0;
    private int id;
    ArrayList<Float> costos = new ArrayList<Float>();
    
    private final String fecha;
    private final String hora;
    
    //Claves Foraneas
    private String idCliente;
    private String idEmpleado;
    
    ArrayList<Integer> servicios= new ArrayList<Integer>();
    
    
    public Factura(String idCliente, String idEmpleado, Servicios costo) {   
      
        contador++;
        servicios = new ArrayList<>();
        costos = new ArrayList<> ();
        
        Calendario fechaA = new Calendario();
        this.id = contador;        
        
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        
        this.addServicio(costo);
        
        this.fecha = fechaA.getFecha();
        this.hora = fechaA.getHora();
        
    }

    public Factura(String idCliente, String idEmpleado) {
     
        contador++;
        
        this.id = contador;        
        
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
       
        Calendario fechaA = new Calendario();
       this.fecha = fechaA.getFecha();
       this.hora = fechaA.getHora();
        
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora(){
     return hora;
    }
 
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
  
   
    public void setCosto(int id, Float valor){
        
        for( int i = 0 ; i < costos.size() ; i++ ){
            if( id ==  this.servicios.get(i) ){
                costos.set(i, valor);
            }
        
        }
    }
    
    public float getCosto( int pos){
        Float valor = costos.get(pos);
       
        return (float)valor;
    }
    
    public float getSubTotal(){
        float costoTotal = 0;
                
        for (int i = 0 ; i < servicios.size(); i++ ){
            costoTotal += getCosto( i );
        }
        return costoTotal;        
    }
    
    public float getIva(){
        float costoTotal=getSubTotal()/100*16;
        return costoTotal;        
    }
    
    public float getCostoTotal(){
        float costoTotal =getIva()+getSubTotal();
        return costoTotal;        
    }
        

    public int tamLista(){
        return servicios.size();
    }
    
    public ArrayList<Integer> getServicios(){
        return this.servicios;
    } 
    
    public ArrayList<Float> getCostos(){
        return this.costos;
    }
    public void setServicios( ArrayList<Integer> datos, ArrayList<Float> costos){
        this.servicios = datos;
        this.costos = costos;
    }
            
    public void eliminarServicio( int pos){
        servicios.remove(pos);
        costos.remove(pos);
     
    }
    public void addServicio( Servicios objeto ){    
        Integer code = (Integer) objeto.getId();
        Float costo = (Float) objeto.getCosto();
        
        servicios.add(code);
        costos.add(costo);        
    }

}
