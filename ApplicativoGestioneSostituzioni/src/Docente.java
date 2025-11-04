import java.util.ArrayList;
import java.util.Objects;

/*
* La classe docente sara uno dei fulcri del programma in quanto sono presenti i vari dati
* naecessari per l'alcoritmo delle sostituzioni
* */

public class Docente {

    private String nome;
    private ArrayList<String> classiInsegnate;
    private ArrayList<String> materieDiInsegnamento;
    private ArrayList<Ora> oreInsegnamento;
    private ArrayList<Ora> oreLibere;
    private ArrayList<Ora> oreDisposizione;


    public Docente(String nome) {
        this.nome = nome;
        this.classiInsegnate = new ArrayList<>();
        this.materieDiInsegnamento = new ArrayList<>();
        this.oreInsegnamento = new ArrayList<>();
        this.oreLibere = new ArrayList<>();
        this.oreDisposizione = new ArrayList<>();
    }

    public void aggiungiClasseInsegnata(String classe) {
        if (!classiInsegnate.contains(classe)) {
            classiInsegnate.add(classe);
        }
    }

    public void aggiungiMateriaDiInsegnamento(String materia) {
        if (!materieDiInsegnamento.contains(materia)) {
            materieDiInsegnamento.add(materia);
        }
    }

    public void aggiungiOraInsegnamento(Ora ora) {
        if (!oreInsegnamento.contains(ora)) {
            oreInsegnamento.add(ora);
        }
    }

    public void aggiungiOraLibera(Ora ora) {
        if (!oreLibere.contains(ora)) {
            oreLibere.add(ora);
        }
    }

    public void aggiungiOraDisposizione(Ora ora) {
        if (!oreDisposizione.contains(ora)) {
            oreDisposizione.add(ora);
        }
    }

    /* l'equals confronta unicamente il nome perche nell' esercizio non
           sono presenti omonimi
        * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Docente docente)) return false;
        return Objects.equals(nome, docente.nome);
    }


}
