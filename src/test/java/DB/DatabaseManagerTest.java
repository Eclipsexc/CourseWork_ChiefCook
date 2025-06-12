package DB;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Test
    public void testGetConnectionReturnsValidConnection() {
        try (Connection conn = DatabaseManager.getConnection()) {
            assertNotNull(conn, "З'єднання не повинно бути null");
            assertFalse(conn.isClosed(), "З'єднання має бути відкритим");
        } catch (Exception e) {
            fail("Не вдалося підключитися до бази: " + e.getMessage());
        }
    }
}
