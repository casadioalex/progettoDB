package mcdonald.view.panels.home.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreateOrderPanel extends JPanel {

    private static final String TITLE = "CREATE NEW ORDER";
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    private final GridBagConstraints gbc = new GridBagConstraints();

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
    }

}
