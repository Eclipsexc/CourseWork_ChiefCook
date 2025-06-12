package Init;

import DB.DatabaseManager;
import DB.SaladDAO;
import DB.VegetableDAO;
import Salad.Salad;
import Vegetables.Vegetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppInitializer.class);

    public static List<Salad> saladsList = new ArrayList<>();
    public static Vegetable[] availableVegetables;
    public static SaladDAO saladDAO;

    public static boolean initializeApp() {
        try (Connection conn = DatabaseManager.getConnection()) {
            LOGGER.info("Підключення до бази даних успішне");

            VegetableDAO vegetableDAO = new VegetableDAO();
            SaladDAO localSaladDAO = new SaladDAO();

            LOGGER.info("Перевірка/ініціалізація овочів...");
            List<Vegetable> vegetables = VegetableSeeder.seedIfEmpty(vegetableDAO);
            availableVegetables = vegetables.toArray(new Vegetable[0]);
            LOGGER.info("Завантажено {} овочів", availableVegetables.length);

            LOGGER.info("Перевірка/ініціалізація салатів...");
            SaladSeeder.seedIfEmpty(localSaladDAO, vegetables);
            saladsList = localSaladDAO.getAllSalads(vegetables);
            LOGGER.info("Завантажено {} салатів", saladsList.size());

            saladDAO = localSaladDAO;
            return true;

        } catch (SQLException e) {
            LOGGER.error("Помилка під час ініціалізації додатку", e);
            return false;
        }
    }
}
