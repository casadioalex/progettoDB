package mcdonald.view.panels.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

    private static final String TITLE = "LOGIN";
    private static final String EMAIL_LABEL_TEXT = "Email:";
    private static final String PASSWORD_LABEL_TEXT = "Password:";
    private static final String LOGIN_BUTTON_TEXT = "Login";
    private static final String CREATE_ACCOUNT_BUTTON_TEXT = "Create Account";

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

    }

    @Override
    public void addNotify() {
        super.addNotify();
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
