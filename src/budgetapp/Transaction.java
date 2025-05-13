// Transaction.java
package budgetapp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base class for all transactions (income or expense).
 */
public abstract class Transaction implements Comparable<Transaction> {
    protected UUID id;
    protected LocalDateTime dateTime;
    protected double amount;

    /**
     * Constructs a Transaction with the specified amount and date/time.
     * @param amount the transaction amount (must be positive)
     * @param dateTime the date and time of the transaction (must not be null)
     * @throws IllegalArgumentException if amount is not positive or dateTime is null
     */
    public Transaction(double amount, LocalDateTime dateTime) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (dateTime == null) throw new IllegalArgumentException("DateTime cannot be null");
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.dateTime = dateTime;
    }

    /**
     * Gets the unique identifier of the transaction.
     * @return the UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the date and time of the transaction.
     * @return the LocalDateTime
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets the transaction amount.
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the effective amount of the transaction (positive for income, negative for expense).
     * @return the effective amount
     */
    public abstract double getEffectiveAmount();

    /**
     * Compares transactions by date and time for sorting.
     * @param other the other transaction
     * @return comparison result
     */
    @Override
    public int compareTo(Transaction other) {
        return this.dateTime.compareTo(other.dateTime);
    }
}