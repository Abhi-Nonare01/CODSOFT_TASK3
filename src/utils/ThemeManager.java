package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Manages the application theme, colors, and typography for SmartATM2026.
 * Implements the Singleton pattern.
 * Provides a premium banking aesthetic with Light and Dark modes.
 */
public class ThemeManager {
    private static ThemeManager instance;
    private boolean darkMode = true;

    // Dark Mode Palette (Premium Banking: Deep Navy, Slate, Gold)
    private final Color darkGradientStart = new Color(2, 6, 23);
    private final Color darkGradientEnd = new Color(30, 58, 138);
    private final Color darkGlassColor = new Color(255, 255, 255, 15);
    private final Color darkGlassBorder = new Color(255, 255, 255, 35);
    private final Color darkTextPrimary = new Color(248, 250, 252);
    private final Color darkTextSecondary = new Color(148, 163, 184);
    private final Color darkInputBackground = new Color(15, 23, 42, 180);
    private final Color darkSidebarColor = new Color(2, 6, 23, 200);

    // Light Mode Palette (Clean, Professional, Trustworthy)
    private final Color lightGradientStart = new Color(240, 249, 255);
    private final Color lightGradientEnd = new Color(186, 230, 253);
    private final Color lightGlassColor = new Color(255, 255, 255, 200);
    private final Color lightGlassBorder = new Color(255, 255, 255, 255);
    private final Color lightTextPrimary = new Color(15, 23, 42);
    private final Color lightTextSecondary = new Color(71, 85, 105);
    private final Color lightInputBackground = new Color(255, 255, 255, 220);
    private final Color lightSidebarColor = new Color(224, 242, 254, 200);

    // Shared Status/Accent Colors
    private final Color accentColor = new Color(59, 130, 246);       // Banking Blue
    private final Color accentHoverColor = new Color(37, 99, 235);
    private final Color goldAccent = new Color(245, 158, 11);        // Premium Gold
    private final Color successColor = new Color(16, 185, 129);      // Emerald Green
    private final Color errorColor = new Color(225, 29, 72);         // Rose Red
    private final Color warningColor = new Color(245, 158, 11);      // Amber

    // Typography
    private final Font displayFont = new Font("Segoe UI", Font.BOLD, 42);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 26);
    private final Font subHeaderFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 15);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 15);

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void toggleTheme() {
        darkMode = !darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    // Dynamic Colors
    public Color getGradientStart() { return darkMode ? darkGradientStart : lightGradientStart; }
    public Color getGradientEnd() { return darkMode ? darkGradientEnd : lightGradientEnd; }
    public Color getGlassColor() { return darkMode ? darkGlassColor : lightGlassColor; }
    public Color getGlassBorder() { return darkMode ? darkGlassBorder : lightGlassBorder; }
    public Color getTextPrimary() { return darkMode ? darkTextPrimary : lightTextPrimary; }
    public Color getTextSecondary() { return darkMode ? darkTextSecondary : lightTextSecondary; }
    public Color getInputBackground() { return darkMode ? darkInputBackground : lightInputBackground; }
    public Color getSidebarColor() { return darkMode ? darkSidebarColor : lightSidebarColor; }

    // Fixed Colors
    public Color getAccentColor() { return accentColor; }
    public Color getAccentHoverColor() { return accentHoverColor; }
    public Color getGoldAccent() { return goldAccent; }
    public Color getSuccessColor() { return successColor; }
    public Color getErrorColor() { return errorColor; }
    public Color getWarningColor() { return warningColor; }

    // Fonts
    public Font getDisplayFont() { return displayFont; }
    public Font getHeaderFont() { return headerFont; }
    public Font getSubHeaderFont() { return subHeaderFont; }
    public Font getNormalFont() { return normalFont; }
    public Font getBoldFont() { return boldFont; }
}