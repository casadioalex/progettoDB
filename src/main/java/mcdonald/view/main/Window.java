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

    private static Optional<String> userEmail = Optional.empty();
    private static Optional<Integer> orderId = Optional.empty();
    private static Optional<String> staffEmail = Optional.empty();

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

    public void switchMainPanel(MainPanels panel) {
        setContentPane(panel.getPanel());
        revalidate();
        repaint();
    }

    public static Optional<String> getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        if (Optional.ofNullable(userEmail).isEmpty() || userEmail.isEmpty()) {
            Window.userEmail = Optional.empty();
        } else {
            Window.userEmail = Optional.of(userEmail);
        }
    }

    public static void setOrderId(Integer orderId) {
        if (orderId == null) {
            Window.orderId = Optional.empty();
        } else {
            Window.orderId = Optional.of(orderId);
        }
    }

    public static Optional<Integer> getOrderId() {
        return orderId;
    }

    public static Optional<String> getStaffEmail() {
        return staffEmail;
    }

    public static void setStaffEmail(String staffEmail) {
        if (Optional.ofNullable(staffEmail).isEmpty() || staffEmail.isEmpty()) {
            Window.staffEmail = Optional.empty();
        } else {
            Window.staffEmail = Optional.of(staffEmail);
        }
    }

}
