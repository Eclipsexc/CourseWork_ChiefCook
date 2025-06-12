package Display;

import FXUtils.FXComponents;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static Init.AppInitializer.availableVegetables;
import static Init.AppInitializer.saladDAO;

public class ConsoleMenu extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger("main");
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        LOGGER.info("Запуск головного меню.");
        primaryStage = stage;

        StackPane root = new StackPane();
        root.getStyleClass().add("main-background");

        VBox panel = FXComponents.createVBox(15, new Insets(30), "menu-panel");

        Label title = new Label("🍅 Шеф-повар");
        title.setFont(new javafx.scene.text.Font("Arial", 26));
        title.getStyleClass().add("title-label");
        panel.getChildren().add(title);

        VBox menuPanel = FXComponents.createVBox(15, Insets.EMPTY);
        panel.getChildren().add(menuPanel);

        FXComponents.addMenuButton(menuPanel, "Переглянути наявні овочі", () -> {
            LOGGER.info("Вибрано: Переглянути наявні овочі");
            VegetableTablePane tablePane = new VegetableTablePane(Arrays.asList(availableVegetables));
            tablePane.showAsNewScene(primaryStage);
        });

        FXComponents.addMenuButton(menuPanel, "Зробити салат", () -> {
            LOGGER.info("Вибрано: Зробити салат");
            new MakeSaladPane(availableVegetables, saladDAO, primaryStage, null).show();
        });

        FXComponents.addMenuButton(menuPanel, "Переглянути список салатів", () -> {
            LOGGER.info("Вибрано: Переглянути список салатів");
            SaladManagerPane managerPane = new SaladManagerPane(
                    Arrays.asList(availableVegetables),
                    () -> {
                        LOGGER.info("Повернення до головного меню.");
                        new ConsoleMenu().start(primaryStage);
                    }
            );

            StackPane newRoot = new StackPane();
            newRoot.getStyleClass().add("main-background");

            VBox wrapper = new VBox(10, managerPane);
            wrapper.setAlignment(Pos.TOP_CENTER);
            wrapper.setPadding(new Insets(20));

            newRoot.getChildren().add(wrapper);

            FXComponents.setScene(primaryStage, newRoot, "/stylesheets/salad_cards.css");
        });

        FXComponents.addMenuButton(menuPanel, "Завершити програму", () -> {
            LOGGER.info("Програму завершено користувачем.");
            System.exit(0);
        });

        root.getChildren().add(panel);

        FXComponents.setScene(primaryStage, root, "/stylesheets/styles.css");
        primaryStage.setTitle("Шеф-повар");
        primaryStage.show();
    }
}
