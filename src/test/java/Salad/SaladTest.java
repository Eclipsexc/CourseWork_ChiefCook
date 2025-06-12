package Salad;

import Vegetables.Vegetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SaladTest {

    private Salad salad;
    private Vegetable tomato;
    private Vegetable lettuce;

    @BeforeEach
    void setUp() {
        salad = new Salad("Салат №1");
        tomato = new Vegetable("Помідор", 18.0, List.of("C", "A"), 0.9, 3.9, 0.2, "соковитий");
        lettuce = new Vegetable("Салат-латук", 15.0, List.of("K", "A"), 1.4, 2.9, 0.2, "ніжний");
    }

    @Test
    void testSetNameAndGetName() {
        assertEquals("Салат №1", salad.getName());
        salad.setName("Літній мікс");
        assertEquals("Літній мікс", salad.getName());
    }

    @Test
    void testSetAndGetId() {
        salad.setId(42);
        assertEquals(42, salad.getId());
    }

    @Test
    void testAddSingleIngredient() {
        salad.addIngredient(tomato, 120.0);
        Map<Vegetable, Double> ingredients = salad.getIngredients();

        assertEquals(1, ingredients.size());
        assertTrue(ingredients.containsKey(tomato));
        assertEquals(120.0, ingredients.get(tomato));
    }

    @Test
    void testAddMultipleDifferentIngredients() {
        salad.addIngredient(tomato, 100.0);
        salad.addIngredient(lettuce, 50.0);

        Map<Vegetable, Double> ingredients = salad.getIngredients();
        assertEquals(2, ingredients.size());
        assertEquals(100.0, ingredients.get(tomato));
        assertEquals(50.0, ingredients.get(lettuce));
    }

    @Test
    void testAddSameIngredientTwice() {
        salad.addIngredient(tomato, 80.0);
        salad.addIngredient(tomato, 70.0);

        Map<Vegetable, Double> ingredients = salad.getIngredients();
        assertEquals(1, ingredients.size());
        assertEquals(150.0, ingredients.get(tomato));
    }

    @Test
    void testEmptySaladInitially() {
        Salad emptySalad = new Salad();
        assertTrue(emptySalad.getIngredients().isEmpty());
        assertNull(emptySalad.getName());
    }
}
