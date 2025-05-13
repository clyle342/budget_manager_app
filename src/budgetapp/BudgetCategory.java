// BudgetCategory.java
package budgetapp;

/**
 * Represents a budget category with a spending limit.
 */
public class BudgetCategory {
    private String name;
    private double limit;
    private double spentSoFar;

    /**
     * Constructs a BudgetCategory with a name and spending limit.
     * @param name the category name
     * @param limit the monthly spending limit
     * @throws IllegalArgumentException if name is null/empty or limit is negative
     */
    public BudgetCategory(String name, double limit) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("Limit cannot be negative");
        }
        this.name = name;
        this.limit = limit;
        this.spentSoFar = 0;
    }

    /**
     * Adds an expense to the category.
     * @param amount the expense amount
     * @throws IllegalArgumentException if amount is negative
     */
    public void addExpense(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Expense amount cannot be negative");
        }
        spentSoFar += amount;
    }

    /**
     * Resets the current expenditure to zero.
     */
    public void resetExpenditure() {
        spentSoFar = 0;
    }

    /**
     * Gets the category name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the monthly limit.
     * @return the limit
     */
    public double getLimit() {
        return limit;
    }

    /**
     * Gets the current expenditure.
     * @return the amount spent so far
     */
    public double getSpentSoFar() {
        return spentSoFar;
    }

    /**
     * Gets the remaining budget.
     * @return the remaining amount
     */
    public double getRemaining() {
        return limit - spentSoFar;
    }

    /**
     * Checks if expenditure exceeds the limit.
     * @return true if over limit
     */
    public boolean isOverLimit() {
        return spentSoFar > limit;
    }

    @Override
    public String toString() {
        return String.format("Category: %s, Limit: $%.2f, Spent: $%.2f", name, limit, spentSoFar);
    }
}