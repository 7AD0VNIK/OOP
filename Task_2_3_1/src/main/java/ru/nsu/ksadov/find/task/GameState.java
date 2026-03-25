package ru.nsu.ksadov.find.task;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/** Manages the core game logic, state, entities, and collision detection. */
public class GameState {
    private static final int MAX_FOOD = 3;
    private static final int TARGET_LENGTH = 10;
    private static final int COUNT_OBS = 10;

    private final int width;
    private final int height;
    private final Snake snake;
    private final List<Snake> bots = new CopyOnWriteArrayList<>();
    private final List<Bot> botStrategies = new CopyOnWriteArrayList<>();
    private final List<Point> foods = new CopyOnWriteArrayList<>();
    private final List<Point> obstacles = new CopyOnWriteArrayList<>();

    private final Random random = new Random();

    private final AtomicInteger score = new AtomicInteger(1);
    private final AtomicBoolean gameOver = new AtomicBoolean(false);
    private final AtomicBoolean gameWon = new AtomicBoolean(false);

    /**
     * Constructs a new game state with the specified grid size.
     */
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

    /** Spawns food items until MAX_FOOD is reached. */
    private void spawnFood() {
        while (foods.size() < MAX_FOOD) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point newFood = new Point(x, y);
            if (!snake.getBody().contains(newFood) && !foods.contains(newFood)
                    && !obstacles.contains(newFood)) {
                foods.add(newFood);
            }
        }
    }

    /** Spawns obstacles until COUNT_OBS is reached. */
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

    /** Advances the game by one step: moves all snakes and checks for collisions. */
    public void step() {
        if (gameOver.get() || gameWon.get()) {
            return;
        }

        moveSnake(snake, snake.getDirection());

        IntStream.range(0, bots.size()).parallel().forEach(i -> {
            Snake bot = bots.get(i);
            Bot strategy = botStrategies.get(i);
            Direction nextDir = strategy.chooseDirection(bot, this);
            bot.setDirection(nextDir);
        });

        for (int i = 0; i < bots.size(); i++) {
            Snake bot = bots.get(i);
            if (!moveSnake(bot, bot.getDirection())) {
                bots.remove(i);
                botStrategies.remove(i);
                i--;
            }
        }
    }


    /** Moves a snake in the given direction. */
    private boolean moveSnake(Snake currentSnake, Direction direction) {
        Point head = currentSnake.getBody().getFirst();
        Point nextPoint = switch (direction) {
            case UP -> new Point(head.x(), head.y() - 1);
            case DOWN -> new Point(head.x(), head.y() + 1);
            case LEFT -> new Point(head.x() - 1, head.y());
            case RIGHT -> new Point(head.x() + 1, head.y());
        };

        if (nextPoint.x() < 0 || nextPoint.x() >= width || nextPoint.y() < 0
                || nextPoint.y() >= height) {
            if (currentSnake == snake) gameOver.set(true);
            return false;
        }

        if (currentSnake.checkSelfCollision(nextPoint)
                || obstacles.contains(nextPoint)) {
            if (currentSnake == snake) gameOver.set(true);
            return false;
        }

        for (Snake other : bots) {
            if (other != currentSnake && other.getBody().contains(nextPoint)) {
                if (currentSnake == snake) gameOver.set(true);
                return false;
            }
        }

        if (currentSnake != snake && snake.getBody().contains(nextPoint)) {
            return false;
        }

        if (foods.contains(nextPoint)) {
            foods.remove(nextPoint);
            currentSnake.eat();
            spawnFood();
            if (currentSnake == snake) {
                int newScore = score.incrementAndGet();
                if (currentSnake.getBody().size() >= TARGET_LENGTH) {
                    gameWon.set(true);
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
        return score.get();
    }

    public boolean isGameOver() {
        return gameOver.get();
    }

    public boolean isGameWon() {
        return gameWon.get();
    }

    public List<Point> getObstacles() {
        return obstacles;
    }

    public List<Snake> getBots() {
        return bots;
    }
}