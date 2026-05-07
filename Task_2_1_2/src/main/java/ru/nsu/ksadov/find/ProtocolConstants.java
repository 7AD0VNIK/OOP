package ru.nsu.ksadov.find;

/**
 * Protocol constants for client-server communication.
 */
public class ProtocolConstants {
    public static final int NO_MORE_TASKS = -1;
    public static final int CHECKSUM_ERROR = -2;
    public static final int CHUNK_SIZE = 1000;
    public static final String MAGIC_NUMBER = "server prime port here:";
    public static final int MULTICAST_PORT = 8888;
    public static final String MULTICAST_GROUP = "224.0.0.1";
    public static final int STATUS_PRINT_INTERVAL_MS = 2000;
}