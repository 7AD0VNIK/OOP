package ru.nsu.ksadov.find.task;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/** Manages the core game logic, state, entities, and collision detection. */
public class GameState {
    private static final int MAX_FOOD = 3;
    private static final int TARGET_LENGTH = 10;
    private static final int COUNT_OBS = 10;
    private static final long BASE_DELAY = 150_000_000L;
    private static final long SPEED_MULTIPLIER = 2_000_000L;
    private static final long MIN_DELAY = 50_000_000L;

    private final int width;
    private final int height;
    private final Snake snake;
    private final List<Snake> bots = new CopyOnWriteArrayList<>();
    private final List<Bot> botStrategies = new CopyOnWriteArrayList<>();
    private final List<Point> foods = new CopyOnWriteArrayList<>();
    private final List<Point> obstacles = new CopyOnWriteArrayList<>();

    private final Random random = new Random();

    private final IntegerProperty score = new SimpleIntegerProperty(1);
    private final AtomicBoolean gameOver = new AtomicBoolean(false);
    private final AtomicBoolean gameWon = new AtomicBoolean(false);

    public long getCurrDelay() {
        long delay = BASE_DELAY - (score.get() * SPEED_MULTIPLIER);
        return Math.max(delay, MIN_DELAY);
    }

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
        int maxAttempts = width * height;
        int attempts = 0;
        while (foods.size() < MAX_FOOD && attempts < maxAttempts) {
            attempts++;
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point newFood = new Point(x, y);
            boolean occupiedByBot = bots.stream().anyMatch(bot -> bot.occupies(newFood));
            if (!snake.occupies(newFood) && !foods.contains(newFood)
                    && !obstacles.contains(newFood) && !occupiedByBot) {
                foods.add(newFood);
            }
        }
    }

    /** Spawns obstacles until COUNT_OBS is reached. */
    private void spawnObs() {
        int maxAttempts = width * height    ;
        int attempts = 0;
        while (obstacles.size() < COUNT_OBS && attempts < maxAttempts) {
            attempts++;
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Point newObs = new Point(x, y);
            boolean occupiedByBot = bots.stream().anyMatch(bot -> bot.occupies(newObs));
            if (!snake.occupies(newObs) && !obstacles.contains(newObs) && !occupiedByBot) {
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


    /** Moves a snake in the given direction.
     * @return true - if the snake successfully moved, fals - otherwise (collided or die).
     */
    private boolean moveSnake(Snake currentSnake, Direction direction) {
        Point nextPoint = currentSnake.getNextHeadPosition(direction);
        if (nextPoint.x() < 0 || nextPoint.x() >= width || nextPoint.y() < 0
                || nextPoint.y() >= height) {
            if (currentSnake == snake) {
                gameOver.set(true);
            }
            return false;
        }

        if (currentSnake.checkSelfCollision(nextPoint)
                || obstacles.contains(nextPoint)) {
            if (currentSnake == snake) {
                gameOver.set(true);
            }
            return false;
        }

        for (Snake other : bots) {
            if (other != currentSnake && other.occupies(nextPoint)) {
                if (currentSnake == snake) {
                    gameOver.set(true);
                }
                return false;
            }
        }

        if (currentSnake != snake && snake.occupies(nextPoint)) {
            return false;
        }

        if (foods.contains(nextPoint)) {
            foods.remove(nextPoint);
            currentSnake.eat();
            spawnFood();
            if (currentSnake == snake) {
                score.set(score.get() + 1);
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
    public IntegerProperty scoreProperty() {
        return score;
    }
}