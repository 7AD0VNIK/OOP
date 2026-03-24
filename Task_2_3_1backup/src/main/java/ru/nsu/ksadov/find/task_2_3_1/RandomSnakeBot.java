package ru.nsu.ksadov.find.task_2_3_1;

import java.util.Random;

/** Defines a bot strategy that moves randomly across the grid. */
public class RandomSnakeBot implements Bot {
    private static final int KEEP_DIRECTION_CHANCE = 80;
    private static final int MAX_PERCENTAGE = 100;
    private final Random random = new Random();

    @Override
    public Direction chooseDirection(Snake bot, GameState state) {
        if (random.nextInt(MAX_PERCENTAGE) < KEEP_DIRECTION_CHANCE) {
            return bot.getDirection();
        }

        Direction current = bot.getDirection();
        boolean turnLeft = random.nextBoolean();

        return switch (current) {
            case UP -> turnLeft ? Direction.LEFT : Direction.RIGHT;
            case DOWN -> turnLeft ? Direction.RIGHT : Direction.LEFT;
            case LEFT -> turnLeft ? Direction.DOWN : Direction.UP;
            case RIGHT -> turnLeft ? Direction.UP : Direction.DOWN;
        };
    }
}