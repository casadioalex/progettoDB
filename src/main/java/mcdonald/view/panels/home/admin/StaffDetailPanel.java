package mcdonald.view.panels.home.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mcdonald.api.main.MainPanels;
import mcdonald.model.common.QueryLoader;
import mcdonald.view.main.Window;

public class StaffDetailPanel extends JPanel {
    private static final String TITLE = "STAFF DETAILS";
    private static final String BACK_BUTTON_TEXT = "BACK TO STAFF MENU";
    private static final String REMOVE_STAFF_BUTTON_TEXT = "REMOVE STAFF MEMBER";

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton backButton;
    private final JButton removeStaffButton;

    public StaffDetailPanel() {
        setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        String[] labels = { "Username:", "Name:", "Surname:", "Email:", "Registration Date:" };
        String[] values = getStaffDetailsByEmail();

        gbc.gridwidth = 1;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy++;
            add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            add(new JLabel(values[i]), gbc);
        }

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.5;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);
        backButton.addActionListener(e -> {
            Window window = (mcdonald.view.main.Window) SwingUtilities.getWindowAncestor(this);
            window.switchMainPanel(MainPanels.STAFF_MENU);
        });

        gbc.gridwidth = 1;
        gbc.gridx++;
        gbc.weightx = 0.5;
        removeStaffButton = new JButton(REMOVE_STAFF_BUTTON_TEXT);
        add(removeStaffButton, gbc);
        removeStaffButton.addActionListener(e -> removeStaffByEmail());
    }

    private String[] getStaffDetailsByEmail() {
        String[] details = new String[5];
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        Window window = (Window) SwingUtilities.getWindowAncestor(this);
        String email = window.getStaffEmail().orElse("");

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("GET_STAFF_DETAIL_BY_EMAIL");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    details[0] = rs.getString("username");
                    details[1] = rs.getString("name");
                    details[2] = rs.getString("surname");
                    details[3] = rs.getString("email");
                    details[4] = rs.getString("registrationDate");
                }
            }
        } catch (Exception e) {
            details[0] = "Errore caricamento dati: " + e.getMessage();
        }
        return details;
    }

    private void removeStaffByEmail() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove the staff member?",
                "Confirm Removal",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION)
            return;

        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";
        Window window = (Window) SwingUtilities.getWindowAncestor(this);
        String email = window.getStaffEmail().orElse("");

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = QueryLoader.loadQuery("REMOVE_STAFF_BY_EMAIL");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                int deleted = stmt.executeUpdate();
                if (deleted > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Staff rimosso con successo.");
                    // TODO: Torna al menu staff o aggiorna la vista
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Nessun membro staff trovato con questa email.");
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Errore: " + e.getMessage());
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
