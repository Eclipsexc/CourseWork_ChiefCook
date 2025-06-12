package Init;

import DB.DatabaseManager;
import DB.SaladDAO;
import DB.VegetableDAO;
import Salad.Salad;
import Vegetables.Vegetable;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SaladSeederTest {

    private SaladDAO saladDAO;
    private VegetableDAO vegetableDAO;
    private List<Salad> backupSalads;
    private List<Vegetable> backupVegetables;
    private List<Vegetable> seededVegetables;

    @BeforeEach
    public void setUp() throws Exception {
        saladDAO = new SaladDAO();
        vegetableDAO = new VegetableDAO();

        backupVegetables = vegetableDAO.getAllVegetables();
        backupSalads = saladDAO.getAllSalads(backupVegetables);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Salad_Ingredients");
            stmt.execute("DELETE FROM Salads");
            stmt.execute("DELETE FROM Vegetables");
        }

        VegetableSeeder.seedIfEmpty(vegetableDAO);
        seededVegetables = vegetableDAO.getAllVegetables();
    }

    @AfterEach
    public void tearDown() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Salad_Ingredients");
            stmt.execute("DELETE FROM Salads");
            stmt.execute("DELETE FROM Vegetables");
        } catch (Exception e) {
            throw new RuntimeException("Помилка очищення БД", e);
        }

        for (Vegetable veg : backupVegetables) {
            vegetableDAO.insertVegetable(veg);
        }

        for (Salad salad : backupSalads) {
            saladDAO.insertSalad(salad);
        }
    }

    @Test
    @Order(1)
    public void testSeedSaladsWithIngredients() {
        SaladSeeder.seedIfEmpty(saladDAO, seededVegetables);

        List<Salad> salads = saladDAO.getAllSalads(seededVegetables);
        assertFalse(salads.isEmpty(), "Очікується, що салати будуть згенеровані");
        assertTrue(salads.size() >= 15, "Має бути хоча б 15 салатів");

        for (Salad salad : salads) {
            assertNotNull(salad.getName());
            assertFalse(salad.getIngredients().isEmpty(), "Салат має мати інгредієнти");
            assertTrue(salad.getIngredients().size() >= 2);

            for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
                assertNotNull(entry.getKey(), "Овоч не має бути null");
                assertTrue(entry.getValue() > 0, "Вага має бути > 0");
            }
        }
    }
}
