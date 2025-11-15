package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class SubStringFinderTest {

    /** Вспомогательная функция: записать текст во временный файл */
    private File createTempFile(String content) throws IOException {
        File file = File.createTempFile("test", ".txt");
        file.deleteOnExit();
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            w.write(content);
        }
        return file;
    }

    @Test
    void testSimple() throws Exception {
        File file = createTempFile("абракадабра");
        List<Integer> result = SubStringFinder.find(file.getAbsolutePath(), "бра");

        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testNoMatches() throws Exception {
        File file = createTempFile("hello world");
        List<Integer> result = SubStringFinder.find(file.getAbsolutePath(), "xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void testBoundaryBetweenBlocks() throws Exception {
        // Сделаем строку так, чтобы "ра" пересекало границу блока.
        // BUFFER_CHARS = 64*1024 => делаем длину ровно 64k-1 символ.
        int block = 64 * 1024;
        String prefix = "а".repeat(block - 1); // много 'а'
        String content = prefix + "б" + "ра";  // "б" в одном блоке, "ра" в следующем

        File file = createTempFile(content);
        List<Integer> result = SubStringFinder.find(file.getAbsolutePath(), "бра");

        assertEquals(List.of(block - 1), result); // позиция "б"
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
}
