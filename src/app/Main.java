package app;

import atm.ATM;
import atm.ATMDashboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * Entry point for SmartATM2026.
 * Sets up the main window frame and launches the ATM Dashboard.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set CrossPlatformLookAndFeel.");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SmartATM 2026 - CodeSoft Java Internship");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(1000, 700));
            frame.setPreferredSize(new Dimension(1100, 750));

            ATM atmCore = new ATM();
            ATMDashboard dashboard = new ATMDashboard(atmCore);

            frame.setContentPane(dashboard);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}