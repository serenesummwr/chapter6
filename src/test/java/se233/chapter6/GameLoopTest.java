package se233.chapter6;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se233.chapter6.controller.GameLoop;
import se233.chapter6.model.Direction;
import se233.chapter6.model.Food;
import se233.chapter6.model.Snake;
import se233.chapter6.view.GameStage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameLoopTest {
    static {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {}
    }
    private GameStage gameStage;
    private Snake snake;
    private Food food;
    private GameLoop gameLoop;
    private Food specialFood;

//    @BeforeAll
//    public static void initJfxRuntime() {
//        javafx.application.Platform.startup(() -> {});
//    }

    @BeforeEach
    public void setUp() {
        gameStage = new GameStage();
        snake = new Snake(new Point2D(0, 0));
        food = new Food(new Point2D(0, 1));
        specialFood = new Food(new Point2D(1, 0));
        gameLoop = new GameLoop(gameStage, snake, food, specialFood);
    }

    private void clockTickHelper() throws Exception {
        ReflectionHelper.invokeMethod(gameLoop, "keyProcess", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "checkCollision", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
    }

    @Test
    public void keyProcess_pressRight_snakeTurnRight() throws Exception {
        ReflectionHelper.setField(gameStage, "key", KeyCode.RIGHT);
        ReflectionHelper.setField(snake, "direction", Direction.DOWN);
        clockTickHelper();
        Direction currentDirection = (Direction) ReflectionHelper.getField(snake, "direction");
        assertEquals(Direction.RIGHT, currentDirection);
    }

    @Test
    public void collided_snakeEatFood_shouldGrow() throws Exception {
        clockTickHelper();
        assertTrue(snake.getLength() > 1);
        clockTickHelper();
        assertNotSame(food.getPosition(), new Point2D(0, 1));
    }

    @Test
    public void testPreventReverseDirection() throws Exception {
        ReflectionHelper.setField(snake, "direction", Direction.RIGHT);
        ReflectionHelper.setField(gameStage, "key", KeyCode.LEFT);
        ReflectionHelper.invokeMethod(gameLoop, "keyProcess", new Class<?>[0]);
        assertEquals(Direction.RIGHT, ReflectionHelper.getField(snake, "direction"),
                "Snake should not be able to reverse direction from RIGHT to LEFT");
    }


   @Test
   public void collided_snakeHitBorder_shouldDie() throws Exception {
       ReflectionHelper.setField(gameStage, "key", KeyCode.LEFT);
       clockTickHelper();
       Boolean running = (Boolean) ReflectionHelper.getField(gameLoop, "running");
       assertFalse(running);
   }

    @Test
    public void redraw_calledThreeTimes_snakeAndFoodShouldRenderThreeTimes() throws Exception {
        GameStage mockGameStage = Mockito.mock(GameStage.class);
        Snake mockSnake = Mockito.mock(Snake.class);
        Food mockFood = Mockito.mock(Food.class);
        Food mockSpecialFood= Mockito.mock(Food.class);
        GameLoop gameLoop = new GameLoop(mockGameStage, mockSnake, mockFood, mockSpecialFood);

        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);

        verify(mockGameStage, times(3)).render(mockSnake, mockFood, mockSpecialFood);
    }

    @Test
    public void whenEatNormalFood_scoreIncreasedByOne() throws Exception {
        ReflectionHelper.setField(gameLoop, "score", 0);
        clockTickHelper();
        assertEquals(ReflectionHelper.getField(gameLoop, "score"), 1, "score should be increased to 1 after eating normal food");
    }

    @Test
    public void whenEatSpecialFood_scoreIncreasedByFive() throws Exception {
        ReflectionHelper.setField(snake, "direction", Direction.RIGHT);
        ReflectionHelper.setField(gameLoop, "score", 0);
        clockTickHelper();
        assertEquals(ReflectionHelper.getField(gameLoop, "score"), 5, "score should be increased to 5 after eating special food");
    }


    @Test
    public void testGoingOppositeDirection() throws Exception {
        ReflectionHelper.setField(snake, "direction", Direction.RIGHT);
        ReflectionHelper.setField(gameStage,"key", KeyCode.LEFT);
        clockTickHelper();
        assertTrue(ReflectionHelper.getField(snake, "direction") == Direction.RIGHT,"Snake should be going to Downward direction.");
    }
}
