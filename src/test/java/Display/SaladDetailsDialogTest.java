package Display;

import DB.SaladDAO;
import Salad.Salad;
import Vegetables.Vegetable;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class SaladDetailsDialogTest extends ApplicationTest {

    private Salad testSalad;
    private SaladDAO saladDAO;
    private List<Vegetable> vegetables;

    @Override
    public void start(Stage stage) {
        saladDAO = new SaladDAO();
        vegetables = List.of(
                new Vegetable("Морква", 35, List.of("A", "C"), 1.2, 8.3, 0.3, "солодкий"),
                new Vegetable("Цибуля", 40, List.of("B6", "C"), 1.1, 9.5, 0.1, "гострий")
        );

        testSalad = new Salad("Тестовий салат");
        testSalad.getIngredients().put(vegetables.get(0), 100.0);
        testSalad.getIngredients().put(vegetables.get(1), 50.0);

        SaladDetailsDialog dialog = new SaladDetailsDialog(
                testSalad,
                vegetables,
                saladDAO,
                stage,
                () -> {}
        );

        dialog.show();
    }

    @BeforeEach
    public void waitForFx() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testDialogOpensWithCorrectTitle() {
        verifyThat(".salad-title", hasText("Тестовий салат"));
    }

    @Test
    public void testIngredientGridPopulatedCorrectly() {
        FxRobot robot = new FxRobot();

        List<Label> labels = robot.lookup(".ingredient-name").queryAllAs(Label.class).stream().toList();
        List<String> actualNames = labels.stream().map(Label::getText).toList();

        List<String> expected = List.of("Морква", "Цибуля");
        assertEquals(2, actualNames.size(), "Має бути 2 інгредієнти");
        assertTrue(actualNames.containsAll(expected), "Список має містити всі очікувані інгредієнти");
    }


    @Test
    public void testMacrosAreDisplayedCorrectly() {
        FxRobot robot = new FxRobot();
        Label macrosLabel = robot.lookup(".stat-label").match((Label l) -> l.getText().contains("Білки")).query();
        assertNotNull(macrosLabel);
        assertTrue(macrosLabel.getText().contains("Білки"));
        assertTrue(macrosLabel.getText().contains("Жири"));
        assertTrue(macrosLabel.getText().contains("Вуглеводи"));
    }

    @Test
    public void testCloseButtonClosesDialog() {
        FxRobot robot = new FxRobot();
        Button close = robot.lookup(".button-blue").query();
        robot.clickOn(close);
        WaitForAsyncUtils.waitForFxEvents();

        boolean closed = robot.listTargetWindows().stream()
                .map(Window::getScene)
                .map(Scene::getRoot)
                .noneMatch(n -> n.lookup(".salad-title") != null);

        assertTrue(closed, "Діалог має бути закритий");
    }
}
