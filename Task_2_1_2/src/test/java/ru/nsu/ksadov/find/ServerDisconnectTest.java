package ru.nsu.ksadov.find;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerDisconnectTest {

    @Test
    public void testWorkerDisconnectRequeuesTask() throws Exception {
        long[] array = new long[3000];
        Arrays.fill(array, 7L);

        int port = 9090;
        Server server = new Server(array, port);
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread.sleep(500);

        try (Socket badSocket = new Socket("localhost", port);
             DataInputStream in = new DataInputStream(badSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(badSocket.getOutputStream())) {

            TaskChunk chunk = MessageSerializer.receiveTaskChunk(in);
            assertNotNull(chunk);
        }

        Thread.sleep(500);
        Worker goodWorker = new Worker(new ServerInfo(java.net.InetAddress.getByName("localhost"), port));
        Thread workerThread = new Thread(() -> goodWorker.start());
        workerThread.start();

        Thread.sleep(2000);
        boolean actualResult = server.getResult();
        server.stop();
        serverThread.join();
        workerThread.join();

        assertFalse(server.getResult());

    }
}