package ru.nsu.ksadov.find.task_2_3_1;

import java.util.LinkedList;

/** Represents a snake entity with a body and movement direction. */
public class Snake {
    private final LinkedList<Point> body = new LinkedList<>();
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

    public LinkedList<Point> getBody() {
        return body;
    }

    public void setDirection(Direction newDir) {
        if (!isOpposite(this.direction, newDir)) {
            this.direction = newDir;
        }
    }

    public Direction getDirection() {
        return this.direction;
    }

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

    public boolean checkSelfCollision(Point newHead) {
        return body.contains(newHead);
    }
}