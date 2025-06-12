package Services;

import Salad.Salad;
import Vegetables.Vegetable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class SaladCalculatorTest {

    private Salad salad;
    private SaladCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SaladCalculator();
        salad = new Salad();

        Vegetable carrot = new Vegetable("Морква", 40.0, List.of("A", "K"), 1.0, 9.0, 0.2, "солодкий");
        Vegetable cucumber = new Vegetable("Огірок", 15.0, List.of("C"), 0.6, 3.6, 0.1, "свіжий");

        salad.addIngredient(carrot, 100.0);
        salad.addIngredient(cucumber, 100.0);
    }

    @Test
    void testCalculateTotalCalories() {
        Assertions.assertEquals(55.0, calculator.calculateTotalCalories(salad));
    }

    @Test
    void testCalculateTotalProteins() {
        Assertions.assertEquals(1.6, calculator.calculateTotalProteins(salad));
    }

    @Test
    void testCalculateTotalFats() {
        Assertions.assertEquals(0.3, calculator.calculateTotalFats(salad));
    }

    @Test
    void testCalculateTotalCarbohydrates() {
        Assertions.assertEquals(12.6, calculator.calculateTotalCarbohydrates(salad));
    }

    @Test
    void testCalculateTotalWeight() {
        Assertions.assertEquals(200.0, calculator.calculateTotalWeight(salad));
    }

    @Test
    void testRound() {
        Assertions.assertEquals(3.14, calculator.round(3.14159));
        Assertions.assertEquals(2.72, calculator.round(2.71828));
        Assertions.assertEquals(1.0, calculator.round(1.004));
    }
}
