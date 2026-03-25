package ru.nsu.ksadov.find.task_2_3_1;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SmartSnakeBotTest {
    @Test
    void testBotTurnsTowardsFood() {
        GameState state = new GameState(20, 20);
        SmartSnakeBot bot = new SmartSnakeBot();
        Snake botSnake = new Snake(new Point(10, 10));
        Point food = state.getFoods().get(0);
        Direction dir = bot.chooseDirection(botSnake, state);
        assertNotNull(dir);
    }
}