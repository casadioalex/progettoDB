package mcdonald.view.panels.home.staff;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class StaffHomePanel extends JPanel {

    private static final String TITLE = "ORDERS";
    private static final String LOGOUT_BUTTON_TEXT = "LOGOUT";
    private static final String REFRESH_BUTTON_TEXT = "REFRESH";

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton logoutButton;
    private final JPanel ordersPanel;
    private final List<Integer> ordersIds = new ArrayList<>();
    private final JButton viewStaffButton;
    private final JButton blockUserButton;

    public StaffHomePanel() {
        setLayout(new GridBagLayout());

        // Imposta i margini qui
        final Insets insets = new Insets(20, 20, 20, 20); // Valore fisso per i margini
        gbc = new GridBagConstraints();
        gbc.insets = insets;

        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Modificato per fare spazio al pulsante
        gbc.weightx = 1.0; // Permette al titolo di usare lo spazio extra
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridx = 0; // Reimposta gridx per la riga successiva
        gbc.gridwidth = 3; // Occupa di nuovo tutta la larghezza
        gbc.gridy++;
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        
        refreshOrdersList(); // Carica e popola la lista all'avvio

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        add(scrollPane, gbc);

        gbc.gridy++;
        gbc.gridwidth = 3;
        JButton refreshButton = new JButton(REFRESH_BUTTON_TEXT);
        add(refreshButton, gbc);
        refreshButton.addActionListener(e -> refreshOrdersList());

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        logoutButton = new JButton(LOGOUT_BUTTON_TEXT);
        add(logoutButton, gbc);
        logoutButton.addActionListener(e -> {
            Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
            Window.setUserEmail(null);
            window.switchMainPanel(MainPanels.LOGIN);
        });

        // Crea i pulsanti admin e aggiungili al pannello, ma nascondili
        gbc.gridx++;
        viewStaffButton = new JButton("VIEW STAFF");
        add(viewStaffButton, gbc);
        viewStaffButton.addActionListener(e -> apriStaffMenu());

        gbc.gridx++;
        blockUserButton = new JButton("VIEW USERS");
        add(blockUserButton, gbc);
        blockUserButton.addActionListener(e -> apriBlockUsers());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        String role = getUserRole();
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        viewStaffButton.setVisible(isAdmin);
        blockUserButton.setVisible(isAdmin);
        refreshOrdersList();
    }

    private void apriOrdine(int orderid) {
        Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
        Window.setOrderId(orderid);
        window.switchMainPanel(MainPanels.ORDER_DETAILS);
    }

    private void apriStaffMenu() {
        Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
        window.switchMainPanel(MainPanels.STAFF_MENU);
    }

    private void apriBlockUsers() {
        Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
        window.switchMainPanel(MainPanels.BLOCK_USERS);
    }

    private void populateOrdersPanel() {
        ordersPanel.removeAll();
        for (int orderid : ordersIds) {
            JButton orderButton = new JButton(String.format("ORDER: #%04d", orderid));
            orderButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            orderButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, orderButton.getPreferredSize().height));
            orderButton.addActionListener(e -> apriOrdine(orderid));
            ordersPanel.add(orderButton);
        }
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void refreshOrdersList() {
        ordersIds.clear();
        getUncompletedOrdersIds();
        populateOrdersPanel();
    }

    private void getUncompletedOrdersIds() {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("GET_UNCOMPLETED_ORDERS");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int order_id = rs.getInt("order_id");
                    ordersIds.add(order_id);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private String getUserRole() {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        String role = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
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
