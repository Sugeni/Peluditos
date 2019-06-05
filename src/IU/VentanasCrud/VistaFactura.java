/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IU.VentanasCrud;

import BD4.ConsultasBD;
import Clases.Cliente;
import Clases.Empleado;
import Clases.Factura;
import Clases.Servicios;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
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
public class VistaFactura extends VistaModelo {
    String cedula;
    String nombre;
    int permisos;
    ArrayList<String> listaServicios= new ArrayList<String>();
    float subtotal = 0;
    float total = 0;
    float iva = 0;
    boolean editar = false;
    String dato;
    
    /**
     * Creates new form VistaFactura
     * @param padre
     */
    public VistaFactura( JFrame padre ) {
        
        super( padre );
        this.setVisible(false);
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        cargarCbServicios();
        mostrarR();
    }
    
        public VistaFactura( JFrame padre, String cedula,int permisos,String nombre ) {
        
        super( padre );
        this.setVisible(false);
        this.cedula= cedula;
        this.nombre= nombre;
        this.permisos=permisos;
        initComponents();
        this.inicializarComponentes();
        this.setLocationRelativeTo(null);
        cargarCbServicios();
        mostrarR();
    }

     @Override
    protected void actualizarR(){}
    @Override
    protected void crearR(){
             if ( ! ( (jfNombre1.getText().equals("") ) || !ConsultasBD.existeCliente(jfNombre1.getText()) ) 
                 ){
            if(!listaServicios.isEmpty()){
                Factura.contador = ConsultasBD.maxFactura();
                Factura datos = new Factura (jfNombre1.getText(),this.cedula) ;
                for(int x=0;x<listaServicios.size();x++) {
                    Servicios aux = new Servicios(); 
                    System.out.println(listaServicios.get(x));
                    aux = ConsultasBD.buscarServicios(listaServicios.get(x)).get(0);
                    System.out.println(aux.getId());
                    System.out.println(aux.getCosto());
                    datos.addServicio(aux);
                }
                ConsultasBD.agregarFactura(datos);
                JOptionPane.showMessageDialog(null, "Factura generada Exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE );
                mostrarR();
                jfNombre1.setText("");
                jfNombre1.setBackground(Color.white);
                jScrollPane3.setVisible(true);
                jScrollPane1.setVisible(false);
                listaServicios= new ArrayList<String>();
                try {
                    imprimirFactura(ConsultasBD.maxFactura());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(VistaFactura.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaFactura.class.getName()).log(Level.SEVERE, null, ex);
                }
             
                
            }else{
                JOptionPane.showMessageDialog(null, "Error: No ha agregado ningun item a la factura."
                                           , "Error", JOptionPane.ERROR_MESSAGE );
            }
            
            
        }    
        else
        {   JOptionPane.showMessageDialog(null, "Datos no Aceptados: Compruebe el nombre\nNo se aceptan Registros repetidos o vacios"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
        
            if ( (jfNombre1.getText().equals("Ingrese el Nombre") ) || (jfNombre1.getText().equals("") || !ConsultasBD.existeCliente(jfNombre1.getText()) ) )
                jfNombre1.setBackground(Color.red);
            else
                jfNombre1.setBackground(Color.white);
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
        DefaultTableModel modelo = (DefaultTableModel) Datos2.getModel();
        List<Factura> datos = new ArrayList<> ();
        String [] columna = new String [6] ;
        
        while(modelo.getRowCount() > 0){
            modelo.setRowCount(0);}
        
        datos = ConsultasBD.obtenerFactura();
        
        for (int i = 0 ; i < datos.size() ; i++ ){
                
                columna [0] =   Integer.toString(datos.get(i).getId());
                columna [1] =   datos.get(i).getIdEmpleado();
                columna [2] =   datos.get(i).getIdCliente();
                columna [3] =   datos.get(i).getFecha();
                columna [4] =   datos.get(i).getHora();
                columna [5] =   Float.toString(datos.get(i).getCostoTotal());
                modelo.addRow(columna); 
            }
        
        Datos2.setModel(modelo);
    }
    
    protected void mostrarRServicios(){
      DefaultTableModel modelo = (DefaultTableModel) Datos.getModel();
        List<Servicios> datos = new ArrayList<> ();
        String [] columna = new String [3] ;
        
        while(modelo.getRowCount() > 0){
            modelo.setRowCount(0);}
        
        datos = ConsultasBD.obtenerServicios();
        
        for (int i = 0 ; i < datos.size() ; i++ ){
                for(int x=0;x<listaServicios.size();x++) {
                    if(listaServicios.get(x).equals(datos.get(i).getNombre())){
                        columna [0] = (Integer.toString( datos.get(i).getId()) );
                        columna [1] =   datos.get(i).getNombre();
                        columna [2] =   Float.toString( datos.get(i).getCosto() );
                        modelo.addRow(columna); 
                    }
            }
        }
        
        Datos.setModel(modelo); 
    }
    private void cargarCbServicios(){
        ArrayList<String> lista = new ArrayList<String>();
        List<Servicios> componentes = ConsultasBD.obtenerServicios();
        
        for ( int i = 0 ; i < componentes.size() ; i++){
            lista.add( componentes.get(i).getNombre());
        }
        
         jCB_Servicio.removeAllItems();
            for (int i=0 ; i< lista.size() ; i++){
                jCB_Servicio.addItem(lista.get(i));
            }
    }
    
    private void agregarServicios(){
        jScrollPane1.setVisible(true);
        jScrollPane3.setVisible(false);
        String combo = (String) jCB_Servicio.getSelectedItem();
        this.listaServicios.add(combo);
        Servicios datos = new Servicios(); 
        datos = ConsultasBD.buscarServicios( combo ).get(0) ;
        subtotal += datos.getCosto();
        iva =(subtotal/100)*16;
        total=subtotal+iva;
        txt_Costo3.setText("Subtotal: "+Float.toString(subtotal)+" BS.S");
        txt_Costo2.setText("Iva: "+Float.toString(iva)+" BS.S");
        txt_Costo.setText("Total: "+Float.toString(total)+" BS.S");
        mostrarRServicios();
    }
    private void removerServicios(){
        jScrollPane1.setVisible(true);
        jScrollPane3.setVisible(false);
        String combo = (String) jCB_Servicio.getSelectedItem();
        for(int x=0;x<listaServicios.size();x++) {
            if(listaServicios.get(x).equals(combo)){
                listaServicios.remove(x);
                Servicios datos = new Servicios(); 
                datos = ConsultasBD.buscarServicios( combo ).get(0) ;
                subtotal -= datos.getCosto();
                iva =(subtotal/100)*16;
                total=subtotal+iva;
                txt_Costo3.setText("Subtotal: "+Float.toString(subtotal)+" BS.S");
                txt_Costo2.setText("Iva: "+Float.toString(iva)+" BS.S");
                txt_Costo.setText("Total: "+Float.toString(total)+" BS.S");
                mostrarRServicios();
                break;
            }
        }
        
        mostrarRServicios();
    }
    
    @Override
    protected void buscarR(){
        
        String respuesta = JOptionPane.showInputDialog( "Inserte el ID de la Factura a buscar");
               respuesta = respuesta.trim();
               dato = respuesta.trim();
               
             if( ( !respuesta.equals("") ) & 
                (ConsultasBD.existeFactura( Integer.parseInt(respuesta)) )){
                    
                 try {
                    imprimirFactura(Integer.parseInt(respuesta));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(VistaFactura.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(VistaFactura.class.getName()).log(Level.SEVERE, null, ex);
                }

                }
             else
                  JOptionPane.showMessageDialog(null, "No se encontro el registro"
                                           , "Error", JOptionPane.ERROR_MESSAGE );
        
    }
    @Override
    protected void borrarR(){
        String respuesta = JOptionPane.showInputDialog( "Inserte el ID de la factura a borrar");
        respuesta = respuesta.trim();

        if( ( !respuesta.equals("") ) & 
           (ConsultasBD.existeFactura( Integer.parseInt(respuesta)) )){
            int i = JOptionPane.showConfirmDialog(this, "Realmente desea borrar la factura","Confirmar    accion" ,JOptionPane.YES_NO_OPTION);

           if( i == 0){
               ConsultasBD.borrarFactura( Integer.parseInt(respuesta));                                       
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
        paneAcciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnFacturar = new javax.swing.JButton();
        panelFormulario = new javax.swing.JPanel();
        txt_Cedula1 = new javax.swing.JLabel();
        jCB_Servicio = new javax.swing.JComboBox<>();
        txt_Servicio = new javax.swing.JLabel();
        txt_Costo = new javax.swing.JLabel();
        txt_Cedula2 = new javax.swing.JLabel();
        jfNombre1 = new javax.swing.JFormattedTextField();
        btn_Eliminar = new javax.swing.JButton();
        btn_Agregar = new javax.swing.JButton();
        txt_Costo2 = new javax.swing.JLabel();
        jfCedulaE1 = new javax.swing.JFormattedTextField();
        txt_Cedula3 = new javax.swing.JLabel();
        txt_Costo3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Datos2 = new javax.swing.JTable();
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
        Txt_Infor.setText("Factura");
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
        Txt_Infor2.setBounds(190, 110, 190, 40);

        paneAcciones.setBackground(new java.awt.Color(255, 255, 0));
        paneAcciones.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        paneAcciones.setForeground(new java.awt.Color(204, 204, 204));
        paneAcciones.setLayout(null);

        btnGuardar.setBackground(new java.awt.Color(0, 0, 204));
        btnGuardar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Guardar.png"))); // NOI18N
        btnGuardar.setText("Facturar");
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
        btnGuardar.setBounds(40, 30, 80, 50);

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
        btnBorrar.setBounds(160, 110, 80, 50);

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
        btnBuscar.setBounds(160, 30, 80, 50);

        btnFacturar.setBackground(new java.awt.Color(153, 0, 153));
        btnFacturar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11)); // NOI18N
        btnFacturar.setForeground(new java.awt.Color(255, 255, 255));
        btnFacturar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Historial.png"))); // NOI18N
        btnFacturar.setText("Reporte");
        btnFacturar.setToolTipText("");
        btnFacturar.setBorder(null);
        btnFacturar.setBorderPainted(false);
        btnFacturar.setContentAreaFilled(false);
        btnFacturar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnFacturar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFacturar.setOpaque(true);
        btnFacturar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Crud/Historial2.png"))); // NOI18N
        btnFacturar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFacturar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFacturarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFacturarMouseExited(evt);
            }
        });
        btnFacturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturarActionPerformed(evt);
            }
        });
        paneAcciones.add(btnFacturar);
        btnFacturar.setBounds(40, 110, 80, 50);

        jPanel1.add(paneAcciones);
        paneAcciones.setBounds(570, 150, 280, 190);

        panelFormulario.setBackground(new java.awt.Color(0, 204, 204));
        panelFormulario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));
        panelFormulario.setLayout(null);

        txt_Cedula1.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Cedula1.setText("ID:");
        panelFormulario.add(txt_Cedula1);
        txt_Cedula1.setBounds(10, 10, 100, 30);

        jCB_Servicio.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        jCB_Servicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelFormulario.add(jCB_Servicio);
        jCB_Servicio.setBounds(120, 110, 310, 30);

        txt_Servicio.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Servicio.setText("Servicios:");
        panelFormulario.add(txt_Servicio);
        txt_Servicio.setBounds(10, 110, 100, 30);

        txt_Costo.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Costo.setText("Total:");
        panelFormulario.add(txt_Costo);
        txt_Costo.setBounds(10, 210, 410, 30);

        txt_Cedula2.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Cedula2.setText("Cliente:");
        panelFormulario.add(txt_Cedula2);
        txt_Cedula2.setBounds(270, 50, 120, 30);

        jfNombre1.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        panelFormulario.add(jfNombre1);
        jfNombre1.setBounds(360, 50, 160, 30);

        btn_Eliminar.setBackground(new java.awt.Color(0, 153, 0));
        btn_Eliminar.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        btn_Eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_Eliminar.setText("-");
        btn_Eliminar.setToolTipText("Elimina el ultimo servicio agregado en la lista");
        btn_Eliminar.setAlignmentY(0.0F);
        btn_Eliminar.setContentAreaFilled(false);
        btn_Eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_Eliminar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn_Eliminar.setOpaque(true);
        btn_Eliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_EliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_EliminarMouseExited(evt);
            }
        });
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });
        panelFormulario.add(btn_Eliminar);
        btn_Eliminar.setBounds(470, 110, 45, 30);

        btn_Agregar.setBackground(new java.awt.Color(0, 153, 0));
        btn_Agregar.setFont(new java.awt.Font("Lucida Bright", 1, 18)); // NOI18N
        btn_Agregar.setForeground(new java.awt.Color(255, 255, 255));
        btn_Agregar.setText("+");
        btn_Agregar.setToolTipText("Agrega el servicio seleccionado a la lista");
        btn_Agregar.setAlignmentY(0.0F);
        btn_Agregar.setContentAreaFilled(false);
        btn_Agregar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_Agregar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn_Agregar.setOpaque(true);
        btn_Agregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_AgregarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_AgregarMouseExited(evt);
            }
        });
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });
        panelFormulario.add(btn_Agregar);
        btn_Agregar.setBounds(430, 110, 45, 30);

        txt_Costo2.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Costo2.setText("Iva:");
        panelFormulario.add(txt_Costo2);
        txt_Costo2.setBounds(10, 180, 400, 30);

        jfCedulaE1.setEditable(false);
        jfCedulaE1.setText(this.cedula);
        jfCedulaE1.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 18)); // NOI18N
        panelFormulario.add(jfCedulaE1);
        jfCedulaE1.setBounds(120, 50, 130, 30);

        txt_Cedula3.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Cedula3.setText("Empleado:");
        panelFormulario.add(txt_Cedula3);
        txt_Cedula3.setBounds(10, 50, 120, 30);

        txt_Costo3.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 24)); // NOI18N
        txt_Costo3.setText("Subtotal:");
        panelFormulario.add(txt_Costo3);
        txt_Costo3.setBounds(10, 150, 330, 30);

        jPanel1.add(panelFormulario);
        panelFormulario.setBounds(30, 150, 530, 260);

        jScrollPane3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 0), 2, true));

        Datos2.setAutoCreateRowSorter(true);
        Datos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Empleado", "Cliente", "Fecha", "Hora", "Costo Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(Datos2);
        if (Datos2.getColumnModel().getColumnCount() > 0) {
            Datos2.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(30, 430, 820, 250);

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
        jScrollPane1.setBounds(30, 430, 820, 250);

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
        ConsultasBD.escribirFactura(ConsultasBD.obtenerFactura());
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnFacturarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFacturarMouseEntered
        btnFacturar.setBackground( new java.awt.Color  (204,0,255) );
    }//GEN-LAST:event_btnFacturarMouseEntered

    private void btnFacturarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFacturarMouseExited
        btnFacturar.setBackground( new java.awt.Color  (153,0,153) );
    }//GEN-LAST:event_btnFacturarMouseExited

    private void btnFacturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturarActionPerformed

    }//GEN-LAST:event_btnFacturarActionPerformed

    private void btn_EliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EliminarMouseEntered
        btn_Eliminar.setBackground( new java.awt.Color (255,102,0) );
    }//GEN-LAST:event_btn_EliminarMouseEntered

    private void btn_EliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EliminarMouseExited
        btn_Eliminar.setBackground( new java.awt.Color(0,153,0) );
    }//GEN-LAST:event_btn_EliminarMouseExited

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        removerServicios();
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void btn_AgregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarMouseEntered
        btn_Agregar.setBackground( new java.awt.Color (255,102,0) );
    }//GEN-LAST:event_btn_AgregarMouseEntered

    private void btn_AgregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_AgregarMouseExited
        btn_Agregar.setBackground( new java.awt.Color(0,153,0) );
    }//GEN-LAST:event_btn_AgregarMouseExited

    private void btn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarActionPerformed
        // TODO add your handling code here:
        agregarServicios();
    }//GEN-LAST:event_btn_AgregarActionPerformed

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton BotonSalir;
    private javax.swing.JTable Datos;
    private javax.swing.JTable Datos2;
    protected javax.swing.JLabel Txt_Infor;
    protected javax.swing.JLabel Txt_Infor1;
    protected javax.swing.JLabel Txt_Infor2;
    protected javax.swing.JLabel Txt_Ingreso;
    private javax.swing.JLabel Txt_user;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnFacturar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btn_Agregar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JComboBox<String> jCB_Servicio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JFormattedTextField jfCedulaE1;
    private javax.swing.JFormattedTextField jfNombre1;
    protected javax.swing.JPanel jp_BarraHerramientas;
    private javax.swing.JPanel paneAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JLabel txt_Cedula1;
    private javax.swing.JLabel txt_Cedula2;
    private javax.swing.JLabel txt_Cedula3;
    private javax.swing.JLabel txt_Costo;
    private javax.swing.JLabel txt_Costo2;
    private javax.swing.JLabel txt_Costo3;
    private javax.swing.JLabel txt_Servicio;
    // End of variables declaration//GEN-END:variables
}
