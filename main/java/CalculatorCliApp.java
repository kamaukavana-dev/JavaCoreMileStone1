import java.util.InputMismatchException;
import java.util.Scanner;
// function class
class Calculator {
    int add(int a, int b) {
        return a + b;        //addition
    }
    int subtract(int a, int b) {
        return a - b;          //subtraction
    }
    int multiply(int a, int b) {
        return a * b;
    }
    int division(int a, int b) {
        if (b == 0) {
            System.out.println("Error: Division by zero!");
            return 0;
        }
        return a / b;                 //division
    }
    int remainder(int a, int b) {
        return a % b;          //returns remainder of the two
    }
}
//main body
public class CalculatorCliApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calc = new Calculator();

        System.out.println("""
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                      🕰️        🗿 CALCULATOR CLI 🗿         🕰️
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                """);

        boolean isRunning = true;
        while (isRunning) {
            try { //Error handler
                // Input
                System.out.print("Enter first number: ");
                int num1 = scanner.nextInt();

                System.out.print("Enter second number: ");
                int num2 = scanner.nextInt();

                System.out.print("Enter operator (+, -, *, /, %): ");
                char operator = scanner.next().charAt(0);

                // Calculation
                switch (operator) {
                    case '+' -> System.out.println("Result = " + calc.add(num1, num2));
                    case '-' -> System.out.println("Result = " + calc.subtract(num1, num2));
                    case '*' -> System.out.println("Result = " + calc.multiply(num1, num2));
                    case '/' -> System.out.println("Result = " + calc.division(num1, num2));
                    case '%' -> System.out.println("Result = " + calc.remainder(num1, num2));
                    default -> System.out.println("Invalid operator!");
                }

                // Continue?
                System.out.print("Do you want to continue? (y/n): ");
                char choice = scanner.next().charAt(0);
                if (choice == 'n' || choice == 'N') {
                    isRunning = false;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter numbers only.");
                scanner.nextLine(); // clear buffer to not bring an error once the calculator reruns
            }
        }

        scanner.close();
        System.out.println("Calculator exited...... Goodbye!");
    }
}
