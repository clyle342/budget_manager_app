// Expense.java
package budgetapp;

import java.time.LocalDateTime;

/**
 * Represents money spent (e.g., rent, groceries).
 */
public class Expense extends Transaction {
    public enum PaymentMethod {
        CASH(0.0), CARD(0.01), ALIPAY(0.005), WECHAT(0.005);

        private final double feeRate;

        PaymentMethod(double feeRate) {
            this.feeRate = feeRate;
        }

        public double getFeeRate() {
            return feeRate;
        }
    }

    private String category;
    private PaymentMethod paymentMethod;

    /**
     * Constructs an Expense transaction.
     * @param amount the expense amount
     * @param dateTime the date and time
     * @param category the expense category
     * @param paymentMethod the payment method
     * @throws IllegalArgumentException if category is null/empty or paymentMethod is null
     */
    public Expense(double amount, LocalDateTime dateTime, String category, PaymentMethod paymentMethod) {
        super(amount, dateTime);
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        this.category = category;
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the category of the expense.
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the payment method.
     * @return the payment method
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public double getEffectiveAmount() {
        double fee = amount * paymentMethod.getFeeRate();
        return -(amount + fee); // Deduct amount plus fee
    }

    @Override
    public String toString() {
        return String.format("Expense on %s of $%.2f (%s) on %s", category, amount, paymentMethod, dateTime);
    }
}