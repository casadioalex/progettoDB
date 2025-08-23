package mcdonald.view.panels.home.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class CreateOrderPanel extends JPanel {

    private static final String TITLE = "CREATE NEW ORDER";
    private static final String BACK_BUTTON_TEXT = "RETURN BACK";
    private static final String CONFIRM_ORDER_BUTTON_TEXT = "CONFIRM ORDER";

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    private final GridBagConstraints gbc = new GridBagConstraints();

    private final JPanel productsPanel;
    private final JScrollPane productsScrollPane;
    private final JPanel orderElementsPanel;
    private final JScrollPane orderElementsScrollPane;
    private final JButton backButton;
    private final JButton confirmOrderButton;
    private final JLabel totalLabel;

    private final Map<String, Integer> currentOrder = new LinkedHashMap<>();
    private final Map<String, Double> productPrices = new LinkedHashMap<>();
    private double total = 0.0;
    private boolean productsLoaded = false;

    private static Optional<String> selectedProductName = Optional.empty();

    public CreateOrderPanel() {
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
        gbc.gridwidth = 1;
        productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsScrollPane = new JScrollPane(productsPanel);
        productsScrollPane.setPreferredSize(new Dimension(250, 300));
        add(productsScrollPane, gbc);

        gbc.gridx++;
        orderElementsPanel = new JPanel();
        orderElementsPanel.setLayout(new BoxLayout(orderElementsPanel, BoxLayout.Y_AXIS));
        orderElementsScrollPane = new JScrollPane(orderElementsPanel);
        orderElementsScrollPane.setPreferredSize(new Dimension(250, 300));
        add(orderElementsScrollPane, gbc);

        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        totalLabel = new JLabel("Total: 0.00€");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(JLabel.CENTER);
        add(totalLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);
        gbc.gridx++;
        confirmOrderButton = new JButton(CONFIRM_ORDER_BUTTON_TEXT);
        add(confirmOrderButton, gbc);

        connectButtonsAction();
    }

    private void connectButtonsAction() {
        backButton.addActionListener(e -> {
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.CLIENT_HOME);
        });

        confirmOrderButton.addActionListener(e -> tryToCreateOrder());
    }

    private void tryToCreateOrder() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Window window = (Window) SwingUtilities.getWindowAncestor(this);
        String userEmail = window.getUserEmail().orElseThrow(() -> new IllegalStateException("User email not set"));

        String url = "jdbc:mysql://localhost:3306/mcdonald";
        String db_user = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_user, db_password)) {
            String getUserAddressQuery = QueryLoader.loadQuery("GET_USER_ADDRESS_FOR_ORDER");
            try (PreparedStatement stmt = conn.prepareStatement(getUserAddressQuery)) {
                stmt.setString(1, userEmail);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String street = rs.getString("street");
                    String number = rs.getString("number");
                    String city = rs.getString("city");

                    createOrder(conn, userEmail, street, number, city);
                } else {
                    JOptionPane.showMessageDialog(this, "User address not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private void createOrder(Connection conn, String userEmail, String street, String number, String city) throws Exception {
        String createOrderQuery = QueryLoader.loadQuery("CREATE_ORDER");
        String createOrderDetailsQuery = QueryLoader.loadQuery("CREATE_ORDER_DETAILS");
        long orderId = -1;

        try (PreparedStatement stmt = conn.prepareStatement(createOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userEmail);
            stmt.setString(2, street);
            stmt.setString(3, number);
            stmt.setString(4, city);
            stmt.setDouble(5, this.total);
            
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            throw e;
        }

        if (orderId == -1) {
            System.err.println("Could not retrieve order ID.");
            return;
        }

        try (PreparedStatement stmt = conn.prepareStatement(createOrderDetailsQuery)) {
            for (Map.Entry<String, Integer> entry : currentOrder.entrySet()) {
                String productName = entry.getKey();
                Integer quantity = entry.getValue();

                stmt.setLong(1, orderId);
                stmt.setString(2, productName);
                stmt.setInt(3, quantity);
                stmt.addBatch();
            }
            stmt.executeBatch();

            JOptionPane.showMessageDialog(this, "Order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            currentOrder.clear();
            updateOrderPanel();
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.CLIENT_HOME);

        } catch (SQLException e) {
            System.err.println("Error creating order details: " + e.getMessage());
        }
    }

    private void loadProducts() {
        productsPanel.removeAll();
        productPrices.clear();
        getProductDataFromDB();

        productPrices.forEach((productName, price) -> {
            QuantitySelectorPanel selector = new QuantitySelectorPanel(productName, price);
            
            selector.getAddButton().addActionListener(e -> {
                addProductToOrder(selector.getProductName(), selector.getQuantity());
            });

            selector.getProductButton().addActionListener(e -> {
                Window window = (Window) SwingUtilities.getWindowAncestor(this);
                selectedProductName = Optional.of(selector.getProductName());
                window.switchMainPanel(MainPanels.VIEW_PRODUCT_INFO);
            });

            productsPanel.add(selector);
            selector.setMaximumSize(new Dimension(Integer.MAX_VALUE, selector.getPreferredSize().height));
        });

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void addProductToOrder(String productName, int quantity) {
        currentOrder.put(productName, currentOrder.getOrDefault(productName, 0) + quantity);
        updateOrderPanel();
    }

    private void removeProductFromOrder(String productName) {
        currentOrder.remove(productName);
        updateOrderPanel();
    }

    private void updateOrderPanel() {
        orderElementsPanel.removeAll();
        this.total = 0.0;

        for (Map.Entry<String, Integer> entry : currentOrder.entrySet()) {
            String productName = entry.getKey();
            Integer quantity = entry.getValue();
            double price = productPrices.getOrDefault(productName, 0.0);
            double subtotal = price * quantity;
            this.total += subtotal;

            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel itemLabel = new JLabel(String.format("%d x %s (%.2f€)", quantity, productName, subtotal));
            JButton removeButton = new JButton("X");
            removeButton.setMargin(new Insets(0, 2, 0, 2));

            removeButton.addActionListener(e -> removeProductFromOrder(productName));

            itemPanel.add(itemLabel);
            itemPanel.add(removeButton);
            orderElementsPanel.add(itemPanel);
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemPanel.getPreferredSize().height));
        }

        totalLabel.setText(String.format("Total: %.2f€", this.total));

        orderElementsPanel.revalidate();
        orderElementsPanel.repaint();
    }

    private void getProductDataFromDB() {
        String url = "jdbc:mysql://localhost:3306/mcdonald";
        String db_user = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_user, db_password)) {
            String query = QueryLoader.loadQuery("GET_ALL_PRODUCTS");

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    productPrices.put(name, price);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching product data: " + e.getMessage());
            
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if (!productsLoaded) {
            loadProducts();
            productsLoaded = true;
        }
    }

    public static Optional<String> getSelectedProductName() {
        return selectedProductName;
    }

    public static void setSelectedProductName(String productName) {
        if (Optional.ofNullable(productName).isEmpty() || productName.isEmpty()) {
            selectedProductName = Optional.empty();
        } else {
            selectedProductName = Optional.of(productName);
        }
    }

}
