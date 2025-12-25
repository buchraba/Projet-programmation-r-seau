import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.io.*;

public class TestCompletGUI extends JFrame {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static int testsPasses = 0;
    private static int testsEchoues = 0;
    
    private JTextArea outputArea;
    private JButton test1Btn, test2Btn, test3Btn, test4Btn, test5Btn, runAllBtn, clearBtn;
    private JPanel buttonPanel, mainPanel;
    
    public TestCompletGUI() {
        setTitle("Suite de Tests - Serveur Web Minimal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Titre
        JLabel titleLabel = new JLabel("SUITE DE TESTS - SERVEUR WEB MINIMAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Zone de texte pour les résultats
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel des boutons
        buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Création des boutons
        test1Btn = createStyledButton("Test 1: index.html (200 OK)", new Color(52, 152, 219));
        test2Btn = createStyledButton("Test 2: Fichier inexistant (404)", new Color(46, 204, 113));
        test3Btn = createStyledButton("Test 3: 5 Clients simultanés", new Color(155, 89, 182));
        test4Btn = createStyledButton("Test 4: Demander une image", new Color(241, 196, 15));
        test5Btn = createStyledButton("Test 5: Instructions navigateur", new Color(230, 126, 34));
        runAllBtn = createStyledButton("▶ Exécuter TOUS les tests", new Color(231, 76, 60));
        clearBtn = createStyledButton("Effacer les résultats", new Color(149, 165, 166));
        
        // Ajout des listeners
        test1Btn.addActionListener(e -> runTest(1));
        test2Btn.addActionListener(e -> runTest(2));
        test3Btn.addActionListener(e -> runTest(3));
        test4Btn.addActionListener(e -> runTest(4));
        test5Btn.addActionListener(e -> runTest(5));
        runAllBtn.addActionListener(e -> runAllTests());
        clearBtn.addActionListener(e -> clearOutput());
        
        // Ajout des boutons au panel
        buttonPanel.add(test1Btn);
        buttonPanel.add(test2Btn);
        buttonPanel.add(test3Btn);
        buttonPanel.add(test4Btn);
        buttonPanel.add(test5Btn);
        buttonPanel.add(new JLabel("")); // Espace vide
        buttonPanel.add(runAllBtn);
        buttonPanel.add(clearBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Message de bienvenue
        appendOutput("╔════════════════════════════════════════════════════════╗\n");
        appendOutput("║          SUITE DE TESTS - SERVEUR WEB MINIMAL         ║\n");
        appendOutput("╚════════════════════════════════════════════════════════╝\n\n");
        appendOutput("Sélectionnez un test à exécuter ou cliquez sur 'Exécuter TOUS les tests'\n");
        appendOutput("Serveur cible: " + HOST + ":" + PORT + "\n\n");
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void appendOutput(String text) {
        outputArea.append(text);
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    
    private void clearOutput() {
        outputArea.setText("");
        testsPasses = 0;
        testsEchoues = 0;
    }
    
    private void runTest(int testNumber) {
        disableButtons();
        
        new Thread(() -> {
            switch (testNumber) {
                case 1:
                    test1_IndexHtml();
                    break;
                case 2:
                    test2_FichierInexistant();
                    break;
                case 3:
                    test3_ClientsSimultanes();
                    break;
                case 4:
                    test4_DemanderImage();
                    break;
                case 5:
                    test5_TestNavigateur();
                    break;
            }
            
            SwingUtilities.invokeLater(() -> enableButtons());
        }).start();
    }
    
    private void runAllTests() {
        disableButtons();
        clearOutput();
        
        new Thread(() -> {
            appendOutput("╔════════════════════════════════════════════════════════╗\n");
            appendOutput("║          EXÉCUTION DE TOUS LES TESTS                   ║\n");
            appendOutput("╚════════════════════════════════════════════════════════╝\n\n");
            
            test1_IndexHtml();
            pause(500);
            test2_FichierInexistant();
            pause(500);
            test3_ClientsSimultanes();
            pause(500);
            test4_DemanderImage();
            pause(500);
            test5_TestNavigateur();
            
            afficherResume();
            
            SwingUtilities.invokeLater(() -> enableButtons());
        }).start();
    }
    
    private void disableButtons() {
        test1Btn.setEnabled(false);
        test2Btn.setEnabled(false);
        test3Btn.setEnabled(false);
        test4Btn.setEnabled(false);
        test5Btn.setEnabled(false);
        runAllBtn.setEnabled(false);
    }
    
    private void enableButtons() {
        test1Btn.setEnabled(true);
        test2Btn.setEnabled(true);
        test3Btn.setEnabled(true);
        test4Btn.setEnabled(true);
        test5Btn.setEnabled(true);
        runAllBtn.setEnabled(true);
    }
    
    // TEST 1: Demander index.html → 200 OK
    public void test1_IndexHtml() {
        appendOutput("\n╔═══════════════════════════════════════════════════╗\n");
        appendOutput("║  TEST 1: Demander index.html → doit retourner 200 OK  ║\n");
        appendOutput("╚═══════════════════════════════════════════════════╝\n");
        
        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out.println("GET /index.html HTTP/1.1");
            out.println("Host: " + HOST);
            out.println();
            
            String statusLine = in.readLine();
            appendOutput("→ Requête: GET /index.html\n");
            appendOutput("← Réponse: " + statusLine + "\n");
            
            if (statusLine != null && statusLine.contains("200 OK")) {
                appendOutput("✓ TEST RÉUSSI: Code 200 OK reçu\n\n");
                testsPasses++;
            } else {
                appendOutput("✗ TEST ÉCHOUÉ: Code attendu 200, reçu: " + statusLine + "\n\n");
                testsEchoues++;
            }
            
            socket.close();
            
        } catch (IOException e) {
            appendOutput("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n\n");
            testsEchoues++;
        }
    }
    
    // TEST 2: Demander fichier inexistant → 404
    public void test2_FichierInexistant() {
        appendOutput("\n╔═══════════════════════════════════════════════════╗\n");
        appendOutput("║  TEST 2: Demander fichier_inexistant.html → 404      ║\n");
        appendOutput("╚═══════════════════════════════════════════════════╝\n");
        
        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out.println("GET /fichier_inexistant.html HTTP/1.1");
            out.println("Host: " + HOST);
            out.println();
            
            String statusLine = in.readLine();
            appendOutput("→ Requête: GET /fichier_inexistant.html\n");
            appendOutput("← Réponse: " + statusLine + "\n");
            
            if (statusLine != null && statusLine.contains("404")) {
                appendOutput("✓ TEST RÉUSSI: Code 404 Not Found reçu\n\n");
                testsPasses++;
            } else {
                appendOutput("✗ TEST ÉCHOUÉ: Code attendu 404, reçu: " + statusLine + "\n\n");
                testsEchoues++;
            }
            
            socket.close();
            
        } catch (IOException e) {
            appendOutput("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n\n");
            testsEchoues++;
        }
    }
    
    // TEST 3: Lancer 5 clients en même temps
    public void test3_ClientsSimultanes() {
        appendOutput("\n╔═══════════════════════════════════════════════════╗\n");
        appendOutput("║  TEST 3: Lancer 5 clients en même temps              ║\n");
        appendOutput("╚═══════════════════════════════════════════════════╝\n");
        
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
                    
                    // Use SwingUtilities to safely update GUI from thread
                    final String response = statusLine;
                    SwingUtilities.invokeLater(() -> {
                        appendOutput("  Client #" + clientNum + " → Réponse: " + response + "\n");
                    });
                    
                    if (statusLine != null && statusLine.contains("200")) {
                        synchronized (reussites) {
                            reussites[0]++;
                        }
                    }
                    
                    socket.close();
                    
                } catch (IOException e) {
                    final String errorMsg = e.getMessage();
                    SwingUtilities.invokeLater(() -> {
                        appendOutput("  Client #" + clientNum + " → Erreur: " + errorMsg + "\n");
                    });
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
        
        appendOutput("\nRésultat: " + reussites[0] + "/5 clients ont reçu leur fichier\n");
        
        if (reussites[0] == 5) {
            appendOutput("✓ TEST RÉUSSI: Tous les clients ont été servis\n\n");
            testsPasses++;
        } else {
            appendOutput("✗ TEST ÉCHOUÉ: Seulement " + reussites[0] + "/5 clients servis\n\n");
            testsEchoues++;
        }
    }
    
    // TEST 4: Demander une image
    public void test4_DemanderImage() {
        appendOutput("\n╔═══════════════════════════════════════════════════╗\n");
        appendOutput("║  TEST 4: Demander une image                          ║\n");
        appendOutput("╚═══════════════════════════════════════════════════╝\n");
        
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
                    
                    String ligne;
                    while ((ligne = in.readLine()) != null && !ligne.isEmpty()) {
                        if (ligne.startsWith("Content-Type:")) {
                            contentType = ligne;
                        }
                    }
                    
                    appendOutput("→ Requête: GET " + imagePath + "\n");
                    appendOutput("← Réponse: " + statusLine + "\n");
                    appendOutput("← " + contentType + "\n");
                    
                    if (statusLine != null && statusLine.contains("200") && 
                        contentType.contains("image/")) {
                        appendOutput("✓ TEST RÉUSSI: Image servie avec bon Content-Type\n\n");
                        testsPasses++;
                        imageTrouvee = true;
                    } else {
                        appendOutput("✗ TEST ÉCHOUÉ: Problème avec l'image\n\n");
                        testsEchoues++;
                        imageTrouvee = true;
                    }
                    
                    socket.close();
                    break;
                    
                } catch (IOException e) {
                    appendOutput("✗ TEST ÉCHOUÉ: " + e.getMessage() + "\n\n");
                    testsEchoues++;
                    imageTrouvee = true;
                    break;
                }
            }
        }
        
        if (!imageTrouvee) {
            appendOutput("⚠ ATTENTION: Aucune image trouvée dans le dossier www/\n");
            appendOutput("  Ajoutez logo.png, image.jpg ou photo.gif pour tester\n");
            appendOutput("  TEST IGNORÉ\n\n");
        }
    }
    
