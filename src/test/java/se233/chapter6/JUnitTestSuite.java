package se233.chapter6;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({GameLoopTest.class, FoodTest.class, SnakeTest.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JUnitTestSuite {
    @BeforeAll
    public static void initJfxRuntime() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
        }
    }
}

