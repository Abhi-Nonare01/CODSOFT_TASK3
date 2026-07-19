package atm;

import model.BankAccount;

import java.util.HashMap;
import java.util.Map;

/**
 * The core controller of the SmartATM2026 system.
 * Handles dynamic session management and dynamic account creation.
 */
public class ATM {
    private final Map<String, BankAccount> database;
    private BankAccount activeSession;

    public ATM() {
        this.database = new HashMap<>();
    }

    /**
     * Authenticates the user or creates a new account dynamically if it doesn't exist.
     *
     * @param accNo The account number provided by the user.
     * @param name The account holder's name.
     * @param pin The 4-digit PIN.
     * @return true if login is successful or account is created, false if PIN is wrong for an existing account.
     */
    public boolean authenticate(String accNo, String name, String pin) {
        // If account already exists, verify the PIN
        if (database.containsKey(accNo)) {
            BankAccount existingAcc = database.get(accNo);
            if (existingAcc.verifyPin(pin)) {
                this.activeSession = existingAcc;
                return true;
            }
            return false; // Wrong PIN for existing account
        }
        // If account does not exist, create a new one dynamically with a demo balance
        else {
            BankAccount newAccount = new BankAccount(accNo, name, pin, 25000.00);
            database.put(accNo, newAccount);
            this.activeSession = newAccount;
            return true;
        }
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