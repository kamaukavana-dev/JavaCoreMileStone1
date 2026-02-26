import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Learner {
    int id;
    String name;
    String school;
    String regNo;
    int yearOfStudy;
    int grade;

    Learner(int id, String name, String school, String regNo, int yearOfStudy, int grade) {
        this.id = id;
        this.name = name;
        this.school = school;
        this.regNo = regNo;
        this.yearOfStudy = yearOfStudy;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return id + ". " + name + " | School: " + school +
                " | RegNo: " + regNo +
                " | Year: " + yearOfStudy +
                " | Grade: " + grade;
    }

    static Learner fromString(String line) {
        try {
            String[] parts = line.split(";", 6);
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String school = parts[2];
            String regNo = parts[3];
            int year = Integer.parseInt(parts[4]);
            int grade = Integer.parseInt(parts[5]);
            return new Learner(id, name, school, regNo, year, grade);
        } catch (Exception e) {
            System.out.println("Error parsing learner line: " + line);
            return null;
        }
    }

    String toFileString() {
        return id + ";" + name + ";" + school + ";" + regNo + ";" + yearOfStudy + ";" + grade;
    }
}

class LearnerRecordManager {
    ArrayList<Learner> learners = new ArrayList<>();
    int nextId = 1;
    String filePath = "learners.txt";

    void loadLearners() {
        try (Scanner reader = new Scanner(new File(filePath))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Learner l = Learner.fromString(line);
                if (l != null) {
                    learners.add(l);
                    nextId = Math.max(nextId, l.id + 1);
                }
            }
            System.out.println("Learner records loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved records found. Starting fresh.");
        }
    }

    void saveLearners() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Learner l : learners) {
                writer.println(l.toFileString());
            }
            System.out.println("Learner records saved.");
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    void addLearner(String name, String school, String regNo, int yearOfStudy, int grade) {
        learners.add(new Learner(nextId++, name, school, regNo, yearOfStudy, grade));
        System.out.println("Learner added successfully!");
    }

    void listLearners() {
        if (learners.isEmpty()) {
            System.out.println("No learners available.");
            return;
        }
        for (Learner l : learners) {
            System.out.println(l);
        }
    }

    void updateGrade(int id, int newGrade) {
        for (Learner l : learners) {
            if (l.id == id) {
                l.grade = newGrade;
                System.out.println("Grade updated successfully!");
                return;
            }
        }
        System.out.println("Learner not found!");
    }

    void deleteLearner(int id) {
        learners.removeIf(l -> l.id == id);
        System.out.println("Learner deleted successfully!");
    }

    void showStats() {
        if (learners.isEmpty()) {
            System.out.println("No data available.");
            return;
        }
        int min = learners.get(0).grade;
        int max = learners.get(0).grade;
        long sum = 0;
        for (Learner l : learners) {
            if (l.grade < min) min = l.grade;
            if (l.grade > max) max = l.grade;
            sum += l.grade;
        }
        double avg = (double) sum / learners.size();
        System.out.println("Average Grade: " + avg);
        System.out.println("Highest Grade: " + max);
        System.out.println("Lowest Grade: " + min);
    }

    void passFail(int threshold) {
        if (learners.isEmpty()) {
            System.out.println("No data available.");
            return;
        }
        for (Learner l : learners) {
            String status = (l.grade >= threshold) ? "Pass" : "Fail";
            System.out.println(l.name + " (" + l.grade + "): " + status);
        }
    }

    // New: Export report to file
    void exportReport(int threshold) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("report.txt"))) {
            if (learners.isEmpty()) {
                writer.println("No data available.");
            } else {
                int min = learners.get(0).grade;
                int max = learners.get(0).grade;
                long sum = 0;
                for (Learner l : learners) {
                    if (l.grade < min) min = l.grade;
                    if (l.grade > max) max = l.grade;
                    sum += l.grade;
                }
                double avg = (double) sum / learners.size();
                writer.println("=== Learner Report ===");
                writer.println("Average Grade: " + avg);
                writer.println("Highest Grade: " + max);
                writer.println("Lowest Grade: " + min);
                writer.println();
                writer.println("Pass/Fail Report (Threshold: " + threshold + ")");
                for (Learner l : learners) {
                    String status = (l.grade >= threshold) ? "Pass" : "Fail";
                    writer.println(l.name + " (" + l.grade + "): " + status);
                }
            }
            System.out.println("Report exported to report.txt");
        } catch (IOException e) {
            System.out.println("Error exporting report: " + e.getMessage());
        }
    }
}

public class StudentManagerCli {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LearnerRecordManager manager = new LearnerRecordManager();
        manager.loadLearners();

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
                    1) Add Learner
                    2) List Learners
                    3) Update Grade
                    4) Delete Learner
                    5) Show Statistics
                    6) Pass/Fail Report
                    7) Export Report to File
                    8) Exit
                    """);
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter school: ");
                    String school = sc.nextLine();
                    System.out.print("Enter registration number: ");
                    String regNo = sc.nextLine();
                    System.out.print("Enter year of study: ");
                    int year = sc.nextInt();
                    System.out.print("Enter grade: ");
                    int grade = sc.nextInt();
                    sc.nextLine();
                    manager.addLearner(name, school, regNo, year, grade);
                }
                case 2 -> manager.listLearners();
                case 3 -> {
                    System.out.print("Enter learner ID to update grade: ");
                    int id = sc.nextInt();
                    System.out.print("Enter new grade: ");
                    int grade = sc.nextInt();
                    sc.nextLine();
                    manager.updateGrade(id, grade);
                }
                case 4 -> {
                    System.out.print("Enter learner ID to delete: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    manager.deleteLearner(id);
                }
                case 5 -> manager.showStats();
                case 6 -> {
                    System.out.print("Enter pass threshold: ");
                    int threshold = sc.nextInt();
                    sc.nextLine();
                    manager.passFail(threshold);
                }
                case 7 -> {
                    System.out.print("Enter pass threshold for report: ");
                    int threshold = sc.nextInt();
                    sc.nextLine();
                    manager.exportReport(threshold);
                }
                case 8 -> {
                    isRunning = false;
                    manager.saveLearners();
                    System.out.println("Exiting Learner Record Manager...");
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }
}
