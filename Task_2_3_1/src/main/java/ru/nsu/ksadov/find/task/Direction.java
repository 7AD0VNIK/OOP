package ru.nsu.ksadov.find.task;

/** Defines the possible movement directions for the snake. */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    /** Defines opposite way.*/
    public boolean isOpposite(Direction other) {
        return (this == UP && other == DOWN)
                || (this == DOWN && other == UP)
                || (this == LEFT && other == RIGHT)
                || (this == RIGHT && other == LEFT);
    }
}