import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class TitleBarSmokeTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("flatlaf.useNativeWindowDecorations", "false");
        SwingUtilities.invokeAndWait(() -> {
            FlatMacDarkLaf.setup();
            UIManager.put("TitlePane.centerTitle", true);
            UIManager.put("RootPaneUI", MacRootPaneUI.class.getName());
            JFrame.setDefaultLookAndFeelDecorated(true);
            JFrame frame = new JFrame("MAC Multi Audio Converter");
            try {
                frame.add(new JLabel("Java 8 compatible - custom Mac-style window controls", SwingConstants.CENTER));
                frame.pack();
                frame.setSize(640, 160);
                frame.validate();
                if (!(frame.getRootPane().getUI() instanceof MacRootPaneUI))
                    throw new AssertionError("Custom decoration was not installed");
                MacRootPaneUI.MacTitlePane title = findTitle(frame.getRootPane());
                if (title == null) throw new AssertionError("Missing title bar");
                java.util.List<JButton> buttons = new java.util.ArrayList<>();
                collectButtons(title, buttons);
                if (buttons.size() != 3) throw new AssertionError("Expected three visible controls: " + buttons.size());
                String[] expected = {"Close", "Iconify", "Maximize"};
                for (int i = 0; i < 3; i++) {
                    JButton b = buttons.get(i);
                    if (!expected[i].equals(b.getAccessibleContext().getAccessibleName()))
                        throw new AssertionError("Wrong traffic light order");
                    if (b.getActionListeners().length == 0) throw new AssertionError("Missing window action");
                    if (SwingUtilities.convertPoint(b, 0, 0, title).x > 110)
                        throw new AssertionError("Controls are not on the left");
                }
                BufferedImage image = new BufferedImage(640, 160, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                frame.getRootPane().printAll(graphics);
                graphics.dispose();
                if (args.length > 0) ImageIO.write(image, "png", new File(args[0]));
                System.out.println("Title bar layout and action wiring passed on Java " + System.getProperty("java.version"));
            } catch (Exception e) { throw new RuntimeException(e); }
            finally { frame.dispose(); }
        });
    }
    private static MacRootPaneUI.MacTitlePane findTitle(Container c) {
        for (Component child : c.getComponents()) {
            if (child instanceof MacRootPaneUI.MacTitlePane) return (MacRootPaneUI.MacTitlePane) child;
            if (child instanceof Container) {
                MacRootPaneUI.MacTitlePane found = findTitle((Container) child);
                if (found != null) return found;
            }
        }
        return null;
    }
    private static void collectButtons(Container c, java.util.List<JButton> buttons) {
        for (Component child : c.getComponents()) {
            if (child instanceof JButton && child.isVisible()) buttons.add((JButton) child);
            else if (child instanceof Container) collectButtons((Container) child, buttons);
        }
    }
}
