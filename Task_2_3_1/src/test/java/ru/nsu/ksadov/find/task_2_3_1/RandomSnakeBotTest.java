package ru.nsu.ksadov.find.task_2_3_1;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}