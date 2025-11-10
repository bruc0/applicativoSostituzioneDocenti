import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    private static Database database;
    private static LettoreSistema lettoreSistema;
    private static PrimoAvvio primoAvvio;
    private static interfacciaPrincipale interfacciaPrincipale;


    public static void main(String[] args) throws IOException {

        lettoreSistema = new LettoreSistema();
        lettoreSistema.writePrimoAccesso(false);



        try {
            if (lettoreSistema.readPrimoAccesso()==true) {
                primoAvvio = new PrimoAvvio();
                primoAvvio.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        primoAvvio.dispose();

                        startDatabase();
                        startInterfacciaPrincipale();
                    }
                                             });
                lettoreSistema.writePrimoAccesso(false);

            }else{
                startDatabase();
                startInterfacciaPrincipale();

            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file contattare l'amministratore di sistema: " + e.getMessage());
        }




    }

    private static void startInterfacciaPrincipale() {
        interfacciaPrincipale = new interfacciaPrincipale();
        interfacciaPrincipale.setDatabase(database);
        interfacciaPrincipale.popolaComboBoxClassi(database.getClassi());
        interfacciaPrincipale.popolaComboBoxDocenti(database.getStrDocenti());

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
