package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a user's Bank Account within the SmartATM2026 system.
 * Manages balance, credentials, and transaction history.
 */
public class BankAccount {
    private final String accountNumber;
    private final String holderName;
    private String pin;
    private double balance;
    private final List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String holderName, String initialPin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = initialPin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();

        if (initialBalance > 0) {
            addTransaction(new Transaction("ACCOUNT OPENING", initialBalance, this.balance));
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void setPin(String newPin) {
        this.pin = newPin;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            addTransaction(new Transaction("DEPOSIT", amount, this.balance));
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            addTransaction(new Transaction("WITHDRAWAL", -amount, this.balance));
            return true;
        }
        return false;
    }

    public boolean transferOut(double amount, String targetAccount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            addTransaction(new Transaction("TRANSFER TO " + targetAccount, -amount, this.balance));
            return true;
        }
        return false;
    }

    public void transferIn(double amount, String sourceAccount) {
        if (amount > 0) {
            this.balance += amount;
            addTransaction(new Transaction("TRANSFER FROM " + sourceAccount, amount, this.balance));
        }
    }

    private void addTransaction(Transaction t) {
        this.transactionHistory.add(t);
    }

    public List<Transaction> getTransactionHistory() {
        List<Transaction> reversed = new ArrayList<>(transactionHistory);
        Collections.reverse(reversed); // Latest first
        return reversed;
    }
}