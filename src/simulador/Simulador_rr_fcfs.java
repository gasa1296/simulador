package simulador;


import static java.lang.Integer.parseInt;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabriel
 */
public class Simulador_rr_fcfs extends javax.swing.JFrame {
    int pcRam;  
    
    int ramRequerida=0;
    
    int Nprocesos=0;
    
    FCFS proceso[];
    FCFS procesoFCFS[];
    STF procesoSTF;
    STFM procesoSTFM[];
    RR rr;
            
    public Simulador_rr_fcfs() {
        
        initComponents();
        
        jAgregar.setEnabled(false);
        jTipo.setEnabled(false);
        jNombre.setEnabled(false);
        jRafaga.setEnabled(false);
        jQuantum.setEnabled(false);
        jTram.setEnabled(false);
        jIniciar.setEnabled(false);
        
        jDetener.setEnabled(false);
        jEliminar.setEnabled(false);
        
    }
    
    int porcentajeUtilizado(int i){
        int Rendimiento = Integer.parseInt((String)jIngreso.getValueAt(i, 2))*100;
        Rendimiento = (int) (Rendimiento/(Double.parseDouble((String)jRam.getText())));
        return Rendimiento;
    }
        
    private class RR extends Thread{ //Objeto de tipo Hilo con extension ejectubale
        boolean estado[];
        int rafaga[];
        public RR(int Nprocesos){
            estado = new boolean [Nprocesos];
            rafaga = new int[Nprocesos];
            for(int h = 0; h < estado.length; h++){
                estado[h] = true;
                rafaga[h] = Integer.parseInt((String)jIngreso.getValueAt(h, 1));
            } 
        }
        @Override
        public void run(){
            try{
                int procesosFinalizados = 0;
                while(procesosFinalizados < Nprocesos){
                    System.out.println("procesos fianlizados " + procesosFinalizados);
                    for(int j = 0; j < Nprocesos; j++){
                        int contadorQuantum = parseInt((String)(jIngreso.getValueAt(j,2)));
                        jIngreso.setValueAt("en proceso", j, 4);
                        while(contadorQuantum != 0 && rafaga[j] != 0 && estado[j] != false){
                            contadorQuantum--;
                            rafaga[j]--;
                            jIngreso.setValueAt(rafaga[j], j, 1);
                            Thread.sleep(950);
                            if(contadorQuantum == 0 && rafaga[j] != 0){
                                jIngreso.setValueAt("pausado",j,4);
                            }
                            else if(rafaga[j] == 0){
                                jIngreso.setValueAt("finalizado",j,4);
                                procesosFinalizados++;
                            }
                            if(estado[j] == false){
                                procesosFinalizados++;
                                jIngreso.setValueAt("detenido",j,4);
                            }
                        }
                    }
                }
            }catch(InterruptedException e){
                
            }
        }
    }
    
    public class FCFS extends Thread{
        boolean estado = true;
        int i;
        
        public FCFS(int i) {
            this.i = i;
        }
        
        @Override
        public void run() {
            try{
                ramRequerida += Integer.parseInt((String)jIngreso.getValueAt(i, 3));
                if(ramRequerida > pcRam){
                    jIngreso.setValueAt("en espera",i,4);
                    while(ramRequerida > pcRam){                    
                        Thread.sleep(950);
                    }
                }
                jIngreso.setValueAt("en proceso", i, 4);
                int j = Integer.parseInt((String)jIngreso.getValueAt(i, 1));
                while(j > 0){
                    j--;
                    jIngreso.setValueAt(String.valueOf(j),i,1);
                    Thread.sleep(950);
                    if(j == 0){
                        jIngreso.setValueAt("finalizado",i,4);
                        ramRequerida = ramRequerida - Integer.parseInt((String) jIngreso.getValueAt(i, 3));
                    }
                    if(estado == false){
                        j = 0;
                        jIngreso.setValueAt("matado",i,4);
                        ramRequerida = ramRequerida - Integer.parseInt((String) jIngreso.getValueAt(i, 3));
                    }
                }
            }
            catch(InterruptedException e){
            }            
        }
    }

    public class STFM extends Thread{
        boolean estado = true;
        int i;
        
