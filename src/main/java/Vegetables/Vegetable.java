package Vegetables;

import java.util.List;

public class Vegetable {

    private int id;
    protected String name;
    protected double caloriesPer100g;
    protected List<String> vitamins;
    protected double proteinsPer100g;
    protected double carbohydratesPer100g;
    protected double fatsPer100g;
    protected String taste;

    public Vegetable(String name, double caloriesPer100g, List<String> vitamins, double proteinsPer100g, double carbohydratesPer100g, double fatsPer100g, String taste) {
        this.name = name;
        this.caloriesPer100g = caloriesPer100g;
        this.vitamins = vitamins;
        this.proteinsPer100g = proteinsPer100g;
        this.carbohydratesPer100g = carbohydratesPer100g;
        this.fatsPer100g = fatsPer100g;
        this.taste = taste;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getCaloriesPer100g() {
        return caloriesPer100g;
    }

    public List<String> getVitamins() {
        return vitamins;
    }

    public double getProteinsPer100g() {
        return proteinsPer100g;
    }

    public double getCarbohydratesPer100g() {
        return carbohydratesPer100g;
    }

    public double getFatsPer100g() {
        return fatsPer100g;
    }

    public String getTaste() {
        return taste;
    }

}