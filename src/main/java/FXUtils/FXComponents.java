package FXUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class FXComponents {

    public static Button createStyledButton(String text, double width) {
        Button button = new Button(text);
        button.setMinWidth(width);
        button.setFont(new Font("Arial", 14));
        button.getStyleClass().add("menu-button");
        button.setEffect(new DropShadow(2, Color.LIGHTGRAY));
        return button;
    }

    public static Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    public static Button createBackButton(String text, Runnable onBack) {
        Button backButton = new Button(text);
        backButton.setId("back-button");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> onBack.run());
        return backButton;
    }


    public static VBox createVBox(int spacing, Insets padding, String... styleClasses) {
        VBox vbox = new VBox(spacing);
        vbox.setPadding(padding);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().addAll(styleClasses);
        return vbox;
    }

    public static VBox createVBox(int spacing, Pos alignment, javafx.scene.Node... children) {
        VBox vbox = new VBox(spacing, children);
        vbox.setAlignment(alignment);
        return vbox;
    }

    public static HBox createHBox(int spacing, Pos alignment, javafx.scene.Node... children) {
        HBox hbox = new HBox(spacing, children);
        hbox.setAlignment(alignment);
        return hbox;
    }

    public static Label createLabel(String text, String... styleClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    public static Label createHeaderLabel(String text, String id) {
        Label label = new Label(text);
        label.getStyleClass().add("salad-header");
        label.setId(id);
        return label;
    }

    public static Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("salad-title");
        label.setWrapText(true);
        return label;
    }

    public static Label createInfoLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("salad-text");
        return label;
    }

    public static ComboBox<String> createComboBox(String prompt, String... items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        comboBox.setPromptText(prompt);
        comboBox.getStyleClass().add("combo-box");
        return comboBox;
    }

    public static VBox createCardVBox(double width, double height) {
        VBox box = new VBox(8);
        box.getStyleClass().add("salad-card");
        box.setMinSize(width, height);
        box.setPrefSize(width, height);
        box.setMaxSize(width, height);
        return box;
    }

    public static GridPane createStandardGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    public static ScrollPane wrapInStyledScrollPane(Region content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setMaxWidth(1000);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    public static VBox createWrapperVBox() {
        VBox box = new VBox(20);
        box.getStyleClass().add("salad-wrapper");
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 30, 30, 30));
        return box;
    }

    public static TextField createTextField(String prompt, String id, String... styleClasses) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setId(id);
        tf.getStyleClass().addAll(styleClasses);
        tf.setFont(new Font("Arial", 14));
        return tf;
    }

    public static Button createButton(String text, String id, String... styleClasses) {
        Button button = new Button(text);
        button.setId(id);
        button.getStyleClass().addAll(styleClasses);
        button.setFont(new Font("Arial", 14));
        return button;
    }

    public static void setScene(Stage stage, Parent root, String cssPath) {
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(
                FXComponents.class.getResource(cssPath)).toExternalForm());
        stage.setScene(scene);
    }
}