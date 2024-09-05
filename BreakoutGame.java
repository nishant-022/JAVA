import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BreakoutGame extends JFrame implements ActionListener, KeyListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 40;
    private static final int BRICK_HEIGHT = 20;
    private static final int ROWS = 5;
    private static final int COLS = 10;

    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int paddleY = HEIGHT - 50;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballSpeedX = 2;
    private int ballSpeedY = 2;
    private boolean isGameOver = false;

    private boolean[] bricks;
   
    public BreakoutGame() {
        setTitle("Breakout Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bricks = new boolean[ROWS * COLS];

        Timer timer = new Timer(10, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            moveBall();
            checkCollision();
            repaint();
        }
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX <= 0 || ballX + BALL_SIZE >= WIDTH) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        if (ballY + BALL_SIZE >= HEIGHT) {
            isGameOver = true;
        }
    }

    private void checkCollision() {
        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
        Rectangle paddleRect = new Rectangle(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        if (ballRect.intersects(paddleRect)) {
            ballSpeedY = -ballSpeedY;
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int index = i * COLS + j;
                if (bricks[index]) {
                    Rectangle brickRect = new Rectangle(j * BRICK_WIDTH, i * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
                    if (ballRect.intersects(brickRect)) {
                        bricks[index] = false;
                        ballSpeedY = -ballSpeedY;
                    }
                }
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (!isGameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.WHITE);
            g.fillRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

            g.setColor(Color.GREEN);

            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    int index = i * COLS + j;
                    if (bricks[index]) {
                        g.fillRect(j * BRICK_WIDTH, i * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
                    }
                }
            }
        } else {
            g.setColor(Color.RED);
            g.drawString("Game Over", WIDTH / 2 - 50, HEIGHT / 2);
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 10;
        }

        if (key == KeyEvent.VK_RIGHT && paddleX + PADDLE_WIDTH < WIDTH) {
            paddleX += 10;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BreakoutGame game = new BreakoutGame();
            game.setVisible(true);

            for (int i = 0; i < game.bricks.length; i++) {
                game.bricks[i] = true;
            }
        });
    }
}