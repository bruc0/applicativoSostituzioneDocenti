import java.io.*;
/*
* il lettore si occupa unicamente della lettura del file
* */
public class LettoreCSV {

    private File src;
    private File fileOutput;
    private static final String CSV_DELIMITER = ",";
    private BufferedReader lettoreCsv;



    public LettoreCSV(File src) throws FileNotFoundException {
        this.src = src;
        lettoreCsv = new BufferedReader(new FileReader(src));
        this.fileOutput = new File("ApplicativoGestioneSostituzioni/Orario_docenti_export.csv");
    }

    public String[] leggiLinea() throws IOException {

        String line = null;
        String[] campi = null;
        line = lettoreCsv.readLine();
        if(line != null)
            campi = line.split(CSV_DELIMITER);
        return campi;
    }


    public void close() throws IOException {
        lettoreCsv.close();
    }



}
