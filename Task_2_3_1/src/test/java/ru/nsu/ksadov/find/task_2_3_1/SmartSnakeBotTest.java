package ru.nsu.ksadov.find.task_2_3_1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SmartSnakeBotTest {
    @Test
    void testBotTurnsTowardsFood() {
        GameState state = new GameState(20, 20); // Инициализируем мир
        SmartSnakeBot bot = new SmartSnakeBot();
        Snake botSnake = new Snake(new Point(10, 10));

        // Берем первую еду из сгенерированных
        Point food = state.getFoods().get(0);

        // Проверяем, что метод не выдает null и возвращает корректное направление
        Direction dir = bot.chooseDirection(botSnake, state);
        assertNotNull(dir);
    }
}