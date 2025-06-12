package Vegetables;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VegetableTest {

    @Test
    void testVegetableInitialization() {
        String name = "Морква";
        double calories = 41.0;
        List<String> vitamins = List.of("A", "K", "C");
        double proteins = 0.9;
        double carbs = 9.6;
        double fats = 0.2;
        String taste = "солодкий";

        Vegetable veg = new Vegetable(name, calories, vitamins, proteins, carbs, fats, taste);

        assertEquals(name, veg.getName());
        assertEquals(calories, veg.getCaloriesPer100g());
        assertEquals(vitamins, veg.getVitamins());
        assertEquals(proteins, veg.getProteinsPer100g());
        assertEquals(carbs, veg.getCarbohydratesPer100g());
        assertEquals(fats, veg.getFatsPer100g());
        assertEquals(taste, veg.getTaste());
    }

    @Test
    void testSetAndGetId() {
        Vegetable veg = new Vegetable("Буряк", 43.0, List.of("B9", "C"), 1.6, 9.6, 0.2, "землистий");
        veg.setId(5);
        assertEquals(5, veg.getId());
    }
}
