/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IU.VentanasCrud;

import BD4.ConsultasBD;
import Clases.Servicios;
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
public class VistaServicios extends VistaModelo{

    /**
     * Creates new form VistaServicios
     * @param padre
     */
    String cedula;
    String nombre;
    int permisos;
    String dato;
    
    public VistaServicios( JFrame padre ) {
        super( padre );
        this.setVisible(false);
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        mostrarR();
    }
    
    public VistaServicios( JFrame padre, String cedula,int permisos,String nombre ) {
        super( padre );
        this.setVisible(false);
        this.cedula= cedula;
        this.nombre= nombre;
        this.permisos=permisos;
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        mostrarR();
    }
    
        boolean editar = false;
    
    @Override
        protected void actualizarR(){
            if(editar){ 
        editar = !editar;   
        if ( (! ( (tfNombre.getText().equals("Ingrese el Nombre") ) 
                 || (tfNombre.getText().equals("") ) 
                 || tf_Costo.getText().equals("") 
                ) 
             ) 
           ){
             ConsultasBD.modificarServicios (tfNombre.getText(), Float.parseFloat(tf_Costo.getText()), dato);
             mostrarR();
             
             tfNombre.setBackground(Color.white);
             tf_Costo.setBackground(Color.white);
             
             JOptionPane.showMessageDialog(null, "Datos Modificados", "Exito", JOptionPane.INFORMATION_MESSAGE );

             
        }    
        else
        {
            JOptionPane.showMessageDialog(null, "Datos no Aceptados: Compruebe el nombre\nNo se aceptan Registros repetidos o vacios"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
            
            if( (tfNombre.getText().equals("Ingrese el Nombre") ) || (tfNombre.getText().equals("")) )
                tfNombre.setBackground(Color.red);
            
            if( (tf_Costo.getText().trim()).equals(""))
                tf_Costo.setBackground(Color.red);
            
        }
        
       }
       else{
        JOptionPane.showMessageDialog(null, "Primero busque el Registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );}
        }
    @Override
        protected void crearR(){
     if ( ! ( (tfNombre.getText().equals("Ingrese el Nombre") ) || (tfNombre.getText().equals("") || ConsultasBD.existeEspecialidad(tfNombre.getText()) ) 
                 || tf_Costo.getText().equals("0.00")) ){
            
            Servicios.contador = ConsultasBD.maxServicios();
            Servicios datos = new Servicios (tfNombre.getText() , Float.parseFloat( tf_Costo.getText() ) );
            ConsultasBD.agregarServicios(datos);
            JOptionPane.showMessageDialog(null, "Datos guardados", "Exito", JOptionPane.INFORMATION_MESSAGE );
            mostrarR();
            
            
            
            tfNombre.setText("Ingrese el nombre");
            tfNombre.setBackground(Color.white);
            tf_Costo.setText("0.00");
            tf_Costo.setBackground(Color.white);
            
        }    
        else
        {   JOptionPane.showMessageDialog(null, "Datos no Aceptados: Compruebe el nombre\nNo se aceptan Registros repetidos o vacios"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
        
            if ( (tfNombre.getText().equals("Ingrese el Nombre") ) || (tfNombre.getText().equals("") || ConsultasBD.existeEspecialidad(tfNombre.getText()) ) )
                tfNombre.setBackground(Color.red);
            else
                tfNombre.setBackground(Color.white);
            
            if(  tf_Costo.getText().equals( "0.00" ))
                tf_Costo.setBackground(Color.red);
            else
                tf_Costo.setBackground(Color.white);
            
        }
    
    }
    @Override
        protected void mostrarR(){
      DefaultTableModel modelo = (DefaultTableModel) Datos.getModel();
        List<Servicios> datos = new ArrayList<> ();
        String [] columna = new String [3] ;
        
        while(modelo.getRowCount() > 0){
            modelo.setRowCount(0);}
        
        datos = ConsultasBD.obtenerServicios();
        
        for (int i = 0 ; i < datos.size() ; i++ ){
                
                columna [0] = (Integer.toString( datos.get(i).getId()) );
                columna [1] =   datos.get(i).getNombre();
                columna [2] =   Float.toString( datos.get(i).getCosto() );
                
                modelo.addRow(columna); 
            }
        
        Datos.setModel(modelo); 
    }
    
    @Override
    protected void buscarR(){
        
         String respuesta = JOptionPane.showInputDialog( "Inserte el nombre del servicio a buscar");
         
         if( ( !respuesta.equals("") || !(respuesta == null ) ) & 
                (ConsultasBD.existeServicios(respuesta))) {
                      Servicios datos = new Servicios(); 
                      datos = ConsultasBD.buscarServicios( respuesta ).get(0) ;
                      editar = true;
                      
                      txt_ID.setText( "ID:" + Integer.toString( datos.getId() ) ) ;
                     
                      tf_Costo.setText( Float.toString(  datos.getCosto() ));
                      tfNombre.setText(respuesta);
                      dato = respuesta;
                      
                      tfNombre.setBackground(Color.white);
                      tf_Costo.setBackground(Color.white);
         }
         else
            JOptionPane.showMessageDialog(null, "El Dato no existe"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
             
            
          
        
 
            
    }
    
    @Override
    protected void borrarR(){
         
         String respuesta = JOptionPane.showInputDialog( "Inserte el nombre del servicio a borrar");
         
         if( ( !respuesta.equals("") || !(respuesta == null ) ) & 
                ConsultasBD.existeServicios(respuesta) ) {
             int i = JOptionPane.showConfirmDialog(this, "Realmente desea borrar el dato","Confirmar    accion" ,JOptionPane.YES_NO_OPTION);
            
             if( i == 0){
              ConsultasBD.borrarServicios( respuesta ) ;
              mostrarR();
             }
                    
         }
         else
            JOptionPane.showMessageDialog(null, "El Dato no existe"
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
        panelFormulario = new javax.swing.JPanel();
        txt_Nombre = new javax.swing.JLabel();
        txt_ID = new javax.swing.JLabel();
        tfNombre = new javax.swing.JTextField();
        txt_Nombre1 = new javax.swing.JLabel();
        tf_Costo = new javax.swing.JFormattedTextField();
        paneAcciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        Txt_Infor1 = new javax.swing.JLabel();
        Txt_Infor2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Datos = new javax.swing.JTable();
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
        Txt_Infor.setText("Servicios");
        Txt_Infor.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Txt_Infor.setAlignmentX(0.5F);
        Txt_Infor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jp_BarraHerramientasLayout.setVerticalGroup(
            jp_BarraHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_BarraHerramientasLayout.createSequentialGroup()
                .addGroup(jp_BarraHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BotonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Txt_Ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(Txt_Infor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jp_BarraHerramientas);
        jp_BarraHerramientas.setBounds(0, 0, 900, 100);

        panelFormulario.setBackground(new java.awt.Color(0, 204, 204));
        panelFormulario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        panelFormulario.setLayout(null);

        txt_Nombre.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Nombre.setText("Precio:");
        panelFormulario.add(txt_Nombre);
        txt_Nombre.setBounds(40, 60, 100, 30);

        txt_ID.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_ID.setText("ID:");
        panelFormulario.add(txt_ID);
        txt_ID.setBounds(40, 110, 100, 30);

        tfNombre.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        tfNombre.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfNombre.setText("Ingrese el Nombre");
        panelFormulario.add(tfNombre);
        tfNombre.setBounds(160, 20, 220, 30);

        txt_Nombre1.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Nombre1.setText("Nombre:");
        panelFormulario.add(txt_Nombre1);
        txt_Nombre1.setBounds(40, 20, 100, 30);

        tf_Costo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        panelFormulario.add(tf_Costo);
        tf_Costo.setBounds(160, 70, 220, 30);

        jPanel1.add(panelFormulario);
        panelFormulario.setBounds(30, 150, 420, 190);

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
        btnGuardar.setBounds(30, 30, 80, 50);

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
        btnBorrar.setBounds(170, 120, 80, 50);

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
        btnBuscar.setBounds(170, 30, 80, 50);

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
        btnEditar.setBounds(30, 120, 80, 50);

        jPanel1.add(paneAcciones);
        paneAcciones.setBounds(570, 150, 280, 190);

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
        Txt_Infor2.setBounds(120, 110, 190, 40);

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));

        Datos.setAutoCreateRowSorter(true);
        Datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Datos);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 360, 820, 310);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(344, Short.MAX_VALUE)
                .addComponent(Txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(Txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(611, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton BotonSalir;
    private javax.swing.JTable Datos;
    protected javax.swing.JLabel Txt_Infor;
    protected javax.swing.JLabel Txt_Infor1;
    protected javax.swing.JLabel Txt_Infor2;
    protected javax.swing.JLabel Txt_Ingreso;
    private javax.swing.JLabel Txt_user;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JPanel jp_BarraHerramientas;
    private javax.swing.JPanel paneAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JTextField tfNombre;
    private javax.swing.JFormattedTextField tf_Costo;
    private javax.swing.JLabel txt_ID;
    private javax.swing.JLabel txt_Nombre;
    private javax.swing.JLabel txt_Nombre1;
    // End of variables declaration//GEN-END:variables
}
