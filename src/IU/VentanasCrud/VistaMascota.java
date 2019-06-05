/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IU.VentanasCrud;

import BD4.ConsultasBD;
import Clases.Especie;
import Clases.Mascota;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Efigenia
 */
public class VistaMascota extends VistaModelo{
    String cedula;
    String nombre;
    int permisos;
    boolean editar = false;
    String dato= "";
    final String[] ESTADO = {"Vivo","Fallecido"};
    
    public VistaMascota( JFrame padre ) {
        super( padre );
        this.setVisible(false);
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        mostrarR();
        cargarCbEspecie();
    }
    
    public VistaMascota(JFrame padre, String cedula,int permisos,String nombre ) {
        super( padre );
        this.setVisible(false);
        this.cedula= cedula;
        this.nombre= nombre;
        this.permisos=permisos;
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        mostrarR();
        cargarCbEspecie();
    }

    void limpiar(){
        tfNombre.setText("");
        tfRaza.setText("");
        jMes.setText("");
    }
    
    private void cargarCbEspecie(){
        ArrayList<String> lista = new ArrayList<String>();
        List<Especie> componentes = ConsultasBD.obtenerEspecie();
        
        for ( int i = 0 ; i < componentes.size() ; i++){
            lista.add( componentes.get(i).getNombre());
        }
        
         jCB_Especie.removeAllItems();
            for (int i=0 ; i< lista.size() ; i++){
                jCB_Especie.addItem(lista.get(i));
            }
    }
    
        private void cargarCbEspecieSelected(int id){
            Especie prueba =  ConsultasBD.buscarEspeciebyID(id).get(0);
            ArrayList<String> lista = new ArrayList<String>();
            List<Especie> componentes = ConsultasBD.obtenerEspecie();

            for ( int i = 0 ; i < componentes.size() ; i++){
                lista.add( componentes.get(i).getNombre());
            }

            jCB_Especie.removeAllItems();
               for (int i=0 ; i< lista.size() ; i++){
                   jCB_Especie.addItem(lista.get(i));
                   if(lista.get(i).equalsIgnoreCase(prueba.getNombre())){
                       jCB_Especie.setSelectedIndex(i);
                   }

               }
    }
    
