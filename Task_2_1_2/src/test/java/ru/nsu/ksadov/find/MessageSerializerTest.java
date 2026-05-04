package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class MessageSerializerTest {

    @Test
    void testTaskChunkSerializationAndDeserialization() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        Task task = new Task(10, 20);
        long[] chunk = {17L, 19L, 23L, 29L, 31L, 37L, 41L, 43L, 47L, 53L};

        MessageSerializer.sendTaskChunk(out, task, chunk);
        out.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);
        TaskChunk received = MessageSerializer.receiveTaskChunk(in);

        assertNotNull(received);
        assertEquals(10, received.getTask().getStart());
        assertEquals(20, received.getTask().getEnd());
        assertArrayEquals(chunk, received.getChunk());
    }

    @Test
    void testEndOfWorkSignal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        MessageSerializer.sendEndOfWork(out);
        out.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        TaskChunk received = MessageSerializer.receiveTaskChunk(in);
        assertNull(received);
    }

    @Test
    void testMultipleTasksSequentially() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        Task task1 = new Task(0, 5);
        long[] chunk1 = {2L, 3L, 5L, 7L, 11L};
        MessageSerializer.sendTaskChunk(out, task1, chunk1);

        Task task2 = new Task(5, 10);
        long[] chunk2 = {13L, 17L, 19L, 23L, 29L};
        MessageSerializer.sendTaskChunk(out, task2, chunk2);

        MessageSerializer.sendEndOfWork(out);
        out.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        TaskChunk r1 = MessageSerializer.receiveTaskChunk(in);
        assertNotNull(r1);
        assertEquals(0, r1.getTask().getStart());
        assertArrayEquals(chunk1, r1.getChunk());

        TaskChunk r2 = MessageSerializer.receiveTaskChunk(in);
        assertNotNull(r2);
        assertEquals(5, r2.getTask().getStart());
        assertArrayEquals(chunk2, r2.getChunk());

        TaskChunk r3 = MessageSerializer.receiveTaskChunk(in);
        assertNull(r3);
    }
}