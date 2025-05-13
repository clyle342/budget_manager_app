// BudgetManager.java
package budgetapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages an entity's budget, including transactions and budget categories.
 */
public class BudgetManager {
    // Using HashMap to associate categories with expenses for efficient retrieval,
    // and ArrayList for income to maintain order and allow sorting.
    private Map<BudgetCategory, List<Expense>> expenses;
    private List<Income> incomes;

    /**
     * Constructs a BudgetManager with no transactions or categories.
     */
    public BudgetManager() {
        expenses = new HashMap<>();
        incomes = new ArrayList<>();
    }

    /**
     * Adds a budget category.
     * @param category the category to add
     * @throws IllegalArgumentException if category is null
     */
    public void addCategory(BudgetCategory category) {
        if (category == null) throw new IllegalArgumentException("Category cannot be null");
        expenses.putIfAbsent(category, new ArrayList<>());
    }

    /**
     * Deletes a budget category and its expenses.
     * @param category the category to delete
     */
    public void deleteCategory(BudgetCategory category) {
        expenses.remove(category);
    }

    /**
     * Adds an income transaction.
     * @param income the income to add
     * @throws IllegalArgumentException if income is null
     */
    public void addIncome(Income income) {
        if (income == null) throw new IllegalArgumentException("Income cannot be null");
        incomes.add(income);
    }

    /**
     * Adds an expense to a category.
     * @param expense the expense to add
     * @param category the budget category
     * @throws MonthlyLimitException if adding expense exceeds category limit
     * @throws IllegalArgumentException if expense or category is null
     */
    public void addExpense(Expense expense, BudgetCategory category) throws MonthlyLimitException {
        if (expense == null) throw new IllegalArgumentException("Expense cannot be null");
        if (category == null) throw new IllegalArgumentException("Category cannot be null");
        double newSpent = category.getSpentSoFar() + Math.abs(expense.getEffectiveAmount());
        if (newSpent > category.getLimit()) {
            throw new MonthlyLimitException("Expense exceeds monthly limit for " + category.getName());
        }
        category.addExpense(Math.abs(expense.getEffectiveAmount()));
        expenses.computeIfAbsent(category, k -> new ArrayList<>()).add(expense);
    }

    /**
     * Gets all budget categories.
     * @return set of categories
     */
    public Set<BudgetCategory> getCategories() {
        return expenses.keySet();
    }

    /**
     * Gets expenses for a category.
     * @param category the budget category
     * @return sorted list of expenses
     */
    public List<Expense> getExpensesByCategory(BudgetCategory category) {
        return expenses.getOrDefault(category, new ArrayList<>()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Gets expenses exceeding a specified amount.
     * @param amount the threshold amount
     * @return sorted list of expenses
     */
    public List<Expense> getExpensesAboveAmount(double amount) {
        List<Expense> result = new ArrayList<>();
        for (List<Expense> expenseList : expenses.values()) {
            for (Expense e : expenseList) {
                if (Math.abs(e.getEffectiveAmount()) > amount) {
                    result.add(e);
                }
            }
        }
        return result.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Gets transactions within a date range.
     * @param startDate the start date
     * @param endDate the end date
     * @return sorted list of transactions
     */
    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Transaction> result = new ArrayList<>();
        result.addAll(incomes.stream()
                .filter(i -> !i.getDateTime().toLocalDate().isBefore(startDate) &&
                             !i.getDateTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList()));
        expenses.values().forEach(list -> result.addAll(list.stream()
                .filter(e -> !e.getDateTime().toLocalDate().isBefore(startDate) &&
                             !e.getDateTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList())));
        return result.stream().sorted().collect(Collectors.toList());
    }
}