import java.util.ArrayList;

public class Lezione {

    private int id;
    private String materia;
    private String classe;
    private Ora ora;
    private char coDocente;
    private String giorno;
    private ArrayList<Docente> docenti;


    public Lezione(int id, String materia, String classe, String giorno, ArrayList<Docente> docenti, Ora ora, char coDocente) {
        this.id = id;
        this.materia = materia;
        this.classe = classe;
        this.giorno = giorno;
        this.docenti = docenti;
        this.ora = ora;
        this.coDocente = coDocente;
    }

    public Lezione(int id, String materia, String classe, String giorno, Docente docente, Ora ora, char coDocente) {
        this.id = id;
        this.materia = materia;
        this.classe = classe;
        this.giorno = giorno;
        docenti = new ArrayList<>();
        docenti.add(docente);
        this.ora = ora;
        this.coDocente = coDocente;
    }

    public Ora getOra() {
        return ora;
    }

    public String getGiorno(){
        return giorno;
    }

    public String getMateria() {
        return materia;
    }

    public String getClasse() {
        return classe;
    }

    public ArrayList<Docente> getDocenti() {
        return docenti;
    }

    public String getDocentiString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < docenti.size(); i++) {
            sb.append(docenti.get(i).getNome());
            if (i < docenti.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String docentiString() {
      String str = "";

        for (Docente docente : docenti) {
          str += docente.getNome() + ";";
      }
        return str;
    }

    public char getCoDocente() {
        return coDocente;
    }
    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return id + "," + ora.toString(ora.getDurata()[0], ora.getDurata()[1]) + "," + materia + "," + docentiString() + "," + classe + "," +coDocente+","+giorno+","+ora.toString(ora.getOraFine()[0], ora.getOraFine()[1]);
    }
    public boolean equals(Lezione lezione) {
        return this.id == lezione.id &&
               this.materia.equals(lezione.materia) &&
               this.classe.equals(lezione.classe) &&
               this.giorno.equals(lezione.giorno) &&
               this.coDocente == lezione.coDocente &&
               this.ora.equals(lezione.ora);
    }
}
