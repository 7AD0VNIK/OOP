package ru.nsu.ksadov.find.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmartSnakeBotTest {
    private SmartSnakeBot bot;
    private GameState state;
    private Point food;

    @BeforeEach
    void setUp() {
        bot = new SmartSnakeBot();
        state = new GameState(20, 20);
    }

    @Test
    void testBotTurnsTowardsFood() {
        GameState state = new GameState(20, 20);
        SmartSnakeBot bot = new SmartSnakeBot();
        Snake botSnake = new Snake(new Point(10, 10));
        Point food = state.getFoods().get(0);
        Direction dir = bot.chooseDirection(botSnake, state);
        assertNotNull(dir);
    }

    @Test
    void testMoveTowardsFoodHorizontal() {
        Snake snake = new Snake(new Point(5, 5));
        state.getFoods().clear();
        state.getFoods().add(new Point(10, 5));

        Direction result = bot.chooseDirection(snake, state);
        assertEquals(Direction.RIGHT, result);
    }

    @Test
    void testNoOppositeDirection() {
        Snake snake = new Snake(new Point(5, 5));
        snake.setDirection(Direction.RIGHT);

        state.getFoods().clear();
        state.getFoods().add(new Point(2, 5));

        Direction result = bot.chooseDirection(snake, state);
        assertNotEquals(Direction.LEFT, result);
    }
}