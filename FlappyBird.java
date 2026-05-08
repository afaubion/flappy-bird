import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private int birdY = 250;
    private int birdVelocity = 0;
    private int gravity = 1;
    private int jumpStrength = -10;
    private int pipeWidth = 50;
    private int pipeGap = 150;
    private int pipeSpeed = 3;
    private ArrayList<Rectangle> pipes;
    private Random rand;
    private Timer timer;
    private boolean gameOver = false;
    private int score = 0;

    public FlappyBird() {
        pipes = new ArrayList<>();
        rand = new Random();
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setBackground(Color.CYAN);
        addPipe();
    }

    private void addPipe() {
        int pipeHeight = rand.nextInt(300) + 50;
        pipes.add(new Rectangle(400, 0, pipeWidth, pipeHeight));
        pipes.add(new Rectangle(400, pipeHeight + pipeGap, pipeWidth, 600 - pipeHeight - pipeGap));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.YELLOW);
        g.fillRect(50, birdY, 20, 20);

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        if (gameOver) {
            g.drawString("Game Over! Press R to Restart", 150, 250);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdVelocity += gravity;
            birdY += birdVelocity;

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= pipeSpeed;
            }

            if (pipes.get(0).x + pipeWidth < 0) {
                pipes.remove(0);
                pipes.remove(0);
                addPipe();
                score++;
            }

            // Collision detection
            Rectangle birdRect = new Rectangle(50, birdY, 20, 20);
            for (Rectangle pipe : pipes) {
                if (birdRect.intersects(pipe)) {
                    gameOver = true;
                }
            }
            if (birdY < 0 || birdY > 480) {
                gameOver = true;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            birdVelocity = jumpStrength;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    private void restartGame() {
        birdY = 250;
        birdVelocity = 0;
        pipes.clear();
        addPipe();
        score = 0;
        gameOver = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}