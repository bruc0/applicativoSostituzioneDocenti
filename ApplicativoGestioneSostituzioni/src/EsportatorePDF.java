import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Classe per esportare il file Orario.csv in formato PDF
 * Utilizza la libreria iText per la generazione del PDF
 */
public class EsportatorePDF {

    private static final String CSV_DELIMITER = ",";
    private File csvFile;
    private String outputPath;

    /**
     * Costruttore che accetta il percorso del file CSV
     * @param csvFilePath Percorso del file CSV da esportare
     */
    public EsportatorePDF(String csvFilePath) {
        this.csvFile = new File(csvFilePath);
        this.outputPath = csvFilePath.replace(".csv", ".pdf");
    }

    /**
     * Costruttore che accetta il percorso del file CSV e il percorso di output
     * @param csvFilePath Percorso del file CSV da esportare
     * @param outputPath Percorso del file PDF di output
     */
    public EsportatorePDF(String csvFilePath, String outputPath) {
        this.csvFile = new File(csvFilePath);
        this.outputPath = outputPath;
    }

    /**
     * Esporta il file CSV in formato PDF
     * @return true se l'esportazione è avvenuta con successo, false altrimenti
     */
    public boolean esportaPDF() {
        try {
            // Leggi i dati dal CSV
            ArrayList<String[]> datiCSV = leggiCSV();

            if (datiCSV.isEmpty()) {
                System.err.println("Il file CSV è vuoto o non esiste");
                return false;
            }

            // Crea il documento PDF
            Document document = new Document(PageSize.A4.rotate()); // Orientamento orizzontale per tabelle larghe
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            document.open();

            // Aggiungi titolo
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Orario Scolastico", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Crea la tabella
            PdfPTable table = creaTabellaOrario(datiCSV);
            document.add(table);

            // Aggiungi data di generazione
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph footer = new Paragraph("\nGenerato il: " + new java.util.Date(), footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();

            System.out.println("PDF generato con successo: " + outputPath);
            return true;

        } catch (FileNotFoundException e) {
            System.err.println("File CSV non trovato: " + csvFile.getAbsolutePath());
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            System.err.println("Errore nella creazione del documento PDF");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Errore di I/O durante la lettura del CSV");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Legge il contenuto del file CSV
     * @return ArrayList contenente le righe del CSV come array di stringhe
     */
    private ArrayList<String[]> leggiCSV() throws IOException {
        ArrayList<String[]> dati = new ArrayList<>();

        if (!csvFile.exists()) {
            return dati;
        }

        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] campi = line.split(CSV_DELIMITER);
            dati.add(campi);
        }

        reader.close();
        return dati;
    }

    /**
     * Crea la tabella PDF dall'ArrayList di dati CSV
     * @param datiCSV Dati letti dal CSV
     * @return PdfPTable formattata
     */
    private PdfPTable creaTabellaOrario(ArrayList<String[]> datiCSV) throws DocumentException {
        // Determina il numero di colonne dalla prima riga
        int numColonne = datiCSV.get(0).length;
        PdfPTable table = new PdfPTable(numColonne);
        table.setWidthPercentage(100);

        // Imposta larghezze relative delle colonne
        float[] columnWidths = calcolaLarghezzeColonne(numColonne);
        table.setWidths(columnWidths);

        // Font per intestazione e celle
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

        // Aggiungi intestazione (prima riga del CSV)
        String[] intestazione = datiCSV.get(0);
        for (String colonna : intestazione) {
            PdfPCell headerCell = new PdfPCell(new Phrase(colonna, headerFont));
            headerCell.setBackgroundColor(new BaseColor(41, 128, 185)); // Blu
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setPadding(8);
            table.addCell(headerCell);
        }

        // Aggiungi le righe di dati
        boolean rigaAlternata = false;
        for (int i = 1; i < datiCSV.size(); i++) {
            String[] riga = datiCSV.get(i);

            for (String cella : riga) {
                PdfPCell dataCell = new PdfPCell(new Phrase(cella, cellFont));
                dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                dataCell.setPadding(5);

                // Alterna i colori delle righe per migliore leggibilità
                if (rigaAlternata) {
                    dataCell.setBackgroundColor(new BaseColor(236, 240, 241)); // Grigio chiaro
                }

                table.addCell(dataCell);
            }

            rigaAlternata = !rigaAlternata;
        }

        return table;
    }

    /**
     * Calcola le larghezze relative delle colonne in base al numero di colonne
     * @param numColonne Numero di colonne nella tabella
     * @return Array di larghezze relative
     */
    private float[] calcolaLarghezzeColonne(int numColonne) {
        // Larghezze personalizzate per il formato dell'orario
        // NUMERO, DURATA, MAT_NOME, DOC_COGN, CLASSE, CO-DOC., GIORNO, O.INIZIO
        if (numColonne == 8) {
            return new float[]{1f, 1.2f, 2f, 2.5f, 1.5f, 1f, 1.5f, 1.2f};
        } else {
            // Larghezze uniformi se il formato è diverso
            float[] widths = new float[numColonne];
            for (int i = 0; i < numColonne; i++) {
                widths[i] = 1f;
            }
            return widths;
        }
    }

    /**
     * Metodo statico di utilità per esportare rapidamente un CSV in PDF
     * @param csvPath Percorso del file CSV
     * @return true se l'esportazione è avvenuta con successo
     */
    public static boolean esporta(String csvPath) {
        EsportatorePDF esportatore = new EsportatorePDF(csvPath);
        return esportatore.esportaPDF();
    }

    /**
     * Metodo statico di utilità per esportare un CSV in PDF con percorso personalizzato
     * @param csvPath Percorso del file CSV
     * @param pdfPath Percorso del file PDF di output
     * @return true se l'esportazione è avvenuta con successo
     */
    public static boolean esporta(String csvPath, String pdfPath) {
        EsportatorePDF esportatore = new EsportatorePDF(csvPath, pdfPath);
        return esportatore.esportaPDF();
    }

    /**
     * Main di esempio per testare la classe
     */
    public static void main(String[] args) {
        // Esempio di utilizzo
        String csvPath = "Orario.csv";
        String pdfPath = "Orario.pdf";

        EsportatorePDF esportatore = new EsportatorePDF(csvPath, pdfPath);

        if (esportatore.esportaPDF()) {
            System.out.println("Esportazione completata con successo!");
        } else {
            System.out.println("Errore durante l'esportazione.");
        }
    }

    // Getter e Setter
    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }
}
