package ui;

import utils.ThemeManager;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A sleek, animated button with rounded corners and smooth hover transitions.
 * Custom text rendering ensures perfect alignment and crisp fonts.
 */
public class RoundedButton extends JButton {
    private final int cornerRadius;
    private Color defaultColor;
    private Color hoverColor;
    private Color currentColor;
    private Timer animationTimer;
    private float alpha = 0.0f;
    private boolean isHovered = false;

    public RoundedButton(String text) {
        this(text, ThemeManager.getInstance().getAccentColor(), ThemeManager.getInstance().getAccentHoverColor(), 25);
    }

    public RoundedButton(String text, Color baseColor, Color hoverColor) {
        this(text, baseColor, hoverColor, 25);
    }

    public RoundedButton(String text, Color baseColor, Color hoverColor, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        this.defaultColor = baseColor;
        this.hoverColor = hoverColor;
        this.currentColor = baseColor;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(ThemeManager.getInstance().getBoldFont());
        setForeground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    startAnimation();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = false;
                    startAnimation();
                }
            }
        });
    }

    public void setColors(Color baseColor, Color hoverColor) {
        this.defaultColor = baseColor;
        this.hoverColor = hoverColor;
        this.currentColor = baseColor;
        repaint();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (!b) {
            currentColor = new Color(100, 116, 139); // Slate-500 for disabled
            isHovered = false;
        } else {
            currentColor = defaultColor;
        }
        repaint();
    }

    private void startAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationTimer = new Timer(15, e -> {
            if (isHovered && alpha < 1.0f) {
                alpha += 0.1f;
            } else if (!isHovered && alpha > 0.0f) {
                alpha -= 0.1f;
            } else {
                ((Timer) e.getSource()).stop();
            }
            alpha = Math.max(0.0f, Math.min(1.0f, alpha));

            int r = (int) (defaultColor.getRed() * (1 - alpha) + hoverColor.getRed() * alpha);
            int g = (int) (defaultColor.getGreen() * (1 - alpha) + hoverColor.getGreen() * alpha);
            int b = (int) (defaultColor.getBlue() * (1 - alpha) + hoverColor.getBlue() * alpha);
            currentColor = new Color(r, g, b);

            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Enforce high-quality rendering for shapes and text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw Shadow
        if (isEnabled()) {
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillRoundRect(0, 4, getWidth(), getHeight() - 4, cornerRadius, cornerRadius);
        }

        // Draw Background
        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight() - 4, cornerRadius, cornerRadius);

        // Draw Text Manually for Perfect Centering
        if (getText() != null && !getText().isEmpty()) {
            g2d.setColor(getForeground());
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();

            int textWidth = fm.stringWidth(getText());
            int textX;

            // Handle alignments safely
            if (getHorizontalAlignment() == SwingConstants.LEFT) {
                textX = 20;
            } else if (getHorizontalAlignment() == SwingConstants.RIGHT) {
                textX = getWidth() - textWidth - 20;
            } else {
                textX = (getWidth() - textWidth) / 2;
            }

            // Vertically center exactly inside the colored area (ignoring the 4px shadow)
            int textY = ((getHeight() - 4 - fm.getHeight()) / 2) + fm.getAscent();

            g2d.drawString(getText(), textX, textY);
        }

        g2d.dispose();
        // Note: intentionally skipping super.paintComponent(g) to avoid duplicate blurry text rendering
    }
}