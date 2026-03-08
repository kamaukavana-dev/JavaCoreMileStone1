import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTrackerTest {

    @TempDir
    Path tempDir;

    private String originalUserDir;

    @BeforeEach
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void addExpenseThenCalculateTotal() throws Exception {
        String output = runMainWithInput("""
                1
                Food
                12.5
                3
                7
                """);

        Path expenseFile = tempDir.resolve("expenses.txt");
        List<String> lines = Files.readAllLines(expenseFile);
        assertEquals(List.of("Food - 12.5"), lines);
        assertTrue(output.contains("Total Expenses: 12.5"));
    }

    @Test
    void deleteExpenseRemovesSelectedLine() throws Exception {
        Path expenseFile = tempDir.resolve("expenses.txt");
        Files.write(expenseFile, List.of("Groceries - 10.0", "Transport - 5.0"));

        runMainWithInput("""
                5
                1
                7
                """);

        List<String> remaining = Files.readAllLines(expenseFile);
        assertEquals(1, remaining.size());
        assertEquals("Transport - 5.0", remaining.get(0));
    }

    @Test
    void viewSortedExpensesOrdersByAmount() throws Exception {
        Path expenseFile = tempDir.resolve("expenses.txt");
        Files.write(expenseFile, List.of(
                "Books - 30.0",
                "Coffee - 5.0",
                "Snacks - 10.0"
        ));

        String output = runMainWithInput("""
                4
                2
                7
                """);

        assertTrue(output.contains("SORTED EXPENSES")); // allow emojis too
        String sortedSection = output.substring(output.indexOf("SORTED EXPENSES"));
        int first = sortedSection.indexOf("1. Coffee - 5.0");
        int second = sortedSection.indexOf("2. Snacks - 10.0");
        int third = sortedSection.indexOf("3. Books - 30.0");

        assertTrue(first >= 0 && second >= 0 && third >= 0, "All sorted entries should appear");
        assertTrue(first < second && second < third, "Entries should be ordered by amount ascending");
    }

    @Test
    void exitsGracefullyWhenInputEnds() throws Exception {
        String output = runMainWithInput("""
                5
                """);

        assertTrue(output.contains("No input detected. Exiting."));
    }

    private String runMainWithInput(String input) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        try {
            System.setIn(testIn);
            System.setOut(new PrintStream(testOut));

            ExpenseTracker.main(new String[0]);
            return testOut.toString(StandardCharsets.UTF_8);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
