import java.io.*;
import java.util.*;

// Book class
class Book {
    private final String title;
    private final String author;
    private boolean isAvailable;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    public void borrowBook() { isAvailable = false; }
    public void returnBook() { isAvailable = true; }

    @Override
    public String toString() {
        return title + " - " + author + " [" + (isAvailable ? "Available" : "Borrowed") + "]";
    }
}

// Member class
class Member {
    private final String name;
    private List<String> borrowedTitles = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addBorrowedTitle(String title) {
        borrowedTitles.add(title);
    }

    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            book.borrowBook();
            borrowedTitles.add(book.getTitle());
            System.out.println(name + " borrowed: " + book.getTitle());
        } else {
            System.out.println("Sorry, " + book.getTitle() + " is already borrowed.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedTitles.contains(book.getTitle())) {
            book.returnBook();
            borrowedTitles.remove(book.getTitle());
            System.out.println(name + " returned: " + book.getTitle());
        } else {
            System.out.println(name + " did not borrow this book.");
        }
    }

    public void viewBorrowedBooks() {
        if (borrowedTitles.isEmpty()) {
            System.out.println(name + " has no borrowed books.");
        } else {
            System.out.println(name + "'s borrowed books:");
            for (String title : borrowedTitles) {
                System.out.println(title);
            }
        }
    }

    @Override
    public String toString() {
        return name + " - Borrowed: " + borrowedTitles;
    }
}

// Library class
class Library {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private static final String BOOK_FILE = "library_books.txt";
    private static final String MEMBER_FILE = "library_members.txt";

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
        System.out.println("Book added successfully!");
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
        } else {
            System.out.println("\n--- Books in Library ---");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    public Book findBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public void addMember(Member member) {
        members.add(member);
        saveMembers();
        System.out.println("Member registered successfully!");
    }

    public Member findMember(String name) {
        for (Member member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;
    }

    // Persistence for books
    public void loadBooks() {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 3) {
                    Book book = new Book(parts[0], parts[1]);
                    if (parts[2].equals("Borrowed")) {
                        book.borrowBook();
                    }
                    books.add(book);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved books yet.");
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    public void saveBooks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book book : books) {
                writer.write(book.getTitle() + " - " + book.getAuthor() + " - " + (book.isAvailable() ? "Available" : "Borrowed"));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // Persistence for members
    public void loadMembers() {
        members.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - Borrowed: ");
                Member member = new Member(parts[0]);
                if (parts.length > 1 && !parts[1].equals("[]")) {
                    String[] borrowed = parts[1].replace("[", "").replace("]", "").split(", ");
                    for (String b : borrowed) {
                        member.addBorrowedTitle(b);
                    }
                }
                members.add(member);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved members yet.");
        } catch (IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
    }

    public void saveMembers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBER_FILE))) {
            for (Member member : members) {
                writer.write(member.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }
}

// Main CLI
public class LibrarySystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        library.loadBooks();
        library.loadMembers();

        int choice;
        do {
            System.out.println("\n===** LIBRARY MANAGEMENT SYSTEM **===");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Register Member");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. View Member Borrowed Books");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                choice = -1;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    library.addBook(new Book(title, author));
                }
                case 2 -> library.viewBooks();
                case 3 -> {
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    library.addMember(new Member(name));
                }
                case 4 -> {
                    System.out.print("Enter member name: ");
                    String memberName = scanner.nextLine();
                    Member member = library.findMember(memberName);
                    if (member != null) {
                        System.out.print("Enter book title: ");
                        String bookTitle = scanner.nextLine();
                        Book book = library.findBook(bookTitle);
                        if (book != null) {
                            member.borrowBook(book);
                            library.saveBooks();
                            library.saveMembers();
                        } else {
                            System.out.println("Book not found.");
                        }
                    } else {
                        System.out.println("Member not found.");
                    }
                }
                case 5 -> {
                    System.out.print("Enter member name: ");
                    String memberName = scanner.nextLine();
                    Member member = library.findMember(memberName);
                    if (member != null) {
                        System.out.print("Enter book title: ");
                        String bookTitle = scanner.nextLine();
                        Book book = library.findBook(bookTitle);
                        if (book != null) {
                            member.returnBook(book);
                            library.saveBooks();
                            library.saveMembers();
                        } else {
                            System.out.println("Book not found.");
                        }
                    } else {
                        System.out.println("Member not found.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter member name: ");
                    String memberName = scanner.nextLine();
                    Member member = library.findMember(memberName);
                    if (member != null) {
                        member.viewBorrowedBooks();
                    } else {
                        System.out.println("Member not found.");
                    }
                }
                case 7 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7);

        scanner.close();
    }
}
