// Income.java
package budgetapp;

import java.time.LocalDateTime;

/**
 * Represents money received (e.g., salary, gift).
 */
public class Income extends Transaction {
    private String source;

    /**
     * Constructs an Income transaction.
     * @param amount the income amount
     * @param dateTime the date and time
     * @param source the source of income (e.g., Salary)
     * @throws IllegalArgumentException if source is null or empty
     */
    public Income(double amount, LocalDateTime dateTime, String source) {
        super(amount, dateTime);
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        this.source = source;
    }

    /**
     * Gets the source of the income.
     * @return the source
     */
    public String getSource() {
        return source;
    }

    @Override
    public double getEffectiveAmount() {
        return amount; // No fees for income
    }

    @Override
    public String toString() {
        return String.format("Income from %s of $%.2f on %s", source, amount, dateTime);
    }
}