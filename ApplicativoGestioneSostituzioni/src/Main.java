import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    private static Database database;
    private static LettoreSistema lettoreSistema;
    private PrimoAvvio primoAvvio;


    public static void main(String[] args) throws IOException {

        lettoreSistema = new LettoreSistema();
        lettoreSistema.writePrimoAccesso(true);



        try {
            if (lettoreSistema.readPrimoAccesso()==true) {
                new PrimoAvvio();
                lettoreSistema.writePrimoAccesso(false);
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file contattare l'amministratore di sistema: " + e.getMessage());
        }



        new interfacciaPrincipale();

    }

}
