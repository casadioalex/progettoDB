package mcdonald.view.panels.home.client;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuantitySelectorPanel extends JPanel {

    private final String productName;
    private final JButton productButton;
    private final JButton minusButton;
    private final JLabel quantityLabel;
    private final JButton plusButton;
    private final JButton addButton;

    private int quantity = 1;

    public QuantitySelectorPanel(String productName, double price) {
        this.productName = productName;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        String buttonText = String.format("%s (%.2f€)", productName, price);
        productButton = new JButton(buttonText);
        
        minusButton = new JButton("-");
        quantityLabel = new JLabel(" " + quantity + " ");
        plusButton = new JButton("+");
        addButton = new JButton("Add");

        minusButton.setEnabled(false);

        add(productButton);
        add(minusButton);
        add(quantityLabel);
        add(plusButton);
        add(addButton);

        plusButton.addActionListener(e -> {
            quantity++;
            quantityLabel.setText(" " + quantity + " ");
            minusButton.setEnabled(true);
        });

        minusButton.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                quantityLabel.setText(" " + quantity + " ");
            }
            if (quantity == 1) {
                minusButton.setEnabled(false);
            }
        });
    }

    public String getProductName() {
        return this.productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getProductButton() {
        return productButton;
    }
}