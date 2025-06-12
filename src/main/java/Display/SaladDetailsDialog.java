package Display;

import DB.SaladDAO;
import Salad.Salad;
import Services.SaladCalculator;
import Vegetables.Vegetable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SaladDetailsDialog extends Dialog<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaladDetailsDialog.class);

    public SaladDetailsDialog(Salad salad,
                              List<Vegetable> allVegetables,
                              SaladDAO saladDAO,
                              Stage parentStage,
                              Runnable onBack) {

        LOGGER.info("Відкрито діалог перегляду салату: {}", salad.getName());

        setTitle("Інформація про салат");
        getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/stylesheets/details_salad.css")).toExternalForm()
        );

        SaladCalculator calc = new SaladCalculator();

        double maxKcal = -1, minKcal = Double.MAX_VALUE;
        Vegetable maxVeg = null, minVeg = null;
        Set<String> vitamins = new TreeSet<>();

        GridPane ingredientGrid = new GridPane();
        ingredientGrid.getStyleClass().add("ingredient-grid");

        int row = 0;
        for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
            Vegetable v = entry.getKey();
            double grams = entry.getValue();
            double kcal = v.getCaloriesPer100g() * grams / 100.0;

            Label name = new Label(v.getName());
            name.getStyleClass().add("ingredient-name");

            Label amount = new Label(String.format("%.2f г", grams));
            amount.getStyleClass().add("ingredient-value");

            ingredientGrid.add(name, 0, row);
            ingredientGrid.add(amount, 1, row);
            row++;

            if (kcal > maxKcal) {
                maxKcal = kcal;
                maxVeg = v;
            }
            if (kcal < minKcal) {
                minKcal = kcal;
                minVeg = v;
            }

            vitamins.addAll(v.getVitamins());
        }

        LOGGER.info("Салат '{}' містить {} інгредієнтів", salad.getName(), salad.getIngredients().size());
        if (maxVeg != null && minVeg != null) {
            LOGGER.info("Макс. ккал: {} ({}), Мін. ккал: {} ({})",
                    maxKcal, maxVeg.getName(), minKcal, minVeg.getName());
        }

        double totalWeight = calc.calculateTotalWeight(salad);
        double totalCalories = calc.calculateTotalCalories(salad);
        double proteins = calc.calculateTotalProteins(salad);
        double fats = calc.calculateTotalFats(salad);
        double carbs = calc.calculateTotalCarbohydrates(salad);

        Label title = new Label(salad.getName());
        title.getStyleClass().add("salad-title");

        Label section1 = new Label("Склад салату:");
        section1.getStyleClass().add("section-title");

        Label total = new Label(String.format("Загальна вага: %.2f г\nЕнергетична цінність: %.2f ккал", totalWeight, totalCalories));
        total.getStyleClass().add("summary-label");

        Label calMax = new Label("Найкалорійніший: " + maxVeg.getName() + String.format(" (%.1f ккал)", calc.round(maxKcal)));
        Label calMin = new Label("Найменш калорійний: " + minVeg.getName() + String.format(" (%.1f ккал)", calc.round(minKcal)));
        calMax.getStyleClass().add("stat-label");
        calMin.getStyleClass().add("stat-label");

        Label vits = new Label("Вітаміни: " + String.join(", ", vitamins));
        vits.getStyleClass().add("stat-label");

        Label macros = new Label(String.format("Білки: %.2f г   Вуглеводи: %.2f г   Жири: %.2f г",
                proteins, carbs, fats));
        macros.getStyleClass().add("stat-label");

        Button editButton = new Button("Редагувати");
        editButton.getStyleClass().add("button-green");
        editButton.setOnAction(e -> {
            LOGGER.info("Редагування салату: {}", salad.getName());
            close();
            new MakeSaladPane(
                    allVegetables.toArray(new Vegetable[0]),
                    saladDAO,
                    parentStage,
                    salad
            ).show();
        });

        Button deleteButton = new Button("Видалити");
        deleteButton.getStyleClass().add("button-red");
        deleteButton.setOnAction(e -> {
            LOGGER.info("Видалено салат: {}", salad.getName());
            saladDAO.deleteSaladById(salad.getId());
            close();
            onBack.run();
        });

        Button closeBtn = new Button("Закрити");
        closeBtn.getStyleClass().add("button-blue");
        closeBtn.setOnAction(e -> {
            LOGGER.info("Закрито діалог салату: {}", salad.getName());
            close();
        });

        HBox buttonsBar = new HBox(10, editButton, deleteButton, closeBtn);
        buttonsBar.setAlignment(Pos.CENTER);
        buttonsBar.setPadding(new Insets(10, 0, 0, 0));

        VBox dialogContent = new VBox(10,
                title,
                section1,
                ingredientGrid,
                total,
                calMax,
                calMin,
                vits,
                macros,
                buttonsBar
        );
        dialogContent.getStyleClass().add("details-pane");

        getDialogPane().setContent(dialogContent);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
    }
}
