package ru.nsu.ksadov.find;

/**
 * Class that implements configuration of the task.
 */
public class Task {
    private final int start;
    private final int end;

    /**
     * .
     */
    public Task(int start, int end) {
        this.end = end;
        this.start = start;
    }

    /**
     * .
     */
    public int getStart() {
        return start;
    }

    /**
     * .
     */
    public int getEnd() {
        return end;
    }
}