        public STFM(int i) {
            this.i = i;
        }
        
        @Override
        public void run() {
            try{
                ramRequerida += Integer.parseInt((String)jIngreso.getValueAt(i, 2));
                if(ramRequerida > pcRam){
                    jIngreso.setValueAt("en espera",i,4);
                    jIngreso.setValueAt(0 + "%", i, 3);
                    while(ramRequerida > pcRam){                    
                        Thread.sleep(950);
                    }
                }
                jIngreso.setValueAt("activo", i, 4);                
                jIngreso.setValueAt(porcentajeUtilizado(i) + "%", i, 3);
                int j = Integer.parseInt((String)jIngreso.getValueAt(i, 1));
                while(j > 0){
                    j--;
                    jIngreso.setValueAt(String.valueOf(j),i,1);
                    Thread.sleep(950);
                    if(j == 0){
                        jIngreso.setValueAt("terminado",i,4);
                        jIngreso.setValueAt(0 + "%", i, 3);
                        ramRequerida = ramRequerida - Integer.parseInt((String) jIngreso.getValueAt(i, 2));
                    }
                    if(estado == false){
                        j = 0;
                        jIngreso.setValueAt("detenido",i,4);
                        jIngreso.setValueAt(0 + "%", i, 3);
                        ramRequerida = ramRequerida - Integer.parseInt((String) jIngreso.getValueAt(i, 2));
                    }
                }
            }
            catch(InterruptedException e){
            }            
        }
    }
    
    public class STF extends Thread{
        boolean estado[];
        boolean terminado = false;
        
        public STF(int Nprocesos) {
            estado = new boolean [Nprocesos];
            for(int k = 0; k < estado.length; k++){
                estado[k] = true;
            }            
        }
        
        @Override
        public void run() {
            try{
                for(int i = 0; i < Nprocesos; i++){
                    jIngreso.setValueAt("en espera",i,4);
                    jIngreso.setValueAt(0 + "%", i, 3);
                }
                for(int i = 0; i < Nprocesos; i++){
                    jIngreso.setValueAt("activo", procesosOrdenados[i][0], 4);                
                    jIngreso.setValueAt(porcentajeUtilizado(procesosOrdenados[i][0]) + "%", i, 3);
                    int j = Integer.parseInt((String)jIngreso.getValueAt(procesosOrdenados[i][0], 1));
                    while(j > 0){
                        j--;
                        jIngreso.setValueAt(String.valueOf(j),procesosOrdenados[i][0],1);
                        Thread.sleep(950);
                        if(j == 0){
                            jIngreso.setValueAt("terminado",procesosOrdenados[i][0],4);
                            jIngreso.setValueAt(0 + "%", procesosOrdenados[i][0], 3);
                        }
                        if(estado[i] == false){
                            j = 0;
                            jIngreso.setValueAt("detenido",procesosOrdenados[i][0],4);
                            jIngreso.setValueAt(0 + "%", procesosOrdenados[i][0], 3);
                        }
                    }
                }
            }
            catch(InterruptedException e){
            }            
        }
    }

    public class FCFS extends Thread{
        boolean estado = true;
        int i;
        
        public FCFS(int i) {
            this.i = i;
        }
        
