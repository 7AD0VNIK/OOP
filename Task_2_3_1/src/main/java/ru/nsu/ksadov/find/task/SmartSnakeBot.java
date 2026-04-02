package ru.nsu.ksadov.find.task;

import java.util.Random;

/** Defines a bot strategy that actively moves towards the first available food. */
public class SmartSnakeBot implements Bot {
    private final Random rnd = new Random();

    @Override
    public Direction chooseDirection(Snake bot, GameState state) {
        if (state.getFoods().isEmpty()) {
            return bot.getDirection();
        }
        Point head = bot.getHead();
        Point target = state.getFoods().get(rnd.nextInt(state.getFoods().size()));


        if (target.x() > head.x() && bot.getDirection() != Direction.LEFT) {
            return Direction.RIGHT;
        }
        if (target.x() < head.x() && bot.getDirection() != Direction.RIGHT) {
            return Direction.LEFT;
        }
        if (target.y() > head.y() && bot.getDirection() != Direction.UP) {
            return Direction.DOWN;
        }
        if (target.y() < head.y() && bot.getDirection() != Direction.DOWN) {
            return Direction.UP;
        }

        return bot.getDirection();
    }
}