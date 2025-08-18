package mcdonald.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Window extends JFrame {

    private static final String TITLE = "McDonald's";
    private static final String ICON_PATH = "/images/icon.png";
    private static final double RESIZE_FACTOR = 0.8;

    private static final int WIDTH_PROPORTION = 4;
    private static final int HEIGHT_PROPORTION = 3;

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
        setVisible(true);
    }

}
