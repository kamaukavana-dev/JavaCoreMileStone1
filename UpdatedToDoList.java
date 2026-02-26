import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Work {
    int id;
    String description;
    boolean isDone;

    Work(int id, String description, boolean isDone) {
        this.id = id;
        this.description = description;
        this.isDone = isDone;
    }

    Work(int id, String description) {
        this(id, description, false);
    }

    void markDone() {
        isDone = true;
    }

    @Override
    public String toString() {
        return id + ". " + description + " [" + (isDone ? "Done" : "Pending") + "]"+"\n";
    }

    // Parse a line like "1. Buy groceries [Pending]"
    static Work fromString(String line) {
        try {
            // Split into "id. description [status]"
            int dotIndex = line.indexOf(".");
            int bracketIndex = line.lastIndexOf("[");
            int id = Integer.parseInt(line.substring(0, dotIndex).trim());
            String desc = line.substring(dotIndex + 1, bracketIndex).trim();
            String status = line.substring(bracketIndex + 1, line.length() - 1).trim();
            boolean done = status.equalsIgnoreCase("Done");
            return new Work(id, desc, done);
        } catch (Exception e) {
            System.out.println("Error parsing line: " + line);
            return null;
        }
    }
}

class ToDoLists {
    ArrayList<Work> tasks = new ArrayList<>();
    int nextId = 1;
    String filePath = "/home/kavana-daniel/Documents/JavaNew/todolist.txt";

    void loadTasks() {
        try (Scanner reader = new Scanner(new File(filePath))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Work task = Work.fromString(line);
                if (task != null) {
                    tasks.add(task);
                    nextId = Math.max(nextId, task.id + 1);
                }
            }
            System.out.println("Tasks loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved tasks found. Starting fresh.");
        }
    }

    void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Work t : tasks) {
                writer.println(t); // uses toString()
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    void addTask(String description) {
        tasks.add(new Work(nextId++, description));
        System.out.println("Task added successfully!");
    }

    void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        for (Work t : tasks) {
            System.out.println(t);
        }
    }

    void markTaskDone(int id) {
        for (Work t : tasks) {
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

public class UpdatedToDoList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ToDoLists todo = new ToDoLists();

        todo.loadTasks();

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
            sc.nextLine();

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
                    todo.saveTasks();
                    System.out.println("Exiting To-Do List Manager...");
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }
}
