import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ServeurWeb {
    private static final int PORT = 8080;
    private static int nombreClients = 0;
    
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        
        try {
            // Créer le ServerSocket sur le port 8080
            serverSocket = new ServerSocket(PORT);
            
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   SERVEUR WEB MINIMAL - DÉMARRÉ       ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("Port: " + PORT);
            System.out.println("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            System.out.println("En attente de connexions...\n");
            
            // Boucle infinie pour accepter les clients
            while (true) {
                try {
                    // Attendre et accepter une connexion client
                    Socket clientSocket = serverSocket.accept();
                    nombreClients++;
                    
                    // Afficher info du client
                    String clientIP = clientSocket.getInetAddress().getHostAddress();
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    
                    System.out.println("[" + timestamp + "] Nouveau client connecté");
                    System.out.println("  → IP: " + clientIP);
                    System.out.println("  → Port: " + clientSocket.getPort());
                    System.out.println("  → Total clients servis: " + nombreClients);
                    System.out.println();
                    
                    // Créer un nouveau thread pour gérer ce client
                    ClientHandler handler = new ClientHandler(clientSocket, nombreClients, null);
                    Thread thread = new Thread(handler);
                    thread.start();
                    
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'acceptation du client: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("ERREUR FATALE SERVEU ");
            
            if (e.getMessage().contains("Address already in use")) {
                System.err.println("Le port " + PORT + " est déjà utilisé.");
                System.err.println("Solutions:");
                System.err.println("  1. Fermez l'autre application utilisant ce port");
                System.err.println("  2. Changez le PORT dans le code");
                System.err.println("  3. Attendez quelques secondes et réessayez");
            } else {
                System.err.println("Erreur: " + e.getMessage());
            }
            
        } finally {
            // Fermer le serveur proprement
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("\nServeur fermé proprement.");
                } catch (IOException e) {
                    System.err.println("Erreur lors de la fermeture: " + e.getMessage());
                }
            }
        }
    }
}