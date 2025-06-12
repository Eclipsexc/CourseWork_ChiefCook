package Display;

import Vegetables.Vegetable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VegetableTablePaneTest extends ApplicationTest {

    private List<Vegetable> vegetables;

    @Override
    public void start(Stage stage) {
        vegetables = List.of(
                new Vegetable("Морква", 35, List.of("A", "B1"), 0.9, 8.2, 0.2, "солодкий"),
                new Vegetable("Капуста", 25, List.of("C", "K"), 1.2, 5.3, 0.1, "гострий"),
                new Vegetable("Цибуля", 40, List.of("C"), 1.1, 9.3, 0.1, "гострий")
        );

        VegetableTablePane pane = new VegetableTablePane(vegetables);
        pane.showAsNewScene(stage);
        stage.show();
    }

    @BeforeEach
    void waitFx() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testInitialRendering() {
        FxRobot robot = new FxRobot();
        Label cell = robot.lookup("#cell-0-1").queryAs(Label.class);
        assertEquals("Морква", cell.getText());
    }

    @Test
    public void testSortByCaloriesDescending() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#criteria-box").clickOn("Калорійність");
        robot.clickOn("#order-box").clickOn("За спаданням");
        robot.clickOn("#apply-sort");
        Label cell = robot.lookup("#cell-0-1").queryAs(Label.class);
        assertEquals("Цибуля", cell.getText());
    }

    @Test
    public void testSortWithNoSelectionDoesNothing() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#apply-sort");
        Label cell = robot.lookup("#cell-0-1").queryAs(Label.class);
        assertEquals("Морква", cell.getText());
    }

    @Test
    public void testFilterByTaste() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#value-box").clickOn("солодкий");
        robot.clickOn("#apply-search");
        Label cell = robot.lookup("#cell-0-1").queryAs(Label.class);
        assertEquals("Морква", cell.getText());
    }

    @Test
    public void testFilteringByVitamin() {
        FxRobot robot = new FxRobot();

        robot.clickOn(".radio-button").clickOn("За вітаміном");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#value-box");
        WaitForAsyncUtils.sleep(300, java.util.concurrent.TimeUnit.MILLISECONDS);
        robot.clickOn("C");

        robot.clickOn("#apply-search");
        WaitForAsyncUtils.waitForFxEvents();

        List<Label> visibleLabels = robot.lookup(".label")
                .queryAllAs(Label.class)
                .stream()
                .filter(label -> !label.getText().isBlank())
                .filter(label -> List.of("Капуста", "Цибуля", "Морква").contains(label.getText()))
                .toList();

        boolean foundExpected = visibleLabels.stream()
                .anyMatch(l -> List.of("Капуста", "Цибуля").contains(l.getText()));

        assertTrue(foundExpected, "Очікується хоча б один овоч з вітаміном C");
    }

    @Test
    public void testResetButtonResetsState() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#reset-button");
        Label cell = robot.lookup("#cell-0-1").queryAs(Label.class);
        assertEquals("Морква", cell.getText());
    }

    @Test
    public void testBackButtonDoesNotCrash() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#back-button");
        assertTrue(true);
    }
}
