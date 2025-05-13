// MonthlyLimitException.java
package budgetapp;

/**
 * Exception thrown when an expense exceeds a budget category's monthly limit.
 */
public class MonthlyLimitException extends Exception {
    /**
     * Constructs a MonthlyLimitException with a message.
     * @param message the error message
     */
    public MonthlyLimitException(String message) {
        super(message);
    }
}