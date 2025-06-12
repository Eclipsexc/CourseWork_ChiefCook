package Services;

import Vegetables.Vegetable;
import java.util.*;
import java.util.stream.Collectors;

public class VegetableSearchService {

    public enum SortCriteria {
        CALORIES, PROTEINS, CARBOHYDRATES, FATS
    }

    public enum SortOrder {
        ASCENDING, DESCENDING
    }

    public List<Vegetable> sort(List<Vegetable> list, SortCriteria criteria, SortOrder order) {
        if (criteria == null) throw new IllegalArgumentException("Sort criteria is null");

        Comparator<Vegetable> comparator = switch (criteria) {
            case CALORIES -> Comparator.comparingDouble(Vegetable::getCaloriesPer100g);
            case PROTEINS -> Comparator.comparingDouble(Vegetable::getProteinsPer100g);
            case CARBOHYDRATES -> Comparator.comparingDouble(Vegetable::getCarbohydratesPer100g);
            case FATS -> Comparator.comparingDouble(Vegetable::getFatsPer100g);
        };

        if (order == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }

        return list.stream().sorted(comparator).toList();
    }


    public List<Vegetable> filterByTaste(List<Vegetable> list, String taste) {
        return list.stream()
                .filter(v -> v.getTaste().equalsIgnoreCase(taste))
                .toList();
    }

    public List<Vegetable> filterByVitamin(List<Vegetable> list, String vitamin) {
        return list.stream()
                .filter(v -> v.getVitamins().stream().anyMatch(vit -> vit.equalsIgnoreCase(vitamin)))
                .toList();
    }

    public Set<String> getAvailableTastes(List<Vegetable> list) {
        return list.stream()
                .map(Vegetable::getTaste)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getAvailableVitamins(List<Vegetable> list) {
        return list.stream()
                .flatMap(v -> v.getVitamins().stream())
                .map(String::toUpperCase)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
