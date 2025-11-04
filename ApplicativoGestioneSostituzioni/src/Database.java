import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Database {

    ArrayList<Docente> docenti;
    Lettore lettore;

    public Database(File src) throws FileNotFoundException {

        lettore = new Lettore(src);

    }

    public void letturaFile(){

        String str;
        try {
            if((str = lettore.leggiLinea()) != null){

                // Salta l'intestazione del CSV
                if(str.startsWith("NUMERO")) {
                    return;
                }

                // Dividi la riga CSV usando il delimitatore ;
                String[] campi = str.split(",");
                String[] durataStr = campi[1].split("h");
                String[] oraInizioStr = campi[7].split("h");
                String[] docentiStr = campi[3].split("");
                if(campi.length >= 8) {

                    int id = Integer.parseInt(campi[0]);
                    Ora ora = new Ora(Integer.parseInt(oraInizioStr[0]), Integer.parseInt(oraInizioStr[1]), Integer.parseInt(durataStr[0]), Integer.parseInt(durataStr[1]));
                    String materia = campi[2];
                    String classe = campi[4];
                    String giorno = campi[6];
                    ArrayList<Docente> docenti = new ArrayList<>();


                }
            }
        }catch (IOException ex){
            System.err.println("Errore durante la lettura del file: " + ex.getMessage());
        }catch (NumberFormatException ex){
            System.err.println("Errore nel formato del numero: " + ex.getMessage());
        }

    }

}
