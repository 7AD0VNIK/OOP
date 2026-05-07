package ru.nsu.ksadov.find;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Utility class for message serialization/deserialization.
 */
public class MessageSerializer {

    /**
     * Sends a task chunk to the worker.
     */
    public static void sendTaskChunk(DataOutputStream out, Task task, long[] chunk)
            throws IOException {
        out.writeInt(task.getStart());
        out.writeInt(task.getEnd());
        out.writeInt(chunk.length);
        for (long value : chunk) {
            out.writeLong(value);
        }
        out.flush();
    }

    /**
     * Sends end of work signal.
     */
    public static void sendEndOfWork(DataOutputStream out) throws IOException {
        out.writeInt(ProtocolConstants.NO_MORE_TASKS);
        out.flush();
    }

    /**
     * Receives task chunk from server.
     * Returns null if no more tasks.
     */
    public static TaskChunk receiveTaskChunk(DataInputStream in) throws IOException {
        int start = in.readInt();
        if (start == ProtocolConstants.NO_MORE_TASKS) {
            return null;
        }

        int end = in.readInt();
        int chunkSize = in.readInt();
        long[] chunk = new long[chunkSize];

        for (int i = 0; i < chunkSize; i++) {
            chunk[i] = in.readLong();
        }

        return new TaskChunk(new Task(start, end), chunk);
    }
}