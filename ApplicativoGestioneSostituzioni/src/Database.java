import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Database {


    ArrayList<Docente> docenti;
    ArrayList<String> classi;
    LettoreCSV lettoreCSV;
    Ora[] ore={new Ora(8, 0, 1, 0), new Ora(9, 0, 1, 0), new Ora(10, 0, 1, 0), new Ora(11, 10, 1, 0), new Ora(12, 0, 1, 0), new Ora(13, 0, 1, 0), new Ora(14, 0, 1, 0)};


    public Database(File src) throws FileNotFoundException {

        lettoreCSV = new LettoreCSV(src);
        docenti = new ArrayList<>();
        classi = new ArrayList<>();

    }

    public void letturaFile(){
        String[] campi ;
        ArrayList<Docente> docentiLezione = new ArrayList<>();


        try {
            while ((campi = lettoreCSV.leggiLinea()) != null ) {

                // Salta l'intestazione del CSV
             if (!campi[0].equals("NUMERO")){


                 System.out.println(campi.length);
                 for (String campiStr : campi) {
                     System.out.println(campiStr);
                 }
                 String[] idstr = campi[0].split("\\.");
                 System.out.println(idstr[0]);
                 String[] durataStr = campi[1].split("h");
                 String[] oraInizioStr = campi[7].split("h");
                 String[] docentiStr = campi[3].split(";");

                 for (String campiStr : campi) {
                     System.out.println(campiStr);
                 }

                 if (campi.length >= 8) {

                     int id = Integer.parseInt(idstr[0]);
                     Ora ora = new Ora(Integer.parseInt(oraInizioStr[0]), Integer.parseInt(oraInizioStr[1]), Integer.parseInt(durataStr[0]), Integer.parseInt(durataStr[1]));
                     String materia = campi[2];
                     String classe = campi[4];
                     String giorno = campi[6];



                     for (int i = 0; i < docentiStr.length; i++) {
                         Docente doc = new Docente(docentiStr[i]);
                         docentiLezione.add(doc);
                         if (!checkDocente(doc.getNome()))
                         this.docenti.add(doc);
                         doc.aggiungiOraInsegnamento(new OraScolastica(ora, giorno));
                         if (materia.equals("Disposizione")) {
                             doc.aggiungiOraDisposizione(new OraScolastica(ora, giorno));
                         } else {
                             doc.aggiungiMateriaDiInsegnamento(materia);

                         }

                         doc.aggiungiClasseInsegnata(classe);

                     }

                     for (Docente d : docenti) {

                         if (!docentiLezione.contains(d)) {
                             d.aggiungiOraLibera(new OraScolastica(ora, giorno));
                         }

                     }

                     if (!this.classi.contains(classe)) {
                         this.classi.add(classe);
                     }

                 }
             }

             campi=null;

            }
        }catch (IOException ex){
            System.err.println("Errore durante la lettura del file: " + ex.getMessage());
        }catch (NumberFormatException ex){
            System.err.println("Errore nel formato del numero: " + ex.getMessage());
        }

    }

    private boolean checkDocente(String docente) {
        for (Docente d : docenti) {
            if (d.getNome().equals(docente)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Docente> getDocenti() {
        return docenti;
    }
    public ArrayList<String> getStrDocenti() {

        ArrayList<String> strDocenti = new ArrayList<>();
        for (Docente docente : docenti) {
            strDocenti.add(docente.getNome());
        }
        return strDocenti;

    }

    public ArrayList<String> getClassi() {
        return classi;
    }
}
