package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

/**
 * .
 */
class ServerInfoTest {
    @Test
    void testServerInfoCreation() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("127.0.0.1");
        int port = 9000;

        ServerInfo info = new ServerInfo(address, port);

        assertEquals(address, info.addr, "IP should be the same");
        assertEquals(port, info.tcpPort, "Ports should be the same");
    }
}