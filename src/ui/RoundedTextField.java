package ui;

import utils.ThemeManager;

import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * A modern input field that doubles as a standard text field or a password field.
 * Handles focus styling, placeholder behavior, and rounded aesthetics.
 */
public class RoundedTextField extends JPasswordField {
    private final int cornerRadius = 15;
    private final String placeholder;
    private boolean isFocused = false;
    private final boolean isPassword;

    public RoundedTextField(int columns, String placeholder, boolean isPassword) {
        super(columns);
        this.placeholder = placeholder;
        this.isPassword = isPassword;

        if (!isPassword) {
            setEchoChar((char) 0);
        } else {
            setEchoChar('•');
        }

        setOpaque(false);
        setBorder(new EmptyBorder(12, 15, 12, 15));
        setFont(ThemeManager.getInstance().getNormalFont());
        setCaretColor(ThemeManager.getInstance().getAccentColor());
        setHorizontalAlignment(JPasswordField.CENTER);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ThemeManager theme = ThemeManager.getInstance();

        g2d.setColor(theme.getInputBackground());
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        if (isFocused) {
            g2d.setColor(theme.getAccentColor());
        } else {
            g2d.setColor(theme.getGlassBorder());
        }
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        String currentText = new String(getPassword());
        if (currentText.isEmpty() && !isFocused) {
            g2d.setColor(theme.getTextSecondary());
            g2d.setFont(getFont());

            int textWidth = g2d.getFontMetrics().stringWidth(placeholder);
            int textX = (getWidth() - textWidth) / 2;

            // FIX: Agar text field chota hai, to text ko center se hata kar left align kar do
            // taaki wo left aur right se cut na ho.
            if (textX < getInsets().left) {
                textX = getInsets().left;
            }

            int textY = (getHeight() + g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent()) / 2;
            g2d.drawString(placeholder, textX, textY);
        }

        g2d.dispose();
        setForeground(theme.getTextPrimary());
        super.paintComponent(g);
    }
}