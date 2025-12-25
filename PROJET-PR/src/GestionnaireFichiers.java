import java.io.*;

public class GestionnaireFichiers {
    
    /**
     * Vérifie si un fichier existe
     */
    public static boolean fichierExiste(String chemin) {
        File fichier = new File(chemin);
        return fichier.exists() && fichier.isFile();
    }
    
    /**
     * Détermine le type MIME d'un fichier selon son extension
     */
    public static String getContentType(String chemin) {
        String extension = "";
        int i = chemin.lastIndexOf('.');
        
        if (i > 0) {
            extension = chemin.substring(i).toLowerCase();
        }
        
        switch (extension) {
            case ".html":
            case ".htm":
                return "text/html; charset=UTF-8";
                
            case ".css":
                return "text/css; charset=UTF-8";
                
            case ".js":
                return "application/javascript; charset=UTF-8";
                
            case ".json":
                return "application/json; charset=UTF-8";
                
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
                
            case ".png":
                return "image/png";
                
            case ".gif":
                return "image/gif";
                
            case ".svg":
                return "image/svg+xml";
                
            case ".ico":
                return "image/x-icon";
                
            case ".txt":
                return "text/plain; charset=UTF-8";
                
            case ".pdf":
                return "application/pdf";
                
            case ".xml":
                return "application/xml; charset=UTF-8";
                
            case ".zip":
                return "application/zip";
                
            default:
                return "application/octet-stream";
        }
    }
    
    /**
     * Envoie un fichier via un OutputStream
     */
    public static void envoyerFichier(File fichier, OutputStream out) throws IOException {
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(fichier);
            byte[] buffer = new byte[4096]; // Buffer de 4KB
            int bytesLus;
            
            // Lire et envoyer le fichier par morceaux
            while ((bytesLus = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLus);
            }
            
            out.flush();
            
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Erreur fermeture fichier: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Lit le contenu d'un fichier texte
     */
    public static String lireFichierTexte(File fichier) throws IOException {
        BufferedReader reader = null;
        StringBuilder contenu = new StringBuilder();
        
        try {
            reader = new BufferedReader(new FileReader(fichier));
            String ligne;
            
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
            
            return contenu.toString();
            
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Erreur fermeture reader: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Obtient la taille d'un fichier en octets
     */
    public static long getTailleFichier(File fichier) {
        return fichier.length();
    }
    
    /**
     * Formate la taille en KB, MB, etc.
     */
    public static String formatTaille(long taille) {
        if (taille < 1024) {
            return taille + " B";
        } else if (taille < 1024 * 1024) {
            return String.format("%.2f KB", taille / 1024.0);
        } else {
            return String.format("%.2f MB", taille / (1024.0 * 1024.0));
        }
    }
}