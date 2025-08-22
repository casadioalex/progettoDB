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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class ClientHomePanel extends JPanel {

    private static final String TITLE = "Client Home";
    private static final String LOGOUT_BUTTON_TEXT = "LOGOUT";
    private static final String NEW_ORDER_BUTTON_TEXT = "NEW ORDER";

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JPanel ordersPanel;
    private final JScrollPane ordersScrollPane;
    private final JButton logoutButton;
    private final JButton newOrderButton;

    private final Map<Integer, String> ordersIds = new LinkedHashMap<>();
    private boolean dataLoaded = false;

    public ClientHomePanel() {
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
        ordersScrollPane = new JScrollPane(ordersPanel);
        ordersScrollPane.setPreferredSize(new Dimension(500, 300));
        add(ordersScrollPane, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        logoutButton = new JButton(LOGOUT_BUTTON_TEXT);
        add(logoutButton, gbc);
        gbc.gridx++;
        newOrderButton = new JButton(NEW_ORDER_BUTTON_TEXT);
        add(newOrderButton, gbc);

    }

    private void populateOrdersPanel() {
        ordersPanel.removeAll();
        ordersIds.forEach((orderid, str) -> {
            JButton orderButton = new JButton(String.format("ORDER: #%04d %s", orderid, str));
            orderButton.setName(String.valueOf(orderid));
            orderButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            orderButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, orderButton.getPreferredSize().height));
            //orderButton.addActionListener(e -> apriOrdine(orderid));
            ordersPanel.add(orderButton);
        });
    }

    private void getOrdersIds() {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        Window window = (Window) SwingUtilities.getWindowAncestor(this);

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("GET_ALL_ORDERS");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, window.getUserEmail().orElseThrow(NullPointerException::new));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int order_id = rs.getInt("order_id");
                    boolean completed = rs.getBoolean("completed");
                    String order_date = rs.getString("order_date");
                    ordersIds.put(order_id, String.format("%s [Completed: %s]", order_date, completed));
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

        if (!dataLoaded) {
            getOrdersIds();
            populateOrdersPanel();
            dataLoaded = true;
        }

        final Dimension size = getPreferredSize();
        final var width = size.getWidth();
        final var height = size.getHeight();

        Insets insets = new Insets((int) (height * HEIGHT_INSET_PROPORTION), (int) (width * WIDTH_INSET_PROPORTION), (int) (height * HEIGHT_INSET_PROPORTION), (int) (width * WIDTH_INSET_PROPORTION));

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
