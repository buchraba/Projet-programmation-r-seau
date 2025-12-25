import java.net.Socket;
import java.io.*;

public class TestComplet {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static int testsPasses = 0;
    private static int testsEchoues = 0;
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║          SUITE DE TESTS - SERVEUR WEB MINIMAL         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // Test 1: Demander index.html → doit retourner 200 OK
        test1_IndexHtml();
        
        pause(500);
        
        // Test 2: Demander fichier_inexistant.html → doit retourner 404
        test2_FichierInexistant();
        
        pause(500);
        
        // Test 3: Lancer 5 clients en même temps
        test3_ClientsSimultanes();
        
        pause(500);
        
        // Test 4: Demander une image
        test4_DemanderImage();
        
        pause(500);
        
        // Test 5: Instructions pour test navigateur
        test5_TestNavigateur();
        
        // Résumé
        afficherResume();
    }
    
    // TEST 1: Demander index.html → 200 OK
    public static void test1_IndexHtml() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  TEST 1: Demander index.html → doit retourner 200 OK  ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        
        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Envoyer requête
            out.println("GET /index.html HTTP/1.1");
            out.println("Host: " + HOST);
            out.println();
            
            // Lire la première ligne (statut HTTP)
            String statusLine = in.readLine();
            System.out.println("→ Requête: GET /index.html");
            System.out.println("← Réponse: " + statusLine);
            
            if (statusLine != null && statusLine.contains("200 OK")) {
                System.out.println("✓ TEST RÉUSSI: Code 200 OK reçu\n");
                testsPasses++;
            } else {
                System.out.println("✗ TEST ÉCHOUÉ: Code attendu 200, reçu: " + statusLine + "\n");
                testsEchoues++;
            }
            
            socket.close();
            
        } catch (IOException e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsEchoues++;
        }
    }
    
    // TEST 2: Demander fichier inexistant → 404
    public static void test2_FichierInexistant() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  TEST 2: Demander fichier_inexistant.html → 404      ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        
        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out.println("GET /fichier_inexistant.html HTTP/1.1");
            out.println("Host: " + HOST);
            out.println();
            
            String statusLine = in.readLine();
            System.out.println("→ Requête: GET /fichier_inexistant.html");
            System.out.println("← Réponse: " + statusLine);
            
            if (statusLine != null && statusLine.contains("404")) {
                System.out.println("✓ TEST RÉUSSI: Code 404 Not Found reçu\n");
                testsPasses++;
            } else {
                System.out.println("✗ TEST ÉCHOUÉ: Code attendu 404, reçu: " + statusLine + "\n");
                testsEchoues++;
            }
            
            socket.close();
            
        } catch (IOException e) {
            System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
            testsEchoues++;
        }
    }
    
    // TEST 3: Lancer 5 clients en même temps
    public static void test3_ClientsSimultanes() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  TEST 3: Lancer 5 clients en même temps              ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        
        Thread[] threads = new Thread[5];
        final int[] reussites = {0};
        
        for (int i = 0; i < 5; i++) {
            final int clientNum = i + 1;
            threads[i] = new Thread(() -> {
                try {
                    Socket socket = new Socket(HOST, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    out.println("GET /index.html HTTP/1.1");
                    out.println("Host: " + HOST);
                    out.println();
                    
                    String statusLine = in.readLine();
                    System.out.println("  Client #" + clientNum + " → Réponse: " + statusLine);
                    
                    if (statusLine != null && statusLine.contains("200")) {
                        synchronized (reussites) {
                            reussites[0]++;
                        }
                    }
                    
                    socket.close();
                    
                } catch (IOException e) {
                    System.out.println("  Client #" + clientNum + " → Erreur: " + e.getMessage());
                }
            });
        }
        
        // Démarrer tous les threads
        for (Thread t : threads) {
            t.start();
        }
        
        // Attendre que tous finissent
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\nRésultat: " + reussites[0] + "/5 clients ont reçu leur fichier");
        
        if (reussites[0] == 5) {
            System.out.println("✓ TEST RÉUSSI: Tous les clients ont été servis\n");
            testsPasses++;
        } else {
            System.out.println("✗ TEST ÉCHOUÉ: Seulement " + reussites[0] + "/5 clients servis\n");
            testsEchoues++;
        }
    }
     
    // TEST 4: Demander une image
    public static void test4_DemanderImage() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  TEST 4: Demander une image                          ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        
        // Vérifier si une image existe
        String[] imagesTest = {"/logo.png", "/image.jpg", "/photo.gif"};
        boolean imageTrouvee = false;
        
        for (String imagePath : imagesTest) {
            String basePath = new File("").getAbsolutePath();
            String projectRoot = basePath;
            if (!basePath.endsWith("src")) {
                projectRoot = basePath + File.separator + ".." + File.separator;
            } else {
                projectRoot = new File(basePath).getParent();
            }
            File f = new File(projectRoot + File.separator + "www" + imagePath);
            if (f.exists()) {
                try {
                    Socket socket = new Socket(HOST, PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    out.println("GET " + imagePath + " HTTP/1.1");
                    out.println("Host: " + HOST);
                    out.println();
                    
                    String statusLine = in.readLine();
                    String contentType = "";
                    
                    // Lire les headers pour trouver Content-Type
                    String ligne;
                    while ((ligne = in.readLine()) != null && !ligne.isEmpty()) {
                        if (ligne.startsWith("Content-Type:")) {
                            contentType = ligne;
                        }
                    }
                    
                    System.out.println("→ Requête: GET " + imagePath);
                    System.out.println("← Réponse: " + statusLine);
                    System.out.println("← " + contentType);
                    
                    if (statusLine != null && statusLine.contains("200") && 
                        contentType.contains("image/")) {
                        System.out.println("✓ TEST RÉUSSI: Image servie avec bon Content-Type\n");
                        testsPasses++;
                        imageTrouvee = true;
                    } else {
                        System.out.println("✗ TEST ÉCHOUÉ: Problème avec l'image\n");
                        testsEchoues++;
                        imageTrouvee = true;
                    }
                    
                    socket.close();
                    break;
                    
                } catch (IOException e) {
                    System.out.println("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n");
                    testsEchoues++;
                    imageTrouvee = true;
                    break;
                }
            }
        }
        
        if (!imageTrouvee) {
            System.out.println("⚠ ATTENTION: Aucune image trouvée dans le dossier www/");
            System.out.println("  Ajoutez logo.png, image.jpg ou photo.gif pour tester");
            System.out.println("  TEST IGNORÉ\n");
        }
    }
    
    // TEST 5: Instructions pour test navigateur
    public static void test5_TestNavigateur() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║  TEST 5: Tester avec un navigateur web               ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println("\n📌 INSTRUCTIONS MANUELLES:");
        System.out.println("   1. Ouvrez Chrome ou Firefox");
        System.out.println("   2. Allez sur: http://localhost:8080");
        System.out.println("   3. Vérifiez que la page s'affiche correctement");
        System.out.println("   4. Testez aussi: http://localhost:8080/pg2.html");
        System.out.println("   5. Testez un 404: http://localhost:8080/inexistant.html");
        System.out.println("\n   → Prenez des CAPTURES D'ÉCRAN pour le rapport!\n");
    }
    
    // Afficher le résumé
    public static void afficherResume() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                    RÉSUMÉ DES TESTS                    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("\n  Tests automatiques réussis: " + testsPasses);
        System.out.println("  Tests automatiques échoués: " + testsEchoues);
        System.out.println("  Tests manuels (navigateur): À faire\n");
        
        if (testsEchoues == 0) {
            System.out.println("  ✓ TOUS LES TESTS AUTOMATIQUES SONT PASSÉS!");
        } else {
            System.out.println("  ✗ Certains tests ont échoué. Vérifiez le serveur.");
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              N'OUBLIEZ PAS POUR LE RAPPORT:            ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Capture: Serveur démarré                           ║");
        System.out.println("║  2. Capture: Test index.html (200 OK)                  ║");
        System.out.println("║  3. Capture: Test 404                                  ║");
        System.out.println("║  4. Capture: 5 clients simultanés (logs serveur)       ║");
        System.out.println("║  5. Capture: Page dans Chrome/Firefox                  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
    
    // Pause entre tests
    private static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}