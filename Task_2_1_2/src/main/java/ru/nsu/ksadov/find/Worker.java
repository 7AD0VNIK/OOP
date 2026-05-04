package ru.nsu.ksadov.find;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Worker is a client that tryes to connect to the Server.
 */
public class Worker {
    private final ServerInfo serverInfo;

    /**
     * Instantiates a new Worker.
     */
    public Worker(ServerInfo info) {
        this.serverInfo = info;
    }

    /**
     * Starts the worker process.
     */
    public void start() {
        try (Socket socket = new Socket(serverInfo.addr, serverInfo.tcpPort);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Successful connect. Data waiting..");
            processTasks(in, out);
            System.out.println("All tasks completed");

        } catch (IOException e) {
            System.out.println("Error. Could not connect to server : " + e.getMessage());
        }
    }

    /**
     * Checks if a number is prime.
     */
    public static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Processes tasks from the server.
     */
    private void processTasks(DataInputStream in, DataOutputStream out) throws IOException {
        while (true) {
            TaskChunk taskChunk = MessageSerializer.receiveTaskChunk(in);

            if (taskChunk == null) {
                break;
            }

            Task task = taskChunk.getTask();
            long[] chunk = taskChunk.getChunk();

            System.out.println("Working with range [" + task.getStart() + ", " + task.getEnd()
                    + "], chunk size: " + chunk.length);

            boolean found = false;
            for (long value : chunk) {
                if (!isPrime(value)) {
                    found = true;
                    break;
                }
            }

            out.writeBoolean(found);
            out.flush();

            if (found) {
                System.out.println("Non-prime number found in range ["
                        + task.getStart() + ", " + task.getEnd() + "]");
            }
        }
    }
}