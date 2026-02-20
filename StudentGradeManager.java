import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Student {
    String name;
    String school;
    String regNo;
    int yearOfStudy;
    int grade;

    Student(String name, String school, String regNo, int yearOfStudy, int grade) {
        this.name = name;
        this.school = school;
        this.regNo = regNo;
        this.yearOfStudy = yearOfStudy;
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}

class GradeManager {
    ArrayList<Student> students = new ArrayList<>(); //shrinks or expands according to the number of students

    void addStudent(String name, String school, String regNo, int yearOfStudy, int grade) {
        students.add(new Student(name, school, regNo, yearOfStudy, grade));
    }

    void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students added yet.");
            return;
        }
        for (Student s : students) {
            System.out.println("""
                    ------------------------------
                    Student Name: %s
                    School: %s
                    Registration No: %s
                    Year of Study: %d
                    Grade: %d
                    ------------------------------
                    """.formatted(s.name, s.school, s.regNo, s.yearOfStudy, s.getGrade()));
        }
    }

    void showStats() {
        if (students.isEmpty()) {
            System.out.println("No data available.");
            return;
        }
        int min = students.get(0).getGrade();
        int max = students.get(0).getGrade();
        long sum = 0;
        for (Student s : students) {
            if (s.getGrade() < min) min = s.getGrade();
            if (s.getGrade() > max) max = s.getGrade();
            sum += s.getGrade();
        }
        double avg = (double) sum / students.size();
        System.out.println("Average: " + avg);
        System.out.println("Highest: " + max);
        System.out.println("Lowest: " + min);
    }

    void passFail(int threshold) {
        for (Student s : students) {
            String status = (s.getGrade() >= threshold) ? "Pass" : "Fail";
            System.out.println(s.name + " (" + s.getGrade() + "): " + status);
        }
    }
}

public class StudentGradeManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GradeManager gm = new GradeManager();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("""
                    Choose an option:
                    1. Add Student
                    2. List Students
                    3. Show Statistics
                    4. Pass/Fail Report
                    5. Exit
                    """);

            try {
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter student name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter school: ");
                        String school = sc.nextLine();
                        System.out.print("Enter registration number: ");
                        String regNo = sc.nextLine();
                        System.out.print("Enter year of study (1-4): ");
                        int year = sc.nextInt();
                        System.out.print("Enter grade (0-100): ");
                        int grade = sc.nextInt();

                        gm.addStudent(name, school, regNo, year, grade);
                    }
                    case 2 -> gm.listStudents();
                    case 3 -> gm.showStats();
                    case 4 -> {
                        System.out.print("Enter pass threshold: ");
                        int threshold = sc.nextInt();
                        gm.passFail(threshold);
                    }
                    case 5 -> {
                        System.out.println("Exiting...");
                        isRunning = false;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter the correct type.");
                sc.nextLine(); // clear buffer
            }
        }

        sc.close();
    }
}
