import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel implements KeyListener, ActionListener {
    BufferedImage imagebg;
    BufferedImage imagefb;

    final int width = 525, height = 550;
    // pipe
    final int pipevelocity = 5;
    final int pipewidth = 50;
    int[] pipe = { width, width + width / 2 };
    int[] gappipe = { (int) (Math.random() * (height - 150)), (int) (Math.random() * (height - 100)) };
    // bird
    int flappyheight = height / 4;
    int flappyV = 0;
    int flappyA = 7;
    int flappyI = 1;
    // gameset
    boolean gameOver = false;

    public Panel() {
        setFocusable(true);
        addKeyListener(this);

        try {
            imagebg = ImageIO.read(new File("D:/Flappybird/bg.png"));
            imagefb = ImageIO.read(new File("D:/Flappybird/fbb.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Timer(40, this).start();
    }

    public void paintComponent(Graphics g) {
        if (!gameOver) {
            g.drawImage(imagebg, 0, 0, null);
            birdpic(g);
            drawpipe(g);
            logic();
        } else {

        }

    }

    public void birdpic(Graphics g) {

        g.drawImage(imagefb, 100, flappyheight + flappyV, null);
    }

    private void drawpipe(Graphics g) {
        for (int i = 0; i < 2; i++) {
            g.setColor(Color.green);
            g.fillRect(pipe[i], 0, pipewidth, height);

            g.setColor(Color.black);
            g.fillRect(pipe[i], gappipe[i], pipewidth, 100);
        }
    }

    private void logic() {
        for (int i = 0; i < 2; i++) {
            if (pipe[i] <= 100 && pipe[i] + pipewidth >= 100) {
                if ((flappyheight + flappyV) >= 0 && (flappyheight + flappyV) <= gappipe[i]
                        || (flappyheight + flappyV + 25) >= gappipe[i] + 100
                                && (flappyheight + flappyV + 25) <= height) {
                    gameOver = true;
                }
            }
            if (pipe[i] + pipewidth <= 0) {
                pipe[i] = width;
                gappipe[i] = (int) (Math.random() * (height - 150));
            }
        }

    }

    public void actionPerformed(ActionEvent e) {
        // bird-action
        flappyA += flappyI;
        flappyV += flappyA;

        // pipe/action
        pipe[0] -= pipevelocity;
        pipe[1] -= pipevelocity;

        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            flappyA = -7;
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }
}
