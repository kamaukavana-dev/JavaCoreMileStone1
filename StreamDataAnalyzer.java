import java.util.List;

public class StreamDataAnalyzer {

    // Count how many lines start with "ERROR" (case-insensitive)
    public long countErrors(List<String> dataStream) {
        return dataStream.stream()
                .filter(line -> line.toUpperCase().startsWith("ERROR"))
                .count();
    }

    // Count how many lines start with "INFO" (case-insensitive)
    public long countInfo(List<String> dataStream) {
        return dataStream.stream()
                .filter(line -> line.toUpperCase().startsWith("INFO"))
                .count();
    }

    // General method: count lines containing a keyword (case-insensitive)
    public long countByKeyword(List<String> dataStream, String keyword) {
        return dataStream.stream()
                .filter(line -> line.toLowerCase().contains(keyword.toLowerCase()))
                .count();
    }
}
