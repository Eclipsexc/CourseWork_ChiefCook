package Display;

import FXUtils.FXComponents;
import FXUtils.NavigationHelper;
import Services.VegetableSearchService;
import Vegetables.Vegetable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class VegetableTablePane extends VBox {
    private static final Logger LOGGER = LoggerFactory.getLogger(VegetableTablePane.class);

    private final GridPane grid = new GridPane();
    private final ScrollPane scrollPane = new ScrollPane(grid);
    private final VegetableSearchService searchService = new VegetableSearchService();

    private List<Vegetable> currentVegetables;
    private List<Vegetable> originalVegetables;

    private ComboBox<String> criteriaBox;
    private ComboBox<String> orderBox;
    private ComboBox<String> valueBox;
    private ToggleGroup searchGroup;

    public VegetableTablePane(List<Vegetable> data) {
        this.currentVegetables = new ArrayList<>(data);
        this.originalVegetables = new ArrayList<>(data);
        initPane();
    }

    public void showAsNewScene(Stage primaryStage) {
        StackPane root = new StackPane();
        root.getStyleClass().add("main-background");

        Pane overlay = new Pane();
        overlay.getStyleClass().add("overlay-pane");

        VBox centeredBox = new VBox(this);
        centeredBox.setAlignment(Pos.CENTER);
        centeredBox.setPadding(new Insets(40));
        StackPane.setAlignment(centeredBox, Pos.CENTER);

        root.getChildren().addAll(overlay, centeredBox);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheets/table.css")).toExternalForm());

        primaryStage.setScene(scene);
        LOGGER.info("Сцена з таблицею овочів ініціалізована.");
    }

    private void initPane() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setupGrid();
        getChildren().addAll(createTopBar(), createWrapper(), createBottomButtons());
        update(currentVegetables);
        LOGGER.info("Таблиця овочів ініціалізована. Всього елементів: {}", currentVegetables.size());
    }

    private void setupGrid() {
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setAlignment(Pos.CENTER);
        grid.getStyleClass().add("veg-table");

        double[] widths = {5, 20, 10, 20, 10, 10, 10, 15};
        for (double width : widths) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(width);
            col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.getStyleClass().add("veg-scroll-pane");
    }

    private VBox createWrapper() {
        VBox wrapper = new VBox(scrollPane);
        wrapper.getStyleClass().add("table-wrapper");
        return wrapper;
    }

    private HBox createTopBar() {
        criteriaBox = new ComboBox<>();
        criteriaBox.setId("criteria-box");
        criteriaBox.getItems().addAll("Калорійність", "Білки", "Вуглеводи", "Жири");
        criteriaBox.setPromptText("Сортувати за...");

        orderBox = new ComboBox<>();
        orderBox.setId("order-box");
        orderBox.getItems().addAll("За зростанням", "За спаданням");
        orderBox.setPromptText("Порядок");

        Button applySort = FXComponents.createStyledButton("Сортувати", "menu-button");
        applySort.setId("apply-sort");
        applySort.setOnAction(e -> applySorting());

        RadioButton byTaste = new RadioButton("За смаком");
        RadioButton byVitamin = new RadioButton("За вітаміном");
        searchGroup = new ToggleGroup();
        byTaste.setToggleGroup(searchGroup);
        byVitamin.setToggleGroup(searchGroup);
        byTaste.setSelected(true);

        valueBox = new ComboBox<>();
        valueBox.setId("value-box");
        valueBox.setPromptText("Оберіть значення");
        updateSearchValues(true);

        searchGroup.selectedToggleProperty().addListener((obs, old, sel) -> updateSearchValues(byTaste.isSelected()));

        Button applySearch = FXComponents.createStyledButton("Пошук", "menu-button");
        applySearch.setId("apply-search");
        applySearch.setOnAction(e -> applyFiltering(byTaste.isSelected()));

        Label searchLabel = new Label("Пошук:");
        searchLabel.getStyleClass().add("search-title");

        HBox radioGroup = new HBox(10, searchLabel, byTaste, byVitamin);
        radioGroup.setAlignment(Pos.CENTER_LEFT);

        HBox labelBox = new HBox(radioGroup);
        labelBox.getStyleClass().add("search-container");
        labelBox.setAlignment(Pos.CENTER_LEFT);
        labelBox.setPadding(new Insets(6, 10, 6, 10));

        HBox sortBlock = new HBox(8, criteriaBox, orderBox, applySort);
        sortBlock.setAlignment(Pos.CENTER_LEFT);

        HBox searchBlock = new HBox(10, labelBox, valueBox, applySearch);
        searchBlock.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(20, sortBlock, spacer, searchBlock);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);
        return topBar;
    }

    private HBox createBottomButtons() {
        Button resetButton = FXComponents.createStyledButton("Скинути параметри", "menu-button");
        resetButton.setId("reset-button");
        resetButton.setOnAction(e -> {
            currentVegetables = new ArrayList<>(originalVegetables);
            update(currentVegetables);
            criteriaBox.setValue(null);
            orderBox.setValue(null);
            boolean isTaste = ((RadioButton) searchGroup.getSelectedToggle()).getText().equals("За смаком");
            updateSearchValues(isTaste);
            valueBox.setValue(null);
            LOGGER.info("Параметри фільтра та сортування скинуто.");
        });

        Button back = FXComponents.createBackButton("Повернутися до меню", () ->
                NavigationHelper.returnToMainMenu((Stage) getScene().getWindow()));

        HBox box = new HBox(20, resetButton, back);
        box.setAlignment(Pos.CENTER);
        VBox.setMargin(box, new Insets(20, 0, 0, 0));
        return box;
    }

    private void updateSearchValues(boolean byTaste) {
        Set<String> values = byTaste
                ? searchService.getAvailableTastes(originalVegetables)
                : searchService.getAvailableVitamins(originalVegetables);
        valueBox.getItems().setAll(values);
    }

    private void applySorting() {
        String criteria = criteriaBox.getValue();
        String order = orderBox.getValue();
        if (criteria == null || order == null) return;

        VegetableSearchService.SortCriteria sc = switch (criteria) {
            case "Калорійність" -> VegetableSearchService.SortCriteria.CALORIES;
            case "Білки" -> VegetableSearchService.SortCriteria.PROTEINS;
            case "Вуглеводи" -> VegetableSearchService.SortCriteria.CARBOHYDRATES;
            case "Жири" -> VegetableSearchService.SortCriteria.FATS;
            default -> throw new IllegalStateException("Unknown criteria: " + criteria);
        };

        VegetableSearchService.SortOrder so = order.equals("За зростанням")
                ? VegetableSearchService.SortOrder.ASCENDING
                : VegetableSearchService.SortOrder.DESCENDING;

        sort(sc, so);
        LOGGER.info("Виконано сортування: {} ({})", sc, so);
    }

    private void applyFiltering(boolean byTaste) {
        String value = valueBox.getValue();
        if (value == null) return;
        if (byTaste) {
            filterByTaste(value);
            LOGGER.info("Виконано фільтрацію за смаком: {}", value);
        } else {
            filterByVitamin(value);
            LOGGER.info("Виконано фільтрацію за вітаміном: {}", value);
        }
    }

    public void update(List<Vegetable> data) {
        this.currentVegetables = data;
        grid.getChildren().clear();

        int col = 0;
        grid.add(createHeader("№"), col++, 0);
        grid.add(createHeader("Назва"), col++, 0);
        grid.add(createHeader("Ккал/100г"), col++, 0);
        grid.add(createHeader("Вітаміни"), col++, 0);
        grid.add(createHeader("Білки"), col++, 0);
        grid.add(createHeader("Вуглеводи"), col++, 0);
        grid.add(createHeader("Жири"), col++, 0);
        grid.add(createHeader("Смак"), col, 0);

        for (int i = 0; i < currentVegetables.size(); i++) {
            Vegetable veg = currentVegetables.get(i);
            int c = 0;
            grid.add(createCell(String.valueOf(i + 1), i, c++), c - 1, i + 1);
            grid.add(createCell(veg.getName(), i, c++), c - 1, i + 1);
            grid.add(createCell(String.format("%.1f", veg.getCaloriesPer100g()), i, c++), c - 1, i + 1);
            grid.add(createCell(String.join(", ", veg.getVitamins()), i, c++), c - 1, i + 1);
            grid.add(createCell(String.format("%.1f", veg.getProteinsPer100g()), i, c++), c - 1, i + 1);
            grid.add(createCell(String.format("%.1f", veg.getCarbohydratesPer100g()), i, c++), c - 1, i + 1);
            grid.add(createCell(String.format("%.1f", veg.getFatsPer100g()), i, c++), c - 1, i + 1);
            grid.add(createCell(veg.getTaste(), i, c), c, i + 1);
        }
        LOGGER.debug("Таблиця оновлена: {} записів", currentVegetables.size());
    }

    public void sort(VegetableSearchService.SortCriteria criteria, VegetableSearchService.SortOrder order) {
        List<Vegetable> sorted = searchService.sort(currentVegetables, criteria, order);
        update(sorted);
    }

    public void filterByTaste(String taste) {
        List<Vegetable> filtered = searchService.filterByTaste(currentVegetables, taste);
        update(filtered);
    }

    public void filterByVitamin(String vitamin) {
        List<Vegetable> filtered = searchService.filterByVitamin(currentVegetables, vitamin);
        update(filtered);
    }

    private Label createHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("header-label");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private Label createCell(String text, int rowIndex, int colIndex) {
        Label label = new Label(text);
        label.getStyleClass().add("cell-label");
        if (rowIndex % 2 == 0) {
            label.getStyleClass().add("even-row");
        }
        label.setId("cell-" + rowIndex + "-" + colIndex);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(text.matches("[\\d\\.,]+") ? Pos.CENTER : Pos.CENTER_LEFT);
        return label;
    }
}
