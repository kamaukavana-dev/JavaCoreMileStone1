import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StreamDataAnalyzerTest {

    private final StreamDataAnalyzer analyzer = new StreamDataAnalyzer();

    private final List<String> sampleStream = Arrays.asList(
            "INFO: User logged in",
            "ERROR: Database connection failed",
            "info: lowercase test",
            "Error: Timeout occurred",
            "InFo: Payment processed",
            "ERROR: Disk full",
            "INFO: User logged out"
    );

    @Test
    void testCountErrorsCaseInsensitive() {
        long errors = analyzer.countErrors(sampleStream);
        assertEquals(3, errors, "Should count 3 ERROR lines regardless of case");
    }

    @Test
    void testCountInfoCaseInsensitive() {
        long infos = analyzer.countInfo(sampleStream);
        assertEquals(4, infos, "Should count 4 INFO lines regardless of case");
    }

    @Test
    void testCountByKeywordCaseInsensitive() {
        long userMentions = analyzer.countByKeyword(sampleStream, "user");
        assertEquals(2, userMentions, "Should count 2 lines mentioning 'User' regardless of case");
    }

    @Test
    void testEmptyStream() {
        List<String> emptyStream = Collections.emptyList();
        assertEquals(0, analyzer.countErrors(emptyStream));
        assertEquals(0, analyzer.countInfo(emptyStream));
    }

    @Test
    void testNullStream() {
        assertThrows(NullPointerException.class, () -> analyzer.countErrors(null));
    }
}
