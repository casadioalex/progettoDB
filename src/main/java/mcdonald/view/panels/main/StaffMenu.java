package mcdonald.view.panels.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class StaffMenu extends JPanel {
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

    public StaffMenu(List<String> staff) {
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

        staffPanel = new JPanel();
        staffPanel.setLayout(new BoxLayout(staffPanel, BoxLayout.Y_AXIS));

        for (String member : staff) {
            JButton staffButton = new JButton(member);
            staffButton.addActionListener(e -> apriOrdine(member));
            staffPanel.add(staffButton);
        }

        JScrollPane scrollPane = new JScrollPane(staffPanel);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        add(scrollPane, gbc);


        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.5;
        backButton = new JButton(BACK_BUTTON_TEXT);
        add(backButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridx++;
        gbc.weightx = 0.5;
        newStaffButton = new JButton(NEW_STAFF_BUTTON_TEXT);
        add(newStaffButton, gbc);
    }

    private void apriOrdine(String order) {
        // Qui puoi aprire un nuovo JFrame o JPanel con i dettagli dell’ordine
        JOptionPane.showMessageDialog(this, "Hai aperto: " + order);
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
