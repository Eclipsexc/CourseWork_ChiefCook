package Services;

import Salad.Salad;
import Vegetables.Vegetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaladSortServiceTest {

    private SaladSortService sortService;
    private Salad salad1;
    private Salad salad2;
    private Salad salad3;

    @BeforeEach
    void setUp() {
        sortService = new SaladSortService();

        Vegetable carrot = new Vegetable("Морква", 41.0, List.of("A"), 0.9, 0.2, 10.0, "г");
        Vegetable tomato = new Vegetable("Томат", 18.0, List.of("C"), 0.9, 0.2, 3.9, "г");
        Vegetable cucumber = new Vegetable("Огірок", 16.0, List.of("K"), 0.8, 0.1, 3.6, "г");

        salad1 = new Salad("Салат 1");
        salad1.addIngredient(carrot, 100); // 41 ккал, 100 г

        salad2 = new Salad("Салат 2");
        salad2.addIngredient(tomato, 200); // 36 ккал, 200 г

        salad3 = new Salad("Салат 3");
        salad3.addIngredient(cucumber, 300); // 48 ккал, 300 г
    }

    @Test
    void testSortByWeightAscending() {
        List<Salad> input = List.of(salad3, salad1, salad2);
        List<Salad> sorted = sortService.sortByWeight(input, true);
        assertEquals(List.of(salad1, salad2, salad3), sorted);
    }

    @Test
    void testSortByWeightDescending() {
        List<Salad> input = List.of(salad1, salad2, salad3);
        List<Salad> sorted = sortService.sortByWeight(input, false);
        assertEquals(List.of(salad3, salad2, salad1), sorted);
    }

    @Test
    void testSortByCaloriesAscending() {
        List<Salad> input = List.of(salad3, salad2, salad1);
        List<Salad> sorted = sortService.sortByCalories(input, true);
        assertEquals(List.of(salad2, salad1, salad3), sorted);
    }

    @Test
    void testSortByCaloriesDescending() {
        List<Salad> input = List.of(salad1, salad2, salad3);
        List<Salad> sorted = sortService.sortByCalories(input, false);
        assertEquals(List.of(salad3, salad1, salad2), sorted);
    }
}
