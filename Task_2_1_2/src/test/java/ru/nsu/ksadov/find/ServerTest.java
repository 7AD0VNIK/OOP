package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * .
 */
class ServerTest {

    @Test
    void testServerWorkerCalculations() throws IOException, InterruptedException {
        long[] testArray = new long[500];
        Arrays.fill(testArray, 17);
        testArray[499] = 10;

        int testPort = 9005;

        Server server = new Server(testArray, testPort);
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread.sleep(500);

        ServerInfo info = new ServerInfo(InetAddress.getByName("127.0.0.1"), testPort);
        Worker worker = new Worker(info);

        Thread workerThread = new Thread(worker::start);
        workerThread.start();

        int maxWaitIterations = 50;
        while (!server.getResult() && maxWaitIterations > 0) {
            Thread.sleep(100);
            maxWaitIterations--;
        }

        assertTrue(server.getResult(), "Server should found prime with worker help");

        workerThread.interrupt();
    }
}