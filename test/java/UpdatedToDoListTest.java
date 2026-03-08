import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedToDoListTest {

    @TempDir
    Path tempDir;

    private ToDoLists todoLists;
    private String originalUserDir;

    @BeforeEach
    void setUp() {
        originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        todoLists = new ToDoLists();
        // Override the file path to use temp directory
        todoLists.filePath = tempDir.resolve("test_todolist.txt").toString();
    }

    @AfterEach
    void tearDown() {
        System.setProperty("user.dir", originalUserDir);
    }

    @Test
    void testAddTask() {
        todoLists.addTask("Test task");
        assertEquals(1, todoLists.tasks.size());
        assertEquals("Test task", todoLists.tasks.get(0).description);
        assertEquals(1, todoLists.tasks.get(0).id);
        assertFalse(todoLists.tasks.get(0).isDone);
    }

    @Test
    void testAddMultipleTasks() {
        todoLists.addTask("First task");
        todoLists.addTask("Second task");
        todoLists.addTask("Third task");

        assertEquals(3, todoLists.tasks.size());
        assertEquals(1, todoLists.tasks.get(0).id);
        assertEquals(2, todoLists.tasks.get(1).id);
        assertEquals(3, todoLists.tasks.get(2).id);
        assertEquals(4, todoLists.nextId);
    }

    @Test
    void testMarkTaskDone() {
        todoLists.addTask("Test task");
        todoLists.markTaskDone(1);

        assertTrue(todoLists.tasks.get(0).isDone);
    }

    @Test
    void testMarkTaskDoneNotFound() {
        todoLists.addTask("Test task");
        todoLists.markTaskDone(999);

        assertFalse(todoLists.tasks.get(0).isDone); // Should remain unchanged
    }

    @Test
    void testDeleteTask() {
        todoLists.addTask("Task to delete");
        assertEquals(1, todoLists.tasks.size());

        todoLists.deleteTask(1);
        assertEquals(0, todoLists.tasks.size());
    }

    @Test
    void testDeleteTaskNotFound() {
        todoLists.addTask("Existing task");
        assertEquals(1, todoLists.tasks.size());

        todoLists.deleteTask(999);
        assertEquals(1, todoLists.tasks.size()); // Should remain unchanged
    }

    @Test
    void testListTasks() {
        todoLists.addTask("Task 1");
        todoLists.addTask("Task 2");

        // listTasks() prints to console, but we can verify tasks exist
        assertEquals(2, todoLists.tasks.size());
    }

    @Test
    void testListTasksEmpty() {
        // Should handle empty list gracefully
        assertEquals(0, todoLists.tasks.size());
    }

    @Test
    void testSaveAndLoadTasks() throws IOException {
        // Add tasks and save
        todoLists.addTask("Task 1");
        todoLists.addTask("Task 2");
        todoLists.markTaskDone(1); // Mark first task as done
        todoLists.saveTasks();

        // Create new instance and load
        ToDoLists newTodoLists = new ToDoLists();
        newTodoLists.filePath = todoLists.filePath;
        newTodoLists.loadTasks();

        assertEquals(2, newTodoLists.tasks.size());
        assertEquals("Task 1", newTodoLists.tasks.get(0).description);
        assertTrue(newTodoLists.tasks.get(0).isDone);
        assertEquals("Task 2", newTodoLists.tasks.get(1).description);
        assertFalse(newTodoLists.tasks.get(1).isDone);
    }

    @Test
    void testLoadTasksFileNotFound() {
        // Should handle missing file gracefully
        ToDoLists newTodoLists = new ToDoLists();
        newTodoLists.filePath = "nonexistent_file.txt";
        newTodoLists.loadTasks();

        assertEquals(0, newTodoLists.tasks.size());
    }

    @Test
    void testWorkConstructor() {
        Work work = new Work(1, "Test description", true);
        assertEquals(1, work.id);
        assertEquals("Test description", work.description);
        assertTrue(work.isDone);
    }

    @Test
    void testWorkConstructorDefaultDone() {
        Work work = new Work(1, "Test description");
        assertEquals(1, work.id);
        assertEquals("Test description", work.description);
        assertFalse(work.isDone);
    }

    @Test
    void testWorkMarkDone() {
        Work work = new Work(1, "Test");
        assertFalse(work.isDone);

        work.markDone();
        assertTrue(work.isDone);
    }

    @Test
    void testWorkToString() {
        Work pendingWork = new Work(1, "Pending task");
        assertEquals("1. Pending task [Pending]\n", pendingWork.toString());

        Work doneWork = new Work(2, "Done task", true);
        assertEquals("2. Done task [Done]\n", doneWork.toString());
    }

    @Test
    void testWorkFromString() {
        String line = "1. Test task [Pending]";
        Work work = Work.fromString(line);

        assertNotNull(work);
        assertEquals(1, work.id);
        assertEquals("Test task", work.description);
        assertFalse(work.isDone);
    }

    @Test
    void testWorkFromStringDone() {
        String line = "2. Completed task [Done]";
        Work work = Work.fromString(line);

        assertNotNull(work);
        assertEquals(2, work.id);
        assertEquals("Completed task", work.description);
        assertTrue(work.isDone);
    }

    @Test
    void testWorkFromStringInvalid() {
        String invalidLine = "invalid format";
        Work work = Work.fromString(invalidLine);
        assertNull(work);
    }

    @Test
    void testWorkFromStringEmpty() {
        String emptyLine = "";
        Work work = Work.fromString(emptyLine);
        assertNull(work);
    }

    @Test
    void testComplexTaskOperations() {
        // Add multiple tasks
        todoLists.addTask("Task 1");
        todoLists.addTask("Task 2");
        todoLists.addTask("Task 3");
        assertEquals(3, todoLists.tasks.size());

        // Mark some as done
        todoLists.markTaskDone(2);
        assertTrue(todoLists.tasks.get(1).isDone);
        assertFalse(todoLists.tasks.get(0).isDone);
        assertFalse(todoLists.tasks.get(2).isDone);

        // Delete one
        todoLists.deleteTask(1);
        assertEquals(2, todoLists.tasks.size());
        assertEquals(2, todoLists.tasks.get(0).id); // First remaining task
        assertEquals(3, todoLists.tasks.get(1).id); // Second remaining task
    }
}
