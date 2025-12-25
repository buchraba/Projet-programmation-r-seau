import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerGUI extends JFrame {
    private static final int PORT = 8080;

    private JTextArea logArea;
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JButton startButton;
    private JButton stopButton;

    private ServerSocket serverSocket;
    private boolean running = false;
    private int clientCount = 0;
    private Thread serverThread;

    public ServerGUI() {
        setTitle("Minimal Web Server - Dashboard");
        setSize(960, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(30, 30, 30));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 40, 40));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        statusLabel = new JLabel("Server Status: Stopped");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statusLabel.setForeground(Color.RED);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(40, 40, 40));

        startButton = new JButton("Start Server");
        styleButton(startButton, new Color(0, 150, 0), Color.WHITE);
        startButton.addActionListener(e -> startServer());

        stopButton = new JButton("Stop Server");
        styleButton(stopButton, new Color(200, 0, 0), Color.WHITE);
        stopButton.addActionListener(e -> stopServer());
        stopButton.setEnabled(false);

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        topPanel.add(statusLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Client Table
        String[] columns = {"Client ID", "IP Address", "Requested File", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientTable = new JTable(tableModel);
        clientTable.setRowHeight(30);
        clientTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clientTable.getTableHeader().setBackground(new Color(50, 50, 50));
        clientTable.getTableHeader().setForeground(Color.WHITE);
        clientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        clientTable.setBackground(new Color(45, 45, 45));
        clientTable.setForeground(Color.WHITE);
        clientTable.setGridColor(new Color(70, 70, 70));
        clientTable.setSelectionBackground(new Color(60, 100, 180));
        clientTable.setSelectionForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(clientTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)), " Active Clients ",
                0, 0, new Font("Segoe UI", Font.BOLD, 15), Color.LIGHT_GRAY));

        // Log Area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 20));
        logArea.setForeground(Color.CYAN);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setMargin(new Insets(10, 10, 10, 10));
        logArea.append("=== Minimal Web Server Dashboard ===\n");
        logArea.append("Ready to start on port " + PORT + "\n\n");

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)), " Server Logs ",
                0, 0, new Font("Segoe UI", Font.BOLD, 15), Color.LIGHT_GRAY));

        // Assemble
        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void startServer() {
        if (running) return;
        running = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        statusLabel.setText("Server Running on port " + PORT);
        statusLabel.setForeground(new Color(0, 200, 0));

        log("Server started at " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    clientCount++;
                    String clientIP = clientSocket.getInetAddress().getHostAddress();

                    log("New client connected → Client #" + clientCount + " from " + clientIP);

                    SwingUtilities.invokeLater(() ->
                            tableModel.addRow(new Object[]{clientCount, clientIP, "Processing...", "-"}));

                    ClientHandler handler = new ClientHandler(clientSocket, clientCount, this);
                    new Thread(handler).start();
                }
            } catch (IOException e) {
                if (running) log("ERROR starting server: " + e.getMessage());
            }
        });
        serverThread.start();
    }

    private void stopServer() {
        running = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        statusLabel.setText("Server Stopped");
        statusLabel.setForeground(Color.RED);
        log("Server stopped by user.");

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log("Error closing server socket: " + e.getMessage());
        }
    }

    public void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        SwingUtilities.invokeLater(() ->
                logArea.append("[" + timestamp + "] " + message + "\n"));
    }

    public void updateClient(int id, String path, int status) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    tableModel.setValueAt(path.isEmpty() ? "/" : path, i, 2);
                    tableModel.setValueAt(status == 0 ? "-" : status, i, 3);

                    // Color status
                    Color statusColor = switch (status) {
                        case 200 -> Color.GREEN;
                        case 404 -> Color.ORANGE;
                        case 403, 400 -> Color.RED;
                        case 500 -> Color.MAGENTA;
                        default -> Color.WHITE;
                    };
                    clientTable.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JLabel label = new JLabel(value.toString());
                        label.setForeground(statusColor);
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        return label;
                    });
                    break;
                }
            }
            clientTable.repaint();
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new ServerGUI());
    }
}