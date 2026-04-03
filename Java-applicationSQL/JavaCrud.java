package JavaSQL;

import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



public class JavaCrud extends javax.swing.JFrame {

    private DbConnection dbConnection;
    PreparedStatement pst;
    
    /**
     * Creates new form JavaCrud
     */
    public JavaCrud() throws SQLException {
        initComponents();
        Connect();
        TableCandidats();
        TableVillages();
        TableNoVisites();
    }
    

    public void Connect() throws SQLException {
        try {
            //commecer par cr�er une connexion � la BD 
            dbConnection = DbConnection.getInstance();
            System.out.println("Connexion de la BD ouverte avec succes");
        } catch (SQLException e) { //une gestion simple des erreurs de la BD
            System.err.println("Erreur de BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void CloseApplication() {
        try {
            //fermer la connexion � la BD
            dbConnection.closeConnection();
            System.out.println("Connexion de la BD fermee avec succes");
        } catch (SQLException e) { //une gestion simple des erreurs de la BD
            System.err.println("Erreur de BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void TableCandidats() {
        try {
            String sqlQuery = "select * from Candidat";
            pst = dbConnection.connection.prepareStatement(sqlQuery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();

            int c = rsm.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTableCandidate.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < c; i++) {
                    v.add(rs.getInt("CandidatID"));
                    v.add(rs.getString("Prenom"));
                    v.add(rs.getString("Nom"));
                    v.add(rs.getString("Date_naissance"));
                    v.add(rs.getString("Parti_Politique"));
                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void TableVillages() {
        try {
            String sqlQuery = "select * from Village";
            pst = dbConnection.connection.prepareStatement(sqlQuery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();

            int c = rsm.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTableVillage.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < c; i++) {
                    v.add(rs.getString("VillageID"));
                    v.add(rs.getString("Nom"));
                    v.add(rs.getString("Region"));
                }
                dtm.addRow(v);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TableActions() {
        int candidatId = Integer.parseInt(txtCanId2.getText());
        String sqlQuery = "SELECT Description, Date FROM ActionDecision WHERE CandidatID = ? ORDER BY Date DESC";
        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setInt(1, candidatId);
            ResultSet rs = pst.executeQuery();

            ResultSetMetaData rsm = rs.getMetaData();
            int c = rsm.getColumnCount();

            DefaultTableModel dtm = (DefaultTableModel) jTableAfficherActions.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                for (int i = 1; i < c; i++) {
                    v.add(rs.getString("Description"));
                    v.add(rs.getDate("Date"));
                }
                dtm.addRow(v);
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TableNoVisites() {
        String sqlQuery = "SELECT vi.VillageID, v.CandidatID, c.Prenom, c.Nom, COUNT(*) AS Nombre_Visites\n"
                + "FROM Candidat c\n"
                + "INNER JOIN Visites v ON c.CandidatID = v.CandidatID\n"
                + "INNER JOIN Village vi ON v.VillageID = vi.VillageID\n"
                + "GROUP BY v.CandidatID, vi.VillageID, c.Prenom, c.Nom;";

        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();

            int c = rsm.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) jTableNoVisites.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i < c; i++) {
                    v.add(rs.getString("VillageID"));
                    v.add(rs.getInt("CandidatID"));
                    v.add(rs.getString("Prenom"));
                    v.add(rs.getString("Nom"));
                    v.add(rs.getInt("Nombre_Visites"));
                }
                dtm.addRow(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TableVisitsPlan() {
        int candidatId = Integer.parseInt(txtCanId5.getText());
        String sqlQuery = "SELECT v.Date_visite, vi.Nom, vi.Region\n"
                + "FROM Visites v\n"
                + "JOIN Village vi ON v.VillageID = vi.VillageID\n"
                + "WHERE v.CandidatID = ?; ";
        
        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setInt(1, candidatId);
            ResultSet rs = pst.executeQuery();

            ResultSetMetaData rsm = rs.getMetaData();
            int c = rsm.getColumnCount();

            DefaultTableModel dtm = (DefaultTableModel) jTableAfficherVisitPlan.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                for (int i = 1; i < c; i++) {
                    v.add(rs.getDate("Date_visite"));
                    v.add(rs.getString("Nom"));
                    v.add(rs.getString("Region"));
                }
                dtm.addRow(v);
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
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

        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtCanId = new javax.swing.JTextField();
        jButtonDeleteCandidate = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButtonModifyCandidate = new javax.swing.JButton();
        txtDateNaiss = new javax.swing.JTextField();
        jButtonSearchCandidate = new javax.swing.JButton();
        txtNom = new javax.swing.JTextField();
        txtPrenom = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButtonInsertCandidate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCandidate = new javax.swing.JTable();
        txtParti = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButtonClearCandidate = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jButtonCalcImpTotal = new javax.swing.JButton();
        txtCampId = new javax.swing.JTextField();
        txtImpTotal = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButtonClearImpTotal = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableVillage = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtVillaNom = new javax.swing.JTextField();
        jButtonClearVillage = new javax.swing.JButton();
        jButtonModifyVillage = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButtonSearVillage = new javax.swing.JButton();
        txtVillaId = new javax.swing.JTextField();
        txtVillaRegion = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jButtonDeleteVillage = new javax.swing.JButton();
        jButtonInsertVillage = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtCanId2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jAfficherActions = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAfficherActions = new javax.swing.JTable();
        jButtonClearActions = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtCanId3 = new javax.swing.JTextField();
        txtDepsTotal = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButtonCalcDepsTotal = new javax.swing.JButton();
        jButtonClearDepsTotal = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableNoVisites = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtVillaId2 = new javax.swing.JTextField();
        txtCanId4 = new javax.swing.JTextField();
        txtNoVisit = new javax.swing.JTextField();
        jButtonClearNoVisit = new javax.swing.JButton();
        txtCandPrenom = new javax.swing.JTextField();
        txtCandNom = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableAfficherVisitPlan = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtCanId5 = new javax.swing.JTextField();
        jButtonAfficherVisitPlan = new javax.swing.JButton();
        jButtonClearVisitPlan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion d'une campagne d'�lectorale");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setForeground(new java.awt.Color(0, 255, 204));

        jButtonDeleteCandidate.setBackground(new java.awt.Color(0, 204, 153));
        jButtonDeleteCandidate.setForeground(new java.awt.Color(51, 51, 51));
        jButtonDeleteCandidate.setText("Supprimer");
        jButtonDeleteCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteCandidateActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nom");

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Date de naissance");

        jButtonModifyCandidate.setBackground(new java.awt.Color(0, 204, 153));
        jButtonModifyCandidate.setForeground(new java.awt.Color(51, 51, 51));
        jButtonModifyCandidate.setText("Modifier");
        jButtonModifyCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyCandidateActionPerformed(evt);
            }
        });

        jButtonSearchCandidate.setBackground(new java.awt.Color(0, 204, 153));
        jButtonSearchCandidate.setForeground(new java.awt.Color(51, 51, 51));
        jButtonSearchCandidate.setText("Chercher par ID");
        jButtonSearchCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchCandidateActionPerformed(evt);
            }
        });

        txtNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Prenom");

        jButtonInsertCandidate.setBackground(new java.awt.Color(0, 204, 153));
        jButtonInsertCandidate.setForeground(new java.awt.Color(51, 51, 51));
        jButtonInsertCandidate.setText("Ins�rer");
        jButtonInsertCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertCandidateActionPerformed(evt);
            }
        });

        jTableCandidate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Candidat ID", "Prenom", "Nom", "Date Naissance", "Parti politique"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableCandidate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCandidateMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableCandidate);
        if (jTableCandidate.getColumnModel().getColumnCount() > 0) {
            jTableCandidate.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTableCandidate.getColumnModel().getColumn(2).setPreferredWidth(50);
            jTableCandidate.getColumnModel().getColumn(3).setHeaderValue("Date Naissance");
            jTableCandidate.getColumnModel().getColumn(4).setHeaderValue("Parti politique");
        }

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Parti politique");

        jButtonClearCandidate.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearCandidate.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearCandidate.setText("Clear");
        jButtonClearCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearCandidateActionPerformed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Candidat ID");

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Affichage des donn�es des candidats");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButtonInsertCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonModifyCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDeleteCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSearchCandidate))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCanId)
                            .addComponent(txtNom)
                            .addComponent(txtParti)
                            .addComponent(txtDateNaiss)
                            .addComponent(txtPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonClearCandidate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtParti, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDateNaiss, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButtonClearCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSearchCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDeleteCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonModifyCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonInsertCandidate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Campagne ID");

        jButtonCalcImpTotal.setBackground(new java.awt.Color(0, 204, 153));
        jButtonCalcImpTotal.setForeground(new java.awt.Color(51, 51, 51));
        jButtonCalcImpTotal.setText("Calculer");
        jButtonCalcImpTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalcImpTotalActionPerformed(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Impact total");

        jButtonClearImpTotal.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearImpTotal.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearImpTotal.setText("Clear");
        jButtonClearImpTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearImpTotalActionPerformed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("<html>Estimer l'impact financier total<br>des projets de loi d'une campagne</html> ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCampId, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtImpTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jButtonCalcImpTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonClearImpTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCampId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImpTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCalcImpTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonClearImpTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Village ID");

        jTableVillage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Village ID", "Village Nom", "Region"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableVillage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableVillageMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableVillage);
        if (jTableVillage.getColumnModel().getColumnCount() > 0) {
            jTableVillage.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTableVillage.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Region");

        jButtonClearVillage.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearVillage.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearVillage.setText("Clear");
        jButtonClearVillage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearVillageActionPerformed(evt);
            }
        });

        jButtonModifyVillage.setBackground(new java.awt.Color(0, 204, 153));
        jButtonModifyVillage.setForeground(new java.awt.Color(51, 51, 51));
        jButtonModifyVillage.setText("Modifier");
        jButtonModifyVillage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyVillageActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Nom de Village");

        jButtonSearVillage.setBackground(new java.awt.Color(0, 204, 153));
        jButtonSearVillage.setForeground(new java.awt.Color(51, 51, 51));
        jButtonSearVillage.setText("Chercher par ID");
        jButtonSearVillage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchVillageActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Affichage des donn�es des villages");

        jButtonDeleteVillage.setBackground(new java.awt.Color(0, 204, 153));
        jButtonDeleteVillage.setForeground(new java.awt.Color(51, 51, 51));
        jButtonDeleteVillage.setText("Supprimer");
        jButtonDeleteVillage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteVillageActionPerformed(evt);
            }
        });

        jButtonInsertVillage.setBackground(new java.awt.Color(0, 204, 153));
        jButtonInsertVillage.setForeground(new java.awt.Color(51, 51, 51));
        jButtonInsertVillage.setText("Ins�rer");
        jButtonInsertVillage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertVillageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jButtonInsertVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jButtonModifyVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDeleteVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(67, 67, 67)
                                        .addComponent(jLabel9)
                                        .addGap(27, 27, 27))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel10))
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtVillaId)
                                    .addComponent(txtVillaRegion)
                                    .addComponent(txtVillaNom, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jLabel14)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonClearVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearVillage))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVillaId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVillaNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVillaRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonClearVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonSearVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDeleteVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonModifyVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonInsertVillage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("<html>Affichage des actions/<br>d�cisions d'un candidat</html>");

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Candidat ID");

        jAfficherActions.setBackground(new java.awt.Color(0, 204, 153));
        jAfficherActions.setForeground(new java.awt.Color(51, 51, 51));
        jAfficherActions.setText("Afficher");
        jAfficherActions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAfficherActionsActionPerformed(evt);
            }
        });

        jTableAfficherActions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Description", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableAfficherActions);

        jButtonClearActions.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearActions.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearActions.setText("Clear");
        jButtonClearActions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jAfficherActions, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonClearActions, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCanId2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCanId2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jAfficherActions, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonClearActions, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("<html>Calculer le total des d�penses <br>des voyages d'un candidat <html>");

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Candidat ID");

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("D�penses totales");

        jButtonCalcDepsTotal.setBackground(new java.awt.Color(0, 204, 153));
        jButtonCalcDepsTotal.setForeground(new java.awt.Color(51, 51, 51));
        jButtonCalcDepsTotal.setText("Calculer");
        jButtonCalcDepsTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalcDepsTotalActionPerformed(evt);
            }
        });

        jButtonClearDepsTotal.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearDepsTotal.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearDepsTotal.setText("Clear");
        jButtonClearDepsTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearDepsTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jButtonCalcDepsTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonClearDepsTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtCanId3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDepsTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanId3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDepsTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 34, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonClearDepsTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(jButtonCalcDepsTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 204, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableNoVisites.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Village ID", "Candidat ID", "Prenom", "Nom", "No. Visites"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableNoVisites.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableNoVisitesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableNoVisites);

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("<html>Affichages nombre de visites<br> � un village par un candidat<html>");

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Village ID");

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Candidat ID");

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("No. visites");

        jButtonClearNoVisit.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearNoVisit.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearNoVisit.setText("Clear");
        jButtonClearNoVisit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearNoVisitActionPerformed(evt);
            }
        });

        jLabel23.setBackground(new java.awt.Color(0, 0, 0));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Nom");

        jLabel24.setBackground(new java.awt.Color(0, 0, 0));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Prenom");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNoVisit)
                                .addComponent(txtVillaId2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCanId4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCandPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonClearNoVisit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCandNom, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtVillaId2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanId4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCandPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCandNom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoVisit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonClearNoVisit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTableAfficherVisitPlan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date visite", "Village Nom", "Region"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTableAfficherVisitPlan);

        jLabel25.setBackground(new java.awt.Color(0, 0, 0));
        jLabel25.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("<html> Affichage des visites <br> planifi�es par un candidat <html>");

        jLabel26.setBackground(new java.awt.Color(0, 0, 0));
        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("Candidat ID");

        jButtonAfficherVisitPlan.setBackground(new java.awt.Color(0, 204, 153));
        jButtonAfficherVisitPlan.setForeground(new java.awt.Color(51, 51, 51));
        jButtonAfficherVisitPlan.setText("Afficher");
        jButtonAfficherVisitPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAfficherVisitPlanActionPerformed(evt);
            }
        });

        jButtonClearVisitPlan.setBackground(new java.awt.Color(0, 204, 153));
        jButtonClearVisitPlan.setForeground(new java.awt.Color(51, 51, 51));
        jButtonClearVisitPlan.setText("Clear");
        jButtonClearVisitPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearVisitPlanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCanId5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jButtonAfficherVisitPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonClearVisitPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCanId5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAfficherVisitPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonClearVisitPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 590, Short.MAX_VALUE))
                .addContainerGap(1515, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(492, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(178, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1579, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 993, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CloseApplication();
            }
        });
    }//GEN-LAST:event_formWindowClosing

    private void jButtonInsertVillageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertVillageActionPerformed
        // TODO add your handling code here:
        String villageId = txtVillaId.getText();
        String villageNom = txtVillaNom.getText();
        String villageReg = txtVillaRegion.getText();

        try {
            String sqlQuery = "INSERT INTO Village(VillageID, Nom, Region)values(?,?,?)";
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setString(1, villageId);
            pst.setString(2, villageNom);
            pst.setString(3, villageReg);

            int k;
            k = pst.executeUpdate();

            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Insertion r�ussite dans le BD");
                txtVillaId.setText("");
                txtVillaNom.setText("");
                txtVillaRegion.setText("");
                txtVillaId.requestFocus();
                TableVillages();
            } else {
                JOptionPane.showMessageDialog(this, "Echec d'insertion dans le BD");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonInsertVillageActionPerformed

    private void jButtonDeleteVillageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteVillageActionPerformed
        // TODO add your handling code here:
        DefaultTableModel d2 = (DefaultTableModel) jTableVillage.getModel();
        int selectIndex = jTableVillage.getSelectedRow();
        String villageId = d2.getValueAt(selectIndex, 0).toString();

        try {
            String sqlQuery = "delete from Village where VillageID=?";
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setString(1, villageId);

            int k = pst.executeUpdate();

            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Information supprim�e avec succ�s");
                txtVillaId.setText("");
                txtVillaNom.setText("");
                txtVillaRegion.setText("");
                txtVillaId.requestFocus();
                TableVillages();
                jButtonInsertVillage.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Echec de suppression");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDeleteVillageActionPerformed

    private void jButtonSearchVillageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchVillageActionPerformed
        // TODO add your handling code here:
        String villageId = txtVillaId.getText().toUpperCase();

        String sqlQuery = "SELECT VillageID, Nom, Region FROM Village where VillageID=?";

        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setString(1, villageId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtVillaNom.setText(rs.getString("Nom"));
                txtVillaRegion.setText(rs.getString("Region"));
            } else {
                txtVillaNom.setText(rs.getString("null"));
                txtVillaRegion.setText(rs.getString("null"));
            }

            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSearchVillageActionPerformed

    private void jButtonModifyVillageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyVillageActionPerformed
        // TODO add your handling code here:
        DefaultTableModel d2 = (DefaultTableModel) jTableVillage.getModel();
        int selectIndex = jTableVillage.getSelectedRow();
        String villageId = d2.getValueAt(selectIndex, 0).toString();

        String villageNom = txtVillaNom.getText();
        String villageReg = txtVillaRegion.getText();

        try {
            String sqlQuery = "UPDATE Village SET Nom=?, Region=? where VillageID=?";
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setString(1, villageNom);
            pst.setString(2, villageReg);
            pst.setString(3, villageId);
            int k = pst.executeUpdate();

            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Information modifi�e avec succ�s");
                txtVillaNom.setText("");
                txtVillaRegion.setText("");
                txtVillaId.requestFocus();
                TableVillages();
                jButtonInsertVillage.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Echec de modification");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonModifyVillageActionPerformed

    private void jButtonClearVillageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearVillageActionPerformed
        // TODO add your handling code here:
        txtVillaId.setText("");
        txtVillaNom.setText("");
        txtVillaRegion.setText("");
        TableVillages();
        jButtonInsertVillage.setEnabled(true);
    }//GEN-LAST:event_jButtonClearVillageActionPerformed

    private void jTableVillageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVillageMouseClicked
        // TODO add your handling code here:
        DefaultTableModel d2 = (DefaultTableModel) jTableVillage.getModel();
        int selectIndex = jTableVillage.getSelectedRow();
        String villageId = String.valueOf(d2.getValueAt(selectIndex, 0));

        txtVillaId.setText(villageId);
        txtVillaNom.setText(d2.getValueAt(selectIndex, 1).toString());
        txtVillaRegion.setText(d2.getValueAt(selectIndex, 2).toString());
        jButtonInsertVillage.setEnabled(false);
    }//GEN-LAST:event_jTableVillageMouseClicked

    private void jButtonClearCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearCandidateActionPerformed
        // TODO add your handling code here:
        txtCanId.setText("");
        txtNom.setText("");
        txtDateNaiss.setText("");
        txtParti.setText("");
        txtPrenom.setText("");
        txtCanId.requestFocus();
        TableCandidats();
        jButtonInsertCandidate.setEnabled(true);
    }//GEN-LAST:event_jButtonClearCandidateActionPerformed

    private void jTableCandidateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCandidateMouseClicked
        // TODO add your handling code here:
        DefaultTableModel d1 = (DefaultTableModel) jTableCandidate.getModel();
        int selectIndex = jTableCandidate.getSelectedRow();
        int candidatId = (int) d1.getValueAt(selectIndex, 0);

        txtCanId.setText(String.valueOf(candidatId));
        txtPrenom.setText(d1.getValueAt(selectIndex, 1).toString());
        txtNom.setText(d1.getValueAt(selectIndex, 2).toString());
        txtParti.setText(d1.getValueAt(selectIndex, 4).toString());
        txtDateNaiss.setText(d1.getValueAt(selectIndex, 3).toString());
        jButtonInsertCandidate.setEnabled(false);
    }//GEN-LAST:event_jTableCandidateMouseClicked

    private void jButtonInsertCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertCandidateActionPerformed
        // TODO add your handling code here:
        String prenom = txtCanId.getText();
        String nom = txtNom.getText();
        String dateNaiss = txtDateNaiss.getText();
        String parti = txtParti.getText();

        try {
            String sqlQuery = "insert into Candidat(Prenom, Nom, Date_naissance, Parti_politique)values(?,?,?,?)";
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setString(1, prenom);
            pst.setString(2, nom);
            pst.setString(3, dateNaiss);
            pst.setString(4, parti);

            int k;
            k = pst.executeUpdate();

            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Insertion r�ussite dans le BD");
                txtCanId.setText("");
                txtNom.setText("");
                txtDateNaiss.setText("");
                txtParti.setText("");
                txtCanId.requestFocus();
                TableCandidats();
            } else {
                JOptionPane.showMessageDialog(this, "Echec d'insertion dans le BD");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonInsertCandidateActionPerformed

    private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomActionPerformed

    private void jButtonSearchCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchCandidateActionPerformed
        // TODO add your handling code here:
        int candidatId = Integer.parseInt(txtCanId.getText());

        String sqlQuery = "SELECT CandidatID, Prenom, Nom, Date_naissance,Parti_politique FROM Candidat where CandidatID=?";

        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setInt(1, candidatId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtPrenom.setText(rs.getString("Prenom"));
                txtNom.setText(rs.getString("Nom"));
                txtDateNaiss.setText(rs.getString("Date_naissance"));
                txtParti.setText(rs.getString("Parti_politique"));
            } else {
                txtPrenom.setText(rs.getString("null"));
                txtNom.setText(rs.getString("null"));
                txtDateNaiss.setText(rs.getString("null"));
                txtParti.setText(rs.getString("null"));
            }

            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSearchCandidateActionPerformed

    private void jButtonModifyCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyCandidateActionPerformed
        // TODO add your handling code here:
        DefaultTableModel d1 = (DefaultTableModel) jTableCandidate.getModel();
        int selectIndex = jTableCandidate.getSelectedRow();
        int candidatId = (int) d1.getValueAt(selectIndex, 0);

        String prenom = txtPrenom.getText();
        String nom = txtNom.getText();
        String dateNaiss = txtDateNaiss.getText();
        String parti = txtParti.getText();

        try {
            String sqlQuery = "update Candidat set Prenom=?, Nom=?, Date_naissance=?, Parti_politique=? where CandidatID=?";
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setString(1, prenom);
            pst.setString(2, nom);
            pst.setString(3, dateNaiss);
            pst.setString(4, parti);
            pst.setInt(5, candidatId);
            int k = pst.executeUpdate();

            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Information modifi�e avec succ�s");
                txtCanId.setText("");
                txtPrenom.setText("");
                txtNom.setText("");
                txtDateNaiss.setText("");
                txtParti.setText("");
                txtCanId.requestFocus();
                TableCandidats();
                jButtonInsertCandidate.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Echec de modification");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonModifyCandidateActionPerformed

    private void jButtonDeleteCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteCandidateActionPerformed
        // TODO add your handling code here:
        DefaultTableModel d1 = (DefaultTableModel) jTableCandidate.getModel();
        int selectIndex = jTableCandidate.getSelectedRow();
        int candidatId = (int) d1.getValueAt(selectIndex, 0);

        try {
            String sqlQuery = "delete from Candidat where CandidatID=?";
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setInt(1, candidatId);

            int k = pst.executeUpdate();

            if (k == 1) {
                JOptionPane.showMessageDialog(this, "Information supprim�e avec succ�s");
                txtCanId.setText("");
                txtPrenom.setText("");
                txtNom.setText("");
                txtDateNaiss.setText("");
                txtParti.setText("");
                txtCanId.requestFocus();
                TableCandidats();
                jButtonInsertCandidate.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Echec de suppression");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonDeleteCandidateActionPerformed

    private void jButtonCalcImpTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCalcImpTotalActionPerformed
        // TODO add your handling code here:
        int campagneId = Integer.parseInt(txtCampId.getText());
        double impactTotal = 0;
        String sqlQuery = "SELECT SUM(ImpactFinancier) AS ImpactTotal FROM ProjetDeLoi WHERE CampagneID = ?";

        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setInt(1, campagneId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                impactTotal = rs.getDouble("ImpactTotal");
                txtImpTotal.setText(String.valueOf(impactTotal));
                txtCampId.requestFocus();
            } else {
                txtImpTotal.setText("null");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCalcImpTotalActionPerformed

    private void jButtonClearImpTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearImpTotalActionPerformed
        // TODO add your handling code here:
        txtCampId.setText("");
        txtImpTotal.setText("");
        txtCampId.requestFocus();
    }//GEN-LAST:event_jButtonClearImpTotalActionPerformed

    private void jAfficherActionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAfficherActionsActionPerformed
        // TODO add your handling code here:
        TableActions();
        txtCanId2.requestFocus();
    }//GEN-LAST:event_jAfficherActionsActionPerformed

    private void jButtonClearActionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionsActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) jTableAfficherActions.getModel();
        dtm.setRowCount(0);
        txtCanId2.setText("");
        txtCanId2.requestFocus();
    }//GEN-LAST:event_jButtonClearActionsActionPerformed

    private void jButtonCalcDepsTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCalcDepsTotalActionPerformed
        // TODO add your handling code here:
        int candidatId = Integer.parseInt(txtCanId3.getText());
        double depenseTotal = 0;
        String sqlQuery = "SELECT SUM(f.Montant) AS TotalDepenses FROM Frais f JOIN VoyageParCandidat vc ON f.VoyageID = vc.VoyageID WHERE vc.CandidatID = ?";

        try {
            pst = dbConnection.connection.prepareStatement(sqlQuery);
            pst.setInt(1, candidatId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                depenseTotal = rs.getDouble("TotalDepenses");
                txtDepsTotal.setText(String.valueOf(depenseTotal));
                txtCanId3.requestFocus();
            } else {
                txtDepsTotal.setText("null");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCalcDepsTotalActionPerformed

    private void jButtonClearDepsTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearDepsTotalActionPerformed
        // TODO add your handling code here:
        txtCanId3.setText("");
        txtDepsTotal.setText("");
        txtCanId3.requestFocus();
    }//GEN-LAST:event_jButtonClearDepsTotalActionPerformed

    private void jButtonClearNoVisitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearNoVisitActionPerformed
        txtCanId4.setText("");
        txtVillaId2.setText("");
        txtCandNom.setText("");
        txtCandPrenom.setText("");
        txtNoVisit.setText("");
    }//GEN-LAST:event_jButtonClearNoVisitActionPerformed

    private void jTableNoVisitesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNoVisitesMouseClicked
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) jTableNoVisites.getModel();
        int selectIndex = jTableNoVisites.getSelectedRow();
        int candidatId = (int) dtm.getValueAt(selectIndex, 1);
        int numVisites = (int) dtm.getValueAt(selectIndex, 4);

        txtVillaId2.setText(dtm.getValueAt(selectIndex, 0).toString());
        txtCanId4.setText(String.valueOf(candidatId));
        txtCandPrenom.setText(dtm.getValueAt(selectIndex, 2).toString());
        txtCandNom.setText(dtm.getValueAt(selectIndex, 3).toString());
        txtNoVisit.setText(String.valueOf(numVisites));
    }//GEN-LAST:event_jTableNoVisitesMouseClicked

    private void jButtonAfficherVisitPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAfficherVisitPlanActionPerformed
        // TODO add your handling code here:
        TableVisitsPlan();
        txtCanId2.requestFocus();
    }//GEN-LAST:event_jButtonAfficherVisitPlanActionPerformed

    private void jButtonClearVisitPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearVisitPlanActionPerformed
        // TODO add your handling code here:
        DefaultTableModel dtm = (DefaultTableModel) jTableAfficherVisitPlan.getModel();
        dtm.setRowCount(0);
        txtCanId5.setText("");
        txtCanId5.requestFocus();
    }//GEN-LAST:event_jButtonClearVisitPlanActionPerformed

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
            java.util.logging.Logger.getLogger(JavaCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JavaCrud().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(JavaCrud.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAfficherActions;
    private javax.swing.JButton jButtonAfficherVisitPlan;
    private javax.swing.JButton jButtonCalcDepsTotal;
    private javax.swing.JButton jButtonCalcImpTotal;
    private javax.swing.JButton jButtonClearActions;
    private javax.swing.JButton jButtonClearCandidate;
    private javax.swing.JButton jButtonClearDepsTotal;
    private javax.swing.JButton jButtonClearImpTotal;
    private javax.swing.JButton jButtonClearNoVisit;
    private javax.swing.JButton jButtonClearVillage;
    private javax.swing.JButton jButtonClearVisitPlan;
    private javax.swing.JButton jButtonDeleteCandidate;
    private javax.swing.JButton jButtonDeleteVillage;
    private javax.swing.JButton jButtonInsertCandidate;
    private javax.swing.JButton jButtonInsertVillage;
    private javax.swing.JButton jButtonModifyCandidate;
    private javax.swing.JButton jButtonModifyVillage;
    private javax.swing.JButton jButtonSearVillage;
    private javax.swing.JButton jButtonSearchCandidate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableAfficherActions;
    private javax.swing.JTable jTableAfficherVisitPlan;
    private javax.swing.JTable jTableCandidate;
    private javax.swing.JTable jTableNoVisites;
    private javax.swing.JTable jTableVillage;
    private javax.swing.JTextField txtCampId;
    private javax.swing.JTextField txtCanId;
    private javax.swing.JTextField txtCanId2;
    private javax.swing.JTextField txtCanId3;
    private javax.swing.JTextField txtCanId4;
    private javax.swing.JTextField txtCanId5;
    private javax.swing.JTextField txtCandNom;
    private javax.swing.JTextField txtCandPrenom;
    private javax.swing.JTextField txtDateNaiss;
    private javax.swing.JTextField txtDepsTotal;
    private javax.swing.JTextField txtImpTotal;
    private javax.swing.JTextField txtNoVisit;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtParti;
    private javax.swing.JTextField txtPrenom;
    private javax.swing.JTextField txtVillaId;
    private javax.swing.JTextField txtVillaId2;
    private javax.swing.JTextField txtVillaNom;
    private javax.swing.JTextField txtVillaRegion;
    // End of variables declaration//GEN-END:variables
}
