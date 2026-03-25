package ru.nsu.ksadov.find.task;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

/** Represents a snake entity with a body and movement direction. */
public class Snake {
    private final Deque<Point> body = new ConcurrentLinkedDeque<>();
    private Direction direction = Direction.RIGHT;

    public Snake(Point startPos) {
        body.add(startPos);
    }

    private boolean isOpposite(Direction curr, Direction next) {
        return (curr == Direction.UP && next == Direction.DOWN) ||
                (curr == Direction.DOWN && next == Direction.UP) ||
                (curr == Direction.LEFT && next == Direction.RIGHT) ||
                (curr == Direction.RIGHT && next == Direction.LEFT);
    }

    /** Moves the snake one step forward. */
    public void move(boolean mustGrow) {
        Point head = body.getFirst();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case RIGHT -> new Point(head.x() + 1, head.y());
            case LEFT -> new Point(head.x() - 1, head.y());
        };

        body.addFirst(newHead);
        if (!mustGrow) {
            body.removeLast();
        }
    }

    public Deque<Point> getBody() {
        return body;
    }

    /** Sets the snake's direction, preventing reversal. */
    public void setDirection(Direction newDir) {
        if (!isOpposite(this.direction, newDir)) {
            this.direction = newDir;
        }
    }

    public Direction getDirection() {
        return this.direction;
    }

    /** Grows the snake by one segment at the head in the current direction. */
    public void eat() {
        Point head = body.getFirst();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case RIGHT -> new Point(head.x() + 1, head.y());
            case LEFT -> new Point(head.x() - 1, head.y());
        };
        body.addFirst(newHead);
    }

    /** Checks if the new head position collides with the snake's own body. */
    public boolean checkSelfCollision(Point newHead) {
        return body.contains(newHead);
    }
}