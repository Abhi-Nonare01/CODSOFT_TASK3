package ui;

import utils.ThemeManager;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom JPanel providing a Glassmorphism effect.
 * Features a semi-transparent background, frosted border, and optional soft shadow.
 */
public class GlassPanel extends JPanel {
    private final int cornerRadius;
    private final boolean drawShadow;
    private Color customGlassColor = null;

    public GlassPanel(int cornerRadius, boolean drawShadow) {
        this.cornerRadius = cornerRadius;
        this.drawShadow = drawShadow;
        setOpaque(false);
    }

    public GlassPanel(int cornerRadius) {
        this(cornerRadius, true);
    }

    public void setCustomGlassColor(Color color) {
        this.customGlassColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ThemeManager theme = ThemeManager.getInstance();

        if (drawShadow) {
            g2d.setColor(new Color(0, 0, 0, 25));
            for (int i = 0; i < 8; i++) {
                g2d.fillRoundRect(i, i + 4, getWidth() - (i * 2), getHeight() - (i * 2), cornerRadius, cornerRadius);
            }
        }

        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2d.setColor(customGlassColor != null ? customGlassColor : theme.getGlassColor());
        g2d.fill(roundedRectangle);

        g2d.setColor(theme.getGlassBorder());
        g2d.draw(roundedRectangle);

        g2d.dispose();
        super.paintComponent(g);
    }
}