    // TEST 5: Instructions pour test navigateur
    public void test5_TestNavigateur() {
        appendOutput("\n╔═══════════════════════════════════════════════════╗\n");
        appendOutput("║  TEST 5: Tester avec un navigateur web               ║\n");
        appendOutput("╚═══════════════════════════════════════════════════╝\n");
        appendOutput("\n📌 INSTRUCTIONS MANUELLES:\n");
        appendOutput("   1. Ouvrez Chrome ou Firefox\n");
        appendOutput("   2. Allez sur: http://localhost:8080\n");
        appendOutput("   3. Vérifiez que la page s'affiche correctement\n");
        appendOutput("   4. Testez aussi: http://localhost:8080/pg2.html\n");
        appendOutput("   5. Testez un 404: http://localhost:8080/inexistant.html\n");
        appendOutput("\n   → Prenez des CAPTURES D'ÉCRAN pour le rapport!\n\n");
    }
    
    // Afficher le résumé
    public void afficherResume() {
        appendOutput("\n╔════════════════════════════════════════════════════════╗\n");
        appendOutput("║                    RÉSUMÉ DES TESTS                    ║\n");
        appendOutput("╚════════════════════════════════════════════════════════╝\n");
        appendOutput("\n  Tests automatiques réussis: " + testsPasses + "\n");
        appendOutput("  Tests automatiques échoués: " + testsEchoues + "\n");
        appendOutput("  Tests manuels (navigateur): À faire\n\n");
        
        if (testsEchoues == 0) {
            appendOutput("  ✓ TOUS LES TESTS AUTOMATIQUES SONT PASSÉS!\n");
        } else {
            appendOutput("  ✗ Certains tests ont échoué. Vérifiez le serveur.\n");
        }
        
        appendOutput("\n╔════════════════════════════════════════════════════════╗\n");
        appendOutput("║              N'OUBLIEZ PAS POUR LE RAPPORT:            ║\n");
        appendOutput("╠════════════════════════════════════════════════════════╣\n");
        appendOutput("║  1. Capture: Serveur démarré                           ║\n");
        appendOutput("║  2. Capture: Test index.html (200 OK)                  ║\n");
        appendOutput("║  3. Capture: Test 404                                  ║\n");
        appendOutput("║  4. Capture: 5 clients simultanés (logs serveur)       ║\n");
        appendOutput("║  5. Capture: Page dans Chrome/Firefox                  ║\n");
        appendOutput("╚════════════════════════════════════════════════════════╝\n\n");
    }
    
    // Pause entre tests
    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestCompletGUI gui = new TestCompletGUI();
            gui.setVisible(true);
        });
    }
}