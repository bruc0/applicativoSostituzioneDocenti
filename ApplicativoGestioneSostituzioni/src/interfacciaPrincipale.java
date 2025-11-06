import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class interfacciaPrincipale {

    private JFrame frame;
    private JComboBox<String> comboBoxDocenti;
    private JComboBox<String> comboBoxClassi;
    private JTable tabellaLezioni;
    private JButton btnRicaricaTabella;
    private JButton btnCambiaFileSorgente;
    private Database database;

    public interfacciaPrincipale() {

        applicaTemaScuro();
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        // Creazione del frame principale con tema scuro
        frame = new JFrame("Gestione Sostituzioni");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(45, 45, 48));

        // Creazione del pannello principale
        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(new Color(45, 45, 48));
        pannelloPrincipale.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Creazione del menu superiore con tema scuro
        JPanel pannelloMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloMenu.setBackground(new Color(55, 55, 58));
        pannelloMenu.setBorder(new EmptyBorder(15, 15, 15, 15));
        pannelloMenu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 82), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Sezione Docente con tema scuro
        JLabel labelDocente = new JLabel("Docente:");
        labelDocente.setPreferredSize(new Dimension(80, 25));
        labelDocente.setForeground(Color.WHITE);
        labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pannelloMenu.add(labelDocente);

        comboBoxDocenti = new JComboBox<>();
        comboBoxDocenti.setPreferredSize(new Dimension(200, 30));
        stileComboBox(comboBoxDocenti);
        pannelloMenu.add(comboBoxDocenti);

        // Spazio tra le sezioni
        pannelloMenu.add(Box.createHorizontalStrut(30));

        // Sezione Classe con tema scuro
        JLabel labelClasse = new JLabel("Classe:");
        labelClasse.setPreferredSize(new Dimension(80, 25));
        labelClasse.setForeground(Color.WHITE);
        labelClasse.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pannelloMenu.add(labelClasse);

        comboBoxClassi = new JComboBox<>();
        comboBoxClassi.setPreferredSize(new Dimension(150, 30));
        stileComboBox(comboBoxClassi);
        pannelloMenu.add(comboBoxClassi);

        // Spazio tra le sezioni
        pannelloMenu.add(Box.createHorizontalStrut(30));

        // Bottone Ricarica Tabella con tema scuro
        btnRicaricaTabella = new JButton("Ricarica Tabella");
        btnRicaricaTabella.setPreferredSize(new Dimension(150, 30));
        stileBottone(btnRicaricaTabella, new Color(70, 130, 180)); // Steel Blue
        pannelloMenu.add(btnRicaricaTabella);

        // Spazio tra i bottoni
        pannelloMenu.add(Box.createHorizontalStrut(10));

        // Bottone Cambia File Sorgente con tema scuro
        btnCambiaFileSorgente = new JButton("Cambia File Sorgente");
        btnCambiaFileSorgente.setPreferredSize(new Dimension(180, 30));
        stileBottone(btnCambiaFileSorgente, new Color(34, 139, 34)); // Forest Green
        pannelloMenu.add(btnCambiaFileSorgente);

        // Aggiunta del menu al frame
        pannelloPrincipale.add(pannelloMenu, BorderLayout.NORTH);

        // Creazione della tabella delle lezioni con tema scuro
        creaTabellaLezioni();
        JScrollPane scrollPane = new JScrollPane(tabellaLezioni);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 82), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(new Color(45, 45, 48));
        pannelloPrincipale.add(scrollPane, BorderLayout.CENTER);

        // Aggiunta del pannello al frame
        frame.add(pannelloPrincipale);
        frame.setVisible(true);
    }

    private void creaTabellaLezioni() {
        // Definizione delle colonne
        String[] colonne = {"Ora", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};

        // Definizione delle ore (dalle 8 alle 16)
        String[] ore = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00",
                       "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00"};

        // Creazione del modello della tabella con tema scuro
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende la tabella non modificabile
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
        };

        // Aggiunta delle righe con le ore
        for (String ora : ore) {
            Object[] riga = {ora, "", "", "", "", ""};
            model.addRow(riga);
        }

        // Creazione della tabella con tema scuro
        tabellaLezioni = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Alternanza colori righe
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(55, 55, 58) : new Color(45, 45, 48));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(70, 130, 180));
                    c.setForeground(Color.WHITE);
                }

                return c;
            }
        };

        stileTabella(tabellaLezioni);
    }

    private void stileTabella(JTable tabella) {
        tabella.setRowHeight(35);
        tabella.getColumnModel().getColumn(0).setPreferredWidth(100); // Colonna Ora
        tabella.getColumnModel().getColumn(1).setPreferredWidth(180); // Lunedì
        tabella.getColumnModel().getColumn(2).setPreferredWidth(180); // Martedì
        tabella.getColumnModel().getColumn(3).setPreferredWidth(180); // Mercoledì
        tabella.getColumnModel().getColumn(4).setPreferredWidth(180); // Giovedì
        tabella.getColumnModel().getColumn(5).setPreferredWidth(180); // Venerdì

        // Font moderni
        tabella.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabella.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabella.getTableHeader().setBackground(new Color(65, 65, 68));
        tabella.getTableHeader().setForeground(Color.WHITE);
        tabella.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(80, 80, 82)));

        // Colori generali
        tabella.setBackground(new Color(45, 45, 48));
        tabella.setForeground(Color.WHITE);
        tabella.setSelectionBackground(new Color(70, 130, 180));
        tabella.setSelectionForeground(Color.WHITE);
        tabella.setGridColor(new Color(80, 80, 82));
        tabella.setIntercellSpacing(new Dimension(1, 1));
    }

    private void stileComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(new Color(65, 65, 68));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 82)));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(new Color(70, 130, 180));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(new Color(65, 65, 68));
                    setForeground(Color.WHITE);
                }
                return this;
            }
        });
    }

    private void stileBottone(JButton bottone, Color colorePrincipale) {
        bottone.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bottone.setBackground(colorePrincipale);
        bottone.setForeground(Color.WHITE);
        bottone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorePrincipale.darker(), 2),
            new EmptyBorder(8, 15, 8, 15)
        ));
        bottone.setFocusPainted(false);
        bottone.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effetti hover
        bottone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bottone.setBackground(colorePrincipale.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bottone.setBackground(colorePrincipale);
            }
        });
    }

    private void applicaTemaScuro() {
        try {
            // Impostazione del look and feel di sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Personalizzazione colori tema scuro
            UIManager.put("Panel.background", new Color(45, 45, 48));
            UIManager.put("Frame.background", new Color(45, 45, 48));
            UIManager.put("OptionPane.background", new Color(45, 45, 48));
            UIManager.put("Button.background", new Color(65, 65, 68));
            UIManager.put("ComboBox.background", new Color(65, 65, 68));
            UIManager.put("List.background", new Color(65, 65, 68));

        } catch (Exception e) {
            System.err.println("Impossibile applicare il tema scuro: " + e.getMessage());
        }
    }

    // Metodi per riempire le combobox (stub - implementazione futura)
    public void popolaComboBoxDocenti(ArrayList<String> docenti) {
        comboBoxDocenti.removeAllItems();
        for (String docente : docenti) {
            comboBoxDocenti.addItem(docente);
        }
    }

    public void popolaComboBoxClassi(ArrayList<String> classi) {
        comboBoxClassi.removeAllItems();
        for (String classe : database.getClassi()) {
            comboBoxClassi.addItem(classe);
        }
    }

    // Metodo per popolare la tabella (stub - implementazione futura)
    public void popolaTabellaLezioni() {

    }

    // Getter per i componenti (utili per будущих implementazioni)
    public JComboBox<String> getComboBoxDocenti() {
        return comboBoxDocenti;
    }

    public JComboBox<String> getComboBoxClassi() {
        return comboBoxClassi;
    }

    public JTable getTabellaLezioni() {
        return tabellaLezioni;
    }

    public JButton getBtnRicaricaTabella() {
        return btnRicaricaTabella;
    }

    public JButton getBtnCambiaFileSorgente() {
        return btnCambiaFileSorgente;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

}
