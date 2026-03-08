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
        bank.createAccount(7001, 1_000, "Alice", 4321);
        bank.createAccount(7002, 500, "Bob", 8765);

        primary = bank.findAccount(7001);
        secondary = bank.findAccount(7002);
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
        BankAccount loggedIn = bank.login(7001, 4321);
        assertNotNull(loggedIn);
        assertEquals("Alice", loggedIn.getAccountName());
    }

    @Test
    void loginFailsWithWrongPassword() {
        BankAccount loggedIn = bank.login(7001, 9999);
        assertNull(loggedIn);
    }

    @Test
    void findAccountReturnsNullWhenMissing() {
        assertNull(bank.findAccount(404));
    }
}
