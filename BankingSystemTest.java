import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the banking domain classes (BankAccount & Bank).
 * These tests focus on balance changes and authentication logic without relying on console I/O.
 */
class BankingSystemTest {

    private Bank bank;
    private BankAccount primary;
    private BankAccount secondary;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        bank.createAccount(1001, 1_000, "Alice", 1111);
        bank.createAccount(1002, 500, "Bob", 2222);

        primary = bank.findAccount(1001);
        secondary = bank.findAccount(1002);
    }

    @Test
    void depositIncreasesBalance() {
        primary.deposit(250);
        assertEquals(1_250, primary.getBalance());
    }

    @Test
    void depositRejectsNonPositiveAmount() {
        primary.deposit(0);
        assertEquals(1_000, primary.getBalance());
    }

    @Test
    void withdrawReducesBalanceWhenSufficient() {
        primary.withdraw(400);
        assertEquals(600, primary.getBalance());
    }

    @Test
    void withdrawDoesNotOverdraw() {
        primary.withdraw(2_000);
        assertEquals(1_000, primary.getBalance());
    }

    @Test
    void transferMovesFundsBetweenAccounts() {
        primary.transfer(secondary, 300);
        assertEquals(700, primary.getBalance());
        assertEquals(800, secondary.getBalance());
    }

    @Test
    void transferFailsWhenInsufficientFunds() {
        primary.transfer(secondary, 5_000);
        assertEquals(1_000, primary.getBalance());
        assertEquals(500, secondary.getBalance());
    }

    @Test
    void loginSucceedsWithCorrectCredentials() {
        BankAccount loggedIn = bank.login(1001, 1111);
        assertNotNull(loggedIn);
        assertEquals("Alice", loggedIn.getAccountName());
    }

    @Test
    void loginFailsWithWrongPassword() {
        BankAccount loggedIn = bank.login(1001, 9999);
        assertNull(loggedIn);
    }

    @Test
    void findAccountReturnsNullWhenMissing() {
        assertNull(bank.findAccount(404));
    }
}
