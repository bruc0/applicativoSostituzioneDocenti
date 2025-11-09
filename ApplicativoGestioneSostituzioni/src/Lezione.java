import java.util.ArrayList;

public class Lezione {

    private int id;
    private String materia;
    private String classe;
    private Ora ora;
    private String giorno;
    private ArrayList<Docente> docenti;


    public Lezione(int id, String materia, String classe, String giorno, ArrayList<Docente> docenti, Ora ora) {
        this.id = id;
        this.materia = materia;
        this.classe = classe;
        this.giorno = giorno;
        this.docenti = docenti;
        this.ora = ora;
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

}
