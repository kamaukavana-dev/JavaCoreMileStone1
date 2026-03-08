import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Arrays;

class ArrayUtils {
    int[] reverseArray(int[] array) {
        int[] reversed = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - 1 - i];
        }
        return reversed;
    }

    boolean isPalindrome(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            if (array[i] != array[array.length - 1 - i]) {
                return false;
            }
        }
        return true;
    }

    int min(int[] array) {
        if (array.length == 0) {
            System.out.println("Array is empty, no minimum.");
            return Integer.MIN_VALUE;
        }
        int min = array[0];
        for (int num : array) {
            if (num < min) min = num;
        }
        return min;
    }

    int max(int[] array) {
        if (array.length == 0) {
            System.out.println("Array is empty, no maximum.");
            return Integer.MAX_VALUE;
        }
        int max = array[0];
        for (int num : array) {
            if (num > max) max = num;
        }
        return max;
    }

    double average(int[] array) {
        if (array.length == 0) {
            System.out.println("Array is empty, no average.");
            return 0.0;
        }
        long sum = 0; // safer for large arrays
        for (int num : array) sum += num;
        return (double) sum / array.length;
    }
}

public class ArrayUtilities {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayUtils utils = new ArrayUtils();

        System.out.println("""
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                            🛞   JAVA ARRAY UTILITIES   🛞
                >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                """);

        int[] myArray = null;

        // Input loop with validation
        while (true) {
            try {
                System.out.print("Enter the size of the array (must be > 0): ");
                int size = sc.nextInt();
                if (size <= 0) {
                    System.out.println("Size must be greater than zero. Try again.");
                    continue;
                }

                myArray = new int[size];
                System.out.println("Enter "+size+" elements of the array: ");
                for (int i = 0; i < size; i++) {
                    myArray[i] = sc.nextInt();
                }
                break; // valid input, exit loop

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter integers only.");
                sc.nextLine(); // clear buffer
            }
        }

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("""
                    Choose an operation:
                    1. Reverse Array
                    2. Check Palindrome
                    3. Find Minimum
                    4. Find Maximum
                    5. Find Average
                    6. Exit
                    """);

            try {
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> System.out.println("Reversed array: " + Arrays.toString(utils.reverseArray(myArray)));
                    case 2 -> System.out.println("Is palindrome? " + utils.isPalindrome(myArray));
                    case 3 -> System.out.println("Minimum value: " + utils.min(myArray));
                    case 4 -> System.out.println("Maximum value: " + utils.max(myArray));
                    case 5 -> System.out.println("Average value: " + utils.average(myArray));
                    case 6 -> {
                        System.out.println("Exiting program...");
                        isRunning = false;
                    }
                    default -> System.out.println("Invalid choice! Please select 1–6.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1–6.");
                sc.nextLine(); // clear buffer
            }
        }

        sc.close();
    }
}

