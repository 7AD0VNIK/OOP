package ru.nsu.ksadov.find;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Implements announce of connection.
 */
public class Announcer implements Runnable {
    private final int tcpPort;
    private final String message;
    private volatile boolean running = true;

    /**
     * Instantiates a new Announcer.
     */
    public Announcer(int tcpPort) {
        this.tcpPort = tcpPort;
        this.message = ProtocolConstants.MAGIC_NUMBER + tcpPort;
    }

    /**
     * .
     */
    public void stop() {
        running = false;
    }

    /**
     * Runs the announcer loop to broadcast server presence.
     */
    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName(ProtocolConstants.MULTICAST_GROUP);
            byte[] buf = message.getBytes();

            System.out.println("MC started...");

            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group,
                        ProtocolConstants.MULTICAST_PORT);
                socket.send(packet);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Announcer error: " + e.getMessage());
        }
    }
}