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
    int i = 0;

    public final int width = 1280, height = 720;
    int bgwidth=0;
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
    // double flappyV = 0;
    // double flappyA = 7;
    // double flappyI = 0.001;
    // gameset
    boolean gameOver = false;

    public Panel() {
        setFocusable(true);
        addKeyListener(this);

        try {
            imagefb = ImageIO.read(new File("img/bluebird-midflap.png"));
            // imagebg = ImageIO.read(new File("img/background-day.png"));
            imagebg = ImageIO.read(new File("img/hd-a5u9zq0a0ymy2dug.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(imagebg.getTileHeight());
        new Timer(40, this).start();
    }

    @Override // Jpanel
    public void paintComponent(Graphics g) {
        if (!gameOver) {
            // g.drawImage(imagebg, 0, 0, null);
            bgpic(g);
            // birdpic(g);
            // drawpipe(g);
            logic();
        } else {
        }

    }

    public void birdpic(Graphics g) {
        g.drawImage(imagefb, 100, flappyheight + flappyV, null);
    }
    public void bgpic(Graphics g){
        g.drawImage(imagebg, bgwidth  , 0,null);
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
                    gameOver = false;
                }
            }
            if (pipe[i] + pipewidth <= 0) {
                pipe[i] = width;
                gappipe[i] = (int) (Math.random() * (height - 150));
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Action Performing"+ ++i);
        // bird-action
        flappyA += flappyI;
        flappyV += flappyA;
        bgwidth --;
        // pipe/action
        // pipe[0] -= pipevelocity;
        // pipe[1] -= pipevelocity;

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
