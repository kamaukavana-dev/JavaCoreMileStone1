import java.util.ArrayList;
import java.util.Scanner;

class Task {
    int id;
    String description;
    boolean isDone;

    Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.isDone = false;
    }

    void markDone() {
        isDone = true;
    }

    @Override
    public String toString() {
        return id + ". " + description + " [" + (isDone ? "Done" : "Pending") + "]";
    }
}

class ToDoList {
    ArrayList<Task> tasks = new ArrayList<>();
    int nextId = 1;

    void addTask(String description) {
        tasks.add(new Task(nextId++, description));
        System.out.println("Task added successfully!");
    }

    void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        for (Task t : tasks) {
            System.out.println(t);
        }
    }

    void markTaskDone(int id) {
        for (Task t : tasks) {
            if (t.id == id) {
                t.markDone();
                System.out.println("Task marked as done!");
                return;
            }
        }
        System.out.println("Task not found!");
    }

    void deleteTask(int id) {
        tasks.removeIf(t -> t.id == id);
        System.out.println("Task deleted successfully!");
    }
}

public class ToDoListManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ToDoList todo = new ToDoList();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("""
                    1) Add Task
                    2) List Tasks
                    3) Mark Task as Done
                    4) Delete Task
                    5) Exit
                    """);
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter task description: ");
                    String desc = sc.nextLine();
                    todo.addTask(desc);
                }
                case 2 -> todo.listTasks();
                case 3 -> {
                    System.out.print("Enter task ID to mark done: ");
                    int id = sc.nextInt();
                    todo.markTaskDone(id);
                }
                case 4 -> {
                    System.out.print("Enter task ID to delete: ");
                    int id = sc.nextInt();
                    todo.deleteTask(id);
                }
                case 5 -> {
                    isRunning = false;
                    System.out.println("Exiting To-Do List Manager...");
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }
}
