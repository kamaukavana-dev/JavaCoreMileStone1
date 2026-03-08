import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationSystemTest {

    private AuthSystem authSystem;

    @BeforeEach
    void setUp() {
        authSystem = new AuthSystem();
    }

    @Test
    void testRegisterNewUser() {
        authSystem.register("testuser", "password123", false);
        assertEquals(1, authSystem.users.size());
        assertEquals("testuser", authSystem.users.get(0).username);
        assertFalse(authSystem.users.get(0).isAdmin);
    }

    @Test
    void testRegisterAdminUser() {
        authSystem.register("admin", "adminpass", true);
        assertEquals(1, authSystem.users.size());
        assertEquals("admin", authSystem.users.get(0).username);
        assertTrue(authSystem.users.get(0).isAdmin);
    }

    @Test
    void testRegisterDuplicateUsername() {
        authSystem.register("testuser", "password123", false);
        authSystem.register("testuser", "differentpass", true);
        assertEquals(1, authSystem.users.size()); // Should not add duplicate
    }

    @Test
    void testLoginSuccessful() {
        authSystem.register("testuser", "password123", false);
        User loggedInUser = authSystem.login("testuser", "password123");
        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.username);
    }

    @Test
    void testLoginWrongPassword() {
        authSystem.register("testuser", "password123", false);
        User loggedInUser = authSystem.login("testuser", "wrongpass");
        assertNull(loggedInUser);
    }

    @Test
    void testLoginNonExistentUser() {
        User loggedInUser = authSystem.login("nonexistent", "password");
        assertNull(loggedInUser);
    }

    @Test
    void testChangePasswordSuccessful() {
        authSystem.register("testuser", "oldpass", false);
        User user = authSystem.login("testuser", "oldpass");
        assertNotNull(user);

        authSystem.changePassword(user, "oldpass", "newpass");
        User loggedInWithNewPass = authSystem.login("testuser", "newpass");
        assertNotNull(loggedInWithNewPass);
    }

    @Test
    void testChangePasswordWrongOldPassword() {
        authSystem.register("testuser", "oldpass", false);
        User user = authSystem.login("testuser", "oldpass");
        assertNotNull(user);

        authSystem.changePassword(user, "wrongoldpass", "newpass");
        // Password should remain unchanged
        User loggedInWithOldPass = authSystem.login("testuser", "oldpass");
        assertNotNull(loggedInWithOldPass);
    }

    @Test
    void testListUsers() {
        authSystem.register("user1", "pass1", false);
        authSystem.register("user2", "pass2", true);
        authSystem.register("admin", "adminpass", true);

        // listUsers() prints to console, but we can verify users exist
        assertEquals(3, authSystem.users.size());
    }

    @Test
    void testListUsersEmpty() {
        // Should handle empty list gracefully
        assertEquals(0, authSystem.users.size());
    }

    @Test
    void testDeleteUser() {
        authSystem.register("user1", "pass1", false);
        authSystem.register("user2", "pass2", false);
        assertEquals(2, authSystem.users.size());

        authSystem.deleteUser("user1");
        assertEquals(1, authSystem.users.size());
        assertEquals("user2", authSystem.users.get(0).username);
    }

    @Test
    void testDeleteNonExistentUser() {
        authSystem.register("user1", "pass1", false);
        assertEquals(1, authSystem.users.size());

        authSystem.deleteUser("nonexistent");
        assertEquals(1, authSystem.users.size()); // Should remain unchanged
    }

    @Test
    void testPasswordHashing() {
        authSystem.register("user", "password", false);
        String hash1 = authSystem.users.get(0).passwordHash;

        // Register another user with same password
        authSystem.register("user2", "password", false);
        String hash2 = authSystem.users.get(1).passwordHash;

        // Hashes should be identical for same password
        assertEquals(hash1, hash2);
    }

    @Test
    void testUserConstructor() {
        User user = new User("testuser", "hashedpass", true);
        assertEquals("testuser", user.username);
        assertEquals("hashedpass", user.passwordHash);
        assertTrue(user.isAdmin);
    }

    @Test
    void testMultipleUsers() {
        authSystem.register("user1", "pass1", false);
        authSystem.register("user2", "pass2", true);
        authSystem.register("user3", "pass3", false);

        assertEquals(3, authSystem.users.size());

        // Verify login works for all users
        assertNotNull(authSystem.login("user1", "pass1"));
        assertNotNull(authSystem.login("user2", "pass2"));
        assertNotNull(authSystem.login("user3", "pass3"));
    }
}
