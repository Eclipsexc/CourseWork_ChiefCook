package Services;

import Salad.Salad;
import Vegetables.Vegetable;
import java.util.Map;

public class SaladCalculator {

    public SaladCalculator() {}

    public double calculateTotalCalories(Salad salad) {
        double totalCalories = 0.0;
        for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
            Vegetable vegetable = entry.getKey();
            double weight = entry.getValue();
            totalCalories += (vegetable.getCaloriesPer100g() * weight) / 100;
        }
        return round(totalCalories);
    }

    public double calculateTotalProteins(Salad salad) {
        double totalProteins = 0.0;
        for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
            Vegetable vegetable = entry.getKey();
            double weight = entry.getValue();
            totalProteins += (vegetable.getProteinsPer100g() * weight) / 100;
        }
        return round(totalProteins);
    }

    public double calculateTotalFats(Salad salad) {
        double totalFats = 0.0;
        for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
            Vegetable vegetable = entry.getKey();
            double weight = entry.getValue();
            totalFats += (vegetable.getFatsPer100g() * weight) / 100;
        }
        return round(totalFats);
    }

    public double calculateTotalCarbohydrates(Salad salad) {
        double totalCarbs = 0.0;
        for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
            Vegetable vegetable = entry.getKey();
            double weight = entry.getValue();
            totalCarbs += (vegetable.getCarbohydratesPer100g() * weight) / 100;
        }
        return round(totalCarbs);
    }

    public double calculateTotalWeight(Salad salad) {
        double totalWeight = 0.0;
        for (double weight : salad.getIngredients().values()) {
            totalWeight += weight;
        }
        return round(totalWeight);
    }

    public double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
