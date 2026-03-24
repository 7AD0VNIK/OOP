package ru.nsu.ksadov.find.task_2_3_1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Manages the core game logic, state, entities, and collision detection. */
public class GameState {
    private static final int MAX_FOOD = 3;
    private static final int TARGET_LENGTH = 10;
    private static final int COUNT_OBS = 10;

    private final int width;
    private final int height;
    private final Snake snake;
    private final List<Snake> bots = new ArrayList<>();
    private final List<Bot> botStrategies = new ArrayList<>();
    private final List<Point> foods = new ArrayList<>();
    private final List<Point> obstacles = new ArrayList<>();
    private final Random random = new Random();

    private int score = 1;
    private boolean gameOver = false;
    private boolean gameWon = false;

    public GameState(int width, int height) {
        this.width = width;
        this.height = height;
        this.snake = new Snake(new Point(width / 2, height / 2));

        bots.add(new Snake(new Point(5, 5)));
        botStrategies.add(new SmartSnakeBot());

        bots.add(new Snake(new Point(15, 15)));
        botStrategies.add(new RandomSnakeBot());

        spawnObs();
        spawnFood();
    }

    private void spawnFood() {
        while (foods.size() < MAX_FOOD) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point newFood = new Point(x, y);
            if (!snake.getBody().contains(newFood) && !foods.contains(newFood) && !obstacles.contains(newFood)) {
                foods.add(newFood);
            }
        }
    }

    private void spawnObs() {
        while (obstacles.size() < COUNT_OBS) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point newObs = new Point(x, y);
            if (!snake.getBody().contains(newObs) && !obstacles.contains(newObs)) {
                obstacles.add(newObs);
            }
        }
    }

    public void step() {
        if (gameOver || gameWon) {
            return;
        }

        moveSnake(snake, snake.getDirection());

        Iterator<Snake> botIterator = bots.iterator();
        Iterator<Bot> strategyIterator = botStrategies.iterator();

        while (botIterator.hasNext() && strategyIterator.hasNext()) {
            Snake bot = botIterator.next();
            Bot strategy = strategyIterator.next();

            Direction nextDir = strategy.chooseDirection(bot, this);
            bot.setDirection(nextDir);

            if (!moveSnake(bot, nextDir)) {
                botIterator.remove();
                strategyIterator.remove();
            }
        }
    }

    private boolean moveSnake(Snake currentSnake, Direction direction) {
        Point head = currentSnake.getBody().getFirst();
        Point nextPoint = switch (direction) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case LEFT -> new Point(head.x() - 1, head.y());
            case RIGHT -> new Point(head.x() + 1, head.y());
        };

        if (nextPoint.x() < 0 || nextPoint.x() >= width || nextPoint.y() < 0 || nextPoint.y() >= height) {
            if (currentSnake == snake) gameOver = true;
            return false;
        }

        if (currentSnake.checkSelfCollision(nextPoint) || obstacles.contains(nextPoint)) {
            if (currentSnake == snake) gameOver = true;
            return false;
        }

        if (foods.contains(nextPoint)) {
            foods.remove(nextPoint);
            currentSnake.eat();
            spawnFood();
            if (currentSnake == snake) {
                score++;
                if (snake.getBody().size() >= TARGET_LENGTH) {
                    gameWon = true;
                }
            }
        } else {
            currentSnake.move(false);
        }
        return true;
    }

    public Snake getSnake() {
        return snake;
    }

    public List<Point> getFoods() {
        return foods;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public List<Point> getObstacles() {
        return obstacles;
    }

    public List<Snake> getBots() {
        return bots;
    }
}