package ru.nsu.ksadov.find;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Class that implements connection to the Server using socket.
 */
public class Discoverer {
    private static final int mulPort = 8888;
    private static final String mulGroup = "224.0.0.1";
    private static final String magicN = "server prime port here:";

    /**
     *.
     */
    public static ServerInfo discover() {
        try (MulticastSocket socket = new MulticastSocket(mulPort)) {
            InetAddress gr = InetAddress.getByName(mulGroup);

            socket.joinGroup(gr);
            byte[] buf = new byte[256];
            System.out.println("worker trying to find server ");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());

                if (received.startsWith(magicN)) {
                    int tcpPort = Integer.parseInt(received.substring(magicN.length()));
                    InetAddress servAdr = packet.getAddress();
                    System.out.println("connected!" + servAdr.getHostAddress() +
                            " TCP port:" + tcpPort);
                    socket.leaveGroup(gr);
                    return new ServerInfo(servAdr, tcpPort);
                }
            }
        } catch (Exception e) {
            System.out.println("cannot find server ");
            e.printStackTrace();
            return null;
        }
    }
}
