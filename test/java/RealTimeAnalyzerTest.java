import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class RealTimeAnalyzerTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testAnalyzerWorkerErrorLine() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        AnalyzerWorker worker = new AnalyzerWorker("ERROR: Database connection failed", errorCount, infoCount);
        worker.run();

        assertEquals(1, errorCount.get());
        assertEquals(0, infoCount.get());
    }

    @Test
    void testAnalyzerWorkerInfoLine() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        AnalyzerWorker worker = new AnalyzerWorker("INFO: User logged in", errorCount, infoCount);
        worker.run();

        assertEquals(0, errorCount.get());
        assertEquals(1, infoCount.get());
    }

    @Test
    void testAnalyzerWorkerCaseInsensitive() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        AnalyzerWorker errorWorker = new AnalyzerWorker("error: something happened", errorCount, infoCount);
        errorWorker.run();

        assertEquals(1, errorCount.get());
        assertEquals(0, infoCount.get());

        // Reset counters
        errorCount.set(0);
        infoCount.set(0);

        AnalyzerWorker infoWorker = new AnalyzerWorker("info: something else", errorCount, infoCount);
        infoWorker.run();

        assertEquals(0, errorCount.get());
        assertEquals(1, infoCount.get());
    }

    @Test
    void testAnalyzerWorkerOtherLines() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        AnalyzerWorker worker = new AnalyzerWorker("DEBUG: Debug message", errorCount, infoCount);
        worker.run();

        assertEquals(0, errorCount.get());
        assertEquals(1, infoCount.get()); // Non-ERROR lines are counted as INFO
    }

    @Test
    void testAnalyzerWorkerEmptyLine() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        AnalyzerWorker worker = new AnalyzerWorker("", errorCount, infoCount);
        worker.run();

        assertEquals(0, errorCount.get());
        assertEquals(1, infoCount.get()); // Empty line is not ERROR, so counted as INFO
    }

    @Test
    void testAnalyzerWorkerNullLine() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        AnalyzerWorker worker = new AnalyzerWorker(null, errorCount, infoCount);
        // Should handle null gracefully
        assertDoesNotThrow(() -> worker.run());
    }

    @Test
    void testMultipleWorkers() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        // Create multiple workers
        AnalyzerWorker worker1 = new AnalyzerWorker("ERROR: Error 1", errorCount, infoCount);
        AnalyzerWorker worker2 = new AnalyzerWorker("INFO: Info 1", errorCount, infoCount);
        AnalyzerWorker worker3 = new AnalyzerWorker("ERROR: Error 2", errorCount, infoCount);
        AnalyzerWorker worker4 = new AnalyzerWorker("INFO: Info 2", errorCount, infoCount);

        // Run all workers
        worker1.run();
        worker2.run();
        worker3.run();
        worker4.run();

        assertEquals(2, errorCount.get());
        assertEquals(2, infoCount.get());
    }

    @Test
    void testConcurrentCounters() {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        // Simulate concurrent access
        Runnable incrementError = () -> {
            for (int i = 0; i < 100; i++) {
                new AnalyzerWorker("ERROR: test", errorCount, infoCount).run();
            }
        };

        Runnable incrementInfo = () -> {
            for (int i = 0; i < 100; i++) {
                new AnalyzerWorker("INFO: test", errorCount, infoCount).run();
            }
        };

        Thread thread1 = new Thread(incrementError);
        Thread thread2 = new Thread(incrementInfo);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        assertEquals(100, errorCount.get());
        assertEquals(100, infoCount.get());
    }
}
