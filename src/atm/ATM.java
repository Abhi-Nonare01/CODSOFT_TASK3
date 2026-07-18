package atm;

import model.BankAccount;

import java.util.HashMap;
import java.util.Map;

/**
 * The core controller of the SmartATM2026 system.
 * Simulates a bank database and handles session management.
 */
public class ATM {
    private final Map<String, BankAccount> database;
    private BankAccount activeSession;

    public ATM() {
        this.database = new HashMap<>();
        initializeMockData();
    }

    /**
     * Pre-loads the system with a demo account.
     */
    private void initializeMockData() {
        // Default demo account: PIN 1234
        BankAccount demoAccount = new BankAccount("4598210045", "John Doe", "1234", 25000.00);

        // Add some mock history
        demoAccount.withdraw(2000.0);
        demoAccount.deposit(5000.0);

        database.put("1234", demoAccount);
    }

    /**
     * Authenticates the user based on the PIN.
     * In a real system, it would require Card No + PIN.
     */
    public boolean authenticate(String pin) {
        BankAccount acc = database.get(pin);
        if (acc != null && acc.verifyPin(pin)) {
            this.activeSession = acc;
            return true;
        }
        return false;
    }

    public void logout() {
        this.activeSession = null;
    }

    public BankAccount getActiveAccount() {
        return activeSession;
    }

    public boolean isSessionActive() {
        return activeSession != null;
    }
}