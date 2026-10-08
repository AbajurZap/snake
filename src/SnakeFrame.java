import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeFrame extends JPanel implements ActionListener, KeyListener {

    private class Tile {

        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    int boardWidth;
    int boardHeight;
    Tile snakeHead;
    ArrayList<Tile> snakeBody = new ArrayList<>();
    Tile food;
    Random random;


    int tileSize = 25;
    Timer gameLoop;
    int velocityX = 0;
    int velocityY = 0;
    boolean gameOver = false;
    int highScore = 0;
    boolean gameWon = false;
    private int currentSpeed = INITIAL_SPEED;
    private static final int INITIAL_SPEED = 100;
    private static final int MIN_SPEED = 50;
    private static final int WIN_SCORE = 100;

    SnakeFrame(int boardWidth, int boardHeight) {

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();
        gameLoop = new Timer(INITIAL_SPEED, this);
        gameLoop.setDelay(currentSpeed);
        loadHighScore();
        gameLoop.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {

    //      Grade
    //      g.setColor(Color.DARK_GRAY);
    //      for (int i = 0; i < boardWidth / tileSize; i++) {
    //      g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
    //      g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
    //   }

        g.setColor(Color.GREEN);
        //g.fillRect(snakeHead.x * tileSize,snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize,snakeHead.y * tileSize, tileSize, tileSize, true);

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));

        if (gameWon) {
             g.setColor(Color.GREEN);
             g.drawString("You Win!", tileSize - 16, tileSize);
              }
            else if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over!", tileSize - 16, tileSize);
             }
           else {
           g.drawString("Points: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
             }

        g.setColor(Color.RED);
        //g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        g.setColor(Color.WHITE);
        g.drawString("High Score: " + String.valueOf(highScore), tileSize + 470, tileSize);

    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean checkCollision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {

        if (checkCollision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            updateHighScore();
            updateGameSpeed();
            placeFood();
            checkWinCondition();
        }

        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Tile snakePart = snakeBody.get(i);
            Tile previousPart = snakeBody.get(i - 1);
            snakePart.x = previousPart.x;
            snakePart.y = previousPart.y;
        }

        if (snakeBody.size() > 0) {
            Tile firstPart = snakeBody.get(0);
            firstPart.x = snakeHead.x;
            firstPart.y = snakeHead.y;
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            if (checkCollision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >= boardWidth ||
          snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >= boardHeight) {
            gameOver = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver) {
            gameLoop.stop();
        }
    }

    void resetGame() {
        gameWon = false;
        gameOver = false;
        snakeHead.x = 5;
        snakeHead.y = 5;
        snakeBody.clear();
        velocityX = 0;
        velocityY = 0;
        currentSpeed = INITIAL_SPEED;
        gameLoop.setDelay(currentSpeed);
        placeFood();
        gameLoop.start();
        repaint();

    }

    void updateHighScore() {
        if (snakeBody.size() > highScore) {
            highScore = snakeBody.size();
            saveHighScore();
        }
    }

     void updateGameSpeed() {
        if (snakeBody.size() > 0 && snakeBody.size() % 5 == 0) {
            currentSpeed = Math.max(currentSpeed - 10, MIN_SPEED);
            gameLoop.setDelay(currentSpeed);
        }
    }

    void checkWinCondition() {
        if (snakeBody.size() >= WIN_SCORE) {
            gameWon = true;
            gameLoop.stop();
        }
    }

    private void loadHighScore() {
    Path path = Paths.get("highscore.txt");

    if (Files.exists(path)) {
        try {
            String score = new String(
                Files.readAllBytes(path),
                StandardCharsets.UTF_8
            ).trim();

            highScore = Integer.parseInt(score);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load high score: " + e.getMessage());
        }
    }
}

private void saveHighScore() {
    Path path = Paths.get("highscore.txt");

    try {
        Files.write(
            path,
            String.valueOf(highScore).getBytes(StandardCharsets.UTF_8)
        );
    } catch (IOException e) {
        System.out.println("Could not save high score: " + e.getMessage());
    }
}

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

        if ((e.getKeyCode() == KeyEvent.VK_SPACE ||
             e.getKeyCode() == KeyEvent.VK_ENTER) && (gameOver || gameWon)) {
            resetGame();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