        @Override
        public void run() {
            try{
                ramRequerida += Integer.parseInt((String)jIngreso.getValueAt(i, 2));
                if(ramRequerida > pcRam){
                    jIngreso.setValueAt("en espera",i,3);
                    jIngreso.setValueAt(0 + "%", i, 4);
                    while(ramRequerida > pcRam){                    
                        Thread.sleep(950);
                    }
                }
                jIngreso.setValueAt("en proceso", i, 3);                
                jIngreso.setValueAt(porcentajeUtilizado(i) + "%", i, 4);
                int j = Integer.parseInt((String)jIngreso.getValueAt(i, 1));
                while(j > 0){
                    j--;
                    jIngreso.setValueAt(String.valueOf(j),i,1);
                    Thread.sleep(950);
                    if(j == 0){
                        jIngreso.setValueAt("finalizado",i,3);
                        jIngreso.setValueAt(0 + "%", i, 4);
                        ramRequerida = ramRequerida - Integer.parseInt((String) jIngreso.getValueAt(i, 2));
                    }
                    if(estado == false){
                        j = 0;
                        jIngreso.setValueAt("matado",i,3);
                        jIngreso.setValueAt(0 + "%", i, 4);
                        ramRequerida = ramRequerida - Integer.parseInt((String) jIngreso.getValueAt(i, 2));
                    }
                }
            }
            catch(InterruptedException e){
            }            
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTipo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jIngreso = new javax.swing.JTable();
        jNombre = new javax.swing.JTextField();
        jRafaga = new javax.swing.JTextField();
        jQuantum = new javax.swing.JTextField();
        jTram = new javax.swing.JTextField();
        jRam = new javax.swing.JTextField();
        jPcAgregar = new javax.swing.JButton();
        jAgregar = new javax.swing.JButton();
        jIniciar = new javax.swing.JButton();
        jDetener = new javax.swing.JButton();
        jEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 255), new java.awt.Color(102, 102, 102)));

        jLabel2.setText("Algoritmo");

        jLabel3.setText("Nombre");

        jLabel5.setText("Quantum");

        jLabel6.setText("Memoria");

        jLabel8.setText("RAM");

        jLabel10.setText("Rafaga");

        jTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RR", "FCFS" }));
        jTipo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTipoActionPerformed(evt);
            }
        });

        jIngreso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Rafaga", "Quantum", "Memoria", "Estado"
            }
        ));
        jIngreso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jIngresoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jIngreso);

        jNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNombreActionPerformed(evt);
            }
        });

        jRafaga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRafagaActionPerformed(evt);
            }
        });
        jRafaga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jRafagaKeyTyped(evt);
            }
        });

        jQuantum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jQuantumActionPerformed(evt);
            }
        });
        jQuantum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jQuantumKeyTyped(evt);
            }
        });

        jTram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTramActionPerformed(evt);
            }
        });
        jTram.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTramKeyTyped(evt);
            }
        });

        jRam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRamActionPerformed(evt);
            }
        });
        jRam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jRamKeyTyped(evt);
            }
        });

        jPcAgregar.setText("Agregar");
        jPcAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPcAgregarActionPerformed(evt);
            }
        });

        jAgregar.setText("Agregar");
        jAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAgregarActionPerformed(evt);
            }
        });

        jIniciar.setText("Iniciar");
        jIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jIniciarActionPerformed(evt);
            }
        });

        jDetener.setText("Detener");
        jDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDetenerActionPerformed(evt);
            }
        });

        jEliminar.setText("Eliminar");
        jEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jIniciar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDetener)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRam, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jPcAgregar))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel10))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jRafaga, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTram, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jAgregar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jEliminar)))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPcAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRafaga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAgregar)
                    .addComponent(jEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jIniciar)
                    .addComponent(jDetener))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTipoActionPerformed
        if(jTipo.getSelectedIndex() == 1){
            jQuantum.setVisible(false);
            jLabel5.setVisible(false);
        }else{
            jQuantum.setVisible(true);
            jLabel5.setVisible(true);
        }
    }//GEN-LAST:event_jTipoActionPerformed

    private void jQuantumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jQuantumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jQuantumActionPerformed

    private void jTramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTramActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTramActionPerformed

    private void jAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAgregarActionPerformed
        if(Integer.parseInt(jTram.getText()) <= Integer.parseInt(jRam.getText())){
            
            jIngreso.setValueAt(jNombre.getText(), Nprocesos, 0);
            jIngreso.setValueAt(jRafaga.getText(), Nprocesos, 1);
            jIngreso.setValueAt(jQuantum.getText(), Nprocesos, 2);
            jIngreso.setValueAt(jTram.getText(), Nprocesos, 3);
            jIngreso.setValueAt("listo", Nprocesos, 4);
        
            Nprocesos ++;

            jNombre.setText(null);
            jRafaga.setText(null);
            jRafaga.setText(null);
            jQuantum.setText(null);
            jTram.setText(null);
            
            jNombre.grabFocus();
            
            jIniciar.setEnabled(true);
            
            jEliminar.setEnabled(true);
        }else{
            JOptionPane.showMessageDialog(null, "El proceso excede las capacidades de la computadora");
            jTram.setText(null);
            jTram.grabFocus();  
        }
        
    }//GEN-LAST:event_jAgregarActionPerformed

    private void jIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jIniciarActionPerformed
        if(jTipo.getSelectedIndex() == 0){
            rr = new RR(Nprocesos);
            rr.start();
        }else{            
            proceso = new FCFS[jIngreso.getRowCount()];
            for(int i = 0;i<jIngreso.getRowCount();i++){
                proceso[i] = new FCFS(i);
                proceso[i].start();
            }
        }
        
        jPcAgregar.setEnabled(false);        
        jRam.setEnabled(false);
        jTipo.setEnabled(false);
        jNombre.setEnabled(false);
        jRafaga.setEnabled(false);
        jQuantum.setEnabled(false);
        jTram.setEnabled(false);
        jAgregar.setEnabled(false);
        jIniciar.setEnabled(false);
        
        jDetener.setEnabled(true);
    }//GEN-LAST:event_jIniciarActionPerformed

    private void jRamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRamActionPerformed

    private void jNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jNombreActionPerformed

    private void jIngresoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jIngresoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jIngresoMouseClicked

    private void jPcAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPcAgregarActionPerformed
        pcRam = Integer.parseInt(jRam.getText());        
                
        jPcAgregar.setEnabled(false);
        
        jTipo.setEnabled(true);
        jNombre.setEnabled(true);
        jRafaga.setEnabled(true);
        jQuantum.setEnabled(true);
        jTram.setEnabled(true);
        jAgregar.setEnabled(true);   
        
        jTipo.grabFocus();
    }//GEN-LAST:event_jPcAgregarActionPerformed

    private void jRafagaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRafagaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRafagaActionPerformed

    private void jRamKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRamKeyTyped
        char c = evt.getKeyChar();
        try{
            int i = Integer.parseInt(Character.toString(c));            
        }catch(NumberFormatException e){
            evt.consume();
        }
    }//GEN-LAST:event_jRamKeyTyped

    private void jRafagaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRafagaKeyTyped
        char c = evt.getKeyChar();
        try{
            int i = Integer.parseInt(Character.toString(c));            
        }catch(NumberFormatException e){
            evt.consume();
        }
    }//GEN-LAST:event_jRafagaKeyTyped

    private void jQuantumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jQuantumKeyTyped
        char c = evt.getKeyChar();
        try{
            int i = Integer.parseInt(Character.toString(c));            
        }catch(NumberFormatException e){
            evt.consume();
        }
    }//GEN-LAST:event_jQuantumKeyTyped

    private void jTramKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTramKeyTyped
        char c = evt.getKeyChar();
        try{
            int i = Integer.parseInt(Character.toString(c));            
        }catch(NumberFormatException e){
            evt.consume();
        }
    }//GEN-LAST:event_jTramKeyTyped

    private void jDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDetenerActionPerformed
        if(jTipo.getSelectedIndex() != 0){
            proceso[jIngreso.getSelectedRow()].estado = false;
        }
        else{
            rr.estado[jIngreso.getSelectedRow()] = false;
        }
        
        
    }//GEN-LAST:event_jDetenerActionPerformed

    private void jEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEliminarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel)jIngreso.getModel();
        modelo.removeRow(jIngreso.getSelectedRow());
        Nprocesos--;
        if(Nprocesos == 0){
            jEliminar.setEnabled(false);
        }
    }//GEN-LAST:event_jEliminarActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Simulador_rr_fcfs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Simulador_rr_fcfs().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAgregar;
    private javax.swing.JButton jDetener;
    private javax.swing.JButton jEliminar;
    private javax.swing.JTable jIngreso;
    private javax.swing.JButton jIniciar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jNombre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jPcAgregar;
    private javax.swing.JTextField jQuantum;
    private javax.swing.JTextField jRafaga;
    private javax.swing.JTextField jRam;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jTipo;
    private javax.swing.JTextField jTram;
    // End of variables declaration//GEN-END:variables
}
