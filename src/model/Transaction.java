package model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a single financial transaction.
 * Automatically records the current timestamp upon creation.
 */
public class Transaction {
    private final String date;
    private final String time;
    private final String type;
    private final double amount;
    private final double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        Date now = new Date();
        this.date = new SimpleDateFormat("dd MMM yyyy").format(now);
        this.time = new SimpleDateFormat("HH:mm:ss").format(now);
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
}