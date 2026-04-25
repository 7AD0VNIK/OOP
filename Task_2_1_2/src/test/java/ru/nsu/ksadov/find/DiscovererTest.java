package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * .
 */
class DiscovererTest {

    @Test
    void testAnnouncerAndDiscoverer() throws InterruptedException {
        int testTcpPort = 9000;
        Thread announcerThread = new Thread(new Announcer(testTcpPort));
        announcerThread.start();

        Thread.sleep(500);

        final ServerInfo[] foundInfo = new ServerInfo[1];
        Thread discovererThread = new Thread(() -> {
            foundInfo[0] = Discoverer.discover();
        });
        discovererThread.start();

        discovererThread.join(3000);

        announcerThread.interrupt();

        assertNotNull(foundInfo[0], "Server does not found with Multicast");
        assertEquals(testTcpPort, foundInfo[0].tcpPort, "Wrong port was found");
    }
}