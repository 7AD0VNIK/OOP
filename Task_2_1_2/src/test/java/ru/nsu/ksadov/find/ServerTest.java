package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * .
 */
class ServerTest {

    @Test
    void testServerWorkerCalculations() throws IOException, InterruptedException {
        long[] testArray = new long[500];
        Arrays.fill(testArray, 17L);
        testArray[499] = 10L;

        int testPort = 9005;
        Server server = new Server(testArray, testPort);
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread.sleep(500);

        ServerInfo info = new ServerInfo(InetAddress.getByName("127.0.0.1"), testPort);
        Worker worker = new Worker(info);
        Thread workerThread = new Thread(worker::start);
        workerThread.start();

        int maxWait = 50;
        while (!server.getResult() && maxWait > 0) {
            Thread.sleep(100);
            maxWait--;
        }

        assertTrue(server.getResult());
        assertEquals(0, server.getActiveWorkers().size());

        workerThread.interrupt();
        server.stop();
        serverThread.join(1000);
    }

    @Test
    void testWorkerDisconnectionGracefulHandling() throws IOException, InterruptedException {
        long[] largePrimeArray = new long[10000];
        Arrays.fill(largePrimeArray, 17L);

        int testPort = 9006;
        Server server = new Server(largePrimeArray, testPort);
        Thread serverThread = new Thread(server);
        serverThread.start();
        Thread.sleep(500);

        Socket workerSocket = new Socket("127.0.0.1", testPort);
        DataInputStream in = new DataInputStream(workerSocket.getInputStream());

        Thread.sleep(1000);

        workerSocket.close();
        Thread.sleep(1000);

        assertTrue(serverThread.isAlive());
        assertFalse(server.getResult());

        server.stop();
        serverThread.join(1000);
    }

    @Test
    void testServerContinuesAfterWorkerDisconnect() throws IOException, InterruptedException {
        long[] testArray = new long[200];
        Arrays.fill(testArray, 17L);
        testArray[150] = 4L;

        int testPort = 9006;
        Server server = new Server(testArray, testPort);
        Thread serverThread = new Thread(server);
        serverThread.start();
        Thread.sleep(500);

        Socket worker1 = new Socket("127.0.0.1", testPort);
        Thread.sleep(500);
        worker1.close();
        Thread.sleep(500);

        ServerInfo info = new ServerInfo(InetAddress.getByName("127.0.0.1"), testPort);
        Worker worker2 = new Worker(info);
        Thread worker2Thread = new Thread(worker2::start);
        worker2Thread.start();

        int maxWait = 50;
        while (!server.getResult() && maxWait > 0) {
            Thread.sleep(100);
            maxWait--;
        }

        assertTrue(server.getResult());
        assertEquals(0, server.getActiveWorkers().size());

        worker2Thread.interrupt();
        server.stop();
        serverThread.join(1000);
    }
}