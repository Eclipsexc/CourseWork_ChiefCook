package DB;

import Salad.Salad;
import Vegetables.Vegetable;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SaladDAOTest {

    private SaladDAO dao;
    private VegetableDAO vegDao;
    private List<Vegetable> mockVegetables;
    private List<Vegetable> backupVegetables;
    private List<Salad> backupSalads;

    @BeforeEach
    public void setup() throws Exception {
        dao = new SaladDAO();
        vegDao = new VegetableDAO();
        mockVegetables = new ArrayList<>();

        backupVegetables = vegDao.getAllVegetables();
        backupSalads = dao.getAllSalads(backupVegetables);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM Salad_Ingredients");
            stmt.execute("DELETE FROM Salads");
            stmt.execute("DELETE FROM Vegetables");

            stmt.execute("SET IDENTITY_INSERT Vegetables ON");
            stmt.execute("""
                INSERT INTO Vegetables (id, name, caloriesPer100g, proteinsPer100g, carbohydratesPer100g, fatsPer100g, taste, vitamins)
                VALUES (1, 'Огірок', 15, 0.6, 3.6, 0.1, 'свіжий', 'C,K'),
                       (2, 'Помідор', 18, 0.9, 3.9, 0.2, 'кисло-солодкий', 'A,C')
            """);
            stmt.execute("SET IDENTITY_INSERT Vegetables OFF");

            Vegetable v1 = new Vegetable("Огірок", 15, List.of("C", "K"), 0.6, 3.6, 0.1, "свіжий");
            v1.setId(1);
            Vegetable v2 = new Vegetable("Помідор", 18, List.of("A", "C"), 0.9, 3.9, 0.2, "кисло-солодкий");
            v2.setId(2);

            mockVegetables.add(v1);
            mockVegetables.add(v2);
        }
    }

    @AfterEach
    public void restoreData() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Salad_Ingredients");
            stmt.execute("DELETE FROM Salads");
            stmt.execute("DELETE FROM Vegetables");
        } catch (Exception e) {
            throw new RuntimeException("Помилка очищення БД", e);
        }

        for (Vegetable veg : backupVegetables) {
            vegDao.insertVegetable(veg);
        }

        for (Salad salad : backupSalads) {
            dao.insertSalad(salad);
        }
    }

    @Test
    @Order(1)
    public void testInsertAndGetSalad() {
        Salad salad = new Salad("Тестовий салат");
        salad.addIngredient(mockVegetables.get(0), 100.0);
        salad.addIngredient(mockVegetables.get(1), 150.0);

        dao.insertSalad(salad);

        List<Salad> salads = dao.getAllSalads(mockVegetables);
        assertFalse(salads.isEmpty(), "Список салатів порожній");

        Salad loaded = salads.stream()
                .filter(s -> s.getName().equals("Тестовий салат"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Салат не знайдено"));

        assertEquals("Тестовий салат", loaded.getName());
        assertEquals(2, loaded.getIngredients().size());
    }

    @Test
    @Order(2)
    public void testDeleteSalad() {
        Salad salad = new Salad("Для видалення");
        salad.addIngredient(mockVegetables.get(0), 50.0);
        dao.insertSalad(salad);

        boolean existsBefore = dao.getAllSalads(mockVegetables)
                .stream().anyMatch(s -> s.getName().equals("Для видалення"));
        assertTrue(existsBefore, "Салат не був доданий");

        dao.deleteSaladById(salad.getId());

        boolean existsAfter = dao.getAllSalads(mockVegetables)
                .stream().anyMatch(s -> s.getName().equals("Для видалення"));
        assertFalse(existsAfter, "Салат не був видалений");
    }
    @Test
    @Order(3)
    public void testUpdateSalad() {
        Salad salad = new Salad("Оригінальний салат");
        salad.addIngredient(mockVegetables.get(0), 120.0);
        dao.insertSalad(salad);

        salad.setName("Оновлений салат");
        salad.getIngredients().clear();
        salad.addIngredient(mockVegetables.get(1), 200.0);
        dao.updateSalad(salad);

        List<Salad> salads = dao.getAllSalads(mockVegetables);
        Salad updated = salads.stream()
                .filter(s -> s.getId() == salad.getId())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Оновлений салат не знайдено"));

        assertEquals("Оновлений салат", updated.getName());
        assertEquals(1, updated.getIngredients().size());
        assertTrue(updated.getIngredients().containsKey(mockVegetables.get(1)));
        assertEquals(200.0, updated.getIngredients().get(mockVegetables.get(1)));
    }

}
