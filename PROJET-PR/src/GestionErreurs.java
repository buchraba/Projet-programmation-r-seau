import java.io.PrintWriter;

public class GestionErreurs {
    
    /**
     * Envoie une erreur 404 - Not Found
     */
    public static void envoyerErreur404(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Connection: close");
        out.println();
        // out.println("<!DOCTYPE html>");
        // out.println("<html lang='fr'>");
        // out.println("<head>");
        // out.println("    <meta charset='UTF-8'>");
        // out.println("    <title>404 - Page Non Trouvée</title>");
        // out.println("    <style>");
        // out.println("        body { font-family: Arial, sans-serif; background: #f4f4f4; text-align: center; padding: 50px; }");
        // out.println("        .error-container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; }");
        // out.println("        h1 { color: #e74c3c; font-size: 72px; margin: 0; }");
        // out.println("        h2 { color: #333; margin: 20px 0; }");
        // out.println("        p { color: #666; line-height: 1.6; }");
        // out.println("        a { color: #3498db; text-decoration: none; }");
        // out.println("        a:hover { text-decoration: underline; }");
        // out.println("    </style>");
        // out.println("</head>");
        // out.println("<body>");
        // out.println("    <div class='error-container'>");
        // out.println("        <h1>404</h1>");
        // out.println("        <h2>Page Non Trouvée</h2>");
        // out.println("        <p>Désolé, la page que vous recherchez n'existe pas sur ce serveur.</p>");
        // out.println("        <p><a href='/'>← Retour à l'accueil</a></p>");
        // out.println("    </div>");
        // out.println("</body>");
        // out.println("</html>");
        out.flush();
    }
    
    /**
     * Envoie une erreur 500 - Internal Server Error
     */
    public static void envoyerErreur500(PrintWriter out) {
        out.println("HTTP/1.1 500 Internal Server Error");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Connection: close");
        out.println();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>500 - Erreur Serveur</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; background: #f4f4f4; text-align: center; padding: 50px; }");
        out.println("        .error-container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; }");
        out.println("        h1 { color: #e67e22; font-size: 72px; margin: 0; }");
        out.println("        h2 { color: #333; margin: 20px 0; }");
        out.println("        p { color: #666; line-height: 1.6; }");
        out.println("        a { color: #3498db; text-decoration: none; }");
        out.println("        a:hover { text-decoration: underline; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='error-container'>");
        out.println("        <h1>500</h1>");
        out.println("        <h2>Erreur Interne du Serveur</h2>");
        out.println("        <p>Une erreur s'est produite lors du traitement de votre requête.</p>");
        out.println("        <p>Veuillez réessayer plus tard.</p>");
        out.println("        <p><a href='/'>← Retour à l'accueil</a></p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }
    
    /**
     * Envoie une erreur 400 - Bad Request
     */
    public static void envoyerErreur400(PrintWriter out) {
        out.println("HTTP/1.1 400 Bad Request");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Connection: close");
        out.println();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>400 - Requête Invalide</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; background: #f4f4f4; text-align: center; padding: 50px; }");
        out.println("        .error-container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; }");
        out.println("        h1 { color: #9b59b6; font-size: 72px; margin: 0; }");
        out.println("        h2 { color: #333; margin: 20px 0; }");
        out.println("        p { color: #666; line-height: 1.6; }");
        out.println("        a { color: #3498db; text-decoration: none; }");
        out.println("        a:hover { text-decoration: underline; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='error-container'>");
        out.println("        <h1>400</h1>");
        out.println("        <h2>Requête Invalide</h2>");
        out.println("        <p>La requête envoyée au serveur est mal formée ou invalide.</p>");
        out.println("        <p><a href='/'>← Retour à l'accueil</a></p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }
    
    /**
     * Envoie une erreur 403 - Forbidden
     */
    public static void envoyerErreur403(PrintWriter out) {
        out.println("HTTP/1.1 403 Forbidden");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Connection: close");
        out.println();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='fr'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>403 - Accès Interdit</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; background: #f4f4f4; text-align: center; padding: 50px; }");
        out.println("        .error-container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; }");
        out.println("        h1 { color: #c0392b; font-size: 72px; margin: 0; }");
        out.println("        h2 { color: #333; margin: 20px 0; }");
        out.println("        p { color: #666; line-height: 1.6; }");
        out.println("        a { color: #3498db; text-decoration: none; }");
        out.println("        a:hover { text-decoration: underline; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='error-container'>");
        out.println("        <h1>403</h1>");
        out.println("        <h2>Accès Interdit</h2>");
        out.println("        <p>Vous n'avez pas la permission d'accéder à cette ressource.</p>");
        out.println("        <p><a href='/'>← Retour à l'accueil</a></p>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }
}