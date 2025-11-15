package ru.nsu.ksadov.find;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для поиска всех вхождений подстроки в большом UTF-8 файле.
 */
public class SubStringFinder {

    /**
     * Ищет все позиции подстроки pattern в файле fileName.
     *
     * @param fileName путь
     * @param pattern  искомая подстрока (не пустая)
     * @return список индексов (0-based) всех найденных вхождений
     * @throws IOException если произошла ошибка чтения файла
     * @throws IllegalArgumentException если pattern пустой или null
     */
    public static List<Integer> find(String fileName, String pattern) throws IOException{
        if (pattern == null) {
            throw new IllegalArgumentException("pattern == null");
        }

        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must be non empty");
        }

        int patternLen = pattern.length();
        int tailKeep = patternLen - 1; // хвост который сохраняем

        List<Integer> res = new ArrayList<>();

        final int BUFFER_CHARS = 1024 * 64; // 64Kb
        long fileCharOffset = 0L;
        String tail = "";

        try (InputStream fis = new FileInputStream(fileName);
             Reader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {

            char[] buffer = new char[BUFFER_CHARS];
            int readChars;

            while ((readChars = reader.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, readChars);

                String combined = tail.isEmpty() ? chunk : tail + chunk;

                int fromIndex = 0;
                while (true) {
                    int found = combined.indexOf(pattern, fromIndex);
                    if (found == -1) {
                        break;
                    }

                    long globalIndex = fileCharOffset - tail.length() + found;

                    if (globalIndex < 0) {
                        throw new IllegalStateException("computed negative global index: " + globalIndex);
                    }

                    res.add((int) globalIndex);

                    fromIndex = found + 1;
                }

                fileCharOffset += readChars;

                if (tailKeep > 0) {
                    if (combined.length() <= tailKeep) {
                        tail = combined;
                    } else {
                        tail = combined.substring(combined.length() - tailKeep);
                    }
                } else {
                    tail = "";
                }
            }
            return res;
        }
    }
}
