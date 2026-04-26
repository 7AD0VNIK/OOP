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
    private long[] array;

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

            receiveData(in);

            System.out.println("Data loaded and correct");
            processTask(in, out);
        } catch (IOException e) {
            System.out.println("Error. Could not connect to server : " + e.getMessage());
        }
    }

    /**
     * Receives data from the server.
     */
    private void receiveData(DataInputStream in) throws IOException {
        int siz = in.readInt();
        this.array = new long[siz];
        long calcChSum = 0;

        for (int i = 0; i < siz; i++) {
            array[i] = in.readLong();
            calcChSum += array[i];
        }
        long realChSum = in.readLong();
        if (calcChSum != realChSum) {
            throw new IOException("Error: data integrity is compromised. Checksums are different");
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
     * Processes tasks from the server, -1 occurs when work is done.
     */
    private void processTask(DataInputStream in, DataOutputStream out) throws IOException {
        while (true) {
            int start = in.readInt();
            if (start == -1) {
                break;
            }

            int end = in.readInt();

            System.out.println("work with range [" + start + ", " + end + "]");

            boolean found = false;
            for (int i = start; i < end; i++) {
                if (!isPrime(array[i])) {
                    found = true;
                    break;
                }
            }

            out.writeBoolean(found);
            out.flush();
        }
    }

    /**
     * Main method to run the Worker.
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("Try to find server...");
            ServerInfo info = Discoverer.discover();

            if (info != null) {
                Worker worker = new Worker(info);
                worker.start();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}