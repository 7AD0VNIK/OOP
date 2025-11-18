package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class SubStringFinderTest {

    /** Вспомогательная функция: записать текст во временный файл. */
    private File createTempFile(String content) throws IOException {
        File file = File.createTempFile("test", ".txt");
        file.deleteOnExit();
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file),
                StandardCharsets.UTF_8)) {
            w.write(content);
        }
        return file;
    }

    @Test
    void testSimple() throws Exception {
        File file = createTempFile("абракадабра");
        List<Long> result = SubStringFinder.find(file.getAbsolutePath(), "бра");

        assertEquals(List.of(1L, 8L), result);
    }

    @Test
    void testNoMatches() throws Exception {
        File file = createTempFile("hello world");
        List<Long> result = SubStringFinder.find(file.getAbsolutePath(), "xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void testBoundaryBetweenBlocks() throws Exception {
        int block = 64 * 1024;
        String prefix = "а".repeat(block - 1);
        String content = prefix + "б" + "ра";

        File file = createTempFile(content);
        List<Long> result = SubStringFinder.find(file.getAbsolutePath(), "бра");

        assertEquals(List.of((long) (block - 1)), result);
    }

    @Test
    void testEmptyPattern() {
        assertThrows(IllegalArgumentException.class, () ->
                SubStringFinder.find("any.txt", "")
        );
    }

    @Test
    void testNullPattern() {
        assertThrows(IllegalArgumentException.class, () ->
                SubStringFinder.find("any.txt", null)
        );
    }

    @Test
    void testHugeUnicodeFile() throws Exception {
        int size = 1_000_000;
        String big = "界".repeat(size) + "你好" + "界".repeat(size);
        File file = createTempFile(big);
        List<Long> result = SubStringFinder.find(file.getAbsolutePath(), "你好");
        assertEquals(List.of((long) size), result);
    }

    @Test
    void testOverlappingMatches() throws Exception {
        String content = "a".repeat(10);
        File file = createTempFile(content);
        List<Long> result = SubStringFinder.find(file.getAbsolutePath(), "aaaa");
        assertEquals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L), result);
    }
    @Test
    void test16GBFileSparse() throws Exception {
        long fileSize = 16L * 1024 * 1024 * 1024;
        String pattern = "XYZ";

        File file = File.createTempFile("16gb_sparse", ".dat");

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(fileSize);

            raf.seek(fileSize - pattern.length());
            raf.write(pattern.getBytes(StandardCharsets.UTF_8));
        }

        try {
            List<Long> result = SubStringFinder.find(file.getAbsolutePath(), pattern);
            long expectedPos = fileSize - pattern.length();
            assertEquals(List.of(expectedPos), result);
        } finally {
            file.delete();
        }
    }

}
