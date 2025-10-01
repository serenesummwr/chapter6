package se233.chapter6;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter6.model.Food;

import static org.junit.jupiter.api.Assertions.assertNotSame;

public class FoodTest {
    private Food food;

//    @BeforeAll
//    public static void initJfxRuntime() {
//        javafx.application.Platform.startup(() -> {});
//    }

    @BeforeEach
    public void setup() {
        food = new Food(new Point2D(0, 0));
    }

    @Test
    public void respawn_shouldBeOnNewPosition() {
        food.respawn();
        assertNotSame(food.getPosition(), new Point2D(0, 0));
    }
}
