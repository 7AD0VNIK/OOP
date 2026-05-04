package ru.nsu.ksadov.find;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that implements mechanism of interaction Server with Workers.
 */
public class ConnectionHandler implements Runnable {
    private Socket socket;
    private long[] array;
    private final ConcurrentLinkedQueue<Task> queue;
    private final AtomicBoolean primeFound;
    private final ConcurrentHashMap<String, String> activeWorkers;
    private String workerName;

    /**
     * Instantiates a new ConnectionHandler.
     */
    public ConnectionHandler(Socket socket, long[] array,
                             ConcurrentLinkedQueue<Task> queue,
                             AtomicBoolean primeFound,
                             ConcurrentHashMap<String, String> activeWorkers,
                             String workerName) {
        this.socket = socket;
        this.array = array;
        this.queue = queue;
        this.primeFound = primeFound;
        this.activeWorkers = activeWorkers;
        this.workerName = workerName;
    }

    /**
     * .
     */
    @Override
    public void run() {
        try {
            handleConversation(socket);
        } catch (IOException e) {
            System.out.println("Error handling worker " + workerName + ": " + e.getMessage());
        } finally {
            activeWorkers.remove(workerName);
            try {
                socket.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    /**
     * Handles conversation with the connected worker.
     */
    private void handleConversation(Socket socket) throws IOException {
        Task currTask = null;
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            activeWorkers.put(workerName, "Working");

            while (!primeFound.get()) {
                currTask = queue.poll();

                if (currTask == null) {
                    MessageSerializer.sendEndOfWork(out);
                    break;
                }

                long[] chunk = getChunkForTask(currTask);
                MessageSerializer.sendTaskChunk(out, currTask, chunk);

                boolean foundNonPrime = in.readBoolean();

                if (foundNonPrime) {
                    primeFound.set(true);
                    activeWorkers.put(workerName, "Found non-prime in ["
                            + currTask.getStart() + ", " + currTask.getEnd() + "]");
                    break;
                }

                activeWorkers.put(workerName, "Completed ["
                        + currTask.getStart() + ", " + currTask.getEnd() + "]");

                currTask = null;
            }

        } catch (IOException e) {
            if (currTask != null) {
                System.out.println("Worker disconnected. Returning task to queue: ["
                        + currTask.getStart() + ", " + currTask.getEnd() + "]");
                queue.add(currTask);
            }
            throw e;
        } finally {
            activeWorkers.remove(workerName);
        }
    }

    /**
     * Gets the chunk of array for the given task.
     */
    private long[] getChunkForTask(Task task) {
        int size = task.getEnd() - task.getStart();
        long[] chunk = new long[size];
        System.arraycopy(array, task.getStart(), chunk, 0, size);
        return chunk;
    }
}