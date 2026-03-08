import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilitiesTest {

    private final ArrayUtils utils = new ArrayUtils();

    @Test
    void testReverseArray() {
        int[] original = {1, 2, 3, 4, 5};
        int[] reversed = utils.reverseArray(original);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, reversed);
    }

    @Test
    void testReverseArrayEmpty() {
        int[] original = {};
        int[] reversed = utils.reverseArray(original);
        assertArrayEquals(new int[]{}, reversed);
    }

    @Test
    void testReverseArraySingleElement() {
        int[] original = {42};
        int[] reversed = utils.reverseArray(original);
        assertArrayEquals(new int[]{42}, reversed);
    }

    @Test
    void testIsPalindromeTrue() {
        int[] palindrome = {1, 2, 3, 2, 1};
        assertTrue(utils.isPalindrome(palindrome));
    }

    @Test
    void testIsPalindromeFalse() {
        int[] notPalindrome = {1, 2, 3, 4, 5};
        assertFalse(utils.isPalindrome(notPalindrome));
    }

    @Test
    void testIsPalindromeEmpty() {
        int[] empty = {};
        assertTrue(utils.isPalindrome(empty));
    }

    @Test
    void testIsPalindromeSingleElement() {
        int[] single = {5};
        assertTrue(utils.isPalindrome(single));
    }

    @Test
    void testMin() {
        int[] array = {5, 2, 8, 1, 9};
        assertEquals(1, utils.min(array));
    }

    @Test
    void testMinEmptyArray() {
        int[] empty = {};
        assertEquals(Integer.MIN_VALUE, utils.min(empty));
    }

    @Test
    void testMinNegativeNumbers() {
        int[] negatives = {-5, -2, -8, -1, -9};
        assertEquals(-9, utils.min(negatives));
    }

    @Test
    void testMax() {
        int[] array = {5, 2, 8, 1, 9};
        assertEquals(9, utils.max(array));
    }

    @Test
    void testMaxEmptyArray() {
        int[] empty = {};
        assertEquals(Integer.MAX_VALUE, utils.max(empty));
    }

    @Test
    void testMaxNegativeNumbers() {
        int[] negatives = {-5, -2, -8, -1, -9};
        assertEquals(-1, utils.max(negatives));
    }

    @Test
    void testAverage() {
        int[] array = {1, 2, 3, 4, 5};
        assertEquals(3.0, utils.average(array));
    }

    @Test
    void testAverageEmptyArray() {
        int[] empty = {};
        assertEquals(0.0, utils.average(empty));
    }

    @Test
    void testAverageSingleElement() {
        int[] single = {7};
        assertEquals(7.0, utils.average(single));
    }

    @Test
    void testAverageWithZero() {
        int[] withZero = {0, 2, 4, 6};
        assertEquals(3.0, utils.average(withZero));
    }
}
