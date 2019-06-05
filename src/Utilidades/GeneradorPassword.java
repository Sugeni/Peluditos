/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.security.SecureRandom;

/**/
public class GeneradorPassword {

    
   private final static char [] DICCIONARIO= {'M','L','K','J','H','I','T','E','F','D','C','B',
                                              'A','Z','Y','X','W','U','V','G','R','S','Q','P',
                                              'O','N','0','1','2','3','4','5','6','7','8','9'} ;
   
   public static String Generador( int tam ){
       char[] pass = new char [tam] ;
       SecureRandom r = new SecureRandom();
       
       for (int i = 0; i<tam ; i++){
           pass[i] = DICCIONARIO[ r.nextInt( DICCIONARIO.length)]   ;
       }
       
       return new String( pass );
   }
   
  
}


