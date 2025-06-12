package Application;

import Display.ConsoleMenu;
import Init.AppInitializer;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        
        logger.info("Ініціалізація програми...");
        boolean success = AppInitializer.initializeApp();

        if (!success) {
            logger.error("Помилка при ініціалізації додатку. Завершення.");
            return;
        }

        logger.info("Ініціалізацію завершено. Запуск GUI.");
        Application.launch(ConsoleMenu.class);
        logger.info("Кінець роботи програми.");
    }
}
