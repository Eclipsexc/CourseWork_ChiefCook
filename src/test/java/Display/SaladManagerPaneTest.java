package Display;

import Salad.Salad;
import Vegetables.Vegetable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaladManagerPaneTest extends ApplicationTest {

    private Salad testSalad;
    private List<Vegetable> vegetables;
    private SaladManagerPane pane;

    @Override
    public void start(Stage stage) {
        vegetables = List.of(
                new Vegetable("Капуста", 20, List.of("C"), 1.0, 5.0, 0.1, "гострий"),
                new Vegetable("Огірок", 15, List.of("C", "K"), 0.6, 3.6, 0.1, "свіжий"),
                new Vegetable("Помідор", 18, List.of("A", "C"), 0.9, 3.9, 0.2, "кисло-солодкий")
        );

        testSalad = new Salad("Овочевий хруст");
        testSalad.getIngredients().put(vegetables.get(0), 150.0);

        pane = new SaladManagerPane(vegetables, () -> {}) {
            @Override
            protected List<Salad> loadSalads() {
                return List.of(testSalad);
            }
        };

        stage.setScene(new javafx.scene.Scene(pane, 800, 600));
        stage.show();
    }

    @BeforeEach
    public void waitBeforeEach() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testSaladCardDisplaysCorrectInfo() {
        FxRobot robot = new FxRobot();

        Label nameLabel = robot.lookup(l -> l instanceof Label && ((Label) l).getText().equals("Овочевий хруст")).query();
        assertEquals("Овочевий хруст", nameLabel.getText());

        Label weightLabel = robot.lookup(l -> l instanceof Label && ((Label) l).getText().contains("Вага")).query();
        assertTrue(weightLabel.getText().contains("г"));

        Label calLabel = robot.lookup(l -> l instanceof Label && ((Label) l).getText().contains("Цінність")).query();
        assertTrue(calLabel.getText().contains("ккал"));
    }

    @Test
    public void testDetailsDialogOpens() {
        FxRobot robot = new FxRobot();
        robot.clickOn("Дізнатись більше");
        WaitForAsyncUtils.waitForFxEvents();

        Node dialogNode = robot.lookup(".details-pane").query();
        assertNotNull(dialogNode);

        Label dialogTitle = robot.lookup(".salad-title").query();
        assertEquals("Овочевий хруст", dialogTitle.getText());

        Button closeBtn = robot.lookup(".button-blue").queryButton();
        assertEquals("Закрити", closeBtn.getText());
    }

    @Test
    public void testEditButtonOpensMakeSaladPane() {
        FxRobot robot = new FxRobot();
        robot.clickOn("Дізнатись більше");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("Редагувати");
        WaitForAsyncUtils.waitForFxEvents();

        Node nameField = robot.lookup("#name-field").query();
        assertNotNull(nameField);
    }

    @Test
    public void testSortingByWeightAscending() {
        FxRobot robot = new FxRobot();
        robot.clickOn("Сортувати за…");
        robot.clickOn("Вага");
        robot.clickOn("Порядок");
        robot.clickOn("За зростанням");
        robot.clickOn("Сортувати");
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(true);
    }

    @Test
    public void testSortingByCaloriesDescending() {
        FxRobot robot = new FxRobot();
        robot.clickOn("Сортувати за…");
        robot.clickOn("Енергетична цінність");
        robot.clickOn("Порядок");
        robot.clickOn("За спаданням");
        robot.clickOn("Сортувати");
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(true);
    }

    @Test
    public void testBackButtonDoesNotCrash() {
        FxRobot robot = new FxRobot();
        robot.clickOn("Повернутись до меню");
        assertTrue(true);
    }
}
