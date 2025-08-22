package mcdonald.view.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mcdonald.api.main.MainPanels;
import mcdonald.view.panels.home.client.ClientHomePanel;
import mcdonald.view.panels.main.LoginPanel;
import mcdonald.view.panels.main.RegisterPanel;

public class Window extends JFrame {

    private static final String TITLE = "McDonald's";
    private static final String ICON_PATH = "/images/icon.png";
    private static final double RESIZE_FACTOR = 0.8;

    private static final int WIDTH_PROPORTION = 4;
    private static final int HEIGHT_PROPORTION = 3;

    private final List<JPanel> panels = new LinkedList<>();
    private Optional<String> userEmail = Optional.of("mario.rossi@email.com");

    public Window() {
        setTitle(TITLE);
        setIconImage(new ImageIcon(getClass().getResource(ICON_PATH)).getImage());
        setSize(getCustomSize());

        panels.add(new ClientHomePanel());
        panels.add(new LoginPanel());
        panels.add(new RegisterPanel());

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

    public void switchMainPanel(MainPanels targetPanel) {
        setContentPane(panels.get(targetPanel.ordinal()));
        revalidate();
        repaint();
    }

    public Optional<String> getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        if (Optional.ofNullable(userEmail).isEmpty() || userEmail.isEmpty()) {
            this.userEmail = Optional.empty();
        } else {
            this.userEmail = Optional.of(userEmail);
        }
    }

}
