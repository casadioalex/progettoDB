package mcdonald.view.panels.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.HashingUtil;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class RegisterPanel extends JPanel {

    private static final String TITLE = "REGISTER";
    private static final String USERNAME_LABEL_TEXT = "Username:";
    private static final String NAME_LABEL_TEXT = "Name:";
    private static final String SURNAME_LABEL_TEXT = "Surname:";
    private static final String EMAIL_LABEL_TEXT = "Email:";
    private static final String PASSWORD_LABEL_TEXT = "Password:";

    private static final String ADDRESS_NUMBER_LABEL_TEXT = "Number:";
    private static final String ADDRESS_STREET_LABEL_TEXT = "Street:";
    private static final String ADDRESS_CITY_LABEL_TEXT = "City:";
    private static final String ADDRESS_POSTAL_CODE_LABEL_TEXT = "Postal Code:";
    private static final String ADDRESS_PROVINCE_LABEL_TEXT = "Province:";

    private static final String REGISTER_BUTTON_TEXT = "REGISTER";
    private static final String LOGIN_BUTTON_TEXT = "LOGIN";

    private static final double WIDTH_INSET_PROPORTION = 0.01;
    private static final double HEIGHT_INSET_PROPORTION = 0.05;

    private static final int TEXT_FIELD_WIDTH = 15;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JTextField usernameField;
    private final JTextField nameField;
    private final JTextField surnameField;
    private final JTextField emailField;
    private final JTextField passwordField;

    private final JTextField addressNumberField;
    private final JTextField addressStreetField;
    private final JTextField addressCityField;
    private final JTextField addressPostalCodeField;
    private final JTextField addressProvinceField;

    private final JButton registerButton;
    private final JButton loginButton;
    private final JLabel errorMessage;

    public RegisterPanel() {
        setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.BOTH;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(USERNAME_LABEL_TEXT), gbc);
        gbc.gridx++;
        usernameField = new JTextField(TEXT_FIELD_WIDTH);
        add(usernameField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(NAME_LABEL_TEXT), gbc);
        gbc.gridx++;
        nameField = new JTextField(TEXT_FIELD_WIDTH);
        add(nameField, gbc);

        // Surname
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(SURNAME_LABEL_TEXT), gbc);
        gbc.gridx++;
        surnameField = new JTextField(TEXT_FIELD_WIDTH);
        add(surnameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(EMAIL_LABEL_TEXT), gbc);
        gbc.gridx++;
        emailField = new JTextField(TEXT_FIELD_WIDTH);
        add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(PASSWORD_LABEL_TEXT), gbc);
        gbc.gridx++;
        passwordField = new JPasswordField(TEXT_FIELD_WIDTH);
        add(passwordField, gbc);

        // Address street
        gbc.gridx++;
        add(new JLabel(ADDRESS_STREET_LABEL_TEXT), gbc);
        gbc.gridx++;
        addressStreetField = new JTextField(TEXT_FIELD_WIDTH);
        add(addressStreetField, gbc);

        // Address number
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(ADDRESS_NUMBER_LABEL_TEXT), gbc);
        gbc.gridx++;
        addressNumberField = new JTextField(TEXT_FIELD_WIDTH / 2);
        add(addressNumberField, gbc);

        // Address city
        gbc.gridx++;
        add(new JLabel(ADDRESS_CITY_LABEL_TEXT), gbc);
        gbc.gridx++;
        addressCityField = new JTextField(TEXT_FIELD_WIDTH);
        add(addressCityField, gbc);

        // Address postal code
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(ADDRESS_POSTAL_CODE_LABEL_TEXT), gbc);
        gbc.gridx++;
        addressPostalCodeField = new JTextField(TEXT_FIELD_WIDTH / 2);
        add(addressPostalCodeField, gbc);

        // Address province
        gbc.gridx++;
        add(new JLabel(ADDRESS_PROVINCE_LABEL_TEXT), gbc);
        gbc.gridx++;
        addressProvinceField = new JTextField(TEXT_FIELD_WIDTH);
        add(addressProvinceField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginButton = new JButton(LOGIN_BUTTON_TEXT);
        add(loginButton, gbc);
        gbc.gridx = 2;
        registerButton = new JButton(REGISTER_BUTTON_TEXT);
        add(registerButton, gbc);

        errorMessage = new JLabel();
        errorMessage.setHorizontalAlignment(JLabel.CENTER);
        errorMessage.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 6;
        add(errorMessage, gbc);
        gbc.gridwidth = 1;

        connectButtonActions();
        applyProportionalInsets();
    }

    private void connectButtonActions() {
        loginButton.addActionListener(e -> {
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.LOGIN);
        });

        registerButton.addActionListener(e -> tryToRegister());
    }

    private boolean checkIfOneIsEmpty() {
        return Arrays.stream(getComponents()).anyMatch(comp -> {
            if (comp instanceof JTextField jTextField) {
                return jTextField.getText().isEmpty();
            }
            return false;
        });
    }

    private void tryToRegister() {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        String username = usernameField.getText();
        String user_name = nameField.getText();
        String user_surname = surnameField.getText();
        String user_email = emailField.getText();
        String user_password = new String(((JPasswordField) passwordField).getPassword());
        String user_hashpassword = HashingUtil.hashPassword(user_password);
        String user_address_number = addressNumberField.getText();
        String user_address_street = addressStreetField.getText();
        String user_address_city = addressCityField.getText();
        String user_address_postal_code = addressPostalCodeField.getText();
        String user_address_province = addressProvinceField.getText();

        if (checkIfOneIsEmpty()) {
            errorMessage.setText("Please fill in all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("REGISTER");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, user_name);
                stmt.setString(3, user_surname);
                stmt.setString(4, user_email);
                stmt.setString(5, user_hashpassword);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User registered successfully!");
                } else {
                    System.out.println("Failed to register user.");
                    return;
                }
            }

            String registerAddressQuery = QueryLoader.loadQuery("REGISTER_ADDRESS");
            try (PreparedStatement stmt = conn.prepareStatement(registerAddressQuery)) {
                stmt.setString(1, user_address_street);
                stmt.setString(2, user_address_number);
                stmt.setString(3, user_address_city);
                stmt.setString(4, user_address_postal_code);
                stmt.setString(5, user_address_province);
                stmt.setString(6, user_email);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Address registered successfully!");
                } else {
                    System.out.println("Failed to register address.");
                    return;
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void applyProportionalInsets() {
        final Dimension size = getPreferredSize();
        final double width = size.getWidth();
        final double height = size.getHeight();

        Insets insets = new Insets(
            (int) (height * HEIGHT_INSET_PROPORTION),
            (int) (width * WIDTH_INSET_PROPORTION),
            (int) (height * HEIGHT_INSET_PROPORTION),
            (int) (width * WIDTH_INSET_PROPORTION)
        );

        GridBagLayout layout = (GridBagLayout) getLayout();
        Arrays.stream(getComponents()).forEach(component -> {
            GridBagConstraints c = layout.getConstraints(component);
            c.insets = insets;
            layout.setConstraints(component, c);
        });
    }

}
