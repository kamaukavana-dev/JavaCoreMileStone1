import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

class User {
    String username;
    String passwordHash;
    boolean isAdmin;

    User(String username, String passwordHash, boolean isAdmin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }
}

class AuthSystem {
    ArrayList<User> users = new ArrayList<>();

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found!");
        }
    }

    void register(String username, String password, boolean isAdmin) {
        for (User u : users) {
            if (u.username.equals(username)) {
                System.out.println("Username already exists!");
                return;
            }
        }
        String hashed = hashPassword(password);
        users.add(new User(username, hashed, isAdmin));
        System.out.println("User registered successfully!");
    }

    User login(String username, String password) {
        String hashed = hashPassword(password);
        for (User u : users) {
            if (u.username.equals(username) && u.passwordHash.equals(hashed)) {
                System.out.println("Login successful! Welcome " + u.username);
                return u;
            }
        }
        System.out.println("Login failed! Wrong credentials.");
        return null;
    }

    void changePassword(User user, String oldPassword, String newPassword) {
        String oldHash = hashPassword(oldPassword);
        if (!user.passwordHash.equals(oldHash)) {
            System.out.println("Old password is incorrect!");
            return;
        }
        user.passwordHash = hashPassword(newPassword);
        System.out.println("Password changed successfully!");
    }

    void listUsers() {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        for (User u : users) {
            System.out.println(u.username + " | Admin: " + u.isAdmin);
        }
    }

    void deleteUser(String username) {
        users.removeIf(u -> u.username.equals(username));
        System.out.println("User deleted successfully!");
    }
}

public class AuthenticationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthSystem auth = new AuthSystem();
        boolean isRunning = true;

        // Pre-register an admin
        auth.register("admin", "admin123", true);

        User loggedIn = null;

        while (isRunning) {
            System.out.println("""
                    1) Register
                    2) Login
                    3) Change Password (must be logged in)
                    4) List Users (Admin only)
                    5) Delete User (Admin only)
                    6) Logout
                    7) Exit
                    """);
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    auth.register(username, password, false);
                }
                case 2 -> {
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    loggedIn = auth.login(username, password);
                }
                case 3 -> {
                    if (loggedIn == null) {
                        System.out.println("You must be logged in to change your password.");
                    } else {
                        System.out.print("Enter old password: ");
                        String oldPass = sc.nextLine();
                        System.out.print("Enter new password: ");
                        String newPass = sc.nextLine();
                        auth.changePassword(loggedIn, oldPass, newPass);
                    }
                }
                case 4 -> {
                    if (loggedIn != null && loggedIn.isAdmin) {
                        auth.listUsers();
                    } else {
                        System.out.println("Admin privileges required!");
                    }
                }
                case 5 -> {
                    if (loggedIn != null && loggedIn.isAdmin) {
                        System.out.print("Enter username to delete: ");
                        String username = sc.nextLine();
                        auth.deleteUser(username);
                    } else {
                        System.out.println("Admin privileges required!");
                    }
                }
                case 6 -> {
                    if (loggedIn != null) {
                        System.out.println("User " + loggedIn.username + " logged out.");
                        loggedIn = null;
                    } else {
                        System.out.println("No user is currently logged in.");
                    }
                }
                case 7 -> {
                    isRunning = false;
                    System.out.println("Exiting Authentication System...");
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }
}
