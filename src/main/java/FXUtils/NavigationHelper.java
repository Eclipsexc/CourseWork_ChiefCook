package FXUtils;

import Display.ConsoleMenu;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationHelper.class);

    public static void returnToMainMenu(Stage stage) {
        try {
            LOGGER.info("Повернення до головного меню");
            new ConsoleMenu().start(stage);
        } catch (Exception ex) {
            LOGGER.error("Не вдалося повернутись до меню", ex);
        }
    }
}
