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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class ViewProductInfo extends JPanel {

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final String BACK_BUTTON_TEXT = "Back to Order";

    private final JLabel titleLabel;
    private final JPanel ingredientsPanel;
    private final JPanel nutritionalInfoPanel;
    private final JButton backButton;

    public ViewProductInfo() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        ingredientsPanel = new JPanel(new GridBagLayout());
        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsPanel);
        ingredientsScrollPane.setPreferredSize(new Dimension(250, 300));
        add(ingredientsScrollPane, gbc);

        gbc.gridx = 1;
        nutritionalInfoPanel = new JPanel(new GridBagLayout());
        JScrollPane nutritionalInfoScrollPane = new JScrollPane(nutritionalInfoPanel);
        nutritionalInfoScrollPane.setPreferredSize(new Dimension(250, 300));
        add(nutritionalInfoScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);

        backButton.addActionListener(e -> {
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.switchMainPanel(MainPanels.CREATE_ORDER);
            }
        });
    }

    public void updateProductInfo() {
        String productName = CreateOrderPanel.getSelectedProductName().orElse("Product");
        titleLabel.setText(productName);

        ingredientsPanel.removeAll();
        nutritionalInfoPanel.removeAll();

        loadData();

        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
        nutritionalInfoPanel.revalidate();
        nutritionalInfoPanel.repaint();
    }

    private void loadData() {
        loadIngredients();
        loadNutritionalInfo();
    }

    private void loadIngredients() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;

        JLabel header = new JLabel("Ingredients");
        header.setFont(HEADER_FONT);
        ingredientsPanel.add(header, gbc);

        for (String ingredient : getIngredientsFromDB()) {
            ingredientsPanel.add(new JLabel(ingredient), gbc);
        }
        gbc.weighty = 1;
        ingredientsPanel.add(new JLabel(""), gbc);
    }

    private void loadNutritionalInfo() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel header = new JLabel("Nutritional Info");
        header.setFont(HEADER_FONT);
        gbc.gridwidth = 2;
        nutritionalInfoPanel.add(header, gbc);
        gbc.gridwidth = 1;

        int row = 1;
        for (Map.Entry<String, String> entry : getNutritionalInfoFromDB().entrySet()) {
            gbc.gridy = row;
            gbc.gridx = 0;
            nutritionalInfoPanel.add(new JLabel(entry.getKey() + ":"), gbc);
            gbc.gridx = 1;
            nutritionalInfoPanel.add(new JLabel(entry.getValue()), gbc);
            row++;
        }
        gbc.gridy = row;
        gbc.weighty = 1;
        nutritionalInfoPanel.add(new JLabel(""), gbc);
    }

    private List<String> getIngredientsFromDB() {
        List<String> ingredients = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String query = QueryLoader.loadQuery("GET_PRODUCT_INGREDIENTS");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, CreateOrderPanel.getSelectedProductName().orElseThrow(() -> new IllegalArgumentException("Product name not found")));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ingredients.add(rs.getString("ingredient_name"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching ingredients: " + e.getMessage());
        }
        return ingredients;
    }

    private Map<String, String> getNutritionalInfoFromDB() {
        Map<String, String> info = new TreeMap<>();
        try (Connection conn = getConnection()) {
            String query = QueryLoader.loadQuery("GET_NUTRITIONAL_INFO");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, CreateOrderPanel.getSelectedProductName().orElseThrow(() -> new IllegalArgumentException("Product name not found")));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    info.put("Calories", rs.getString("calories"));
                    info.put("Carbohydrates", rs.getString("carbohydrates"));
                    info.put("Proteins", rs.getString("proteins"));
                    info.put("Fats", rs.getString("fats"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching nutritional info: " + e.getMessage());
        }
        return info;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mcdonald";
        return DriverManager.getConnection(url, "root", "");
    }

    @Override
    public void addNotify() {
        super.addNotify();
        updateProductInfo();
    }

}
