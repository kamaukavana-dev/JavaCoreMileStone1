import java.util.*;
import java.util.concurrent.*;

// Worker thread that analyzes a single line of data
class AnalyzerWorker implements Runnable {
    private String line;

    public AnalyzerWorker(String line) {
        this.line = line;
    }

    @Override
    public void run() {
        // Simple analysis: check if line contains ERROR
        if (line.startsWith("ERROR")) {
            System.out.println(Thread.currentThread().getName() +
                    " ALERT: Found error -> " + line);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " INFO: Processed -> " + line);
        }
    }
}

public class RealTimeAnalyzer {
    public static void main(String[] args) throws InterruptedException {
        // Thread pool to handle incoming data
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Simulated continuous data stream
        List<String> simulatedStream = Arrays.asList(
                "INFO: User logged in",
                "ERROR: Database connection failed",
                "INFO: Transaction complete",
                "ERROR: Timeout occurred",
                "INFO: Payment processed",
                "ERROR: Disk full",
                "INFO: User logged out"
        );

        // Feed data one by one with delay to simulate "real-time"
        for (String line : simulatedStream) {
            executor.submit(new AnalyzerWorker(line)); // assign each line to a thread
            Thread.sleep(1000); // wait 1 second before next line arrives
        }

        // Shutdown after all tasks are submitted
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Stream analysis complete.");
    }
}
