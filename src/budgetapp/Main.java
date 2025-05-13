package budgetapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Budget Management Application.
 */
public class Main {
    // Predefined template categories with default limits
    private static final List<BudgetCategory> TEMPLATE_CATEGORIES = Arrays.asList(
        new BudgetCategory("Food", 500.0),
        new BudgetCategory("Rent", 1000.0),
        new BudgetCategory("Transport", 200.0),
        new BudgetCategory("Utilities", 300.0),
        new BudgetCategory("Entertainment", 150.0)
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BudgetManager manager = new BudgetManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (true) {
            System.out.println("\n==== Budget Management Menu ====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Add Budget Category");
            System.out.println("4. Delete Budget Category");
            System.out.println("5. View Expenses by Category");
            System.out.println("6. View Expenses Above Amount");
            System.out.println("7. View Transactions by Date Range");
            System.out.println("8. Reset Category Expenditure");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.print("Enter income amount (e.g., 1000.00): ");
                        double incomeAmount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter source (e.g., Salary): ");
                        String source = scanner.nextLine();
                        System.out.print("Enter date and time (yyyy-MM-dd HH:mm, e.g., 2025-03-03 12:12): ");
                        LocalDateTime incomeDate = LocalDateTime.parse(scanner.nextLine(), formatter);
                        manager.addIncome(new Income(incomeAmount, incomeDate, source));
                        System.out.println("Income added!");
                        break;

                    case 2:
                        BudgetCategory selectedCategory = selectCategory(scanner, manager, true);
                        if (selectedCategory == null) {
                            break; // User chose not to create a new category
                        }
                        System.out.print("Enter expense amount (e.g., 50.00): ");
                        double expenseAmount = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter payment method (CASH, CARD, ALIPAY, WECHAT): ");
                        Expense.PaymentMethod method = Expense.PaymentMethod.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("Enter date and time (yyyy-MM-dd HH:mm, e.g., 2025-03-03 12:12): ");
                        LocalDateTime expenseDate = LocalDateTime.parse(scanner.nextLine(), formatter);
                        manager.addExpense(new Expense(expenseAmount, expenseDate, selectedCategory.getName(), method), selectedCategory);
                        System.out.println("Expense added!");
                        break;

                    case 3:
                        System.out.print("Enter category name for expenses (e.g., Food, Rent, Transport): ");
                        String newCategoryName = scanner.nextLine();
                        System.out.print("Enter monthly limit (e.g., 500.00): ");
                        double limit = Double.parseDouble(scanner.nextLine());
                        BudgetCategory newCategory = new BudgetCategory(newCategoryName, limit);
                        manager.addCategory(newCategory);
                        System.out.println("Category added!");
                        break;

                    case 4:
                        BudgetCategory categoryToDelete = selectCategory(scanner, manager, false);
                        if (categoryToDelete != null) {
                            manager.deleteCategory(categoryToDelete);
                            System.out.println("Category deleted!");
                        }
                        break;

                    case 5:
                        BudgetCategory categoryToView = selectCategory(scanner, manager, false);
                        if (categoryToView != null) {
                            System.out.println("\nExpenses for " + categoryToView.getName() + ":");
                            for (Expense e : manager.getExpensesByCategory(categoryToView)) {
                                System.out.println(e);
                            }
                        }
                        break;

                    case 6:
                        System.out.print("Enter minimum expense amount (e.g., 100.00): ");
                        double minAmount = Double.parseDouble(scanner.nextLine());
                        System.out.println("\nExpenses above $" + minAmount + ":");
                        for (Expense e : manager.getExpensesAboveAmount(minAmount)) {
                            System.out.println(e);
                        }
                        break;

                    case 7:
                        System.out.print("Enter start date (yyyy-MM-dd): ");
                        LocalDate start = LocalDate.parse(scanner.nextLine());
                        System.out.print("Enter end date (yyyy-MM-dd): ");
                        LocalDate end = LocalDate.parse(scanner.nextLine());
                        System.out.println("\nTransactions from " + start + " to " + end + ":");
                        for (Transaction t : manager.getTransactionsByDateRange(start, end)) {
                            System.out.println(t);
                        }
                        break;

                    case 8:
                        BudgetCategory categoryToReset = selectCategory(scanner, manager, false);
                        if (categoryToReset != null) {
                            categoryToReset.resetExpenditure();
                            System.out.println("Category expenditure reset!");
                        }
                        break;

                    case 9:
                        System.out.println("Exiting Budget Manager. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Try again.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format. Use yyyy-MM-dd HH:mm (e.g., 2025-03-03 12:12).");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (MonthlyLimitException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays available categories and lets the user select one.
     * @param scanner the Scanner for user input
     * @param manager the BudgetManager containing categories
     * @param allowCreate whether to allow creating a new category if none exist
     * @return the selected BudgetCategory, or null if none selected
     */
    private static BudgetCategory selectCategory(Scanner scanner, BudgetManager manager, boolean allowCreate) {
        List<BudgetCategory> categories = new ArrayList<>(manager.getCategories());
        
        if (categories.isEmpty()) {
            if (!allowCreate) {
                System.out.println("No categories available.");
                return null;
            }
            System.out.println("No categories exist. Choose a template category or create a custom one:");
            for (int i = 0; i < TEMPLATE_CATEGORIES.size(); i++) {
                BudgetCategory template = TEMPLATE_CATEGORIES.get(i);
                System.out.printf("%d. %s (Limit: $%.2f)%n", i + 1, template.getName(), template.getLimit());
            }
            System.out.printf("%d. Create custom category%n", TEMPLATE_CATEGORIES.size() + 1);
            System.out.print("Enter choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == TEMPLATE_CATEGORIES.size() + 1) {
                    System.out.print("Enter custom category name for expenses (e.g., Food, Rent, Transport): ");
                    String customName = scanner.nextLine();
                    System.out.print("Enter monthly limit (e.g., 500.00): ");
                    double customLimit = Double.parseDouble(scanner.nextLine());
                    BudgetCategory customCategory = new BudgetCategory(customName, customLimit);
                    manager.addCategory(customCategory);
                    return customCategory;
                } else if (choice >= 1 && choice <= TEMPLATE_CATEGORIES.size()) {
                    BudgetCategory selectedTemplate = TEMPLATE_CATEGORIES.get(choice - 1);
                    BudgetCategory newCategory = new BudgetCategory(selectedTemplate.getName(), selectedTemplate.getLimit());
                    manager.addCategory(newCategory);
                    return newCategory;
                } else {
                    System.out.println("Invalid choice.");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Try again.");
                return null;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Available categories:");
            for (int i = 0; i < categories.size(); i++) {
                BudgetCategory cat = categories.get(i);
                System.out.printf("%d. %s (Limit: $%.2f, Spent: $%.2f)%n", 
                    i + 1, cat.getName(), cat.getLimit(), cat.getSpentSoFar());
            }
            if (allowCreate) {
                System.out.printf("%d. Create new category%n", categories.size() + 1);
            }
            System.out.print("Enter choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (allowCreate && choice == categories.size() + 1) {
                    System.out.print("Enter new category name for expenses (e.g., Food, Rent, Transport): ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter monthly limit (e.g., 500.00): ");
                    double newLimit = Double.parseDouble(scanner.nextLine());
                    BudgetCategory newCategory = new BudgetCategory(newName, newLimit);
                    manager.addCategory(newCategory);
                    return newCategory;
                } else if (choice >= 1 && choice <= categories.size()) {
                    return categories.get(choice - 1);
                } else {
                    System.out.println("Invalid choice.");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Try again.");
                return null;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                return null;
            }
        }
    }
}