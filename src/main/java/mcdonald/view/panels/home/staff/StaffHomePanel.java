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
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mcdonald.model.common.QueryLoader;

public class StaffHomePanel extends JPanel {

    private static final String TITLE = "STAFF HOME";
    private static final String LOGOUT_BUTTON_TEXT = "LOGOUT";

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton logoutButton;
    private final JPanel ordersPanel;
    private final List<Integer> ordersIds = new ArrayList<>();

    public StaffHomePanel(String userEmail) {
        setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridy++;
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        getUncompletedOrdersIds();
        for (int orderid : ordersIds) {
            JButton orderButton = new JButton(String.format("ORDER: #%04d", orderid));
            orderButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            orderButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, orderButton.getPreferredSize().height));
            orderButton.addActionListener(e -> apriOrdine(orderid));
            ordersPanel.add(orderButton);
        }

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        add(scrollPane, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        logoutButton = new JButton(LOGOUT_BUTTON_TEXT);
        add(logoutButton, gbc);

        String role = getUserRole(userEmail);
        if ("ADMIN".equalsIgnoreCase(role)) {
            gbc.gridx++;
            JButton viewStaffButton = new JButton("VIEW STAFF");
            add(viewStaffButton, gbc);
            viewStaffButton.addActionListener(e -> apriStaffMenu());
            
        }
    }

    private void apriOrdine(int orderid) {
        //TODO: spostarsi a order details dando come parametro order per la query
        JOptionPane.showMessageDialog(this, "Hai aperto: " + orderid);
    }

    private void apriStaffMenu() {
        //TODO: spostarsi staff menu
        JOptionPane.showMessageDialog(this, "Hai aperto: Staff Menu" );
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

    private String getUserRole(String email) {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        String role = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("GET_USER_ROLE");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
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
