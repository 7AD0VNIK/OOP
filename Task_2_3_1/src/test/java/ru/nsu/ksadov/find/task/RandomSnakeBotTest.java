package ru.nsu.ksadov.find.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RandomSnakeBotTest {
    @Test
    void testRandomBotDoesNotCrash() {
        GameState state = new GameState(20, 20);
        RandomSnakeBot bot = new RandomSnakeBot();
        Snake botSnake = new Snake(new Point(10, 10));
        for (int i = 0; i < 100; i++) {
            Direction dir = bot.chooseDirection(botSnake, state);
            assertNotNull(dir);
        }
    }

    @Test
    void testRandomChangeWithinConstraints() {
        RandomSnakeBot bot = new RandomSnakeBot();
        GameState state = new GameState(20, 20);
        Snake snake = new Snake(new Point(10, 10));
        snake.setDirection(Direction.UP);

        Set<Direction> results = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            results.add(bot.chooseDirection(snake, state));
        }

        assertFalse(results.contains(Direction.DOWN),
                "рандомный бот не должен разворачиваться мгновенно");
        assertTrue(results.size() > 1,
                "бот должен уметь менять направление, а не только ехать прямо");
    }
}