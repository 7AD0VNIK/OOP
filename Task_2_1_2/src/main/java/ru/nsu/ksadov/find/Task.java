package ru.nsu.ksadov.find;

/**
 * Class that implements configuration of the task.
 */
public class Task {
    private final int start;
    private final int end;

    /**
     * Instantiates a new Task.
     */
    public Task(int start, int end) {
        this.end = end;
        this.start = start;
    }

    /**
     * Gets the start index.
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the end index.
     */
    public int getEnd() {
        return end;
    }
}