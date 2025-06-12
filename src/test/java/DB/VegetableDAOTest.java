package DB;

import Vegetables.Vegetable;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VegetableDAOTest {

    private VegetableDAO dao;
    private List<Vegetable> backupVegetables;

    @BeforeEach
    public void backupAndSetup() throws Exception {
        dao = new VegetableDAO();

        backupVegetables = dao.getAllVegetables();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM Salad_Ingredients");
            stmt.execute("DELETE FROM Salads");
            stmt.execute("DELETE FROM Vegetables");

            stmt.execute("SET IDENTITY_INSERT Vegetables ON");
            stmt.execute("""
                INSERT INTO Vegetables (id, name, caloriesPer100g, proteinsPer100g, carbohydratesPer100g, fatsPer100g, taste, vitamins)
                VALUES (1, 'Буряк', 43, 1.6, 9.6, 0.2, 'солодкуватий', 'B9,C'),
                       (2, 'Морква', 41, 0.9, 10.0, 0.2, 'солодкий', 'A,K')
            """);
            stmt.execute("SET IDENTITY_INSERT Vegetables OFF");
        }
    }

    @AfterEach
    public void restoreOriginalData() throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Salad_Ingredients");
            stmt.execute("DELETE FROM Salads");
            stmt.execute("DELETE FROM Vegetables");
        }

        for (Vegetable veg : backupVegetables) {
            dao.insertVegetable(veg);
        }
    }

    @Test
    @Order(1)
    public void testGetAllVegetables() {
        List<Vegetable> vegetables = dao.getAllVegetables();

        assertNotNull(vegetables, "Список не повинен бути null");
        assertEquals(2, vegetables.size(), "Очікується 2 овочі");

        Vegetable v1 = vegetables.get(0);
        assertEquals("Буряк", v1.getName());
        assertEquals(43, v1.getCaloriesPer100g());
        assertEquals(List.of("B9", "C"), v1.getVitamins());
        assertEquals("солодкуватий", v1.getTaste());

        Vegetable v2 = vegetables.get(1);
        assertEquals("Морква", v2.getName());
        assertEquals(41, v2.getCaloriesPer100g());
        assertEquals(List.of("A", "K"), v2.getVitamins());
        assertEquals("солодкий", v2.getTaste());
    }

    @Test
    @Order(2)
    public void testEmptyResult() throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Vegetables");
        }

        List<Vegetable> vegetables = dao.getAllVegetables();
        assertTrue(vegetables.isEmpty(), "Очікується порожній список");
    }
}
