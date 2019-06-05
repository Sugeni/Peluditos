
package Utilidades;

/**
 *
 * @author Efigenia
 */

import java.util.Calendar;

public class Calendario {
    Calendar fecha ;
    private final int year;
    private final int mes;
    private final int dia;       
    private final int hora;
    private final int minuto ;
    private final  int segundo ;
   
   public Calendario(){
        this.fecha = Calendar.getInstance();
        this.year = fecha.get(Calendar.YEAR);
        this.mes = fecha.get(Calendar.MONTH) + 1;
        this.dia = fecha.get(Calendar.DAY_OF_MONTH) ;   
        this.hora = fecha.get(Calendar.HOUR_OF_DAY);
        this.minuto = fecha.get(Calendar.MINUTE) ;
        this.segundo = fecha.get(Calendar.SECOND);
    }

    public String getFecha(){
        String fecha;
        fecha = Integer.toString(this.year) + "-" + Integer.toString(this.mes) + "-" + Integer.toString(this.mes);
        
        return fecha;
    }
    
    public String getHora(){
    String horaA;
    horaA = Integer.toString(this.hora) + ":" + Integer.toString(this.minuto) + ":" + Integer.toString(this.segundo);
    return horaA;
    }
        
    
}
