package ru.nsu.ksadov.find.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameStateTest {
    private GameState state;

    @BeforeEach
    void setUp() {
        state = new GameState(20, 20);
    }

    @Test
    void testInitialization() {
        assertNotNull(state.getSnake());
        assertEquals(3, state.getFoods().size());
        assertEquals(10, state.getObstacles().size());
        assertFalse(state.isGameOver());
        assertFalse(state.isGameWon());
        assertEquals(1, state.getScore());
    }

    @Test
    void testStepMovesSnake() {
        Point initialHead = state.getSnake().getBody().getFirst();
        state.step();
        Point newHead = state.getSnake().getBody().getFirst();

        assertNotEquals(initialHead, newHead);
    }

    @Test
    void testWallCollisionCausesGameOver() {
        for (int i = 0; i < 15; i++) {
            state.getSnake().setDirection(Direction.RIGHT);
            state.step();
        }

        assertTrue(state.isGameOver());
    }

    @Test
    void testStopUpdatingAfterGameOver() {
        for (int i = 0; i < 15; i++) {
            state.step();
        }
        assertTrue(state.isGameOver());

        Point headAfterDeath = state.getSnake().getBody().getFirst();
        state.step();
        assertEquals(headAfterDeath, state.getSnake().getBody().getFirst());
    }
}