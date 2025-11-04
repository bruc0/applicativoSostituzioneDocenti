import javax.imageio.IIOException;
import java.io.*;
/*
* il lettore si occupa unicamente della lettura del file
* */
public class Lettore {

    private File src;
    private static final String CSV_DELIMITER = ",";
    private BufferedReader lettoreCsv;


    public Lettore(File src) throws FileNotFoundException {
        this.src = src;
        lettoreCsv = new BufferedReader(new FileReader(src));
    }

    public String leggiLinea() throws IOException {

        String line;
        line = lettoreCsv.readLine();
        return line;
    }

}
