package ru.nsu.ksadov.find.task;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/** Represents a snake entity with a body and movement direction. */
public class Snake {
    private final Deque<Point> body = new ConcurrentLinkedDeque<>();
    private Direction direction = Direction.RIGHT;

    public Snake(Point startPos) {
        body.add(startPos);
    }

    /** Defines next head position. */
    public Point getNextHeadPosition(Direction dir) {
        Point head = body.getFirst();
        return switch (dir) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case RIGHT -> new Point(head.x() + 1, head.y());
            case LEFT -> new Point(head.x() - 1, head.y());
        };
    }

    /** Moves the snake one step forward. */
    public void move(boolean mustGrow) {
        Point head = body.getFirst();
        Point newHead = getNextHeadPosition(direction);

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
        if (!this.direction.isOpposite(newDir)) {
            this.direction = newDir;
        }
    }

    public boolean occupies(Point p) {
        return body.contains(p);
    }

    public Point getHead() {
        return body.getFirst();
    }

    public Direction getDirection() {
        return this.direction;
    }

    /** Grows the snake by one segment at the head in the current direction. */
    public void eat() {
        Point newHead = getNextHeadPosition(direction);
        body.addFirst(newHead);
    }

    /** Checks if the new head position collides with the snake's own body. */
    public boolean checkSelfCollision(Point newHead) {
        if (!body.isEmpty() && newHead.equals(body.getLast())) {
            return false;
        }
        return body.contains(newHead);
    }
}