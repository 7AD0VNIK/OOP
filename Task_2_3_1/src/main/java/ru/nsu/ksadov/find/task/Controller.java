package ru.nsu.ksadov.find.task;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


/** Handles JavaFX user interactions, game rendering, and the main animation loop. */
public class Controller {
    private static final int CELL_SIZE = 20;
    private static final int GAME_GRID_SIZE = 20;
    private static final long BASE_DELAY = 150_000_000L;
    private static final long SPEED_MULTIPLIER = 2_000_000L;
    private static final long MIN_DELAY = 50_000_000L;
    private static final int TEXT_X_POS = 150;
    private static final int TEXT_Y_POS = 200;

    @FXML private Canvas gameCanvas;
    @FXML private Label label;

    private GameState gameState;
    private long lastUpdate = 0;
    private boolean movedThisFrame = false;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            long delay = BASE_DELAY - (gameState.getScore() * SPEED_MULTIPLIER);
            if (delay < MIN_DELAY) {
                delay = MIN_DELAY;
            }

            if (now - lastUpdate >= delay) {
                gameState.step();
                movedThisFrame = false;
                lastUpdate = now;
            }
            draw();
        }
    };

    /** Initializes the game state and starts the animation loop. */
    @FXML
    public void initialize() {
        gameState = new GameState(GAME_GRID_SIZE, GAME_GRID_SIZE);
        draw();
        timer.start();
    }

    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.setFill(Color.GRAY);
        for (Point p : gameState.getObstacles()) {
            gc.fillRect(p.x() * CELL_SIZE, p.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        gc.setFill(Color.ORANGERED);
        for (Point food : gameState.getFoods()) {
            gc.fillOval(food.x() * CELL_SIZE, food.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        drawSnake(gameState.getSnake(), Color.GOLD);
        for (Snake bot : gameState.getBots()) {
            drawSnake(bot, Color.DARKORCHID);
        }

        label.setText("Score: " + gameState.getScore());

        if (gameState.isGameOver()) {
            gc.setFill(Color.WHITE);
            gc.fillText("GAME OVER", TEXT_X_POS, TEXT_Y_POS);
            timer.stop();
        } else if (gameState.isGameWon()) {
            gc.setFill(Color.WHITE);
            gc.fillText("YOU WON", TEXT_X_POS, TEXT_Y_POS);
            timer.stop();
        }
    }

    /** Drawing snake method. */
    private void drawSnake(Snake s, Color bodyColor) {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        boolean isHead = true;

        for (Point p : s.getBody()) {
            if (isHead) {
                gc.setFill(bodyColor.darker());
                gc.fillRoundRect(p.x() * CELL_SIZE, p.y() * CELL_SIZE, CELL_SIZE - 1,
                        CELL_SIZE - 1, 8, 8);
                gc.setFill(Color.GREEN);
                gc.fillOval(p.x() * CELL_SIZE + 4, p.y() * CELL_SIZE + 4, 4, 4);
                gc.fillOval(p.x() * CELL_SIZE + 12, p.y() * CELL_SIZE + 4, 4, 4);
                isHead = false;
            } else {
                gc.setFill(bodyColor);
                gc.fillRoundRect(p.x() * CELL_SIZE, p.y() * CELL_SIZE, CELL_SIZE - 1,
                        CELL_SIZE - 1, 8, 8);
            }
        }
    }

    /** Resets the game to its initial state. */
    @FXML
    public void handleRestart() {
        gameState = new GameState(GAME_GRID_SIZE, GAME_GRID_SIZE);
        movedThisFrame = false;
        timer.start();
        draw();
        gameCanvas.requestFocus();
    }

    /** Handles keyboard input for controlling the player snake. */
    @FXML
    public void handleButtonsPressed(KeyEvent event) {
        if (movedThisFrame) {
            return;
        }

        Direction currDir = gameState.getSnake().getDirection();
        Direction newDir = currDir;

        switch (event.getCode()) {
            case UP, W -> newDir = Direction.UP;
            case DOWN, S -> newDir = Direction.DOWN;
            case LEFT, A -> newDir = Direction.LEFT;
            case RIGHT, D -> newDir = Direction.RIGHT;
            default -> {}
        }

        if (newDir != currDir) {
            gameState.getSnake().setDirection(newDir);
            movedThisFrame = true;
        }
    }

    
}