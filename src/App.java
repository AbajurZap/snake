
import javax.swing.*;



public class App {
    public static void main(String[] args) throws Exception {
        int boardWindth = 600;
        int boardHeight = boardWindth;


        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWindth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeFrame snakeFrame = new SnakeFrame(boardWindth, boardHeight);
        frame.add(snakeFrame);
        frame.pack();
        snakeFrame.requestFocus();
    }




}
