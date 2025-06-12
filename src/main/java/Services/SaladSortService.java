package Services;

import Salad.Salad;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SaladSortService {

    private final SaladCalculator calculator = new SaladCalculator();

    public List<Salad> sortByWeight(List<Salad> salads, boolean ascending) {
        Comparator<Salad> comparator = Comparator.comparingDouble(calculator::calculateTotalWeight);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return salads.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Salad> sortByCalories(List<Salad> salads, boolean ascending) {
        Comparator<Salad> comparator = Comparator.comparingDouble(calculator::calculateTotalCalories);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return salads.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
