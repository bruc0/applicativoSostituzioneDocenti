import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {


    ArrayList<Docente> docenti;
    ArrayList<String> classi;
    LettoreCSV lettoreCSV;
    ArrayList<Lezione> lezioni;
    HashMap<String, ArrayList<OraScolastica>> assenzeDocenti = new HashMap<>();
    Ora[] ore={new Ora(8, 0, 1, 0), new Ora(9, 0, 1, 0), new Ora(10, 0, 1, 0), new Ora(11, 10, 1, 0), new Ora(12, 0, 1, 0), new Ora(13, 0, 1, 0), new Ora(14, 0, 1, 0)};
    ArrayList<Lezione> lezioneDaSostituire = new ArrayList<>();

    public Database(File src) throws FileNotFoundException {

        lettoreCSV = new LettoreCSV(src);
        docenti = new ArrayList<>();
        classi = new ArrayList<>();
        lezioni = new ArrayList<>();

    }

    public void letturaFile(){
        String[] campi ;

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


                 if (campi.length >= 8) {

                     int id = Integer.parseInt(idstr[0]);
                     Ora ora = new Ora(Integer.parseInt(oraInizioStr[0]), Integer.parseInt(oraInizioStr[1]), Integer.parseInt(durataStr[0]), Integer.parseInt(durataStr[1]));
                     String materia = campi[2];
                     String classe = campi[4];
                     String giorno = campi[6];

                     // Crea un nuovo ArrayList per i docenti di questa lezione
                     ArrayList<Docente> docentiLezione = new ArrayList<>();

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

                     lezioni.add(new Lezione(id, materia, classe, giorno, docentiLezione, new Ora(Integer.parseInt(oraInizioStr[0]), Integer.parseInt(oraInizioStr[1]), Integer.parseInt(durataStr[0]), Integer.parseInt(durataStr[1]))));

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

    public Docente getDocente(String nome){
        for (Docente docente : docenti) {
            if (docente.getNome().equals(nome)) {
                return docente;
            }
        }
        return null;
    }

    public Lezione getLezione(Ora ora){
        for (Lezione lezione : lezioni) {

            if(lezione.getOra().sovrapposizioneOra(ora)){
                return lezione;
            }

        }

        return null;
    }

    public ArrayList<String> getClassi() {
        return classi;
    }

    public void setAssenzeDocenti(HashMap<String, ArrayList<OraScolastica>> assenzeDocenti) {
        this.assenzeDocenti = assenzeDocenti;
    }

    public HashMap<String, ArrayList<OraScolastica>> getAssenzeDocenti() {
        return assenzeDocenti;
    }

    /**
     * Metodo principale per gestire le sostituzioni dei docenti assenti.
     * Identifica le lezioni che necessitano di sostituzione e trova docenti disponibili.
     */
    public void Sostituzione(){
        // Pulisci la lista delle lezioni da sostituire
        lezioneDaSostituire.clear();

        // 1. Identifica tutte le lezioni che necessitano di sostituzione
        identificaLezioniDaSostituire();

        // 2. Per ogni lezione da sostituire, trova un docente sostituto
        for (Lezione lezione : lezioneDaSostituire) {
            Docente sostituto = trovaDocenteSostituto(lezione);
            if (sostituto != null) {
                System.out.println("Lezione: " + lezione.getMateria() + " - " + lezione.getClasse() +
                                 " (" + lezione.getGiorno() + " " + lezione.getOra().getOraInizio()[0] + ":" +
                                 String.format("%02d", lezione.getOra().getOraInizio()[1]) + ")");
                System.out.println("  Docente assente: " + lezione.getDocentiString());
                System.out.println("  Docente sostituto: " + sostituto.getNome());
                System.out.println();
            } else {
                System.out.println("ATTENZIONE: Nessun sostituto trovato per la lezione:");
                System.out.println("  " + lezione.getMateria() + " - " + lezione.getClasse() +
                                 " (" + lezione.getGiorno() + " " + lezione.getOra().getOraInizio()[0] + ":" +
                                 String.format("%02d", lezione.getOra().getOraInizio()[1]) + ")");
                System.out.println();
            }
        }
    }

    /**
     * Identifica tutte le lezioni che necessitano di sostituzione basandosi sulle assenze registrate.
     */
    private void identificaLezioniDaSostituire() {
        // Itera su tutti i docenti assenti
        for (String nomeDocente : assenzeDocenti.keySet()) {
            Docente docenteAssente = getDocente(nomeDocente);
            if (docenteAssente == null) continue;

            ArrayList<OraScolastica> oreAssenza = assenzeDocenti.get(nomeDocente);

            // Per ogni ora di assenza, trova le lezioni corrispondenti
            for (OraScolastica oraAssenza : oreAssenza) {
                // Cerca tutte le lezioni del docente in quel giorno e ora
                for (Lezione lezione : lezioni) {
                    // Verifica se la lezione è del docente assente
                    boolean docenteNellaLezione = false;
                    for (Docente doc : lezione.getDocenti()) {
                        if (doc.getNome().equals(nomeDocente)) {
                            docenteNellaLezione = true;
                            break;
                        }
                    }

                    // Verifica se giorno e ora coincidono
                    if (docenteNellaLezione &&
                        lezione.getGiorno().equalsIgnoreCase(oraAssenza.giorno) &&
                        lezione.getOra().sovrapposizioneOra(oraAssenza)) {

                        // Aggiungi alla lista solo se non è già presente
                        if (!lezioneDaSostituire.contains(lezione)) {
                            lezioneDaSostituire.add(lezione);
                        }
                    }
                }
            }
        }
    }

    /**
     * Trova un docente sostituto per una lezione specifica.
     * Criteri di selezione specifici:
     * 1. COMPRESENZA: Se il docente assente è in compresenza, sostituto = collega presente
     * 2. DISPONIBILITÀ DA DISPOSIZIONI:
     *    - Docente della stessa classe
     *    - Docente di materia affine
     *    - Qualsiasi altro docente libero
     * 3. COMPRESENZE NON QUINTE:
     *    - Prima docenti della classe o affini
     *    - Poi altri docenti non delle quinte
     * 4. COMPRESENZE QUINTE:
     *    - Prima materia affine
     *    - Poi qualsiasi materia
     * 5. ORE LIBERE A PAGAMENTO:
     *    - Docenti liberi in quell'ora
     *    - Priorità a chi ha lezione nell'ora precedente o successiva
     */
    private Docente trovaDocenteSostituto(Lezione lezione) {
        String giorno = lezione.getGiorno();
        Ora ora = lezione.getOra();
        String materia = lezione.getMateria();
        String classe = lezione.getClasse();

        // Verifica se è una classe quinta
        boolean isClasseQuinta = classe.startsWith("5");

        // 1. COMPRESENZA: Se ci sono più docenti nella lezione, uno è assente e l'altro presente
        if (lezione.getDocenti().size() > 1) {
            for (Docente docente : lezione.getDocenti()) {
                // Verifica se questo docente NON è assente
                boolean isAssente = false;
                if (assenzeDocenti.containsKey(docente.getNome())) {
                    for (OraScolastica oraAssenza : assenzeDocenti.get(docente.getNome())) {
                        if (oraAssenza.giorno.equalsIgnoreCase(giorno) &&
                            ora.sovrapposizioneOra(oraAssenza)) {
                            isAssente = true;
                            break;
                        }
                    }
                }
                // Se non è assente, è il sostituto ideale (collega presente)
                if (!isAssente) {
                    return docente;
                }
            }
        }

        // Liste per categorizzare i docenti disponibili
        ArrayList<Docente> docentiDisposizioneStessaClasse = new ArrayList<>();
        ArrayList<Docente> docentiDisposizioneMateriaAffine = new ArrayList<>();
        ArrayList<Docente> docentiDisposizioneAltri = new ArrayList<>();
        ArrayList<Docente> docentiDisposizioneNonQuinte = new ArrayList<>();
        ArrayList<Docente> docentiLiberiConLezioneAdiacente = new ArrayList<>();
        ArrayList<Docente> docentiLiberiGenerici = new ArrayList<>();

        // Analizza tutti i docenti
        for (Docente docente : docenti) {
            // Salta i docenti assenti in questo orario
            if (isDocenteAssente(docente, giorno, ora)) {
                continue;
            }

            // Salta i docenti già impegnati nella lezione da sostituire
            if (isDocenteImpegnatoNellaLezione(docente, lezione)) {
                continue;
            }

            // 2. DISPONIBILITÀ DA DISPOSIZIONI
            boolean haDisposizione = haDisposizioneInOrario(docente, giorno, ora);

            if (haDisposizione) {
                boolean insegnaClasse = docente.getClassiInsegnate().contains(classe);
                boolean materiaDiInsegnamento = docente.getMaterieDiInsegnamento().contains(materia);
                boolean materiaAffine = isMateriaAffine(materia, docente.getMaterieDiInsegnamento());

                // Per compresenze non quinte
                if (!isClasseQuinta && lezione.getDocenti().size() > 1) {
                    if (insegnaClasse || materiaDiInsegnamento || materiaAffine) {
                        docentiDisposizioneStessaClasse.add(docente);
                    } else if (!insegnaClasseQuinta(docente)) {
                        docentiDisposizioneNonQuinte.add(docente);
                    }
                }
                // Per compresenze quinte
                else if (isClasseQuinta && lezione.getDocenti().size() > 1) {
                    if (materiaDiInsegnamento || materiaAffine) {
                        docentiDisposizioneMateriaAffine.add(docente);
                    } else {
                        docentiDisposizioneAltri.add(docente);
                    }
                }
                // Per lezioni normali (non compresenza)
                else {
                    if (insegnaClasse) {
                        docentiDisposizioneStessaClasse.add(docente);
                    } else if (materiaDiInsegnamento || materiaAffine) {
                        docentiDisposizioneMateriaAffine.add(docente);
                    } else {
                        docentiDisposizioneAltri.add(docente);
                    }
                }
            }
            // 5. ORE LIBERE A PAGAMENTO
            else if (isLiberoInOrario(docente, giorno, ora)) {
                // Priorità a chi ha lezione nell'ora precedente o successiva
                if (haLezioneAdiacente(docente, giorno, ora)) {
                    docentiLiberiConLezioneAdiacente.add(docente);
                } else {
                    docentiLiberiGenerici.add(docente);
                }
            }
        }

        // Restituisci il miglior candidato disponibile in base ai criteri

        // Compresenze non quinte
        if (!isClasseQuinta && lezione.getDocenti().size() > 1) {
            if (!docentiDisposizioneStessaClasse.isEmpty()) {
                return docentiDisposizioneStessaClasse.get(0);
            }
            if (!docentiDisposizioneNonQuinte.isEmpty()) {
                return docentiDisposizioneNonQuinte.get(0);
            }
        }

        // Compresenze quinte
        if (isClasseQuinta && lezione.getDocenti().size() > 1) {
            if (!docentiDisposizioneMateriaAffine.isEmpty()) {
                return docentiDisposizioneMateriaAffine.get(0);
            }
            if (!docentiDisposizioneAltri.isEmpty()) {
                return docentiDisposizioneAltri.get(0);
            }
        }

        // Lezioni normali - priorità disposizioni
        if (!docentiDisposizioneStessaClasse.isEmpty()) {
            return docentiDisposizioneStessaClasse.get(0);
        }
        if (!docentiDisposizioneMateriaAffine.isEmpty()) {
            return docentiDisposizioneMateriaAffine.get(0);
        }
        if (!docentiDisposizioneAltri.isEmpty()) {
            return docentiDisposizioneAltri.get(0);
        }

        // Ore libere a pagamento
        if (!docentiLiberiConLezioneAdiacente.isEmpty()) {
            return docentiLiberiConLezioneAdiacente.get(0);
        }
        if (!docentiLiberiGenerici.isEmpty()) {
            return docentiLiberiGenerici.get(0);
        }

        return null; // Nessun sostituto disponibile
    }

    /**
     * Verifica se un docente è assente in un determinato orario
     */
    private boolean isDocenteAssente(Docente docente, String giorno, Ora ora) {
        if (assenzeDocenti.containsKey(docente.getNome())) {
            for (OraScolastica oraAssenza : assenzeDocenti.get(docente.getNome())) {
                if (oraAssenza.giorno.equalsIgnoreCase(giorno) &&
                    ora.sovrapposizioneOra(oraAssenza)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica se un docente è già impegnato nella lezione
     */
    private boolean isDocenteImpegnatoNellaLezione(Docente docente, Lezione lezione) {
        for (Docente doc : lezione.getDocenti()) {
            if (doc.getNome().equals(docente.getNome())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se un docente ha disposizione in un determinato orario
     */
    private boolean haDisposizioneInOrario(Docente docente, String giorno, Ora ora) {
        for (OraScolastica oraDisp : docente.getOreDisposizione()) {
            if (oraDisp.giorno.equalsIgnoreCase(giorno) &&
                ora.sovrapposizioneOra(oraDisp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se un docente è libero in un determinato orario
     */
    private boolean isLiberoInOrario(Docente docente, String giorno, Ora ora) {
        for (OraScolastica oraLibera : docente.getOreLibere()) {
            if (oraLibera.giorno.equalsIgnoreCase(giorno) &&
                ora.sovrapposizioneOra(oraLibera)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se un docente insegna in classi quinte
     */
    private boolean insegnaClasseQuinta(Docente docente) {
        for (String classe : docente.getClassiInsegnate()) {
            if (classe.startsWith("5")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se un docente ha lezione nell'ora precedente o successiva
     */
    private boolean haLezioneAdiacente(Docente docente, String giorno, Ora ora) {
        int oraCorrente = ora.getOraInizio()[0];

        for (OraScolastica oraInsegnamento : docente.getOreInsegnamento()) {
            if (oraInsegnamento.giorno.equalsIgnoreCase(giorno)) {
                int oraLezione = oraInsegnamento.getOraInizio()[0];
                // Verifica se è nell'ora precedente o successiva
                if (Math.abs(oraLezione - oraCorrente) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica se due materie sono affini
     * Puoi personalizzare questa logica in base alle tue esigenze
     */
    private boolean isMateriaAffine(String materia, ArrayList<String> materieDiInsegnamento) {
        // Definisci gruppi di materie affini
        String[][] materieAffini = {
            {"Matematica", "Fisica", "Informatica"},
            {"Italiano", "Storia", "Geografia", "Filosofia"},
            {"Inglese", "Francese", "Spagnolo", "Tedesco"},
            {"Scienze", "Biologia", "Chimica"},
            {"Arte", "Disegno", "Storia dell'Arte"},
            {"Educazione Fisica", "Scienze Motorie"}
        };

        // Trova il gruppo della materia
        for (String[] gruppo : materieAffini) {
            boolean materiaInGruppo = false;
            for (String m : gruppo) {
                if (m.equalsIgnoreCase(materia)) {
                    materiaInGruppo = true;
                    break;
                }
            }

            // Se la materia è in questo gruppo, verifica se il docente insegna altre materie del gruppo
            if (materiaInGruppo) {
                for (String materiaDocente : materieDiInsegnamento) {
                    for (String m : gruppo) {
                        if (m.equalsIgnoreCase(materiaDocente) && !m.equalsIgnoreCase(materia)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Restituisce la lista delle lezioni che necessitano di sostituzione.
     */
    public ArrayList<Lezione> getLezioniDaSostituire() {
        return lezioneDaSostituire;
    }



}
