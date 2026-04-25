package ru.nsu.ksadov.find;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of Server that gaves some tasks to the Workers
 */
public class Server implements Runnable{
    private final ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<>();
    private final ServerSocket serverSocket;
    private final long[] array;
    private final AtomicBoolean primeFound = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, String> activeWorkers = new ConcurrentHashMap<>();

    /**
     * .
     */
    public Server(long[] array, int port) throws IOException {
        this.array = array;
        this.serverSocket = new ServerSocket(port);
        initQueue(1000);
        Thread announcerThread = new Thread(new Announcer(port));
        announcerThread.setDaemon(true);
        announcerThread.start();

        Thread printerThread = new Thread(() -> printer(activeWorkers));
        printerThread.setDaemon(true);
        printerThread.start();
    }

    /**
     * .
     */
    private void initQueue(int chunkSize) {
        for (int i = 0; i < array.length; i += chunkSize) {
            int end = Math.min(i + chunkSize, array.length);
            queue.add(new Task(i, end));
        }
    }

    /**
     * Daemon thread that shows info about current state for evaery 2 seconds.
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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

    }

    /**
     * .
     */
    @Override
    public void run() {
        System.out.println("Server started...");
        int count = 0;
        try {
            while (!primeFound.get()) {
                String wName = "worker" + count;
                Socket socket = serverSocket.accept();
                System.out.println("New worker connected: " + wName);

                ConnectionHandler handler = new ConnectionHandler(socket, array, queue, primeFound, activeWorkers
                        , wName);
                new Thread(handler).start();
                count++;
            }
        } catch (IOException e) {
            if (!primeFound.get()) {
                e.printStackTrace();
            }
        }
    }

    /**
     *.
     */
    public boolean getResult() {
        return primeFound.get();
    }

    /**
     *.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        long[] testArray = new long[10000];
        Arrays.fill(testArray, 17);
        testArray[9999] = 10;

        Server server = new Server(testArray, 9000);
        Thread serverThread = new Thread(server);
        serverThread.start();

        serverThread.join();
        System.out.println("Result: " + server.getResult());
    }
}
