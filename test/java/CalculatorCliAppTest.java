import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorCliAppTest {

    private final Calculator calculator = new Calculator();

    @Test
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
        assertEquals(0, calculator.add(-2, 2));
        assertEquals(-5, calculator.add(-2, -3));
        assertEquals(100, calculator.add(50, 50));
    }

    @Test
    void testSubtract() {
        assertEquals(1, calculator.subtract(3, 2));
        assertEquals(0, calculator.subtract(5, 5));
        assertEquals(-5, calculator.subtract(-2, 3));
        assertEquals(5, calculator.subtract(10, 5));
    }

    @Test
    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3));
        assertEquals(0, calculator.multiply(0, 5));
        assertEquals(-6, calculator.multiply(2, -3));
        assertEquals(25, calculator.multiply(5, 5));
    }

    @Test
    void testDivision() {
        assertEquals(2, calculator.division(6, 3));
        assertEquals(3, calculator.division(9, 3));
        assertEquals(0, calculator.division(0, 5));
        assertEquals(-2, calculator.division(-6, 3));
    }

    @Test
    void testDivisionByZero() {
        // Division by zero should return 0 and print error message
        assertEquals(0, calculator.division(5, 0));
    }

    @Test
    void testRemainder() {
        assertEquals(1, calculator.remainder(7, 3));
        assertEquals(0, calculator.remainder(6, 3));
        assertEquals(2, calculator.remainder(8, 3));
        assertEquals(1, calculator.remainder(10, 3));
    }

    @Test
    void testRemainderWithNegativeNumbers() {
        assertEquals(-1, calculator.remainder(-7, 3));
        assertEquals(1, calculator.remainder(7, -3));
        assertEquals(-1, calculator.remainder(-7, -3));
    }

    @Test
    void testComplexArithmetic() {
        // Test combination of operations
        int result = calculator.add(
            calculator.multiply(2, 3),
            calculator.subtract(10, 4)
        );
        assertEquals(12, result); // (2*3) + (10-4) = 6 + 6 = 12
    }

    @Test
    void testLargeNumbers() {
        assertEquals(2000000, calculator.add(1000000, 1000000));
        assertEquals(0, calculator.subtract(1000000, 1000000));
        assertEquals(1000000000, calculator.multiply(10000, 100000));
    }

    @Test
    void testEdgeCases() {
        // Test with Integer.MAX_VALUE and Integer.MIN_VALUE
        assertEquals(-1, calculator.add(Integer.MAX_VALUE, 1)); // Overflow
        assertEquals(0, calculator.multiply(0, Integer.MAX_VALUE));
        assertEquals(0, calculator.division(0, Integer.MAX_VALUE));
    }
}
