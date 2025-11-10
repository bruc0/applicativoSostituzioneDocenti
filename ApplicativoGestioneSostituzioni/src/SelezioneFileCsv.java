import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SelezioneFileCsv extends JFrame {

    private JLabel labelPercorso;
    private JButton btnSeleziona;
    private JButton btnConferma;
    private File fileSelezionato;

    public SelezioneFileCsv() {
        // Configurazione della finestra
        setTitle("Selezione File CSV");
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Pannello superiore con titolo
        JPanel panelTitolo = new JPanel();
        JLabel labelTitolo = new JLabel("Seleziona il file CSV da importare");
        labelTitolo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitolo.add(labelTitolo);
        add(panelTitolo, BorderLayout.NORTH);

        // Pannello centrale con percorso file
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel labelInfo = new JLabel("File selezionato:");
        panelCentro.add(labelInfo, BorderLayout.NORTH);

        labelPercorso = new JLabel("Nessun file selezionato");
        labelPercorso.setForeground(Color.GRAY);
        labelPercorso.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        labelPercorso.setPreferredSize(new Dimension(400, 30));
        labelPercorso.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentro.add(labelPercorso, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Pannello inferiore con pulsanti
        JPanel panelPulsanti = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnSeleziona = new JButton("Sfoglia...");
        btnSeleziona.setPreferredSize(new Dimension(120, 35));
        btnSeleziona.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selezionaFile();
            }
        });

        btnConferma = new JButton("Conferma");
        btnConferma.setPreferredSize(new Dimension(120, 35));
        btnConferma.setEnabled(false);
        btnConferma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confermaFile();
            }
        });

        panelPulsanti.add(btnSeleziona);
        panelPulsanti.add(btnConferma);

        add(panelPulsanti, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void selezionaFile() {
        JFileChooser fileChooser = new JFileChooser();

        // Imposta il filtro per i file CSV
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "File CSV (*.csv)", "csv");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Imposta la directory iniziale (directory corrente)
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        // Mostra il dialogo di selezione
        int risultato = fileChooser.showOpenDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            fileSelezionato = fileChooser.getSelectedFile();
            labelPercorso.setText(fileSelezionato.getAbsolutePath());
            labelPercorso.setForeground(Color.BLACK);
            btnConferma.setEnabled(true);
        }
        setVisible(false);
    }

    private void confermaFile() {
        if (fileSelezionato != null && fileSelezionato.exists()) {
            JOptionPane.showMessageDialog(this,
                "File selezionato:\n" + fileSelezionato.getAbsolutePath(),
                "Conferma",
                JOptionPane.INFORMATION_MESSAGE);

           writeFile(fileSelezionato.getAbsolutePath());

        } else {
            JOptionPane.showMessageDialog(this,
                "Il file selezionato non esiste!",
                "Errore",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeFile(String filePath) {
        try(FileWriter writer = new FileWriter("src.txt", false)) {
            writer.write(filePath);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    public File getFileSelezionato() {
        return fileSelezionato;
    }

}
