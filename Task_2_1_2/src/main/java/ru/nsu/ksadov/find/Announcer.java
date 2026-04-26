package ru.nsu.ksadov.find;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Implements announce of connection.
 */
public class Announcer implements Runnable {
        private final int tcpPort;
        private final String mulGroup = "224.0.0.1";
        private final int mulPort = 8888;
        private final String magicN = "server prime port here:";

        /**
         * Instantiates a new Announcer.
         */
        public Announcer(int tcpPort) {
            this.tcpPort = tcpPort;
        }

        /**
         * Runs the announcer loop to broadcast server presence.
         */
        @Override
        public void run() {
            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress group = InetAddress.getByName(mulGroup);
                String msg = magicN + tcpPort;
                byte[] buf = msg.getBytes();

                System.out.println("MC started...");

                while (!Thread.currentThread().isInterrupted()) {
                    DatagramPacket packet = new DatagramPacket(buf,
                            buf.length, group, mulPort);
                    socket.send(packet);

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException e) {
                System.out.println("MC announcer error" + e.getMessage());
            }
        }
}
