/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class principal extends javax.swing.JFrame {

    private LineaNum numlinea;
    private SistemaArch archivo;
    Lexer lexico;
    public int cont, contant = 0;
    public boolean band;
    public Stack<String> pilaAuxiliar = new Stack();
    public Stack<String> pilaPrincipal = new Stack();
    public ArrayList<String> simbolosTerm = new ArrayList<>(Arrays.asList("id", "num", "int", "float", "char", 
            ",", ";", "+", "-", "*", "/", "=", "(", ")")); //Simbolos terminales o tokens
    public ArrayList<String> columnas = new ArrayList<>(Arrays.asList("id", "num", "int", "float", "char", 
            ",", ";", "+", "-", "*", "/", "=", "(", ")", "$", "P", "Tipo", 
            "V", "A", "S", "E", "T", "F")); //Columnas de la tabla sintactica
    public String componente; //Va guardando de uno por uno los componentes del arreglo durante el for
    public String produccionesP[][] = {
        {"P'", "P"}, {"P", "Tipo id V"}, {"P", "A"}, {"Tipo", "int"}, {"Tipo", "float"}, {"Tipo", "char"},
        {"V", ", id V"}, {"V", "; P"}, {"A", "id = S ;"}, {"S", "+ E"}, {"S", "- E"}, {"S", "E"},
        {"E", "E + T"}, {"E", "E - T"}, {"E", "T"}, {"T", "T * F"}, {"T", "T / F"}, {"T", "F"},
        {"F", "( E )"}, {"F", "id"}, {"F", "num"}}; //Priducciones de la gramatica 
    public String[][] transicion = {
        {"I 7","err","I 4","I 5","I 6","err","err","err","err","err","err","err","err","err","err","I1","I2","err","I 3","err","err","err","err"},
        {"err","err","err","err","err","err","err","err","err","err","err","err","err","err","P 0","err","err","err","err","err","err","err","err"},
        {"I 8","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","err","err","err","err","err","err","err","err","P 2","err","err","err","err","err","err","err","err"},
        {"P 3","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"P 4","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"P 5","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","err","err","err","err","err","I 9","err","err","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","I 11","I 12","err","err","err","err","err","err","err","err","err","err","I 10","err","err","err","err","err"},
        {"I 20","I 21","err","err","err","err","err","I 14","I 15","err","err","err","I 19","err","err","err","err","err","err","I 13","I 16","I 17","I 18"},
        {"err","err","err","err","err","err","err","err","err","err","err","err","err","err","P 1","err","err","err","err","err","err","err","err"},
        {"I 22","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"I 7","err","I 4","I 5","I 6","err","err","err","err","err","err","err","err","err","err","I 23","I 2","err","I 3","err","err","err","err"},
        {"err","err","err","err","err","err","I 24","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","I 25","I 17","I 18"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","I 26","I 17","I 18"},
        {"err","err","err","err","err","err","P 11","I 27","I 28","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 14","P 14","P 14","I 29","I 30","err","err","P 14","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 17","P 17","P 17","P 17","P 17","err","err","P 17","err","err","err","err","err","err","err","err","err"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","I 31","I 17","I 18"},
        {"err","err","err","err","err","err","P 19","P 19","P 19","P 19","P 19","err","err","P 19","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 20","P 20","P 20","P 20","P 20","err","err","P 20","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","I 11","I 12","err","err","err","err","err","err","err","err","err","err","I 32","err","err","err","err","err"},
        {"err","err","err","err","err","err","err","err","err","err","err","err","err","err","P 7","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","err","err","err","err","err","err","err","err","P 8","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 9","I 27","I 28","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 10","I 27","I 28","err","err","err","err","err","err","err","err","err","err","err","err","err","err"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","err","I 33","I 18"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","err","I 34","I 18"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","err","err","I 35"},
        {"I 20","I 21","err","err","err","err","err","err","err","err","err","err","I 19","err","err","err","err","err","err","err","err","err","I 36"},
        {"err","err","err","err","err","err","err","I 27","I 28","err","err","err","err","I 37","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","err","err","err","err","err","err","err","err","P 6","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 12","P 12","P 12","I 29","I 30","err","err","P 12","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 13","P 13","P 13","I 29","I 30","err","err","P 13","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 15","P 15","P 15","P 15","P 15","err","err","P 15","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 16","P 16","P 16","P 16","P 16","err","err","P 16","err","err","err","err","err","err","err","err","err"},
        {"err","err","err","err","err","err","P 18","P 18","P 18","P 18","P 18","err","err","P 18","err","err","err","err","err","err","err","err","err"},
    };
    String res, err;

    public principal() {
        initComponents();
        inicializar();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        ///Comentario importante
    }

    private void AnalisisLexico() {
        try {
            File codigo = new File("archivo.txt");
            FileOutputStream output = new FileOutputStream(codigo);
            byte[] bytes = txtCodigoBase.getText().getBytes();
            output.write(bytes);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(codigo), "UTF-8"));
            lexico = new Lexer(entrada);
            String accion = "";
            String errorLex = "";
            while (true) {
                Tokens token = lexico.yylex();
                if (token == null) {
                    //AnalisisSintactico("$", "", (infoToken.numeroLinea + 1) + ""); IMPORTANTE
                    //componentes = "";
                    Iterator it = lexico.tablaSimbolos.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, Token> entry = (Map.Entry) it.next();

                        //AnalisisSintactico(entry.getValue().getToken(), entry.getValue().getLexema(), (entry.getValue().getnumLinea() + 1)+"");
                        //componentes += entry.getValue().getToken()+",";
                        //System.out.println("Lexema: " + entry.getValue().getLexema() + " Token: " + entry.getValue().getToken() + " Numero de linea: " + (entry.getValue().getnumLinea() + 1));
                    }
                    accion += "";
                    txtLexico.setText(accion);
                    return;
                }
                switch (token) {
                    case Error:
                        errorLex += "Error lexico en la linea " + (lexico.posLinea + 1) + " simbolo: " + lexico.lexema + " incorecto" + "\n";
                        accion += "Error lexico en la linea " + (lexico.posLinea + 1) + " simbolo: " + lexico.lexema + " incorecto" + "\n";
                        txtAreaTerminal.setText(errorLex);
                        break;
                    default:
                        if (token.getSimbolo() == null) {
                            accion += token + "\n";
                            //AnalisisSintactico(token + "", lexico.lexema, (lexico.posLinea + 1) + "");
                            //IMPORTANTE
                        } else {
                            accion += token.getSimbolo() + "\n";
                            //AnalisisSintactico(token.getSimbolo(), lexico.lexema, (lexico.posLinea + 1) + "");
                            //IMPORTANTE
                        }
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void AnalisisSintactico(String comp, String lexema, String nlinea) {
        
    }

    private void InicializarPilas() {
        pilaPrincipal.clear();
//        cadena.add("$"); //Añade el terminador de cadena
        pilaAuxiliar.push("Z");
        pilaPrincipal.push("$");
        pilaPrincipal.push("P");
    }

    private void inicializar() {
        archivo = new SistemaArch();
        setTitle("Compilador LR");
        numlinea = new LineaNum(txtCodigoBase);
        jScrollPane2.setRowHeaderView(numlinea);
    }

    private void Cerrar() {
        String opciones[] = {"Cerrar", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Estas seguro de que quieres cerrar el programa? Todo cambio sin guardar se perdera", "Cierre de programa", 0, 0, null, opciones, EXIT_ON_CLOSE);
        if (eleccion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void LimpiarComp() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        txtCodigoBase = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLexico = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnArchivo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSintactico = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaTerminal = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCodigoBase.setColumns(20);
        txtCodigoBase.setRows(5);
        txtCodigoBase.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoBaseKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txtCodigoBase);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 53, 500, 260));

        txtLexico.setColumns(20);
        txtLexico.setRows(5);
        jScrollPane3.setViewportView(txtLexico);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 506, 180));

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setRollover(true);

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo-arch_24.png"))); // NOI18N
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);

        btnArchivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir-documento-24.png"))); // NOI18N
        btnArchivo.setFocusable(false);
        btnArchivo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArchivo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArchivoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnArchivo);

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar-24.png"))); // NOI18N
        btnGuardar.setFocusable(false);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGuardar);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/icons8-play-24.png"))); // NOI18N
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1116, 35));

        txtSintactico.setColumns(20);
        txtSintactico.setRows(5);
        jScrollPane4.setViewportView(txtSintactico);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 300, 506, 180));

        txtAreaTerminal.setColumns(20);
        txtAreaTerminal.setRows(5);
        jScrollPane1.setViewportView(txtAreaTerminal);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 500, 130));

        jLabel1.setText("Terminal");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 100, -1));

        jLabel2.setText("Análisis Léxico");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, 100, -1));

        jLabel3.setText("Análisis Sintáctico");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 280, 100, -1));

        jMenu1.setText("Archivo");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo-arch_24.png"))); // NOI18N
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir-documento-24.png"))); // NOI18N
        jMenuItem2.setText("Abrir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar-24.png"))); // NOI18N
        jMenuItem3.setText("Guardar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardarComo-24.png"))); // NOI18N
        jMenuItem4.setText("Guardar como");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icons/IconoIDE.png"));
        return retValue;
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        txtLexico.setText("");
        archivo.Nuevo(this);
        Limpiar();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Cerrar();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        archivo.Guardar(this);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        txtLexico.setText("");
        archivo.Abrir(this);
        Limpiar();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        txtLexico.setText("");
        archivo.guardarC(this);
        Limpiar();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void txtCodigoBaseKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoBaseKeyReleased
        int key = evt.getKeyCode();
        //if (KeyEvent.getKeyText(key).length() > 0) {
        if ((key >= 65 && key <= 90) || (key >= 48 && key <= 57) || (key >= 97 && key <= 122) || (key != 27 && (key >= 37
                && key <= 40) && !(key >= 16 && key <= 18) && key != 524 && key != 20)) {
            if (!getTitle().contains("*")) {
                setTitle(getTitle() + "*");
            }
            // Analisis dinamico
            //InicializarPilas();
            //res = "";
            //err = "";
            //AnalisisLexico();
            //AnalisisSintactico("$","","");
        }
    }//GEN-LAST:event_txtCodigoBaseKeyReleased

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        txtLexico.setText("");
        archivo.Nuevo(this);
        LimpiarComp();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArchivoActionPerformed
        txtLexico.setText("");
        archivo.Abrir(this);
        LimpiarComp();
    }//GEN-LAST:event_btnArchivoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        archivo.Guardar(this);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void Limpiar() {
        txtAreaTerminal.setText("");
        txtLexico.setText("");
        txtSintactico.setText("");
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        InicializarPilas();
        Limpiar();
        res = "";
        err = "";
        AnalisisLexico();
        //AnalisisSintactico("$","","");
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArchivo;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea txtAreaTerminal;
    public javax.swing.JTextArea txtCodigoBase;
    private javax.swing.JTextArea txtLexico;
    private javax.swing.JTextArea txtSintactico;
    // End of variables declaration//GEN-END:variables
}
