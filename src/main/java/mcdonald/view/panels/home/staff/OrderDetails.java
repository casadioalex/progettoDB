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
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mcdonald.model.common.QueryLoader;

public class OrderDetails extends JPanel {
    private static final String TITLE = "ORDER DETAILS";
    private static final String BACK_BUTTON_TEXT = "BACK TO HOME";
    private static final String COMPLETE_ORDER_BUTTON_TEXT = "COMPLETE ORDER";

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton backButton;
    private final JButton completeOrderButton;
    private final JTable orderTable;
    private final DefaultTableModel tableModel;

    public OrderDetails(int orderId) {
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
        gbc.weightx = 1;
        gbc.weighty = 1;
        tableModel = new DefaultTableModel(new Object[] { "Product Name", "Quantity" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        add(scrollPane, gbc);

        getOrderDetailsById(orderId);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridx++;
        completeOrderButton = new JButton(COMPLETE_ORDER_BUTTON_TEXT);
        add(completeOrderButton, gbc);
        completeOrderButton.addActionListener(e -> completeOrder(orderId));
    }

    private void getOrderDetailsById(int orderId) {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("GET_ORDER_DETAILS_BY_ORDER_ID");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, orderId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String productName = rs.getString("product_name");
                        int quantity = rs.getInt("quantity");
                        tableModel.addRow(new Object[] { productName, quantity });
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void completeOrder(int orderId) {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("COMPLETE_ORDER");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, orderId);
                int updated = stmt.executeUpdate();
                if (updated > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Ordine completato!");
                    completeOrderButton.setEnabled(false);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Errore: ordine non trovato.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
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
