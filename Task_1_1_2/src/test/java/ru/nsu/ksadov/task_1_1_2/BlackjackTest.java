package ru.nsu.ksadov.task_1_1_2;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackSimpleTest {

    @Test
    void gameCanBeInitialized() {
        // простая проверка что игра запускается без ошибок
        assertDoesNotThrow(() -> {
            System.setIn(new ByteArrayInputStream("0\n0\n".getBytes()));
            Blackjack.main(new String[]{});
        });
    }

    @Test
    void playerCanChooseStand() {
        assertDoesNotThrow(() -> {
            System.setIn(new ByteArrayInputStream("0\n0\n".getBytes()));
            Blackjack.main(new String[]{});
        });
    }

    @Test
    void playerCanChooseHit() {
        assertDoesNotThrow(() -> {
            System.setIn(new ByteArrayInputStream("1\n0\n0\n".getBytes()));
            Blackjack.main(new String[]{});
        });
    }
}