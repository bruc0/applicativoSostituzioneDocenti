public class OraScolastica extends Ora {

    String giorno;

    public OraScolastica(int oraInizio, String giorno) {
        super(oraInizio);
        this.giorno = giorno;
    }

    public OraScolastica(int oraInizio, int minutiInizio, int durata, int minutiDurata, String giorno) {
        super(oraInizio, minutiInizio, durata, minutiDurata);
        this.giorno = giorno;
    }

    public OraScolastica(Ora ora, String giorno) {
        super(ora.getOraInizio()[0], ora.getOraInizio()[1], ora.getDurata()[0], ora.getDurata()[1]);
        this.giorno = giorno;
    }


    public boolean sovrapposizioneOra(Ora ora , String giorno) {
        return super.sovrapposizioneOra(ora) && this.giorno.equals(giorno);
    }
}
