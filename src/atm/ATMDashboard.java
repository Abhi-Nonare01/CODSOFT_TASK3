package atm;

import model.Transaction;
import ui.GlassPanel;
import ui.GradientPanel;
import ui.RoundedButton;
import ui.RoundedTextField;
import utils.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Graphical User Interface for the SmartATM2026 application.
 * Manages the Login view, Dashboard Layout, and all banking operations.
 */
public class ATMDashboard extends GradientPanel {

    private final ATM atmCore;
    private final CardLayout mainCardLayout;
    private final JPanel mainContainer;

    // Login Components
    private RoundedTextField pinField;

    // Dashboard Components
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JLabel headerNameLabel;
    private JLabel headerAccLabel;
    private JLabel headerBalanceLabel;

    private final CardLayout contentCardLayout;
    private final JPanel contentContainer;

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public ATMDashboard(ATM atmCore) {
        this.atmCore = atmCore;
        this.setLayout(new BorderLayout());

        mainCardLayout = new CardLayout();
        mainContainer = new JPanel(mainCardLayout);
        mainContainer.setOpaque(false);

        contentCardLayout = new CardLayout();
        contentContainer = new JPanel(contentCardLayout);
        contentContainer.setOpaque(false);

        mainContainer.add(createLoginScreen(), "LOGIN");
        mainContainer.add(createMainDashboard(), "DASHBOARD");

        this.add(mainContainer, BorderLayout.CENTER);

        startClock();
        mainCardLayout.show(mainContainer, "LOGIN");
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            if (timeLabel != null) timeLabel.setText(new SimpleDateFormat("hh:mm:ss a").format(now));
            if (dateLabel != null) dateLabel.setText(new SimpleDateFormat("EEEE, dd MMM yyyy").format(now));
        });
        timer.start();
    }

    /* =========================================
     * LOGIN SCREEN
     * ========================================= */
    private JPanel createLoginScreen() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel loginCard = new GlassPanel(30);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(new EmptyBorder(50, 60, 50, 60));
        loginCard.setPreferredSize(new Dimension(450, 480));

        // Removed Emoji, using clean text
        JLabel logo = new JLabel("SmartATM", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        logo.setForeground(ThemeManager.getInstance().getAccentColor());
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Welcome Back");
        title.setFont(ThemeManager.getInstance().getHeaderFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Secure Banking Gateway");
        subtitle.setFont(ThemeManager.getInstance().getNormalFont());
        subtitle.setForeground(ThemeManager.getInstance().getTextSecondary());
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        pinField = new RoundedTextField(4, "Enter 4-Digit PIN", true);
        pinField.setMaximumSize(new Dimension(280, 50)); // Widened slightly
        pinField.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pinField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton loginBtn = new RoundedButton("Access Account", ThemeManager.getInstance().getAccentColor(), ThemeManager.getInstance().getAccentHoverColor());
        loginBtn.setMaximumSize(new Dimension(280, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> processLogin());

        RoundedButton exitBtn = new RoundedButton("Exit Terminal", ThemeManager.getInstance().getErrorColor(), ThemeManager.getInstance().getErrorColor().brighter());
        exitBtn.setMaximumSize(new Dimension(280, 45));
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.addActionListener(e -> System.exit(0));

        RoundedButton themeBtn = new RoundedButton("Toggle Theme", new Color(71, 85, 105), new Color(100, 116, 139));
        themeBtn.setMaximumSize(new Dimension(150, 35));
        themeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeBtn.setFont(ThemeManager.getInstance().getNormalFont());
        themeBtn.addActionListener(e -> toggleTheme());

        loginCard.add(logo);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCard.add(title);
        loginCard.add(Box.createRigidArea(new Dimension(0, 5)));
        loginCard.add(subtitle);
        loginCard.add(Box.createRigidArea(new Dimension(0, 40)));
        loginCard.add(pinField);
        loginCard.add(Box.createRigidArea(new Dimension(0, 25)));
        loginCard.add(loginBtn);
        loginCard.add(Box.createRigidArea(new Dimension(0, 15)));
        loginCard.add(exitBtn);
        loginCard.add(Box.createRigidArea(new Dimension(0, 30)));
        loginCard.add(themeBtn);

        wrapper.add(loginCard);

        // Default PIN hint
        JLabel hint = new JLabel("Demo PIN: 1234");
        hint.setForeground(ThemeManager.getInstance().getTextSecondary());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        wrapper.add(hint, gbc);

        return wrapper;
    }

    private void processLogin() {
        String pin = new String(pinField.getPassword()).trim();
        if (pin.isEmpty()) {
            showError("PIN cannot be empty.");
            return;
        }

        if (atmCore.authenticate(pin)) {
            pinField.setText("");
            updateDashboardHeader();
            loadHomeView();
            mainCardLayout.show(mainContainer, "DASHBOARD");
        } else {
            showError("Invalid PIN. Access Denied.");
            pinField.setText("");
        }
    }

    /* =========================================
     * DASHBOARD LAYOUT
     * ========================================= */
    private JPanel createMainDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(15, 15));
        dashboard.setOpaque(false);
        dashboard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // SIDEBAR
        GlassPanel sidebar = new GlassPanel(20, false);
        sidebar.setCustomGlassColor(ThemeManager.getInstance().getSidebarColor());
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, 0)); // Slightly adjusted width
        sidebar.setBorder(new EmptyBorder(25, 20, 25, 20));

        // Removed Emoji, used pure clean typography
        JLabel logo = new JLabel("SmartATM");
        logo.setFont(ThemeManager.getInstance().getHeaderFont());
        logo.setForeground(ThemeManager.getInstance().getTextPrimary());
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel = new JLabel("Date");
        dateLabel.setFont(ThemeManager.getInstance().getNormalFont());
        dateLabel.setForeground(ThemeManager.getInstance().getTextSecondary());
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeLabel = new JLabel("Time");
        timeLabel.setFont(ThemeManager.getInstance().getBoldFont());
        timeLabel.setForeground(ThemeManager.getInstance().getGoldAccent());
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(dateLabel);
        sidebar.add(timeLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Removed Emojis from buttons to ensure it works cross-platform
        sidebar.add(createMenuButton("Home", "HOME"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("Withdraw", "WITHDRAW"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("Deposit", "DEPOSIT"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("Fast Cash", "FAST_CASH"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("Transfer", "TRANSFER"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("Statement", "STATEMENT"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createMenuButton("Change PIN", "CHANGE_PIN"));

        sidebar.add(Box.createVerticalGlue());

        RoundedButton logoutBtn = new RoundedButton("Logout", ThemeManager.getInstance().getErrorColor(), ThemeManager.getInstance().getErrorColor().brighter(), 15);
        logoutBtn.setMaximumSize(new Dimension(250, 45));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            atmCore.logout();
            mainCardLayout.show(mainContainer, "LOGIN");
        });
        sidebar.add(logoutBtn);

        // HEADER
        GlassPanel header = new GlassPanel(20, false);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);

        headerNameLabel = new JLabel("Welcome, User");
        headerNameLabel.setFont(ThemeManager.getInstance().getHeaderFont());
        headerNameLabel.setForeground(ThemeManager.getInstance().getTextPrimary());

        headerAccLabel = new JLabel("A/C: XXXX-XXXX");
        headerAccLabel.setFont(ThemeManager.getInstance().getNormalFont());
        headerAccLabel.setForeground(ThemeManager.getInstance().getTextSecondary());

        headerLeft.add(headerNameLabel);
        headerLeft.add(headerAccLabel);

        JPanel headerRight = new JPanel(new BorderLayout());
        headerRight.setOpaque(false);
        JLabel balTitle = new JLabel("Available Balance", SwingConstants.RIGHT);
        balTitle.setFont(ThemeManager.getInstance().getNormalFont());
        balTitle.setForeground(ThemeManager.getInstance().getTextSecondary());

        headerBalanceLabel = new JLabel("₹0.00", SwingConstants.RIGHT);
        headerBalanceLabel.setFont(ThemeManager.getInstance().getDisplayFont());
        headerBalanceLabel.setForeground(ThemeManager.getInstance().getSuccessColor());

        headerRight.add(balTitle, BorderLayout.NORTH);
        headerRight.add(headerBalanceLabel, BorderLayout.CENTER);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // CONTENT AREA Setup
        contentContainer.add(createHomeView(), "HOME");
        contentContainer.add(createWithdrawView(), "WITHDRAW");
        contentContainer.add(createDepositView(), "DEPOSIT");
        contentContainer.add(createFastCashView(), "FAST_CASH");
        contentContainer.add(createTransferView(), "TRANSFER");
        contentContainer.add(createStatementView(), "STATEMENT");
        contentContainer.add(createChangePinView(), "CHANGE_PIN");

        JPanel rightWrapper = new JPanel(new BorderLayout(0, 15));
        rightWrapper.setOpaque(false);
        rightWrapper.add(header, BorderLayout.NORTH);
        rightWrapper.add(contentContainer, BorderLayout.CENTER);

        dashboard.add(sidebar, BorderLayout.WEST);
        dashboard.add(rightWrapper, BorderLayout.CENTER);

        return dashboard;
    }

    private RoundedButton createMenuButton(String text, String cardName) {
        RoundedButton btn = new RoundedButton(text, new Color(51, 65, 85, 150), ThemeManager.getInstance().getAccentColor(), 15);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setHorizontalAlignment(SwingConstants.CENTER); // Changed to center alignment for cleaner look
        btn.setBorder(new EmptyBorder(0, 0, 0, 0));
        btn.setFont(ThemeManager.getInstance().getBoldFont());
        btn.addActionListener(e -> {
            if (cardName.equals("STATEMENT")) {
                updateStatementView();
            }
            contentCardLayout.show(contentContainer, cardName);
        });
        return btn;
    }

    private void updateDashboardHeader() {
        if (atmCore.isSessionActive()) {
            headerNameLabel.setText("Welcome, " + atmCore.getActiveAccount().getHolderName());
            String acc = atmCore.getActiveAccount().getAccountNumber();
            headerAccLabel.setText("A/C: ****" + acc.substring(acc.length() - 4));
            headerBalanceLabel.setText(currencyFormatter.format(atmCore.getActiveAccount().getBalance()));
        }
    }

    /* =========================================
     * OPERATION VIEWS
     * ========================================= */

    private void loadHomeView() {
        contentCardLayout.show(contentContainer, "HOME");
    }

    private JPanel createHomeView() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("Dashboard Overview", SwingConstants.CENTER);
        title.setFont(ThemeManager.getInstance().getDisplayFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());

        JLabel msg = new JLabel("Please select an operation from the sidebar menu to proceed.");
        msg.setFont(ThemeManager.getInstance().getSubHeaderFont());
        msg.setForeground(ThemeManager.getInstance().getTextSecondary());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; panel.add(title, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(20,0,0,0); panel.add(msg, gbc);
        return panel;
    }

    private JPanel createWithdrawView() {
        // Shortened placeholder and widened field to prevent text cut-off
        return createInputForm("Withdraw Funds", "Amount to Withdraw (₹)", "Withdraw", e -> {
            RoundedTextField inputField = (RoundedTextField) ((JButton)e.getSource()).getClientProperty("inputField");
            try {
                double amount = Double.parseDouble(inputField.getText().trim());
                if (amount <= 0) {
                    showError("Amount must be greater than zero.");
                } else if (atmCore.getActiveAccount().withdraw(amount)) {
                    showSuccess("Successfully withdrawn " + currencyFormatter.format(amount));
                    inputField.setText("");
                    updateDashboardHeader();
                } else {
                    showError("Insufficient Balance.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid amount.");
            }
        });
    }

    private JPanel createDepositView() {
        return createInputForm("Deposit Funds", "Amount to Deposit (₹)", "Deposit", e -> {
            RoundedTextField inputField = (RoundedTextField) ((JButton)e.getSource()).getClientProperty("inputField");
            try {
                double amount = Double.parseDouble(inputField.getText().trim());
                if (amount <= 0) {
                    showError("Amount must be greater than zero.");
                } else {
                    atmCore.getActiveAccount().deposit(amount);
                    showSuccess("Successfully deposited " + currencyFormatter.format(amount));
                    inputField.setText("");
                    updateDashboardHeader();
                }
            } catch (NumberFormatException ex) {
                showError("Invalid amount.");
            }
        });
    }

    private JPanel createTransferView() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(500, 350));

        JLabel title = new JLabel("Transfer Funds");
        title.setFont(ThemeManager.getInstance().getHeaderFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Increased field widths to 400px to ensure text fits
        RoundedTextField accField = new RoundedTextField(15, "Target Account No.", false);
        accField.setMaximumSize(new Dimension(400, 45));
        accField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedTextField amountField = new RoundedTextField(15, "Transfer Amount (₹)", false);
        amountField.setMaximumSize(new Dimension(400, 45));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton submitBtn = new RoundedButton("Send Money");
        submitBtn.setMaximumSize(new Dimension(400, 45));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> {
            try {
                String targetAcc = accField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());

                if (targetAcc.isEmpty()) {
                    showError("Target account cannot be empty.");
                    return;
                }

                if (amount <= 0) {
                    showError("Amount must be greater than zero.");
                } else if (atmCore.getActiveAccount().transferOut(amount, targetAcc)) {
                    showSuccess("Successfully transferred " + currencyFormatter.format(amount) + " to " + targetAcc);
                    accField.setText("");
                    amountField.setText("");
                    updateDashboardHeader();
                } else {
                    showError("Insufficient Balance.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid amount.");
            }
        });

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(accField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(amountField);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(submitBtn);

        wrapper.add(card);
        return wrapper;
    }

    private JPanel createChangePinView() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(500, 350));

        JLabel title = new JLabel("Change Security PIN");
        title.setFont(ThemeManager.getInstance().getHeaderFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedTextField oldPinField = new RoundedTextField(15, "Enter Current PIN", true);
        oldPinField.setMaximumSize(new Dimension(400, 45));
        oldPinField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedTextField newPinField = new RoundedTextField(15, "Enter New PIN", true);
        newPinField.setMaximumSize(new Dimension(400, 45));
        newPinField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton submitBtn = new RoundedButton("Update PIN", ThemeManager.getInstance().getWarningColor(), ThemeManager.getInstance().getWarningColor().brighter());
        submitBtn.setMaximumSize(new Dimension(400, 45));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> {
            String oldPin = new String(oldPinField.getPassword());
            String newPin = new String(newPinField.getPassword());

            if(oldPin.isEmpty() || newPin.isEmpty()) {
                showError("Fields cannot be empty.");
                return;
            }

            if(atmCore.getActiveAccount().verifyPin(oldPin)) {
                atmCore.getActiveAccount().setPin(newPin);
                showSuccess("PIN changed successfully!");
                oldPinField.setText("");
                newPinField.setText("");
            } else {
                showError("Incorrect current PIN.");
            }
        });

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(oldPinField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(newPinField);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(submitBtn);

        wrapper.add(card);
        return wrapper;
    }

    private JPanel createFastCashView() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(600, 400));

        JLabel title = new JLabel("Fast Cash");
        title.setFont(ThemeManager.getInstance().getHeaderFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel grid = new JPanel(new GridLayout(3, 2, 20, 20));
        grid.setOpaque(false);

        int[] amounts = {500, 1000, 2000, 5000, 10000, 20000};
        for (int amount : amounts) {
            RoundedButton btn = new RoundedButton("₹ " + amount);
            btn.setFont(ThemeManager.getInstance().getHeaderFont());
            btn.addActionListener(e -> {
                if (atmCore.getActiveAccount().withdraw(amount)) {
                    showSuccess("Please collect your cash: ₹ " + amount);
                    updateDashboardHeader();
                } else {
                    showError("Insufficient Balance.");
                }
            });
            grid.add(btn);
        }

        card.add(grid);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel statementContainer;

    private JPanel createStatementView() {
        statementContainer = new JPanel(new BorderLayout());
        statementContainer.setOpaque(false);
        return statementContainer;
    }

    private void updateStatementView() {
        statementContainer.removeAll();

        GlassPanel card = new GlassPanel(20);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Mini Statement");
        title.setFont(ThemeManager.getInstance().getHeaderFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        for (Transaction t : atmCore.getActiveAccount().getTransactionHistory()) {
            GlassPanel row = new GlassPanel(10, false);
            row.setLayout(new BorderLayout());
            row.setBorder(new EmptyBorder(10, 15, 10, 15));
            row.setMaximumSize(new Dimension(800, 60));

            JPanel left = new JPanel(new GridLayout(2, 1));
            left.setOpaque(false);
            JLabel type = new JLabel(t.getType());
            type.setFont(ThemeManager.getInstance().getBoldFont());
            type.setForeground(ThemeManager.getInstance().getTextPrimary());

            JLabel dt = new JLabel(t.getDate() + " " + t.getTime());
            dt.setFont(ThemeManager.getInstance().getNormalFont());
            dt.setForeground(ThemeManager.getInstance().getTextSecondary());
            left.add(type);
            left.add(dt);

            JPanel right = new JPanel(new GridLayout(2, 1));
            right.setOpaque(false);

            JLabel amt = new JLabel((t.getAmount() > 0 ? "+" : "") + currencyFormatter.format(t.getAmount()), SwingConstants.RIGHT);
            amt.setFont(ThemeManager.getInstance().getBoldFont());
            amt.setForeground(t.getAmount() > 0 ? ThemeManager.getInstance().getSuccessColor() : ThemeManager.getInstance().getErrorColor());

            JLabel bal = new JLabel("Bal: " + currencyFormatter.format(t.getBalanceAfter()), SwingConstants.RIGHT);
            bal.setFont(ThemeManager.getInstance().getNormalFont());
            bal.setForeground(ThemeManager.getInstance().getTextSecondary());

            right.add(amt);
            right.add(bal);

            row.add(left, BorderLayout.WEST);
            row.add(right, BorderLayout.EAST);

            listPanel.add(row);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = ThemeManager.getInstance().getGlassBorder();
                this.trackColor = new Color(0,0,0,0);
            }
            @Override protected JButton createDecreaseButton(int orientation) { return createZeroBtn(); }
            @Override protected JButton createIncreaseButton(int orientation) { return createZeroBtn(); }
            private JButton createZeroBtn() { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;}
        });

        card.add(scrollPane, BorderLayout.CENTER);
        statementContainer.add(card, BorderLayout.CENTER);
        statementContainer.revalidate();
        statementContainer.repaint();
    }

    private JPanel createInputForm(String titleText, String placeholder, String btnText, java.awt.event.ActionListener action) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        GlassPanel card = new GlassPanel(25);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(50, 60, 50, 60));
        card.setPreferredSize(new Dimension(500, 300));

        JLabel title = new JLabel(titleText);
        title.setFont(ThemeManager.getInstance().getHeaderFont());
        title.setForeground(ThemeManager.getInstance().getTextPrimary());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Increased field width to 400px so long placeholders won't be cut off
        RoundedTextField inputField = new RoundedTextField(15, placeholder, false);
        inputField.setMaximumSize(new Dimension(400, 50));
        inputField.setFont(ThemeManager.getInstance().getHeaderFont());
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton submitBtn = new RoundedButton(btnText);
        submitBtn.setMaximumSize(new Dimension(400, 45));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.putClientProperty("inputField", inputField);
        submitBtn.addActionListener(action);

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        card.add(inputField);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(submitBtn);

        wrapper.add(card);
        return wrapper;
    }

    private void showSuccess(String message) {
        UIManager.put("OptionPane.background", ThemeManager.getInstance().getGradientStart());
        UIManager.put("Panel.background", ThemeManager.getInstance().getGradientStart());
        UIManager.put("OptionPane.messageForeground", ThemeManager.getInstance().getSuccessColor());
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        UIManager.put("OptionPane.background", ThemeManager.getInstance().getGradientStart());
        UIManager.put("Panel.background", ThemeManager.getInstance().getGradientStart());
        UIManager.put("OptionPane.messageForeground", ThemeManager.getInstance().getErrorColor());
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void toggleTheme() {
        ThemeManager.getInstance().toggleTheme();
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        repaint();
    }
}