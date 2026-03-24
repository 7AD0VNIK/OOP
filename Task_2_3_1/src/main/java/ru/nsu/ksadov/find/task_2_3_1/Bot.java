package ru.nsu.ksadov.find.task_2_3_1;

/** Interface for defining snake bot behavior strategies. */
public interface Bot {
    Direction chooseDirection(Snake bot, GameState state);
}