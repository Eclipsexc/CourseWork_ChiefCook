package Init;

import DB.VegetableDAO;
import Vegetables.Vegetable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VegetableSeederTest {

    @Test
    public void testSeederAddsVegetablesIfEmpty() {
        VegetableDAO dao = new VegetableDAO();
        dao.clearAll();

        List<Vegetable> before = dao.getAllVegetables();
        assertTrue(before.isEmpty(), "Перед сидом таблиця має бути порожня");

        List<Vegetable> seeded = VegetableSeeder.seedIfEmpty(dao);
        assertFalse(seeded.isEmpty(), "Овочі мають бути згенеровані");
        assertTrue(seeded.size() >= 10, "Очікується хоча б 10 овочів");

        for (Vegetable v : seeded) {
            assertNotNull(v.getId());
            assertNotNull(v.getName());
        }
    }
}
