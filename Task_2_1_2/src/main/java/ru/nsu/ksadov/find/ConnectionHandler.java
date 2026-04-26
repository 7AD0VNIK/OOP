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
    public ConnectionHandler(Socket socket, long[] array, ConcurrentLinkedQueue<Task> queue,
                             AtomicBoolean primeFound, ConcurrentHashMap<String, String> activeWorkers,
                             String workerName) {
        this.socket = socket;
        this.array = array;
        this.queue = queue;
        this.primeFound = primeFound;
        this.activeWorkers = activeWorkers;
        this.workerName = workerName;
    }

    /**
     * Handles conversation with the connected worker.
     */
    public void handleConversation(Socket socket) {
        Task currTask = null;
        try (socket) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            sendInitData(this.array, out);
            while (!primeFound.get()) {
                currTask = (Task) queue.poll();

                if (currTask == null) {
                    out.writeInt(-1);
                    out.flush();
                    break;
                }

                activeWorkers.put(workerName, "Range: [" + currTask.getStart() + " : " + currTask.getEnd() + "]");

                out.writeInt(currTask.getStart());
                out.writeInt(currTask.getEnd());
                out.flush();

                boolean found = in.readBoolean();
                if (found) {
                    primeFound.set(true);
                    System.out.println("Worker found prime! Stopping");
                    break;
                }

                currTask = null;
            }
            out.flush();
        } catch (IOException e) {
            System.out.println(workerName + "Worker disconnected or error happen");
            if (currTask != null) {
                System.out.println("Task returning [" + currTask.getStart() + "] to queue");
                queue.add(currTask);
            }
        } finally {
            activeWorkers.remove(workerName);
        }
    }

    /**
     * Runs the connection handler thread.
     */
    @Override
    public void run() {
        handleConversation(socket);
    }

    /**
     * Calculates the checksum of the given array.
     */
    private long checkSum(long[] arr) {
        long sum = 0;
        for(long num : arr) {
            sum += num;
        }
        return sum;
    }

    /**
     * Sends initial data to the worker.
     */
    public void sendInitData(long[] arr, DataOutputStream out) throws IOException {
        out.writeInt(arr.length);
        for (long num : arr) {
            out.writeLong(num);
        }
        out.writeLong(checkSum(arr));
        out.flush();
        System.out.println("Arr sent to worker. Which size:" + arr.length);
    }
}