package FXUtils;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import static org.junit.jupiter.api.Assertions.*;

public class FXComponentsTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.show();
    }

    @BeforeEach
    public void setupEach() {
        interact(() -> {
            if (stage.getScene() == null) {
                stage.setScene(new Scene(new Pane(), 900, 600));
            }
        });
    }

    @Test
    public void testCreateStyledButtonWithWidth() {
        Button button = FXComponents.createStyledButton("Test", 150);
        assertEquals("Test", button.getText());
        assertEquals(150, button.getMinWidth());
    }

    @Test
    public void testCreateStyledButtonWithStyleClass() {
        Button button = FXComponents.createStyledButton("Test", "custom-class");
        assertTrue(button.getStyleClass().contains("custom-class"));
    }

    @Test
    public void testCreateBackButton() {
        final boolean[] invoked = {false};
        Button button = FXComponents.createBackButton("Назад", () -> invoked[0] = true);
        button.fire();
        assertTrue(invoked[0]);
    }

    @Test
    public void testCreateVBoxWithStyleClass() {
        VBox box = FXComponents.createVBox(10, new Insets(5), "box-style");
        assertTrue(box.getStyleClass().contains("box-style"));
    }

    @Test
    public void testCreateVBoxWithChildren() {
        Label l1 = new Label("1");
        Label l2 = new Label("2");
        VBox box = FXComponents.createVBox(5, Pos.CENTER, l1, l2);
        assertEquals(2, box.getChildren().size());
    }

    @Test
    public void testCreateHBox() {
        Label l1 = new Label("1");
        Label l2 = new Label("2");
        HBox box = FXComponents.createHBox(5, Pos.CENTER, l1, l2);
        assertEquals(2, box.getChildren().size());
    }

    @Test
    public void testCreateLabel() {
        Label label = FXComponents.createLabel("text", "style1", "style2");
        assertEquals("text", label.getText());
        assertTrue(label.getStyleClass().contains("style1"));
        assertTrue(label.getStyleClass().contains("style2"));
    }

    @Test
    public void testCreateHeaderLabel() {
        Label label = FXComponents.createHeaderLabel("Header", "header-id");
        assertEquals("Header", label.getText());
        assertEquals("header-id", label.getId());
    }

    @Test
    public void testCreateTitleLabel() {
        Label label = FXComponents.createTitleLabel("Title");
        assertEquals("Title", label.getText());
    }

    @Test
    public void testCreateInfoLabel() {
        Label label = FXComponents.createInfoLabel("Info");
        assertEquals("Info", label.getText());
    }

    @Test
    public void testCreateComboBox() {
        ComboBox<String> combo = FXComponents.createComboBox("Choose", "One", "Two");
        assertEquals(2, combo.getItems().size());
        assertEquals("Choose", combo.getPromptText());
    }

    @Test
    public void testCreateCardVBox() {
        VBox card = FXComponents.createCardVBox(100, 50);
        assertEquals(100, card.getMinWidth());
        assertEquals(50, card.getMinHeight());
    }

    @Test
    public void testCreateStandardGridPane() {
        GridPane grid = FXComponents.createStandardGridPane();
        assertNotNull(grid);
        assertEquals(Pos.CENTER, grid.getAlignment());
    }

    @Test
    public void testWrapInStyledScrollPane() {
        VBox content = new VBox();
        ScrollPane scroll = FXComponents.wrapInStyledScrollPane(content);
        assertEquals(content, scroll.getContent());
    }

    @Test
    public void testCreateWrapperVBox() {
        VBox box = FXComponents.createWrapperVBox();
        assertTrue(box.getStyleClass().contains("salad-wrapper"));
    }

    @Test
    public void testCreateTextField() {
        TextField tf = FXComponents.createTextField("hint", "id-field", "cls");
        assertEquals("hint", tf.getPromptText());
        assertEquals("id-field", tf.getId());
        assertTrue(tf.getStyleClass().contains("cls"));
    }

    @Test
    public void testCreateButton() {
        Button btn = FXComponents.createButton("Click", "btn-id", "a", "b");
        assertEquals("Click", btn.getText());
        assertEquals("btn-id", btn.getId());
        assertTrue(btn.getStyleClass().contains("a"));
        assertTrue(btn.getStyleClass().contains("b"));
    }

    @Test
    public void testSetScene() {
        Pane root = new Pane();
        interact(() -> FXComponents.setScene(stage, root, "/stylesheets/styles.css"));
        assertEquals(root, stage.getScene().getRoot());
    }

}
