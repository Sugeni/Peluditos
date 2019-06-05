/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD4;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 *
 * @author Efigenia
 */
public class Conexion {
    /*ObjectContainer: Representa la base de datos y es la interfaz que te permite interactuar con
       con db4O */  
     static public final ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "Peluditos");
     
    //Permite almacenar los resultados de las busquedas
     static public ObjectSet resultado;
     
     Conexion(){ 
     
        }
   
     static public void  cerrarBD(){
        //Cerrara la base de datos y liberara todos los recursos asociados a el
         db.close();
        }
}
