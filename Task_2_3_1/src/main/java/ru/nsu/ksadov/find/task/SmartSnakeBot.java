package ru.nsu.ksadov.find.task;

/** Defines a bot strategy that actively moves towards the first available food. */
public class SmartSnakeBot implements Bot {
    @Override
    public Direction chooseDirection(Snake bot, GameState state) {
        if (state.getFoods().isEmpty()) {
            return bot.getDirection();
        }

        Point head = bot.getBody().getFirst();
        Point target = state.getFoods().get(0);

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