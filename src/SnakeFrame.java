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

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody = new ArrayList<>();

    // Food
    Tile food;
    Random random;

    // Logic
    int tileSize = 25;
    Timer gameLoop;
    int velocityX = 0;
    int velocityY = 0;
    boolean gameOver = false;
    int highScore = 0;


    SnakeFrame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        // Snake
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        // Food
        food = new Tile(10, 10);
        random = new Random();
        placeFood();
        // Game loop
        gameLoop = new Timer(100, this);
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
        // Cabeça da cobra
        g.setColor(Color.GREEN);
        //g.fillRect(snakeHead.x * tileSize,snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize,snakeHead.y * tileSize, tileSize, tileSize, true);

     
     
        // Corpo da cobra
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);

        }

        //pontos
        g.setFont(new Font("Aarial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over!", tileSize -16, tileSize);}        
      else{
            g.drawString("Points: " + String.valueOf(snakeBody.size()), tileSize -16, tileSize);
        }
    

        // Comida
        g.setColor(Color.RED);
        //g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        
      g.setColor(Color.WHITE);
      g.drawString("High Score: " + String.valueOf(highScore), tileSize + 470, tileSize );




    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean Collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
        
    }

    public void move() {
        // A cobra comeu a comida
        if (Collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            updateHighScore();
            placeFood();
        }
        // Move o corpo
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Tile snakePart = snakeBody.get(i);
            Tile previousPart = snakeBody.get(i - 1);
            snakePart.x = previousPart.x;
            snakePart.y = previousPart.y;
        }
        // Primeiro pedaço do corpo segue a cabeça
        if (snakeBody.size() > 0) {
            Tile firstPart = snakeBody.get(0);
            firstPart.x = snakeHead.x;
            firstPart.y = snakeHead.y;
        }
        // Move a cabeça
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            if (Collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        
        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>=boardWidth ||
          snakeHead.y*tileSize<0 || snakeHead.y*tileSize>=boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver)
            {gameLoop.stop();}
    }

    void gameReset(){
     gameOver = false;
        snakeHead.x = 5;
            snakeHead.y = 5;
            snakeBody.clear();
            velocityX = 0;
            velocityY = 0;
            placeFood();
            gameLoop.start();


    }

    void updateHighScore(){
    if (snakeBody.size() > highScore){
    highScore = snakeBody.size();
    };

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

      if(e.getKeyCode()==KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER && gameOver){
        gameReset();
      }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }















}