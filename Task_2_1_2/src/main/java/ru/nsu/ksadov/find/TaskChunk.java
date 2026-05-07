package ru.nsu.ksadov.find;

/**
 * Class that holds a task with its corresponding data chunk.
 */
public class TaskChunk {
    private final Task task;
    private final long[] chunk;

    public TaskChunk(Task task, long[] chunk) {
        this.task = task;
        this.chunk = chunk;
    }

    public Task getTask() {
        return task;
    }

    public long[] getChunk() {
        return chunk;
    }
}