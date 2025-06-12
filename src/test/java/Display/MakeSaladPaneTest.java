package Display;

import DB.SaladDAO;
import Vegetables.Vegetable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class MakeSaladPaneTest extends ApplicationTest {

    private SaladDAO mockDAO;
    private Vegetable[] vegetables;

    @Override
    public void start(Stage stage) {
        mockDAO = Mockito.mock(SaladDAO.class);
        vegetables = new Vegetable[] {
                new Vegetable("Огірок", 15, List.of("C", "K"), 0.6, 3.6, 0.1, "свіжий"),
                new Vegetable("Помідор", 18, List.of("A", "C"), 0.9, 3.9, 0.2, "кисло-солодкий")
        };
        new MakeSaladPane(vegetables, mockDAO, stage, null).show();
        stage.show();
    }

    @BeforeEach
    public void waitForFx() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testSuccessfulSaveWithNameAndIngredient() {
        FxRobot robot = new FxRobot();

        robot.clickOn("#name-field").write("Весняний салат");
        robot.clickOn("#vegetable-combo").clickOn("1. Огірок");
        robot.clickOn("#weight-field").write("100");
        robot.clickOn("#add-button");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#save-button");
        WaitForAsyncUtils.waitForFxEvents();

        verify(mockDAO).insertSalad(Mockito.argThat(salad ->
                salad.getName().equals("Весняний салат") &&
                        salad.getIngredients().containsKey(vegetables[0])
        ));
    }

    @Test
    public void testSaveWithoutNameShowsAlert() {
        FxRobot robot = new FxRobot();

        robot.clickOn("#vegetable-combo").clickOn("1. Огірок");
        robot.clickOn("#weight-field").write("100");
        robot.clickOn("#add-button");
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#save-button");
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(robot.listTargetWindows().stream().anyMatch(w ->
                w.getScene().getRoot().lookup(".dialog-pane") != null
        ));
    }

    @Test
    public void testAddIngredientInvalidWeightShowsAlert() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#vegetable-combo").clickOn("1. Огірок");
        robot.clickOn("#weight-field").write("abc");
        robot.clickOn("#add-button");

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(robot.listTargetWindows().stream().anyMatch(w ->
                w.getScene().getRoot().lookup(".dialog-pane") != null
        ));
    }

    @Test
    public void testSaveWithoutIngredientsShowsAlert() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#name-field").write("Без овочів");
        robot.clickOn("#save-button");

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(robot.listTargetWindows().stream().anyMatch(w ->
                w.getScene().getRoot().lookup(".dialog-pane") != null
        ));
    }

    @Test
    public void testIngredientListUpdatesAfterAdd() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#vegetable-combo").clickOn("1. Огірок");
        robot.clickOn("#weight-field").write("123");
        robot.clickOn("#add-button");

        WaitForAsyncUtils.waitForFxEvents();

        List<TextField> weights = robot.lookup(".ingredient-weight").queryAllAs(TextField.class)
                .stream().toList();
        assertEquals(1, weights.size());
        assertEquals("123.0", weights.get(0).getText());
    }

    @Test
    public void testEditIngredientWeight() {
        FxRobot robot = new FxRobot();

        robot.clickOn("#name-field").write("Огірковий мікс");

        robot.clickOn("#vegetable-combo").clickOn("1. Огірок");
        robot.clickOn("#weight-field").write("100");
        robot.clickOn("#add-button");
        WaitForAsyncUtils.waitForFxEvents();

        TextField weightField = robot.lookup(".ingredient-weight").queryAs(TextField.class);
        assertNotNull(weightField);
        assertEquals("100.0", weightField.getText());

        robot.doubleClickOn(weightField).write("200");
        robot.clickOn("#save-button");
        WaitForAsyncUtils.waitForFxEvents();

        verify(mockDAO).insertSalad(Mockito.argThat(salad ->
                salad.getName().equals("Огірковий мікс") &&
                        Math.abs(salad.getIngredients().get(vegetables[0]) - 200.0) < 0.001
        ));
    }

}
