/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Inicio;

import dao.ContatoDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import modelo.Contato;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme;
import javax.swing.UIManager;

/**
 *
 * @author leandro
 */
public class Inicio extends javax.swing.JFrame {
    private ContatoDAO dao;
    private DefaultTableModel modeloTabela;
    private int paginaAtual = 1;
    private int registrosPorPagina = 10;
    private int totalRegistros = 0;
    private int totalPaginas = 0;
    private String termoPesquisa = "";
    /**
     * Creates new form Inicio
     */
    public Inicio() {
        dao = new ContatoDAO();
        initComponents();
        configurarTabela();
        configurarPaginacao();
        carregarDados();
        setLocationRelativeTo(null);  // ✅ null para centralizar na tela

    }
    private void configurarTabela() {
        // Configurar modelo da tabela
        modeloTabela = (DefaultTableModel) tabela.getModel();
        
        // Configurar header
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(63, 81, 181));
        tabela.getTableHeader().setForeground(Color.WHITE);
        
        // ✅ ADICIONADO: altura das linhas para comportar os botões e o círculo
        tabela.setRowHeight(30);
        
        // Renderer personalizado para a coluna Status (círculos coloridos)
        tabela.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        
        // Renderer e Editor para a coluna Ações (botões)
        tabela.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tabela.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(this));
        
        // Larguras das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(200);
    }
    
    private void configurarPaginacao() {
        // Configurar combo de registros por página
        comboRegistrosPorPagina.removeAllItems();
        comboRegistrosPorPagina.addItem("10");
        comboRegistrosPorPagina.addItem("20");
        comboRegistrosPorPagina.addItem("50");
        comboRegistrosPorPagina.addItem("100");
        comboRegistrosPorPagina.setSelectedItem("10");
    }
    
    public void carregarDados() {
        modeloTabela.setRowCount(0);        
        // Calcular offset
        int offset = (paginaAtual - 1) * registrosPorPagina;
        
        // Buscar contatos com paginação
        List<Contato> contatos;
        if (termoPesquisa.isEmpty()) {
            contatos = dao.listarComPaginacao(registrosPorPagina, offset);
            totalRegistros = dao.contarTotal();
        } else {
            contatos = dao.pesquisarComPaginacao(termoPesquisa, registrosPorPagina, offset);
            totalRegistros = dao.contarPesquisa(termoPesquisa);
        }        
        // Calcular total de páginas
        totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        if (totalPaginas == 0) totalPaginas = 1;        
        // Preencher tabela
        for (Contato contato : contatos) {
            Object[] linha = {
                contato.getId(),
                contato.getNome(),
                contato.getTelefone(),
                contato.getEmail(),
                contato.getStatus(),
                "botoes"
            };
            modeloTabela.addRow(linha);
        }        
        // Atualizar controles de paginação
        atualizarPaginacao();
    }
    
    private void atualizarPaginacao() {
        // Atualizar label de informações
        int inicio = totalRegistros == 0 ? 0 : (paginaAtual - 1) * registrosPorPagina + 1;
        int fim = Math.min(paginaAtual * registrosPorPagina, totalRegistros);
        
        lblInfoPaginacao.setText(String.format(
            "Mostrando %d a %d de %d registros | Página %d de %d",
            inicio, fim, totalRegistros, paginaAtual, totalPaginas
        ));
        
        // Atualizar estado dos botões
        btnPrimeira.setEnabled(paginaAtual > 1);
        btnAnterior.setEnabled(paginaAtual > 1);
        btnProxima.setEnabled(paginaAtual < totalPaginas);
        btnUltima.setEnabled(paginaAtual < totalPaginas);
        
        // Atualizar campo de página atual
        txtPaginaAtual.setText(String.valueOf(paginaAtual));
        lblTotalPaginas.setText("de " + totalPaginas);
    }  
    
    private void pesquisar() {
        termoPesquisa = txtPesquisa.getText().trim();
        paginaAtual = 1;
        carregarDados();
        
        if (!termoPesquisa.isEmpty() && totalRegistros == 0) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum contato encontrado com o termo: " + termoPesquisa, 
                "Pesquisa", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void irParaPrimeiraPagina() {
        paginaAtual = 1;
        carregarDados();
    }
    
    private void irParaPaginaAnterior() {
        if (paginaAtual > 1) {
            paginaAtual--;
            carregarDados();
        }
    }
    
    private void irParaProximaPagina() {
        if (paginaAtual < totalPaginas) {
            paginaAtual++;
            carregarDados();
        }
    }
    
    private void irParaUltimaPagina() {
        paginaAtual = totalPaginas;
        carregarDados();
    }
    
    private void irParaPagina() {
        try {
            int pagina = Integer.parseInt(txtPaginaAtual.getText());
            if (pagina >= 1 && pagina <= totalPaginas) {
                paginaAtual = pagina;
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Página deve estar entre 1 e " + totalPaginas, 
                    "Atenção", 
                    JOptionPane.WARNING_MESSAGE);
                txtPaginaAtual.setText(String.valueOf(paginaAtual));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Digite um número válido!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            txtPaginaAtual.setText(String.valueOf(paginaAtual));
        }
    }
    
    private void alterarRegistrosPorPagina() {
        Object selectedItem = comboRegistrosPorPagina.getSelectedItem();
        if (selectedItem != null) {
            registrosPorPagina = Integer.parseInt(comboRegistrosPorPagina.getSelectedItem().toString());
            paginaAtual = 1;
        } else {         
            System.out.println("No item selected in the JComboBox.");
        }        
        carregarDados();
    }
    
    public void abrirTelaCadastro(Contato contato) {
        TelaCadastro tela = new TelaCadastro(this, contato);
        tela.setVisible(true);
    }
    
    public void abrirTelaVisualizacao(int id) {
        Contato contato = dao.buscarPorId(id);
        if (contato != null) {
            TelaVisualizacao tela = new TelaVisualizacao(contato);
            tela.setVisible(true);
        }
    }
    
    public void excluirContato(int id) {
        int resposta = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir este contato?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (resposta == JOptionPane.YES_OPTION) {
            if (dao.excluir(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Contato excluído com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Ajustar página se necessário
                if (modeloTabela.getRowCount() == 1 && paginaAtual > 1) {
                    paginaAtual--;
                }
                
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir contato!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Renderer para círculos de status
    class StatusCellRenderer extends JPanel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setOpaque(true);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            
            removeAll();
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            String status = value.toString();
            JLabel circulo = new JLabel("●");
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); ///Tamanho do circuito
            circulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));///Tamanho do circuito
            
            if ("Ativo".equals(status)) {
                circulo.setForeground(new Color(76, 175, 80));
            } else {
                circulo.setForeground(new Color(244, 67, 54));
            }
            
            add(circulo);
            return this;
        }
    }
    
    // Renderer para botões de ação
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            removeAll();
            
            JButton btnVisualizar = new JButton("👁");
            JButton btnEditar = new JButton("✏");
            JButton btnExcluir = new JButton("🗑");
            
            btnVisualizar.setToolTipText("Visualizar");
            btnEditar.setToolTipText("Editar");
            btnExcluir.setToolTipText("Excluir");
            
            add(btnVisualizar);
            add(btnEditar);
            add(btnExcluir);
            
            return this;
        }
    }
    
    // Editor para botões de ação
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnVisualizar, btnEditar, btnExcluir;
        private Inicio telaListagem;
        private int linhaAtual;
        
        public ButtonEditor(Inicio tela) {
            super(new JCheckBox());
            this.telaListagem = tela;
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnVisualizar = new JButton("👁");
            btnEditar = new JButton("✏");
            btnExcluir = new JButton("🗑");
            
            btnVisualizar.addActionListener(e -> {
                int id = (int) tabela.getValueAt(linhaAtual, 0);
                telaListagem.abrirTelaVisualizacao(id);
                fireEditingStopped();
            });
            
            btnEditar.addActionListener(e -> {
                int id = (int) tabela.getValueAt(linhaAtual, 0);
                Contato contato = dao.buscarPorId(id);
                telaListagem.abrirTelaCadastro(contato);
                fireEditingStopped();
            });
            
            btnExcluir.addActionListener(e -> {
                int id = (int) tabela.getValueAt(linhaAtual, 0);
                telaListagem.excluirContato(id);
                fireEditingStopped();
            });
            
            panel.add(btnVisualizar);
            panel.add(btnEditar);
            panel.add(btnExcluir);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            linhaAtual = row;
            return panel;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        comboRegistrosPorPagina = new javax.swing.JComboBox<>();
        btnPrimeira = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        txtPaginaAtual = new javax.swing.JTextField();
        btnProxima = new javax.swing.JButton();
        btnUltima = new javax.swing.JButton();
        lblTotalPaginas = new javax.swing.JLabel();
        lblInfoPaginacao = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone", "E-mail", "Status", "Ações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabela);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Registros por Pagina:");

        btnPrimeira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_first_16px.png"))); // NOI18N
        btnPrimeira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrimeiraActionPerformed(evt);
            }
        });

        btnAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_previous_16px.png"))); // NOI18N
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });

        txtPaginaAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnProxima.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_next_16px.png"))); // NOI18N
        btnProxima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximaActionPerformed(evt);
            }
        });

        btnUltima.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_last_16px.png"))); // NOI18N
        btnUltima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUltimaActionPerformed(evt);
            }
        });

        lblTotalPaginas.setText("de");

        lblInfoPaginacao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoPaginacao.setText("Info");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboRegistrosPorPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrimeira)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAnterior)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaginaAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProxima)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUltima)
                .addGap(18, 18, 18)
                .addComponent(lblInfoPaginacao, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProxima)
                    .addComponent(btnUltima)
                    .addComponent(lblInfoPaginacao, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(comboRegistrosPorPagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPrimeira)
                        .addComponent(btnAnterior)
                        .addComponent(txtPaginaAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTotalPaginas)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Pesquisa:");

        btnPesquisar.setBackground(new java.awt.Color(0, 102, 204));
        btnPesquisar.setForeground(new java.awt.Color(255, 255, 255));
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_search_client_16px.png"))); // NOI18N
        btnPesquisar.setText("Buscar");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        btnLimpar.setBackground(new java.awt.Color(255, 204, 0));
        btnLimpar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_broom_16px.png"))); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnNovo.setBackground(new java.awt.Color(0, 204, 102));
        btnNovo.setForeground(new java.awt.Color(255, 255, 255));
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_new_copy_16px.png"))); // NOI18N
        btnNovo.setText("Novo Contato");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Gerenciamento de Contatos");
        lblTitulo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 3, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNovo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNovo)
                    .addComponent(btnLimpar)
                    .addComponent(btnPesquisar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        // TODO add your handling code here:
        irParaPaginaAnterior();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:
        txtPesquisa.setText("");
        termoPesquisa = "";
        paginaAtual = 1;
        carregarDados();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // TODO add your handling code here:
        pesquisar();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        // TODO add your handling code here:
        abrirTelaCadastro(null);
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnPrimeiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrimeiraActionPerformed
        // TODO add your handling code here:
        irParaPrimeiraPagina();
    }//GEN-LAST:event_btnPrimeiraActionPerformed

    private void btnUltimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUltimaActionPerformed
        // TODO add your handling code here:
        irParaUltimaPagina();
    }//GEN-LAST:event_btnUltimaActionPerformed

    private void btnProximaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximaActionPerformed
        // TODO add your handling code here:
        irParaPagina();
    }//GEN-LAST:event_btnProximaActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    // ✅ FlatLaf - substitui o bloco Nimbus inteiro
    try {
        //com.formdev.flatlaf.FlatDarkLaf.setup();   // Tema escuro
        // ou use:
        // com.formdev.flatlaf.FlatLightLaf.setup();  // Tema claro
        // com.formdev.flatlaf.FlatIntelliJLaf.setup(); // Estilo IntelliJ
        com.formdev.flatlaf.FlatDarculaLaf.setup();  // Estilo Darcula
    } catch (Exception ex) {
        System.err.println("Falha ao aplicar FlatLaf: " + ex.getMessage());
    }

    java.awt.EventQueue.invokeLater(() -> new Inicio().setVisible(true));
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnPrimeira;
    private javax.swing.JButton btnProxima;
    private javax.swing.JButton btnUltima;
    private javax.swing.JComboBox<String> comboRegistrosPorPagina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInfoPaginacao;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalPaginas;
    private javax.swing.JTable tabela;
    private javax.swing.JTextField txtPaginaAtual;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
