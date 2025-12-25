import java.net.Socket;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientWeb {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("===  TESTS  ===\n");

        // // Test 1 : Demander index.html → doit retourner 200 OK
        // System.out.println("TEST 1 : Demande de /index.html ");
        // testerRequete("/index.html");
        // separerTests();

        // // Test 2 : Demander un fichier inexistant → doit retourner 404
        // System.out.println("TEST 2 : Demande de /fichier_inexistant.html ");
        // testerRequete("/fichier_inexistant.html");
        // separerTests();

        // Test 3 : Lancer 5 clients en même temps
        System.out.println("TEST 3 : Lancer 5 clients simultanés");
        testerConcurrence(5, "/index.html");
        separerTests();

        // // Test 4 : Demander une image
        // System.out.println("TEST 4 : Demande d'une image ");
        // // À adapter selon le nom réel de l'image dans ton dossier www
        // testerRequete("/image.jpg"); 
        // testerRequete("/img1.png");  // change avec le vrai nom de ton image
        // separerTests();

        // // Test 5 : Rappel pour test manuel avec navigateur
        // System.out.println("TEST 5 : Test manuel avec navigateur web");
        // System.out.println("→ Ouvrez Chrome ou Firefox et allez à : http://" + HOST + ":" + PORT + "/index.html");
        // System.out.println("→ Testez aussi : http://" + HOST + ":" + PORT + "/pg2.html");
       

        System.out.println("\n=== FIN DES TESTS ===");
    }

    public static void testerRequete(String chemin) {
        Socket socket = null;

        try {
            System.out.println("Connexion au serveur " + HOST + ":" + PORT + " pour demander : " + chemin);

            socket = new Socket(HOST, PORT);
            System.out.println(" Connecté");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Envoi de la requête HTTP
            out.println("GET " + chemin + " HTTP/1.1");
            out.println("Host: " + HOST);
            out.println("Connection: close");
            out.println();

            // Lecture de la réponse
            System.out.println("\n← Réponse du serveur :");
            System.out.println("-".repeat(60));

            String ligne;
            int ligneNum = 0;
            boolean enTete = true;

            while ((ligne = in.readLine()) != null) {
                ligneNum++;

                if (ligne.isEmpty() && enTete) {
                    enTete = false;
                    System.out.println("-".repeat(60));
                    System.out.println("Corps de la réponse :");
                    System.out.println("-".repeat(60));
                    continue;
                }

                if (ligneNum <= 30) {
                    System.out.println(ligne);
                } else if (ligneNum == 31) {
                    System.out.println("... (suite tronquée)");
                    break;
                }
            }

            System.out.println("-".repeat(60));
            System.out.println(" Fin de la réponse\n");

        } catch (IOException e) {
            System.err.println(" Erreur : " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Erreur fermeture socket : " + e.getMessage());
                }
            }
        }
    }

    // Test de concurrence : plusieurs clients en parallèle
    public static void testerConcurrence(int nbClients, String chemin) {
        ExecutorService executor = Executors.newFixedThreadPool(nbClients);

        for (int i = 1; i <= nbClients; i++) {
            final int clientId = i;
            final String fichier = chemin;

            executor.submit(() -> {
                System.out.println("[Client " + clientId + "] Démarrage - demande de " + fichier);
                testerRequete(fichier);
                System.out.println("[Client " + clientId + "] Terminé\n");
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Les clients n'ont pas tous terminé dans le délai imparti.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Séparateur visuel entre les tests
    public static void separerTests() {
        System.out.println("\n" + "=".repeat(80) + "\n");
    }
}