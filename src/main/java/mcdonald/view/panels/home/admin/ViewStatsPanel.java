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
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class ViewStatsPanel extends JPanel {
    private static final String TITLE = "STATS MENU";
    private static final String BACK_BUTTON_TEXT = "BACK TO HOME";

    private static final String VIEW_BEST_PRODUCT_LAST_WEEK = "BEST PRODUCT (LAST WEEK)";
    private static final String VIEW_BEST_PRODUCT_ALL_TIME = "BEST PRODUCT (ALL TIME)";
    private static final String VIEW_ORDERS_ABOVE_15_PERCENT = "ORDERS ABOVE 15\u20ac (%)";

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    // Connessione al DB - da centralizzare in una classe dedicata se giÃ  presente nel progetto
    private static final String DB_NAME = "mcdonald";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton backButton;
    private final JPanel statsPanel;
    private final JButton vlwpoButton;
    private final JButton vwol3dButton;
    private final JButton vcwmsButton;

    public ViewStatsPanel() {
        setLayout(new GridBagLayout());

        final Insets insets = new Insets(20, 20, 20, 20);
        gbc = new GridBagConstraints();
        gbc.insets = insets;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy++;
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(statsPanel);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridy++;
        vlwpoButton = new JButton(VIEW_BEST_PRODUCT_LAST_WEEK);
        add(vlwpoButton, gbc);
        vlwpoButton.addActionListener(e -> showBestProductLastWeek());

        gbc.gridx++;
        vwol3dButton = new JButton(VIEW_BEST_PRODUCT_ALL_TIME);
        add(vwol3dButton, gbc);
        vwol3dButton.addActionListener(e -> showBestProductAllTime());

        gbc.gridx++;
        vcwmsButton = new JButton(VIEW_ORDERS_ABOVE_15_PERCENT);
        add(vcwmsButton, gbc);
        vcwmsButton.addActionListener(e -> showOrdersAbove15Percent());

        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy++;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);
        backButton.addActionListener(e -> {
            Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.STAFF_HOME);
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        String role = getUserRole();
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        vlwpoButton.setVisible(isAdmin);
        vwol3dButton.setVisible(isAdmin);
        vcwmsButton.setVisible(isAdmin);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void addStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        statsPanel.add(label);
    }

    private void refreshStatsPanel() {
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    // VIEW_BEST_PRODUCT_LAST_WEEK
    private void showBestProductLastWeek() {
        statsPanel.removeAll();

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(QueryLoader.loadQuery("GET_BEST_PRODUCT_LAST_WEEK"));
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("total_quantity_sold");
                double earnings = rs.getDouble("total_earnings");

                addStatLabel("Prodotto: " + name);
                addStatLabel(String.format("Prezzo unitario: %.2f \u20ac", price));
                addStatLabel("Quantita' venduta: " + quantity);
                addStatLabel(String.format("Guadagno totale: %.2f \u20ac", earnings));
            } 
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            addStatLabel("Errore nel recupero dei dati.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            addStatLabel("Errore nel caricamento della query.");
        }

        refreshStatsPanel();
    }

    // VIEW_BEST_PRODUCT_ALL_TIME
    private void showBestProductAllTime() {
        statsPanel.removeAll();

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(QueryLoader.loadQuery("GET_BEST_PRODUCT_ALL_TIME"));
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("total_quantity_sold");
                double earnings = rs.getDouble("total_earnings");

                addStatLabel("Prodotto: " + name);
                addStatLabel(String.format("Prezzo unitario: %.2f \u20ac", price));
                addStatLabel("Quantita' venduta: " + quantity);
                addStatLabel(String.format("Guadagno totale: %.2f \u20ac", earnings));
            } 
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            addStatLabel("Errore nel recupero dei dati.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            addStatLabel("Errore nel caricamento della query.");
        }

        refreshStatsPanel();
    }

    // VIEW_ORDERS_ABOVE_15_PERCENT
    private void showOrdersAbove15Percent() {
        statsPanel.removeAll();

        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(QueryLoader.loadQuery("GET_ORDERS_ABOVE_15_PERCENT"));
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int above = rs.getInt("orders_above_15");
                int total = rs.getInt("total_orders");
                double percent = rs.getDouble("percentage");

                addStatLabel("Ordini sopra i 15\u20ac: " + above + " su " + total);
                addStatLabel(String.format("Percentuale: %.2f%%", percent));
            } 
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            addStatLabel("Errore nel recupero dei dati.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            addStatLabel("Errore nel caricamento della query.");
        }

        refreshStatsPanel();
    }

    private String getUserRole() {
        String role = "";

        try (Connection conn = getConnection()) {
            String query = QueryLoader.loadQuery("GET_USER_ROLE");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, Window.getUserEmail().orElse(""));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return role;
    }
}