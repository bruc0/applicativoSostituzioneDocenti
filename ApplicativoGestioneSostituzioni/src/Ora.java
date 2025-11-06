


public class Ora {

    private int[] oraInizio = new int[2];
    private int[] oraFine = new int[2];
    private int[] durata = new int[2];

    public Ora(int oraInizio) {
        this.oraInizio[0] = oraInizio;
        this.oraInizio[1] = oraInizio;
    }



    public Ora(int oraInizio, int minutiInizio, int durata, int minutiDurata) {
        this.oraInizio[0] = oraInizio;
        this.oraInizio[1] = minutiInizio;
        this.durata[0] = durata;
        this.durata[1] = minutiDurata;
        this.oraFine[0] = oraInizio + durata;
        this.oraFine[1] = minutiInizio + minutiDurata;
        if (this.oraFine[1] >= 60) {
            this.oraFine[0]++;
            this.oraFine[1] -= 60;
        }
    }



    public int[] getOraInizio() {
        return oraInizio;
    }

    public int[] getOraFine() {
        return oraFine;
    }

    public int[] getDurata() {
        return durata;
    }

    public boolean sovrapposizioneOra(Ora ora) {
        return (this.oraInizio[0] <= ora.oraInizio[0] && this.oraInizio[0] + this.durata[0] > ora.oraInizio[0]) ||
                (this.oraInizio[0] < ora.oraInizio[0] && this.oraInizio[0] + this.durata[0] >= ora.oraInizio[0] + ora.durata[0]);
    }
}
