/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD4;

import Clases.Cliente;
import Clases.Consulta;
import Clases.Empleado;
import Clases.Especialidad;
import Clases.Especie;
import Clases.Factura;
import Clases.Mascota;
import Clases.Servicios;
import Clases.Veterinario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Efigenia
 */
public class ConsultasBD {
    
    //Consulta
     public static void agregarConsulta(Consulta revision){ 
      Conexion.db.store(revision);     
   }
    
     public static int maxConsulta(){
    int valor = 0;
    Consulta instancia = null ;
    List<Consulta> datos = obtenerConsulta();
    for (Object o : datos) {
        instancia = (Consulta) o;
        if( instancia.getId() > valor)
            valor = instancia.getId();
    }
   
    return valor;
   
   }  
    
     public static boolean existeConsulta( int id, boolean individual){
      boolean existe = false;
      Consulta dato;
       for (Object o : buscarConsultabyID( id ) ){
           dato = (Consulta) o;
           if( dato.getId() == id ){
                existe = !existe ;
                break;
            }
       }
       return false;
     }
       
    public static boolean existeConsultabyID( int id){
      boolean existe = false;
      Consulta consulta = ConsultasBD.buscarConsultabyID(id).get(0);
      if(consulta.getId() == id){
          return true;
      }
      return false;
     }
     //id propio o de la mascota
    public static void borrarConsulta(int id, boolean individual){
        
        for( Consulta o : buscarConsulta( id, individual) ){
            Conexion.db.delete( o);
        }
   
    }
    
    public static List<Consulta> obtenerConsulta(){
       
       Conexion.resultado = Conexion.db.queryByExample( Consulta.class);
       return (List<Consulta>)Conexion.resultado; 
       
   }   
    
