package Display;

import DB.SaladDAO;
import Salad.Salad;
import Services.SaladCalculator;
import Services.SaladSortService;
import Vegetables.Vegetable;
import FXUtils.FXComponents;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SaladManagerPane extends StackPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaladManagerPane.class);

    protected final SaladDAO saladDAO = new SaladDAO();
    protected final List<Vegetable> allVegetables;
    protected final Runnable onBack;
    private final SaladCalculator calc = new SaladCalculator();
    private final SaladSortService sortService = new SaladSortService();
    private GridPane grid;
    protected List<Salad> salads;

    public SaladManagerPane(List<Vegetable> allVegetables, Runnable onBack) {
        this.allVegetables = allVegetables;
        this.onBack = onBack;
        this.salads = loadSalads();
        LOGGER.info("Завантажено {} салатів з бази", salads.size());

        this.grid = FXComponents.createStandardGridPane();
        VBox sortingBox = createSortingControls();

        renderGrid(salads);

        ScrollPane scrollPane = FXComponents.wrapInStyledScrollPane(grid);

        VBox wrapper = FXComponents.createWrapperVBox();
        Label header = FXComponents.createHeaderLabel("\uD83E\uDD57Список салатів", "saladListTitle");
        Button backButton = FXComponents.createBackButton("Повернутись до меню", onBack);

        wrapper.getChildren().addAll(header, sortingBox, scrollPane, backButton);

        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        getChildren().add(wrapper);
    }

    protected List<Salad> loadSalads() {
        return saladDAO.getAllSalads(allVegetables);
    }

    private VBox createSortingControls() {
        ComboBox<String> criteriaBox = FXComponents.createComboBox("Сортувати за…", "Вага", "Енергетична цінність");
        ComboBox<String> orderBox = FXComponents.createComboBox("Порядок", "За зростанням", "За спаданням");

        Button applySort = FXComponents.createStyledButton("Сортувати", "sort-button");
        applySort.setOnAction(e -> {
            String crit = criteriaBox.getValue();
            String ord = orderBox.getValue();
            if (crit == null || ord == null) return;

            boolean asc = ord.equals("За зростанням");
            if (crit.equals("Вага")) {
                salads = sortService.sortByWeight(salads, asc);
                LOGGER.info("Салати відсортовано за вагою ({})", asc ? "ASC" : "DESC");
            } else {
                salads = sortService.sortByCalories(salads, asc);
                LOGGER.info("Салати відсортовано за енергетичною цінністю ({})", asc ? "ASC" : "DESC");
            }
            renderGrid(salads);
        });

        HBox controls = new HBox(10, criteriaBox, orderBox, applySort);
        controls.setAlignment(Pos.CENTER);
        controls.getStyleClass().add("sorting-controls");

        VBox box = new VBox(10, controls);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void renderGrid(List<Salad> salads) {
        grid.getChildren().clear();
        int col = 0, row = 0;
        for (Salad salad : salads) {
            VBox card = createSaladCard(salad);
            grid.add(card, col, row);
            if (++col == 3) {
                col = 0;
                row++;
            }
        }
        LOGGER.debug("Сітка салатів оновлена. Відображено {} салатів", salads.size());
    }

    private VBox createSaladCard(Salad salad) {
        VBox box = FXComponents.createCardVBox(200, 145);

        Label title = FXComponents.createTitleLabel(salad.getName());

        double weight = calc.calculateTotalWeight(salad);
        double calories = calc.calculateTotalCalories(salad);

        Label weightLabel = FXComponents.createInfoLabel("Вага: " + weight + " г");
        Label calLabel = FXComponents.createInfoLabel("Цінність: " + calories + " ккал");

        Button details = FXComponents.createStyledButton("Дізнатись більше", "salad-button");
        details.setOnAction(e -> {
            LOGGER.info("Перегляд деталей салату: {}", salad.getName());
            showDetailsPopup(salad);
        });

        box.getChildren().addAll(title, weightLabel, calLabel, details);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void showDetailsPopup(Salad salad) {
        SaladDetailsDialog dialog = new SaladDetailsDialog(
                salad,
                allVegetables,
                saladDAO,
                (Stage) getScene().getWindow(),
                () -> {
                    this.salads = saladDAO.getAllSalads(allVegetables);
                    LOGGER.info("Оновлено список салатів після дії у вікні деталей");
                    renderGrid(this.salads);
                }
        );
        dialog.showAndWait();
    }
}
