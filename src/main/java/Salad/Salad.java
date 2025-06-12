package Salad;

import Vegetables.Vegetable;

import java.util.HashMap;
import java.util.Map;

public class Salad {
    private int id;
    private String name;
    private final Map<Vegetable, Double> ingredients = new HashMap<>();

    public Salad() {}

    public Salad(String name) {
        this.name = name;
    }

    public void addIngredient(Vegetable vegetable, double weight) {
        ingredients.merge(vegetable, weight, Double::sum);
    }

    public Map<Vegetable, Double> getIngredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
