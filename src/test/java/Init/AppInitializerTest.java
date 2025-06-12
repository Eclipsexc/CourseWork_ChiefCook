package Init;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppInitializerTest {

    @Test
    public void testAppInitializationSuccess() {
        boolean success = AppInitializer.initializeApp();
        assertTrue(success, "Програма має ініціалізуватись успішно");

        assertNotNull(AppInitializer.availableVegetables, "Овочі мають бути доступні");
        assertTrue(AppInitializer.availableVegetables.length >= 10, "Очікується хоча б 10 овочів");

        assertNotNull(AppInitializer.saladsList, "Список салатів не має бути null");
        assertFalse(AppInitializer.saladsList.isEmpty(), "Салати мають бути згенеровані");
    }
}
