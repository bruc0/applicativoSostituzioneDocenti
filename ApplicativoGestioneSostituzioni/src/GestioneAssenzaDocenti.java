import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GestioneAssenzaDocenti extends JFrame {

    private Database database;
    private JTable tabellaDocenti;
    private DefaultTableModel modelTabella;
    private JButton btnAggiungiAssenza;
    private JButton btnRimuoviAssenza;
    private JButton btnConferma;

    // Mappa per tenere traccia delle assenze: Docente -> Lista di OraScolastica
    private HashMap<String, ArrayList<OraScolastica>> assenzeDocenti;

    public GestioneAssenzaDocenti(Database database) {
        this.database = database;
        this.assenzeDocenti = new HashMap<>();

        // Configurazione della finestra
        setTitle("Gestione Assenze Docenti");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Pannello superiore con titolo
        JPanel panelTitolo = new JPanel();
        panelTitolo.setBackground(new Color(70, 130, 180));
        JLabel labelTitolo = new JLabel("Gestione Assenze Docenti");
        labelTitolo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitolo.setForeground(Color.WHITE);
        panelTitolo.add(labelTitolo);
        add(panelTitolo, BorderLayout.NORTH);

        // Pannello centrale con tabella
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crea la tabella dei docenti
        String[] colonne = {"Docente", "Assenze Programmate"};
        modelTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendi la tabella non editabile direttamente
            }
        };

        tabellaDocenti = new JTable(modelTabella);
        tabellaDocenti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaDocenti.setRowHeight(25);
        tabellaDocenti.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabellaDocenti.setFont(new Font("Arial", Font.PLAIN, 12));

        // Popola la tabella con i docenti
        popolaTabella();

        JScrollPane scrollPane = new JScrollPane(tabellaDocenti);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Pannello inferiore con pulsanti
        JPanel panelPulsanti = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelPulsanti.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        btnAggiungiAssenza = new JButton("Aggiungi Assenza");
        btnAggiungiAssenza.setPreferredSize(new Dimension(150, 40));
        btnAggiungiAssenza.setBackground(new Color(34, 139, 34));
        btnAggiungiAssenza.setForeground(Color.WHITE);
        btnAggiungiAssenza.setFocusPainted(false);
        btnAggiungiAssenza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aggiungiAssenza();
            }
        });

        btnRimuoviAssenza = new JButton("Rimuovi Assenza");
        btnRimuoviAssenza.setPreferredSize(new Dimension(150, 40));
        btnRimuoviAssenza.setBackground(new Color(220, 20, 60));
        btnRimuoviAssenza.setForeground(Color.WHITE);
        btnRimuoviAssenza.setFocusPainted(false);
        btnRimuoviAssenza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rimuoviAssenza();
            }
        });

        btnConferma = new JButton("Conferma");
        btnConferma.setPreferredSize(new Dimension(150, 40));
        btnConferma.setBackground(new Color(70, 130, 180));
        btnConferma.setForeground(Color.WHITE);
        btnConferma.setFocusPainted(false);
        btnConferma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confermaAssenze();
                database.setAssenzeDocenti(assenzeDocenti);
                database.Sostituzione();
            }
        });

        panelPulsanti.add(btnAggiungiAssenza);
        panelPulsanti.add(btnRimuoviAssenza);
        panelPulsanti.add(btnConferma);

        add(panelPulsanti, BorderLayout.SOUTH);
    }

    private void popolaTabella() {
        modelTabella.setRowCount(0);

        ArrayList<Docente> docenti = database.getDocenti();
        docenti.sort((d1, d2) -> d1.getNome().compareToIgnoreCase(d2.getNome()));
        for (Docente docente : docenti) {
            String nomeDocente = docente.getNome();
            String assenze = getAssenzeString(nomeDocente);
            modelTabella.addRow(new Object[]{nomeDocente, assenze});
        }
    }

    private String getAssenzeString(String nomeDocente) {
        ArrayList<OraScolastica> assenze = assenzeDocenti.get(nomeDocente);
        if (assenze == null || assenze.isEmpty()) {
            return "Nessuna assenza";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < assenze.size(); i++) {
            OraScolastica ora = assenze.get(i);
            sb.append(ora.giorno).append(" ").append(ora.getOraInizio()[0]).append(":").append(String.format("%02d", ora.getOraInizio()[1]));
            if (i < assenze.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private void aggiungiAssenza() {
        int rigaSelezionata = tabellaDocenti.getSelectedRow();

        if (rigaSelezionata == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona un docente dalla tabella!",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeDocente = (String) modelTabella.getValueAt(rigaSelezionata, 0);

        // Dialog per selezionare giorno e ora
        JDialog dialog = new JDialog(this, "Aggiungi Assenza - " + nomeDocente, true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Selezione giorno
        JLabel labelGiorno = new JLabel("Giorno:");
        String[] giorni = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        JComboBox<String> comboGiorno = new JComboBox<>(giorni);

        // Tipo di assenza
        JLabel labelTipo = new JLabel("Tipo assenza:");
        String[] tipiAssenza = {"Ora singola", "Range di ore", "Tutto il giorno"};
        JComboBox<String> comboTipo = new JComboBox<>(tipiAssenza);

        // Selezione ora inizio
        JLabel labelOraInizio = new JLabel("Ora inizio:");
        String[] ore = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"};
        JComboBox<String> comboOraInizio = new JComboBox<>(ore);

        // Selezione ora fine (per range)
        JLabel labelOraFine = new JLabel("Ora fine:");
        JComboBox<String> comboOraFine = new JComboBox<>(ore);
        comboOraFine.setSelectedIndex(1); // Default: 9:00
        comboOraFine.setEnabled(false);

        // Selezione durata (per ora singola)
        JLabel labelDurata = new JLabel("Durata (ore):");
        String[] durate = {"1", "2"};
        JComboBox<String> comboDurata = new JComboBox<>(durate);

        panelForm.add(labelGiorno);
        panelForm.add(comboGiorno);
        panelForm.add(labelTipo);
        panelForm.add(comboTipo);
        panelForm.add(labelOraInizio);
        panelForm.add(comboOraInizio);
        panelForm.add(labelOraFine);
        panelForm.add(comboOraFine);
        panelForm.add(labelDurata);
        panelForm.add(comboDurata);

        dialog.add(panelForm, BorderLayout.CENTER);

        // Listener per cambiare i campi abilitati in base al tipo
        comboTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) comboTipo.getSelectedItem();
                if (tipo.equals("Ora singola")) {
                    comboOraInizio.setEnabled(true);
                    comboOraFine.setEnabled(false);
                    comboDurata.setEnabled(true);
                    labelOraInizio.setText("Ora inizio:");
                } else if (tipo.equals("Range di ore")) {
                    comboOraInizio.setEnabled(true);
                    comboOraFine.setEnabled(true);
                    comboDurata.setEnabled(false);
                    labelOraInizio.setText("Ora inizio:");
                } else { // Tutto il giorno
                    comboOraInizio.setEnabled(false);
                    comboOraFine.setEnabled(false);
                    comboDurata.setEnabled(false);
                    labelOraInizio.setText("Ora inizio:");
                }
            }
        });

        // Pulsanti
        JPanel panelPulsantiDialog = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnOk = new JButton("OK");
        JButton btnAnnulla = new JButton("Annulla");

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String giorno = (String) comboGiorno.getSelectedItem();
                String tipo = (String) comboTipo.getSelectedItem();

                // Inizializza la lista se non esiste
                if (!assenzeDocenti.containsKey(nomeDocente)) {
                    assenzeDocenti.put(nomeDocente, new ArrayList<>());
                }

                if (tipo.equals("Tutto il giorno")) {
                    // Aggiungi tutte le ore della giornata (8:00 - 15:00)
                    String[] oreTutteGiorno = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"};
                    for (String oraStr : oreTutteGiorno) {
                        String[] oraParts = oraStr.split(":");
                        int ora = Integer.parseInt(oraParts[0]);
                        int minuti = Integer.parseInt(oraParts[1]);
                        OraScolastica oraScolastica = new OraScolastica(ora, minuti, 1, 0, giorno);
                        assenzeDocenti.get(nomeDocente).add(oraScolastica);
                    }
                } else if (tipo.equals("Range di ore")) {
                    // Aggiungi range di ore
                    String oraInizioStr = (String) comboOraInizio.getSelectedItem();
                    String oraFineStr = (String) comboOraFine.getSelectedItem();

                    int indiceInizio = comboOraInizio.getSelectedIndex();
                    int indiceFine = comboOraFine.getSelectedIndex();

                    if (indiceFine <= indiceInizio) {
                        JOptionPane.showMessageDialog(dialog,
                            "L'ora di fine deve essere successiva all'ora di inizio!",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Aggiungi tutte le ore nel range
                    for (int i = indiceInizio; i <= indiceFine; i++) {
                        String oraStr = ore[i];
                        String[] oraParts = oraStr.split(":");
                        int ora = Integer.parseInt(oraParts[0]);
                        int minuti = Integer.parseInt(oraParts[1]);
                        OraScolastica oraScolastica = new OraScolastica(ora, minuti, 1, 0, giorno);
                        assenzeDocenti.get(nomeDocente).add(oraScolastica);
                    }
                } else { // Ora singola
                    String oraStr = (String) comboOraInizio.getSelectedItem();
                    int durata = Integer.parseInt((String) comboDurata.getSelectedItem());

                    String[] oraParts = oraStr.split(":");
                    int ora = Integer.parseInt(oraParts[0]);
                    int minuti = Integer.parseInt(oraParts[1]);

                    OraScolastica oraScolastica = new OraScolastica(ora, minuti, durata, 0, giorno);
                    assenzeDocenti.get(nomeDocente).add(oraScolastica);
                }

                // Aggiorna la tabella
                popolaTabella();
                dialog.dispose();
            }
        });

        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        panelPulsantiDialog.add(btnOk);
        panelPulsantiDialog.add(btnAnnulla);
        dialog.add(panelPulsantiDialog, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void rimuoviAssenza() {
        int rigaSelezionata = tabellaDocenti.getSelectedRow();

        if (rigaSelezionata == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleziona un docente dalla tabella!",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeDocente = (String) modelTabella.getValueAt(rigaSelezionata, 0);
        ArrayList<OraScolastica> assenze = assenzeDocenti.get(nomeDocente);

        if (assenze == null || assenze.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Questo docente non ha assenze programmate!",
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crea un dialog per selezionare quale assenza rimuovere
        String[] opzioni = new String[assenze.size()];
        for (int i = 0; i < assenze.size(); i++) {
            OraScolastica ora = assenze.get(i);
            opzioni[i] = ora.giorno + " " + ora.getOraInizio()[0] + ":" + String.format("%02d", ora.getOraInizio()[1]);
        }

        String scelta = (String) JOptionPane.showInputDialog(
            this,
            "Seleziona l'assenza da rimuovere:",
            "Rimuovi Assenza - " + nomeDocente,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opzioni,
            opzioni[0]
        );

        if (scelta != null) {
            for (int i = 0; i < opzioni.length; i++) {
                if (opzioni[i].equals(scelta)) {
                    assenze.remove(i);
                    break;
                }
            }
            popolaTabella();
        }
    }

    private void confermaAssenze() {
        StringBuilder riepilogo = new StringBuilder("Riepilogo Assenze:\n\n");

        if (assenzeDocenti.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nessuna assenza programmata!",
                "Informazione",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (String docente : assenzeDocenti.keySet()) {
            ArrayList<OraScolastica> assenze = assenzeDocenti.get(docente);
            if (!assenze.isEmpty()) {
                riepilogo.append(docente).append(":\n");
                for (OraScolastica ora : assenze) {
                    riepilogo.append("  - ").append(ora.giorno).append(" ")
                            .append(ora.getOraInizio()[0]).append(":")
                            .append(String.format("%02d", ora.getOraInizio()[1]))
                            .append(" (").append(ora.getDurata()[0]).append("h)\n");
                }
                riepilogo.append("\n");
            }


        }

        int risposta = JOptionPane.showConfirmDialog(this,
            riepilogo.toString(),
            "Conferma Assenze",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE);

        if (risposta == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Assenze confermate con successo!",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE);
            // Qui puoi aggiungere la logica per processare le assenze
            // Ad esempio: database.processsaAssenze(assenzeDocenti);
        }
    }

    public HashMap<String, ArrayList<OraScolastica>> getAssenzeDocenti() {
        return assenzeDocenti;
    }

}
