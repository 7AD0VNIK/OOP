package ru.nsu.ksadov.find.task_2_3_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomSnakeBotTest {
    @Test
    void testRandomBotDoesNotCrash() {
        GameState state = new GameState(20, 20);
        RandomSnakeBot bot = new RandomSnakeBot();
        Snake botSnake = new Snake(new Point(10, 10));

        // Вызовем 100 раз, чтобы покрыть все ветки вероятностей (80% и 20%)
        for (int i = 0; i < 100; i++) {
            Direction dir = bot.chooseDirection(botSnake, state);
            assertNotNull(dir);
        }
    }
}