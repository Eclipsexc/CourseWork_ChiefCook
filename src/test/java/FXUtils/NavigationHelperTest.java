package FXUtils;

import Display.ConsoleMenu;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NavigationHelperTest {

    @Test
    void testReturnToMainMenuSuccess() {
        try (MockedConstruction<ConsoleMenu> mocked = mockConstruction(ConsoleMenu.class, (mock, context) -> {
            doNothing().when(mock).start(any(Stage.class));
        })) {
            Stage mockStage = mock(Stage.class);

            NavigationHelper.returnToMainMenu(mockStage);

            assertEquals(1, mocked.constructed().size());
            verify(mocked.constructed().get(0)).start(mockStage);
        }
    }

    @Test
    void testReturnToMainMenuFailure() {
        try (MockedConstruction<ConsoleMenu> mocked = mockConstruction(ConsoleMenu.class, (mock, context) -> {
            doThrow(new RuntimeException("Помилка меню")).when(mock).start(any(Stage.class));
        })) {
            Stage mockStage = mock(Stage.class);

            NavigationHelper.returnToMainMenu(mockStage);

            assertEquals(1, mocked.constructed().size());
            verify(mocked.constructed().get(0)).start(mockStage);
        }
    }
}
