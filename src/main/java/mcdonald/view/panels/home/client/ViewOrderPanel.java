package mcdonald.view.panels.home.client;

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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class ViewOrderPanel extends JPanel {

    private static final String TITLE = "ORDER DETAILS";
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 14);

    private final GridBagConstraints gbc = new GridBagConstraints();

    private final JPanel orderDetailsPanel;
    private final JScrollPane orderDetailsScrollPane;
    private final JLabel titleLabel;
    private final JButton backButton;

    public ViewOrderPanel() {
        setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        orderDetailsPanel = new JPanel(new GridBagLayout());
        orderDetailsScrollPane = new JScrollPane(orderDetailsPanel);
        orderDetailsScrollPane.setPreferredSize(new Dimension(500, 300));
        add(orderDetailsScrollPane, gbc);

        gbc.gridy++;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.CLIENT_HOME);
        });
        add(backButton, gbc);
    }

    public void loadOrder() {
        int orderId = ClientHomePanel.getSelectedOrderId()
                .orElseThrow(() -> new IllegalStateException("Cannot view order details without a selected order."));

        titleLabel.setText("ORDER #" + orderId);
        orderDetailsPanel.removeAll();
        
        GridBagConstraints detailsGbc = new GridBagConstraints();
        detailsGbc.insets = new Insets(5, 5, 5, 5);
        detailsGbc.fill = GridBagConstraints.HORIZONTAL;
        detailsGbc.weightx = 1.0;

        detailsGbc.gridy = 0;
        
        detailsGbc.gridx = 0;
        JLabel productHeader = new JLabel("Product");
        productHeader.setFont(HEADER_FONT);
        orderDetailsPanel.add(productHeader, detailsGbc);

        detailsGbc.gridx = 1;
        JLabel quantityHeader = new JLabel("Quantity");
        quantityHeader.setFont(HEADER_FONT);
        quantityHeader.setHorizontalAlignment(SwingConstants.CENTER);
        orderDetailsPanel.add(quantityHeader, detailsGbc);

        String url = "jdbc:mysql://localhost:3306/mcdonald";
        String dbEmail = "root";
        String dbPassword = "";
        int row = 1;

        try (Connection conn = DriverManager.getConnection(url, dbEmail, dbPassword)) {
            String query = QueryLoader.loadQuery("GET_ORDER_DETAILS");
            
            try(PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, orderId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String productName = rs.getString("product_name");
                    int quantity = rs.getInt("quantity");

                    detailsGbc.gridy = row;
                    detailsGbc.gridx = 0;
                    JLabel productLabel = new JLabel(productName);
                    productLabel.setFont(CONTENT_FONT);
                    orderDetailsPanel.add(productLabel, detailsGbc);

                    detailsGbc.gridx = 1;
                    JLabel quantityLabel = new JLabel(String.valueOf(quantity));
                    quantityLabel.setFont(CONTENT_FONT);
                    quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderDetailsPanel.add(quantityLabel, detailsGbc);

                    row++;
                }

            }

        } catch (SQLException e) {
            System.err.println("Error loading order details: " + e.getMessage());
            orderDetailsPanel.removeAll();
            orderDetailsPanel.add(new JLabel("Error loading order details."));
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            orderDetailsPanel.removeAll();
            orderDetailsPanel.add(new JLabel("Unexpected error occurred."));
        }
        
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridy = row + 1;
        filler.weighty = 1.0;
        orderDetailsPanel.add(new JPanel(), filler);

        orderDetailsPanel.revalidate();
        orderDetailsPanel.repaint();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        loadOrder();
    }

}
