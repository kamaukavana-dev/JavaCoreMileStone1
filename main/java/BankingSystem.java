import java.util.ArrayList;
import java.util.Scanner;

class BankAccount {
    private final int accountNumber;
    private int balance;
    private final String accountName;
    private final int password;

    BankAccount(int accountNumber, int balance, String accountName, int password) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountName = accountName;
        this.password = password;
    }

    public int getAccountNumber() { return accountNumber; }
    public int getBalance() { return balance; }
    public String getAccountName() { return accountName; }
    public int getPassword() { return password; }

    public void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
            System.out.println("New balance: $" + balance);
        } else {
            System.out.println("Deposit amount must be above zero!");
        }
    }

    public void withdraw(int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: $" + amount);
            System.out.println("New balance: $" + balance);
        } else {
            System.out.println("Insufficient balance or invalid amount!");
        }
    }

    public void transfer(BankAccount target, int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            target.balance += amount;
            System.out.println("Transferred $" + amount + " to " + target.getAccountName());
            System.out.println("Your new balance: $" + balance);
        } else {
            System.out.println("Transfer failed: insufficient funds or invalid amount.");
        }
    }
}

class Bank {
    private final ArrayList<BankAccount> accounts = new ArrayList<>();

    public void createAccount(int accountNumber, int balance, String accountName, int password) {
        accounts.add(new BankAccount(accountNumber, balance, accountName, password));
        System.out.println("Account created successfully!");
    }

    public BankAccount findAccount(int accountNumber) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }

    public BankAccount login(int accountNumber, int password) {
        BankAccount acc = findAccount(accountNumber);
        if (acc != null && acc.getPassword() == password) {
            System.out.println("Login successful! Welcome, " + acc.getAccountName());
            return acc;
        }
        System.out.println("Login failed! Wrong credentials.");
        return null;
    }

    public void listAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }
        for (BankAccount acc : accounts) {
            System.out.println("Account: " + acc.getAccountNumber() +
                    " | Holder: " + acc.getAccountName() +
                    " | Balance: $" + acc.getBalance());
        }
    }
}

public class BankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();

        // Pre-create some accounts
        bank.createAccount(111222, 80000, "Daniel Maina", 1234);
        bank.createAccount(555222, 500000, "John Maina", 4321);
        bank.createAccount(444222, 720000, "Esther Mwaniki", 7878);

        System.out.println("""
                
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                                 🏦 WELCOME TO EQUITY BANK 🏦
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                """);

        BankAccount activeAccount = null;

        boolean isRunning = true;
        while (isRunning) {
            // If no active account, force login
            if (activeAccount == null) {
                System.out.print("Enter account number: ");
                int accNo = scanner.nextInt();
                System.out.print("Enter password: ");
                int password = scanner.nextInt();
                activeAccount = bank.login(accNo, password);
                if (activeAccount == null) {
                    continue; // retry login
                }
            }

            System.out.println("""
                    1) Check Balance
                    2) Deposit
                    3) Withdraw
                    4) Transfer
                    5) Create New Account
                    6) List All Accounts
                    7) Switch Account
                    8) Exit
                    """);
            System.out.print("Choose option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1 -> System.out.println("Balance: $" + activeAccount.getBalance());
                case 2 -> {
                    System.out.print("Enter amount to deposit: ");
                    int amount = scanner.nextInt();
                    activeAccount.deposit(amount);
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: ");
                    int amount = scanner.nextInt();
                    activeAccount.withdraw(amount);
                }
                case 4 -> {
                    System.out.print("Enter target account number: ");
                    int targetAccNo = scanner.nextInt();
                    BankAccount target = bank.findAccount(targetAccNo);
                    if (target != null) {
                        System.out.print("Enter transfer amount: ");
                        int amount = scanner.nextInt();
                        activeAccount.transfer(target, amount);
                    } else {
                        System.out.println("Target account not found!");
                    }
                }
                case 5 -> {
                    System.out.print("Enter new account number: ");
                    int newAccNo = scanner.nextInt();
                    System.out.print("Enter initial balance: ");
                    int balance = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.print("Enter account holder name: ");
                    String name = scanner.nextLine();
                    System.out.print("Set account password (numeric): ");
                    int newPassword = scanner.nextInt();
                    bank.createAccount(newAccNo, balance, name, newPassword);
                }
                case 6 -> bank.listAccounts();
                case 7 -> {
                    System.out.println("Logging out...");
                    activeAccount = null; // force re-login
                }
                case 8 -> {
                    isRunning = false;
                    System.out.println("Exiting Bank menu...");
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