    //id propio o de la Mascota
    public static List<Consulta> buscarConsulta( int id, boolean individual){
        
       Consulta instancia = null;
        List<Consulta> Lista = obtenerConsulta();
        List<Consulta> segundaL= new ArrayList<Consulta>();
        
        if( individual){
            
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Consulta)Lista.get(i);
                if( id == instancia.getId()  ){
                    segundaL.add(instancia);
            }
        }
        } else{
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Consulta)Lista.get(i);
                if( id == instancia.getMascota()){
                    segundaL.add(instancia);
                }
            }
        }        
        return segundaL;       
   } 
    
    public static List<Consulta> buscarConsultabyID( int id){
        
        Consulta instancia = null;
        List<Consulta> Lista = obtenerConsulta();
        List<Consulta> segundaL= new ArrayList<Consulta>();
 
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Consulta)Lista.get(i);
                if( id == instancia.getId()  ){
                    segundaL.add(instancia);
                }
            }
        return segundaL; 
   } 
    
    
   
   
    //Cedula del Veterinario
     //Buscar por Cedula
    public static List<Consulta> buscarConsulta( String cedula){
        
        Consulta instancia = null;
        List<Consulta> Lista = obtenerConsulta();
        List<Consulta> segundaL= new ArrayList<Consulta>();
        for ( int i = 0 ; i < Lista.size(); i++ ){
            instancia = (Consulta)Lista.get(i);
            if(cedula.equals( instancia.getIDVeterinario()) ){
                segundaL.add(instancia);
            }
        }
        return segundaL;      
        
   }   
    
    public static void borrarConsulta(String cedula){
        
        for( Consulta o : buscarConsulta(cedula) ){
            Conexion.db.delete( o );
        }
   }      
    
    public static void modificarConsulta( Consulta visita, int id){ 
       
        List<Consulta> visitas = buscarConsulta( id, true);
        Consulta instancia = null;
        
        for (Consulta o : visitas){
            instancia = o;
            
            instancia.setCosto( visita.getCosto());
            instancia.setDiagnostico(visita.getDiagnostico());
            instancia.setIDVeterinario(visita.getIDVeterinario());
            instancia.setId( visita.getId() );
            instancia.setMascota( visita.getMascota());
            instancia.setTratamiento( visita.getTratamiento());
            
            Conexion.db.store( instancia);
    }            
            
        
        
    }
  
    public static void escribirConsulta(List<Consulta> result){
        Consulta escrito;
       
        for (Consulta o : result) {
            escrito = o;
            System.out.println("ID:" + escrito.getId()            +
                               " Costo:" + escrito.getCosto()          +
                               " Diagnostico:" + escrito.getDiagnostico()     +
                               " Veterinario:" + escrito.getIDVeterinario()     +
                               " Mascota:" + escrito.getMascota()+
                               " Tratamiento:" + escrito.getTratamiento()
                            );

        }
    }
   
    //Especialidad
    //Agrega el dato 
   public static void agregarEspecialidad(String nombre){ 
        nombre = nombre.toUpperCase();
        Especialidad.contador = maxEspecialidad();
        Especialidad clase = new Especialidad(nombre);
        Conexion.db.store(clase);     
   }   
   //Borra la especialidad
   public static void borrarEspecialidad(String nombre){
       
       nombre = nombre.toUpperCase();
        for( Object o : buscarEspecialidad(nombre) ){
            Conexion.db.delete((Especialidad) o);
        }
   }     
   /*Obtiene todos los elementos enviando un objeto Vacio*/
   public static List<Especialidad> obtenerEspecialidades(){
       
       Conexion.resultado = Conexion.db.queryByExample( Especialidad.class);
       return (List<Especialidad>)Conexion.resultado; 
   }   
   /*Cuenta el maximo para el id*/
   public static int maxEspecialidad(){
    int valor = 0;
    Especialidad instancia = null ;
    
    for (Object o : obtenerEspecialidades()) {
        instancia = (Especialidad) o;
        if( instancia.getId() > valor)
            valor = instancia.getId();
    }
   
    return valor;
   
   }  
   /*Busca una especialidad en particular*/
   public static List<Especialidad> buscarEspecialidad( String nombre){
       
       nombre = nombre.toUpperCase();
       Especialidad proto = new Especialidad();
       proto.setNombre(nombre);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Especialidad>) Conexion.resultado;
   }   
   
    public static List<Especialidad> buscarEspecialidad( int id){
       
       Especialidad proto = new Especialidad();
       proto.setId(id);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Especialidad>) Conexion.resultado;
   }   
   /*Verifica si existe una especialidad*/
   public static boolean existeEspecialidad(String nombre){
       nombre = nombre.toUpperCase();
       boolean existe = false;
       Especialidad dato = null;
       for (Object o : buscarEspecialidad(nombre) ){
           dato = (Especialidad) o;
           if( dato.getNombre() == null ? nombre == null : dato.getNombre().equals(nombre) ){
                existe = !existe ;
                break;
            }
       }           
       return existe;
    }  
   /*Modificar especialidad*/
   public static void modificarEspecialidad( String nombre, String dato){ 
       
        nombre = nombre.toUpperCase();
        dato = dato.toUpperCase();
        Especialidad escrito = new Especialidad();
        escrito.setNombre(dato);
        
        Conexion.resultado = Conexion.db.queryByExample(escrito);

        for (Object o : Conexion.resultado) {
            escrito = (Especialidad) o;
            escrito.setNombre(nombre);
            Conexion.db.store(escrito);
        }
        
    }
   //Escribe todos
    public static void escribirEspecialidad(List<Especialidad> result){
        Especialidad escrito;
       
        for (Especialidad o : result) {
            escrito = o;
            System.out.println("Id:"+ escrito.getId() +
                               " Nombre:" + escrito.getNombre());
        }
    }

    //Empleado
    public static void agregarEmpleado(Empleado persona){ 
      persona.setNombre(persona.getNombre().toUpperCase());
      persona.setApellido(persona.getApellido().toUpperCase());
      
      Conexion.db.store(persona);     
   }
    public static void borrarEmpleado(String cedula){
        
        for( Empleado o : buscarEmpleado(cedula) ){
            Conexion.db.delete( o);
        }
   }           
    public static List<Empleado> obtenerEmpleado(){
       
       Conexion.resultado = Conexion.db.queryByExample( Empleado.class);
       return (List<Empleado>)Conexion.resultado; 
       
   }   
    public static List<Empleado> buscarEmpleado( String cedula){
        
       Empleado proto = new Empleado( 0, 0 ,  cedula, null, null, null, null);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Empleado>) Conexion.resultado;
   }   
    public static boolean existeEmpleado(String cedula){
       
       boolean existe = false;
       Empleado dato = null;
       for ( Empleado o : buscarEmpleado( cedula) ){
           
           dato =  o;
           if( dato.getCedula() == null ? cedula == null : dato.getCedula().equals(cedula) ){
                existe = !existe ;
                break;
            }
       }
       
       return existe;
    }  
    
    public static void modificarEmpleado( Empleado persona, String cedula){ 
        persona.setNombre(persona.getNombre().toUpperCase());
        persona.setApellido(persona.getApellido().toUpperCase());
       
        List<Empleado> personas = buscarEmpleado( cedula);
        Empleado instancia = null;
        for (Object o : personas){
            instancia = (Empleado) o;
            
            instancia.setCedula(persona.getCedula());
            instancia.setNombre( persona.getNombre());
            instancia.setApellido( persona.getApellido());
            instancia.setnContacto( persona.getnContacto());
            instancia.setcElectronico( persona.getcElectronico());
            instancia.setTEmpleado(persona.gettEmpleado());
            instancia.setSueldo(persona.getSueldo());
            
            Conexion.db.store( instancia);
    }            
            
        
        
    }
  
    public static void escribirEmpleado(List<Empleado> result){
        Empleado escrito;
       
        for (Empleado o : result) {
            escrito = o;
            System.out.println("Cedula:" + escrito.getCedula()           +
                               " Nombre:" + escrito.getNombre()          +
                               " Apellido:" + escrito.getApellido()      +
                               " Telefono:" + escrito.getnContacto()     +
                               " Correo:" + escrito.getcElectronico()    +
                               " Tipo Empleado:" + escrito.gettEmpleado()+
                               " Sueldo:" + escrito.getSueldo()          +
                               " Pass:" + escrito.getPass()
                            );

        }
    }
    
    //Especie
    public static void agregarEspecie(String nombre){ 
        nombre = nombre.toUpperCase();
        Especie.contador = maxEspecie();
        Especie clase = new Especie(nombre);
        Conexion.db.store(clase);     
   }
    public static void borrarEspecie(String nombre){
       nombre = nombre.toUpperCase();
        for( Object o : buscarEspecie(nombre) ){
            Conexion.db.delete((Especie) o);
        }
   }           
    public static List<Especie> obtenerEspecie(){
       
       Conexion.resultado = Conexion.db.queryByExample( Especie.class);
       return (List<Especie>)Conexion.resultado; 
       
   }   
    public static int maxEspecie(){
    int valor = 0;
    Especie instancia = null ;
    List<Especie> datos = obtenerEspecie();
    for (Object o : datos) {
        instancia = (Especie) o;
        if( instancia.getId() > valor)
            valor = instancia.getId();
    }
   
    return valor;
   
   }  
    public static List<Especie> buscarEspecie( String nombre){
       nombre = nombre.toUpperCase();
       Especie proto = new Especie();
       proto.setNombre(nombre);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Especie>) Conexion.resultado;
   }
    public static List<Especie> buscarEspeciebyID( int id){
       Especie proto = new Especie();
       proto.setId(id);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Especie>) Conexion.resultado;
   }  
    public static boolean existeEspecie(String nombre){
       nombre = nombre.toUpperCase();
       
       boolean existe = false;
       Especie dato = null;
       for ( Object o : buscarEspecie( nombre) ){
           
           dato = (Especie) o;
           if( dato.getNombre() == null ? nombre == null : dato.getNombre().equals(nombre) ){
                existe = !existe ;
                break;
            }
       }           
       return existe;
    }  
    public static void modificarEspecie( String nombre, String dato){ 
        nombre = nombre.toUpperCase();
        dato = dato.toUpperCase();
        Especie escrito = new Especie();
        escrito.setNombre(dato);
        
        Conexion.resultado = Conexion.db.queryByExample(escrito);

        for (Object o : Conexion.resultado) {
            escrito = (Especie) o;
            escrito.setNombre(nombre);
            Conexion.db.store(escrito);
        }
        
    }
    public static void escribirEspecie(List<Especie> result){
        Especie escrito;
       
        for (Object o : result) {
            escrito = (Especie)o;
            System.out.println("Id:"+ escrito.getId() +
                               " Nombre:" + escrito.getNombre());
        }
    }
   
    //Cliente
    public static void agregarCliente(Cliente persona){ 
      persona.setNombre(persona.getNombre().toUpperCase());
      persona.setApellido(persona.getApellido().toUpperCase());
      
      Conexion.db.store(persona);     
   }
    public static void borrarCliente(String cedula){
        
        for( Object o : buscarCliente(cedula) ){
            Conexion.db.delete((Cliente) o);
        }
   }           
    public static List<Cliente> obtenerCliente(){
       
       Conexion.resultado = Conexion.db.queryByExample( Cliente.class);
       return (List<Cliente>)Conexion.resultado; 
       
   }   
    public static List<Cliente> buscarCliente( String cedula){
        
       Cliente proto = new Cliente( 0, cedula, null, null, null, null);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Cliente>) Conexion.resultado;
   }   
    public static boolean existeCliente(String cedula){
       
       boolean existe = false;
       Cliente dato = null;
       for ( Object o : buscarCliente( cedula) ){
           
           dato = (Cliente) o;
           if( dato.getCedula() == null ? cedula == null : dato.getCedula().equals(cedula) ){
                existe = !existe ;
                break;
            }
       }
       
       return existe;
    }  
    public static void modificarCliente( Cliente persona, String cedula){ 
        persona.setNombre(persona.getNombre().toUpperCase());
        persona.setApellido(persona.getApellido().toUpperCase());
        List<Cliente> personas = buscarCliente( cedula);
        Cliente instancia = null;
        for (Object o : personas){
            instancia = (Cliente) o;
            
            instancia.setCedula(persona.getCedula());
            instancia.settCliente(persona.gettCliente());
            instancia.setNombre( persona.getNombre());
            instancia.setApellido( persona.getApellido());
            instancia.setnContacto( persona.getnContacto());
            instancia.setcElectronico( persona.getcElectronico());
            
            Conexion.db.store( instancia);
    }            
            
        
        
    }
    public static void escribirCliente(List<Cliente> result){
        Cliente escrito;
       
        for (Cliente o : result) {
            
            escrito = o ;
            System.out.println("Cedula:" + escrito.getCedula()           +
                               " Nombre:" + escrito.getNombre()          +
                               " Apellido:" + escrito.getApellido()      +
                               " Telefono:" + escrito.getnContacto()     +
                               " Correo:" + escrito.getcElectronico()    
                            );
        }

    }

    
    //Factura
     public static void agregarFactura(Factura revision){ 
      Conexion.db.store(revision);     
   }    
     public static int maxFactura(){
    int valor = 0;
    Factura instancia = null ;
    List<Factura> datos = obtenerFactura();
    for (Object o : datos) {
        instancia = (Factura) o;
        if( instancia.getId() > valor)
            valor = instancia.getId();
    }
   
    return valor;
   
   }  
     //id propio 
    public static void borrarFactura(int id){
        
        for( Factura o : buscarFactura( id) ){
            Conexion.db.delete( o);
        }
   
    }
    
     public static void borrarFactura(String cedula, char tpersona){
        
        for( Factura o : buscarFactura(cedula, tpersona) ){
            Conexion.db.delete( o );
        }
   } 
    
    public static List<Factura> obtenerFactura(){
       
       Conexion.resultado = Conexion.db.queryByExample( Factura.class);
       return (List<Factura>)Conexion.resultado; 
       
   }    
    
    public static boolean existeFactura(int id){
       
       boolean existe = false;
       Factura dato = null;
       for ( Object o : buscarFactura(id) ){
           
           dato = (Factura) o;
           if(dato.getId() == id ){
                existe = !existe ;
                break;
            }
       }
       
       return existe;
    }  
    //id propio 
    public static List<Factura> buscarFactura( int id){
        
       Factura instancia = null;
        List<Factura> Lista = obtenerFactura();
        List<Factura> segundaL= new ArrayList<Factura>();
 
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Factura)Lista.get(i);
                if( id == instancia.getId()  ){
                    segundaL.add(instancia);
                }
            }
        return segundaL;       
   }   
   
    //Buscar por Cedula
    public static List<Factura> buscarFactura( String cedula, char tpersona){
        
        Factura instancia = null;
        List<Factura> Lista = obtenerFactura();
        List<Factura> segundaL= new ArrayList<Factura>();
        
        if( ( 'E' == tpersona) || ('e' == tpersona) ){
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Factura)Lista.get(i);
                if(cedula.equals( instancia.getIdEmpleado()) ){
                    segundaL.add(instancia);
                }
            }
        }
        else
        
        if( ( 'C' == tpersona) || ('c' == tpersona) ){
                 
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Factura)Lista.get(i);
                if(cedula.equals( instancia.getIdCliente()) ){
                    segundaL.add(instancia);
                }
            }
             }
            
        return segundaL;      
        
   }   

    public static void modificarFactura( Factura factura, int id){ 
       
       
        List<Factura> facturas = buscarFactura( id);
       Factura instancia = null;
        for ( Factura o : facturas){
            
            instancia =  o;
            instancia.setIdCliente( factura.getIdCliente() );
            instancia.setIdEmpleado( factura.getIdEmpleado());
            instancia.setServicios( factura.getServicios(), factura.getCostos());
            
            Conexion.db.store( instancia);
    }            
            
        
        
        
    }

    public static void escribirFactura(List<Factura> result){
        Factura escrito;
       
        for (Factura o : result) {
            escrito = o;
            System.out.println("ID:" + escrito.getId()                          +
                               " Costo Total:" + escrito.getCostoTotal()        +
                               " Hora:" + escrito.getHora()                     +
                               " Fecha:" + escrito.getFecha()                   +
                               " Cliente:" + escrito.getIdCliente()             +
                               " Veterinario:" + escrito.getIdEmpleado()
                            );

        }
    }
   
  
    //Mascota
    public static void agregarMascota(Mascota animal){ 
        
        Mascota.contador = maxMascota();
        animal.setId(Mascota.contador + 1);
        animal.setNombre( animal.getNombre().toUpperCase());
        animal.setRaza( animal.getRaza().toUpperCase());
        
        
        Conexion.db.store(animal);     
   }
    public static void borrarMascota(String cedula){
        
        for( Mascota o : buscarMascota(cedula) ){
            Conexion.db.delete( o );
        }
   }           
    public static void borrarMascota(int id, boolean individual){
            
        for( Mascota o : buscarMascota(id, individual) ){
            Conexion.db.delete((Mascota) o);
        }
   }      
    public static List<Mascota> obtenerMascota(){
       
       Conexion.resultado = Conexion.db.queryByExample( Mascota.class);
       return (List<Mascota>)Conexion.resultado; 
       
   }   
    public static int maxMascota(){
        
        int valor = 0;
        Mascota instancia = null ;
        List<Mascota> datos = obtenerMascota();
        for (Object o : datos) {
            instancia = (Mascota) o;
            if( instancia.getId() > valor)
                valor = instancia.getId();
        }
   
        return valor;   
    }  
    //Buscar por Cedula
    public static List<Mascota> buscarMascota( String cedula){
        
        Mascota instancia = null;
        List<Mascota> Lista = obtenerMascota();
        List<Mascota> segundaL= new ArrayList<Mascota>();
        for ( int i = 0 ; i < Lista.size(); i++ ){
            instancia = (Mascota)Lista.get(i);
            if(cedula.equals( instancia.getIdCliente() ) ){
                segundaL.add(instancia);
            }
        }
        return segundaL;      
   }   
   //Buscar por ID individual o especie
    public static List<Mascota> buscarMascota( int id, boolean individual){
        
        Mascota instancia = null;
        List<Mascota> Lista = obtenerMascota();
        List<Mascota> segundaL= new ArrayList<Mascota>();
        
        if( individual){
            
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Mascota)Lista.get(i);
                if( id == instancia.getId()  ){
                    segundaL.add(instancia);
            }
        }
        } else{
            for ( int i = 0 ; i < Lista.size(); i++ ){
                instancia = (Mascota)Lista.get(i);
                if( id == instancia.getIdEspecie()){
                    segundaL.add(instancia);
                }
            }
        }        
        return segundaL;          
    }         
    public static boolean existeMascota(String cedula, Mascota animal){
   
       boolean existe = false;
       Mascota dato = null;
       //verifica si existe en la cedula dada
       for ( Mascota o : buscarMascota( cedula) ){
           System.out.println("Estado de existe =" + existe);
           dato = o;
           
           if( dato.getIdCliente().equals( animal.getIdCliente())){
             existe= !existe;
             break;
               }
       }
       
       //verifica si existe con la especie dada 
       /*
       if(!existe){
           
            for ( Object o : buscarMascota( especie, false) ){
                System.out.println("Estado de existe =" + existe);
                dato = (Mascota) o;
                if( dato.getIdEspecie() == animal.getIdEspecie() ){
                    existe= !existe;
                    break;
               }
            } 
       }
       */
      
       return existe;
    }     
    
    public static boolean existeMascota( int id){
        boolean existe = false;
        Mascota dato = null;
       //verifica si existe en la cedula dada
       for ( Mascota o : buscarMascota( id, true) ){
           System.out.println("Estado de existe =" + existe);
           dato = o;
           
           if( dato.getId() == id){
             existe= !existe;
             break;
               }
       }
       
       return existe;
    }