    @Override
    protected void actualizarR(){
    
    if(editar){ 
        
        editar = !editar;   
            if( (!(tfNombre.getText().trim().equals("") 
                 || (jfCedula.getText().trim()).equals(""))        )
                  & (ConsultasBD.existeCliente(jfCedula.getText().trim()))
              ) 
             {
                String comboEspecie = (String) jCB_Especie.getSelectedItem();
                String comboEstado = (String) jCB_Estado.getSelectedItem();
                
                boolean estado = true;
                if( comboEstado.equalsIgnoreCase("Fallecido")){
                   estado = false;
                }
       
               Mascota animal = new Mascota( tfNombre.getText(), tfRaza.getText(), 
                                             Integer.parseInt(jfAge.getText()), Integer.parseInt(jMes.getText()) 
                                            ,estado,
                                             ConsultasBD.buscarEspecie(comboEspecie).get(0).getId(),
                                             jfCedula.getText()
                                            );
             System.out.print("El valor del Dato es:"+ this.dato);
             ConsultasBD.modificarMascota(animal, Integer.parseInt(dato));
                
               
             JOptionPane.showMessageDialog(null, "Datos Modificados", "Exito", JOptionPane.INFORMATION_MESSAGE );
             mostrarR();
             limpiar();
        }    
        else
        {
            JOptionPane.showMessageDialog(null, "Datos no Aceptados: Compruebe el nombre\nNo se aceptan Registros repetidos o vacios"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
            
        }
        
       }
       else{
        JOptionPane.showMessageDialog(null, "Primero busque el Registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );}
    
    }
    @Override
    protected void crearR(){
    
                if( !tfNombre.getText().trim().equals("") || !jfAge.getText().equals("")   
                     || !jMes.getText().equals("") 
                        ){
                     tfNombre.setBackground(Color.white);
            
                if(  ConsultasBD.existeCliente(jfCedula.getText()) ){
                
                String comboEspecie = (String) jCB_Especie.getSelectedItem();
                String comboEstado = (String) jCB_Especie.getSelectedItem();
                boolean estado = true;
                if( comboEstado.equalsIgnoreCase("Fallecido")){
                   estado = false;
                }
       
               Mascota animal = new Mascota( tfNombre.getText(), tfRaza.getText(), 
                                             Integer.parseInt(jfAge.getText()), Integer.parseInt(jMes.getText()) 
                                            ,estado,
                                             ConsultasBD.buscarEspecie(comboEspecie).get(0).getId(),
                                             jfCedula.getText()
                                            );
               ConsultasBD.agregarMascota(animal);
                
            
                //ConsultasBD.agregarMascota(animal);
                JOptionPane.showMessageDialog(null, "Datos guardados", "Exito", JOptionPane.INFORMATION_MESSAGE );
                mostrarR();
                    
            }
            else{
                JOptionPane.showMessageDialog(null, "El campo cedula y nombre no deben estar Vacios"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
                jfCedula.setBackground(Color.red);
                limpiar();
                
            }
                
        }
        else{
            JOptionPane.showMessageDialog(null, "No se acepta que el campo nombre, mes y año esten vacios"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
            tfNombre.setBackground(Color.red);
            jfAge.setBackground(Color.yellow);
            jMes.setBackground(Color.yellow);
        }
            

    
    }
    @Override
    protected void mostrarR(){
    DefaultTableModel modelo = (DefaultTableModel) Datos.getModel();
        List<Mascota> datos = new ArrayList<> ();
        String [] columna = new String [7] ;
        
        while(modelo.getRowCount() > 0){
            modelo.setRowCount(0);}
        
        datos = ConsultasBD.obtenerMascota();
        
        for (int i = 0 ; i < datos.size() ; i++ ){
                
                columna [0] =   Integer.toString(datos.get(i).getId());
                columna [1] =   datos.get(i).getIdCliente();
                columna [2] =   datos.get(i).getNombre();
                Especie prueba =  ConsultasBD.buscarEspeciebyID(datos.get(i).getIdEspecie()).get(0);
                columna [3] =   prueba.getNombre();
                columna [4] =   datos.get(i).getRaza();
                columna [5] =   Integer.toString( datos.get(i).getYear()) + " años," +
                                Integer.toString( datos.get(i).getMes()) + " meses;" ;
                
                if( datos.get(i).isEstado() )
                    columna [6] = "Vivo";
                else
                    columna [6] = "Fallecido";
                
                modelo.addRow(columna); 
            }
        
        Datos.setModel(modelo); 
    
    }
    @Override
    protected void buscarR(){
        String respuesta = JOptionPane.showInputDialog( "Inserte el id de la mascota a buscar");
               respuesta = respuesta.trim();
               this.dato = respuesta;
               int valorRespuesta = 0;
               valorRespuesta += Integer.parseInt( respuesta );
             
               if( ( !respuesta.equals("") ) & 
                (ConsultasBD.existeMascota(valorRespuesta) )){
                 editar = true;
                 Mascota animal = ConsultasBD.buscarMascota(valorRespuesta, true).get(0);
                 txt_ID.setText("ID:" + Integer.toString( animal.getId() ));
                 tfNombre.setText( animal.getNombre());
                 tfRaza.setText(animal.getRaza());
                 cargarCbEspecieSelected(animal.getIdEspecie());
                 if( animal.isEstado() ){
                    jCB_Estado.setSelectedIndex(0);}
                 else
                     jCB_Estado.setSelectedIndex(1);
                 
                 jfCedula.setText( animal.getIdCliente());
                 jfAge.setText(Integer.toString( animal.getYear()));
                 jMes.setText(Integer.toString( animal.getMes()));
                 
                 
                 
               }
             else
                  JOptionPane.showMessageDialog(null, "No se encontro el registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
    }
    @Override
    protected void borrarR(){
             String respuesta = JOptionPane.showInputDialog( "Inserte el id de la mascota a borrar");
             int  respuesta1 =Integer.parseInt( respuesta.trim());
               
             if( ( !respuesta.equals("") ) & 
                (ConsultasBD.existeMascota(respuesta1) )){
                 int i = JOptionPane.showConfirmDialog(this, "Realmente desea borrar el dato","Confirmar    accion" ,JOptionPane.YES_NO_OPTION);
            
                if( i == 0){
                    //Borra primero las consultas asociadas a la mascota
                    List <Mascota> persona = new ArrayList<>();
                    persona = ConsultasBD.buscarMascota( respuesta1, true );
                   
                    
                    for (Mascota o : persona)
                    ConsultasBD.borrarConsulta(o.getId(), false);
                    
                    //Borra la mascota
                    ConsultasBD.borrarMascota(respuesta1, true);
                    
                    mostrarR();
                }
                 
             }
             else
                  JOptionPane.showMessageDialog(null, "No se encontro el registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
    
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jp_BarraHerramientas = new javax.swing.JPanel();
        BotonSalir = new javax.swing.JButton();
        Txt_Ingreso = new javax.swing.JLabel();
        Txt_Infor = new javax.swing.JLabel();
        Txt_Infor3 = new javax.swing.JLabel();
        Txt_Infor1 = new javax.swing.JLabel();
        Txt_Infor2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Datos = new javax.swing.JTable();
        paneAcciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        panelFormulario = new javax.swing.JPanel();
        txt_Nombre = new javax.swing.JLabel();
        txt_Edad = new javax.swing.JLabel();
        tfNombre = new javax.swing.JTextField();
        txt_Cedula = new javax.swing.JLabel();
        txt_Raza = new javax.swing.JLabel();
        tfRaza = new javax.swing.JTextField();
        txt_Especie = new javax.swing.JLabel();
        jfCedula = new javax.swing.JFormattedTextField();
        txt_Estado = new javax.swing.JLabel();
        jCB_Estado = new javax.swing.JComboBox<>(ESTADO);
        txt_ID = new javax.swing.JLabel();
        jCB_Especie = new javax.swing.JComboBox<>();
        jMes = new javax.swing.JFormattedTextField();
        jfAge = new javax.swing.JFormattedTextField();
        txt_Mes = new javax.swing.JLabel();
        txt_Mes1 = new javax.swing.JLabel();
        Txt_user = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(new java.awt.Dimension(900, 700));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 700));
        jPanel1.setLayout(null);

        jp_BarraHerramientas.setBackground(new java.awt.Color(0, 153, 0));
        jp_BarraHerramientas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 0), 1, true));

        BotonSalir.setBackground(new java.awt.Color(0, 153, 0));
        BotonSalir.setFont(new java.awt.Font("Leelawadee UI", 1, 18)); // NOI18N
        BotonSalir.setForeground(new java.awt.Color(255, 255, 255));
        BotonSalir.setText("X");
        BotonSalir.setContentAreaFilled(false);
        BotonSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BotonSalir.setOpaque(true);
        BotonSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BotonSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BotonSalirMouseExited(evt);
            }
        });
        BotonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonSalirActionPerformed(evt);
            }
        });

        Txt_Ingreso.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 24)); // NOI18N
        Txt_Ingreso.setForeground(new java.awt.Color(255, 255, 255));
        Txt_Ingreso.setText("         Clinica Peluditos Pet");
        Txt_Ingreso.setAlignmentX(0.5F);
        Txt_Ingreso.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Txt_Ingreso.setInheritsPopupMenu(false);

        Txt_Infor.setFont(new java.awt.Font("Leelawadee UI", 0, 36)); // NOI18N
        Txt_Infor.setForeground(new java.awt.Color(255, 255, 255));
        Txt_Infor.setText("Mascotas");
        Txt_Infor.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Txt_Infor.setAlignmentX(0.5F);
        Txt_Infor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Txt_Infor3.setFont(new java.awt.Font("Leelawadee UI", 0, 14)); // NOI18N
        Txt_Infor3.setForeground(new java.awt.Color(255, 255, 255));
        Txt_Infor3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Txt_Infor3.setText("Bienvenid@ "+this.nombre);
        Txt_Infor3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Txt_Infor3.setAlignmentX(0.5F);
        Txt_Infor3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jp_BarraHerramientasLayout = new javax.swing.GroupLayout(jp_BarraHerramientas);
        jp_BarraHerramientas.setLayout(jp_BarraHerramientasLayout);
        jp_BarraHerramientasLayout.setHorizontalGroup(
            jp_BarraHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_BarraHerramientasLayout.createSequentialGroup()
                .addGap(0, 252, Short.MAX_VALUE)
                .addComponent(Txt_Ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186)
                .addComponent(BotonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jp_BarraHerramientasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Txt_Infor, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Txt_Infor3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jp_BarraHerramientasLayout.setVerticalGroup(
            jp_BarraHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_BarraHerramientasLayout.createSequentialGroup()
                .addGroup(jp_BarraHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BotonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Txt_Ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jp_BarraHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Txt_Infor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_BarraHerramientasLayout.createSequentialGroup()
                        .addComponent(Txt_Infor3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanel1.add(jp_BarraHerramientas);
        jp_BarraHerramientas.setBounds(0, 0, 900, 100);

        Txt_Infor1.setFont(new java.awt.Font("Leelawadee UI", 0, 36)); // NOI18N
        Txt_Infor1.setForeground(new java.awt.Color(0, 153, 0));
        Txt_Infor1.setText("Acciones");
        Txt_Infor1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Txt_Infor1.setAlignmentX(0.5F);
        Txt_Infor1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Txt_Infor1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(Txt_Infor1);
        Txt_Infor1.setBounds(640, 110, 160, 40);

        Txt_Infor2.setFont(new java.awt.Font("Leelawadee UI", 0, 36)); // NOI18N
        Txt_Infor2.setForeground(new java.awt.Color(0, 153, 0));
        Txt_Infor2.setText("Formulario");
        Txt_Infor2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Txt_Infor2.setAlignmentX(0.5F);
        Txt_Infor2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Txt_Infor2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(Txt_Infor2);
        Txt_Infor2.setBounds(200, 110, 190, 40);

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));

        Datos.setAutoCreateRowSorter(true);
        Datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Propietario", "Nombre", "Especie", "Raza", "Edad", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Datos);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 380, 820, 290);

        paneAcciones.setBackground(new java.awt.Color(255, 255, 0));
        paneAcciones.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        paneAcciones.setForeground(new java.awt.Color(204, 204, 204));
        paneAcciones.setLayout(null);

        btnGuardar.setBackground(new java.awt.Color(0, 0, 204));
        btnGuardar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setToolTipText("");
        btnGuardar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 204), 2));
        btnGuardar.setBorderPainted(false);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setOpaque(true);
        btnGuardar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Guardar2.png"))); // NOI18N
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardarMouseExited(evt);
            }
        });
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        paneAcciones.add(btnGuardar);
        btnGuardar.setBounds(10, 20, 80, 50);

        btnBorrar.setBackground(new java.awt.Color(255, 0, 0));
        btnBorrar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnBorrar.setForeground(new java.awt.Color(255, 255, 255));
        btnBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Borrar.png"))); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.setToolTipText("");
        btnBorrar.setBorder(null);
        btnBorrar.setBorderPainted(false);
        btnBorrar.setContentAreaFilled(false);
        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBorrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBorrar.setOpaque(true);
        btnBorrar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Borrar2.png"))); // NOI18N
        btnBorrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBorrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBorrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBorrarMouseExited(evt);
            }
        });
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });
        paneAcciones.add(btnBorrar);
        btnBorrar.setBounds(190, 110, 80, 50);

        btnBuscar.setBackground(new java.awt.Color(0, 153, 0));
        btnBuscar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Buscar.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.setToolTipText("");
        btnBuscar.setBorder(null);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscar.setOpaque(true);
        btnBuscar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Buscar2.png"))); // NOI18N
        btnBuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarMouseExited(evt);
            }
        });
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        paneAcciones.add(btnBuscar);
        btnBuscar.setBounds(100, 20, 80, 50);

        btnEditar.setBackground(new java.awt.Color(255, 102, 0));
        btnEditar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Editar.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.setToolTipText("");
        btnEditar.setBorder(null);
        btnEditar.setBorderPainted(false);
        btnEditar.setContentAreaFilled(false);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEditar.setOpaque(true);
        btnEditar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Editar2.png"))); // NOI18N
        btnEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditarMouseExited(evt);
            }
        });
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        paneAcciones.add(btnEditar);
        btnEditar.setBounds(190, 20, 80, 50);

        btnHistorial.setBackground(new java.awt.Color(153, 0, 153));
        btnHistorial.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnHistorial.setForeground(new java.awt.Color(255, 255, 255));
        btnHistorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Historial.png"))); // NOI18N
        btnHistorial.setText("Historial");
        btnHistorial.setToolTipText("");
        btnHistorial.setBorder(null);
        btnHistorial.setBorderPainted(false);
        btnHistorial.setContentAreaFilled(false);
        btnHistorial.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnHistorial.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHistorial.setOpaque(true);
        btnHistorial.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Historial2.png"))); // NOI18N
        btnHistorial.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHistorialMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHistorialMouseExited(evt);
            }
        });
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });
        paneAcciones.add(btnHistorial);
        btnHistorial.setBounds(10, 110, 80, 50);

        jPanel1.add(paneAcciones);
        paneAcciones.setBounds(570, 150, 280, 190);

        panelFormulario.setBackground(new java.awt.Color(0, 204, 204));
        panelFormulario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        panelFormulario.setLayout(null);

        txt_Nombre.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Nombre.setText("Nombre:");
        panelFormulario.add(txt_Nombre);
        txt_Nombre.setBounds(10, 50, 100, 30);

        txt_Edad.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Edad.setText("Edad:");
        panelFormulario.add(txt_Edad);
        txt_Edad.setBounds(280, 90, 90, 30);

        tfNombre.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 14)); // NOI18N
        tfNombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        panelFormulario.add(tfNombre);
        tfNombre.setBounds(110, 50, 130, 30);

        txt_Cedula.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Cedula.setText("Cedula:");
        panelFormulario.add(txt_Cedula);
        txt_Cedula.setBounds(280, 10, 100, 30);

        txt_Raza.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Raza.setText("Raza:");
        panelFormulario.add(txt_Raza);
        txt_Raza.setBounds(10, 90, 100, 30);

        tfRaza.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 14)); // NOI18N
        tfRaza.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        panelFormulario.add(tfRaza);
        tfRaza.setBounds(110, 90, 130, 30);

        txt_Especie.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Especie.setText("Especie:");
        panelFormulario.add(txt_Especie);
        txt_Especie.setBounds(280, 50, 100, 30);
        panelFormulario.add(jfCedula);
        jfCedula.setBounds(370, 10, 130, 30);

        txt_Estado.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Estado.setText("Estado:");
        panelFormulario.add(txt_Estado);
        txt_Estado.setBounds(10, 130, 170, 30);

        panelFormulario.add(jCB_Estado);
        jCB_Estado.setBounds(110, 130, 130, 30);

        txt_ID.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_ID.setText("ID:");
        panelFormulario.add(txt_ID);
        txt_ID.setBounds(10, 10, 100, 30);

        jCB_Especie.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 11)); // NOI18N
        jCB_Especie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCB_Especie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCB_EspecieActionPerformed(evt);
            }
        });
        panelFormulario.add(jCB_Especie);
        jCB_Especie.setBounds(370, 50, 140, 30);

        jMes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        panelFormulario.add(jMes);
        jMes.setBounds(410, 130, 50, 30);

        jfAge.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jfAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfAgeActionPerformed(evt);
            }
        });
        panelFormulario.add(jfAge);
        jfAge.setBounds(410, 90, 50, 30);

        txt_Mes.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        txt_Mes.setText("Meses:");
        panelFormulario.add(txt_Mes);
        txt_Mes.setBounds(350, 130, 60, 30);

        txt_Mes1.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        txt_Mes1.setText("Años:");
        panelFormulario.add(txt_Mes1);
        txt_Mes1.setBounds(350, 90, 60, 30);

        jPanel1.add(panelFormulario);
        panelFormulario.setBounds(30, 150, 530, 190);

        Txt_user.setFont(new java.awt.Font("Leelawadee UI", 0, 14)); // NOI18N
        Txt_user.setForeground(new java.awt.Color(255, 255, 255));
        Txt_user.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Txt_user.setText("Bienvenid@ "+this.nombre);
        Txt_user.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Txt_user.setAlignmentX(0.5F);
        Txt_user.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(177, 177, 177)
                    .addComponent(Txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(177, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(330, 330, 330)
                    .addComponent(Txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(330, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonSalirMouseEntered
        BotonSalir.setBackground( new java.awt.Color (255,0,0) );
    }//GEN-LAST:event_BotonSalirMouseEntered

    private void BotonSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotonSalirMouseExited
        BotonSalir.setBackground( new java.awt.Color(0,153,0) );
    }//GEN-LAST:event_BotonSalirMouseExited

    private void BotonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSalirActionPerformed
        this.setVisible(false);
        padre.setVisible(true);
    }//GEN-LAST:event_BotonSalirActionPerformed

    private void btnGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseEntered

        btnGuardar.setBackground( new java.awt.Color (51,51,255) );
    }//GEN-LAST:event_btnGuardarMouseEntered

    private void btnGuardarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseExited
        btnGuardar.setBackground( new java.awt.Color (0,0,204) );
    }//GEN-LAST:event_btnGuardarMouseExited

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        crearR();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBorrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseEntered

        btnBorrar.setBackground( new java.awt.Color (255,51,51) );
    }//GEN-LAST:event_btnBorrarMouseEntered

    private void btnBorrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseExited

        btnBorrar.setBackground( new java.awt.Color (255,0,0) );
    }//GEN-LAST:event_btnBorrarMouseExited

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        borrarR();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseEntered

        btnBuscar.setBackground( new java.awt.Color (0,204,0) );
    }//GEN-LAST:event_btnBuscarMouseEntered

    private void btnBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseExited

        btnBuscar.setBackground( new java.awt.Color (0,153,0) );
    }//GEN-LAST:event_btnBuscarMouseExited

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarR();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEditarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseEntered

        btnEditar.setBackground( new java.awt.Color (255,153,51) );
    }//GEN-LAST:event_btnEditarMouseEntered

    private void btnEditarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseExited

        btnEditar.setBackground( new java.awt.Color (255,102,0) );
    }//GEN-LAST:event_btnEditarMouseExited

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        actualizarR();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnHistorialMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistorialMouseEntered
        btnHistorial.setBackground( new java.awt.Color  (204,0,255) );
    }//GEN-LAST:event_btnHistorialMouseEntered

    private void btnHistorialMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistorialMouseExited
        btnHistorial.setBackground( new java.awt.Color  (153,0,153) );
    }//GEN-LAST:event_btnHistorialMouseExited

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void jfAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfAgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jfAgeActionPerformed

    private void jCB_EspecieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCB_EspecieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCB_EspecieActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton BotonSalir;
    private javax.swing.JTable Datos;
    protected javax.swing.JLabel Txt_Infor;
    protected javax.swing.JLabel Txt_Infor1;
    protected javax.swing.JLabel Txt_Infor2;
    protected javax.swing.JLabel Txt_Infor3;
    protected javax.swing.JLabel Txt_Ingreso;
    private javax.swing.JLabel Txt_user;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JComboBox<String> jCB_Especie;
    private javax.swing.JComboBox<String> jCB_Estado;
    private javax.swing.JFormattedTextField jMes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFormattedTextField jfAge;
    private javax.swing.JFormattedTextField jfCedula;
    protected javax.swing.JPanel jp_BarraHerramientas;
    private javax.swing.JPanel paneAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JTextField tfNombre;
    private javax.swing.JTextField tfRaza;
    private javax.swing.JLabel txt_Cedula;
    private javax.swing.JLabel txt_Edad;
    private javax.swing.JLabel txt_Especie;
    private javax.swing.JLabel txt_Estado;
    private javax.swing.JLabel txt_ID;
    private javax.swing.JLabel txt_Mes;
    private javax.swing.JLabel txt_Mes1;
    private javax.swing.JLabel txt_Nombre;
    private javax.swing.JLabel txt_Raza;
    // End of variables declaration//GEN-END:variables
}
