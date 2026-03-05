import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        // Main menu loop
        do {
            System.out.println("\n  🎰🎰 EXPENSE TRACKER 🎰🎰  \n");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Calculate Total");
            System.out.println("4. View Sorted Expenses");
            System.out.println("5. Delete Expense");
            System.out.println("6. Edit Expense"); // ✅ new feature
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                choice = -1; // invalid choice
            }

            // Handle menu options
            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> viewExpenses();
                case 3 -> calculateTotal();
                case 4 -> viewSortedExpenses(scanner);
                case 5 -> deleteExpense(scanner);
                case 6 -> editExpense(scanner); // ✅ new feature
                case 7 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7); // ✅ exit only when user chooses 7

        scanner.close();
    }

    // ------------------- ADD EXPENSE -------------------
    private static void addExpense(Scanner scanner) {
        try {
            System.out.print("Enter category: ");
            String category = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount < 0) {
                throw new IllegalArgumentException("Expense cannot be negative!");
            }

            // Append new expense to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(category + " - " + amount);
                writer.newLine();
                System.out.println("Expense added successfully!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount! Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // ------------------- VIEW EXPENSES -------------------
    private static void viewExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int index = 1; // ✅ numbering for easier delete/edit
            System.out.println("\n  🎰🎰 EXPENSE TRACKER 🎰🎰  \n");
            while ((line = reader.readLine()) != null) {
                System.out.println(index + ". " + line);
                index++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No expenses recorded yet.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // ------------------- CALCULATE TOTAL -------------------
    private static void calculateTotal() {
        double total = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) { // ✅ safety check
                    total += Double.parseDouble(parts[1]);
                }
            }
            System.out.println("Total Expenses: " + total);
        } catch (FileNotFoundException e) {
            System.out.println("No expenses recorded yet.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error calculating total: " + e.getMessage());
        }
    }

    // ------------------- VIEW SORTED EXPENSES -------------------
    private static void viewSortedExpenses(Scanner scanner) {
        List<String[]> expenses = new ArrayList<>();

        // Load expenses into a list
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    expenses.add(parts);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No expenses recorded yet.");
            return;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        // Ask user how to sort
        System.out.println("Sort by: 1. Category  2. Amount");
        int sortChoice;
        try {
            sortChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Defaulting to Category.");
            sortChoice = 1;
        }

        // Perform sorting
        if (sortChoice == 1) {
            expenses.sort(Comparator.comparing(arr -> arr[0])); // sort by category
        } else if (sortChoice == 2) {
            expenses.sort(Comparator.comparingDouble(arr -> Double.parseDouble(arr[1]))); // sort by amount
        }

        // Display sorted expenses
        System.out.println("\n  🎰🎰 SORTED EXPENSES 🎰🎰  \n");
        int index = 1;
        for (String[] exp : expenses) {
            System.out.println(index + ". " + exp[0] + " - " + exp[1]);
            index++;
        }
    }

    // ------------------- DELETE EXPENSE -------------------
    private static void deleteExpense(Scanner scanner) {
        List<String> expenses = loadExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        // Show expenses with numbering
        System.out.println("\n  🎰🎰 DELETE EXPENSE 🎰🎰  \n");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }

        // Ask user which expense to delete
        System.out.print("Enter the number of the expense to delete: ");
        int deleteIndex = getValidIndex(scanner, expenses.size());
        if (deleteIndex == -1) return;

        // Remove and rewrite file
        String removed = expenses.remove(deleteIndex);
        System.out.println("Deleted: " + removed);
        saveExpenses(expenses);
    }

    // ------------------- EDIT EXPENSE -------------------
    private static void editExpense(Scanner scanner) {
        List<String> expenses = loadExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        // Show expenses with numbering
        System.out.println("\n  🎰🎰 EDIT EXPENSE 🎰🎰  \n");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }

        // Ask user which expense to edit
        System.out.print("Enter the number of the expense to edit: ");
        int editIndex = getValidIndex(scanner, expenses.size());
        if (editIndex == -1) return;

        // Ask for new values
        System.out.print("Enter new category: ");
        String newCategory = scanner.nextLine();
        System.out.print("Enter new amount: ");
        double newAmount;
        try {
            newAmount = Double.parseDouble(scanner.nextLine());
            if (newAmount < 0) {
                System.out.println("Expense cannot be negative!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
            return;
        }

        // Update expense
        String updated = newCategory + " - " + newAmount;
        expenses.set(editIndex, updated);
        System.out.println("Updated expense: " + updated);

        // Rewrite file with updated list
        saveExpenses(expenses);
    }

    // ------------------- HELPER METHODS -------------------

    // Load all expenses from the file into a list
    private static List<String> loadExpenses() {
        List<String> expenses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expenses.add(line);
            }
        } catch (IOException ignored) {
            // If file not found or error, just return empty list
        }
        return expenses;
    }

    // Save all expenses back to the file (overwrite mode)
    private static void saveExpenses(List<String> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String exp : expenses) {
                writer.write(exp);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }

    // Validate user input for selecting an expense index
    private static int getValidIndex(Scanner scanner, int size) {
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1; // convert to 0-based index
            if (index < 0 || index >= size) {
                System.out.println("Invalid choice!");
                return -1; // signal invalid
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return -1; // signal invalid
        }
    }
}
