package ru.nsu.ksadov.find.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeTest {
    private Snake snake;

    @BeforeEach
    void setUp() {
        snake = new Snake(new Point(10, 10));
    }

    @Test
    void testInitialState() {
        assertEquals(1, snake.getBody().size());
        assertEquals(new Point(10, 10), snake.getBody().getFirst());
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void testMoveWithoutGrowing() {
        snake.move(false);
        assertEquals(1, snake.getBody().size());
        assertEquals(new Point(11, 10), snake.getBody().getFirst());
    }

    @Test
    void testEatAndGrow() {
        snake.eat();
        assertEquals(2, snake.getBody().size());
        assertEquals(new Point(11, 10), snake.getBody().getFirst());
        assertEquals(new Point(10, 10), snake.getBody().getLast());
    }

    @Test
    void testSetDirectionValid() {
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
        snake.move(false);
        assertEquals(new Point(10, 9), snake.getBody().getFirst());
    }

    @Test
    void testSetDirectionOppositeIsIgnored() {
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void testCheckSelfCollision() {
        snake.eat();
        snake.eat();
        assertTrue(snake.checkSelfCollision(new Point(11, 10)));
        assertFalse(snake.checkSelfCollision(new Point(0, 0)));
    }
}