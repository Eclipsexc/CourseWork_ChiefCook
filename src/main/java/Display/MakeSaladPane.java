package Display;

import DB.SaladDAO;
import FXUtils.FXComponents;
import FXUtils.NavigationHelper;
import Salad.Salad;
import Services.SaladCalculator;
import Vegetables.Vegetable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;  // Правильний імпорт для TextFormatter
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MakeSaladPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(MakeSaladPane.class);

    private final Vegetable[] availableVegetables;
    private final SaladDAO saladDAO;
    private final Stage primaryStage;
    private final Salad editingSalad;
    private final SaladCalculator calculator = new SaladCalculator();
    private final Salad currentSalad = new Salad();

    private VBox ingredientsBox;
    private ComboBox<String> vegetableCombo;
    private TextField weightField;
    private TextField nameField;

    public MakeSaladPane(Vegetable[] availableVegetables, SaladDAO saladDAO, Stage primaryStage, Salad editingSalad) {
        this.availableVegetables = availableVegetables;
        this.saladDAO = saladDAO;
        this.primaryStage = primaryStage;
        this.editingSalad = editingSalad;
        initializeSaladState();
    }

    public void show() {
        VBox formLayout = buildFormLayout();
        Pane overlay = new Pane();
        overlay.getStyleClass().add("overlay-pane");

        StackPane root = new StackPane(overlay, formLayout);
        StackPane.setAlignment(formLayout, Pos.CENTER);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/stylesheets/create_edit_salad.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void initializeSaladState() {
        Salad base = (editingSalad != null) ? new Salad(editingSalad.getName()) : new Salad();
        if (editingSalad != null) {
            base.getIngredients().putAll(editingSalad.getIngredients());
            base.setId(editingSalad.getId());
        }
        currentSalad.setName(base.getName());
        currentSalad.getIngredients().putAll(base.getIngredients());
        currentSalad.setId(base.getId());
    }

    private VBox buildFormLayout() {
        VBox layout = FXComponents.createVBox(10, new Insets(10, 20, 20, 20), "form-layout");
        layout.setMaxWidth(520);

        Label title = FXComponents.createLabel(
                editingSalad == null ? "\uD83C\uDF74Створення нового салату" : "\uD83C\uDF74Редагування салату",
                "highlight-label"
        );
        title.setId("title-label");

        nameField = FXComponents.createTextField("Введіть назву салату", "name-field", "text-field");
        if (editingSalad != null) nameField.setText(editingSalad.getName());

        vegetableCombo = new ComboBox<>();
        vegetableCombo.setId("vegetable-combo");
        for (int i = 0; i < availableVegetables.length; i++) {
            vegetableCombo.getItems().add((i + 1) + ". " + availableVegetables[i].getName());
        }
        vegetableCombo.setPromptText("Оберіть овоч");
        vegetableCombo.getStyleClass().add("combo-box");

        weightField = FXComponents.createTextField("Задайте вагу (у грамах)", "weight-field", "text-field");

        TextFormatter<Double> weightFormatter = new TextFormatter<>(change -> {
            String newText = change.getText();

            if (newText.matches("[0-9]*\\.?[0-9]*")) {
                return change;
            }
            return null;
        });
        weightField.setTextFormatter(weightFormatter);

        ingredientsBox = FXComponents.createVBox(10, Pos.CENTER);
        ScrollPane scrollPane = FXComponents.createFixedHeightScroll(ingredientsBox, 180);
        populateIngredientsBox();

        Button addBtn = FXComponents.createButton("Додати інгредієнт", "add-button", "card-button");
        addBtn.setOnAction(e -> handleAddIngredient());

        Button saveBtn = FXComponents.createButton("Зберегти салат", "save-button", "card-button");
        saveBtn.setOnAction(e -> handleSave());

        Button backBtn = FXComponents.createButton("Повернутись до меню", "back-button", "card-button");
        backBtn.setOnAction(e -> NavigationHelper.returnToMainMenu(primaryStage));

        HBox btnBox = FXComponents.createHBox(20, Pos.CENTER, saveBtn, backBtn);
        VBox buttonPanel = FXComponents.createVBox(12, Pos.CENTER, addBtn, scrollPane, btnBox);

        layout.getChildren().addAll(title, nameField, vegetableCombo, weightField, buttonPanel);
        return layout;
    }

    private void populateIngredientsBox() {
        ingredientsBox.getChildren().clear();
        Map<Vegetable, Double> ingredients = currentSalad.getIngredients();

        if (ingredients.isEmpty()) {
            ingredientsBox.getChildren().add(FXComponents.createLabel("Салат поки порожній", "empty-ingredient-label"));
            return;
        }

        for (Map.Entry<Vegetable, Double> entry : ingredients.entrySet()) {
            Vegetable veg = entry.getKey();

            Label nameLabel = FXComponents.createLabel(veg.getName(), "ingredient-name");
            TextField weightEdit = new TextField(String.valueOf(calculator.round(entry.getValue())));
            weightEdit.getStyleClass().add("ingredient-weight");

            weightEdit.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    handleWeightEdit(weightEdit, veg, entry.getValue());
                }
            });

            Button deleteBtn = FXComponents.createButton("✖", "remove-button");
            deleteBtn.setOnAction(e -> {
                currentSalad.getIngredients().remove(veg);
                LOGGER.info("Видалено інгредієнт: {}", veg.getName());
                populateIngredientsBox();
            });

            Label gramLabel = FXComponents.createLabel("г");
            gramLabel.setStyle("-fx-text-fill: #555;");

            HBox row = FXComponents.createHBox(10, Pos.CENTER_LEFT, nameLabel, weightEdit, gramLabel, deleteBtn);
            row.getStyleClass().add("ingredient-row");
            ingredientsBox.getChildren().add(row);
        }
    }

    private void handleAddIngredient() {
        String selected = vegetableCombo.getValue();
        String weightText = weightField.getText().trim();
        if (selected == null || weightText.isEmpty()) return;
        try {
            double weight = Double.parseDouble(weightText);

            if (weight <= 0) {
                throw new IllegalArgumentException("Вага не може бути нульовою або від'ємною");
            }

            int index = Integer.parseInt(selected.split("\\.")[0]) - 1;
            Vegetable veg = availableVegetables[index];
            currentSalad.getIngredients().merge(veg, weight, Double::sum);
            LOGGER.info("Додано інгредієнт: {} ({} г)", veg.getName(), weight);
            weightField.clear();
            populateIngredientsBox();
        } catch (Exception ex) {
            LOGGER.error("Помилка при додаванні інгредієнта", ex);
            new Alert(Alert.AlertType.ERROR, "Введіть правильне число більше за нуль.").showAndWait();
        }
    }

    private void handleWeightEdit(TextField field, Vegetable veg, double oldWeight) {
        try {
            double newWeight = Double.parseDouble(field.getText().trim());

            if (newWeight <= 0) {
                throw new IllegalArgumentException("Вага не може бути нульовою або від'ємною");
            }

            currentSalad.getIngredients().put(veg, newWeight);
            LOGGER.info("Оновлено вагу для {}: {} г", veg.getName(), newWeight);
        } catch (Exception ex) {
            field.setText(String.valueOf(calculator.round(oldWeight)));
            LOGGER.warn("Некоректна вага для {}. Повернуто старе значення: {}", veg.getName(), oldWeight);
            new Alert(Alert.AlertType.WARNING, "Некоректна вага. Вага має бути більше за нуль.").showAndWait();
        }
    }

    private void handleSave() {
        currentSalad.setName(nameField.getText().trim());
        try {
            validateBeforeSave(currentSalad);
            if (editingSalad == null) {
                saladDAO.insertSalad(currentSalad);
                LOGGER.info("Створено новий салат: {}", currentSalad.getName());
            } else {
                saladDAO.updateSalad(currentSalad);
                LOGGER.info("Оновлено салат: {}", currentSalad.getName());
            }
            new Alert(Alert.AlertType.INFORMATION, "Салат збережено!").showAndWait();
            NavigationHelper.returnToMainMenu(primaryStage);
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Помилка валідації: {}", ex.getMessage());
            new Alert(Alert.AlertType.WARNING, ex.getMessage()).showAndWait();
        } catch (Exception ex) {
            LOGGER.error("Не вдалося зберегти салат", ex);
            new Alert(Alert.AlertType.ERROR, "Помилка збереження: " + ex.getMessage()).showAndWait();
        }
    }

    private void validateBeforeSave(Salad salad) {
        if (salad.getName() == null || salad.getName().isBlank())
            throw new IllegalArgumentException("Назва не може бути порожньою");
        if (salad.getIngredients().isEmpty())
            throw new IllegalArgumentException("Салат має містити хоча б один інгредієнт");
    }
}
