package mcdonald.view.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import mcdonald.api.main.MainPanels;

public class Window extends JFrame {

    private static final String TITLE = "McDonald's";
    private static final String ICON_PATH = "/images/icon.png";
    private static final double RESIZE_FACTOR = 0.8;

    private static final int WIDTH_PROPORTION = 4;
    private static final int HEIGHT_PROPORTION = 3;

    private Optional<String> userEmail = Optional.empty();

    public Window() {
        setTitle(TITLE);
        setIconImage(new ImageIcon(getClass().getResource(ICON_PATH)).getImage());
        setSize(getCustomSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Dimension getCustomSize() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = (int) (Math.min(size.getWidth(), size.getHeight()) * RESIZE_FACTOR);
        final int height = width * HEIGHT_PROPORTION / WIDTH_PROPORTION;

        return new Dimension(width, height);
    }

    public void display() {
        setContentPane(MainPanels.LOGIN.getPanel());
        setLocationByPlatform(true);
        setVisible(true);
        setResizable(false);
    }

    public void switchMainPanel(MainPanels targetPanel) {
        setContentPane(targetPanel.getPanel());
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
