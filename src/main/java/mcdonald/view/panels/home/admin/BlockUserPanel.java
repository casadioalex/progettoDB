package mcdonald.view.panels.home.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.view.main.Window;

public class BlockUserPanel extends JPanel {
    private static final String TITLE = "USERS LIST";
    private static final String BACK_BUTTON_TEXT = "BACK TO HOME";

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton backButton;
    private final JPanel clientPanel;

    public BlockUserPanel() {
        setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        clientPanel = new JPanel();
        clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
        List<String> clients = getAllUsers();
        for (String client : clients) {
            JButton clientButton = new JButton(client);
            clientButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            clientButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, clientButton.getPreferredSize().height));
            clientButton.addActionListener(e -> {
                String email = client.substring(client.indexOf('(') + 1, client.indexOf(')'));
                boolean isBlocked = client.contains("[BLOCKED]");
                String action = isBlocked ? "sbloccare" : "bloccare";
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "Vuoi davvero " + action + " l'utente " + email + "?",
                        (isBlocked ? "Sblocca" : "Blocca") + " utente",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    if (isBlocked) {
                        unblockUser(email);
                    } else {
                        blockUser(email);
                    }
                    refreshUserList();
                }
            });
            clientPanel.add(clientButton);
        }
        JScrollPane scrollPane = new JScrollPane(clientPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 0.5;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);
        backButton.addActionListener(e -> {
            Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.STAFF_HOME);
        });
    }

    private List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = mcdonald.model.common.QueryLoader.loadQuery("GET_ALL_USERS");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String username = rs.getString("username");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String email = rs.getString("email");
                    String role = rs.getString("role");
                    boolean blocked = rs.getBoolean("blocked");
                    users.add(username + " - " + name + " " + surname + " (" + email + ") [" + role + "]"
                            + (blocked ? " [BLOCKED]" : ""));
                }
            }
        } catch (Exception e) {
            users.add("Errore caricamento utenti: " + e.getMessage());
        }
        return users;
    }

    private void blockUser(String email) {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = mcdonald.model.common.QueryLoader.loadQuery("BLOCK_USER");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore nel blocco utente: " + e.getMessage());
        }
    }

    private void unblockUser(String email) {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = "UNBLOCK_USER";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore nello sblocco utente: " + e.getMessage());
        }
    }

    private void refreshUserList() {
        clientPanel.removeAll();
        List<String> clients = getAllUsers();
        for (String client : clients) {
            JButton clientButton = new JButton(client);
            clientButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            clientButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, clientButton.getPreferredSize().height));
            clientButton.addActionListener(e -> {
                String email = client.substring(client.indexOf('(') + 1, client.indexOf(')'));
                boolean isBlocked = client.contains("[BLOCKED]");
                String action = isBlocked ? "sbloccare" : "bloccare";
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "Vuoi davvero " + action + " l'utente " + email + "?",
                        (isBlocked ? "Sblocca" : "Blocca") + " utente",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    if (isBlocked) {
                        unblockUser(email);
                    } else {
                        blockUser(email);
                    }
                    refreshUserList();
                }
            });
            clientPanel.add(clientButton);
        }
        clientPanel.revalidate();
        clientPanel.repaint();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        final Dimension size = getPreferredSize();
        final var width = size.getWidth();
        final var height = size.getHeight();

        Insets insets = new Insets((int) (height * HEIGHT_INSET_PROPORTION), (int) (width * WIDTH_INSET_PROPORTION),
                (int) (height * HEIGHT_INSET_PROPORTION), (int) (width * WIDTH_INSET_PROPORTION));

        GridBagLayout layout = (GridBagLayout) getLayout();
        Arrays.stream(getComponents()).forEach(component -> {
            gbc = layout.getConstraints(component);
            gbc.insets = insets;
            layout.setConstraints(component, gbc);
        });

        revalidate();
        repaint();
    }
}
