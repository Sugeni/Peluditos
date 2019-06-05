/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Efigenia
 */
public class Veterinario extends Empleado {
    
    private ArrayList<Integer> especialidades;

    public Veterinario( float sueldo, String cedula, String nombre, 
                       String apellido, String nContacto, String cElectronico) {
        
        super(2, sueldo, cedula, nombre, apellido, nContacto, cElectronico);
        especialidades = new ArrayList<>();
    }
    
    
   Consulta realizarConsulta(String Diagnostico, String Tratamiento 
                             , int Mascota, float costo){
       
        Consulta revision = new Consulta( Diagnostico, Tratamiento, this.cedula, Mascota, costo);
        return revision;   
   }
   
   public void setEspecialidades( ArrayList <Integer> datos){
       especialidades = datos;
   }
   
   public ArrayList<Integer> getEspecialidades (){
       return especialidades;
    }
   public void agregarEspecialidad( Especialidad dato){
        especialidades.add( (Integer) dato.getId() );
    }
    
    public void eliminarEspecialidad(int pos){
        especialidades.remove(pos);
    }
    public int obtenerEspecialidad( int pos){
        return especialidades.get(pos);
    }
    
    public int numEspecialidades(){
     return especialidades.size();
    }
    
    public boolean existeEspecialidad(Integer dato){
        boolean centinela = false;
        
        for(int i = 0; i < numEspecialidades(); i++ ){
            if( Objects.equals(especialidades.get(i), dato) ){
                centinela = !centinela;
                break;}
        }
        
        return centinela;
    }
}
