/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IU.VentanasCrud;

import BD4.ConsultasBD;
import Clases.Cliente;
import Clases.Consulta;
import Clases.Empleado;
import Clases.Factura;
import Clases.Mascota;
import Clases.Servicios;
import IU.VentanaAdministrador;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Efigenia
 */
public class VistaConsultas extends VistaModelo {
    String cedula;
    String nombre;
    int permisos;
    boolean editar = false;
    String dato;
    
    public VistaConsultas( JFrame padre ) {
        super( padre );
        this.setVisible(false);
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        mostrarR();
       
    }
    
        public VistaConsultas( JFrame padre, String cedula,int permisos,String nombre ) {
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
    

    
    @Override
    protected void actualizarR(){
     if(editar){
         
         Consulta consulta = ConsultasBD.buscarConsulta( Integer.parseInt(dato),  true ).get(0);
         
         consulta.setCosto( Float.parseFloat( jFCosto.getText() ));
         consulta.setDiagnostico( tfDiagnostico.getText() );
         consulta.setTratamiento( tfTratamiento.getText());
         ConsultasBD.modificarConsulta(consulta, Integer.parseInt(dato) );
         
     }
     else
     {  JOptionPane.showMessageDialog(null, "Primero busque el registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );     
     } 
    }
    @Override
    protected void crearR(){
     if( ! (jTCedula.getText().trim().equals("") || jfMascota.getText().trim().equals("")  
            || jFCosto.getText().trim().equals("")) ){
     
     Consulta.contador = ConsultasBD.maxConsulta();
     Consulta registro = new Consulta ( tfDiagnostico.getText(), tfTratamiento.getText(), 
                                        this.cedula, Integer.parseInt( jfMascota.getText()),
                                        Float.parseFloat(jFCosto.getText())
                                       );
     
     ConsultasBD.agregarConsulta(registro);
     
     Servicios servicio;
     servicio = ConsultasBD.buscarServicios("Consulta").get(0);
     Factura.contador = ConsultasBD.maxFactura();
     Factura facturado = new Factura( jTCedula.getText(), this.cedula, servicio);
     ConsultasBD.agregarFactura(facturado);
     try {
                    imprimirFactura(ConsultasBD.maxFactura());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(VistaFactura.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaFactura.class.getName()).log(Level.SEVERE, null, ex);
                }
     JOptionPane.showMessageDialog(null, "Datos guardados", "Exito", JOptionPane.INFORMATION_MESSAGE );
     mostrarR();
     }
     else{
         JOptionPane.showMessageDialog(null, "Los campos Costo, Cedula y Mascota no deben estar vacios o llevar el membrete original"
                                           , "Error", JOptionPane.ERROR_MESSAGE );     
     }
     
    }
    
    public void imprimirFactura(int id) throws FileNotFoundException, DocumentException{
        Factura factura = ConsultasBD.buscarFactura(id).get(0);
        System.out.println(factura.getIdCliente());
        System.out.println(factura.getIdEmpleado());
        System.out.println(factura.getId());
        Cliente cliente = ConsultasBD.buscarCliente(factura.getIdCliente()).get(0);
        Empleado empleado = ConsultasBD.buscarEmpleado(factura.getIdEmpleado()).get(0);
        
        // Se crea el documento// Se crea el documento
        Document documento = new Document();

        // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
        FileOutputStream ficheroPdf = new FileOutputStream("fichero.pdf");

        // Se asocia el documento al OutputStream y se indica que el espaciado entre
        // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
        PdfWriter.getInstance(documento,ficheroPdf).setInitialLeading(20);

        // Se abre el documento.
        documento.open();
        
        Paragraph para = new Paragraph("Clinica Peluditos Pet",
				FontFactory.getFont("arial",   // fuente
				22,                            // tamaño
				Font.ITALIC,                   // estilo
				BaseColor.GREEN));
        para.setAlignment(Element.ALIGN_CENTER);
        documento.add(para);
        documento.add(new Paragraph(" "));
        para = new Paragraph("Factura N° "+factura.getId());
        para.setAlignment(Element.ALIGN_CENTER);
        documento.add(para);
        para = new Paragraph("Fecha: "+factura.getFecha()+" Hora: "+factura.getHora());
        para.setAlignment(Element.ALIGN_LEFT);
        documento.add(para);
        documento.add(new Paragraph(" "));
        
        PdfPTable cabecera = new PdfPTable(2);
        
        para = new Paragraph("Empleado: "+empleado.getNombre()+" "+empleado.getApellido());
        PdfPCell cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph("Cliente: "+cliente.getNombre()+" "+cliente.getApellido());
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph("cedula: "+empleado.getCedula());
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph("cedula: "+cliente.getCedula());
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        documento.add(cabecera);
        
        documento.add(new Paragraph(" "));
        
        PdfPTable tabla = new PdfPTable(2);
        para = new Paragraph("Servicio",
				FontFactory.getFont("arial",   // fuente
				16,                            // tamaño
				Font.ITALIC,                   // estilo
				BaseColor.BLACK));
        tabla.addCell(para);
        
        para = new Paragraph("Costo",
                            FontFactory.getFont("arial",   // fuente
                            16,                            // tamaño
                            Font.ITALIC,                   // estilo
                            BaseColor.BLACK));
        tabla.addCell(para);
        
         for(int x=0;x<factura.getServicios().size();x++) {
             
            Servicios aux = ConsultasBD.buscarServicios(factura.getServicios().get(x)).get(0);
            tabla.addCell(aux.getNombre());
            tabla.addCell(Float.toString(factura.getCostos().get(x))+" BS.S");
         }
         
        documento.add(tabla);
        
        documento.add(new Paragraph(" "));
        
        cabecera = new PdfPTable(2);
        
        para = new Paragraph(" ");
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph("Subtotal: "+factura.getSubTotal()+" BS.S");
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph(" ");
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph("Iva: "+factura.getIva()+" BS.S");
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph(" ");
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        para = new Paragraph("Total: "+factura.getCostoTotal()+" BS.S");
        cellOne = new PdfPCell(para);
        cellOne.setBorder(0);
        cabecera.addCell(cellOne);
        
        documento.add(cabecera);
        
        
        
        documento.close();
        
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("fichero.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }
    @Override
    protected void mostrarR(){
        DefaultTableModel modelo = (DefaultTableModel) Datos.getModel();
        List<Consulta> datos = new ArrayList<> ();
        String [] columna = new String [6] ;
        
        while(modelo.getRowCount() > 0){
            modelo.setRowCount(0);}
        
        datos = ConsultasBD.obtenerConsulta();
        
        for (int i = 0 ; i < datos.size() ; i++ ){
                
                columna [0] =   Integer.toString(datos.get(i).getId());
                columna [1] =   datos.get(i).getIDVeterinario();
                columna [2] =   Integer.toString(datos.get(i).getMascota() );
                columna [3] =   Float.toString(datos.get(i).getCosto());
                columna [4] =   datos.get(i).getDiagnostico();
                columna [5] =   datos.get(i).getTratamiento();
                modelo.addRow(columna); 
            }
        
        Datos.setModel(modelo);
    }
    @Override
    protected void buscarR(){
               
               String respuesta = JOptionPane.showInputDialog( "Inserte el ID de la Consulta a buscar");
               respuesta = respuesta.trim();
               dato = respuesta.trim();
               Consulta consulta;
             if( ( !respuesta.equals("") ) & 
                (ConsultasBD.existeConsultabyID( Integer.parseInt(respuesta)) )){
                 
               
                    consulta = ConsultasBD.buscarConsulta( Integer.parseInt( respuesta ) , true).get( 0 );                                       
                    jFCosto.setText( Float.toString( consulta.getCosto()));
                    tfDiagnostico.setText( consulta.getDiagnostico() );
                    tfTratamiento.setText( consulta.getTratamiento());
                    jfMascota.setText( Integer.toString(consulta.getMascota()));
                    Mascota animal = ConsultasBD.buscarMascota( consulta.getMascota(), true).get(0);
                    jTCedula.setText( animal.getIdCliente());
                
                    mostrarR();
                }
                 
             
             else
                  JOptionPane.showMessageDialog(null, "No se encontro el registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
    
    }
    @Override
    protected void borrarR(){
               String respuesta = JOptionPane.showInputDialog( "Inserte el ID de la Consulta a borrar");
               respuesta = respuesta.trim();
               
             if( ( !respuesta.equals("") ) & 
                (ConsultasBD.existeConsultabyID( Integer.parseInt(respuesta)) )){
                 int i = JOptionPane.showConfirmDialog(this, "Realmente desea borrar el dato","Confirmar    accion" ,JOptionPane.YES_NO_OPTION);
            
                if( i == 0){
                    ConsultasBD.borrarConsulta( Integer.parseInt( respuesta ) , true);                                       
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
        Txt_Infor1 = new javax.swing.JLabel();
        Txt_Infor2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Datos = new javax.swing.JTable();
        paneAcciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        panelFormulario = new javax.swing.JPanel();
        txt_Costo = new javax.swing.JLabel();
        txt_Cedula = new javax.swing.JLabel();
        txt_Mascota = new javax.swing.JLabel();
        txt_ID = new javax.swing.JLabel();
        jFCosto = new javax.swing.JFormattedTextField();
        txt_Diagnostico = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfDiagnostico = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        tfTratamiento = new javax.swing.JTextArea();
        txt_Tratamiento = new javax.swing.JLabel();
        jfMascota = new javax.swing.JFormattedTextField();
        jTCedula = new javax.swing.JTextField();
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
        Txt_Infor.setText("Consultas");
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
        Txt_Infor2.setBounds(170, 110, 190, 40);

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));

        Datos.setAutoCreateRowSorter(true);
        Datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Veterinario", "Mascota", "Costo", "Diagnostico", "Tratamiento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(Datos);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 420, 820, 250);

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
        btnGuardar.setBounds(50, 30, 80, 50);

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
        btnBorrar.setBounds(170, 110, 80, 50);

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
        btnEditar.setBounds(50, 110, 80, 50);

        jPanel1.add(paneAcciones);
        paneAcciones.setBounds(570, 150, 280, 190);

        panelFormulario.setBackground(new java.awt.Color(0, 204, 204));
        panelFormulario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        panelFormulario.setLayout(null);

        txt_Costo.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Costo.setText("Costo:");
        panelFormulario.add(txt_Costo);
        txt_Costo.setBounds(10, 50, 100, 30);

        txt_Cedula.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Cedula.setText("Cedula:");
        panelFormulario.add(txt_Cedula);
        txt_Cedula.setBounds(270, 10, 100, 30);

        txt_Mascota.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Mascota.setText("Mascota:");
        panelFormulario.add(txt_Mascota);
        txt_Mascota.setBounds(270, 50, 100, 30);

        txt_ID.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_ID.setText("ID:");
        panelFormulario.add(txt_ID);
        txt_ID.setBounds(10, 10, 100, 30);

        jFCosto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        panelFormulario.add(jFCosto);
        jFCosto.setBounds(90, 50, 170, 30);

        txt_Diagnostico.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Diagnostico.setText("Diagnostico:");
        panelFormulario.add(txt_Diagnostico);
        txt_Diagnostico.setBounds(10, 90, 190, 30);

        tfDiagnostico.setColumns(20);
        tfDiagnostico.setFont(new java.awt.Font("Leelawadee UI", 1, 10)); // NOI18N
        tfDiagnostico.setRows(5);
        jScrollPane2.setViewportView(tfDiagnostico);

        panelFormulario.add(jScrollPane2);
        jScrollPane2.setBounds(10, 130, 240, 120);

        tfTratamiento.setColumns(20);
        tfTratamiento.setFont(new java.awt.Font("Leelawadee UI", 1, 10)); // NOI18N
        tfTratamiento.setRows(5);
        jScrollPane3.setViewportView(tfTratamiento);

        panelFormulario.add(jScrollPane3);
        jScrollPane3.setBounds(270, 130, 250, 120);

        txt_Tratamiento.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Tratamiento.setText("Tratamiento:");
        panelFormulario.add(txt_Tratamiento);
        txt_Tratamiento.setBounds(270, 90, 190, 30);

        jfMascota.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jfMascota.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        panelFormulario.add(jfMascota);
        jfMascota.setBounds(380, 50, 130, 30);
        panelFormulario.add(jTCedula);
        jTCedula.setBounds(380, 10, 130, 30);

        jPanel1.add(panelFormulario);
        panelFormulario.setBounds(30, 150, 530, 260);

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
                .addGap(51, 51, 51)
                .addComponent(Txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(609, Short.MAX_VALUE))
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
        /*ConsultasBD.escribirConsulta(ConsultasBD.obtenerConsulta());
        Consulta aux= ConsultasBD.buscarConsultabyID(10).get(0);
        System.out.println(aux.getMascota());*/
        
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
    private javax.swing.JFormattedTextField jFCosto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTCedula;
    private javax.swing.JFormattedTextField jfMascota;
    protected javax.swing.JPanel jp_BarraHerramientas;
    private javax.swing.JPanel paneAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JTextArea tfDiagnostico;
    private javax.swing.JTextArea tfTratamiento;
    private javax.swing.JLabel txt_Cedula;
    private javax.swing.JLabel txt_Costo;
    private javax.swing.JLabel txt_Diagnostico;
    private javax.swing.JLabel txt_ID;
    private javax.swing.JLabel txt_Mascota;
    private javax.swing.JLabel txt_Tratamiento;
    // End of variables declaration//GEN-END:variables
}
