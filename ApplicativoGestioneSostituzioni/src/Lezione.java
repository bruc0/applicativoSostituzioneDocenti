import java.util.ArrayList;

public class Lezione {

    private int id;
    private String materia;
    private String classe;
    private String giorno;
    private ArrayList<Docente> docenti;


    public Lezione(int id, String materia, String classe, String giorno, ArrayList<Docente> docenti) {
        this.id = id;
        this.materia = materia;
        this.classe = classe;
        this.giorno = giorno;
        this.docenti = docenti;
    }

}
