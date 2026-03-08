import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class AnalyzerWorker implements Runnable {
    private String line;
    private AtomicInteger errorCount;
    private AtomicInteger infoCount;

    public AnalyzerWorker(String line, AtomicInteger errorCount, AtomicInteger infoCount) {
        this.line = line;
        this.errorCount = errorCount;
        this.infoCount = infoCount;
    }

    @Override
    public void run() {
        if (line.startsWith("ERROR")) {
            errorCount.incrementAndGet();
        } else {
            infoCount.incrementAndGet();
        }
    }
}

public class RealTimeAnalyzer {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger infoCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<String> simulatedStream = Arrays.asList(
                "INFO: User logged in",
                "ERROR: Database connection failed",
                "INFO: Transaction complete",
                "ERROR: Timeout occurred",
                "INFO: Payment processed",
                "ERROR: Disk full",
                "INFO: User logged out"
        );

        // Feed data one by one with delay
        for (String line : simulatedStream) {
            executor.submit(new AnalyzerWorker(line, errorCount, infoCount));
            Thread.sleep(1000);

            // Clear console effect (optional, depends on terminal support)
            System.out.print("\033[H\033[2J");
            System.out.flush();

            // Dashboard-style output
            System.out.println("🎰 REAL-TIME DASHBOARD 🎰");
            System.out.println("----------------------------");
            System.out.printf("%-10s | %-10s%n", "INFO", "ERROR");
            System.out.println("----------------------------");
            System.out.printf("%-10d | %-10d%n", infoCount.get(), errorCount.get());
            System.out.println("----------------------------");
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nFinal Totals -> INFO: " + infoCount.get() +
                " | ERROR: " + errorCount.get());
        System.out.println("Stream analysis complete.");
    }
}
