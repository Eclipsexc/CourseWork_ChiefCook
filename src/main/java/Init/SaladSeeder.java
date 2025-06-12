package Init;

import DB.SaladDAO;
import Salad.Salad;
import Vegetables.Vegetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SaladSeeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaladSeeder.class);

    public static void seedIfEmpty(SaladDAO saladDAO, List<Vegetable> vegetables) {
        if (!saladDAO.getAllSalads(vegetables).isEmpty()) {
            LOGGER.info("Таблиця салатів вже містить дані — сідер не використовується.");
            return;
        }

        List<String> names = List.of(
                "Овочевий хруст", "Сонячна тарілка", "Гострий мікс", "Фітнес-бомба", "Свіжість лісу",
                "Кремовий день", "Літній настрій", "Бадьорість ранку", "Гармонія смаку", "Блискавка",
                "Зелений шторм", "Смак свободи", "Червоний спалах", "Острів здоров’я", "Салат-настрій",
                "Салат «Огірок і друзі»", "Салат «Пекінський привіт»", "Формула вітамінів", "Гострий герой", "Вибух кольорів"
        );

        LOGGER.info("Сідінг таблиці салатів ({}) випадковими даними...", names.size());

        Random random = new Random();
        int count = 0;
        for (String name : names) {
            Salad salad = new Salad(name);
            int n = 2 + random.nextInt(4);
            Set<Integer> used = new HashSet<>();

            for (int i = 0; i < n; i++) {
                int idx;
                do {
                    idx = random.nextInt(vegetables.size());
                } while (!used.add(idx));

                Vegetable veg = vegetables.get(idx);
                double weight = 30 + random.nextInt(200);
                salad.addIngredient(veg, weight);
            }

            saladDAO.insertSalad(salad);
            count++;
        }

        LOGGER.info("Успішно згенеровано {} салатів.", count);
    }
}
