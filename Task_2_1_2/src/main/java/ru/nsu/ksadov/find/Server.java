package ru.nsu.ksadov.find;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of Server that gives some tasks to the Workers.
 */
public class Server implements Runnable {
    private final ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();
    private final ServerSocket serverSocket;
    private final long[] array;
    private final AtomicBoolean primeFound = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, String> activeWorkers = new ConcurrentHashMap<>();
    private final ExecutorService threadPool;
    private volatile boolean serverRunning = true;

    /**
     * Instantiates a new Server.
     */
    public Server(long[] array, int port) throws IOException {
        this.array = array;
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2);
        initQueue(ProtocolConstants.CHUNK_SIZE);

        Thread announcerThread = new Thread(new Announcer(port));
        announcerThread.setDaemon(true);
        announcerThread.start();

        Thread printerThread = new Thread(() -> printer(activeWorkers));
        printerThread.setDaemon(true);
        printerThread.start();
    }

    /**
     * Initializes the task queue with chunks.
     */
    private void initQueue(int chunkSize) {
        for (int i = 0; i < array.length; i += chunkSize) {
            int end = Math.min(i + chunkSize, array.length);
            queue.add(new Task(i, end));
        }
    }

    /**
     * Daemon thread that shows info about current state for every 2 seconds.
     */
    private void printer(ConcurrentHashMap<String, String> activeWorkers) {
        while (!primeFound.get()) {
            System.out.println("\n--- Curr state ---");
            if (activeWorkers.isEmpty()) {
                System.out.println("No connected workers.");
            } else {
                for (String key : activeWorkers.keySet()) {
                    System.out.println(key + " -> " + activeWorkers.get(key));
                }
            }
            System.out.println("Tasks remaining: " + queue.size());

            try {
                Thread.sleep(ProtocolConstants.STATUS_PRINT_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
     * Runs the server logic.
     */
    @Override
    public void run() {
        System.out.println("Server started...");
        int count = 0;

        try {
            while (!primeFound.get() && serverRunning) {
                String workerName = "worker" + count;
                Socket socket = serverSocket.accept();
                System.out.println("New worker connected: " + workerName);

                ConnectionHandler handler = new ConnectionHandler(
                        socket, array, queue, primeFound, activeWorkers, workerName);

                threadPool.submit(handler);
                count++;
            }
        } catch (IOException e) {
            if (serverRunning && !primeFound.get()) {
                System.err.println("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            serverRunning = false;
            shutdown();
        }
    }

    /**
     * Gracefully shuts down the server.
     */
    private void shutdown() {
        System.out.println("Shutting down server...");
        try {
            serverSocket.close();
        } catch (IOException e) {
            //ignore
        }
        threadPool.shutdown();
    }

    /**
     * Gets the result indicating if a prime was found.
     */
    public boolean getResult() {
        return primeFound.get();
    }

    /**
     * Stops the server gracefully.
     */
    public void stop() {
        serverRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            //ignore
        }
        threadPool.shutdownNow();
    }

    /**
     * Gets the map of active workers (useful for testing/monitoring).
     */
    public ConcurrentHashMap<String, String> getActiveWorkers() {
        return activeWorkers;
    }
}