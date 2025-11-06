import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    private static Database database;
    private static LettoreSistema lettoreSistema;
    private static PrimoAvvio primoAvvio;


    public static void main(String[] args) throws IOException {

        lettoreSistema = new LettoreSistema();
       lettoreSistema.writePrimoAccesso(true);



        try {
            if (lettoreSistema.readPrimoAccesso()==true) {
                primoAvvio = new PrimoAvvio();
                primoAvvio.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        primoAvvio.dispose();

                        startInterfacciaPrincipale();
                        startDatabase();
                    }
                                             });
                lettoreSistema.writePrimoAccesso(false);

            }else{
                startInterfacciaPrincipale();
                startInterfacciaPrincipale();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file contattare l'amministratore di sistema: " + e.getMessage());
        }





    }

    private static void startInterfacciaPrincipale() {
        new interfacciaPrincipale();
    }

    private static void startDatabase(){
        try {
            database = new Database(new File(lettoreSistema.readFile()));
            database.letturaFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
