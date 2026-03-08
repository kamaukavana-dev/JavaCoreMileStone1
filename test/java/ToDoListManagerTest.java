import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToDoListManagerTest {

    @Test
    void addTaskAssignsIncrementingIds() {
        ToDoList list = new ToDoList();

        list.addTask("Buy milk");
        list.addTask("Read book");

        assertEquals(2, list.tasks.size());
        assertEquals(1, list.tasks.get(0).id);
        assertEquals("Buy milk", list.tasks.get(0).description);
        assertFalse(list.tasks.get(0).isDone);
        assertEquals(2, list.tasks.get(1).id);
    }

    @Test
    void markTaskDoneSetsFlag() {
        ToDoList list = new ToDoList();
        list.addTask("Finish project");

        list.markTaskDone(1);

        assertTrue(list.tasks.get(0).isDone);
    }

    @Test
    void deleteTaskRemovesMatchingEntry() {
        ToDoList list = new ToDoList();
        list.addTask("First");
        list.addTask("Second");

        list.deleteTask(1);

        assertEquals(1, list.tasks.size());
        assertEquals(2, list.tasks.get(0).id);
        assertEquals("Second", list.tasks.get(0).description);
    }
}
