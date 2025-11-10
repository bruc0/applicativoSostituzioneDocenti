import java.io.*;
/*
* il lettore si occupa unicamente della lettura del file
* */
public class LettoreCSV {

    private File src;
    private File out;
    private static final String CSV_DELIMITER = ",";
    private BufferedReader lettoreCsv;
    private BufferedWriter scrittoreCsv;




    public LettoreCSV(File src) throws FileNotFoundException {
        this.src = src;

        out = new File("Orario.csv");
        lettoreCsv = new BufferedReader(new FileReader(src));
        try {
            scrittoreCsv = new BufferedWriter(new FileWriter(out, false));
        } catch (IOException e) {
            try {
                out.createNewFile();
                scrittoreCsv = new BufferedWriter(new FileWriter(out, false));
                FileWriter fileWriter = new FileWriter(out);
                fileWriter.write(out.getPath());
                fileWriter.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String[] leggiLinea() throws IOException {

        String line = null;
        String[] campi = null;
        line = lettoreCsv.readLine();
        if(line != null)
            campi = line.split(CSV_DELIMITER);
        return campi;
    }

    public void scriviLinea(Lezione lezione) throws IOException {

        scrittoreCsv.write(lezione.toString());
        scrittoreCsv.write("\n");
    }


    public void close() throws IOException {
        lettoreCsv.close();
    }

    public void setSrc(File src) {
        this.src = src;
    }



}
