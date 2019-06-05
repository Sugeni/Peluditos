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
public class Consulta {
    
    static public int contador = 0;
    
    private int id;
    private String Diagnostico;
    private String Tratamiento;
    private float costo;
           
    //Claves foraneas
    private String IDVeterinario;
    private int Mascota;
    
    public Consulta(){
         this.id = 0;
        this.Diagnostico = null;
        this.Tratamiento = null;
        this.IDVeterinario = null;
        this.Mascota = 0;
        this.costo = 0;
    }
    public Consulta( String Diagnostico, String Tratamiento, String IDVeterinario, int Mascota, float costo) {
        
        Consulta.contador++;
        this.id = contador;
        this.Diagnostico = Diagnostico;
        this.Tratamiento = Tratamiento;
        this.IDVeterinario = IDVeterinario;
        this.Mascota = Mascota;
        this.costo = costo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
     
    
    
    public String getDiagnostico() {
        return Diagnostico;
    }

    public void setDiagnostico(String Diagnostico) {
        this.Diagnostico = Diagnostico;
    }

    public String getTratamiento() {
        return Tratamiento;
    }

    public void setTratamiento(String Tratamiento) {
        this.Tratamiento = Tratamiento;
    }

    public String getIDVeterinario() {
        return IDVeterinario;
    }

    public void setIDVeterinario(String IDVeterinario) {
        this.IDVeterinario = IDVeterinario;
    }

    public int getMascota() {
        return Mascota;
    }

    public void setMascota(int Mascota) {
        this.Mascota = Mascota;
    }

    
}
