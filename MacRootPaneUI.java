import com.formdev.flatlaf.ui.FlatRootPaneUI;
import com.formdev.flatlaf.ui.FlatTitlePane;
import com.formdev.flatlaf.util.UIScale;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/** Mac-style controls using FlatLaf's existing window actions and resize handler. */
public class MacRootPaneUI extends FlatRootPaneUI {
    public static ComponentUI createUI(JComponent component) {
        return new MacRootPaneUI();
    }

    @Override protected FlatTitlePane createTitlePane() {
        return new MacTitlePane(rootPane);
    }

    static class MacTitlePane extends FlatTitlePane {
        MacTitlePane(JRootPane root) {
            super(root);
            // Retain FlatLaf's mouse layer, window listeners, and button actions.
            removeAll();
            setLayout(new BorderLayout());
            buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, UIScale.scale(4), UIScale.scale(7)));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, UIScale.scale(6), 0, UIScale.scale(6)));
            buttonPanel.removeAll();
            buttonPanel.add(closeButton);
            buttonPanel.add(iconifyButton);
            buttonPanel.add(maximizeButton);
            buttonPanel.add(restoreButton);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(buttonPanel, BorderLayout.WEST);
            add(titleLabel, BorderLayout.CENTER);
            JPanel balance = new JPanel() {
                @Override public Dimension getPreferredSize() {
                    return buttonPanel.getPreferredSize();
                }
            };
            balance.setOpaque(false);
            add(balance, BorderLayout.EAST);
        }

        @Override protected JButton createButton(String key, String accessibleName, ActionListener action) {
            JButton button = super.createButton(key, accessibleName, action);
            button.setIcon(new TrafficLightIcon(key));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setOpaque(false);
            button.setRolloverEnabled(true);
            button.setPreferredSize(UIScale.scale(new Dimension(20, 20)));
            button.setMinimumSize(button.getPreferredSize());
            button.setToolTipText(accessibleName);
            return button;
        }
    }

    private static class TrafficLightIcon implements Icon {
        private final String key;
        TrafficLightIcon(String key) { this.key = key; }
        public int getIconWidth() { return UIScale.scale(14); }
        public int getIconHeight() { return UIScale.scale(14); }
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.translate(x, y);
                UIScale.scaleGraphics(g);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean close = key.contains("close");
                boolean minimize = key.contains("iconify");
                Color color = close ? new Color(255, 95, 87)
                        : minimize ? new Color(255, 189, 46) : new Color(40, 201, 64);
                AbstractButton button = (AbstractButton) component;
                Window window = SwingUtilities.getWindowAncestor(component);
                if (!button.isEnabled() || (window != null && !window.isActive())) color = new Color(105, 105, 110);
                if (button.getModel().isPressed()) color = color.darker();
                g.setColor(color);
                g.fillOval(1, 1, 12, 12);
                if (button.getModel().isRollover() && button.isEnabled()) {
                    g.setColor(new Color(55, 55, 55));
                    g.setStroke(new BasicStroke(1.2f));
                    if (close) {
                        g.drawLine(5, 5, 9, 9);
                        g.drawLine(9, 5, 5, 9);
                    } else if (minimize) {
                        g.drawLine(4, 7, 10, 7);
                    } else {
                        g.drawLine(4, 7, 10, 7);
                        if (!key.contains("restore")) g.drawLine(7, 4, 7, 10);
                    }
                }
            } finally { g.dispose(); }
        }
    }
}
