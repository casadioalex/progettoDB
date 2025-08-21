package mcdonald.view.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mcdonald.view.panels.main.LoginPanel;
import mcdonald.view.panels.main.OrderDetails;
import mcdonald.view.panels.main.RegisterPanel;
import mcdonald.view.panels.main.StaffHome;

public class Window extends JFrame {

    private static final String TITLE = "McDonald's";
    private static final String ICON_PATH = "/images/icon.png";
    private static final double RESIZE_FACTOR = 0.8;

    private static final int WIDTH_PROPORTION = 4;
    private static final int HEIGHT_PROPORTION = 3;

    private final List<JPanel> panels = new LinkedList<>();

    List<String> orders = List.of(
            "Ordine #1001 - Mario Rossi - Totale €15.50 - In attesa",
            "Ordine #1002 - Luca Bianchi - Totale €22.90 - Consegnato",
            "Ordine #1003 - Anna Verdi - Totale €8.40 - In preparazione",
            "Ordine #1004 - Chiara Neri - Totale €19.70 - Consegnato",
            "Ordine #1005 - Paolo Gialli - Totale €12.00 - Annullato",
            "Ordine #1006 - Giulia Fontana - Totale €25.30 - In preparazione",
            "Ordine #1007 - Matteo Greco - Totale €18.20 - Consegnato",
            "Ordine #1008 - Elisa Romano - Totale €10.50 - In attesa",
            "Ordine #1009 - Davide Colombo - Totale €16.75 - In preparazione",
            "Ordine #1010 - Sara Conti - Totale €21.40 - Consegnato",
            "Ordine #1011 - Marco Esposito - Totale €14.30 - Consegnato",
            "Ordine #1012 - Laura Ferri - Totale €19.95 - Annullato",
            "Ordine #1013 - Stefano Ricci - Totale €11.60 - In preparazione",
            "Ordine #1014 - Alessia Marino - Totale €9.90 - In attesa",
            "Ordine #1015 - Francesco De Luca - Totale €27.80 - Consegnato",
            "Ordine #1016 - Martina Gallo - Totale €13.70 - In preparazione",
            "Ordine #1017 - Andrea Riva - Totale €17.25 - Consegnato",
            "Ordine #1018 - Chiara Moretti - Totale €20.10 - In attesa",
            "Ordine #1019 - Giorgio Bassi - Totale €12.80 - Consegnato",
            "Ordine #1020 - Federica Leone - Totale €23.60 - In preparazione");

    public Window() {
        setTitle(TITLE);
        setIconImage(new ImageIcon(getClass().getResource(ICON_PATH)).getImage());
        setSize(getCustomSize());

        panels.add(new OrderDetails(orders));
        panels.add(new LoginPanel());
        panels.add(new RegisterPanel());
        panels.add(new StaffHome(orders));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Dimension getCustomSize() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = (int) (Math.min(size.getWidth(), size.getHeight()) * RESIZE_FACTOR);
        final int height = width * HEIGHT_PROPORTION / WIDTH_PROPORTION;

        return new Dimension(width, height);
    }

    public void display() {
        setContentPane(panels.getFirst());
        setLocationByPlatform(true);
        setVisible(true);
    }

}
