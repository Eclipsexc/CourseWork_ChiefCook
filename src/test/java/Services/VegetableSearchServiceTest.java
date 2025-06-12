package Services;

import Vegetables.Vegetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VegetableSearchServiceTest {

    private VegetableSearchService service;
    private List<Vegetable> sampleList;

    private Vegetable tomato;
    private Vegetable carrot;
    private Vegetable spinach;

    @BeforeEach
    void setUp() {
        service = new VegetableSearchService();

        tomato = new Vegetable("Томат", 20.0, List.of("C", "A"), 0.9, 0.2, 3.9, "солодкий");
        carrot = new Vegetable("Морква", 35.0, List.of("A", "K"), 1.0, 0.3, 7.0, "солодкий");
        spinach = new Vegetable("Шпинат", 23.0, List.of("C", "E"), 2.9, 0.4, 1.1, "гіркий");

        sampleList = List.of(tomato, carrot, spinach);
    }

    @Test
    void testSortByCaloriesAscending() {
        List<Vegetable> sorted = service.sort(sampleList, VegetableSearchService.SortCriteria.CALORIES, VegetableSearchService.SortOrder.ASCENDING);
        assertEquals(List.of(tomato, spinach, carrot), sorted);
    }

    @Test
    void testSortByProteinsDescending() {
        List<Vegetable> sorted = service.sort(sampleList, VegetableSearchService.SortCriteria.PROTEINS, VegetableSearchService.SortOrder.DESCENDING);
        assertEquals(List.of(spinach, carrot, tomato), sorted);
    }

    @Test
    void testFilterByTaste() {
        List<Vegetable> sweet = service.filterByTaste(sampleList, "солодкий");
        assertEquals(2, sweet.size());
        assertTrue(sweet.contains(tomato));
        assertTrue(sweet.contains(carrot));
    }

    @Test
    void testFilterByVitamin() {
        List<Vegetable> withC = service.filterByVitamin(sampleList, "C");
        assertEquals(2, withC.size());
        assertTrue(withC.contains(tomato));
        assertTrue(withC.contains(spinach));
    }

    @Test
    void testGetAvailableTastes() {
        Set<String> tastes = service.getAvailableTastes(sampleList);
        assertEquals(Set.of("солодкий", "гіркий"), tastes);
    }

    @Test
    void testGetAvailableVitamins() {
        Set<String> vitamins = service.getAvailableVitamins(sampleList);
        assertEquals(Set.of("A", "C", "E", "K"), vitamins);
    }

    @Test
    void testSortWithNullCriteriaThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.sort(sampleList, null, VegetableSearchService.SortOrder.ASCENDING));
        assertEquals("Sort criteria is null", ex.getMessage());
    }
}
