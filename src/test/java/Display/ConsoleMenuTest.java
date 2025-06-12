package Display;

import Init.AppInitializer;
import Vegetables.Vegetable;
import DB.SaladDAO;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class ConsoleMenuTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        AppInitializer.availableVegetables = new Vegetable[]{
                new Vegetable("Огірок", 15, List.of("C"), 0.5, 3.0, 0.1, "свіжий")
        };
        AppInitializer.saladDAO = new SaladDAO();
        new ConsoleMenu().start(stage);
    }

    @BeforeEach
    public void setUp() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testAllButtonsVisible() {
        List<String> labels = List.of(
                "Переглянути наявні овочі",
                "Зробити салат",
                "Переглянути список салатів",
                "Завершити програму"
        );
        for (String label : labels) {
            verifyThat(label, isVisible());
        }
    }

    @Test
    public void testVegetableTableNavigation() {
        clickOn("Переглянути наявні овочі");
        WaitForAsyncUtils.waitForFxEvents();

        boolean found = lookup(".label").queryAllAs(Label.class).stream()
                .anyMatch(l -> l.getText().contains("Овочі") || l.getText().contains("Пошук"));
        assertTrue(found, "Очікується завантаження вікна овочів");
    }

    @Test
    public void testMakeSaladNavigation() {
        clickOn("Зробити салат");
        WaitForAsyncUtils.waitForFxEvents();

        boolean found = lookup("#title-label").tryQuery().isPresent();
        assertTrue(found, "Очікується завантаження вікна створення салату");
    }

    @Test
    public void testSaladListNavigation() {
        clickOn("Переглянути список салатів");
        WaitForAsyncUtils.waitForFxEvents();

        boolean found = lookup("#saladListTitle").tryQuery().isPresent();
        assertTrue(found, "Очікується завантаження списку салатів");
    }

    @Test
    public void testExitButtonPresenceOnly() {
        verifyThat("Завершити програму", isVisible());
    }
}
