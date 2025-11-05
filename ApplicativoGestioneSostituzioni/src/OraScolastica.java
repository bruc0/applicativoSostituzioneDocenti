public class OraScolastica extends Ora {

    String giorno;

    public OraScolastica(int oraInizio, String giorno) {
        super(oraInizio);
        this.giorno = giorno;
    }

    public OraScolastica(int oraInizio, int minutiInizio, int durata, int minutiDurata) {
        super(oraInizio, minutiInizio, durata, minutiDurata);
    }
}
