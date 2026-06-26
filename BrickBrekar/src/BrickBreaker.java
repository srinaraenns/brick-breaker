import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BrickBreaker extends JFrame implements KeyListener, ActionListener {
    private int paddleX = 300; // Paddle X-coordinate
    private int ballX = 350, ballY = 400; // Ball coordinates
    private int ballXDir = -1, ballYDir = -2; // Ball movement direction
    private boolean play = false; // Game state
    private Timer timer;
    private int delay = 8; // Speed of the ball
    private int[][] bricks;
    private int totalBricks = 21;

    public BrickBreaker() {
        bricks = new int[3][7]; // 3 rows, 7 columns of bricks
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                bricks[i][j] = 1; // 1 means the brick is present
            }
        }

        setTitle("Brick Breaker");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        addKeyListener(this);

        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // Clear screen
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);

        // Draw paddle
        g.setColor(Color.green);
        g.fillRect(paddleX, 550, 100, 10);

        // Draw ball
        g.setColor(Color.yellow);
        g.fillOval(ballX, ballY, 20, 20);

        // Draw bricks
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] == 1) {
                    g.setColor(Color.red);
                    g.fillRect(j * 100 + 50, i * 50 + 50, 80, 30);
                    g.setColor(Color.black);
                    g.drawRect(j * 100 + 50, i * 50 + 50, 80, 30);
                }
            }
        }

        // Check game over
        if (ballY > 570) {
            play = false;
            ballXDir = ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", 280, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Restart", 250, 350);
        }

        // Check win
        if (totalBricks == 0) {
            play = false;
            ballXDir = ballYDir = 0;
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You Win!", 300, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Restart", 250, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            ballX += ballXDir;
            ballY += ballYDir;

            // Ball collision with paddle
            if (ballY + 20 >= 550 && ballX + 10 >= paddleX && ballX + 10 <= paddleX + 100) {
                ballYDir = -ballYDir;
            }

            // Ball collision with walls
            if (ballX < 0 || ballX > 680) ballXDir = -ballXDir;
            if (ballY < 0) ballYDir = -ballYDir;

            // Ball collision with bricks
            outer:
            for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[0].length; j++) {
                    if (bricks[i][j] == 1) {
                        int brickX = j * 100 + 50;
                        int brickY = i * 50 + 50;
                        int brickWidth = 80;
                        int brickHeight = 30;

                        if (ballX + 20 >= brickX && ballX <= brickX + brickWidth &&
                                ballY + 20 >= brickY && ballY <= brickY + brickHeight) {
                            bricks[i][j] = 0;
                            ballYDir = -ballYDir;
                            totalBricks--;
                            break outer;
                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX < 600) {
            paddleX += 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !play) {
            play = true;
            ballX = 350;
            ballY = 400;
            ballXDir = -1;
            ballYDir = -2;
            paddleX = 300;
            totalBricks = 21;

            // Reset bricks
            for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[0].length; j++) {
                    bricks[i][j] = 1;
                }
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new BrickBreaker();
    }
}
