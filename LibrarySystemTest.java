import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibrarySystemTest {

    @TempDir
    Path tempDir;

    @Test
    void addBookIsPersistedAndReloadable() throws Exception {
        runScript(AddBookScript.class, "Dune", "Frank Herbert");

        List<String> bookLines = Files.readAllLines(tempDir.resolve("library_books.txt"));
        assertEquals(1, bookLines.size());
        assertEquals("Dune - Frank Herbert - Available", bookLines.get(0));
    }

    @Test
    void borrowAndReturnCycleUpdatesAvailabilityAndMemberRecords() throws Exception {
        runScript(AddBookScript.class, "1984", "George Orwell");
        runScript(RegisterMemberScript.class, "Alice");
        runScript(BorrowScript.class, "Alice", "1984");

        List<String> booksAfterBorrow = Files.readAllLines(tempDir.resolve("library_books.txt"));
        assertTrue(booksAfterBorrow.get(0).endsWith("Borrowed"), "Book should be marked borrowed");

        List<String> membersAfterBorrow = Files.readAllLines(tempDir.resolve("library_members.txt"));
        assertTrue(membersAfterBorrow.get(0).contains("1984"), "Member record should show borrowed book");

        runScript(ReturnScript.class, "Alice", "1984");

        List<String> booksAfterReturn = Files.readAllLines(tempDir.resolve("library_books.txt"));
        assertTrue(booksAfterReturn.get(0).endsWith("Available"), "Book should be available after return");

        List<String> membersAfterReturn = Files.readAllLines(tempDir.resolve("library_members.txt"));
        assertTrue(membersAfterReturn.get(0).endsWith("[]"), "Borrowed list should be empty after return");
    }

    private void runScript(Class<?> scriptClass, String... args) throws Exception {
        String classpath = System.getProperty("java.class.path");
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(classpath);
        command.add(scriptClass.getName());
        for (String arg : args) {
            command.add(arg);
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(tempDir.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();
        assertEquals(0, exitCode, "Script " + scriptClass.getSimpleName() + " failed with output:\n" + output);
    }

    public static class AddBookScript {
        public static void main(String[] args) {
            Library library = new Library();
            library.loadBooks();
            library.loadMembers();
            library.addBook(new Book(args[0], args[1]));
        }
    }

    public static class RegisterMemberScript {
        public static void main(String[] args) {
            Library library = new Library();
            library.loadBooks();
            library.loadMembers();
            library.addMember(new Member(args[0]));
        }
    }

    public static class BorrowScript {
        public static void main(String[] args) {
            Library library = new Library();
            library.loadBooks();
            library.loadMembers();
            Member member = library.findMember(args[0]);
            Book book = library.findBook(args[1]);
            if (member == null || book == null) {
                System.err.println("Missing member or book");
                System.exit(1);
            }
            member.borrowBook(book);
            library.saveBooks();
            library.saveMembers();
        }
    }

    public static class ReturnScript {
        public static void main(String[] args) {
            Library library = new Library();
            library.loadBooks();
            library.loadMembers();
            Member member = library.findMember(args[0]);
            Book book = library.findBook(args[1]);
            if (member == null || book == null) {
                System.err.println("Missing member or book");
                System.exit(1);
            }
            member.returnBook(book);
            library.saveBooks();
            library.saveMembers();
        }
    }
}
