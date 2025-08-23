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
import java.sql.ResultSet;
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

public class LoginPanel extends JPanel {

    private static final String TITLE = "LOGIN";
    private static final String EMAIL_LABEL_TEXT = "Email:";
    private static final String PASSWORD_LABEL_TEXT = "Password:";
    private static final String LOGIN_BUTTON_TEXT = "LOGIN";
    private static final String CREATE_ACCOUNT_BUTTON_TEXT = "CREATE ACCOUNT";

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private static final int TEXT_FIELD_WIDTH = 20;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JTextField emailField;
    private final JTextField passwordField;
    private final JButton loginButton;
    private final JButton createAccountButton;
    private final JLabel errorMessage;

    public LoginPanel() {
        setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(EMAIL_LABEL_TEXT), gbc);
        gbc.gridx++;
        emailField = new JTextField(TEXT_FIELD_WIDTH);
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(PASSWORD_LABEL_TEXT), gbc);
        gbc.gridx++;
        passwordField = new JPasswordField(TEXT_FIELD_WIDTH);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        createAccountButton = new JButton(CREATE_ACCOUNT_BUTTON_TEXT);
        add(createAccountButton, gbc);
        gbc.gridx++;

        loginButton = new JButton(LOGIN_BUTTON_TEXT);
        add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        errorMessage = new JLabel();
        errorMessage.setHorizontalAlignment(JLabel.CENTER);
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);
        add(errorMessage, gbc);
        gbc.gridwidth = 1;

        connectButtonActions();
        applyProportionalInsets();
    }

    private void connectButtonActions() {
        createAccountButton.addActionListener(e -> {
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.REGISTER);
        });

        loginButton.addActionListener(e -> tryToLogin());
    }

    private void tryToLogin() {
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        String user_email = emailField.getText();
        String user_password = new String(((JPasswordField) passwordField).getPassword());

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("LOGIN");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user_email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String userRole = rs.getString("role");
                    if (HashingUtil.checkPassword(user_password, storedHash)) {
                        System.out.println("Login successful");
                        errorMessage.setVisible(false);
                        handleLoginSuccess(userRole);
                    } else {
                        errorMessage.setText("Email or password is incorrect");
                        errorMessage.setVisible(true);
                    }
                } else {
                    errorMessage.setText("Email or password is incorrect");
                    errorMessage.setVisible(true);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleLoginSuccess(String userRole) {
        Window window = (Window) SwingUtilities.getWindowAncestor(this);
        switch (userRole.toUpperCase()) {
            case "CLIENT" -> window.switchMainPanel(MainPanels.CLIENT_HOME);
                
            case "STAFF" -> {
                // TODO: window.switchMainPanel(MainPanels.STAFF_HOME);
            }
            case "ADMIN" -> {
                // TODO: window.switchMainPanel(MainPanels.ADMIN_HOME);
            }
            default -> {
                System.err.println("Unknown user role: " + userRole);
                errorMessage.setText("An error occurred. Please try again.");
                errorMessage.setVisible(true);
            }
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
