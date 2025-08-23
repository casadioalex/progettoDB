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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class StaffMenuPanel extends JPanel {
    private static final String TITLE = "STAFF MENU";
    private static final String BACK_BUTTON_TEXT = "BACK TO HOME";
    private static final String NEW_STAFF_BUTTON_TEXT = "NEW STAFF MEMBER";

    private static final double WIDTH_INSET_PROPORTION = 0.05;
    private static final double HEIGHT_INSET_PROPORTION = 0.1;

    private static final String TITLE_FONT_NAME = "Arial";
    private static final int TITLE_FONT_SIZE = 24;
    private static final Font TITLE_FONT = new Font(TITLE_FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);

    private GridBagConstraints gbc = new GridBagConstraints();

    private final JButton backButton;
    private final JButton newStaffButton;
    private final JPanel staffPanel;

    public StaffMenuPanel() {
        setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        staffPanel = new JPanel();
        staffPanel.setLayout(new BoxLayout(staffPanel, BoxLayout.Y_AXIS));

        List<String> staff = getAllStaffMembers();
        for (String member : staff) {
            JButton staffButton = new JButton(member);
            staffButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
            staffButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, staffButton.getPreferredSize().height));
            staffButton.addActionListener(e -> openStaffDetails(member));
            staffPanel.add(staffButton);
        }

        JScrollPane scrollPane = new JScrollPane(staffPanel);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 0.5;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);

        gbc.gridx++;
        newStaffButton = new JButton(NEW_STAFF_BUTTON_TEXT);
        add(newStaffButton, gbc);

        newStaffButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Apertura RegisterStaffPanel...");
           // TODO: Apri RegisterStaffPanel qui
            // TODO: Sostituisci con la logica per mostrare il pannello di registrazione
            // staff
        });
    }

    private void openStaffDetails(String member) {
        // Qui puoi estrarre l'email dal testo del bottone se necessario TODO
        // Esempio: "Mario Rossi (mario@email.com)"
        String email = member.substring(member.indexOf('(') + 1, member.indexOf(')'));
        // Sostituisci questa JOptionPane con la logica per aprire il vero pannello dei
        // dettagli
        JOptionPane.showMessageDialog(this, "Staff details per: " + email);
    }

    private List<String> getAllStaffMembers() {
        List<String> staffList = new ArrayList<>();
        String database = "mcdonald";
        String url = "jdbc:mysql://localhost:3306/" + database;
        String db_email = "root";
        String db_password = "";

        try (Connection conn = DriverManager.getConnection(url, db_email, db_password)) {
            String query = mcdonald.model.common.QueryLoader.loadQuery("GET_ALL_STAFF");
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String email = rs.getString("email");
                    staffList.add(name + " " + surname + " (" + email + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento staff: " + e.getMessage());
        }
        return staffList;
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
