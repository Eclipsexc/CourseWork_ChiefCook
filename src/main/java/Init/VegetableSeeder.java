package Init;

import DB.VegetableDAO;
import Vegetables.Vegetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class VegetableSeeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(VegetableSeeder.class);

    public static List<Vegetable> seedIfEmpty(VegetableDAO vegetableDAO) {
        List<Vegetable> existing = vegetableDAO.getAllVegetables();
        if (!existing.isEmpty()) {
            return existing;
        }

        List<Vegetable> initial = List.of(
                new Vegetable("Авокадо", 160, Arrays.asList("C", "E", "K", "B6"), 2.0, 9.0, 15.0, "кремовий"),
                new Vegetable("Буряк", 37, Arrays.asList("C", "B9"), 1.5, 7.6, 0.1, "солодкий"),
                new Vegetable("Морква", 41, Arrays.asList("A", "K", "C", "B6"), 0.9, 9.6, 0.2, "солодкий"),
                new Vegetable("Цвітна капуста", 21, Arrays.asList("C", "K", "B6"), 2.4, 2.3, 0.3, "гіркуватий"),
                new Vegetable("Перець Чілі", 40, Arrays.asList("C", "A", "B6"), 1.9, 9.0, 0.4, "гострий"),
                new Vegetable("Перець Халапеньйо", 29, Arrays.asList("C", "A", "B6", "E"), 0.9, 6.5, 0.4, "гострий"),
                new Vegetable("Пекінська капуста", 12, Arrays.asList("A", "C", "K"), 1.2, 3.2, 0.2, "гіркуватий"),
                new Vegetable("Кукурудза", 97, Arrays.asList("C", "B1", "B5", "B9"), 3.0, 18.2, 1.2, "солодкий"),
                new Vegetable("Огірок", 15, Arrays.asList("C", "K"), 0.7, 3.0, 0.1, "водянистий"),
                new Vegetable("Цибуля", 24, Arrays.asList("C", "B6"), 2.2, 5.7, 0.3, "кислий"),
                new Vegetable("Редиска", 14, Arrays.asList("C", "B6"), 1.0, 4.4, 0.2, "гострий")
        );

        for (Vegetable veg : initial) {
            vegetableDAO.insertVegetable(veg);
            LOGGER.debug("Додано овоч: {}", veg.getName());
        }

        List<Vegetable> result = vegetableDAO.getAllVegetables();
        LOGGER.info("Успішно збережено {} овочів у таблицю.", result.size());
        return result;
    }
}
