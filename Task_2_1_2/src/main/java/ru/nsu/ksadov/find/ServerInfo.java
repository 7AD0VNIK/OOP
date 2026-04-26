package ru.nsu.ksadov.find;

import java.net.InetAddress;

/**
 * Class that implements info about the Server.
 */
public class ServerInfo {
    public final InetAddress addr;
    public final int tcpPort;

    /**
     * Instantiates a new ServerInfo.
     */
    public ServerInfo(InetAddress inet, int tcpPort) {
        this.addr = inet;
        this.tcpPort = tcpPort;
    }
}