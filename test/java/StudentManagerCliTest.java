import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StudentManagerCliTest {

    @TempDir
    Path tempDir;

    @Test
    void addSaveAndReloadLearners() throws Exception {
        Path dataFile = tempDir.resolve("learners.txt");

        LearnerRecordManager manager = new LearnerRecordManager();
        manager.filePath = dataFile.toString();
        manager.loadLearners();

        manager.addLearner("Alice", "Sci", "R001", 2, 80);
        manager.addLearner("Bob", "Eng", "R002", 3, 65);
        manager.saveLearners();

        LearnerRecordManager reloaded = new LearnerRecordManager();
        reloaded.filePath = dataFile.toString();
        reloaded.loadLearners();

        assertEquals(2, reloaded.learners.size());
        assertEquals("Alice", reloaded.learners.get(0).name);
        assertEquals(80, reloaded.learners.get(0).grade);
        assertEquals(3, reloaded.nextId); // should advance past last id
    }

    @Test
    void updateGradeAndExportReport() throws Exception {
        Path dataFile = tempDir.resolve("learners.txt");
        Path reportFile = tempDir.resolve("report.txt");

        LearnerRecordManager manager = new LearnerRecordManager();
        manager.filePath = dataFile.toString();
        manager.reportPath = reportFile.toString();
        manager.loadLearners();

        manager.addLearner("Charlie", "Biz", "R003", 1, 50);
        manager.updateGrade(1, 70);
        assertEquals(70, manager.learners.get(0).grade);

        manager.exportReport(60);

        assertTrue(Files.exists(reportFile));
        String report = Files.readString(reportFile);
        assertTrue(report.contains("Average Grade"));
        assertTrue(report.contains("Charlie"));
        assertTrue(report.contains("Pass/Fail Report"));
    }
}
