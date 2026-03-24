package ru.nsu.ksadov.find.task_2_3_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    private GameState state;

    @BeforeEach
    void setUp() {
        state = new GameState(20, 20);
    }

    @Test
    void testInitialization() {
        assertNotNull(state.getSnake());
        assertEquals(3, state.getFoods().size()); // Должно быть 3 еды (MAX_FOOD)
        assertEquals(10, state.getObstacles().size()); // 10 препятствий
        assertFalse(state.isGameOver());
        assertFalse(state.isGameWon());
        assertEquals(1, state.getScore());
    }

    @Test
    void testStepMovesSnake() {
        Point initialHead = state.getSnake().getBody().getFirst();
        state.step();
        Point newHead = state.getSnake().getBody().getFirst();

        // Змейка должна была сдвинуться с места
        assertNotEquals(initialHead, newHead);
    }

    @Test
    void testWallCollisionCausesGameOver() {
        // Ширина 20, змейка стартует в (10, 10) и смотрит вправо.
        // Заставим её сделать 15 шагов вправо, она гарантированно врежется в правую стену.
        for (int i = 0; i < 15; i++) {
            state.getSnake().setDirection(Direction.RIGHT);
            state.step();
        }

        assertTrue(state.isGameOver());
    }

    @Test
    void testStopUpdatingAfterGameOver() {
        // Убиваем змейку об стену
        for (int i = 0; i < 15; i++) {
            state.step();
        }
        assertTrue(state.isGameOver());

        Point headAfterDeath = state.getSnake().getBody().getFirst();

        // Пытаемся сделать еще один шаг
        state.step();

        // Убеждаемся, что игра игнорирует шаги после Game Over
        assertEquals(headAfterDeath, state.getSnake().getBody().getFirst());
    }
}