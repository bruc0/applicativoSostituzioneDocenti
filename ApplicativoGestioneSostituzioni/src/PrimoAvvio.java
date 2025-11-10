import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PrimoAvvio extends JFrame {

    private JTextField filePathField;
    private JButton browseButton;

    public PrimoAvvio() {
        super("Primo Avvio - Seleziona un file");

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Impossibile caricare il look and feel.");
        }


        setLayout(new BorderLayout(15, 15));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 150);
        setLocationRelativeTo(null);

        // Campo di testo (non modificabile)
        filePathField = new JTextField();
        filePathField.setEditable(false);
        filePathField.setFont(new Font("Consolas", Font.PLAIN, 14));

        // Bottone per scegliere il file
        browseButton = new JButton("Scegli file...");
        browseButton.setFocusPainted(false);
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        browseButton.addActionListener(e -> scegliFile());

        // Etichetta di titolo
        JLabel label = new JLabel("Seleziona un file da aprire:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        // Pannello centrale
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setOpaque(false);
        panel.add(filePathField, BorderLayout.CENTER);
        panel.add(browseButton, BorderLayout.EAST);

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(45, 45, 45)); // sfondo scuro personalizzato
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void writeFile(String filePath) {
        try(FileWriter writer = new FileWriter("src.txt", false)) {
            writer.write(filePath);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    private void scegliFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            JOptionPane.showMessageDialog(this,
                    "Hai selezionato:\n" + selectedFile.getName(),
                    "File selezionato",
                    JOptionPane.INFORMATION_MESSAGE);

            writeFile(selectedFile.getPath());
            dispose();
        }
    }

}






