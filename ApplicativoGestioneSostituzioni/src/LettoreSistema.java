import java.io.*;

public class LettoreSistema {

    private File src;
    private File pathCsv;
    private FileInputStream in;

    public LettoreSistema() {
        src = new File("settings.dat");
        pathCsv = new File("src.txt");
        try {
            in = new FileInputStream(src);
            writePrimoAccesso(false);
        } catch (FileNotFoundException e) {
            fileNotFound();
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }

    public boolean readPrimoAccesso() throws IOException {
        FileInputStream fis = new FileInputStream(src);
        DataInputStream dis = new DataInputStream(fis);
        boolean primoAccesso = dis.readBoolean();
        dis.close();
        fis.close();
        return primoAccesso;

    }

    public void writePrimoAccesso(boolean data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(src);
             DataOutputStream dos = new DataOutputStream(fos)) {
            dos.writeBoolean(data);
        }
    }

    private void fileNotFound() {
        try {
            // Crea il file settings.dat
            File settingsFile = new File("settings.dat");

            // Crea un nuovo file vuoto
            boolean created = settingsFile.createNewFile();

            if (created) {
                System.out.println("File settings.dat creato con successo.");
            } else {
                System.out.println("Il file settings.dat esiste già.");
            }

            // Imposta il percorso del file corrente al nuovo file
            this.src = settingsFile;

            // Scrive false nel file usando writeBoolean
            writePrimoAccesso(true);

        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file settings.dat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String readFile() throws IOException {
        FileInputStream fis = new FileInputStream(pathCsv);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            return br.readLine();
        } catch (IOException e) {

            try {
                // Crea il file settings.dat
                File settingsFile = new File("src.txt");

                // Crea un nuovo file vuoto
                boolean created = settingsFile.createNewFile();

                if (created) {
                    System.out.println("File src.txt creato con successo.");
                } else {
                    System.out.println("Il file src.txt esiste già.");
                }

                return pathCsv.getPath();
            } finally {
                fis.close();
            }
        }
    }
}