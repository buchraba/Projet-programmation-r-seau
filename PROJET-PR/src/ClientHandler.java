import java.net.Socket;
import java.io.*;
import java.io.File;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int clientId;
    private ServerGUI gui;  // Reference to GUI

    public ClientHandler(Socket socket, int id, ServerGUI gui) {
        this.clientSocket = socket;
        this.clientId = id;
        this.gui = gui;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        OutputStream outStream = null;
        String clientIP = clientSocket.getInetAddress().getHostAddress();

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            outStream = clientSocket.getOutputStream();

            String requete = in.readLine();
            if (requete == null || requete.isEmpty()) {
                gui.log("Client #" + clientId + " (" + clientIP + ") → Empty request [400]");
                GestionErreurs.envoyerErreur400(out);
                gui.updateClient(clientId, "", 400);
                return;
            }

            gui.log("Client #" + clientId + " (" + clientIP + ") → " + requete);

            String ligne;
            while ((ligne = in.readLine()) != null && !ligne.isEmpty()) {}

            String[] parts = requete.split(" ");
            if (parts.length < 3) {
                gui.log("Client #" + clientId + " → Malformed request [400]");
                GestionErreurs.envoyerErreur400(out);
                gui.updateClient(clientId, "", 400);
                return;
            }

            String methode = parts[0];
            String chemin = parts[1];

            if (!methode.equals("GET")) {
                gui.log("Client #" + clientId + " → Method " + methode + " not allowed [400]");
                GestionErreurs.envoyerErreur400(out);
                gui.updateClient(clientId, chemin, 400);
                return;
            }

            if (chemin.contains("..")) {
                gui.log("Client #" + clientId + " → Directory traversal attempt [403]");
                GestionErreurs.envoyerErreur403(out);
                gui.updateClient(clientId, chemin, 403);
                return;
            }

            if (chemin.equals("/")) chemin = "/index.html";

            // Construire le chemin complet en fonction du répertoire d'exécution
            String basePath = new File("").getAbsolutePath();
            String projectRoot = basePath;
            if (!basePath.endsWith(File.separator + "src")) {
                projectRoot = basePath + File.separator + ".." + File.separator;
            } else {
                projectRoot = new File(basePath).getParent();
            }
            String cheminComplet = projectRoot + File.separator + "www" + chemin;
            File fichier = new File(cheminComplet);

            if (!fichier.exists()) {
                gui.log("Client #" + clientId + " → " + chemin + " not found [404]");
                GestionErreurs.envoyerErreur404(out);
                gui.updateClient(clientId, chemin, 404);
                return;
            }

            if (!fichier.isFile()) {
                gui.log("Client #" + clientId + " → " + chemin + " is not a file [403]");
                GestionErreurs.envoyerErreur403(out);
                gui.updateClient(clientId, chemin, 403);
                return;
            }

            if (!fichier.canRead()) {
                gui.log("Client #" + clientId + " → Permission denied [403]");
                GestionErreurs.envoyerErreur403(out);
                gui.updateClient(clientId, chemin, 403);
                return;
            }

            String contentType = GestionnaireFichiers.getContentType(chemin);
            long fileSize = fichier.length();

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println("Content-Length: " + fileSize);
            out.println("Connection: close");
            out.println();
            out.flush();

            GestionnaireFichiers.envoyerFichier(fichier, outStream);

            gui.log("Client #" + clientId + " → " + chemin + " sent successfully [200]");
            gui.updateClient(clientId, chemin, 200);

        } catch (Exception e) {
            gui.log("Client #" + clientId + " → Error: " + e.getMessage() + " [500]");
            try {
                if (out != null) GestionErreurs.envoyerErreur500(out);
            } catch (Exception ignored) {}
            gui.updateClient(clientId, "", 500);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (outStream != null) outStream.close();
                if (clientSocket != null) clientSocket.close();
                gui.log("Client #" + clientId + " → Connection closed");
            } catch (IOException e) {
                gui.log("Error closing resources for client #" + clientId);
            }
        }
    }
}