//Modificar por id
    public static void modificarMascota( Mascota animal, int id) { 
        
        animal.setNombre( animal.getNombre().toUpperCase());
        animal.setRaza( animal.getRaza().toUpperCase());
        animal.setId(id);
        Mascota escrito = null; 
        
        escribirMascota(buscarMascota(id, true));
        for (Mascota o : buscarMascota(id, true)) {
            
            escrito = o;
            escrito.setNombre( animal.getNombre());
            escrito.setRaza( animal.getRaza());
            escrito.setEstado(animal.isEstado());
            escrito.setYear(animal.getYear());
            escrito.setMes(animal.getMes());
            escrito.setIdEspecie(animal.getIdEspecie());
            escrito.setIdCliente(animal.getIdCliente());
            Conexion.db.store(escrito);
        
        }
        
    }
    public static void escribirMascota(List<Mascota> result){
        Mascota escrito;
       
        for (Object o : result) {
            escrito = (Mascota)o;
            System.out.println("Id:"+ escrito.getId() +
                               " Nombre:" + escrito.getNombre() +
                               " Raza:" + escrito.getRaza()  +                    
                                " Especie:" + escrito.getIdEspecie()+
                                 " Estado:" + escrito.isEstado()+
                                 " ID dueno:" + escrito.getIdCliente()
            );
        }
    }

   //Servicios
   public static void agregarServicios(Servicios dato){ 
       
        dato.setNombre( dato.getNombre().toUpperCase());
        Conexion.db.store(dato);     
   
   }      
   public static void borrarServicios(String nombre){
       
        nombre = nombre.toUpperCase();
        for( Servicios o : buscarServicios(nombre) ){
            Conexion.db.delete(o);
        }
   }     
   public static List<Servicios> obtenerServicios(){
       
       Conexion.resultado = Conexion.db.queryByExample( Servicios.class);
       return (List<Servicios>)Conexion.resultado; 
       
   }   
   public static int maxServicios(){
    int valor = 0;
    Servicios instancia = null ;
    
    for (Servicios o : obtenerServicios()) {
        instancia = o;
        if( instancia.getId() > valor)
            valor = instancia.getId();
    }
   
    return valor;
   
   }  
   public static List<Servicios> buscarServicios( String nombre){
       
       nombre = nombre.toUpperCase();
       Servicios proto = new Servicios();
       proto.setNombre(nombre);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Servicios>) Conexion.resultado;
   } 
   
    public static List<Servicios> buscarServicios( int id){
       
       Servicios proto = new Servicios();
       proto.setId(id);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Servicios>) Conexion.resultado;
   } 
   public static boolean existeServicios(String nombre){
       
       nombre = nombre.toUpperCase();
       boolean existe = false;
       Servicios dato = null;
       for ( Servicios o : buscarServicios(nombre) ){
           dato =  o;
           if( dato.getNombre() == null ? nombre == null : dato.getNombre().equals(nombre) ){
                existe = !existe ;
                break;
            }
       }           
       return existe;
    }  
   public static void modificarServicios( String nombre, float precio, String dato){ 
       
        nombre = nombre.toUpperCase();
        dato = dato.toUpperCase();
        
        Servicios escrito = new Servicios();
        escrito.setNombre(dato);
        
        Conexion.resultado = Conexion.db.queryByExample(escrito);

        for (Object o : Conexion.resultado) {
            escrito = (Servicios) o;
            escrito.setNombre(nombre);
            escrito.setCosto(precio);
            Conexion.db.store(escrito);
        }
        
    }   
    public static void escribirServicios(List<Servicios> result){
        Servicios escrito;
       
        for (Servicios o : result) {
            escrito = o;
            System.out.println("Id:"+ escrito.getId()           +
                               " Nombre:" + escrito.getNombre() +
                               " Costo:" + escrito.getCosto());
        }
    }

    
    //Veterinario
    public static void agregarVeterinario(Veterinario persona){ 
     
      persona.setNombre(persona.getNombre().toUpperCase());
      persona.setApellido(persona.getApellido().toUpperCase());
      
      Conexion.db.store(persona);     
   }
    
    public static void borrarVeterinario(String cedula){
        
        for( Empleado o : buscarVeterinario(cedula) ){
            Conexion.db.delete( o);
        }
   }           
    public static List<Veterinario> obtenerVeterinario(){
       
       Conexion.resultado = Conexion.db.queryByExample( Veterinario.class);
       return (List<Veterinario>)Conexion.resultado; 
       
   }   
   
    public static List<Veterinario> buscarVeterinario( String cedula){
        
       Veterinario proto = new Veterinario(  0 ,  cedula, null, null, null, null);
       Conexion.resultado = Conexion.db.queryByExample(proto);
       
       return (List<Veterinario>) Conexion.resultado;
       
   }   
    public static boolean existeVeterinario(String cedula){
       
       boolean existe = false;
       Veterinario dato = null;
       for ( Veterinario o : buscarVeterinario( cedula) ){
           
           dato =  o;
           if( dato.getCedula() == null ? cedula == null : dato.getCedula().equals(cedula) ){
                existe = !existe ;
                break;
            }
       }
       
       return existe;
    }  
    
    public static void modificarVeterinario( Veterinario persona, String cedula){ 
        
        persona.setNombre(persona.getNombre().toUpperCase());
        persona.setApellido(persona.getApellido().toUpperCase());
       
        List<Veterinario> personas = buscarVeterinario( cedula);
        Veterinario instancia = null;
        for ( Veterinario o : personas){
            instancia =  o;
            
            instancia.setCedula(persona.getCedula());
            instancia.setNombre( persona.getNombre());
            instancia.setApellido( persona.getApellido());
            instancia.setnContacto( persona.getnContacto());
            instancia.setcElectronico( persona.getcElectronico());
            instancia.setTEmpleado(persona.gettEmpleado());
            instancia.setSueldo(persona.getSueldo());
            instancia.setEspecialidades( persona.getEspecialidades());
            Conexion.db.store( instancia);
    }            
            
        
        
    }
  
    public static void escribirVeterinario( List<Veterinario> result){
        
        Veterinario escrito;
       
        for (Veterinario o : result) {
            escrito = o;
            System.out.print("Cedula:" + escrito.getCedula()           +
                               " Nombre:" + escrito.getNombre()          +
                               " Apellido:" + escrito.getApellido()      +
                               " Telefono:" + escrito.getnContacto()     +
                               " Correo:" + escrito.getcElectronico()    +
                               " Tipo Empleado:" + escrito.gettEmpleado()+
                               " Sueldo:" + escrito.getSueldo()
                            );
            System.out.print(" Especialidades: ");
            for ( int i = 0 ; i < o.numEspecialidades(); i++){
                System.out.print( o.obtenerEspecialidad( i ) );
                System.out.print( " " );
            
            }
            System.out.println();
        }
    }
    
   
}

