package ru.nsu.ksadov.find.task;

/** Interface for defining snake bot behavior strategies. */
public interface Bot {
    Direction chooseDirection(Snake bot, GameState state);
}